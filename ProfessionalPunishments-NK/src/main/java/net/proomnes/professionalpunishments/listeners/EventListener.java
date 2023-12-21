package net.proomnes.professionalpunishments.listeners;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerChatEvent;
import cn.nukkit.event.player.PlayerPreLoginEvent;
import lombok.AllArgsConstructor;
import net.proomnes.professionalpunishments.ProfessionalPunishments;
import net.proomnes.professionalpunishments.objects.Punishment;
import net.proomnes.professionalpunishments.util.messages.MessageKeys;

@AllArgsConstructor
public class EventListener implements Listener {

    private final ProfessionalPunishments professionalPunishments;

    @EventHandler
    public void on(final PlayerPreLoginEvent event) {
        final Player player = event.getPlayer();

        // check if player is banned
        this.professionalPunishments.getBanService().isBanned(player.getName(), is -> {
            if (is) {
                this.professionalPunishments.getServer().getScheduler().scheduleDelayedTask(this.professionalPunishments, () -> {
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
                            player.kick(this.professionalPunishments.getMessageLoader().get(
                                    MessageKeys.SYSTEM_PLAYER_BANNED, punishment.getId(), punishment.getReason(), punishment.getInitiator(), punishment.getDate(), this.professionalPunishments.getRemainingTime(punishment.getExpire()
                                    )), false);
                        }
                    });
                }, 45);
            }
        });
    }

    @EventHandler
    public void on(final PlayerChatEvent event) {
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
