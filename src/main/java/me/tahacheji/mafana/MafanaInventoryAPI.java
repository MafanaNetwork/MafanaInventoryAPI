package me.tahacheji.mafana;

import me.tahacheji.mafana.command.InventoryCommand;
import me.tahacheji.mafana.command.ServerInventoryCommand;
import me.tahacheji.mafana.command.StashCommand;
import me.tahacheji.mafana.commandExecutor.CommandHandler;
import me.tahacheji.mafana.data.InventoryDatabase;
import me.tahacheji.mafana.data.PlayerInventoryDatabase;
import me.tahacheji.mafana.event.PlayerJoin;
import me.tahacheji.mafana.event.PlayerLeave;
import me.tahacheji.mafana.util.InventoryUtil;
import org.bukkit.plugin.java.JavaPlugin;

public final class MafanaInventoryAPI extends JavaPlugin {

    private static MafanaInventoryAPI instance;
    private PlayerInventoryDatabase playerInventoryDatabase;
    private InventoryDatabase inventoryDatabase;

    @Override
    public void onEnable() {
        instance = this;
        playerInventoryDatabase = new PlayerInventoryDatabase();
        inventoryDatabase = new InventoryDatabase();
        playerInventoryDatabase.connect();
        inventoryDatabase.connect();
        CommandHandler.registerCommands(InventoryCommand.class, this);
        CommandHandler.registerCommands(StashCommand.class, this);
        CommandHandler.registerCommands(ServerInventoryCommand.class, this);
        getServer().getPluginManager().registerEvents(new PlayerJoin(), this);
        getServer().getPluginManager().registerEvents(new PlayerLeave(), this);
        new InventoryUtil().autoUpdatePlayersInventory();
    }

    @Override
    public void onDisable() {
        new InventoryUtil().savePlayersInventory();
        inventoryDatabase.close();
        playerInventoryDatabase.close();
    }


    public static MafanaInventoryAPI getInstance() {
        return instance;
    }

    public InventoryDatabase getInventoryDatabase() {
        return inventoryDatabase;
    }

    public PlayerInventoryDatabase getPlayerInventoryDatabase() {
        return playerInventoryDatabase;
    }
}
