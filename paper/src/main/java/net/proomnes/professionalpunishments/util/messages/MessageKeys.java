package net.proomnes.professionalpunishments.util.messages;

import lombok.Getter;

@Getter
public enum MessageKeys {

    /*
      SYSTEM MESSAGES
     */
    SYSTEM_PREFIX("system.prefix", "§8» §bPunishments §8| §7", false),
    SYSTEM_PLAYER_BANNED("system.player.banned", "", false),
    SYSTEM_PLAYER_MUTED("system.player.muted", "", false),

    SYSTEM_TIME_DAYS("", "days", false),
    SYSTEM_TIME_DAY("", "day", false),
    SYSTEM_TIME_HOURS("", "hours", false),
    SYSTEM_TIME_HOUR("", "hour", false),
    SYSTEM_TIME_MINUTES("", "minutes", false),
    SYSTEM_TIME_MINUTE("", "minute", false),
    SYSTEM_TIME_SECONDS("", "Some seconds", false),
    SYSTEM_TIME_PERMANENT("", "Permanent", false),

    /*
      PLUGIN MESSAGES
     */
    PUNISHMENT_INVALID_TIME_FORMAT("", "", true),
    PUNISHMENT_INVALID_TARGET("", "", true),
    PUNISHMENT_INVALID_REASON("", "", true),
    PUNISHMENT_BAN_ALREADY_BANNED("", "", true),
    PUNISHMENT_BAN_SUCCESSFULLY_BANNED("", "", true),
    PUNISHMENT_BAN_SUCCESSFULLY_UNBANNED("", "", true),
    PUNISHMENT_MUTE_ALREADY_MUTED("", "", true),
    PUNISHMENT_MUTE_SUCCESSFULLY_MUTED("", "", true),


    ;

    private final String key;
    private final String defaultMessage;
    private final boolean prefix;

    MessageKeys(final String key, final String defaultMessage, final boolean prefix) {
        this.key = key;
        this.defaultMessage = defaultMessage;
        this.prefix = prefix;
    }

}
