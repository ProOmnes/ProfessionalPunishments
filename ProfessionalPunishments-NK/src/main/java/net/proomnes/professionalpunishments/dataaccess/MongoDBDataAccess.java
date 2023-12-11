package net.proomnes.professionalpunishments.dataaccess;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import net.proomnes.professionalpunishments.ProfessionalPunishments;
import net.proomnes.professionalpunishments.objects.Punishment;
import org.bson.Document;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class MongoDBDataAccess implements IDataAccess {

    private final ProfessionalPunishments professionalPunishments;
    private MongoClient mongoClient;
    private MongoCollection<Document> banCollection, muteCollection, warningCollection, logCollection;

    public MongoDBDataAccess(final ProfessionalPunishments professionalPunishments) {
        this.professionalPunishments = professionalPunishments;
        CompletableFuture.runAsync(() -> {
            // connect MongoDB database with provided information
            final MongoClientURI mongoClientURI = new MongoClientURI(professionalPunishments.getConfig().getString("mongodb.uri"));
            this.mongoClient = new MongoClient(mongoClientURI);
            final MongoDatabase punishmentDatabase = mongoClient.getDatabase(professionalPunishments.getConfig().getString("mongodb.database"));

            // collections where all punishment data will be stored
            this.banCollection = punishmentDatabase.getCollection("bans");
            this.muteCollection = punishmentDatabase.getCollection("mutes");
            this.warningCollection = punishmentDatabase.getCollection("warnings");
            this.logCollection = punishmentDatabase.getCollection("logs");

            professionalPunishments.getLogger().info("[MongoDB] Connection established.");
        });
    }

    /**
     * @param target    The player who will be banned
     * @param reason    The reason why the player has to be banned
     * @param initiator The player who initiated this action
     * @param minutes   The time, how long the player will be banned
     */
    @Override
    public void banPlayer(String target, String reason, String initiator, int minutes) {
        CompletableFuture.runAsync(() -> {
            // inserting new ban
            final String generatedId = this.professionalPunishments.getRandomId(5, "B");
            final String date = this.professionalPunishments.getDate();
            final long expire = System.currentTimeMillis() + (minutes * 60000L);

            final Document document = new Document(
                    "_id", generatedId
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
            );

            this.banCollection.insertOne(document);

            // inserting ban log
            this.insertLog(new Punishment.Log(this.professionalPunishments.getRandomId(5, "BL"), generatedId, Punishment.LogType.LOG_BAN, target, reason, initiator, date));
        });
    }

    /**
     * @param target The player who is probably banned
     * @param is     Consumer
     */
    @Override
    public void isBanned(String target, Consumer<Boolean> is) {
        CompletableFuture.runAsync(() -> {
            final Document document = this.banCollection.find(new Document("target", target)).first();
            is.accept(document != null);
        });
    }

    /**
     * @param target    The player whose ban is to be revoked
     * @param initiator The player who initiated this action
     * @param reason    The reason why the ban is to be revoked
     */
    @Override
    public void unbanPlayer(String target, String initiator, String reason) {
        CompletableFuture.runAsync(() -> {
            this.getBan(target, punishment -> {
                // get active ban and inserting unban log
                final String generatedId = this.professionalPunishments.getRandomId(5, "UBL");
                final String date = this.professionalPunishments.getDate();
                this.insertLog(new Punishment.Log(generatedId, punishment.getId(), Punishment.LogType.LOG_UNBAN, target, reason, initiator, date));

                // delete active ban
                this.banCollection.deleteOne(new Document("_id", punishment.getId()));
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
    public void mutePlayer(String target, String reason, String initiator, int minutes) {
        CompletableFuture.runAsync(() -> {
            // inserting new ban
            final String generatedId = this.professionalPunishments.getRandomId(5, "M");
            final String date = this.professionalPunishments.getDate();
            final long expire = System.currentTimeMillis() + (minutes * 60000L);

            final Document document = new Document(
                    "_id", generatedId
            ).append(
                    "relatedId", "null"
            ).append(
                    "target", target
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
            );

            this.muteCollection.insertOne(document);

            // inserting mute log
            this.insertLog(new Punishment.Log(this.professionalPunishments.getRandomId(5, "ML"), generatedId, Punishment.LogType.LOG_MUTE, target, reason, initiator, date));
        });
    }

    /**
     * @param target The player who is probably muted
     * @param is     Consumer
     */
    @Override
    public void isMuted(String target, Consumer<Boolean> is) {
        CompletableFuture.runAsync(() -> {
            final Document document = this.muteCollection.find(new Document("target", target)).first();
            is.accept(document != null);
        });
    }

    /**
     * @param target    The player whose mute is to be revoked
     * @param initiator The player who initiated this action
     * @param reason    The reason why the mute is to be revoked
     */
    @Override
    public void unmutePlayer(String target, String initiator, String reason) {
        CompletableFuture.runAsync(() -> {
            this.getMute(target, punishment -> {
                // get active ban and inserting unban log
                final String generatedId = this.professionalPunishments.getRandomId(5, "UML");
                final String date = this.professionalPunishments.getDate();
                this.insertLog(new Punishment.Log(generatedId, punishment.getId(), Punishment.LogType.LOG_UNMUTE, target, reason, initiator, date));

                // delete active ban
                this.muteCollection.deleteOne(new Document("_id", punishment.getId()));
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
    public void warnPlayer(String target, String reason, String initiator, int minutes) {
        CompletableFuture.runAsync(() -> {
            // inserting new warning
            final String generatedId = this.professionalPunishments.getRandomId(5, "W");
            final String date = this.professionalPunishments.getDate();
            final long expire = System.currentTimeMillis() + (minutes * 60000L);

            final Document document = new Document(
                    "_id", generatedId
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
            );

            this.warningCollection.insertOne(document);

            // inserting warning log
            this.insertLog(new Punishment.Log(this.professionalPunishments.getRandomId(5, "WL"), generatedId, Punishment.LogType.LOG_WARNING, target, reason, initiator, date));
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
            for (final Document document : this.warningCollection.find(new Document("target", target))) {
                final Punishment entry = new Punishment(
                        document.getString("_id"),
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

            for (final Document document : this.warningCollection.find()) {
                punishments.add(new Punishment(
                        document.getString("_id"),
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
    public void unwarnPlayer(String target, String warnId, String initiator, String reason) {
        CompletableFuture.runAsync(() -> {
            this.getPunishment(warnId, punishment -> {
                // get active warning and inserting unwarn log
                final String generatedId = this.professionalPunishments.getRandomId(5, "UWL");
                final String date = this.professionalPunishments.getDate();
                this.insertLog(new Punishment.Log(generatedId, punishment.getId(), Punishment.LogType.LOG_WARNING, target, reason, initiator, date));

                // delete active warning
                this.muteCollection.deleteOne(new Document("_id", punishment.getId()));
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
                    is = this.banCollection.find(new Document("_id", id)).first() != null;
                    break;
                }
                case PUNISHMENT_MUTE -> {
                    is = this.muteCollection.find(new Document("_id", id)).first() != null;
                    break;
                }
                case PUNISHMENT_WARNING -> {
                    is = this.warningCollection.find(new Document("_id", id)).first() != null;
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

            is = this.logCollection.find(new Document("_id", id).append("type", type.name())).first() != null;

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

            final Document document = this.banCollection.find(new Document("target", target)).first();
            if (document != null) {
                punishment = new Punishment(
                        document.getString("_id"),
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

            for (final Document document : this.banCollection.find()) {
                punishments.add(new Punishment(
                        document.getString("_id"),
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

            final Document document = this.muteCollection.find(new Document("target", target)).first();
            if (document != null) {
                punishment = new Punishment(
                        document.getString("_id"),
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

            for (final Document document : this.muteCollection.find()) {
                punishments.add(new Punishment(
                        document.getString("_id"),
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
            MongoCollection<Document> tempCollection = null;

            switch (punishment.getType()) {
                case PUNISHMENT_BAN -> tempCollection = this.banCollection;
                case PUNISHMENT_MUTE -> tempCollection = this.muteCollection;
                case PUNISHMENT_WARNING -> tempCollection = this.warningCollection;
            }

            // update active punishment
            tempCollection.updateOne(
                    Objects.requireNonNull(tempCollection.find(new Document("_id", punishment.getId())).first()),
                    new Document("$set", new Document("reason", reason))
            );

            // update punishment log entry
            this.logCollection.updateOne(
                    Objects.requireNonNull(this.logCollection.find(new Document("relatedId", punishment.getId())).first()),
                    new Document("$set", new Document("reason", reason))
            );
        });
    }

    /**
     * @param punishment Punishment that is to be changed
     * @param minutes    New time
     */
    @Override
    public void setPunishmentEnding(Punishment punishment, int minutes) {
        CompletableFuture.runAsync(() -> {
            MongoCollection<Document> tempCollection = null;

            switch (punishment.getType()) {
                case PUNISHMENT_BAN -> tempCollection = this.banCollection;
                case PUNISHMENT_MUTE -> tempCollection = this.muteCollection;
                case PUNISHMENT_WARNING -> tempCollection = this.warningCollection;
            }

            // update active punishment
            tempCollection.updateOne(
                    Objects.requireNonNull(tempCollection.find(new Document("_id", punishment.getId())).first()),
                    new Document("$set", new Document("expire", System.currentTimeMillis() + (minutes * 60000L)))
            );
        });
    }

    /**
     * @param id                 The ID via which a punishment is to be identified
     * @param punishmentConsumer Consumer
     */
    @Override
    public void getPunishment(String id, Consumer<Punishment> punishmentConsumer) {
        CompletableFuture.runAsync(() -> {
            final Document ban = this.banCollection.find(new Document("_id", id)).first();
            if (ban == null) {
                final Document mute = this.muteCollection.find(new Document("_id", id)).first();
                if (mute == null) {
                    final Document warning = this.warningCollection.find(new Document("_id", id)).first();
                    if (warning == null) {
                        punishmentConsumer.accept(null);
                    } else {
                        punishmentConsumer.accept(new Punishment(
                                warning.getString("_id"),
                                warning.getString("relatedId"),
                                Punishment.Type.valueOf(warning.getString("type").toUpperCase()),
                                warning.getString("target"),
                                warning.getString("reason"),
                                warning.getString("initiator"),
                                warning.getString("date"),
                                warning.getLong("expire")
                        ));
                    }
                } else {
                    punishmentConsumer.accept(new Punishment(
                            mute.getString("_id"),
                            mute.getString("relatedId"),
                            Punishment.Type.valueOf(mute.getString("type").toUpperCase()),
                            mute.getString("target"),
                            mute.getString("reason"),
                            mute.getString("initiator"),
                            mute.getString("date"),
                            mute.getLong("expire")
                    ));
                }
            } else {
                punishmentConsumer.accept(new Punishment(
                        ban.getString("_id"),
                        ban.getString("relatedId"),
                        Punishment.Type.valueOf(ban.getString("type").toUpperCase()),
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
                    "_id", log.getId()
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

            this.logCollection.insertOne(document);
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

            for (final Document document : this.logCollection.find(new Document("target", target).append("logType", type.name()))) {
                logs.add(new Punishment.Log(
                        document.getString("_id"),
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
     * @param punishmentConsumer Consumer
     */
    @Override
    public void getAllLogs(Consumer<Set<Punishment.Log>> punishmentConsumer) {
        CompletableFuture.runAsync(() -> {
            final Set<Punishment.Log> logs = new HashSet<>();

            for (final Document document : this.logCollection.find()) {
                logs.add(new Punishment.Log(
                        document.getString("_id"),
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
            for (final Document document : this.logCollection.find(new Document("logType", type.name()).append("target", target))) {
                this.logCollection.deleteOne(document);
            }
        });
    }

    /**
     * @param id The ID via which a punishment log entry is to be deleted
     */
    @Override
    public void deleteLogEntry(String id) {
        CompletableFuture.runAsync(() -> {
            this.logCollection.deleteOne(new Document("_id", id));
        });
    }

}