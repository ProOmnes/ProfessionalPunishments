package net.proomnes.professionalpunishments.commands.warning;

import net.proomnes.professionalpunishments.ProfessionalPunishments;
import net.proomnes.professionalpunishments.util.messages.MessageKeys;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TempwarnCommand extends BukkitCommand {

    private final ProfessionalPunishments plugin;

    public TempwarnCommand(final ProfessionalPunishments professionalPunishments) {
        super(professionalPunishments.getConfig().getString("commands.tempwarn.name"));
        this.setDescription(professionalPunishments.getConfig().getString("commands.tempwarn.description"));
        this.setAliases(professionalPunishments.getConfig().getStringList("commands.tempwarn.aliases"));
        this.setPermission(professionalPunishments.getConfig().getString("commands.tempwarn.permission"));

        this.plugin = professionalPunishments;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, String[] args) {
        if (!this.testPermission(sender)) return true;
        if (args.length >= 3) {
            final String target = args[0];
            final String timeFormat = args[1];
            final StringBuilder builder = new StringBuilder();
            for (int i = 2; i < args.length; ++i) builder.append(args[i]).append(" ");
            final String reason = builder.substring(0, builder.length() - 1);

            final int minutes = this.plugin.timeFormatToMinutes(timeFormat);
            if (minutes == 0) {
                sender.sendMessage(this.plugin.getMessageLoader().get(MessageKeys.SYSTEM_INVALID_TIME_FORMAT));
                return true;
            }

            this.plugin.getWarningService().warnPlayer(target, reason, sender.getName(), minutes);
            sender.sendMessage(this.plugin.getMessageLoader().get(
                    MessageKeys.PUNISHMENT_TEMPWARN_SUCCESSFULLY_WARNED, target, reason
            ));
        } else {
            sender.sendMessage(this.plugin.getMessageLoader().get(
                    MessageKeys.PUNISHMENT_TEMPWARN_USAGE, this.getName(), this.getDescription()
            ));
        }
        return true;
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
