package net.proomnes.professionalpunishments.commands.ban;

import net.proomnes.professionalpunishments.ProfessionalPunishments;
import net.proomnes.professionalpunishments.util.messages.MessageKeys;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EditbanCommand extends BukkitCommand {

    private final ProfessionalPunishments plugin;

    public EditbanCommand(final ProfessionalPunishments professionalPunishments) {
        super(professionalPunishments.getConfig().getString("commands.editban.name"));
        this.setDescription(professionalPunishments.getConfig().getString("commands.editban.description"));
        this.setAliases(professionalPunishments.getConfig().getStringList("commands.editban.aliases"));
        this.setPermission(professionalPunishments.getConfig().getString("commands.editban.permission"));

        this.plugin = professionalPunishments;
    }

    // TODO: remove Spacing

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
                        final StringBuilder builder = new StringBuilder();
                        for (int i = 2; i < args.length; ++i) builder.append(args[i]).append(" ");
                        final String reason = builder.substring(0, builder.length() - 1);

                        this.plugin.getBanService().getBan(target, punishment -> {
                            this.plugin.getDataService().setPunishmentReason(punishment, reason);
                            sender.sendMessage(this.plugin.getMessageLoader().get(
                                    MessageKeys.PUNISHMENT_EDITBAN_REASON, target, reason, punishment.getId()
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
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        if (sender.hasPermission(this.getPermission())) {
            final List<String> completer = new ArrayList<>();

            if (args.length == 1) {
                this.plugin.getBanService().cachedBans.forEach(punishment -> {
                    completer.add(punishment.getTarget());
                });
            } else if (args.length == 2) {
                completer.add("reason");
                completer.add("duration");
            }

            return completer;
        }
        return Collections.emptyList();
    }
}
