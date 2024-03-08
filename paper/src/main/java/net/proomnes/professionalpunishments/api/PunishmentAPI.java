package net.proomnes.professionalpunishments.api;

import net.proomnes.professionalpunishments.services.BanService;
import net.proomnes.professionalpunishments.services.MuteService;
import net.proomnes.professionalpunishments.services.WarningService;
import net.proomnes.professionalpunishments.util.messages.MessageLoader;

public record PunishmentAPI(BanService banService, MuteService muteService, WarningService warningService,
                            MessageLoader messageLoader) {

}
