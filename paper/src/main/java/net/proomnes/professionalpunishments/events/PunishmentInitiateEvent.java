package net.proomnes.professionalpunishments.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.proomnes.professionalpunishments.objects.Punishment;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
@Getter
public class PunishmentInitiateEvent extends Event {

    private final HandlerList handlers = new HandlerList();
    private final Punishment punishment;
    private final String id, initiator;

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
