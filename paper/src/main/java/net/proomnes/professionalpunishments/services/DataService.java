package net.proomnes.professionalpunishments.services;

import net.proomnes.professionalpunishments.ProfessionalPunishments;
import net.proomnes.professionalpunishments.events.PunishmentChangeEvent;
import net.proomnes.professionalpunishments.events.PunishmentLogDeleteEvent;
import net.proomnes.professionalpunishments.events.PunishmentLogInsertEvent;
import net.proomnes.professionalpunishments.objects.Punishment;
import net.proomnes.professionalpunishments.objects.Reason;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class DataService {

    private final ProfessionalPunishments professionalPunishments;
    public final Set<Punishment.Log> cachedLogs = new HashSet<>();
    public final LinkedHashSet<Reason> banPresets = new LinkedHashSet<>();
    public final LinkedHashSet<Reason> mutePresets = new LinkedHashSet<>();
    public final LinkedHashSet<Reason> warningPresets = new LinkedHashSet<>();

    public DataService(final ProfessionalPunishments professionalPunishments) {
        this.professionalPunishments = professionalPunishments;

        int check = 0;
        while (check == 0) {
            if (this.professionalPunishments.getDataAccess().connected()) {
                this.professionalPunishments.getDataAccess().getAllLogs(this.cachedLogs::addAll);
                check++;
            }
        }

        this.professionalPunishments.getConfig().getStringList("presets.ban").forEach(entry -> {
            final String[] presetData = entry.split(":");
            this.banPresets.add(new Reason(presetData[0], presetData[1], presetData[2]));
        });
        this.professionalPunishments.getConfig().getStringList("presets.mute").forEach(entry -> {
            final String[] presetData = entry.split(":");
            this.mutePresets.add(new Reason(presetData[0], presetData[1], presetData[2]));
        });
        this.professionalPunishments.getConfig().getStringList("presets.warning").forEach(entry -> {
            final String[] presetData = entry.split(":");
            this.warningPresets.add(new Reason(presetData[0], presetData[1], presetData[2]));
        });
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
                this.professionalPunishments.getBanService().cachedBans
                        .stream()
                        .filter(entry -> entry.getId().equals(punishment.getId()))
                        .peek(entry -> entry.setReason(reason))
                        .toList();
            }
            case PUNISHMENT_MUTE -> {
                this.professionalPunishments.getMuteService().cachedMutes
                        .stream()
                        .filter(entry -> entry.getId().equals(punishment.getId()))
                        .peek(entry -> entry.setReason(reason))
                        .toList();
            }
            case PUNISHMENT_WARNING -> {
                this.professionalPunishments.getWarningService().cachedWarnings
                        .stream()
                        .filter(entry -> entry.getId().equals(punishment.getId()))
                        .peek(entry -> entry.setReason(reason))
                        .toList();
            }
        }

        this.cachedLogs
                .stream()
                .filter(log -> log.getRelatedId().equals(punishment.getId()))
                .peek(log -> log.setReason(reason)).toList();

        this.professionalPunishments.getDataAccess().setPunishmentReason(punishment, reason);
        this.professionalPunishments.getServer().getPluginManager().callEvent(new PunishmentChangeEvent(punishment, reason, PunishmentChangeEvent.ChangeType.REASON));
    }

    public void setPunishmentEnding(final Punishment punishment, final int minutes) {
        final long expire = System.currentTimeMillis() + (minutes * 60000L);
        switch (punishment.getType()) {
            case PUNISHMENT_BAN -> {
                this.professionalPunishments.getBanService().cachedBans
                        .stream()
                        .filter(entry -> entry.getId().equals(punishment.getId()))
                        .peek(entry -> entry.setExpire(expire))
                        .toList();
            }
            case PUNISHMENT_MUTE -> {
                this.professionalPunishments.getMuteService().cachedMutes
                        .stream()
                        .filter(entry -> entry.getId().equals(punishment.getId()))
                        .peek(entry -> entry.setExpire(expire))
                        .toList();
            }
            case PUNISHMENT_WARNING -> {
                this.professionalPunishments.getWarningService().cachedWarnings
                        .stream()
                        .filter(entry -> entry.getId().equals(punishment.getId()))
                        .peek(entry -> entry.setExpire(expire))
                        .toList();
            }
        }

        this.professionalPunishments.getDataAccess().setPunishmentEnding(punishment, minutes);
        this.professionalPunishments.getServer().getPluginManager().callEvent(new PunishmentChangeEvent(punishment, expire, PunishmentChangeEvent.ChangeType.EXPIRATION));
    }

    public void getPunishment(final String id, final Consumer<Punishment> punishmentConsumer) {
        this.professionalPunishments.getDataAccess().getPunishment(id, punishmentConsumer::accept);
    }

    public void insertLog(final Punishment.Log log) {
        this.professionalPunishments.getDataAccess().insertLog(log);
        this.cachedLogs.add(log);

        this.professionalPunishments.getServer().getPluginManager().callEvent(new PunishmentLogInsertEvent(log));
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
        this.professionalPunishments.getDataAccess().deleteLogEntry(id);
        this.cachedLogs.removeIf(log -> log.getId().equals(id));

        this.professionalPunishments.getServer().getPluginManager().callEvent(new PunishmentLogDeleteEvent(id));
    }

}
