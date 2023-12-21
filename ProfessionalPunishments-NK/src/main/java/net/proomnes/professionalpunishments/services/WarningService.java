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
import java.util.stream.Collectors;

public class WarningService {

    private final ProfessionalPunishments professionalPunishments;
    public final Set<Punishment> cachedWarnings = new HashSet<>();

    public WarningService(final ProfessionalPunishments professionalPunishments) {
        this.professionalPunishments = professionalPunishments;

        this.professionalPunishments.getDataAccess().getAllActiveWarnings(this.cachedWarnings::addAll);
    }

    public void warnPlayer(final String target, final String reason, final String initiator, final int minutes) {
        this.professionalPunishments.getDataAccess().warnPlayer(target, reason, initiator, minutes, id -> {
            this.getWarning(id, punishment -> {
                this.cachedWarnings.add(punishment);

                this.professionalPunishments.getServer().getPluginManager().callEvent(new PunishmentInitiateEvent(punishment, id, initiator));

                // inserting warning log
                this.professionalPunishments.getDataService().insertLog(new Punishment.Log(this.professionalPunishments.getRandomId(5, "WL"), id, Punishment.LogType.LOG_WARNING, target, reason, initiator, punishment.getDate()));
            });
        });
    }

    public void unwarnPlayer(final String target, final String warnId, final String initiator, final String reason) {
        this.getWarning(warnId, punishment -> {
            this.professionalPunishments.getDataAccess().punishmentIdExists(warnId, Punishment.Type.PUNISHMENT_WARNING, is -> {
                if (is) {
                    this.professionalPunishments.getDataAccess().unwarnPlayer(target, warnId, initiator, reason, id -> {
                        this.cachedWarnings.removeIf(warning -> warning.getId().equals(punishment.getId()));

                        this.professionalPunishments.getServer().getPluginManager().callEvent(new PunishmentAbortEvent(punishment, target, initiator, reason, id));

                        this.professionalPunishments.getDataService().insertLog(new Punishment.Log(id, punishment.getId(), Punishment.LogType.LOG_WARNING, target, reason, initiator, this.professionalPunishments.getDate()));
                    });
                } else {
                    this.cachedWarnings.removeIf(warning -> warning.getId().equals(punishment.getId()));
                }
            });
        });
    }

    public void getWarning(final String id, final Consumer<Punishment> punishment) {
        final AtomicReference<Punishment> punishmentReference = new AtomicReference<>(null);

        try {
            punishmentReference.set(this.cachedWarnings
                    .stream()
                    .filter(ban -> ban.getId().equals(id))
                    .findFirst()
                    .get()
            );
        } catch (final NoSuchElementException exception) {
            punishmentReference.set(null);
        }

        if (punishmentReference.get() == null) {
            this.professionalPunishments.getDataAccess().getPunishment(id, punishmentQuery -> {
                punishment.accept(punishmentQuery);
                this.cachedWarnings.add(punishmentQuery);
            });
        } else {
            punishment.accept(punishmentReference.get());
        }
    }

    public void getActiveWarnings(final String target, final Consumer<Set<Punishment>> punishments) {
        punishments.accept(this.cachedWarnings
                .stream()
                .filter(punishment -> punishment.getTarget().equals(target))
                .collect(Collectors.toSet())
        );
    }

}
