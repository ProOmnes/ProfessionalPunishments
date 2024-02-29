package net.proomnes.professionalpunishments.commands.warning;

import net.proomnes.professionalpunishments.ProfessionalPunishments;
import net.proomnes.professionalpunishments.util.messages.MessageKeys;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class UnwarnCommand extends Command implements TabCompleter {

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
            final StringBuilder reason = new StringBuilder();
            for (int i = 3; i < args.length; ++i) reason.append(args[i]).append(" ");

            this.plugin.getWarningService().getWarning(warnId, punishment -> {
                if (punishment == null || !punishment.getTarget().equalsIgnoreCase(target)) {
                    sender.sendMessage(this.plugin.getMessageLoader().get(
                            MessageKeys.PUNISHMENT_UNWARN_INVALID_ID, target, warnId
                    ));
                    return;
                }

                this.plugin.getWarningService().unwarnPlayer(target, warnId, sender.getName(), reason.toString());
                sender.sendMessage(this.plugin.getMessageLoader().get(
                        MessageKeys.PUNISHMENT_UNWARN_SUCCESSFULLY_UNWARNED, target, reason.toString()
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
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return null;
    }

}
