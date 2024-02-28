package net.proomnes.professionalpunishments.commands.mute;

import net.proomnes.professionalpunishments.ProfessionalPunishments;
import net.proomnes.professionalpunishments.objects.Punishment;
import net.proomnes.professionalpunishments.util.messages.MessageKeys;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MutelogCommand extends Command implements TabCompleter {

    private final ProfessionalPunishments plugin;

    public MutelogCommand(final ProfessionalPunishments professionalPunishments) {
        super(professionalPunishments.getConfig().getString("commands.mutelog.name"));
        this.setDescription(professionalPunishments.getConfig().getString("commands.mutelog.description"));
        this.setAliases(professionalPunishments.getConfig().getStringList("commands.mutelog.aliases"));
        this.setPermission(professionalPunishments.getConfig().getString("commands.mutelog.permission"));

        this.plugin = professionalPunishments;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String s, @NotNull String[] args) {
        if (!this.testPermission(sender)) return true;
        if (args.length == 1) {
            final String target = args[0];
            this.plugin.getDataService().getLogs(target, Punishment.LogType.LOG_MUTE, logs -> {
                if (logs.isEmpty()) {
                    sender.sendMessage(this.plugin.getMessageLoader().get(
                            MessageKeys.PUNISHMENT_MUTELOG_EMPTY, target
                    ));
                    return;
                }

                sender.sendMessage(this.plugin.getMessageLoader().get(
                        MessageKeys.PUNISHMENT_MUTELOG_HEADER, logs.size(), target
                ));
                logs.forEach(log -> {
                    sender.sendMessage(this.plugin.getMessageLoader().get(
                            MessageKeys.PUNISHMENT_MUTELOG_ENTRY, log.getId(), log.getRelatedId(), log.getTarget(),
                            log.getReason(), log.getInitiator(), log.getDate()
                    ));
                    sender.sendMessage(this.plugin .getMessageLoader().get(
                            MessageKeys.PUNISHMENT_MUTELOG_ACTIONS, log.getRelatedId(), log.getTarget()
                    ));
                });
            });
        } else {
            sender.sendMessage(this.plugin.getMessageLoader().get(
                    MessageKeys.PUNISHMENT_MUTELOG_USAGE, this.getName(), this.getDescription()
            ));
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return null;
    }
}
