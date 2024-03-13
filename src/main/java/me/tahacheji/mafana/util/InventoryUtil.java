package me.tahacheji.mafana.util;

import de.tr7zw.nbtapi.NBTItem;
import me.tahacheji.mafana.MafanaInventoryAPI;
import me.tahacheji.mafana.commandExecutor.paramter.Param;
import me.tahacheji.mafana.data.PlayerInventory;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;

public class InventoryUtil {


    public String itemStackArrayToBase64(ItemStack[] items) throws IllegalStateException {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

            dataOutput.writeInt(items.length);

            for (ItemStack item : items) {
                dataOutput.writeObject(item);
            }

            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("Unable to save item stacks.", e);
        }
    }

    public ItemStack[] itemStackArrayFromBase64(String data) throws IOException {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            ItemStack[] items = new ItemStack[dataInput.readInt()];

            for (int i = 0; i < items.length; i++) {
                items[i] = (ItemStack) dataInput.readObject();
            }

            dataInput.close();
            return items;
        } catch (ClassNotFoundException e) {
            throw new IOException("Unable to decode class type.", e);
        }
    }

    public String itemToBase64(ItemStack item) throws IllegalStateException {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

            dataOutput.writeObject(item);

            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("Unable to save item stack.", e);
        }
    }

    public ItemStack itemFromBase64(String data) {
        try {
            byte[] bytes = Base64Coder.decodeLines(data);
            if (bytes == null || bytes.length == 0) {
                return null;
            }

            ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);

            ItemStack item = (ItemStack) dataInput.readObject();

            dataInput.close();
            return item;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }


    public CompletableFuture<ItemStack[]> checkForBlackListedItems(ItemStack[] list) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                List<String> idList = MafanaInventoryAPI.getInstance().getInventoryDatabase().getIgnoreIDList("MAFANATION").join();
                List<String> materialList = MafanaInventoryAPI.getInstance().getInventoryDatabase().getMaterialList("MAFANATION").join();
                List<String> displayNameList = MafanaInventoryAPI.getInstance().getInventoryDatabase().getDisplayNameList("MAFANATION").join();
                List<ItemStack> i = new ArrayList<>();

                for (ItemStack itemStack : list) {
                    if (itemStack != null && itemStack.getItemMeta() != null) {
                        boolean isBlacklisted = false;

                        if (new NBTItem(itemStack).hasTag("ITEM_ID") && idList.contains(new NBTItem(itemStack).getString("ITEM_ID"))) {
                            isBlacklisted = true;
                        }

                        Material material = itemStack.getType();
                        if (materialList.contains(material.name())) {
                            isBlacklisted = true;
                        }

                        if (itemStack.getItemMeta().hasDisplayName()) {
                            String displayName = ChatColor.stripColor(itemStack.getItemMeta().getDisplayName());
                            if (displayNameList.contains(displayName)) {
                                isBlacklisted = true;
                            }
                        }

                        if (!isBlacklisted) {
                            i.add(itemStack);
                        } else {
                            i.add(new ItemStack(Material.AIR));
                        }
                    } else {
                        i.add(new ItemStack(Material.AIR));
                    }
                }

                return i.toArray(new ItemStack[0]);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }).exceptionally(ex -> {
            ex.printStackTrace();
            return null;
        });
    }


    public void autoUpdatePlayersInventory() {
        MafanaInventoryAPI.getInstance().getInventoryDatabase().getAutoUpdates("MAFANATION").thenAcceptAsync(string -> {
            int i = Integer.parseInt(string);
            Bukkit.getScheduler().runTaskTimerAsynchronously(MafanaInventoryAPI.getInstance(), () -> {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    new InventoryUtil().checkForBlackListedItems(player.getInventory().getContents())
                            .thenCombine(new InventoryUtil().checkForBlackListedItems(player.getInventory().getArmorContents()),
                                    (inventoryItems, armorItems) -> {
                                        player.getInventory().setContents(inventoryItems);
                                        player.getInventory().setArmorContents(armorItems);
                                        PlayerInventory x = new PlayerInventory(player.getUniqueId().toString(),
                                                new InventoryUtil().itemStackArrayToBase64(new InventoryUtil().checkForBlackListedItems(inventoryItems).join()),
                                                new InventoryUtil().itemStackArrayToBase64(new InventoryUtil().checkForBlackListedItems(armorItems).join()),
                                                MafanaInventoryAPI.getInstance().getPlayerInventoryDatabase().getStash(player.getUniqueId()).join(),
                                                MafanaInventoryAPI.getInstance().getPlayerInventoryDatabase().getUserValues(player.getUniqueId()).join(),
                                                "" + System.currentTimeMillis());
                                        MafanaInventoryAPI.getInstance().getPlayerInventoryDatabase().setPlayerInventory(x);
                                        return null;
                                    }).exceptionally(ex -> {
                                ex.printStackTrace();
                                return null;
                            });
                }
            }, 20, 20L * i);
        });
    }

    public void updatePlayersInventory() {
        MafanaInventoryAPI.getInstance().getInventoryDatabase().getAutoUpdates("MAFANATION").thenAcceptAsync(string -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                new InventoryUtil().checkForBlackListedItems(player.getInventory().getContents())
                        .thenCombine(new InventoryUtil().checkForBlackListedItems(player.getInventory().getArmorContents()),
                                (inventoryItems, armorItems) -> {
                                    player.getInventory().setContents(inventoryItems);
                                    player.getInventory().setArmorContents(armorItems);
                                    PlayerInventory x = new PlayerInventory(player.getUniqueId().toString(),
                                            new InventoryUtil().itemStackArrayToBase64(new InventoryUtil().checkForBlackListedItems(inventoryItems).join()),
                                            new InventoryUtil().itemStackArrayToBase64(new InventoryUtil().checkForBlackListedItems(armorItems).join()),
                                            MafanaInventoryAPI.getInstance().getPlayerInventoryDatabase().getStash(player.getUniqueId()).join(),
                                            MafanaInventoryAPI.getInstance().getPlayerInventoryDatabase().getUserValues(player.getUniqueId()).join(),
                                            "" + System.currentTimeMillis());
                                    MafanaInventoryAPI.getInstance().getPlayerInventoryDatabase().setPlayerInventory(x);
                                    return null;
                                }).exceptionally(ex -> {
                            ex.printStackTrace();
                            return null;
                        });
            }
        });
    }

    public void savePlayersInventory() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            PlayerInventory x = new PlayerInventory(player.getUniqueId().toString(),
                    new InventoryUtil().itemStackArrayToBase64(player.getInventory().getContents()),
                    new InventoryUtil().itemStackArrayToBase64(player.getInventory().getArmorContents()),
                    MafanaInventoryAPI.getInstance().getPlayerInventoryDatabase().getStash(player.getUniqueId()).join(),
                    MafanaInventoryAPI.getInstance().getPlayerInventoryDatabase().getUserValues(player.getUniqueId()).join(),
                    "" + System.currentTimeMillis());
            MafanaInventoryAPI.getInstance().getPlayerInventoryDatabase().setPlayerInventorySync(player, x);
        }
    }


}
