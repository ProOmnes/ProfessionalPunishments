package net.proomnes.professionalpunishments.commands.mute;

import net.proomnes.professionalpunishments.ProfessionalPunishments;
import net.proomnes.professionalpunishments.util.messages.MessageKeys;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TempmuteCommand extends Command implements TabCompleter {

    private final ProfessionalPunishments plugin;

    public TempmuteCommand(final ProfessionalPunishments professionalPunishments) {
        super(professionalPunishments.getConfig().getString("commands.tempmute.name"));
        this.setDescription(professionalPunishments.getConfig().getString("commands.tempmute.description"));
        this.setAliases(professionalPunishments.getConfig().getStringList("commands.tempmute.aliases"));
        this.setPermission(professionalPunishments.getConfig().getString("commands.tempmute.permission"));

        this.plugin = professionalPunishments;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, String[] args) {
        if (!this.testPermission(sender)) return true;
        if (args.length >= 3) {
            final String target = args[0];
            final String timeFormat = args[1];
            final StringBuilder reason = new StringBuilder();
            for (int i = 3; i < args.length; ++i) reason.append(args[i]).append(" ");

            this.plugin.getMuteService().isMuted(target, is -> {
                if (is) {
                    sender.sendMessage(this.plugin.getMessageLoader().get(MessageKeys.SYSTEM_PLAYER_ALREADY_MUTED, target));
                    return;
                }

                final int minutes = this.plugin.timeFormatToMinutes(timeFormat);
                if (minutes == 0) {
                    sender.sendMessage(this.plugin.getMessageLoader().get(MessageKeys.SYSTEM_INVALID_TIME_FORMAT));
                    return;
                }

                this.plugin.getMuteService().mutePlayer(target, reason.toString(), sender.getName(), minutes);
                sender.sendMessage(this.plugin.getMessageLoader().get(
                        MessageKeys.PUNISHMENT_TEMPMUTE_SUCCESSFULLY_MUTED, target, reason.toString()
                ));
            });
        } else {
            sender.sendMessage(this.plugin.getMessageLoader().get(
                    MessageKeys.PUNISHMENT_TEMPMUTE_USAGE, this.getName(), this.getDescription()
            ));
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return null;
    }

}
