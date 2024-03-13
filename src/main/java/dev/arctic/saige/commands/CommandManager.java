package dev.arctic.saige.commands;

import dev.arctic.saige.utilities.AssistantRequest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static dev.arctic.saige.SaiGE.character;

public class CommandManager implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (command.getName().equalsIgnoreCase("ai")) {
            if (args.length == 0) {
                sender.sendMessage("Please provide a sub-command.");
                return false;
            }

            switch (args[0].toLowerCase()) {
                case "personality":
                    if (sender instanceof Player) {
                        String json = character.getCharacterAsJSON();
                        sender.sendMessage(json);
                    }
                    break;
                case "recycle":
                    new AssistantRequest().createRetrieveRunAsync(null, null, "initialize");
                    sender.sendMessage("AI controller has been reset.");
                    break;
                default:
                    sender.sendMessage("Unknown sub-command.");
            }
            return true;
        }
        return false;
    }
}
