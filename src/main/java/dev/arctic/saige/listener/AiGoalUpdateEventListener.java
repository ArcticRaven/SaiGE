package dev.arctic.saige.listener;

import dev.arctic.saige.events.AiGoalUpdateEvent;
import dev.arctic.saige.utilities.AssistantRequest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import static dev.arctic.saige.SaiGE.plugin;

public class AiGoalUpdateEventListener implements Listener {

    @EventHandler
    public void onAiGoalUpdateEvent(AiGoalUpdateEvent event) {
        String goal = event.getGoalCommand();

        plugin.getLogger().info("Goal: " + goal);

        new AssistantRequest().createRetrieveRunAsync("asst_KmCegqPU9Uu3V18cfa7wsLEm", null, goal);
    }
}
