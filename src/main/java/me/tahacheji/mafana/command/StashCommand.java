package me.tahacheji.mafana.command;

import me.tahacheji.mafana.MafanaInventoryAPI;
import me.tahacheji.mafana.commandExecutor.Command;
import me.tahacheji.mafana.commandExecutor.paramter.Param;
import me.tahacheji.mafana.data.OfflineProxyPlayer;
import me.tahacheji.mafana.display.PlayerInventoryDisplay;
import me.tahacheji.mafana.display.PlayerStashDisplay;
import me.tahacheji.mafana.util.InventoryUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class StashCommand {

    @Command(names = "mia stash", permission = "", playerOnly = true)
    public void openStash(Player player, @Param(name = "target") OfflineProxyPlayer offlineProxyPlayer) {
        try {
            new PlayerStashDisplay().getPlayerStashDisplay(UUID.fromString(offlineProxyPlayer.getPlayerUUID()), player).thenAcceptAsync(updatedGui -> Bukkit.getScheduler().runTask(MafanaInventoryAPI.getInstance(), () -> updatedGui.open(player)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Command(names = "mia admin send", permission = "mafana.admin", playerOnly = true)
    public void sendStash(Player player, @Param(name = "target") OfflineProxyPlayer offlineProxyPlayer) {
        ItemStack itemStack = player.getItemInHand();
        if(itemStack != null && itemStack.getItemMeta() != null) {
            UUID uuid = UUID.fromString(offlineProxyPlayer.getPlayerUUID());
            String s = new InventoryUtil().itemToBase64(itemStack);
            MafanaInventoryAPI.getInstance().getPlayerInventoryDatabase().addStashItem(uuid, s).thenRunAsync(() -> {
                player.sendMessage(ChatColor.GREEN + "Complete.");
            });
        }
    }
}
