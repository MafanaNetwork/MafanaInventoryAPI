package me.tahacheji.mafana.data;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import de.tr7zw.nbtapi.NBT;
import de.tr7zw.nbtapi.NBTItem;
import me.tahacheji.mafana.MafanaNetworkCommunicator;
import me.tahacheji.mafana.util.InventoryUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class PlayerInventoryDatabase extends MySQL {

    SQLGetter sqlGetter = new SQLGetter(this);

    public PlayerInventoryDatabase() {
        super("localhost", "3306", "51251", "51251", "d5f02bb941");
    }

    public CompletableFuture<Boolean> addPlayerInventory(PlayerInventory playerInventory) {
        return CompletableFuture.supplyAsync(() -> {
            if (!sqlGetter.existsAsync(playerInventory.getUUID()).join()) {
                Gson gson = new Gson();
                return sqlGetter.setStringAsync(new DatabaseValue("NAME", playerInventory.getUUID(), MafanaNetworkCommunicator.getInstance().getPlayerDatabase().getOfflineProxyPlayerAsync(playerInventory.getUUID()).join().getPlayerName()))
                        .thenCompose(__ -> sqlGetter.setStringAsync(new DatabaseValue("INVENTORY", playerInventory.getUUID(), playerInventory.getRawInventory())))
                        .thenCompose(__ -> sqlGetter.setStringAsync(new DatabaseValue("ARMOR", playerInventory.getUUID(), playerInventory.getRawArmor())))
                        .thenCompose(__ -> sqlGetter.setStringAsync(new DatabaseValue("STASH", playerInventory.getUUID(), gson.toJson(playerInventory.getRawStash()))))
                        .thenCompose(__ -> sqlGetter.setStringAsync(new DatabaseValue("USER_VALUES", playerInventory.getUUID(), gson.toJson(playerInventory.getUserValues()))))
                        .thenCompose(__ -> sqlGetter.setStringAsync(new DatabaseValue("LAST_SEEN", playerInventory.getUUID(), playerInventory.getLastSeen())))
                        .thenApply(__ -> true)
                        .join();
            }
            return false;
        });
    }

    public CompletableFuture<Boolean> setPlayerInventory(PlayerInventory playerInventory) {
        Gson gson = new Gson();
        return sqlGetter.setStringAsync(new DatabaseValue("NAME", playerInventory.getUUID(), MafanaNetworkCommunicator.getInstance().getPlayerDatabase().getOfflineProxyPlayerAsync(playerInventory.getUUID()).join().getPlayerName()))
                .thenCompose(__ -> sqlGetter.setStringAsync(new DatabaseValue("INVENTORY", playerInventory.getUUID(), playerInventory.getRawInventory())))
                .thenCompose(__ -> sqlGetter.setStringAsync(new DatabaseValue("ARMOR", playerInventory.getUUID(), playerInventory.getRawArmor())))
                .thenCompose(__ -> sqlGetter.setStringAsync(new DatabaseValue("STASH", playerInventory.getUUID(), gson.toJson(playerInventory.getRawStash()))))
                .thenCompose(__ -> sqlGetter.setStringAsync(new DatabaseValue("USER_VALUES", playerInventory.getUUID(), gson.toJson(playerInventory.getUserValues()))))
                .thenCompose(__ -> sqlGetter.setStringAsync(new DatabaseValue("LAST_SEEN", playerInventory.getUUID(), playerInventory.getLastSeen())))
                .thenApply(__ -> true);
    }

    public void setPlayerInventorySync(Player player, PlayerInventory playerInventory) {
        Gson gson = new Gson();
        sqlGetter.setStringSync(new DatabaseValue("NAME", playerInventory.getUUID(), player.getName()));
        sqlGetter.setStringAsync(new DatabaseValue("INVENTORY", playerInventory.getUUID(), playerInventory.getRawInventory()));
        sqlGetter.setStringAsync(new DatabaseValue("ARMOR", playerInventory.getUUID(), playerInventory.getRawArmor()));
        sqlGetter.setStringAsync(new DatabaseValue("STASH", playerInventory.getUUID(), gson.toJson(playerInventory.getRawStash())));
        sqlGetter.setStringAsync(new DatabaseValue("USER_VALUES", playerInventory.getUUID(), gson.toJson(playerInventory.getUserValues())));
        sqlGetter.setStringAsync(new DatabaseValue("LAST_SEEN", playerInventory.getUUID(), playerInventory.getLastSeen()));

    }

    public CompletableFuture<PlayerInventory> getPlayerInventory(UUID uuid) {
        Gson gson = new Gson();
        return CompletableFuture.supplyAsync(() -> {
            String rawInventory = sqlGetter.getStringAsync(uuid, new DatabaseValue("INVENTORY")).join();
            String rawArmor = sqlGetter.getStringAsync(uuid, new DatabaseValue("ARMOR")).join();
            List<String> rawStash = gson.fromJson(sqlGetter.getStringAsync(uuid, new DatabaseValue("STASH")).join(), new TypeToken<List<String>>() {
            }.getType());
            List<String> user_values = gson.fromJson(sqlGetter.getStringAsync(uuid, new DatabaseValue("USER_VALUES")).join(), new TypeToken<List<String>>() {
            }.getType());
            String last_seen = sqlGetter.getStringAsync(uuid, new DatabaseValue("LAST_SEEN")).join();
            return new PlayerInventory(uuid.toString(), rawInventory, rawArmor, rawStash, user_values, last_seen);
        }).exceptionally(ex -> {
            ex.printStackTrace();
            return null;
        });
    }

    public CompletableFuture<Void> replaceUserValue(UUID uuid, String replace, String newValue) {
        return getUserValues(uuid).thenComposeAsync(values -> {
            List<String> x = new ArrayList<>();
            String r = null;
            if (values != null) {
                x.addAll(values);
            }
            for (String l : x) {
                if (l.equalsIgnoreCase(replace)) {
                    r = l;
                }
            }
            x.remove(r);
            x.add(newValue);
            return setUserValues(uuid, x);
        });
    }

    public CompletableFuture<Void> addUserValue(UUID uuid, String s) {
        return getUserValues(uuid).thenComposeAsync(values -> {
            List<String> x = new ArrayList<>();
            if (values != null) {
                x.addAll(values);
            }
            x.add(s);
            return setUserValues(uuid, x);
        });
    }

    public CompletableFuture<Void> setUserValues(UUID uuid, List<String> s) {
        Gson gson = new Gson();
        return sqlGetter.setStringAsync(new DatabaseValue("USER_VALUES", uuid, gson.toJson(s)));
    }

    public CompletableFuture<List<String>> getUserValues(UUID uuid) {
        return sqlGetter.getStringAsync(uuid, new DatabaseValue("USER_VALUES"))
                .thenApply(x -> {
                    Gson gson = new Gson();
                    List<String> values = gson.fromJson(x, new TypeToken<List<String>>() {
                    }.getType());
                    return values != null ? values : new ArrayList<>();
                });
    }

    public CompletableFuture<Void> removeStashItem(UUID uuid, ItemStack s) {
        return getStash(uuid).thenComposeAsync(values -> {
            List<String> x = new ArrayList<>();
            String itemToRemove = null;
            if (values != null) {
                x.addAll(values);
            }
            for (String d : x) {
                ItemStack q = new InventoryUtil().itemFromBase64(d);
                if (q != null) {
                    if (q.getItemMeta() != null) {
                        if (new NBTItem(q).hasTag("GameItemUUID")) {
                            if (s != null && s.getItemMeta() != null) {
                                if (new NBTItem(s).hasTag("GameItemUUID")) {
                                    if (new NBTItem(q).getString("GameItemUUID").equalsIgnoreCase(new NBTItem(s).getString("GameItemUUID"))) {
                                        itemToRemove = d;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            x.remove(itemToRemove);
            return setStash(uuid, x);
        });
    }

    public CompletableFuture<Void> removeStashItem(UUID uuid, String s) {
        return getStash(uuid).thenComposeAsync(values -> {
            List<String> x = new ArrayList<>();
            String itemToRemove = null;
            if (values != null) {
                x.addAll(values);
            }
            for (String d : x) {
                if (d.equalsIgnoreCase(s)) {
                    itemToRemove = d;
                }
            }
            x.remove(itemToRemove);
            return setStash(uuid, x);
        });
    }

    public CompletableFuture<Boolean> existStashItem(UUID uuid, String s) {
        return getStash(uuid).thenApplyAsync(values -> {
            List<String> x = new ArrayList<>();
            if (values != null) {
                x.addAll(values);
            }
            for (String d : x) {
                if (d.equalsIgnoreCase(s)) {
                    return true;
                }
            }
            return false;
        });
    }

    public CompletableFuture<Void> addStashItem(UUID uuid, ItemStack s) {
        return getStash(uuid).thenComposeAsync(values -> {
            List<String> x = new ArrayList<>();
            if (values != null) {
                x.addAll(values);
            }
            x.add(new InventoryUtil().itemToBase64(s));
            return setStash(uuid, x);
        });
    }

    public CompletableFuture<Void> addStashItem(UUID uuid, String s) {
        return getStash(uuid).thenComposeAsync(values -> {
            List<String> x = new ArrayList<>();
            if (values != null) {
                x.addAll(values);
            }
            x.add(s);
            return setStash(uuid, x);
        });
    }

    public CompletableFuture<Void> setStash(UUID uuid, List<String> s) {
        Gson gson = new Gson();
        return sqlGetter.setStringAsync(new DatabaseValue("STASH", uuid, gson.toJson(s)));
    }

    public CompletableFuture<List<String>> getStash(UUID uuid) {
        return sqlGetter.getStringAsync(uuid, new DatabaseValue("STASH"))
                .thenApply(x -> {
                    Gson gson = new Gson();
                    List<String> values = gson.fromJson(x, new TypeToken<List<String>>() {
                    }.getType());
                    return values != null ? values : new ArrayList<>();
                });
    }


    @Override
    public SQLGetter getSqlGetter() {
        return sqlGetter;
    }

    public void connect() {
        sqlGetter.createTable("players_inventory",
                new DatabaseValue("NAME", ""),
                new DatabaseValue("INVENTORY", ""),
                new DatabaseValue("ARMOR", ""),
                new DatabaseValue("STASH", ""),
                new DatabaseValue("USER_VALUES", ""),
                new DatabaseValue("LAST_SEEN", ""));
    }
}
