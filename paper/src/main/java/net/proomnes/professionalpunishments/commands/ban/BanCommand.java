package net.proomnes.professionalpunishments.commands.ban;

import net.proomnes.professionalpunishments.ProfessionalPunishments;
import net.proomnes.professionalpunishments.objects.Reason;
import net.proomnes.professionalpunishments.util.messages.MessageKeys;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BanCommand extends Command implements TabCompleter {

    private final ProfessionalPunishments plugin;

    public BanCommand(final ProfessionalPunishments professionalPunishments) {
        super(professionalPunishments.getConfig().getString("commands.ban.name"));
        this.setDescription(professionalPunishments.getConfig().getString("commands.ban.description"));
        this.setAliases(professionalPunishments.getConfig().getStringList("commands.ban.aliases"));
        this.setPermission(professionalPunishments.getConfig().getString("commands.ban.permission"));

        this.plugin = professionalPunishments;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String s, @NotNull String[] args) {
        if (!this.testPermission(sender)) return true;
        if (args.length == 2) {
            final String target = args[0];
            final String preset = args[1];

            this.plugin.getBanService().isBanned(target, is -> {
                if (is) {
                    sender.sendMessage(this.plugin.getMessageLoader().get(
                            MessageKeys.SYSTEM_PLAYER_ALREADY_BANNED, target
                    ));
                    return;
                }

                if (this.plugin.getDataService().banPresets
                        .stream()
                        .noneMatch(reason -> reason.getId().equals(preset))
                ) {
                    sender.sendMessage(this.plugin.getMessageLoader().get(
                            MessageKeys.PUNISHMENT_BAN_INVALID_PRESET, preset
                    ));
                    return;
                }

                final Reason reason = this.plugin.getDataService().banPresets
                        .stream()
                        .filter(set -> set.getId().equals(preset))
                        .findFirst()
                        .get();

                this.plugin.getBanService().banPlayer(target, reason.getReason(), sender.getName(), reason.getMinutes());
                sender.sendMessage(this.plugin.getMessageLoader().get(
                        MessageKeys.PUNISHMENT_BAN_SUCCESSFULLY_BANNED, target, reason.getReason()
                ));
            });
        } else {
            this.plugin.getDataService().banPresets.forEach(reason -> {
                sender.sendMessage(this.plugin.getMessageLoader().get(
                        MessageKeys.PUNISHMENT_BAN_PRESET, reason.getId(), reason.getReason(), reason.getMinutes()
                ));
            });
            sender.sendMessage(this.plugin.getMessageLoader().get(
                    MessageKeys.PUNISHMENT_BAN_USAGE, this.getName(), this.getDescription()
            ));
        }
        return false;
    }


    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return null;
    }

}
