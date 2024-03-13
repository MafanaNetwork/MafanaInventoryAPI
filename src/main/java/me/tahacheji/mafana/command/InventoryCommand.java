package me.tahacheji.mafana.command;

import me.tahacheji.mafana.MafanaInventoryAPI;
import me.tahacheji.mafana.commandExecutor.Command;
import me.tahacheji.mafana.commandExecutor.paramter.Param;
import me.tahacheji.mafana.data.OfflineProxyPlayer;
import me.tahacheji.mafana.display.PlayerInventoryDisplay;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.UUID;

public class InventoryCommand {

    @Command(names = "mia inventory", permission = "", playerOnly = true)
    public void openInventory(Player player, @Param(name = "target") OfflineProxyPlayer offlineProxyPlayer) {
        try {
            new PlayerInventoryDisplay().getPlayerInventoryDisplay(UUID.fromString(offlineProxyPlayer.getPlayerUUID()), player).thenAcceptAsync(updatedGui -> Bukkit.getScheduler().runTask(MafanaInventoryAPI.getInstance(), () -> updatedGui.open(player)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Command(names = "mia toggle inventory", permission = "", playerOnly = true)
    public void toggleInventory(Player player) {
        MafanaInventoryAPI.getInstance().getPlayerInventoryDatabase().getUserValues(player.getUniqueId()).thenAcceptAsync(list -> {
           for(String s : list) {
               if(s.equalsIgnoreCase("VIEW_INVENTORY_MESSAGE_TRUE")) {
                   MafanaInventoryAPI.getInstance().getPlayerInventoryDatabase().replaceUserValue(player.getUniqueId(), "VIEW_INVENTORY_MESSAGE_TRUE", "VIEW_INVENTORY_MESSAGE_FALSE").thenRunAsync(() -> {
                       player.sendMessage(ChatColor.YELLOW + "Toggled OFF inventory view messages.");
                   });
                   break;
               } else if(s.equalsIgnoreCase("VIEW_INVENTORY_MESSAGE_FALSE")) {
                   MafanaInventoryAPI.getInstance().getPlayerInventoryDatabase().replaceUserValue(player.getUniqueId(), "VIEW_INVENTORY_MESSAGE_FALSE", "VIEW_INVENTORY_MESSAGE_TRUE").thenRunAsync(() -> {
                       player.sendMessage(ChatColor.YELLOW + "Toggled ON inventory view messages.");
                   });
                   break;
               }
           }
        });
    }

    @Command(names = "mia toggle stash", permission = "", playerOnly = true)
    public void toggleStash(Player player) {
        MafanaInventoryAPI.getInstance().getPlayerInventoryDatabase().getUserValues(player.getUniqueId()).thenAcceptAsync(list -> {
            for(String s : list) {
                if(s.equalsIgnoreCase("VIEW_STASH_MESSAGE_TRUE")) {
                    MafanaInventoryAPI.getInstance().getPlayerInventoryDatabase().replaceUserValue(player.getUniqueId(), "VIEW_STASH_MESSAGE_TRUE", "VIEW_STASH_MESSAGE_FALSE").thenRunAsync(() -> {
                        player.sendMessage(ChatColor.YELLOW + "Toggled OFF inventory view messages.");
                    });
                    break;
                } else if(s.equalsIgnoreCase("VIEW_STASH_MESSAGE_FALSE")) {
                    MafanaInventoryAPI.getInstance().getPlayerInventoryDatabase().replaceUserValue(player.getUniqueId(), "VIEW_STASH_MESSAGE_FALSE", "VIEW_STASH_MESSAGE_TRUE").thenRunAsync(() -> {
                        player.sendMessage(ChatColor.YELLOW + "Toggled ON inventory view messages.");
                    });
                    break;
                }
            }
        });
    }
}
