package net.proomnes.professionalpunishments.commands.data;

import net.proomnes.professionalpunishments.ProfessionalPunishments;
import net.proomnes.professionalpunishments.objects.Punishment;
import net.proomnes.professionalpunishments.util.messages.MessageKeys;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CheckpunishmentCommand extends Command implements TabCompleter {

    private final ProfessionalPunishments plugin;

    public CheckpunishmentCommand(final ProfessionalPunishments professionalPunishments) {
        super(professionalPunishments.getConfig().getString("commands.checkpunishment.name"));
        this.setDescription(professionalPunishments.getConfig().getString("commands.checkpunishment.description"));
        this.setAliases(professionalPunishments.getConfig().getStringList("commands.checkpunishment.aliases"));
        this.setPermission(professionalPunishments.getConfig().getString("commands.checkpunishment.permission"));

        this.plugin = professionalPunishments;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String s, @NotNull String[] args) {
        if (!this.testPermission(sender)) return true;
        if (args.length == 1) {
            final String id = args[0];
            this.plugin.getDataService().punishmentIdExists(id, id.startsWith("B") ? Punishment.Type.PUNISHMENT_BAN : id.startsWith("M") ? Punishment.Type.PUNISHMENT_MUTE : Punishment.Type.PUNISHMENT_WARNING,
                    is -> {
                        if (!is) {
                            sender.sendMessage(this.plugin.getMessageLoader().get(MessageKeys.SYSTEM_PUNISHMENT_ID_NOT_FOUND, id));
                            return;
                        }

                        this.plugin.getDataService().getPunishment(id, punishment -> {
                            sender.sendMessage(this.plugin.getMessageLoader().get(
                                    MessageKeys.PUNISHMENT_CHECKPUNISHMENT_HEADER, punishment.getId()
                            ));
                            sender.sendMessage(this.plugin.getMessageLoader().get(
                                    MessageKeys.PUNISHMENT_CHECKPUNISHMENT_CONTENT, punishment.getId(), punishment.getRelatedId(),
                                    punishment.getTarget(), punishment.getReason(), punishment.getInitiator(), punishment.getDate(),
                                    this.plugin.getRemainingTime(punishment.getExpire())
                            ));
                        });
                    });
        } else {
            sender.sendMessage(this.plugin.getMessageLoader().get(
                    MessageKeys.PUNISHMENT_CHECKPUNISHMENT_USAGE, this.getName(), this.getDescription()
            ));
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return null;
    }
}
