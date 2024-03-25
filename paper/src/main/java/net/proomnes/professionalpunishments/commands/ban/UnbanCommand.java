package net.proomnes.professionalpunishments.commands.ban;

import net.proomnes.professionalpunishments.ProfessionalPunishments;
import net.proomnes.professionalpunishments.util.messages.MessageKeys;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UnbanCommand extends BukkitCommand {

    private final ProfessionalPunishments plugin;

    public UnbanCommand(final ProfessionalPunishments professionalPunishments) {
        super(professionalPunishments.getConfig().getString("commands.unban.name"));
        this.setDescription(professionalPunishments.getConfig().getString("commands.unban.description"));
        this.setAliases(professionalPunishments.getConfig().getStringList("commands.unban.aliases"));
        this.setPermission(professionalPunishments.getConfig().getString("commands.unban.permission"));

        this.plugin = professionalPunishments;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String s, @NotNull String[] args) {
        if (!this.testPermission(sender)) return true;
        if (args.length >= 2) {
            final String target = args[0];
            final StringBuilder builder = new StringBuilder();
            for (int i = 1; i < args.length; ++i) builder.append(args[i]).append(" ");
            final String reason = builder.substring(0, builder.length() - 1);

            this.plugin.getBanService().isBanned(target, is -> {
                if (!is) {
                    sender.sendMessage(this.plugin.getMessageLoader().get(
                            MessageKeys.SYSTEM_PLAYER_NOT_BANNED, target
                    ));
                    return;
                }

                this.plugin.getBanService().unbanPlayer(target, sender.getName(), reason);
                sender.sendMessage(this.plugin.getMessageLoader().get(
                        MessageKeys.PUNISHMENT_UNBAN_SUCCESSFULLY_UNBANNED, target, reason
                ));
            });
        } else {
            sender.sendMessage(this.plugin.getMessageLoader().get(
                    MessageKeys.PUNISHMENT_UNBAN_USAGE, this.getName(), this.getDescription()
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
