package net.proomnes.professionalpunishments.util.tasks;

import lombok.AllArgsConstructor;
import net.proomnes.professionalpunishments.ProfessionalPunishments;

import java.util.concurrent.CompletableFuture;

@AllArgsConstructor
public class UpdateDataTask implements Runnable {

    private final ProfessionalPunishments professionalPunishments;

    // TODO: 21.12.2023 check if all players are kicked if they are banned on update

    /**
     * When an object implementing interface {@code Runnable} is used
     * to create a thread, starting the thread causes the object's
     * {@code run} method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method {@code run} is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        CompletableFuture.runAsync(() -> {
            // update ban data
            this.professionalPunishments.getBanService().cachedBans.clear();
            this.professionalPunishments.getDataAccess().getAllBans(punishmentSet -> {
                punishmentSet.forEach(punishment -> {
                    this.professionalPunishments.getBanService().cachedBans.add(punishment);
                });
            });

            // update mute data
            this.professionalPunishments.getMuteService().cachedMutes.clear();
            this.professionalPunishments.getDataAccess().getAllMutes(punishmentSet -> {
                punishmentSet.forEach(punishment -> {
                    this.professionalPunishments.getMuteService().cachedMutes.add(punishment);
                });
            });

            // update warning data
            this.professionalPunishments.getWarningService().cachedWarnings.clear();
            this.professionalPunishments.getDataAccess().getAllActiveWarnings(punishmentSet -> {
                punishmentSet.forEach(punishment -> {
                    this.professionalPunishments.getWarningService().cachedWarnings.add(punishment);
                });
            });
        });
    }
}
