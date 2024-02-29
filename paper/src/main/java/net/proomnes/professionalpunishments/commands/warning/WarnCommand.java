package net.proomnes.professionalpunishments.commands.warning;

import net.proomnes.professionalpunishments.ProfessionalPunishments;
import net.proomnes.professionalpunishments.objects.Reason;
import net.proomnes.professionalpunishments.util.messages.MessageKeys;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class WarnCommand extends Command implements TabCompleter {

    private final ProfessionalPunishments plugin;

    public WarnCommand(final ProfessionalPunishments professionalPunishments) {
        super(professionalPunishments.getConfig().getString("commands.warn.name"));
        this.setDescription(professionalPunishments.getConfig().getString("commands.warn.description"));
        this.setAliases(professionalPunishments.getConfig().getStringList("commands.warn.aliases"));
        this.setPermission(professionalPunishments.getConfig().getString("commands.warn.permission"));

        this.plugin = professionalPunishments;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String s, @NotNull String[] args) {
        if (!this.testPermission(sender)) return true;
        if (args.length == 2) {
            final String target = args[0];
            final String preset = args[1];

            if (this.plugin.getDataService().warningPresets
                    .stream()
                    .noneMatch(reason -> reason.getId().equals(preset))
            ) {
                sender.sendMessage(this.plugin.getMessageLoader().get(
                        MessageKeys.PUNISHMENT_WARN_INVALID_PRESET, preset
                ));
                return true;
            }

            final Reason reason = this.plugin.getDataService().warningPresets
                    .stream()
                    .filter(set -> set.getId().equals(preset))
                    .findFirst()
                    .get();

            this.plugin.getWarningService().warnPlayer(target, reason.getReason(), sender.getName(), reason.getMinutes());
            sender.sendMessage(this.plugin.getMessageLoader().get(
                    MessageKeys.PUNISHMENT_WARN_SUCCESSFULLY_WARNED, target, reason.getReason()
            ));
        } else {
            this.plugin.getDataService().warningPresets.forEach(reason -> {
                sender.sendMessage(this.plugin.getMessageLoader().get(
                        MessageKeys.PUNISHMENT_WARN_PRESET, reason.getId(), reason.getReason(), reason.getMinutes()
                ));
            });
            sender.sendMessage(this.plugin.getMessageLoader().get(
                    MessageKeys.PUNISHMENT_WARN_USAGE, this.getName(), this.getDescription()
            ));
        }
        return false;
    }


    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return null;
    }
}
