package net.proomnes.professionalpunishments.events;

import cn.nukkit.event.Event;
import cn.nukkit.event.HandlerList;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.proomnes.professionalpunishments.objects.Punishment;

@AllArgsConstructor
@Getter
public class PunishmentInitiateEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final Punishment punishment;
    private final String id, initiator;

    public static HandlerList getHandlers() {
        return handlers;
    }

}
