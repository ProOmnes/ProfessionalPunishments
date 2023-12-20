package net.proomnes.professionalpunishments.services;

import lombok.Getter;
import net.proomnes.professionalpunishments.ProfessionalPunishments;
import net.proomnes.professionalpunishments.objects.Punishment;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

@Getter
public class BanService {

    private final ProfessionalPunishments professionalPunishments;
    public final Map<String, Punishment> cachedBans = new HashMap<>();

    public BanService(final ProfessionalPunishments professionalPunishments) {
        this.professionalPunishments = professionalPunishments;

        this.professionalPunishments.getDataAccess().getAllBans(punishmentSet -> {
            punishmentSet.forEach(punishment -> {
                this.cachedBans.put(punishment.getId(), punishment);
            });
        });
    }

    public void banPlayer(final String target, final String reason, final String initiator, final int minutes) {
        this.professionalPunishments.getDataAccess().banPlayer(target, reason, initiator, minutes, id -> {
            this.getBan(target, punishment -> {
                this.cachedBans.put(id, punishment);

                // call event
            });
        });
    }

    public void isBanned(final String target, final Consumer<Boolean> is) {
        final AtomicReference<Punishment> punishment = new AtomicReference<>(null);

        this.cachedBans.values().forEach(entry -> {
            if (entry.getTarget().equals(target)) {
                punishment.set(entry);
            }
        });

        if (punishment.get() == null) {
            this.professionalPunishments.getDataAccess().isBanned(target, is::accept);
        } else {
            is.accept(true);
        }
    }

    public void unbanPlayer(final String target, final String initiator, final String reason) {
        this.getBan(target, punishment -> {
            this.professionalPunishments.getDataAccess().isBanned(target, is -> {
                if (is) {
                    this.professionalPunishments.getDataAccess().unbanPlayer(target, initiator, reason, id -> {
                        this.cachedBans.remove(punishment.getId());

                        // call event
                    });
                } else {
                    this.cachedBans.remove(punishment.getId());
                }
            });
        });
    }

    public void getBan(final String target, final Consumer<Punishment> punishment) {
        final AtomicReference<Punishment> punishmentReference = new AtomicReference<>(null);

        this.cachedBans.values().forEach(entry -> {
            if (entry.getTarget().equals(target)) {
                punishmentReference.set(entry);
            }
        });

        if (punishmentReference.get() == null) {
            this.professionalPunishments.getDataAccess().getBan(target, punishmentQuery -> {
                punishment.accept(punishmentQuery);
                this.cachedBans.put(punishmentQuery.getId(), punishmentQuery);
            });
        } else {
            punishment.accept(punishmentReference.get());
        }
    }

    public void banIdExists(final String id, final Consumer<Boolean> exists) {
        if (this.cachedBans.containsKey(id)) {
            exists.accept(true);
        } else {
            this.professionalPunishments.getDataAccess().punishmentIdExists(id, Punishment.Type.PUNISHMENT_BAN, exists::accept);
        }
    }

}
