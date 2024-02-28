package net.proomnes.professionalpunishments.commands.mute;

import net.proomnes.professionalpunishments.ProfessionalPunishments;
import net.proomnes.professionalpunishments.util.messages.MessageKeys;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class UnmuteCommand extends Command implements TabCompleter {

    private final ProfessionalPunishments plugin;

    public UnmuteCommand(final ProfessionalPunishments professionalPunishments) {
        super(professionalPunishments.getConfig().getString("commands.unmute.name"));
        this.setDescription(professionalPunishments.getConfig().getString("commands.unmute.description"));
        this.setAliases(professionalPunishments.getConfig().getStringList("commands.unmute.aliases"));
        this.setPermission(professionalPunishments.getConfig().getString("commands.unmute.permission"));

        this.plugin = professionalPunishments;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String s, @NotNull String[] args) {
        if (!this.testPermission(sender)) return true;
        if (args.length >= 2) {
            final String target = args[0];
            final StringBuilder reason = new StringBuilder();
            for (int i = 2; i < args.length; ++i) reason.append(args[i]).append(" ");

            this.plugin.getMuteService().isMuted(target, is -> {
                if (!is) {
                    sender.sendMessage(this.plugin.getMessageLoader().get(
                            MessageKeys.SYSTEM_PLAYER_NOT_MUTED, target
                    ));
                    return;
                }

                this.plugin.getMuteService().unmutePlayer(target, sender.getName(), reason.toString());
                sender.sendMessage(this.plugin.getMessageLoader().get(
                        MessageKeys.PUNISHMENT_UNMUTE_SUCCESSFULLY_UNMUTED, target, reason.toString()
                ));
            });
        } else {
            sender.sendMessage(this.plugin.getMessageLoader().get(
                    MessageKeys.PUNISHMENT_UNMUTE_USAGE, this.getName(), this.getDescription()
            ));
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return null;
    }

}
