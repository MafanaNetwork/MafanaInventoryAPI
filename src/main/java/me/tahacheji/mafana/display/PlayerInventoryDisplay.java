package me.tahacheji.mafana.display;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import me.tahacheji.mafana.MafanaInventoryAPI;
import me.tahacheji.mafana.MafanaNetworkCommunicator;
import me.tahacheji.mafana.data.OfflineProxyPlayer;
import me.tahacheji.mafana.data.PlayerInventory;
import me.tahacheji.mafana.data.ProxyPlayer;
import me.tahacheji.mafana.util.InventoryUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class PlayerInventoryDisplay {


    public CompletableFuture<Gui> getPlayerInventoryDisplay(UUID uuid, Player open) {
        return CompletableFuture.supplyAsync(() -> {
            OfflineProxyPlayer offlineProxyPlayer = MafanaNetworkCommunicator.getInstance().getPlayerDatabase().getOfflineProxyPlayerAsync(uuid).join();
            PlayerInventory playerInventory = MafanaInventoryAPI.getInstance().getPlayerInventoryDatabase().getPlayerInventory(uuid).join();
            ProxyPlayer proxyPlayer = MafanaNetworkCommunicator.getInstance().getNetworkCommunicatorDatabase().getProxyPlayerAsync(uuid).join();
            if(proxyPlayer != null && !open.hasPermission("mafana.admin")) {
                List<String> s = MafanaInventoryAPI.getInstance().getPlayerInventoryDatabase().getUserValues(uuid).join();
                if(s.contains("VIEW_INVENTORY_MESSAGE_TRUE")) {
                    proxyPlayer.sendMessages(ChatColor.YELLOW + open.getName() + " is viewing your inventory.", ChatColor.GRAY + "/mia toggle inventory to toggle off this message");
                }
            }
            Player player = Bukkit.getPlayer(uuid);
            Gui gui;
            if (!open.hasPermission("mafana.admin")) {
                gui = Gui.gui()
                        .title(Component.text(ChatColor.YELLOW + offlineProxyPlayer.getPlayerName() + "'s Inventory"))
                        .rows(6)
                        .disableAllInteractions()
                        .create();
            } else {
                gui = Gui.gui()
                        .title(Component.text(ChatColor.YELLOW + offlineProxyPlayer.getPlayerName() + "'s Inventory"))
                        .rows(6)
                        .create();
            }
            gui.setItem(0, ItemBuilder.from(Material.PURPLE_STAINED_GLASS_PANE).setName(" ").setLore(" ").asGuiItem(event -> {
                event.setCancelled(true);
            }));
            gui.setItem(5, ItemBuilder.from(Material.PURPLE_STAINED_GLASS_PANE).setName(" ").setLore(" ").asGuiItem(event -> {
                event.setCancelled(true);
            }));
            gui.setItem(6, ItemBuilder.from(Material.PURPLE_STAINED_GLASS_PANE).setName(" ").setLore(" ").asGuiItem(event -> {
                event.setCancelled(true);
            }));
            gui.setItem(7, ItemBuilder.from(Material.PURPLE_STAINED_GLASS_PANE).setName(" ").setLore(" ").asGuiItem(event -> {
                event.setCancelled(true);
            }));
            gui.setItem(8, ItemBuilder.from(Material.PURPLE_STAINED_GLASS_PANE).setName(" ").setLore(" ").asGuiItem(event -> {
                event.setCancelled(true);
            }));
            gui.setItem(9, ItemBuilder.from(Material.PURPLE_STAINED_GLASS_PANE).setName(" ").setLore(" ").asGuiItem(event -> {
                event.setCancelled(true);
            }));
            gui.setItem(10, ItemBuilder.from(Material.PURPLE_STAINED_GLASS_PANE).setName(" ").setLore(" ").asGuiItem(event -> {
                event.setCancelled(true);
            }));
            gui.setItem(11, ItemBuilder.from(Material.PURPLE_STAINED_GLASS_PANE).setName(" ").setLore(" ").asGuiItem(event -> {
                event.setCancelled(true);
            }));
            gui.setItem(12, ItemBuilder.from(Material.PURPLE_STAINED_GLASS_PANE).setName(" ").setLore(" ").asGuiItem(event -> {
                event.setCancelled(true);
            }));
            gui.setItem(13, ItemBuilder.from(Material.PURPLE_STAINED_GLASS_PANE).setName(" ").setLore(" ").asGuiItem(event -> {
                event.setCancelled(true);
            }));
            gui.setItem(14, ItemBuilder.from(Material.PURPLE_STAINED_GLASS_PANE).setName(" ").setLore(" ").asGuiItem(event -> {
                event.setCancelled(true);
            }));
            gui.setItem(15, ItemBuilder.from(Material.PURPLE_STAINED_GLASS_PANE).setName(" ").setLore(" ").asGuiItem(event -> {
                event.setCancelled(true);
            }));
            gui.setItem(16, ItemBuilder.from(Material.PURPLE_STAINED_GLASS_PANE).setName(" ").setLore(" ").asGuiItem(event -> {
                event.setCancelled(true);
            }));
            gui.setItem(17, ItemBuilder.from(Material.PURPLE_STAINED_GLASS_PANE).setName(" ").setLore(" ").asGuiItem(event -> {
                event.setCancelled(true);
            }));

            gui.setItem(8, ItemBuilder.from(Material.BARRIER).setName(ChatColor.RED + "Close").asGuiItem(event -> {
                event.setCancelled(true);
                event.getWhoClicked().closeInventory();
            }));
            gui.setItem(7, ItemBuilder.from(Material.BRUSH).setName(ChatColor.YELLOW + "View Stash").asGuiItem(event -> {
                event.setCancelled(true);
                new PlayerStashDisplay().getPlayerStashDisplay(uuid, open).thenAcceptAsync(updatedGui -> Bukkit.getScheduler().runTask(MafanaInventoryAPI.getInstance(), () -> updatedGui.open(open)));
            }));
            if(open.hasPermission("mafana.admin")) {
                gui.setItem(6, ItemBuilder.from(Material.PAPER).setName(ChatColor.YELLOW + "Save").asGuiItem(event -> {
                    List<ItemStack> inventoryList = new ArrayList<>();
                    List<ItemStack> armorList = new ArrayList<>();
                    armorList.add(gui.getInventory().getItem(4));
                    armorList.add(gui.getInventory().getItem(3));
                    armorList.add(gui.getInventory().getItem(2));
                    armorList.add(gui.getInventory().getItem(1));

                    int slot = 45;
                    for (int i = 0; i < 9; i++) {
                        inventoryList.add(gui.getInventory().getItem(slot));
                        slot++;
                    }

                    slot = 18;
                    for (int i = 9; i < 36; i++) {
                        inventoryList.add(gui.getInventory().getItem(slot));
                        slot++;
                    }

                    ItemStack[] inventoryArray = inventoryList.toArray(new ItemStack[0]);
                    ItemStack[] armorArray = armorList.toArray(new ItemStack[0]);

                    if (player != null) {
                        player.getInventory().setContents(inventoryArray);
                        player.getInventory().setArmorContents(armorArray);
                    } else {
                        PlayerInventory x = playerInventory;
                        x.setRawInventory(new InventoryUtil().itemStackArrayToBase64(inventoryArray));
                        x.setRawArmor(new InventoryUtil().itemStackArrayToBase64(armorArray));
                        MafanaInventoryAPI.getInstance().getPlayerInventoryDatabase().setPlayerInventory(x);
                    }
                    event.setCancelled(true);
                }));
            }

            ItemStack[] inventory;
            ItemStack[] armor;
            try {
                if (player != null) {
                    inventory = player.getInventory().getContents();
                    armor = player.getInventory().getArmorContents();
                } else {
                    inventory = new InventoryUtil().itemStackArrayFromBase64(playerInventory.getRawInventory());
                    armor = new InventoryUtil().itemStackArrayFromBase64(playerInventory.getRawArmor());
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            if (armor[3] != null) {
                gui.setItem(1, new GuiItem(armor[3]));
            } else {
                gui.setItem(1, ItemBuilder.from(Material.AIR).asGuiItem());
            }
            if (armor[2] != null) {
                gui.setItem(2, new GuiItem(armor[2]));
            } else {
                gui.setItem(2, ItemBuilder.from(Material.AIR).asGuiItem());
            }
            if (armor[1] != null) {
                gui.setItem(3, new GuiItem(armor[1]));
            } else {
                gui.setItem(3, ItemBuilder.from(Material.AIR).asGuiItem());
            }
            if (armor[0] != null) {
                gui.setItem(4, new GuiItem(armor[0]));
            } else {
                gui.setItem(4, ItemBuilder.from(Material.AIR).asGuiItem());
            }

            int slot = 18;
            for (int i = 9; i < 36; i++) {
                if (inventory[i] != null) {
                    gui.setItem(slot, new GuiItem(inventory[i]));
                } else {
                    gui.setItem(slot, ItemBuilder.from(Material.AIR).asGuiItem());
                }
                slot++;
            }
            slot = 45;
            for (int i = 0; i < 9; i++) {
                if (inventory[i] != null) {
                    gui.setItem(slot, new GuiItem(inventory[i]));
                } else {
                    gui.setItem(slot, ItemBuilder.from(Material.AIR).asGuiItem());
                }
                slot++;
            }

            return gui;
        }).exceptionally(ex -> {
            ex.printStackTrace();
            return null;
        });
    }


}
