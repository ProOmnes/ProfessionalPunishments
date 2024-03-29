package net.proomnes.professionalpunishments.util.messages;

import lombok.Getter;

@Getter
public enum MessageKeys {

    // Default messages are formatted with MiniMessage: https://docs.advntr.dev/minimessage/format.html

    /*
      SYSTEM MESSAGES
     */
    SYSTEM_PREFIX(
            "system.prefix",
            "<dark_gray>» <gradient:#487fc0:#62cdff>Punishments</gradient> <dark_gray>| <gray>",
            false
    ),
    SYSTEM_PLAYER_NOT_BANNED(
            "system.player.not-banned",
            "<aqua>{0} <gray>is not banned.",
            true
    ),
    SYSTEM_PLAYER_NOT_MUTED(
            "system.player.not-muted",
            "<aqua>{0} <gray>is not muted.",
            true
    ),
    SYSTEM_PUNISHMENT_ID_NOT_FOUND(
            "system.punishment.id-not-found",
            "<aqua>{0} <gray>is not a valid punishment identification or not active anymore.",
            true
    ),
    SYSTEM_LOG_ID_NOT_FOUND(
            "system.log.id-not-found",
            "<aqua>{0} <gray>is not a valid log identification.",
            true
    ),
    SYSTEM_PLAYER_ALREADY_BANNED(
            "system.player.already-banned",
            "<aqua>{0} <gray>is already banned.",
            true
    ),
    SYSTEM_PLAYER_ALREADY_MUTED(
            "system.player.already-muted",
            "<aqua>{0} <gray>is already muted.",
            true
    ),
    SYSTEM_INVALID_TIME_FORMAT(
            "system.time.invalid-format",
            "<gray>Please provide a valid time format. Examples: <aqua>5d12h<gray>, <aqua>3d<gray>, <aqua>2d12h30m",
            true
    ),
    SYSTEM_SCREEN_BAN(
            "system.screen.ban",
            "<gradient:#487fc0:#62cdff>You are banned!</gradient><newline><gray>Reason: <aqua>{1}<newline><gray>Initiator: <aqua>{2} <gray>ID: <aqua>{0}<newline><gray>Remaining time: <aqua>{4}",
            false
    ),
    SYSTEM_SCREEN_MUTE(
            "system.screen.mute",
            "<newline><gradient:#487fc0:#62cdff>You are muted!</gradient><newline><gray>Reason: <aqua>{1}<newline><gray>Initiator: <aqua>{2} <gray>ID: <aqua>{0}<newline><gray>Remaining time: <aqua>{4}<newline>",
            false
    ),

    SYSTEM_TIME_DAYS("system.time.days", "days", false),
    SYSTEM_TIME_DAY("system.time.day", "day", false),
    SYSTEM_TIME_HOURS("system.time.hours", "hours", false),
    SYSTEM_TIME_HOUR("system.time.hour", "hour", false),
    SYSTEM_TIME_MINUTES("system.time.minutes", "minutes", false),
    SYSTEM_TIME_MINUTE("system.time.minute", "minute", false),
    SYSTEM_TIME_SECONDS("system.time.seconds", "Some seconds left...", false),
    SYSTEM_TIME_PERMANENT("system.time.permanent", "Permanent", false),

