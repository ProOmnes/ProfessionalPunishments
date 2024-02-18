package net.proomnes.professionalpunishments.listeners;

import io.papermc.paper.event.player.AsyncChatEvent;
import lombok.AllArgsConstructor;
import net.kyori.adventure.text.Component;
import net.proomnes.professionalpunishments.ProfessionalPunishments;
import net.proomnes.professionalpunishments.objects.Punishment;
import net.proomnes.professionalpunishments.util.messages.MessageKeys;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerPreLoginEvent;

@AllArgsConstructor
public class EventListener implements Listener {

    private final ProfessionalPunishments professionalPunishments;

    @EventHandler
    public void on(final PlayerPreLoginEvent event) {
        final Player player = (Player) event.getAddress();

        // check if player is banned
        this.professionalPunishments.getBanService().isBanned(player.getName(), is -> {
            if (is) {
                this.professionalPunishments.getServer().getScheduler().scheduleSyncDelayedTask(this.professionalPunishments, () -> {
                    // get active ban
                    this.professionalPunishments.getBanService().getBan(player.getName(), punishment -> {
                        // check if this punishment is not permanently
                        if (punishment.getExpire() != -1) {
                            // check if expiration time is reached
                            if (punishment.getExpire() < System.currentTimeMillis()) {
                                // if reached, unban the player
                                this.professionalPunishments.getBanService().unbanPlayer(player.getName(), "SYSTEM", "Punishment is expired.");
                                return;
                            }

                            // if not reached, kick the player
                            player.kick(Component.text(this.professionalPunishments.getMessageLoader().get(
                                    MessageKeys.SYSTEM_PLAYER_BANNED, punishment.getId(), punishment.getReason(), punishment.getInitiator(), punishment.getDate(), this.professionalPunishments.getRemainingTime(punishment.getExpire()
                                    ))), PlayerKickEvent.Cause.UNKNOWN);
                        }
                    });
                }, 45);
            }
        });
    }

    @EventHandler
    public void on(final AsyncChatEvent event) {
        final Player player = event.getPlayer();

        if (this.professionalPunishments.getMuteService().cachedMutes
                .stream()
                .anyMatch(mute -> mute.getTarget().equals(player.getName()))) {

            final Punishment mute = this.professionalPunishments.getMuteService().cachedMutes
                    .stream()
                    .filter(punishment -> punishment.getTarget().equals(player.getName()))
                    .findFirst()
                    .get();

            if (mute.getExpire() < System.currentTimeMillis()) {
                this.professionalPunishments.getMuteService().unmutePlayer(player.getName(), "SYSTEM", "Punishment is expired.");
                return;
            }

            player.sendMessage(this.professionalPunishments.getMessageLoader().get(
                    MessageKeys.SYSTEM_PLAYER_MUTED, mute.getId(), mute.getReason(), mute.getInitiator(), mute.getDate(), this.professionalPunishments.getRemainingTime(mute.getExpire()))
            );
            event.setCancelled(true);
        }
    }

}
