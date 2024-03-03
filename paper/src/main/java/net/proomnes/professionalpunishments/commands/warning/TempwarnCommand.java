package net.proomnes.professionalpunishments.commands.warning;

import net.proomnes.professionalpunishments.ProfessionalPunishments;
import net.proomnes.professionalpunishments.util.messages.MessageKeys;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TempwarnCommand extends Command implements TabCompleter {

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
            final StringBuilder reason = new StringBuilder();
            for (int i = 2; i < args.length; ++i) reason.append(args[i]).append(" ");

            final int minutes = this.plugin.timeFormatToMinutes(timeFormat);
            if (minutes == 0) {
                sender.sendMessage(this.plugin.getMessageLoader().get(MessageKeys.SYSTEM_INVALID_TIME_FORMAT));
                return true;
            }

            this.plugin.getWarningService().warnPlayer(target, reason.toString(), sender.getName(), minutes);
            sender.sendMessage(this.plugin.getMessageLoader().get(
                    MessageKeys.PUNISHMENT_TEMPWARN_SUCCESSFULLY_WARNED, target, reason.toString()
            ));
        } else {
            sender.sendMessage(this.plugin.getMessageLoader().get(
                    MessageKeys.PUNISHMENT_TEMPWARN_USAGE, this.getName(), this.getDescription()
            ));
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return null;
    }

}
