package me.tahacheji.mafana.event;

import me.tahacheji.mafana.MafanaInventoryAPI;
import me.tahacheji.mafana.data.PlayerInventory;
import me.tahacheji.mafana.util.InventoryUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PlayerJoin implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerInventory x = new PlayerInventory(player.getUniqueId().toString(),
                new InventoryUtil().itemStackArrayToBase64(player.getInventory().getContents()),
                new InventoryUtil().itemStackArrayToBase64(player.getInventory().getArmorContents()),
                new ArrayList<>(), List.of("VIEW_INVENTORY_MESSAGE_TRUE", "VIEW_STASH_MESSAGE_TRUE"), "" + System.currentTimeMillis());
        MafanaInventoryAPI.getInstance().getPlayerInventoryDatabase().addPlayerInventory(x).thenAcceptAsync(exist -> {
            if (!exist) {
                MafanaInventoryAPI.getInstance().getPlayerInventoryDatabase().getPlayerInventory(player.getUniqueId()).thenAcceptAsync(playerInventory -> {
                    try {
                        new InventoryUtil().checkForBlackListedItems(new InventoryUtil().itemStackArrayFromBase64(playerInventory.getRawInventory()))
                                .thenCombine(new InventoryUtil().checkForBlackListedItems(new InventoryUtil().itemStackArrayFromBase64(playerInventory.getRawArmor())),
                                        (inventoryItems, armorItems) -> {
                                            player.getInventory().setContents(inventoryItems);
                                            player.getInventory().setArmorContents(armorItems);
                                            return null;
                                        }).join();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        });
    }

}