    /*
      PLUGIN MESSAGES
     */
    PUNISHMENT_BAN_INVALID_PRESET(
            "punishment.ban.invalid-preset",
            "<aqua>{0} <gray>is not a valid preset id for bans.",
            true
    ),
    PUNISHMENT_BAN_PRESET(
            "punishment.ban.preset",
            "<aqua>{0} <gray>| <aqua>{1} <gray>Duration: <aqua>{2}",
            true
    ),
    PUNISHMENT_BAN_USAGE(
            "punishment.ban.usage",
            "<gray>Usage: <aqua>/{0} <player> <presetID> <gray>- {1}",
            true
    ),
    PUNISHMENT_BAN_SUCCESSFULLY_BANNED(
            "punishment.ban.successfully-banned",
            "<gray>You successfully banned <aqua>{0} <gray>for <aqua>{1}<gray>.",
            true
    ),
    PUNISHMENT_TEMPBAN_SUCCESSFULLY_BANNED(
            "punishment.tempban.successfully-banned",
            "<gray>You successfully banned <aqua>{0} <gray>for <aqua>{1}<gray>.",
            true
    ),
    PUNISHMENT_TEMPBAN_USAGE(
            "punishment.tempban.usage",
            "<gray>Usage: <aqua>/{0} <player> <timeFormat> <reason> <gray>- {1}",
            true
    ),
    PUNISHMENT_UNBAN_SUCCESSFULLY_UNBANNED(
            "punishment.unban.successfully-unbanned",
            "<gray>You successfully unbanned player <aqua>{0} <gray> for <aqua>{1}<gray>.",
            true
    ),
    PUNISHMENT_UNBAN_USAGE(
            "punishment.unban.usage",
            "<gray>Usage: <aqua>/{0} <player> <reason> <gray>- {1}",
            true
    ),
    PUNISHMENT_BANLOG_EMPTY(
            "punishment.banlog.empty",
            "<gray>There is no banlog data about <aqua>{0}<gray>.",
            true
    ),
    PUNISHMENT_BANLOG_HEADER(
            "punishment.banlog.header",
            "<gray>Banlog data of <aqua>{1}<gray>. Entries: <aqua>{0}",
            true
    ),
    PUNISHMENT_BANLOG_ENTRY(
            "punishment.banlog.entry",
            "<newline><gray>LogID: <aqua>{0}<newline><gray>PunishmentID: <aqua>{1}<newline><gray>Reason: <aqua>{3}<newline><gray>Initiator: <aqua>{4}<newline><gray>Date: <aqua>{5}",
            false
    ),
    PUNISHMENT_BANLOG_ACTIONS(
            "punishment.banlog.actions",
            "<click:suggest_command:/checkpunishment {0}><gray>[<gold>Check Punishment<gray>]</click> <click:suggest_command:/deletepunishment {0}><gray>[<red>Delete Entry<gray>]</click>",
            false
    ),
    PUNISHMENT_BANLOG_USAGE(
            "punishment.banlog.usage",
            "<gray>Usage: <aqua>/{0} <player> <gray>- {1}",
            true
    ),
    PUNISHMENT_EDITBAN_REASON(
            "punishment.editban.reason",
            "<gray>You successfully edited the reason of the active ban of <aqua>{0} <gray>to <aqua>{1}<gray>.",
            true
    ),
    PUNISHMENT_EDITBAN_DURATION(
            "punishment.editban.duration",
            "<gray>You successfully edited the duration of the active ban of <aqua>{0} <gray>to <aqua>{1}<gray>.",
            true
    ),
    PUNISHMENT_EDITBAN_USAGE(
            "punishment.editban.usage",
            "<gray>Usage: <aqua>/{0} <player> <reason|duration> <newReason|newTimeFormat> <gray>- {1}",
            true
    ),
    PUNISHMENT_CHECKBAN_HEADER(
            "punishment.checkban.header",
            "<gray>Ban entry <aqua>{1} <gray>of player <aqua>{0}<gray>:",
            true
    ),
    PUNISHMENT_CHECKBAN_CONTENT(
            "punishment.checkban.content",
            "<newline><gray>PunishmentID: <aqua>{0}<newline><gray>Reason: <aqua>{3}<newline><gray>Initiator: <aqua>{4}<newline><gray>Date: <aqua>{5}<newline><gray>Remaining time: <aqua>{6}",
            false
    ),
    PUNISHMENT_CHECKBAN_USAGE(
            "punishment.checkban.usage",
            "<gray>Usage: <aqua>/{0} <player> <gray>- {1}",
            true
    ),

