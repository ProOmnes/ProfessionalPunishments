package net.proomnes.professionalpunishments.util.tasks;

import cn.nukkit.scheduler.Task;
import lombok.AllArgsConstructor;
import net.proomnes.professionalpunishments.ProfessionalPunishments;

import java.util.concurrent.CompletableFuture;

@AllArgsConstructor
public class UpdateDataTask extends Task {

    private final ProfessionalPunishments professionalPunishments;

    @Override
    public void onRun(int i) {
        CompletableFuture.runAsync(() -> {
            // update ban data
            this.professionalPunishments.getBanService().cachedBans.clear();
            this.professionalPunishments.getDataAccess().getAllBans(punishmentSet -> {
                punishmentSet.forEach(punishment -> {
                    this.professionalPunishments.getBanService().cachedBans.put(punishment.getId(), punishment);
                });
            });

            // update mute data
            this.professionalPunishments.getMuteService().cachedMutes.clear();
            this.professionalPunishments.getDataAccess().getAllMutes(punishmentSet -> {
                punishmentSet.forEach(punishment -> {
                    this.professionalPunishments.getMuteService().cachedMutes.put(punishment.getId(), punishment);
                });
            });

            // update warning data
            this.professionalPunishments.getWarningService().cachedWarnings.clear();
            this.professionalPunishments.getDataAccess().getAllActiveWarnings(punishmentSet -> {
                punishmentSet.forEach(punishment -> {
                    this.professionalPunishments.getWarningService().cachedWarnings.put(punishment.getId(), punishment);
                });
            });
        });
    }
}
