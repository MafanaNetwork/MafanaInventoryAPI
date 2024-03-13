package me.tahacheji.mafana.data;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import me.tahacheji.mafana.MafanaNetworkCommunicator;
import me.tahacheji.mafana.util.EncryptionUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class InventoryDatabase extends MySQL {

    SQLGetter sqlGetter = new SQLGetter(this);

    public InventoryDatabase() {
        super("localhost", "3306", "51251", "51251", "d5f02bb941");
    }

    public CompletableFuture<Boolean> setServerIgnoreList(String server) {
        return CompletableFuture.supplyAsync(() -> {
            UUID uuid = new EncryptionUtil().stringToUUID(server);
            if (!sqlGetter.existsAsync(uuid).join()) {
                return sqlGetter.setStringAsync(new DatabaseValue("SERVER", uuid, server))
                        .thenCompose(__ -> sqlGetter.setStringAsync(new DatabaseValue("IGNORE_ID_LIST", uuid, "[]")))
                        .thenCompose(__ -> sqlGetter.setStringAsync(new DatabaseValue("MATERIAL_LIST", uuid, "[]")))
                        .thenCompose(__ -> sqlGetter.setStringAsync(new DatabaseValue("DISPLAY_NAME_LIST", uuid, "[]")))
                        .thenCompose(__ -> sqlGetter.setStringAsync(new DatabaseValue("AUTO_UPDATES", uuid, "[]")))
                        .thenApply(__ -> true)
                        .join();
            }
            return false;
        });
    }

    public CompletableFuture<Void> removeIgnoreId(String server, String s) {
        return getIgnoreIDList(server).thenComposeAsync(values -> {
            List<String> x = new ArrayList<>();
            String itemToRemove = null;
            if(values != null) {
                x.addAll(values);
            }
            for(String d : x) {
                if(d.equalsIgnoreCase(s)) {
                    itemToRemove = d;
                }
            }
            x.remove(itemToRemove);
            return setIgnoreIdList(server, x);
        });
    }

    public CompletableFuture<Void> addIgnoreID(String server, String id) {
        return getIgnoreIDList(server).thenComposeAsync(values -> {
            List<String> x = new ArrayList<>();
            if(values != null) {
                x.addAll(values);
            }
            x.add(id);
            return setIgnoreIdList(server, x);
        });
    }


    public CompletableFuture<List<String>> getIgnoreIDList(String server) {
        return sqlGetter.getStringAsync(new EncryptionUtil().stringToUUID(server), new DatabaseValue("IGNORE_ID_LIST"))
                .thenApply(x -> {
                    Gson gson = new Gson();
                    List<String> values = gson.fromJson(x, new TypeToken<List<String>>() {
                    }.getType());
                    return values != null ? values : new ArrayList<>();
                });
    }

    public CompletableFuture<Void> setIgnoreIdList(String server, List<String> s) {
        Gson gson = new Gson();
        return sqlGetter.setStringAsync(new DatabaseValue("IGNORE_ID_LIST", new EncryptionUtil().stringToUUID(server), gson.toJson(s)));
    }

    public CompletableFuture<Void> removeMaterial(String server, String s) {
        return getMaterialList(server).thenComposeAsync(values -> {
            List<String> x = new ArrayList<>();
            String itemToRemove = null;
            if(values != null) {
                x.addAll(values);
            }
            for(String d : x) {
                if(d.equalsIgnoreCase(s)) {
                    itemToRemove = d;
                }
            }
            x.remove(itemToRemove);
            return setMaterialList(server, x);
        });
    }

    public CompletableFuture<Void> addMaterial(String server, String id) {
        return getMaterialList(server).thenComposeAsync(values -> {
            List<String> x = new ArrayList<>();
            if(values != null) {
                x.addAll(values);
            }
            x.add(id);
            return setMaterialList(server, x);
        });
    }

    public CompletableFuture<List<String>> getMaterialList(String server) {
        return sqlGetter.getStringAsync(new EncryptionUtil().stringToUUID(server), new DatabaseValue("MATERIAL_LIST"))
                .thenApply(x -> {
                    Gson gson = new Gson();
                    List<String> values = gson.fromJson(x, new TypeToken<List<String>>() {
                    }.getType());
                    return values != null ? values : new ArrayList<>();
                });
    }

    public CompletableFuture<Void> setMaterialList(String server, List<String> s) {
        Gson gson = new Gson();
        return sqlGetter.setStringAsync(new DatabaseValue("MATERIAL_LIST", new EncryptionUtil().stringToUUID(server), gson.toJson(s)));
    }

    public CompletableFuture<Void> removeDisplayName(String server, String s) {
        return getDisplayNameList(server).thenComposeAsync(values -> {
            List<String> x = new ArrayList<>();
            String itemToRemove = null;
            if(values != null) {
                x.addAll(values);
            }
            for(String d : x) {
                if(d.equalsIgnoreCase(s)) {
                    itemToRemove = d;
                }
            }
            x.remove(itemToRemove);
            return setDisplayNameList(server, x);
        });
    }

    public CompletableFuture<Void> addDisplayName(String server, String id) {
        return getDisplayNameList(server).thenComposeAsync(values -> {
            List<String> x = new ArrayList<>();
            if(values != null) {
                x.addAll(values);
            }
            x.add(id);
            return setDisplayNameList(server, x);
        });
    }

    public CompletableFuture<List<String>> getDisplayNameList(String server) {
        return sqlGetter.getStringAsync(new EncryptionUtil().stringToUUID(server), new DatabaseValue("DISPLAY_NAME_LIST"))
                .thenApply(x -> {
                    Gson gson = new Gson();
                    List<String> values = gson.fromJson(x, new TypeToken<List<String>>() {
                    }.getType());
                    return values != null ? values : new ArrayList<>();
                });
    }

    public CompletableFuture<Void> setDisplayNameList(String server, List<String> s) {
        Gson gson = new Gson();
        return sqlGetter.setStringAsync(new DatabaseValue("DISPLAY_NAME_LIST", new EncryptionUtil().stringToUUID(server), gson.toJson(s)));
    }


    public CompletableFuture<String> getAutoUpdates(String server) {
        return sqlGetter.getStringAsync(new EncryptionUtil().stringToUUID(server), new DatabaseValue("AUTO_UPDATES"));
    }

    public CompletableFuture<Void> setAutoUpdates(String server, String s) {
        return sqlGetter.setStringAsync(new DatabaseValue("AUTO_UPDATES", new EncryptionUtil().stringToUUID(server), s));
    }

    @Override
    public SQLGetter getSqlGetter() {
        return sqlGetter;
    }

    public void connect() {
        sqlGetter.createTable("server_inventory_rules",
                new DatabaseValue("SERVER", ""),
                new DatabaseValue("IGNORE_ID_LIST", ""),
                new DatabaseValue("MATERIAL_LIST", ""),
                new DatabaseValue("DISPLAY_NAME_LIST", ""),
                new DatabaseValue("AUTO_UPDATES", ""));
    }

}
