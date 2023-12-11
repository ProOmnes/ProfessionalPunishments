package net.proomnes.professionalpunishments.dataaccess;

import net.proomnes.easysql.Column;
import net.proomnes.easysql.Document;
import net.proomnes.easysql.EasySQL;
import net.proomnes.professionalpunishments.ProfessionalPunishments;
import net.proomnes.professionalpunishments.objects.Punishment;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class MySQLDataAccess implements IDataAccess {

    private final ProfessionalPunishments professionalPunishments;

    private final EasySQL client;

    public MySQLDataAccess(final ProfessionalPunishments professionalPunishments) {
        this.professionalPunishments = professionalPunishments;

        this.client = new EasySQL(
                this.professionalPunishments.getConfig().getString("mysql.host"),
                this.professionalPunishments.getConfig().getString("mysql.port"),
                this.professionalPunishments.getConfig().getString("mysql.user"),
                this.professionalPunishments.getConfig().getString("mysql.password"),
                this.professionalPunishments.getConfig().getString("mysql.database")
        );

        this.client.createTable("bans", "id",
                new Column("id", Column.Type.VARCHAR, 7)
                        .append("relatedId", Column.Type.VARCHAR, 10)
                        .append("type", Column.Type.VARCHAR, 30)
                        .append("target", Column.Type.VARCHAR, 30)
                        .append("reason", Column.Type.VARCHAR, 150)
                        .append("initiator", Column.Type.VARCHAR, 30)
                        .append("date", Column.Type.VARCHAR, 30)
                        .append("expire", Column.Type.LONG)
        );

        this.client.createTable("mutes", "id",
                new Column("id", Column.Type.VARCHAR, 7)
                        .append("relatedId", Column.Type.VARCHAR, 10)
                        .append("type", Column.Type.VARCHAR, 30)
                        .append("target", Column.Type.VARCHAR, 30)
                        .append("reason", Column.Type.VARCHAR, 150)
                        .append("initiator", Column.Type.VARCHAR, 30)
                        .append("date", Column.Type.VARCHAR, 30)
                        .append("expire", Column.Type.LONG)
        );

        this.client.createTable("warnings", "id",
                new Column("id", Column.Type.VARCHAR, 7)
                        .append("relatedId", Column.Type.VARCHAR, 10)
                        .append("type", Column.Type.VARCHAR, 30)
                        .append("target", Column.Type.VARCHAR, 30)
                        .append("reason", Column.Type.VARCHAR, 150)
                        .append("initiator", Column.Type.VARCHAR, 30)
                        .append("date", Column.Type.VARCHAR, 30)
                        .append("expire", Column.Type.LONG)
        );

        this.client.createTable("logs", "id",
                new Column("id", Column.Type.VARCHAR, 7)
                        .append("relatedId", Column.Type.VARCHAR, 10)
                        .append("logType", Column.Type.VARCHAR, 30)
                        .append("target", Column.Type.VARCHAR, 30)
                        .append("reason", Column.Type.VARCHAR, 150)
                        .append("initiator", Column.Type.VARCHAR, 30)
                        .append("date", Column.Type.VARCHAR, 30)
        );

        professionalPunishments.getLogger().info("[MySQLClient] Connection established.");
    }

    /**
     * @param target    The player who will be banned
     * @param reason    The reason why the player has to be banned
     * @param initiator The player who initiated this action
     * @param minutes   The time, how long the player will be banned
     */
    @Override
    public void banPlayer(String target, String reason, String initiator, int minutes, Consumer<String> id) {
        CompletableFuture.runAsync(() -> {
            final String generatedId = this.professionalPunishments.getRandomId(5, "B");
            final String date = this.professionalPunishments.getDate();
            final long expire = System.currentTimeMillis() + (minutes * 60000L);

            // inserting ban
            this.client.insert("bans",
                    new Document(
                            "id", generatedId
                    ).append(
                            "relatedId", "null"
                    ).append(
                            "type", Punishment.Type.PUNISHMENT_BAN.name()
                    ).append(
                            "target", target
                    ).append(
                            "reason", reason
                    ).append(
                            "initiator", initiator
                    ).append(
                            "date", date
                    ).append(
                            "expire", expire
                    )
            );

            // inserting ban log
            this.insertLog(new Punishment.Log(this.professionalPunishments.getRandomId(5, "BL"), generatedId, Punishment.LogType.LOG_BAN, target, reason, initiator, date));

            id.accept(generatedId);
        });
    }

    /**
     * @param target The player who is probably banned
     * @param is     Consumer
     */
    @Override
    public void isBanned(String target, Consumer<Boolean> is) {
        CompletableFuture.runAsync(() -> {
            final Document document = this.client.find("bans", "target", target).first();
            is.accept(document != null);
        });
    }

    /**
     * @param target    The player whose ban is to be revoked
     * @param initiator The player who initiated this action
     * @param reason    The reason why the ban is to be revoked
     */
    @Override
    public void unbanPlayer(String target, String initiator, String reason, Consumer<String> id) {
        CompletableFuture.runAsync(() -> {
            this.getBan(target, punishment -> {
                // get active ban and inserting unban log
                final String generatedId = this.professionalPunishments.getRandomId(5, "UBL");
                final String date = this.professionalPunishments.getDate();
                this.insertLog(new Punishment.Log(generatedId, punishment.getId(), Punishment.LogType.LOG_UNBAN, target, reason, initiator, date));

                // delete active ban
                this.client.delete("bans", "id", punishment.getId());

                id.accept(generatedId);
            });
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
        CompletableFuture.runAsync(() -> {
            final String generatedId = this.professionalPunishments.getRandomId(5, "M");
            final String date = this.professionalPunishments.getDate();
            final long expire = System.currentTimeMillis() + (minutes * 60000L);

            // inserting mute
            this.client.insert("mutes",
                    new Document(
                            "id", generatedId
                    ).append(
                            "relatedId", "null"
                    ).append(
                            "type", Punishment.Type.PUNISHMENT_MUTE.name()
                    ).append(
                            "target", target
                    ).append(
                            "reason", reason
                    ).append(
                            "initiator", initiator
                    ).append(
                            "date", date
                    ).append(
                            "expire", expire
                    )
            );

            // inserting mute log
            this.insertLog(new Punishment.Log(this.professionalPunishments.getRandomId(5, "ML"), generatedId, Punishment.LogType.LOG_MUTE, target, reason, initiator, date));

            id.accept(generatedId);
        });
    }

    /**
     * @param target The player who is probably muted
     * @param is     Consumer
     */
    @Override
    public void isMuted(String target, Consumer<Boolean> is) {
        CompletableFuture.runAsync(() -> {
            final Document document = this.client.find("mutes", "target", target).first();
            is.accept(document != null);
        });
    }

    /**
     * @param target    The player whose mute is to be revoked
     * @param initiator The player who initiated this action
     * @param reason    The reason why the mute is to be revoked
     */
    @Override
    public void unmutePlayer(String target, String initiator, String reason, Consumer<String> id) {
        CompletableFuture.runAsync(() -> {
            this.getMute(target, punishment -> {
                // get active mute and inserting unmute log
                final String generatedId = this.professionalPunishments.getRandomId(5, "UML");
                final String date = this.professionalPunishments.getDate();
                this.insertLog(new Punishment.Log(generatedId, punishment.getId(), Punishment.LogType.LOG_UNMUTE, target, reason, initiator, date));

                // delete active mute
                this.client.delete("mutes", "id", punishment.getId());

                id.accept(generatedId);
            });
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
        CompletableFuture.runAsync(() -> {
            final String generatedId = this.professionalPunishments.getRandomId(5, "W");
            final String date = this.professionalPunishments.getDate();
            final long expire = System.currentTimeMillis() + (minutes * 60000L);

            // inserting warning
            this.client.insert("warnings",
                    new Document(
                            "id", generatedId
                    ).append(
                            "relatedId", "null"
                    ).append(
                            "type", Punishment.Type.PUNISHMENT_WARNING.name()
                    ).append(
                            "target", target
                    ).append(
                            "reason", reason
                    ).append(
                            "initiator", initiator
                    ).append(
                            "date", date
                    ).append(
                            "expire", expire
                    )
            );

            // inserting warning log
            this.insertLog(new Punishment.Log(this.professionalPunishments.getRandomId(5, "WL"), generatedId, Punishment.LogType.LOG_WARNING, target, reason, initiator, date));

            id.accept(generatedId);
        });
    }

    /**
     * @param target      Player, who is currently warned
     * @param punishments Consumer
     */
    @Override
    public void getActiveWarnings(String target, Consumer<Set<Punishment>> punishments) {
        CompletableFuture.runAsync(() -> {
            final Set<Punishment> warnings = new HashSet<>();

            // get all warnings
            for (final Document document : this.client.find("warnings", "target", target).results()) {
                final Punishment entry = new Punishment(
                        document.getString("id"),
                        document.getString("relatedId"),
                        Punishment.Type.valueOf(document.getString("type").toUpperCase()),
                        document.getString("target"),
                        document.getString("reason"),
                        document.getString("initiator"),
                        document.getString("date"),
                        document.getLong("expire")
                );
                warnings.add(entry);
            }

            punishments.accept(warnings);
        });
    }

    /**
     * @param punishmentConsumer Consumer
     */
    @Override
    public void getAllActiveWarnings(Consumer<Set<Punishment>> punishmentConsumer) {
        CompletableFuture.runAsync(() -> {
            final Set<Punishment> punishments = new HashSet<>();

            for (final Document document : this.client.find("warnings").results()) {
                punishments.add(new Punishment(
                        document.getString("id"),
                        document.getString("relatedId"),
                        Punishment.Type.valueOf(document.getString("type").toUpperCase()),
                        document.getString("target"),
                        document.getString("reason"),
                        document.getString("initiator"),
                        document.getString("date"),
                        document.getLong("expire")
                ));
            }

            punishmentConsumer.accept(punishments);
        });
    }

    /**
     * @param target    The player whose warning is to be revoked
     * @param warnId    The exact warning identification
     * @param initiator The player who initiated this action
     * @param reason    The reason why the warning is to be revoked
     */
    @Override
    public void unwarnPlayer(String target, String warnId, String initiator, String reason, Consumer<String> id) {
        CompletableFuture.runAsync(() -> {
            this.getPunishment(warnId, punishment -> {
                // get active mute and inserting unwarn log
                final String generatedId = this.professionalPunishments.getRandomId(5, "UWL");
                final String date = this.professionalPunishments.getDate();
                this.insertLog(new Punishment.Log(generatedId, punishment.getId(), Punishment.LogType.LOG_WARNING, target, reason, initiator, date));

                // delete active warning
                this.client.delete("warnings", "id", punishment.getId());

                id.accept(generatedId);
            });
        });
    }

    /**
     * @param id     The ID that is to be checked
     * @param type   The punishment type to be searched for
     * @param exists Consumer
     */
    @Override
    public void punishmentIdExists(String id, Punishment.Type type, Consumer<Boolean> exists) {
        CompletableFuture.runAsync(() -> {
            boolean is = false;

            switch (type) {
                case PUNISHMENT_BAN -> {
                    is = this.client.find("bans", "id", id).first() != null;
                    break;
                }
                case PUNISHMENT_MUTE -> {
                    is = this.client.find("mutes", "id", id).first() != null;
                    break;
                }
                case PUNISHMENT_WARNING -> {
                    is = this.client.find("warnings", "id", id).first() != null;
                    break;
                }
            }

            exists.accept(is);
        });
    }

    /**
     * @param id     The ID that is to be checked
     * @param type   The punishment log type to be searched for
     * @param exists Consumer
     */
    @Override
    public void punishmentLogIdExists(String id, Punishment.LogType type, Consumer<Boolean> exists) {
        CompletableFuture.runAsync(() -> {
            boolean is = false;

            is = this.client.find("logs", "id", id).first() != null;

            exists.accept(is);
        });
    }

    /**
     * @param target             Player, who is currently banned
     * @param punishmentConsumer Consumer
     */
    @Override
    public void getBan(String target, Consumer<Punishment> punishmentConsumer) {
        CompletableFuture.runAsync(() -> {
            Punishment punishment = null;

            final Document document = this.client.find("bans", "target", target).first();
            if (document != null) {
                punishment = new Punishment(
                        document.getString("id"),
                        document.getString("relatedId"),
                        Punishment.Type.valueOf(document.getString("type").toUpperCase()),
                        document.getString("target"),
                        document.getString("reason"),
                        document.getString("initiator"),
                        document.getString("date"),
                        document.getLong("expire")
                );
            }

            punishmentConsumer.accept(punishment);
        });
    }

    /**
     * @param punishmentConsumer Consumer
     */
    @Override
    public void getAllBans(Consumer<Set<Punishment>> punishmentConsumer) {
        CompletableFuture.runAsync(() -> {
            final Set<Punishment> punishments = new HashSet<>();

            for (final Document document : this.client.find("bans").results()) {
                punishments.add(new Punishment(
                        document.getString("id"),
                        document.getString("relatedId"),
                        Punishment.Type.valueOf(document.getString("type").toUpperCase()),
                        document.getString("target"),
                        document.getString("reason"),
                        document.getString("initiator"),
                        document.getString("date"),
                        document.getLong("expire")
                ));
            }

            punishmentConsumer.accept(punishments);
        });
    }

    /**
     * @param target             Player, who is currently muted
     * @param punishmentConsumer Consumer
     */
    @Override
    public void getMute(String target, Consumer<Punishment> punishmentConsumer) {
        CompletableFuture.runAsync(() -> {
            Punishment punishment = null;

            final Document document = this.client.find("mutes", "target", target).first();
            if (document != null) {
                punishment = new Punishment(
                        document.getString("id"),
                        document.getString("relatedId"),
                        Punishment.Type.valueOf(document.getString("type").toUpperCase()),
                        document.getString("target"),
                        document.getString("reason"),
                        document.getString("initiator"),
                        document.getString("date"),
                        document.getLong("expire")
                );
            }

            punishmentConsumer.accept(punishment);
        });
    }

    /**
     * @param punishmentConsumer Consumer
     */
    @Override
    public void getAllMutes(Consumer<Set<Punishment>> punishmentConsumer) {
        CompletableFuture.runAsync(() -> {
            final Set<Punishment> punishments = new HashSet<>();

            for (final Document document : this.client.find("mutes").results()) {
                punishments.add(new Punishment(
                        document.getString("id"),
                        document.getString("relatedId"),
                        Punishment.Type.valueOf(document.getString("type").toUpperCase()),
                        document.getString("target"),
                        document.getString("reason"),
                        document.getString("initiator"),
                        document.getString("date"),
                        document.getLong("expire")
                ));
            }

            punishmentConsumer.accept(punishments);
        });
    }

    /**
     * @param punishment Punishment that is to be changed
     * @param reason     New reason
     */
    @Override
    public void setPunishmentReason(Punishment punishment, String reason) {
        CompletableFuture.runAsync(() -> {
            String table = "";

            switch (punishment.getType()) {
                case PUNISHMENT_BAN -> table = "bans";
                case PUNISHMENT_MUTE -> table = "mutes";
                case PUNISHMENT_WARNING -> table = "warnings";
            }

            // update active punishment
            this.client.update(table, new Document("id", punishment.getId()), new Document("reason", reason));

            // update log entry
            this.client.update("logs", new Document("relatedId", punishment.getId()), new Document("reason", reason));
        });
    }

    /**
     * @param punishment Punishment that is to be changed
     * @param minutes    New time
     */
    @Override
    public void setPunishmentEnding(Punishment punishment, int minutes) {
        CompletableFuture.runAsync(() -> {
            String table = "";

            switch (punishment.getType()) {
                case PUNISHMENT_BAN -> table = "bans";
                case PUNISHMENT_MUTE -> table = "mutes";
                case PUNISHMENT_WARNING -> table = "warnings";
            }

            // update active punishment
            this.client.update(table, new Document("id", punishment.getId()), new Document("expire", System.currentTimeMillis() + (minutes * 60000L)));
        });
    }

    /**
     * @param id                 The ID via which a punishment is to be identified
     * @param punishmentConsumer Consumer
     */
    @Override
    public void getPunishment(String id, Consumer<Punishment> punishmentConsumer) {
        CompletableFuture.runAsync(() -> {
            final Document ban = this.client.find("bans", "id", id).first();
            if (ban == null) {
                final Document mute = this.client.find("mutes", "id", id).first();
                if (mute == null) {
                    final Document warning = this.client.find("warnings", "id", id).first();
                    if (warning == null) {
                        punishmentConsumer.accept(null);
                    } else {
                        punishmentConsumer.accept(new Punishment(
                                warning.getString("id"),
                                warning.getString("relatedId"),
                                Punishment.Type.valueOf(warning.getString("type")),
                                warning.getString("target"),
                                warning.getString("reason"),
                                warning.getString("initiator"),
                                warning.getString("date"),
                                warning.getLong("expire")
                        ));
                    }
                } else {
                    punishmentConsumer.accept(new Punishment(
                            mute.getString("id"),
                            mute.getString("relatedId"),
                            Punishment.Type.valueOf(mute.getString("type")),
                            mute.getString("target"),
                            mute.getString("reason"),
                            mute.getString("initiator"),
                            mute.getString("date"),
                            mute.getLong("expire")
                    ));
                }
            } else {
                punishmentConsumer.accept(new Punishment(
                        ban.getString("id"),
                        ban.getString("relatedId"),
                        Punishment.Type.valueOf(ban.getString("type")),
                        ban.getString("target"),
                        ban.getString("reason"),
                        ban.getString("initiator"),
                        ban.getString("date"),
                        ban.getLong("expire")
                ));
            }
        });
    }

    /**
     * @param log Punishment whose data is to be created as a log
     */
    @Override
    public void insertLog(Punishment.Log log) {
        CompletableFuture.runAsync(() -> {
            final Document document = new Document(
                    "id", log.getId()
            ).append(
                    "relatedId", log.getRelatedId()
            ).append(
                    "logType", log.getLogType().name()
            ).append(
                    "target", log.getTarget()
            ).append(
                    "reason", log.getReason()
            ).append(
                    "initiator", log.getInitiator()
            ).append(
                    "date", log.getDate()
            );

            this.client.insert("logs", document);
        });
    }

    /**
     * @param target             The player whose data should be printed
     * @param type               Type of punishment log
     * @param punishmentConsumer Consumer
     */
    @Override
    public void getLogs(String target, Punishment.LogType type, Consumer<Set<Punishment.Log>> punishmentConsumer) {
        CompletableFuture.runAsync(() -> {
            final Set<Punishment.Log> logs = new HashSet<>();

            for (final Document document : this.client.find("logs", "target", target).results()) {
                if (document.getString("logType").equals(type.name())) {
                    logs.add(new Punishment.Log(
                            document.getString("id"),
                            document.getString("relatedId"),
                            Punishment.LogType.valueOf(document.getString("logType").toUpperCase()),
                            document.getString("target"),
                            document.getString("reason"),
                            document.getString("initiator"),
                            document.getString("date")
                    ));
                }
            }

            punishmentConsumer.accept(logs);
        });
    }

    /**
     * @param punishmentConsumer Consumer
     */
    @Override
    public void getAllLogs(Consumer<Set<Punishment.Log>> punishmentConsumer) {
        CompletableFuture.runAsync(() -> {
            final Set<Punishment.Log> logs = new HashSet<>();

            for (final Document document : this.client.find("logs").results()) {
                logs.add(new Punishment.Log(
                        document.getString("id"),
                        document.getString("relatedId"),
                        Punishment.LogType.valueOf(document.getString("logType").toUpperCase()),
                        document.getString("target"),
                        document.getString("reason"),
                        document.getString("initiator"),
                        document.getString("date")
                ));
            }

            punishmentConsumer.accept(logs);
        });
    }

    /**
     * @param target The player whose data should be cleared
     * @param type   Type of punishment
     */
    @Override
    public void clearLogs(String target, Punishment.LogType type) {
        CompletableFuture.runAsync(() -> {
            for (final Document document : this.client.find("logs", "target", target).results()) {
                if (document.getString("logType").equals(type.name())) {
                    this.deleteLogEntry(document.getString("id"));
                }
            }
        });
    }

    /**
     * @param id The ID via which a punishment log entry is to be deleted
     */
    @Override
    public void deleteLogEntry(String id) {
        CompletableFuture.runAsync(() -> {
            this.client.delete("logs", new Document("id", id));
        });
    }
}
