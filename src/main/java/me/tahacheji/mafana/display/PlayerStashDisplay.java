package me.tahacheji.mafana.display;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import me.tahacheji.mafana.MafanaInventoryAPI;
import me.tahacheji.mafana.MafanaNetworkCommunicator;
import me.tahacheji.mafana.data.ProxyPlayer;
import me.tahacheji.mafana.util.InventoryUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class PlayerStashDisplay {


    public CompletableFuture<PaginatedGui> getPlayerStashDisplay(UUID uuid, Player open) {
        return CompletableFuture.supplyAsync(() -> {
            PaginatedGui gui = Gui.paginated()
                    .title(Component.text(ChatColor.YELLOW + MafanaNetworkCommunicator.getInstance().getPlayerDatabase().getOfflineProxyPlayerAsync(uuid).join().getPlayerName() + "'s Stash"))
                    .rows(6)
                    .pageSize(28)
                    .disableAllInteractions()
                    .create();

            ProxyPlayer proxyPlayer = MafanaNetworkCommunicator.getInstance().getNetworkCommunicatorDatabase().getProxyPlayerAsync(uuid).join();
            if(proxyPlayer != null && !open.hasPermission("mafana.admin")) {
                List<String> s = MafanaInventoryAPI.getInstance().getPlayerInventoryDatabase().getUserValues(uuid).join();
                if(s.contains("VIEW_STASH_MESSAGE_TRUE")) {
                    proxyPlayer.sendMessages(ChatColor.YELLOW + open.getName() + " is viewing your stash.", ChatColor.GRAY + "/mia toggle stash to toggle off this message");
                }
            }

            gui.setItem(0, ItemBuilder.from(Material.LIGHT_BLUE_STAINED_GLASS_PANE).setName(" ").setLore(" ").asGuiItem());
            gui.setItem(1, ItemBuilder.from(Material.LIGHT_BLUE_STAINED_GLASS_PANE).setName(" ").setLore(" ").asGuiItem());
            gui.setItem(2, ItemBuilder.from(Material.LIGHT_BLUE_STAINED_GLASS_PANE).setName(" ").setLore(" ").asGuiItem());
            gui.setItem(3, ItemBuilder.from(Material.LIGHT_BLUE_STAINED_GLASS_PANE).setName(" ").setLore(" ").asGuiItem());
            gui.setItem(4, ItemBuilder.from(Material.LIGHT_BLUE_STAINED_GLASS_PANE).setName(" ").setLore(" ").asGuiItem());
            gui.setItem(5, ItemBuilder.from(Material.LIGHT_BLUE_STAINED_GLASS_PANE).setName(" ").setLore(" ").asGuiItem());
            gui.setItem(6, ItemBuilder.from(Material.LIGHT_BLUE_STAINED_GLASS_PANE).setName(" ").setLore(" ").asGuiItem());
            gui.setItem(7, ItemBuilder.from(Material.LIGHT_BLUE_STAINED_GLASS_PANE).setName(" ").setLore(" ").asGuiItem());
            gui.setItem(8, ItemBuilder.from(Material.LIGHT_BLUE_STAINED_GLASS_PANE).setName(" ").setLore(" ").asGuiItem());
            gui.setItem(17, ItemBuilder.from(Material.LIGHT_BLUE_STAINED_GLASS_PANE).setName(" ").setLore(" ").asGuiItem());
            gui.setItem(26, ItemBuilder.from(Material.LIGHT_BLUE_STAINED_GLASS_PANE).setName(" ").setLore(" ").asGuiItem());
            gui.setItem(35, ItemBuilder.from(Material.LIGHT_BLUE_STAINED_GLASS_PANE).setName(" ").setLore(" ").asGuiItem());
            gui.setItem(45, ItemBuilder.from(Material.LIGHT_BLUE_STAINED_GLASS_PANE).setName(" ").setLore(" ").asGuiItem());
            gui.setItem(53, ItemBuilder.from(Material.LIGHT_BLUE_STAINED_GLASS_PANE).setName(" ").setLore(" ").asGuiItem());
            gui.setItem(52, ItemBuilder.from(Material.LIGHT_BLUE_STAINED_GLASS_PANE).setName(" ").setLore(" ").asGuiItem());
            gui.setItem(51, ItemBuilder.from(Material.LIGHT_BLUE_STAINED_GLASS_PANE).setName(" ").setLore(" ").asGuiItem());
            gui.setItem(50, ItemBuilder.from(Material.LIGHT_BLUE_STAINED_GLASS_PANE).setName(" ").setLore(" ").asGuiItem());
            gui.setItem(48, ItemBuilder.from(Material.LIGHT_BLUE_STAINED_GLASS_PANE).setName(" ").setLore(" ").asGuiItem());
            gui.setItem(47, ItemBuilder.from(Material.LIGHT_BLUE_STAINED_GLASS_PANE).setName(" ").setLore(" ").asGuiItem());
            gui.setItem(46, ItemBuilder.from(Material.LIGHT_BLUE_STAINED_GLASS_PANE).setName(" ").setLore(" ").asGuiItem());
            gui.setItem(44, ItemBuilder.from(Material.LIGHT_BLUE_STAINED_GLASS_PANE).setName(" ").setLore(" ").asGuiItem());
            gui.setItem(36, ItemBuilder.from(Material.LIGHT_BLUE_STAINED_GLASS_PANE).setName(" ").setLore(" ").asGuiItem());
            gui.setItem(27, ItemBuilder.from(Material.LIGHT_BLUE_STAINED_GLASS_PANE).setName(" ").setLore(" ").asGuiItem());
            gui.setItem(18, ItemBuilder.from(Material.LIGHT_BLUE_STAINED_GLASS_PANE).setName(" ").setLore(" ").asGuiItem());
            gui.setItem(9, ItemBuilder.from(Material.LIGHT_BLUE_STAINED_GLASS_PANE).setName(" ").setLore(" ").asGuiItem());
            gui.setItem(6, 3, ItemBuilder.from(Material.PAPER).setName(ChatColor.DARK_GRAY + "Previous")
                    .asGuiItem(event -> gui.previous()));
            gui.setItem(6, 7, ItemBuilder.from(Material.PAPER).setName(ChatColor.DARK_GRAY + "Next")
                    .asGuiItem(event -> gui.next()));
            gui.setItem(49, ItemBuilder.from(Material.BARRIER).setName(ChatColor.RED + "Back").asGuiItem(event -> {
                new PlayerInventoryDisplay().getPlayerInventoryDisplay(uuid, open).thenAcceptAsync(updatedGui -> Bukkit.getScheduler().runTask(MafanaInventoryAPI.getInstance(), () -> updatedGui.open(open)));
            }));
            List<String> x = MafanaInventoryAPI.getInstance().getPlayerInventoryDatabase().getStash(uuid).join();
            if(uuid.equals(open.getUniqueId()) || open.hasPermission("mafana.admin")) {
                gui.setItem(50, ItemBuilder.from(Material.GOLD_BLOCK).setName(ChatColor.GOLD + "Claim All").asGuiItem(event -> {
                    for (String s : x) {
                        ItemStack itemStack = new InventoryUtil().itemFromBase64(s);
                        if (itemStack != null && itemStack.getItemMeta() != null) {
                            open.getInventory().addItem(itemStack);
                        }
                    }
                    MafanaInventoryAPI.getInstance().getPlayerInventoryDatabase().setStash(uuid, new ArrayList<>());
                    open.closeInventory();
                }));
            }
            for(String s : x) {
                ItemStack itemStack = new InventoryUtil().itemFromBase64(s);
                if(itemStack != null && itemStack.getItemMeta() != null) {
                    gui.addItem(new GuiItem(itemStack, event -> {
                        if(uuid.equals(open.getUniqueId()) || open.hasPermission("mafana.admin")) {
                            if(MafanaInventoryAPI.getInstance().getPlayerInventoryDatabase().existStashItem(uuid, s).join()) {
                                open.getInventory().addItem(itemStack);
                                MafanaInventoryAPI.getInstance().getPlayerInventoryDatabase().removeStashItem(uuid, s);
                            } else {
                                open.sendMessage(ChatColor.RED + "This item does not exist");
                            }
                        }
                    }));
                }
            }
            return gui;
        });
    }
}
