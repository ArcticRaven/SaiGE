package dev.arctic.saige.listener;

import dev.arctic.saige.events.AiDataUpdateEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import static dev.arctic.saige.SaiGE.plugin;

public class AiDataUpdateEventListener implements Listener {

    @EventHandler
    public void onAiDataUpdateEvent(AiDataUpdateEvent event) {

        plugin.getLogger().info("Data Update Requested: " + event.getDataContent());
    }
}
