package me.tahacheji.mafana.command;

import me.tahacheji.mafana.MafanaInventoryAPI;
import me.tahacheji.mafana.commandExecutor.Command;
import me.tahacheji.mafana.commandExecutor.bukkit.Material;
import me.tahacheji.mafana.commandExecutor.paramter.Param;
import me.tahacheji.mafana.data.OfflineProxyPlayer;
import me.tahacheji.mafana.util.InventoryUtil;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class ServerInventoryCommand {

    @Command(names = "mia admin setServer", permission = "mafana.admin", playerOnly = true)
    public void setServer(Player player,@Param(name = "server") String server) {
        MafanaInventoryAPI.getInstance().getInventoryDatabase().setServerIgnoreList(server).thenRunAsync(() -> {
            player.sendMessage(ChatColor.GREEN + "Complete.");
        });
    }

    @Command(names = "mia admin update", permission = "mafana.admin", playerOnly = true)
    public void update(Player player) {
        new InventoryUtil().updatePlayersInventory();
        player.sendMessage(ChatColor.GREEN + "Complete.");
    }

    @Command(names = "mia admin setAutoUpdate", permission = "mafana.admin", playerOnly = true)
    public void setAutoUpdate(Player player, @Param(name = "server") String server, @Param(name = "id") String x) {
        MafanaInventoryAPI.getInstance().getInventoryDatabase().setAutoUpdates(server, x).thenRunAsync(() -> {
            player.sendMessage(ChatColor.GREEN + "Complete.");
        });
    }

    @Command(names = "mia admin addIgnoreId", permission = "mafana.admin", playerOnly = true)
    public void addIgnoreId(Player player,@Param(name = "server") String server, @Param(name = "id") String id) {
        MafanaInventoryAPI.getInstance().getInventoryDatabase().addIgnoreID(server, id).thenRunAsync(() -> {
            player.sendMessage(ChatColor.GREEN + "Complete.");
        });
    }

    @Command(names = "mia admin removeIgnoreId", permission = "mafana.admin", playerOnly = true)
    public void removeIgnoreId(Player player,@Param(name = "server") String server, @Param(name = "id") String id) {
        MafanaInventoryAPI.getInstance().getInventoryDatabase().removeIgnoreId(server, id).thenRunAsync(() -> {
            player.sendMessage(ChatColor.GREEN + "Complete.");
        });
    }

    @Command(names = "mia admin addDisplayName", permission = "mafana.admin", playerOnly = true)
    public void addDisplayName(Player player,@Param(name = "server") String server, @Param(name = "id") String id) {
        MafanaInventoryAPI.getInstance().getInventoryDatabase().addDisplayName(server, id).thenRunAsync(() -> {
            player.sendMessage(ChatColor.GREEN + "Complete.");
        });
    }

    @Command(names = "mia admin removeDisplayName", permission = "mafana.admin", playerOnly = true)
    public void removeDisplayName(Player player,@Param(name = "server") String server, @Param(name = "id") String id) {
        MafanaInventoryAPI.getInstance().getInventoryDatabase().removeDisplayName(server, id).thenRunAsync(() -> {
            player.sendMessage(ChatColor.GREEN + "Complete.");
        });
    }

    @Command(names = "mia admin addMaterial", permission = "mafana.admin", playerOnly = true)
    public void addMaterial(Player player, @Param(name = "server") String server, @Param(name = "id") me.tahacheji.mafana.commandExecutor.bukkit.Material id) {
        for(org.bukkit.Material m : id.getMaterials()) {
            MafanaInventoryAPI.getInstance().getInventoryDatabase().addMaterial(server, m.name()).thenRunAsync(() -> {
                player.sendMessage(ChatColor.GREEN + "Complete.");
            });
        }
    }

    @Command(names = "mia admin removeMaterial", permission = "mafana.admin", playerOnly = true)
    public void removeMaterial(Player player,@Param(name = "server") String server, @Param(name = "id") Material id) {
        for(org.bukkit.Material m : id.getMaterials()) {
            MafanaInventoryAPI.getInstance().getInventoryDatabase().removeMaterial(server, m.name()).thenRunAsync(() -> {
                player.sendMessage(ChatColor.GREEN + "Complete.");
            });
        }
    }

    @Command(names = "mia admin list material", permission = "mafana.admin", playerOnly = true)
    public void listMaterial(Player player,@Param(name = "server") String server) {
        MafanaInventoryAPI.getInstance().getInventoryDatabase().getMaterialList(server).thenAcceptAsync(list -> {
            StringBuilder x = new StringBuilder();
            for(String s : list) {
                x.append(s);
            }
            player.sendMessage(ChatColor.YELLOW + x.toString() + ",");
        });
    }

    @Command(names = "mia admin list ignoreID", permission = "mafana.admin", playerOnly = true)
    public void listIgnoreID(Player player,@Param(name = "server") String server) {
        MafanaInventoryAPI.getInstance().getInventoryDatabase().getIgnoreIDList(server).thenAcceptAsync(list -> {
            StringBuilder x = new StringBuilder();
            for(String s : list) {
                x.append(s);
            }
            player.sendMessage(ChatColor.YELLOW + x.toString() + ",");
        });
    }

    @Command(names = "mia admin list displayName", permission = "mafana.admin", playerOnly = true)
    public void listDisplayName(Player player, @Param(name = "server") String server) {
        MafanaInventoryAPI.getInstance().getInventoryDatabase().getDisplayNameList(server).thenAcceptAsync(list -> {
            StringBuilder x = new StringBuilder();
            for(String s : list) {
                x.append(s);
            }
            player.sendMessage(ChatColor.YELLOW + x.toString() + ",");
        });
    }
}
