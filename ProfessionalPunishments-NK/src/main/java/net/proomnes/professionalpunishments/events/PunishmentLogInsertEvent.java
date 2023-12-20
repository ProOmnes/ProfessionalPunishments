package net.proomnes.professionalpunishments.events;

import cn.nukkit.event.Event;
import cn.nukkit.event.HandlerList;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.proomnes.professionalpunishments.objects.Punishment;

@AllArgsConstructor
@Getter
public class PunishmentLogInsertEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final Punishment.Log log;

    public static HandlerList getHandlers() {
        return handlers;
    }

}
