package net.proomnes.professionalpunishments.commands.ban;

import net.proomnes.professionalpunishments.ProfessionalPunishments;
import net.proomnes.professionalpunishments.objects.Punishment;
import net.proomnes.professionalpunishments.util.messages.MessageKeys;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BanlogCommand extends BukkitCommand {

    private final ProfessionalPunishments plugin;

    public BanlogCommand(final ProfessionalPunishments professionalPunishments) {
        super(professionalPunishments.getConfig().getString("commands.banlog.name"));
        this.setDescription(professionalPunishments.getConfig().getString("commands.banlog.description"));
        this.setAliases(professionalPunishments.getConfig().getStringList("commands.banlog.aliases"));
        this.setPermission(professionalPunishments.getConfig().getString("commands.banlog.permission"));

        this.plugin = professionalPunishments;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String s, @NotNull String[] args) {
        if (!this.testPermission(sender)) return true;
        if (args.length == 1) {
            final String target = args[0];
            this.plugin.getDataService().getLogs(target, Punishment.LogType.LOG_BAN, logs -> {
                if (logs.isEmpty()) {
                    sender.sendMessage(this.plugin.getMessageLoader().get(
                            MessageKeys.PUNISHMENT_BANLOG_EMPTY, target
                    ));
                    return;
                }

                sender.sendMessage(this.plugin.getMessageLoader().get(
                        MessageKeys.PUNISHMENT_BANLOG_HEADER, logs.size(), target
                ));
                logs.forEach(log -> {
                    sender.sendMessage(this.plugin.getMessageLoader().get(
                            MessageKeys.PUNISHMENT_BANLOG_ENTRY, log.getId(), log.getRelatedId(), log.getTarget(),
                            log.getReason(), log.getInitiator(), log.getDate()
                    ));
                    sender.sendMessage(this.plugin .getMessageLoader().get(
                            MessageKeys.PUNISHMENT_BANLOG_ACTIONS, log.getRelatedId(), log.getTarget()
                    ));
                });
            });
        } else {
            sender.sendMessage(this.plugin.getMessageLoader().get(
                    MessageKeys.PUNISHMENT_BANLOG_USAGE, this.getName(), this.getDescription()
            ));
        }
        return false;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        if (sender.hasPermission(this.getPermission())) {
            final List<String> completer = new ArrayList<>();

            if (args.length == 1) {
                this.plugin.getServer().getOnlinePlayers().forEach(player -> {
                    completer.add(player.getName());
                });
            }

            return completer;
        }
        return Collections.emptyList();
    }
}
