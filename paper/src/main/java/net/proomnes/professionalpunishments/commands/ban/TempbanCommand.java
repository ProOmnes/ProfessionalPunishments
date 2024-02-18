package net.proomnes.professionalpunishments.commands.ban;

import net.proomnes.professionalpunishments.ProfessionalPunishments;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TempbanCommand extends Command {

    private final ProfessionalPunishments professionalPunishments;

    public TempbanCommand(final ProfessionalPunishments professionalPunishments) {
        super(professionalPunishments.getConfig().getString("commands.tempban.name"));
        this.setDescription(professionalPunishments.getConfig().getString("commands.tempban.description"));
        this.setAliases(professionalPunishments.getConfig().getStringList("commands.tempban.aliases"));
        this.setPermission(professionalPunishments.getConfig().getString("commands.tempban.permission"));

        this.professionalPunishments = professionalPunishments;
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!this.testPermission(sender)) return true;
        if (sender instanceof Player player) {
            if (args.length == 1) {

            } else {

            }
        }
        return true;
    }
}
