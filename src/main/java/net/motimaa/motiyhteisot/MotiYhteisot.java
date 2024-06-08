package net.motimaa.motiyhteisot;

import net.milkbowl.vault.economy.Economy;
import net.motimaa.motiyhteisot.commands.CommandManager;
import net.motimaa.motiyhteisot.commands.TabComplete;
import net.motimaa.motiyhteisot.gui.GuiManager;
import net.motimaa.motiyhteisot.listeners.ClickListener;
import net.motimaa.motiyhteisot.listeners.JoinListener;
import net.motimaa.motiyhteisot.listeners.OrganizationChat;
import net.motimaa.motiyhteisot.listeners.WarListener;
import net.motimaa.motiyhteisot.storage.Storage;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class MotiYhteisot extends JavaPlugin {

    private static MotiYhteisot instance;

    public final String prefix = OrganizationChat.translateColors(getConfig().getString("prefix"));

    private Manager manager;

    private Storage storage;

    private GuiManager guiManager;

    private CommandManager commandManager;

    private static Economy economy;

    @Override
    public void onEnable() {

        instance = this;

        saveDefaultConfig();

        manager = new Manager();

        storage.load(false);

        guiManager = new GuiManager();
        commandManager = new CommandManager();

        getCommand("yhteisö").setExecutor(commandManager);
        getCommand("yhteisö").setTabCompleter(new TabComplete());

        Bukkit.getPluginManager().registerEvents(new ClickListener(), this);
        Bukkit.getPluginManager().registerEvents(new JoinListener(), this);
        Bukkit.getPluginManager().registerEvents(new OrganizationChat(), this);
        Bukkit.getPluginManager().registerEvents(new WarListener(), this);

        if (!setupEconomy()) {
            Utils.severe("Ei löydetty vaulttia/eco pluginia, disabloidaan yhteisöt");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new Placeholders().register();
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return true;
    }

    public Manager getManager() {
        return manager;
    }

    public Storage getStorage() {
        return storage;
    }

    public static Economy getEconomy() {
        return economy;
    }

    public GuiManager getGuiManager() {
        return guiManager;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public static MotiYhteisot getInstance() {
        return instance;
    }

    public void setStorage(Storage storage) {
        this.storage = storage;
    }

    public void setManager(Manager manager) {
        this.manager = manager;
    }
}
