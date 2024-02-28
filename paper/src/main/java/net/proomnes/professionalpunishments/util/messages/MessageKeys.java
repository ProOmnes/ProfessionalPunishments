package net.proomnes.professionalpunishments.util.messages;

import lombok.Getter;

@Getter
public enum MessageKeys {

    /*
      SYSTEM MESSAGES
     */
    SYSTEM_PREFIX("system.prefix", "", false),
    SYSTEM_PLAYER_NOT_BANNED("", "", true),
    SYSTEM_PLAYER_NOT_MUTED("", "", true),
    SYSTEM_PLAYER_ALREADY_BANNED("", "", true),
    SYSTEM_PLAYER_ALREADY_MUTED("", "", true),
    SYSTEM_INVALID_TIME_FORMAT("", "", true),
    SYSTEM_SCREEN_BAN("", "", false),
    SYSTEM_SCREEN_MUTE("", "", false),

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
    PUNISHMENT_BAN_INVALID_PRESET("", "", true),
    PUNISHMENT_BAN_PRESET("", "", true),
    PUNISHMENT_BAN_USAGE("", "", true),
    PUNISHMENT_BAN_SUCCESSFULLY_BANNED("", "", true),
    PUNISHMENT_TEMPBAN_SUCCESSFULLY_BANNED("", "", true),
    PUNISHMENT_TEMPBAN_USAGE("", "", true),
    PUNISHMENT_UNBAN_SUCCESSFULLY_UNBANNED("", "", true),
    PUNISHMENT_UNBAN_USAGE("", "", true),
    PUNISHMENT_BANLOG_EMPTY("", "", true),
    PUNISHMENT_BANLOG_HEADER("", "", true),
    PUNISHMENT_BANLOG_ENTRY("", "", true),
    PUNISHMENT_BANLOG_ACTIONS("", "", true),
    PUNISHMENT_BANLOG_USAGE("", "", true),
    PUNISHMENT_EDITBAN_REASON("", "", true),
    PUNISHMENT_EDITBAN_DURATION("", "", true),
    PUNISHMENT_EDITBAN_USAGE("", "", true),
    PUNISHMENT_CHECKBAN_HEADER("", "", true),
    PUNISHMENT_CHECKBAN_CONTENT("", "", true),
    PUNISHMENT_CHECKBAN_USAGE("", "", true),
    PUNISHMENT_MUTE_INVALID_PRESET("", "", true),
    PUNISHMENT_MUTE_PRESET("", "", true),
    PUNISHMENT_MUTE_USAGE("", "", true),
    PUNISHMENT_MUTE_SUCCESSFULLY_MUTED("", "", true),
    PUNISHMENT_TEMPMUTE_SUCCESSFULLY_MUTED("", "", true),
    PUNISHMENT_TEMPMUTE_USAGE("", "", true),
    PUNISHMENT_UNMUTE_SUCCESSFULLY_UNMUTED("", "", true),
    PUNISHMENT_UNMUTE_USAGE("", "", true),
    PUNISHMENT_MUTELOG_EMPTY("", "", true),
    PUNISHMENT_MUTELOG_HEADER("", "", true),
    PUNISHMENT_MUTELOG_ENTRY("", "", true),
    PUNISHMENT_MUTELOG_ACTIONS("", "", true),
    PUNISHMENT_MUTELOG_USAGE("", "", true),
    PUNISHMENT_EDITMUTE_REASON("", "", true),
    PUNISHMENT_EDITMUTE_DURATION("", "", true),
    PUNISHMENT_EDITMUTE_USAGE("", "", true),
    PUNISHMENT_CHECKMUTE_HEADER("", "", true),
    PUNISHMENT_CHECKMUTE_CONTENT("", "", true),
    PUNISHMENT_CHECKMUTE_USAGE("", "", true),


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