    PUNISHMENT_MUTE_INVALID_PRESET(
            "punishment.mute.invalid-preset",
            "<aqua>{0} <gray>is not a valid preset id for mutes.",
            true
    ),
    PUNISHMENT_MUTE_PRESET(
            "punishment.mute.preset",
            "<aqua>{0} <gray>| <aqua>{1} <gray>Duration: <aqua>{2}",
            true
    ),
    PUNISHMENT_MUTE_USAGE(
            "punishment.mute.usage",
            "<gray>Usage: <aqua>/{0} <player> <presetID> <gray>- {1}",
            true),
    PUNISHMENT_MUTE_SUCCESSFULLY_MUTED(
            "punishment.mute.successfully-muted",
            "<gray>You successfully muted <aqua>{0} <gray>for <aqua>{1}<gray>.",
            true
    ),
    PUNISHMENT_TEMPMUTE_SUCCESSFULLY_MUTED(
            "punishment.tempmute.successfully-muted",
            "<gray>You successfully muted <aqua>{0} <gray>for <aqua>{1}<gray>.",
            true
    ),
    PUNISHMENT_TEMPMUTE_USAGE(
            "punishment.tempmute.usage",
            "<gray>Usage: <aqua>/{0} <player> <timeFormat> <reason> <gray>- {1}",
            true
    ),
    PUNISHMENT_UNMUTE_SUCCESSFULLY_UNMUTED(
            "punishment.unmute.successfully-unmuted",
            "<gray>You successfully unmuted player <aqua>{0} <gray> for <aqua>{1}<gray>.",
            true
    ),
    PUNISHMENT_UNMUTE_USAGE(
            "punishment.unmute.usage",
            "<gray>Usage: <aqua>/{0} <player> <reason> <gray>- {1}",
            true
    ),
    PUNISHMENT_MUTELOG_EMPTY(
            "punishment.mutelog.empty",
            "<gray>There is no mutelog data about <aqua>{0}<gray>.",
            true
    ),
    PUNISHMENT_MUTELOG_HEADER(
            "punishment.mutelog.header",
            "<gray>Mutelog data of <aqua>{1}<gray>. Entries: <aqua>{0}",
            true
    ),
    PUNISHMENT_MUTELOG_ENTRY(
            "punishment.mutelog.entry",
            "<newline><gray>LogID: <aqua>{0}<newline><gray>PunishmentID: <aqua>{1}<newline><gray>Reason: <aqua>{3}<newline><gray>Initiator: <aqua>{4}<newline><gray>Date: <aqua>{5}",
            true
    ),
    PUNISHMENT_MUTELOG_ACTIONS(
            "punishment.mutelog.actions",
            "<click:suggest_command:/checkpunishment {0}><gray>[<gold>Check Punishment<gray>]</click> <click:suggest_command:/deletepunishment {0}><gray>[<red>Delete Entry<gray>]</click>",
            true
    ),
    PUNISHMENT_MUTELOG_USAGE(
            "punishment.mutelog.usage",
            "<gray>Usage: <aqua>/{0} <player> <gray>- {1}",
            true
    ),
    PUNISHMENT_EDITMUTE_REASON(
            "punishment.editmute.reason",
            "<gray>You successfully edited the reason of the active mute of <aqua>{0} <gray>to <aqua>{1}<gray>.",
            true
    ),
    PUNISHMENT_EDITMUTE_DURATION(
            "punishment.editmute.duration",
            "<gray>You successfully edited the duration of the active mute of <aqua>{0} <gray>to <aqua>{1}<gray>.",
            true
    ),
    PUNISHMENT_EDITMUTE_USAGE(
            "punishment.editmute.usage",
            "<gray>Usage: <aqua>/{0} <player> <reason|duration> <newReason|newTimeFormat> <gray>- {1}",
            true
    ),
    PUNISHMENT_CHECKMUTE_HEADER(
            "punishment.checkmute.header",
            "<gray>Mute entry <aqua>{1} <gray>of player <aqua>{0}<gray>:",
            true
    ),
    PUNISHMENT_CHECKMUTE_CONTENT(
            "punishment.checkmute.content",
            "<newline><gray>PunishmentID: <aqua>{0}<newline><gray>Reason: <aqua>{3}<newline><gray>Initiator: <aqua>{4}<newline><gray>Date: <aqua>{5}<newline><gray>Remaining time: <aqua>{6}",
            true
    ),
    PUNISHMENT_CHECKMUTE_USAGE(
            "punishment.checkmute.usage",
            "<gray>Usage: <aqua>/{0} <player> <gray>- {1}",
            true
    ),

