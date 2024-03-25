package net.proomnes.professionalpunishments.commands.data;

import net.proomnes.professionalpunishments.ProfessionalPunishments;
import net.proomnes.professionalpunishments.objects.Punishment;
import net.proomnes.professionalpunishments.util.messages.MessageKeys;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ClearlogCommand extends BukkitCommand {

    private final ProfessionalPunishments plugin;

    public ClearlogCommand(final ProfessionalPunishments professionalPunishments) {
        super(professionalPunishments.getConfig().getString("commands.clearlog.name"));
        this.setDescription(professionalPunishments.getConfig().getString("commands.clearlog.description"));
        this.setAliases(professionalPunishments.getConfig().getStringList("commands.clearlog.aliases"));
        this.setPermission(professionalPunishments.getConfig().getString("commands.clearlog.permission"));

        this.plugin = professionalPunishments;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String s, @NotNull String[] args) {
        if (!this.testPermission(sender)) return true;
        if (args.length == 2) {
            final String target = args[0];
            try {
                final Punishment.LogType logType = Punishment.LogType.valueOf(args[1].toUpperCase());
                this.plugin.getDataService().clearLogs(target, logType);
                sender.sendMessage(this.plugin.getMessageLoader().get(
                        MessageKeys.PUNISHMENT_CLEARLOG_CLEARED, target, logType.name().split("_")[1].toLowerCase()
                ));
            } catch (final IllegalArgumentException exception) {
                sender.sendMessage(this.plugin.getMessageLoader().get(
                        MessageKeys.PUNISHMENT_CLEARLOG_INVALID_TYPE, args[1]
                ));
            }
        } else {
            sender.sendMessage(this.plugin.getMessageLoader().get(
                    MessageKeys.PUNISHMENT_CLEARLOG_USAGE, this.getName(), this.getDescription()
            ));
        }
        return false;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        if (sender.hasPermission(this.getPermission())) {
            final List<String> completer = new ArrayList<>();

            if (args.length == 1) {
                this.plugin.getServer().getOnlinePlayers().forEach(player -> {
                    completer.add(player.getName());
                });
            }

            return completer;
        }
        return Collections.emptyList();
    }
}
