package dev.arctic.saige;

import dev.arctic.saige.character.Character;
import dev.arctic.saige.commands.CommandManager;
import dev.arctic.saige.commands.TabComplete;
import dev.arctic.saige.listener.AiDataUpdateEventListener;
import dev.arctic.saige.listener.AiGoalUpdateEventListener;
import dev.arctic.saige.listener.PlayerChatEventListener;
import dev.arctic.saige.utilities.AssistantRequest;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Objects;

public final class SaiGE extends JavaPlugin {

    public static String API_KEY = null;
    public static SaiGE plugin;
    public static String commonThread;

    @Getter @Setter
    public static Character character;

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;

        if (!new File(getDataFolder().getAbsolutePath(), "config.yml").exists()) {
            saveDefaultConfig();
        }
        this.API_KEY = getConfig().getString("Api Key");

        final PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new PlayerChatEventListener(), this);
        pm.registerEvents(new AiGoalUpdateEventListener(), this);
        pm.registerEvents(new AiDataUpdateEventListener(), this);

        character = Character.getDefaultCharacter();

        CommandManager commandManager = new CommandManager();
        Objects.requireNonNull(this.getCommand("ai")).setExecutor(commandManager);
        Objects.requireNonNull(this.getCommand("ai")).setTabCompleter(new TabComplete());


        new AssistantRequest().createRetrieveRunAsync(null,null, "intialize");

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
