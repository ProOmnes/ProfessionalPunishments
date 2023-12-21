package net.proomnes.professionalpunishments.services;

import net.proomnes.professionalpunishments.ProfessionalPunishments;
import net.proomnes.professionalpunishments.events.PunishmentAbortEvent;
import net.proomnes.professionalpunishments.events.PunishmentInitiateEvent;
import net.proomnes.professionalpunishments.objects.Punishment;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class MuteService {

    private final ProfessionalPunishments professionalPunishments;
    public final Set<Punishment> cachedMutes = new HashSet<>();

    public MuteService(final ProfessionalPunishments professionalPunishments) {
        this.professionalPunishments = professionalPunishments;

        this.professionalPunishments.getDataAccess().getAllMutes(this.cachedMutes::addAll);
    }

    public void mutePlayer(final String target, final String reason, final String initiator, final int minutes) {
        this.professionalPunishments.getDataAccess().mutePlayer(target, reason, initiator, minutes, id -> {
            this.getMute(target, punishment -> {
                this.cachedMutes.add(punishment);

                this.professionalPunishments.getServer().getPluginManager().callEvent(new PunishmentInitiateEvent(punishment, id, initiator));

                // inserting mute log
                this.professionalPunishments.getDataService().insertLog(new Punishment.Log(this.professionalPunishments.getRandomId(5, "ML"), id, Punishment.LogType.LOG_MUTE, target, reason, initiator, punishment.getDate()));
            });
        });
    }

    public void isMuted(final String target, final Consumer<Boolean> is) {
        is.accept(this.cachedMutes
                .stream()
                .anyMatch(punishment -> punishment.getTarget().equals(target))
        );
    }

    public void unmutePlayer(final String target, final String initiator, final String reason) {
        this.getMute(target, punishment -> {
            this.professionalPunishments.getDataAccess().isMuted(target, is -> {
                if (is) {
                    this.professionalPunishments.getDataAccess().unmutePlayer(target, initiator, reason, id -> {
                        this.cachedMutes.removeIf(mute -> mute.getId().equals(punishment.getId()));

                        this.professionalPunishments.getServer().getPluginManager().callEvent(new PunishmentAbortEvent(punishment, target, initiator, reason, id));

                        this.professionalPunishments.getDataService().insertLog(new Punishment.Log(id, punishment.getId(), Punishment.LogType.LOG_UNMUTE, target, reason, initiator, this.professionalPunishments.getDate()));
                    });
                } else {
                    this.cachedMutes.removeIf(mute -> mute.getId().equals(punishment.getId()));
                }
            });
        });
    }

    public void getMute(final String target, final Consumer<Punishment> punishment) {
        final AtomicReference<Punishment> punishmentReference = new AtomicReference<>(null);

        try {
            punishmentReference.set(this.cachedMutes
                    .stream()
                    .filter(ban -> ban.getTarget().equals(target))
                    .findFirst()
                    .get()
            );
        } catch (final NoSuchElementException exception) {
            punishmentReference.set(null);
        }

        if (punishmentReference.get() == null) {
            this.professionalPunishments.getDataAccess().getMute(target, punishmentQuery -> {
                punishment.accept(punishmentQuery);
                this.cachedMutes.add(punishmentQuery);
            });
        } else {
            punishment.accept(punishmentReference.get());
        }
    }

    public void muteIdExists(final String id, final Consumer<Boolean> exists) {
        exists.accept(this.cachedMutes
                .stream()
                .anyMatch(punishment -> punishment.getId().equals(id))
        );
    }

}
