package net.proomnes.professionalpunishments.objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@AllArgsConstructor
@Getter
@Setter
public class Punishment {

    private final String id;
    private String relatedId;
    private final Type type;
    private String target;
    private String reason;
    private String initiator;
    private String date;
    private long expire;

    public boolean targetIsOnline() {
        final Player player = Bukkit.getPlayer(this.target);
        return player != null;
    }

    public boolean initiatorIsOnline() {
        final Player player = Bukkit.getPlayer(this.initiator);
        return player != null;
    }

    @AllArgsConstructor
    @Getter
    @Setter
    public static class Log {

        private final String id;
        private String relatedId;
        private final LogType logType;
        private String target;
        private String reason;
        private String initiator;
        private String date;

    }

    public enum Type {

        PUNISHMENT_BAN, PUNISHMENT_MUTE, PUNISHMENT_WARNING

    }

    public enum LogType {

        LOG_BAN, LOG_MUTE, LOG_WARNING, LOG_KICK, LOG_UNBAN, LOG_UNMUTE

    }

}
