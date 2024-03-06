package net.proomnes.professionalpunishments.listeners;

import io.papermc.paper.event.player.AsyncChatEvent;
import lombok.AllArgsConstructor;
import net.proomnes.professionalpunishments.ProfessionalPunishments;
import net.proomnes.professionalpunishments.objects.Punishment;
import net.proomnes.professionalpunishments.util.messages.MessageKeys;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;

@AllArgsConstructor
public class EventListener implements Listener {

    private final ProfessionalPunishments professionalPunishments;

    @EventHandler
    public void on(final AsyncPlayerPreLoginEvent event) {
        final String player = event.getName();

        // check if player is banned
        this.professionalPunishments.getBanService().isBanned(player, is -> {
            if (is) {
                // get active ban
                this.professionalPunishments.getBanService().getBan(player, punishment -> {
                    // check if this punishment is not permanent
                    if (punishment.getExpire() != -1) {
                        // check if expiration time is reached
                        if (punishment.getExpire() < System.currentTimeMillis()) {
                            // if reached, unban the player
                            this.professionalPunishments.getBanService().unbanPlayer(player, "SYSTEM", "Punishment expired");
                            return;
                        }

                        // if not reached, kick the player
                        event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, this.professionalPunishments.getMessageLoader().get(
                                MessageKeys.SYSTEM_SCREEN_BAN, punishment.getId(), punishment.getReason(), punishment.getInitiator(), punishment.getDate(), this.professionalPunishments.getRemainingTime(punishment.getExpire()
                                )));
                    }
                });
            }
        });
    }

    @EventHandler
    public void on(final AsyncChatEvent event) {
        final Player player = event.getPlayer();

        // check if player is muted
        if (this.professionalPunishments.getMuteService().cachedMutes
                .stream()
                .anyMatch(mute -> mute.getTarget().equals(player.getName()))) {

            // if muted, get the mute data
            final Punishment mute = this.professionalPunishments.getMuteService().cachedMutes
                    .stream()
                    .filter(punishment -> punishment.getTarget().equals(player.getName()))
                    .findFirst()
                    .get();

            // check if mute is already expired; if expired, return
            if (mute.getExpire() < System.currentTimeMillis()) {
                this.professionalPunishments.getMuteService().unmutePlayer(player.getName(), "SYSTEM", "Punishment is expired.");
                return;
            }

            // if muted, cancel the event and block message
            player.sendMessage(this.professionalPunishments.getMessageLoader().get(
                    MessageKeys.SYSTEM_SCREEN_MUTE, mute.getId(), mute.getReason(), mute.getInitiator(), mute.getDate(), this.professionalPunishments.getRemainingTime(mute.getExpire()))
            );
            event.setCancelled(true);
        }
    }

}
