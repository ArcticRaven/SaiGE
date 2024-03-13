package dev.arctic.saige.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class TabComplete implements TabCompleter {

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();

        if (command.getName().equalsIgnoreCase("ai")) {
            if (args.length == 1) { // First argument, suggest subcommands
                completions.add("personality");
                completions.add("recycle");
            } else {
                completions.add(""); // No further completions
            }
        }

        return completions;
    }
}
