package net.proomnes.professionalpunishments.objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Punishment {

    private final String id;
    private final String relatedId;
    private final Type type;
    private final String target;
    private final String reason;
    private final String initiator;
    private final String date;
    private final long expire;

    @AllArgsConstructor
    @Getter
    @Setter
    public static class Log {

        private final String id;
        private final String relatedId;
        private final LogType logType;
        private final String target;
        private final String reason;
        private final String initiator;
        private final String date;

    }

    public enum Type {

        PUNISHMENT_BAN, PUNISHMENT_MUTE, PUNISHMENT_WARNING

    }

    public enum LogType {

        LOG_BAN, LOG_MUTE, LOG_WARNING, LOG_KICK, LOG_UNBAN, LOG_UNMUTE

    }

}
