package net.proomnes.professionalpunishments.services;

import net.proomnes.professionalpunishments.ProfessionalPunishments;
import net.proomnes.professionalpunishments.objects.Punishment;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class DataService {

    private final ProfessionalPunishments professionalPunishments;
    public final Set<Punishment.Log> cachedLogs = new HashSet<>();

    public DataService(final ProfessionalPunishments professionalPunishments) {
        this.professionalPunishments = professionalPunishments;

        this.professionalPunishments.getDataAccess().getAllLogs(this.cachedLogs::addAll);
    }

    public void punishmentIdExists(final String id, final Punishment.Type type, final Consumer<Boolean> exists) {
        this.professionalPunishments.getDataAccess().punishmentIdExists(id, type, exists::accept);
    }

    public void punishmentLogIdExists(final String id, final Punishment.LogType type, final Consumer<Boolean> exists) {
        exists.accept(this.cachedLogs
                .stream()
                .anyMatch(log -> log.getId().equals(id) && log.getLogType() == type)
        );
    }

    public void setPunishmentReason(final Punishment punishment, final String reason) {
        switch (punishment.getType()) {
            case PUNISHMENT_BAN -> {
                this.professionalPunishments.getBanService().cachedBans.get(punishment.getId()).setReason(reason);
            }
            case PUNISHMENT_MUTE -> {
                this.professionalPunishments.getMuteService().cachedMutes.get(punishment.getId()).setReason(reason);
            }
            case PUNISHMENT_WARNING -> {
                this.professionalPunishments.getWarningService().cachedWarnings.get(punishment.getId()).setReason(reason);
            }
        }

        this.cachedLogs
                .stream()
                .filter(log -> log.getRelatedId().equals(punishment.getId()))
                .peek(log -> log.setReason(reason)).toList();

        this.professionalPunishments.getDataAccess().setPunishmentReason(punishment, reason);
    }

    public void setPunishmentEnding(final Punishment punishment, final int minutes) {
        final long expire = System.currentTimeMillis() + (minutes * 60000L);
        switch (punishment.getType()) {
            case PUNISHMENT_BAN -> {
                this.professionalPunishments.getBanService().cachedBans.get(punishment.getId()).setExpire(expire);
            }
            case PUNISHMENT_MUTE -> {
                this.professionalPunishments.getMuteService().cachedMutes.get(punishment.getId()).setExpire(expire);
            }
            case PUNISHMENT_WARNING -> {
                this.professionalPunishments.getWarningService().cachedWarnings.get(punishment.getId()).setExpire(expire);
            }
        }

        this.professionalPunishments.getDataAccess().setPunishmentEnding(punishment, minutes);
    }

    public void getPunishment(final String id, final Consumer<Punishment> punishmentConsumer) {
        this.professionalPunishments.getDataAccess().getPunishment(id, punishmentConsumer::accept);
    }

    public void insertLog(final Punishment.Log log) {
        this.professionalPunishments.getDataService().insertLog(log);
        this.cachedLogs.add(log);
    }

    public void getLogs(final String target, final Punishment.LogType type, final Consumer<Set<Punishment.Log>> punishmentConsumer) {
        punishmentConsumer.accept(
                this.cachedLogs
                        .stream()
                        .filter(log -> log.getTarget().equals(target) && log.getLogType() == type)
                        .collect(Collectors.toSet())
        );
    }

    public void clearLogs(final String target, final Punishment.LogType type) {
        this.professionalPunishments.getDataAccess().clearLogs(target, type);

        this.cachedLogs
                .stream()
                .filter(log -> log.getTarget().equals(target))
                .forEach(log -> this.deleteLogEntry(log.getId()));
    }

    public void deleteLogEntry(final String id) {
        this.professionalPunishments.getDataService().deleteLogEntry(id);
        this.cachedLogs.removeIf(log -> log.getId().equals(id));
    }

}
