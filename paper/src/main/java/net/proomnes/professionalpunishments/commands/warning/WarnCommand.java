package net.proomnes.professionalpunishments.commands.warning;

import net.proomnes.professionalpunishments.ProfessionalPunishments;
import net.proomnes.professionalpunishments.objects.Reason;
import net.proomnes.professionalpunishments.util.messages.MessageKeys;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WarnCommand extends BukkitCommand {

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

            this.plugin.getWarningService().warnPlayer(target, reason.getReason(), sender.getName(), this.plugin.timeFormatToMinutes(reason.getDuration()));
            sender.sendMessage(this.plugin.getMessageLoader().get(
                    MessageKeys.PUNISHMENT_WARN_SUCCESSFULLY_WARNED, target, reason.getReason()
            ));
        } else {
            this.plugin.getDataService().warningPresets.forEach(reason -> {
                sender.sendMessage(this.plugin.getMessageLoader().get(
                        MessageKeys.PUNISHMENT_WARN_PRESET, reason.getId(), reason.getReason(), reason.getDuration()
                ));
            });
            sender.sendMessage(this.plugin.getMessageLoader().get(
                    MessageKeys.PUNISHMENT_WARN_USAGE, this.getName(), this.getDescription()
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
            } else if (args.length == 2) {
                this.plugin.getDataService().warningPresets.forEach(reason -> {
                    completer.add(reason.getId());
                });
            }

            return completer;
        }
        return Collections.emptyList();
    }
}
