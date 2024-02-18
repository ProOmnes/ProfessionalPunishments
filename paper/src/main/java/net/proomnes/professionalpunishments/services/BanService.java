package net.proomnes.professionalpunishments.services;

import lombok.Getter;
import net.proomnes.professionalpunishments.ProfessionalPunishments;
import net.proomnes.professionalpunishments.events.PunishmentAbortEvent;
import net.proomnes.professionalpunishments.events.PunishmentInitiateEvent;
import net.proomnes.professionalpunishments.objects.Punishment;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

@Getter
public class BanService {

    private final ProfessionalPunishments professionalPunishments;
    public final Set<Punishment> cachedBans = new HashSet<>();

    public BanService(final ProfessionalPunishments professionalPunishments) {
        this.professionalPunishments = professionalPunishments;

        this.professionalPunishments.getDataAccess().getAllBans(this.cachedBans::addAll);
    }

    public void banPlayer(final String target, final String reason, final String initiator, final int minutes) {
        this.professionalPunishments.getDataAccess().banPlayer(target, reason, initiator, minutes, id -> {
            this.getBan(target, punishment -> {
                this.cachedBans.add(punishment);

                this.professionalPunishments.getServer().getPluginManager().callEvent(new PunishmentInitiateEvent(punishment, id, initiator));

                // inserting ban log
                this.professionalPunishments.getDataService().insertLog(new Punishment.Log(this.professionalPunishments.getRandomId(5, "BL"), id, Punishment.LogType.LOG_BAN, target, reason, initiator, punishment.getDate()));
            });
        });
    }

    public void isBanned(final String target, final Consumer<Boolean> is) {
        is.accept(this.cachedBans
                .stream()
                .anyMatch(punishment -> punishment.getTarget().equals(target))
        );
    }

    public void unbanPlayer(final String target, final String initiator, final String reason) {
        this.getBan(target, punishment -> {
            this.professionalPunishments.getDataAccess().isBanned(target, is -> {
                if (is) {
                    this.professionalPunishments.getDataAccess().unbanPlayer(target, initiator, reason, id -> {
                        this.cachedBans.removeIf(ban -> ban.getId().equals(punishment.getId()));

                        this.professionalPunishments.getServer().getPluginManager().callEvent(new PunishmentAbortEvent(punishment, target, initiator, reason, id));

                        this.professionalPunishments.getDataService().insertLog(new Punishment.Log(id, punishment.getId(), Punishment.LogType.LOG_UNBAN, target, reason, initiator, this.professionalPunishments.getDate()));
                    });
                } else {
                    this.cachedBans.removeIf(ban -> ban.getId().equals(punishment.getId()));
                }
            });
        });
    }

    public void getBan(final String target, final Consumer<Punishment> punishment) {
        final AtomicReference<Punishment> punishmentReference = new AtomicReference<>(null);

        try {
            punishmentReference.set(this.cachedBans
                    .stream()
                    .filter(ban -> ban.getTarget().equals(target))
                    .findFirst()
                    .get()
            );
        } catch (final NoSuchElementException exception) {
            punishmentReference.set(null);
        }

        if (punishmentReference.get() == null) {
            this.professionalPunishments.getDataAccess().getBan(target, punishmentQuery -> {
                punishment.accept(punishmentQuery);
                this.cachedBans.add(punishmentQuery);
            });
        } else {
            punishment.accept(punishmentReference.get());
        }
    }

    public void banIdExists(final String id, final Consumer<Boolean> exists) {
        exists.accept(this.cachedBans
                .stream()
                .anyMatch(punishment -> punishment.getId().equals(id))
        );
    }

}
