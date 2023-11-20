package net.proomnes.professionalpunishments.dataaccess;

import net.proomnes.professionalpunishments.objects.Punishment;

import java.util.Set;
import java.util.function.Consumer;

public interface IDataAccess {

    /**
     * @param target The player who will be banned
     * @param reason The reason why the player has to be banned
     * @param initiator The player who initiated this action
     * @param minutes The time, how long the player will be banned
     */
    void banPlayer(final String target, final String reason, final String initiator, final int minutes);

    /**
     * @param target The player who is probably banned
     * @param is Consumer
     */
    void isBanned(final String target, final Consumer<Boolean> is);

    /**
     * @param target The player whose ban is to be revoked
     * @param initiator The player who initiated this action
     * @param reason The reason why the ban is to be revoked
     */
    void unbanPlayer(final String target, final String initiator, final String reason);

    /**
     * @param target The player who will be muted
     * @param reason The reason why the player has to be muted
     * @param initiator The player who initiated this action
     * @param minutes The time, how long the player will be muted
     */
    void mutePlayer(final String target, final String reason, final String initiator, final int minutes);

    /**
     * @param target The player who is probably muted
     * @param is Consumer
     */
    void isMuted(final String target, final Consumer<Boolean> is);

    /**
     * @param target The player whose mute is to be revoked
     * @param initiator The player who initiated this action
     * @param reason The reason why the mute is to be revoked
     */
    void unmutePlayer(final String target, final String initiator, final String reason);

    /**
     * @param id The ID that is to be checked
     * @param type The punishment type to be searched for
     * @param searchInHistory Search in logs
     * @param exists Consumer
     */
    void punishmentIdExists(final String id, final Punishment.Type type, final boolean searchInHistory, final Consumer<Boolean> exists);

    /**
     * @param target Player, who is currently banned
     * @param punishmentConsumer Consumer
     */
    void getBan(final String target, final Consumer<Punishment> punishmentConsumer);

    /**
     * @param target Player to whom the logs are to be queried
     * @param punishmentConsumer Consumer
     */
    void getBanLogs(final String target, final Consumer<Set<Punishment>> punishmentConsumer);

    /**
     * @param target Player, who is currently muted
     * @param punishmentConsumer Consumer
     */
    void getMute(final String target, final Consumer<Punishment> punishmentConsumer);

    /**
     * @param target Player to whom the logs are to be queried
     * @param punishmentConsumer Consumer
     */
    void getMuteLogs(final String target, final Consumer<Set<Punishment>> punishmentConsumer);

    /**
     * @param target Player to whom the logs are to be queried
     * @param warnings Consumer
     */
    void getWarnings(final String target, final Consumer<Set<Punishment>> warnings);

    /**
     * @param punishment Punishment that is to be changed
     * @param reason New reason
     */
    void setPunishmentReason(final Punishment punishment, final String reason);

    /**
     * @param punishment Punishment that is to be changed
     * @param minutes New time
     */
    void setPunishmentEnding(final Punishment punishment, final int minutes);

    /**
     * @param id The ID via which a punishment is to be identified
     * @param punishmentConsumer Consumer
     */
    void getPunishment(final String id, final Consumer<Punishment> punishmentConsumer);

    /**
     * @param punishment Punishment whose data is to be created as a log
     */
    void insertLog(final Punishment punishment);

    /**
     * @param target The player whose data should be cleared
     * @param type Type of punishment
     */
    void clearLogs(final String target, final Punishment.Type type);

    /**
     * @param id The ID via which a punishment is to be deleted
     */
    void deleteEntry(final String id);

}
