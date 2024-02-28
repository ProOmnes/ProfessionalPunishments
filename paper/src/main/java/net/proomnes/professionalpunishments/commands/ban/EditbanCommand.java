package net.proomnes.professionalpunishments.commands.ban;

import net.proomnes.professionalpunishments.ProfessionalPunishments;
import net.proomnes.professionalpunishments.util.messages.MessageKeys;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EditbanCommand extends Command implements TabCompleter {

    private final ProfessionalPunishments plugin;

    public EditbanCommand(final ProfessionalPunishments professionalPunishments) {
        super(professionalPunishments.getConfig().getString("commands.editban.name"));
        this.setDescription(professionalPunishments.getConfig().getString("commands.editban.description"));
        this.setAliases(professionalPunishments.getConfig().getStringList("commands.editban.aliases"));
        this.setPermission(professionalPunishments.getConfig().getString("commands.editban.permission"));

        this.plugin = professionalPunishments;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String s, @NotNull String[] args) {
        if (!this.testPermission(sender)) return true;
        if (args.length >= 3) {
            final String target = args[0];
            this.plugin.getBanService().isBanned(target, is -> {
                if (!is) {
                    sender.sendMessage(this.plugin.getMessageLoader().get(MessageKeys.SYSTEM_PLAYER_NOT_BANNED, target));
                    return;
                }

                switch (args[1]) {
                    case "reason":
                        final StringBuilder reason = new StringBuilder();
                        for (int i = 3; i < args.length; ++i) reason.append(args[i]).append(" ");

                        this.plugin.getBanService().getBan(target, punishment -> {
                            this.plugin.getDataService().setPunishmentReason(punishment, reason.toString());
                            sender.sendMessage(this.plugin.getMessageLoader().get(
                                    MessageKeys.PUNISHMENT_EDITBAN_REASON, target, reason.toString(), punishment.getId()
                            ));
                        });
                        break;
                    case "duration":
                        final int minutes = this.plugin.timeFormatToMinutes(args[2]);
                        if (minutes == 0) {
                            sender.sendMessage(this.plugin.getMessageLoader().get(MessageKeys.SYSTEM_INVALID_TIME_FORMAT));
                            return;
                        }

                        this.plugin.getBanService().getBan(target, punishment -> {
                            this.plugin.getDataService().setPunishmentEnding(punishment, minutes);
                            sender.sendMessage(this.plugin.getMessageLoader().get(
                                    MessageKeys.PUNISHMENT_EDITBAN_DURATION, target, args[2], punishment.getId()
                            ));
                        });
                        break;
                    default:
                        sender.sendMessage(this.plugin.getMessageLoader().get(
                                MessageKeys.PUNISHMENT_EDITBAN_USAGE, this.getName(), this.getDescription()
                        ));
                        break;
                }
            });
        } else {
            sender.sendMessage(this.plugin.getMessageLoader().get(
                    MessageKeys.PUNISHMENT_EDITBAN_USAGE, this.getName(), this.getDescription()
            ));
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return null;
    }
}
