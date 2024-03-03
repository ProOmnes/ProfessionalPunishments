package net.proomnes.professionalpunishments.commands.warning;

import net.proomnes.professionalpunishments.ProfessionalPunishments;
import net.proomnes.professionalpunishments.util.messages.MessageKeys;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EditwarningCommand extends Command implements TabCompleter {

    private final ProfessionalPunishments plugin;

    public EditwarningCommand(final ProfessionalPunishments professionalPunishments) {
        super(professionalPunishments.getConfig().getString("commands.editwarning.name"));
        this.setDescription(professionalPunishments.getConfig().getString("commands.editwarning.description"));
        this.setAliases(professionalPunishments.getConfig().getStringList("commands.editwarning.aliases"));
        this.setPermission(professionalPunishments.getConfig().getString("commands.editwarning.permission"));

        this.plugin = professionalPunishments;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String s, @NotNull String[] args) {
        if (!this.testPermission(sender)) return true;
        if (args.length >= 3) {
            final String warnId = args[0];
            this.plugin.getWarningService().getWarning(warnId, punishment -> {
                if (punishment == null) {
                    sender.sendMessage(this.plugin.getMessageLoader().get(MessageKeys.PUNISHMENT_EDITWARN_INVALID_ID, warnId));
                    return;
                }

                switch (args[1]) {
                    case "reason":
                        final StringBuilder reason = new StringBuilder();
                        for (int i = 2; i < args.length; ++i) reason.append(args[i]).append(" ");

                        this.plugin.getDataService().setPunishmentReason(punishment, reason.toString());
                        sender.sendMessage(this.plugin.getMessageLoader().get(
                                MessageKeys.PUNISHMENT_EDITWARN_REASON, punishment.getTarget(), reason.toString(), punishment.getId()
                        ));
                        break;
                    case "duration":
                        final int minutes = this.plugin.timeFormatToMinutes(args[2]);
                        if (minutes == 0) {
                            sender.sendMessage(this.plugin.getMessageLoader().get(MessageKeys.SYSTEM_INVALID_TIME_FORMAT));
                            return;
                        }

                        this.plugin.getDataService().setPunishmentEnding(punishment, minutes);
                        sender.sendMessage(this.plugin.getMessageLoader().get(
                                MessageKeys.PUNISHMENT_EDITWARN_DURATION, punishment.getTarget(), args[2], punishment.getId()
                        ));
                        break;
                    default:
                        sender.sendMessage(this.plugin.getMessageLoader().get(
                                MessageKeys.PUNISHMENT_EDITWARN_USAGE, this.getName(), this.getDescription()
                        ));
                        break;
                }
            });
        } else {
            sender.sendMessage(this.plugin.getMessageLoader().get(
                    MessageKeys.PUNISHMENT_EDITWARN_USAGE, this.getName(), this.getDescription()
            ));
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return null;
    }

}
