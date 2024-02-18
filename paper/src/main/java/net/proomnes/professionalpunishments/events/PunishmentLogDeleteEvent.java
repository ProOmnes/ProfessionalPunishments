package net.proomnes.professionalpunishments.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
@Getter
public class PunishmentLogDeleteEvent extends Event {

    private final HandlerList handlers = new HandlerList();
    private final String logId;

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
