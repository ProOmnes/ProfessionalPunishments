package net.proomnes.professionalpunishments.commands.ban;

import net.proomnes.professionalpunishments.ProfessionalPunishments;
import net.proomnes.professionalpunishments.util.messages.MessageKeys;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class UnbanCommand extends Command implements TabCompleter {

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
            final StringBuilder reason = new StringBuilder();
            for (int i = 1; i < args.length; ++i) reason.append(args[i]).append(" ");

            this.plugin.getBanService().isBanned(target, is -> {
                if (!is) {
                    sender.sendMessage(this.plugin.getMessageLoader().get(
                            MessageKeys.SYSTEM_PLAYER_NOT_BANNED, target
                    ));
                    return;
                }

                this.plugin.getBanService().unbanPlayer(target, sender.getName(), reason.toString());
                sender.sendMessage(this.plugin.getMessageLoader().get(
                        MessageKeys.PUNISHMENT_UNBAN_SUCCESSFULLY_UNBANNED, target, reason.toString()
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
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return null;
    }
}
