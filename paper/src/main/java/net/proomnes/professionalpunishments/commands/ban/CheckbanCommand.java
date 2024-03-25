package net.proomnes.professionalpunishments.commands.ban;

import net.proomnes.professionalpunishments.ProfessionalPunishments;
import net.proomnes.professionalpunishments.util.messages.MessageKeys;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CheckbanCommand extends BukkitCommand {

    private final ProfessionalPunishments plugin;

    public CheckbanCommand(final ProfessionalPunishments professionalPunishments) {
        super(professionalPunishments.getConfig().getString("commands.checkban.name"));
        this.setDescription(professionalPunishments.getConfig().getString("commands.checkban.description"));
        this.setAliases(professionalPunishments.getConfig().getStringList("commands.checkban.aliases"));
        this.setPermission(professionalPunishments.getConfig().getString("commands.checkban.permission"));

        this.plugin = professionalPunishments;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String s, @NotNull String[] args) {
        if (!this.testPermission(sender)) return true;
        if (args.length == 1) {
            final String target = args[0];
            this.plugin.getBanService().isBanned(target, is -> {
                if (!is) {
                    sender.sendMessage(this.plugin.getMessageLoader().get(MessageKeys.SYSTEM_PLAYER_NOT_BANNED, target));
                    return;
                }

                this.plugin.getBanService().getBan(target, punishment -> {
                    sender.sendMessage(this.plugin.getMessageLoader().get(
                            MessageKeys.PUNISHMENT_CHECKBAN_HEADER, target, punishment.getId()
                    ));
                    sender.sendMessage(this.plugin.getMessageLoader().get(
                            MessageKeys.PUNISHMENT_CHECKBAN_CONTENT, punishment.getId(), punishment.getRelatedId(),
                            punishment.getTarget(), punishment.getReason(), punishment.getInitiator(), punishment.getDate(),
                            this.plugin.getRemainingTime(punishment.getExpire())
                    ));
                });
            });
        } else {
            sender.sendMessage(this.plugin.getMessageLoader().get(
                    MessageKeys.PUNISHMENT_CHECKBAN_USAGE, this.getName(), this.getDescription()
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
            }

            return completer;
        }
        return Collections.emptyList();
    }
}
