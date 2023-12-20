package net.proomnes.professionalpunishments.events;

import cn.nukkit.event.Event;
import cn.nukkit.event.HandlerList;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PunishmentLogDeleteEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final String logId;

    public static HandlerList getHandlers() {
        return handlers;
    }

}
