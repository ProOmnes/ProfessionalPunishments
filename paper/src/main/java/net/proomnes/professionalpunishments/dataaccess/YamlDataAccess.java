package net.proomnes.professionalpunishments.dataaccess;

import net.proomnes.configutils.Config;
import net.proomnes.professionalpunishments.ProfessionalPunishments;
import net.proomnes.professionalpunishments.objects.Punishment;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class YamlDataAccess implements IDataAccess {

    private final ProfessionalPunishments professionalPunishments;
    private final Config bans, mutes, warnings, logs;

    public YamlDataAccess(final ProfessionalPunishments professionalPunishments) {
        this.professionalPunishments = professionalPunishments;

        Config.saveResource("/data/bans.yml", professionalPunishments);
        Config.saveResource("/data/mutes.yml", professionalPunishments);
        Config.saveResource("/data/warnings.yml", professionalPunishments);
        Config.saveResource("/data/logs.yml", professionalPunishments);
        this.bans = new Config(professionalPunishments.getDataFolder() + "/data/bans.yml", Config.YAML);
        this.mutes = new Config(professionalPunishments.getDataFolder() + "/data/mutes.yml", Config.YAML);
        this.warnings = new Config(professionalPunishments.getDataFolder() + "/data/warnings.yml", Config.YAML);
        this.logs = new Config(professionalPunishments.getDataFolder() + "/data/logs.yml", Config.YAML);
    }

    /**
     * @param target    The player who will be banned
     * @param reason    The reason why the player has to be banned
     * @param initiator The player who initiated this action
     * @param minutes   The time, how long the player will be banned
     */
    @Override
    public void banPlayer(String target, String reason, String initiator, int minutes, Consumer<String> id) {
        final String generatedId = this.professionalPunishments.getRandomId(5, "B");
        final String date = this.professionalPunishments.getDate();
        final long expire = System.currentTimeMillis() + (minutes * 60000L);

        this.bans.set("ban." + generatedId + ".relatedId", "null");
        this.bans.set("ban." + generatedId + ".type", Punishment.Type.PUNISHMENT_BAN.name());
        this.bans.set("ban." + generatedId + ".target", target);
        this.bans.set("ban." + generatedId + ".reason", reason);
        this.bans.set("ban." + generatedId + ".initiator", initiator);
        this.bans.set("ban." + generatedId + ".date", date);
        this.bans.set("ban." + generatedId + ".expire", expire);
        this.bans.save();
        this.bans.reload();

        id.accept(generatedId);
    }

    /**
     * @param target The player who is probably banned
     * @param is     Consumer
     */
    @Override
    public void isBanned(String target, Consumer<Boolean> is) {
        final AtomicBoolean isBanned = new AtomicBoolean(false);

        this.bans.getSection("ban").getAll().getKeys(false).forEach(id -> {
            if (this.bans.getString("ban." + id + ".target").equals(target)) {
                isBanned.set(true);
            }
        });

        is.accept(isBanned.get());
    }

    /**
     * @param target    The player whose ban is to be revoked
     * @param initiator The player who initiated this action
     * @param reason    The reason why the ban is to be revoked
     */
    @Override
    public void unbanPlayer(String target, String initiator, String reason, Consumer<String> id) {
        this.getBan(target, punishment -> {
            // get active ban and inserting unban log
            final String generatedId = this.professionalPunishments.getRandomId(5, "UBL");

            // delete active ban
            final Map<String, Object> objectMap = this.bans.getSection("ban").getAllMap();
            objectMap.remove(punishment.getId());
            this.bans.set("ban", objectMap);
            this.bans.save();
            this.bans.reload();

            id.accept(generatedId);
        });
    }

    /**
     * @param target    The player who will be muted
     * @param reason    The reason why the player has to be muted
     * @param initiator The player who initiated this action
     * @param minutes   The time, how long the player will be muted
     */
    @Override
    public void mutePlayer(String target, String reason, String initiator, int minutes, Consumer<String> id) {
        final String generatedId = this.professionalPunishments.getRandomId(5, "M");
        final String date = this.professionalPunishments.getDate();
        final long expire = System.currentTimeMillis() + (minutes * 60000L);

        this.mutes.set("mute." + generatedId + ".relatedId", "null");
        this.mutes.set("mute." + generatedId + ".type", Punishment.Type.PUNISHMENT_MUTE.name());
        this.mutes.set("mute." + generatedId + ".target", target);
        this.mutes.set("mute." + generatedId + ".reason", reason);
        this.mutes.set("mute." + generatedId + ".initiator", initiator);
        this.mutes.set("mute." + generatedId + ".date", date);
        this.mutes.set("mute." + generatedId + ".expire", expire);
        this.mutes.save();
        this.mutes.reload();

        id.accept(generatedId);
    }

    /**
     * @param target The player who is probably muted
     * @param is     Consumer
     */
    @Override
    public void isMuted(String target, Consumer<Boolean> is) {
        final AtomicBoolean isMuted = new AtomicBoolean(false);

        this.mutes.getSection("mute").getAll().getKeys(false).forEach(id -> {
            if (this.mutes.getString("mute." + id + ".target").equals(target)) {
                isMuted.set(true);
            }
        });

        is.accept(isMuted.get());
    }

    /**
     * @param target    The player whose mute is to be revoked
     * @param initiator The player who initiated this action
     * @param reason    The reason why the mute is to be revoked
     */
    @Override
    public void unmutePlayer(String target, String initiator, String reason, Consumer<String> id) {
        this.getMute(target, punishment -> {
            final String generatedId = this.professionalPunishments.getRandomId(5, "UML");

            // delete active mute
            final Map<String, Object> objectMap = this.mutes.getSection("mute").getAllMap();
            objectMap.remove(punishment.getId());
            this.mutes.set("mute", objectMap);
            this.mutes.save();
            this.mutes.reload();

            id.accept(generatedId);
        });
    }

    /**
     * @param target    The player who will be warned
     * @param reason    The reason why the player has to be warned
     * @param initiator The player who initiated this action
     * @param minutes   The time, how long the player will be warned
     */
    @Override
    public void warnPlayer(String target, String reason, String initiator, int minutes, Consumer<String> id) {
        final String generatedId = this.professionalPunishments.getRandomId(5, "W");
        final String date = this.professionalPunishments.getDate();
        final long expire = System.currentTimeMillis() + (minutes * 60000L);

        this.warnings.set("warning." + generatedId + ".relatedId", "null");
        this.warnings.set("warning." + generatedId + ".type", Punishment.Type.PUNISHMENT_WARNING.name());
        this.warnings.set("warning." + generatedId + ".target", target);
        this.warnings.set("warning." + generatedId + ".reason", reason);
        this.warnings.set("warning." + generatedId + ".initiator", initiator);
        this.warnings.set("warning." + generatedId + ".date", date);
        this.warnings.set("warning." + generatedId + ".expire", expire);
        this.warnings.save();
        this.warnings.reload();

        id.accept(generatedId);
    }

    /**
     * @param target      Player, who is currently warned
     * @param punishments Consumer
     */
    @Override
    public void getActiveWarnings(String target, Consumer<Set<Punishment>> punishments) {
        final Set<Punishment> warnings = new HashSet<>();

        this.warnings.getSection("warning").getAll().getKeys(false).forEach(id -> {
            if (this.warnings.getString("warning." + id + ".target").equals(target)) {
                if (this.warnings.getLong("warning." + id + ".expire") > System.currentTimeMillis()) {
                    warnings.add(new Punishment(
                            id,
                            this.warnings.getString("warning." + id + ".relatedId"),
                            Punishment.Type.valueOf(this.warnings.getString("warning." + id + ".type")),
                            this.warnings.getString("warning." + id + ".target"),
                            this.warnings.getString("warning." + id + ".reason"),
                            this.warnings.getString("warning." + id + ".initiator"),
                            this.warnings.getString("warning." + id + ".date"),
                            this.warnings.getLong("warning." + id + ".expire")
                    ));
                }
            }
        });

        punishments.accept(warnings);
    }

    /**
     * @param punishmentConsumer Consumer
     */
    @Override
    public void getAllActiveWarnings(Consumer<Set<Punishment>> punishmentConsumer) {
        final Set<Punishment> warnings = new HashSet<>();

        this.warnings.getSection("warning").getAll().getKeys(false).forEach(id -> {
            if (this.warnings.getLong("warning." + id + ".expire") > System.currentTimeMillis()) {
                warnings.add(new Punishment(
                        id,
                        this.warnings.getString("warning." + id + ".relatedId"),
                        Punishment.Type.valueOf(this.warnings.getString("warning." + id + ".type")),
                        this.warnings.getString("warning." + id + ".target"),
                        this.warnings.getString("warning." + id + ".reason"),
                        this.warnings.getString("warning." + id + ".initiator"),
                        this.warnings.getString("warning." + id + ".date"),
                        this.warnings.getLong("warning." + id + ".expire")
                ));
            }
        });

        punishmentConsumer.accept(warnings);
    }

    /**
     * @param target    The player whose warning is to be revoked
     * @param warnId    The exact warning identification
     * @param initiator The player who initiated this action
     * @param reason    The reason why the warning is to be revoked
     */
    @Override
    public void unwarnPlayer(String target, String warnId, String initiator, String reason, Consumer<String> id) {
        final String generatedId = this.professionalPunishments.getRandomId(5, "UWL");

        // delete active warning
        final Map<String, Object> objectMap = this.warnings.getSection("warning").getAllMap();
        objectMap.remove(warnId);
        this.warnings.set("warning", objectMap);
        this.warnings.save();
        this.warnings.reload();

        id.accept(generatedId);
    }

    /**
     * @param id     The ID that is to be checked
     * @param type   The punishment type to be searched for
     * @param exists Consumer
     */
    @Override
    public void punishmentIdExists(String id, Punishment.Type type, Consumer<Boolean> exists) {
        switch (type) {
            case PUNISHMENT_BAN -> {
                exists.accept(this.bans.exists("ban." + id));
            }
            case PUNISHMENT_MUTE -> {
                exists.accept(this.mutes.exists("mute." + id));
            }
            case PUNISHMENT_WARNING -> {
                exists.accept(this.warnings.exists("warning." + id));
            }
            default -> exists.accept(false);
        }
    }

    /**
     * @param id     The ID that is to be checked
     * @param type   The punishment log type to be searched for
     * @param exists Consumer
     */
    @Override
    public void punishmentLogIdExists(String id, Punishment.LogType type, Consumer<Boolean> exists) {
        exists.accept(this.logs.getString("log." + id + ".logType").equals(type.name()));
    }

    /**
     * @param target             Player, who is currently banned
     * @param punishmentConsumer Consumer
     */
    @Override
    public void getBan(String target, Consumer<Punishment> punishmentConsumer) {
        this.bans.getSection("ban").getAll().getKeys(false).forEach(id -> {
            if (this.bans.getString("ban." + id + ".target").equals(target)) {
                punishmentConsumer.accept(new Punishment(
                        id,
                        this.bans.getString("ban." + id + ".relatedId"),
                        Punishment.Type.valueOf(this.bans.getString("ban." + id + ".type")),
                        this.bans.getString("ban." + id + ".target"),
                        this.bans.getString("ban." + id + ".reason"),
                        this.bans.getString("ban." + id + ".initiator"),
                        this.bans.getString("ban." + id + ".date"),
                        this.bans.getLong("ban." + id + ".expire")
                ));
            }
        });
    }

    /**
     * @param punishmentConsumer Consumer
     */
    @Override
    public void getAllBans(Consumer<Set<Punishment>> punishmentConsumer) {
        final Set<Punishment> punishments = new HashSet<>();

        this.bans.getSection("ban").getAll().getKeys(false).forEach(id -> {
            punishments.add(new Punishment(
                    id,
                    this.bans.getString("ban." + id + ".relatedId"),
                    Punishment.Type.valueOf(this.bans.getString("ban." + id + ".type")),
                    this.bans.getString("ban." + id + ".target"),
                    this.bans.getString("ban." + id + ".reason"),
                    this.bans.getString("ban." + id + ".initiator"),
                    this.bans.getString("ban." + id + ".date"),
                    this.bans.getLong("ban." + id + ".expire")
            ));
        });

        punishmentConsumer.accept(punishments);
    }

    /**
     * @param target             Player, who is currently muted
     * @param punishmentConsumer Consumer
     */
    @Override
    public void getMute(String target, Consumer<Punishment> punishmentConsumer) {
        this.mutes.getSection("mute").getAll().getKeys(false).forEach(id -> {
            if (this.mutes.getString("mute." + id + ".target").equals(target)) {
                punishmentConsumer.accept(new Punishment(
                        id,
                        this.mutes.getString("mute." + id + ".relatedId"),
                        Punishment.Type.valueOf(this.mutes.getString("mute." + id + ".type")),
                        this.mutes.getString("mute." + id + ".target"),
                        this.mutes.getString("mute." + id + ".reason"),
                        this.mutes.getString("mute." + id + ".initiator"),
                        this.mutes.getString("mute." + id + ".date"),
                        this.mutes.getLong("mute." + id + ".expire")
                ));
            }
        });
    }

    /**
     * @param punishmentConsumer Consumer
     */
    @Override
    public void getAllMutes(Consumer<Set<Punishment>> punishmentConsumer) {
        final Set<Punishment> punishments = new HashSet<>();

        this.mutes.getSection("mute").getAll().getKeys(false).forEach(id -> {
            punishments.add(new Punishment(
                    id,
                    this.mutes.getString("mute." + id + ".relatedId"),
                    Punishment.Type.valueOf(this.mutes.getString("mute." + id + ".type")),
                    this.mutes.getString("mute." + id + ".target"),
                    this.mutes.getString("mute." + id + ".reason"),
                    this.mutes.getString("mute." + id + ".initiator"),
                    this.mutes.getString("mute." + id + ".date"),
                    this.mutes.getLong("mute." + id + ".expire")
            ));
        });

        punishmentConsumer.accept(punishments);
    }

    /**
     * @param punishment Punishment that is to be changed
     * @param reason     New reason
     */
    @Override
    public void setPunishmentReason(Punishment punishment, String reason) {
        switch (punishment.getType()) {
            case PUNISHMENT_BAN -> {
                this.bans.set("ban." + punishment.getId() + ".reason", reason);
                this.bans.save();
                this.bans.reload();
            }
            case PUNISHMENT_MUTE -> {
                this.mutes.set("mute." + punishment.getId() + ".reason", reason);
                this.mutes.save();
                this.mutes.reload();
            }
            case PUNISHMENT_WARNING -> {
                this.warnings.set("warning." + punishment.getId() + ".reason", reason);
                this.warnings.save();
                this.warnings.reload();
            }
        }

        this.logs.getSection("log").getAll().getKeys().forEach(id -> {
            if (this.logs.getString("log." + id + ".relatedId").equals(punishment.getRelatedId())) {
                this.logs.set("log." + id + ".reason", reason);
                this.logs.save();
                this.logs.reload();
            }
        });
    }

    /**
     * @param punishment Punishment that is to be changed
     * @param minutes    New time
     */
    @Override
    public void setPunishmentEnding(Punishment punishment, int minutes) {
        switch (punishment.getType()) {
            case PUNISHMENT_BAN -> {
                this.bans.set("ban." + punishment.getId() + ".expire", System.currentTimeMillis() + (minutes * 60000L));
                this.bans.save();
                this.bans.reload();
            }
            case PUNISHMENT_MUTE -> {
                this.mutes.set("mute." + punishment.getId() + ".expire", System.currentTimeMillis() + (minutes * 60000L));
                this.mutes.save();
                this.mutes.reload();
            }
            case PUNISHMENT_WARNING -> {
                this.warnings.set("warning." + punishment.getId() + ".expire", System.currentTimeMillis() + (minutes * 60000L));
                this.warnings.save();
                this.warnings.reload();
            }
        }
    }

    /**
     * @param id                 The ID via which a punishment is to be identified
     * @param punishmentConsumer Consumer
     */
    @Override
    public void getPunishment(String id, Consumer<Punishment> punishmentConsumer) {
        if (this.bans.exists("ban." + id)) {
            punishmentConsumer.accept(new Punishment(
                    id,
                    this.bans.getString("ban." + id + ".relatedId"),
                    Punishment.Type.valueOf(this.bans.getString("ban." + id + ".type")),
                    this.bans.getString("ban." + id + ".target"),
                    this.bans.getString("ban." + id + ".reason"),
                    this.bans.getString("ban." + id + ".initiator"),
                    this.bans.getString("ban." + id + ".date"),
                    this.bans.getLong("ban." + id + ".expire")
            ));
        } else if (this.mutes.exists("mute." + id)) {
            punishmentConsumer.accept(new Punishment(
                    id,
                    this.mutes.getString("mute." + id + ".relatedId"),
                    Punishment.Type.valueOf(this.mutes.getString("mute." + id + ".type")),
                    this.mutes.getString("mute." + id + ".target"),
                    this.mutes.getString("mute." + id + ".reason"),
                    this.mutes.getString("mute." + id + ".initiator"),
                    this.mutes.getString("mute." + id + ".date"),
                    this.mutes.getLong("mute." + id + ".expire")
            ));
        } else if (this.warnings.exists("warning." + id)) {
            if (this.warnings.getLong("warning." + id + ".expire") > System.currentTimeMillis()) {
                punishmentConsumer.accept(new Punishment(
                        id,
                        this.warnings.getString("warning." + id + ".relatedId"),
                        Punishment.Type.valueOf(this.warnings.getString("warning." + id + ".type")),
                        this.warnings.getString("warning." + id + ".target"),
                        this.warnings.getString("warning." + id + ".reason"),
                        this.warnings.getString("warning." + id + ".initiator"),
                        this.warnings.getString("warning." + id + ".date"),
                        this.warnings.getLong("warning." + id + ".expire")
                ));
            } else punishmentConsumer.accept(null);
        } else punishmentConsumer.accept(null);
    }

    /**
     * @param log Punishment whose data is to be created as a log
     */
    @Override
    public void insertLog(Punishment.Log log) {
        this.logs.set("log." + log.getId() + ".relatedId", log.getRelatedId());
        this.logs.set("log." + log.getId() + ".logType", log.getLogType().name());
        this.logs.set("log." + log.getId() + ".target", log.getTarget());
        this.logs.set("log." + log.getId() + ".reason", log.getReason());
        this.logs.set("log." + log.getId() + ".initiator", log.getInitiator());
        this.logs.set("log." + log.getId() + ".date", log.getDate());
        this.logs.save();
        this.logs.reload();
    }

    /**
     * @param target             The player whose data should be printed
     * @param type               Type of punishment log
     * @param punishmentConsumer Consumer
     */
    @Override
    public void getLogs(String target, Punishment.LogType type, Consumer<Set<Punishment.Log>> punishmentConsumer) {
        final Set<Punishment.Log> logs = new HashSet<>();

        this.logs.getSection("log").getAll().getKeys(false).forEach(id -> {
            if (this.logs.getString("log." + id + ".target").equals(target)) {
                logs.add(new Punishment.Log(
                        id,
                        this.logs.getString("log." + id + ".relatedId"),
                        Punishment.LogType.valueOf(this.logs.getString("log." + id + ".logType")),
                        this.logs.getString("log." + id + ".target"),
                        this.logs.getString("log." + id + ".reason"),
                        this.logs.getString("log." + id + ".initiator"),
                        this.logs.getString("log." + id + ".date")
                ));
            }
        });

        punishmentConsumer.accept(logs);
    }

    /**
     * @param punishmentConsumer Consumer
     */
    @Override
    public void getAllLogs(Consumer<Set<Punishment.Log>> punishmentConsumer) {
        final Set<Punishment.Log> logs = new HashSet<>();

        this.logs.getSection("log").getAll().getKeys(false).forEach(id -> {
            logs.add(new Punishment.Log(
                    id,
                    this.logs.getString("log." + id + ".relatedId"),
                    Punishment.LogType.valueOf(this.logs.getString("log." + id + ".logType")),
                    this.logs.getString("log." + id + ".target"),
                    this.logs.getString("log." + id + ".reason"),
                    this.logs.getString("log." + id + ".initiator"),
                    this.logs.getString("log." + id + ".date")
            ));
        });

        punishmentConsumer.accept(logs);
    }

    /**
     * @param target The player whose data should be cleared
     * @param type   Type of punishment
     */
    @Override
    public void clearLogs(String target, Punishment.LogType type) {
        this.logs.getSection("log").getAll().getKeys(false).forEach(id -> {
            if (this.logs.getString("log." + id + ".target").equals(target)) {
                if (this.logs.getString("log." + id + ".logType").equals(type.name().toUpperCase())) {
                    this.deleteLogEntry(id);
                }
            }
        });
    }

    /**
     * @param id The ID via which a punishment log entry is to be deleted
     */
    @Override
    public void deleteLogEntry(String id) {
        final Map<String, Object> objectMap = this.logs.getSection("log").getAllMap();
        objectMap.remove(id);
        this.logs.set("log", objectMap);
        this.logs.save();
        this.logs.reload();
    }

    @Override
    public boolean connected() {
        return true;
    }
}
