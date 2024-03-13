package dev.arctic.saige.events;

import dev.arctic.saige.utilities.PersonalityRequest;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter
public class AiDataUpdateEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private final String dataContent;
    public String response;

    public AiDataUpdateEvent(String dataContent) {
        this.dataContent = dataContent;
        new PersonalityRequest().createRetrieveRunAsync(dataContent);
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
