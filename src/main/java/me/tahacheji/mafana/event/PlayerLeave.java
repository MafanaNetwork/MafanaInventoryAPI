package me.tahacheji.mafana.event;

import me.tahacheji.mafana.MafanaInventoryAPI;
import me.tahacheji.mafana.data.PlayerInventory;
import me.tahacheji.mafana.util.InventoryUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public class PlayerLeave implements Listener {

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        CompletableFuture.supplyAsync(() -> {
            PlayerInventory x = new PlayerInventory(player.getUniqueId().toString(),
                    new InventoryUtil().itemStackArrayToBase64(new InventoryUtil().checkForBlackListedItems(player.getInventory().getContents()).join()),
                    new InventoryUtil().itemStackArrayToBase64(new InventoryUtil().checkForBlackListedItems(player.getInventory().getArmorContents()).join()),
                    MafanaInventoryAPI.getInstance().getPlayerInventoryDatabase().getStash(player.getUniqueId()).join(),
                    MafanaInventoryAPI.getInstance().getPlayerInventoryDatabase().getUserValues(player.getUniqueId()).join(),
                    "" + System.currentTimeMillis());
            MafanaInventoryAPI.getInstance().getPlayerInventoryDatabase().setPlayerInventory(x);
            return null;
        });

    }
}
