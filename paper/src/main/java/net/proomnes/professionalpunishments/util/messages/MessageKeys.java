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

    /*
      UI DISPLAYS
     */
    UI_BUTTON_BACK("ui.button.back", "§8» §cBack", false),

    UI_BAN_BAN_TITLE("", "", false),
    UI_BAN_BAN_TARGET("", "", false),
    UI_BAN_BAN_TARGET_PLACEHOLDER("", "", false),
    UI_BAN_BAN_REASON("", "", false),
    UI_BAN_BAN_REASON_PLACEHOLDER("", "", false),
    UI_BAN_BAN_TIME("", "", false),
    UI_BAN_BAN_TIME_PLACEHOLDER("", "", false),
    UI_BAN_BAN_INFO("", "", false),

    UI_BAN_LOG_TITLE("", "", false),
    UI_BAN_LOG_INFO("", "", false),
    UI_BAN_LOG_ENTRY("", "", false),

    UI_BAN_INFO_TITLE("", "", false),
    UI_BAN_INFO_CONTENT("", "", false),
    UI_BAN_INFO_ACTION_LOGS("", "", false),
    UI_BAN_INFO_ACTION_UNBAN("", "", false),
    UI_BAN_INFO_ACTION_EDIT("", "", false),

    UI_BAN_UNBAN_TITLE("", "", false),
    UI_BAN_UNBAN_TARGET("", "", false),
    UI_BAN_UNBAN_TARGET_PLACEHOLDER("", "", false),
    UI_BAN_UNBAN_REASON("", "", false),
    UI_BAN_UNBAN_REASON_PLACEHOLDER("", "", false),

    UI_MUTE_MUTE_TITLE("", "", false),
    UI_MUTE_MUTE_TARGET("", "", false),
    UI_MUTE_MUTE_TARGET_PLACEHOLDER("", "", false),
    UI_MUTE_MUTE_REASON("", "", false),
    UI_MUTE_MUTE_REASON_PLACEHOLDER("", "", false),
    UI_MUTE_MUTE_TIME("", "", false),
    UI_MUTE_MUTE_TIME_PLACEHOLDER("", "", false),
    UI_MUTE_MUTE_INFO("", "", false),


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
