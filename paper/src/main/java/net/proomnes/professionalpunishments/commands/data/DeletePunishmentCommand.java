package net.proomnes.professionalpunishments.commands.data;

import net.proomnes.professionalpunishments.ProfessionalPunishments;
import net.proomnes.professionalpunishments.objects.Punishment;
import net.proomnes.professionalpunishments.util.messages.MessageKeys;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class DeletePunishmentCommand extends BukkitCommand {

    private final ProfessionalPunishments plugin;

    public DeletePunishmentCommand(final ProfessionalPunishments professionalPunishments) {
        super(professionalPunishments.getConfig().getString("commands.deletepunishment.name"));
        this.setDescription(professionalPunishments.getConfig().getString("commands.deletepunishment.description"));
        this.setAliases(professionalPunishments.getConfig().getStringList("commands.deletepunishment.aliases"));
        this.setPermission(professionalPunishments.getConfig().getString("commands.deletepunishment.permission"));

        this.plugin = professionalPunishments;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String s, @NotNull String[] args) {
        if (!this.testPermission(sender)) return true;
        if (args.length == 1) {
            final String id = args[0];
            this.plugin.getDataService().punishmentLogIdExists(id, id.startsWith("BL") ? Punishment.LogType.LOG_BAN :
                            id.startsWith("ML") ? Punishment.LogType.LOG_MUTE : id.startsWith("WL") ? Punishment.LogType.LOG_WARNING :
                                    id.startsWith("UBL") ? Punishment.LogType.LOG_UNBAN : id.startsWith("UML") ? Punishment.LogType.LOG_UNMUTE : Punishment.LogType.LOG_KICK,
                    is -> {
                        if (!is) {
                            sender.sendMessage(this.plugin.getMessageLoader().get(
                                    MessageKeys.SYSTEM_LOG_ID_NOT_FOUND, id
                            ));
                            return;
                        }

                        this.plugin.getDataService().deleteLogEntry(id);
                        sender.sendMessage(this.plugin.getMessageLoader().get(
                                MessageKeys.PUNISHMENT_DELETEPUNISHMENT_DELETED, id
                        ));
                    });
        } else {
            sender.sendMessage(this.plugin.getMessageLoader().get(
                    MessageKeys.PUNISHMENT_DELETEPUNISHMENT_USAGE, this.getName(), this.getDescription()
            ));
        }
        return false;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        return Collections.emptyList();
    }
}
