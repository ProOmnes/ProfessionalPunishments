package net.proomnes.professionalpunishments.objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Punishment {

    private final String id;
    private final Type type;
    private final String target;
    private final String reason;
    private final String initiator;
    private final String date;
    private final long ending;

    public enum Type {

        PUNISHMENT_BAN, PUNISHMENT_MUTE, PUNISHMENT_WARNING, PUNISHMENT_KICK

    }

}
