package net.proomnes.professionalpunishments.commands.mute;

import net.proomnes.professionalpunishments.ProfessionalPunishments;
import net.proomnes.professionalpunishments.util.messages.MessageKeys;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CheckmuteCommand extends Command implements TabCompleter {

    private final ProfessionalPunishments plugin;

    public CheckmuteCommand(final ProfessionalPunishments professionalPunishments) {
        super(professionalPunishments.getConfig().getString("commands.checkmute.name"));
        this.setDescription(professionalPunishments.getConfig().getString("commands.checkmute.description"));
        this.setAliases(professionalPunishments.getConfig().getStringList("commands.checkmute.aliases"));
        this.setPermission(professionalPunishments.getConfig().getString("commands.checkmute.permission"));

        this.plugin = professionalPunishments;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String s, @NotNull String[] args) {
        if (!this.testPermission(sender)) return true;
        if (args.length == 1) {
            final String target = args[0];
            this.plugin.getMuteService().isMuted(target, is -> {
                if (!is) {
                    sender.sendMessage(this.plugin.getMessageLoader().get(MessageKeys.SYSTEM_PLAYER_NOT_MUTED, target));
                    return;
                }

                this.plugin.getMuteService().getMute(target, punishment -> {
                    sender.sendMessage(this.plugin.getMessageLoader().get(
                            MessageKeys.PUNISHMENT_CHECKMUTE_HEADER, target, punishment.getId()
                    ));
                    sender.sendMessage(this.plugin.getMessageLoader().get(
                            MessageKeys.PUNISHMENT_CHECKMUTE_CONTENT, punishment.getId(), punishment.getRelatedId(),
                            punishment.getTarget(), punishment.getReason(), punishment.getInitiator(), punishment.getDate(),
                            this.plugin.getRemainingTime(punishment.getExpire())
                    ));
                });
            });
        } else {
            sender.sendMessage(this.plugin.getMessageLoader().get(
                    MessageKeys.PUNISHMENT_CHECKMUTE_USAGE, this.getName(), this.getDescription()
            ));
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return null;
    }
}
