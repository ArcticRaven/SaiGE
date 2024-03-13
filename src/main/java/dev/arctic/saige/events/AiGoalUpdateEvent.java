package dev.arctic.saige.events;

import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter
public class AiGoalUpdateEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private final String goalCommand;

    public AiGoalUpdateEvent(String goalCommand) {
        this.goalCommand = goalCommand;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}

