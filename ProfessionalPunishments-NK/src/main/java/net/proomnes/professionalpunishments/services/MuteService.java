package net.proomnes.professionalpunishments.services;

import net.proomnes.professionalpunishments.ProfessionalPunishments;
import net.proomnes.professionalpunishments.objects.Punishment;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class MuteService {

    private final ProfessionalPunishments professionalPunishments;
    public final Map<String, Punishment> cachedMutes = new HashMap<>();

    public MuteService(final ProfessionalPunishments professionalPunishments) {
        this.professionalPunishments = professionalPunishments;

        this.professionalPunishments.getDataAccess().getAllMutes(punishmentSet -> {
            punishmentSet.forEach(punishment -> {
                this.cachedMutes.put(punishment.getId(), punishment);
            });
        });
    }

    public void mutePlayer(final String target, final String reason, final String initiator, final int minutes) {
        this.professionalPunishments.getDataAccess().mutePlayer(target, reason, initiator, minutes, id -> {
            this.getMute(target, punishment -> {
                this.cachedMutes.put(id, punishment);

                // call event
            });
        });
    }

    public void isMuted(final String target, final Consumer<Boolean> is) {
        final AtomicReference<Punishment> punishment = new AtomicReference<>(null);

        this.cachedMutes.values().forEach(entry -> {
            if (entry.getTarget().equals(target)) {
                punishment.set(entry);
            }
        });

        if (punishment.get() == null) {
            this.professionalPunishments.getDataAccess().isMuted(target, is::accept);
        } else {
            is.accept(true);
        }
    }

    public void unmutePlayer(final String target, final String initiator, final String reason) {
        this.getMute(target, punishment -> {
            this.professionalPunishments.getDataAccess().isMuted(target, is -> {
                if (is) {
                    this.professionalPunishments.getDataAccess().unmutePlayer(target, initiator, reason, id -> {
                        this.cachedMutes.remove(punishment.getId());

                        // call event
                    });
                } else {
                    this.cachedMutes.remove(punishment.getId());
                }
            });
        });
    }

    public void getMute(final String target, final Consumer<Punishment> punishment) {
        final AtomicReference<Punishment> punishmentReference = new AtomicReference<>(null);

        this.cachedMutes.values().forEach(entry -> {
            if (entry.getTarget().equals(target)) {
                punishmentReference.set(entry);
            }
        });

        if (punishmentReference.get() == null) {
            this.professionalPunishments.getDataAccess().getMute(target, punishmentQuery -> {
                punishment.accept(punishmentQuery);
                this.cachedMutes.put(punishmentQuery.getId(), punishmentQuery);
            });
        } else {
            punishment.accept(punishmentReference.get());
        }
    }

    public void muteIdExists(final String id, final Consumer<Boolean> exists) {
        if (this.cachedMutes.containsKey(id)) {
            exists.accept(true);
        } else {
            this.professionalPunishments.getDataAccess().punishmentIdExists(id, Punishment.Type.PUNISHMENT_MUTE, exists::accept);
        }
    }

}
