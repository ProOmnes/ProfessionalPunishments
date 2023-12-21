package net.proomnes.professionalpunishments.events;

import cn.nukkit.event.Event;
import cn.nukkit.event.HandlerList;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.proomnes.professionalpunishments.objects.Punishment;

@AllArgsConstructor
@Getter
public class PunishmentAbortEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final Punishment punishment;
    private final String target, initiator, reason, id;

    public static HandlerList getHandlers() {
        return handlers;
    }

}