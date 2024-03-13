package dev.arctic.saige.listener;

import dev.arctic.saige.SaiGE;
import dev.arctic.saige.utilities.AssistantRequest;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerChatEventListener implements Listener {

    @EventHandler
    public void onPlayerChat(AsyncChatEvent event) {
        String message = PlainTextComponentSerializer.plainText().serialize(event.message());
        String playerName = event.getPlayer().getName();
        new AssistantRequest().createRetrieveRunAsync(null,SaiGE.commonThread,playerName + " : " + message);
    }
}