    PUNISHMENT_WARN_INVALID_PRESET(
            "punishment.warn.invalid-preset",
            "<aqua>{0} <gray>is not a valid preset id for warnings.",
            true
    ),
    PUNISHMENT_WARN_PRESET(
            "punishment.warn.preset",
            "<aqua>{0} <gray>| <aqua>{1} <gray>Duration: <aqua>{2}",
            true
    ),
    PUNISHMENT_WARN_SUCCESSFULLY_WARNED(
            "punishment.warn.successfully-warned",
            "<gray>You successfully warned <aqua>{0} <gray>for <aqua>{1}<gray>.",
            true
    ),
    PUNISHMENT_WARN_USAGE(
            "punishment.warn.usage",
            "<gray>Usage: <aqua>/{0} <player> <presetID> <gray>- {1}",
            true
    ),
    PUNISHMENT_UNWARN_INVALID_ID(
            "punishment.unwarn.invalid-id",
            "<aqua>{0} <gray>is not a valid warning id.",
            true
    ),
    PUNISHMENT_UNWARN_SUCCESSFULLY_UNWARNED(
            "punishment.unwarn.successfully-unwarned",
            "<gray>You successfully unwarned player <aqua>{0} <gray> for <aqua>{1}<gray>.",
            true
    ),
    PUNISHMENT_UNWARN_USAGE(
            "punishment.unwarn.usage",
            "<gray>Usage: <aqua>/{0} <player> <reason> <gray>- {1}",
            true
    ),
    PUNISHMENT_TEMPWARN_SUCCESSFULLY_WARNED(
            "punishment.tempwarn.successfully-warned",
            "<gray>You successfully warned <aqua>{0} <gray>for <aqua>{1}<gray>.",
            true
    ),
    PUNISHMENT_TEMPWARN_USAGE(
            "punishment.tempwarn.usage",
            "<gray>Usage: <aqua>/{0} <player> <timeFormat> <reason> <gray>- {1}",
            true
    ),
    PUNISHMENT_EDITWARN_INVALID_ID(
            "punishment.editwarn.invalid-id",
            "<aqua>{0} <gray>is not a valid warning id.",
            true
    ),
    PUNISHMENT_EDITWARN_REASON(
            "punishment.editwarn.reason",
            "<gray>You successfully edited the reason of the active warning of <aqua>{0} <gray>to <aqua>{1}<gray>.",
            true
    ),
    PUNISHMENT_EDITWARN_DURATION(
            "punishment.editwarn.duration",
            "<gray>You successfully edited the duration of the active warning of <aqua>{0} <gray>to <aqua>{1}<gray>.",
            true
    ),
    PUNISHMENT_EDITWARN_USAGE(
            "punishment.editwarn.usage",
            "<gray>Usage: <aqua>/{0} <player> <reason|duration> <newReason|newTimeFormat> <gray>- {1}",
            true
    ),
    PUNISHMENT_WARNLOG_EMPTY(
            "punishment.warnlog.empty",
            "<gray>There is no warnlog data about <aqua>{0}<gray>.",
            true
    ),
    PUNISHMENT_WARNLOG_HEADER(
            "punishment.warnlog.header",
            "<gray>Warnlog data of <aqua>{1}<gray>. Entries: <aqua>{0}",
            true
    ),
    PUNISHMENT_WARNLOG_ENTRY(
            "punishment.warnlog.entry",
            "<newline><gray>LogID: <aqua>{0}<newline><gray>PunishmentID: <aqua>{1}<newline><gray>Reason: <aqua>{3}<newline><gray>Initiator: <aqua>{4}<newline><gray>Date: <aqua>{5}",
            false
    ),
    PUNISHMENT_WARNLOG_ACTIONS(
            "punishment.warnlog.actions",
            "<click:suggest_command:/checkpunishment {0}><gray>[<gold>Check Punishment<gray>]</click> <click:suggest_command:/deletepunishment {0}><gray>[<red>Delete Entry<gray>]</click>",
            false
    ),
    PUNISHMENT_WARNLOG_USAGE(
            "punishment.warnlog.usage",
            "<gray>Usage: <aqua>/{0} <player> <gray>- {1}",
            true
    ),
    PUNISHMENT_WARNINGS_EMPTY(
            "punishment.warnings.empty",
            "<gray>There are no active warnings about <aqua>{0}<gray>.",
            true
    ),
    PUNISHMENT_WARNINGS_HEADER(
            "punishment.warnings.header",
            "<gray>Active warnings of <aqua>{1}<gray>. Count: <aqua>{0}",
            true
    ),
    PUNISHMENT_WARNINGS_ENTRY(
            "punishment.warnings.entry",
            "<newline><gray>PunishmentID: <aqua>{0}<newline><gray>Reason: <aqua>{3}<newline><gray>Initiator: <aqua>{4}<newline><gray>Date: <aqua>{5}<newline><gray>Remaining time: <aqua>{6}",
            false
    ),
    PUNISHMENT_WARNINGS_ACTIONS(
            "punishment.warnings.actions",
            "<click:suggest_command:/unwarn {1} {0} Some reason><gray>[<gold>Cancel warning<gray>]</click>",
            false
    ),
    PUNISHMENT_WARNINGS_USAGE(
            "punishment.warnings.usage",
            "<gray>Usage: <aqua>/{0} <player> <gray>- {1}",
            true
    ),

