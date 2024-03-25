package net.proomnes.professionalpunishments.commands.warning;

import net.proomnes.professionalpunishments.ProfessionalPunishments;
import net.proomnes.professionalpunishments.util.messages.MessageKeys;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UnwarnCommand extends BukkitCommand {

    private final ProfessionalPunishments plugin;

    public UnwarnCommand(final ProfessionalPunishments professionalPunishments) {
        super(professionalPunishments.getConfig().getString("commands.unwarn.name"));
        this.setDescription(professionalPunishments.getConfig().getString("commands.unwarn.description"));
        this.setAliases(professionalPunishments.getConfig().getStringList("commands.unwarn.aliases"));
        this.setPermission(professionalPunishments.getConfig().getString("commands.unwarn.permission"));

        this.plugin = professionalPunishments;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String s, @NotNull String[] args) {
        if (!this.testPermission(sender)) return true;
        if (args.length >= 3) {
            final String target = args[0];
            final String warnId = args[1];
            final StringBuilder builder = new StringBuilder();
            for (int i = 2; i < args.length; ++i) builder.append(args[i]).append(" ");
            final String reason = builder.substring(0, builder.length() - 1);

            this.plugin.getWarningService().getWarning(warnId, punishment -> {
                if (punishment == null || !punishment.getTarget().equalsIgnoreCase(target)) {
                    sender.sendMessage(this.plugin.getMessageLoader().get(
                            MessageKeys.PUNISHMENT_UNWARN_INVALID_ID, target, warnId
                    ));
                    return;
                }

                this.plugin.getWarningService().unwarnPlayer(target, warnId, sender.getName(), reason);
                sender.sendMessage(this.plugin.getMessageLoader().get(
                        MessageKeys.PUNISHMENT_UNWARN_SUCCESSFULLY_UNWARNED, target, reason
                ));
            });
        } else {
            sender.sendMessage(this.plugin.getMessageLoader().get(
                    MessageKeys.PUNISHMENT_UNWARN_USAGE, this.getName(), this.getDescription()
            ));
        }
        return false;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        if (sender.hasPermission(this.getPermission())) {
            final List<String> completer = new ArrayList<>();

            if (args.length == 1) {
                this.plugin.getWarningService().cachedWarnings.forEach(punishment -> {
                    if (!completer.contains(punishment.getTarget())) {
                        completer.add(punishment.getTarget());
                    }
                });
            } else if (args.length == 2) {
                this.plugin.getWarningService().cachedWarnings.forEach(punishment -> {
                    if (punishment.getTarget().equalsIgnoreCase(args[0]) && punishment.getExpire() > System.currentTimeMillis()) {
                        completer.add(punishment.getId());
                    }
                });
            }

            return completer;
        }
        return Collections.emptyList();
    }
}
