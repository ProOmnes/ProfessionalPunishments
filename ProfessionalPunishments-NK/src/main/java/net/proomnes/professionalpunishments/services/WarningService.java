package net.proomnes.professionalpunishments.services;

import net.proomnes.professionalpunishments.ProfessionalPunishments;
import net.proomnes.professionalpunishments.objects.Punishment;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class WarningService {

    private final ProfessionalPunishments professionalPunishments;
    public final Map<String, Punishment> cachedWarnings = new HashMap<>();

    public WarningService(final ProfessionalPunishments professionalPunishments) {
        this.professionalPunishments = professionalPunishments;

        this.professionalPunishments.getDataAccess().getAllActiveWarnings(punishmentSet -> {
            punishmentSet.forEach(punishment -> {
                this.cachedWarnings.put(punishment.getId(), punishment);
            });
        });
    }

    public void warnPlayer(final String target, final String reason, final String initiator, final int minutes) {
        this.professionalPunishments.getDataAccess().warnPlayer(target, reason, initiator, minutes, id -> {
            this.getWarning(id, punishment -> {
                this.cachedWarnings.put(id, punishment);

                // call event
            });
        });
    }

    public void unwarnPlayer(final String target, final String warnId, final String initiator, final String reason) {
        this.getWarning(warnId, punishment -> {
            this.professionalPunishments.getDataAccess().punishmentIdExists(warnId, Punishment.Type.PUNISHMENT_WARNING, is -> {
                if (is) {
                    this.professionalPunishments.getDataAccess().unwarnPlayer(target, warnId, initiator, reason, id -> {
                        this.cachedWarnings.remove(punishment.getId());

                        // call event
                    });
                } else {
                    this.cachedWarnings.remove(punishment.getId());
                }
            });
        });
    }

    public void getWarning(final String id, final Consumer<Punishment> punishment) {
        final AtomicReference<Punishment> punishmentReference = new AtomicReference<>(null);

        if (this.cachedWarnings.containsKey(id)) punishmentReference.set(this.cachedWarnings.get(id));

        if (punishmentReference.get() == null) {
            this.professionalPunishments.getDataAccess().getPunishment(id, punishmentQuery -> {
                punishment.accept(punishmentQuery);
                this.cachedWarnings.put(punishmentQuery.getId(), punishmentQuery);
            });
        } else {
            punishment.accept(punishmentReference.get());
        }
    }

    public void getActiveWarnings(final String target, final Consumer<Set<Punishment>> punishments) {
        this.professionalPunishments.getDataAccess().getActiveWarnings(target, punishmentQuery -> {
            punishmentQuery.forEach(punishment -> {
                if (!this.cachedWarnings.containsKey(punishment.getId())) {
                    this.cachedWarnings.put(punishment.getId(), punishment);
                }
            });
            punishments.accept(punishmentQuery);
        });
    }

}
