package net.proomnes.professionalpunishments.commands.warning;

import net.proomnes.professionalpunishments.ProfessionalPunishments;
import net.proomnes.professionalpunishments.util.messages.MessageKeys;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class WarningsCommand extends Command implements TabCompleter {

    private final ProfessionalPunishments plugin;

    public WarningsCommand(final ProfessionalPunishments professionalPunishments) {
        super(professionalPunishments.getConfig().getString("commands.warnings.name"));
        this.setDescription(professionalPunishments.getConfig().getString("commands.warnings.description"));
        this.setAliases(professionalPunishments.getConfig().getStringList("commands.warnings.aliases"));
        this.setPermission(professionalPunishments.getConfig().getString("commands.warnings.permission"));

        this.plugin = professionalPunishments;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String s, @NotNull String[] args) {
        if (!this.testPermission(sender)) return true;
        if (args.length == 1) {
            final String target = args[0];
            this.plugin.getWarningService().getActiveWarnings(target, warnings -> {
                if (warnings.isEmpty()) {
                    sender.sendMessage(this.plugin.getMessageLoader().get(
                            MessageKeys.PUNISHMENT_WARNINGS_EMPTY, target
                    ));
                    return;
                }

                sender.sendMessage(this.plugin.getMessageLoader().get(
                        MessageKeys.PUNISHMENT_WARNINGS_HEADER, warnings.size(), target
                ));
                warnings.forEach(warning -> {
                    sender.sendMessage(this.plugin.getMessageLoader().get(
                            MessageKeys.PUNISHMENT_WARNINGS_ENTRY, warning.getId(), warning.getRelatedId(), warning.getTarget(),
                            warning.getReason(), warning.getInitiator(), warning.getDate()
                    ));
                    sender.sendMessage(this.plugin .getMessageLoader().get(
                            MessageKeys.PUNISHMENT_WARNINGS_ACTIONS, warning.getRelatedId(), warning.getTarget()
                    ));
                });
            });
        } else {
            sender.sendMessage(this.plugin.getMessageLoader().get(
                    MessageKeys.PUNISHMENT_WARNINGS_USAGE, this.getName(), this.getDescription()
            ));
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return null;
    }

}