    PUNISHMENT_CHECKPUNISHMENT_HEADER(
            "punishment.checkpunishment.header",
            "<gray>Punishment entry <aqua>{0}<gray>:",
            true
    ),
    PUNISHMENT_CHECKPUNISHMENT_CONTENT(
            "punishment.checkpunishment.content",
            "<newline><gray>PunishmentID: <aqua>{0}<newline><gray>RelatedID: <aqua>{1}<newline><gray>Target: <aqua>{2}<newline><gray>Reason: <aqua>{3}<newline><gray>Initiator: <aqua>{4}<newline><gray>Date: <aqua>{5}<newline><gray>Remaining time: <aqua>{6}",
            false
    ),
    PUNISHMENT_CHECKPUNISHMENT_USAGE(
            "punishment.checkpunishment.usage",
            "<gray>Usage: <aqua>/{0} <punishmentID> <gray>- {1}",
            true
    ),
    PUNISHMENT_CLEARLOG_CLEARED(
            "punishment.clearlog.cleared",
            "<gray>You successfully cleared all {1} logs from <aqua>{0}<gray>.",
            true
    ),
    PUNISHMENT_CLEARLOG_INVALID_TYPE(
            "punishment.clearlog.invalid-type",
            "<aqua>{0} <gray>is not a valid log type.",
            true
    ),
    PUNISHMENT_CLEARLOG_USAGE(
            "punishment.clearlog.usage",
            "<gray>Usage: <aqua>/{0} <target> <logType> <gray>- {1}",
            true
    ),
    PUNISHMENT_DELETEPUNISHMENT_DELETED(
            "punishment.deletepunishment.deleted",
            "<gray>You successfully deleted entry <aqua>{0}<gray>.",
            true
    ),
    PUNISHMENT_DELETEPUNISHMENT_USAGE(
            "punishment.deletepunishment.usage",
            "<gray>Usage: <aqua>/{0} <logID> <logType> <gray>- {1}",
            true
    ),
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
