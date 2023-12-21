package net.proomnes.professionalpunishments;

import cn.nukkit.plugin.PluginBase;
import lombok.Getter;
import net.proomnes.professionalpunishments.dataaccess.IDataAccess;
import net.proomnes.professionalpunishments.dataaccess.MongoDBDataAccess;
import net.proomnes.professionalpunishments.dataaccess.MySQLDataAccess;
import net.proomnes.professionalpunishments.dataaccess.YamlDataAccess;
import net.proomnes.professionalpunishments.listeners.EventListener;
import net.proomnes.professionalpunishments.services.BanService;
import net.proomnes.professionalpunishments.services.DataService;
import net.proomnes.professionalpunishments.services.MuteService;
import net.proomnes.professionalpunishments.services.WarningService;
import net.proomnes.professionalpunishments.util.messages.MessageLoader;
import net.proomnes.professionalpunishments.util.tasks.UpdateDataTask;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

@Getter
public class ProfessionalPunishments extends PluginBase {

    private IDataAccess dataAccess;

    private BanService banService;
    private MuteService muteService;
    private WarningService warningService;
    private DataService dataService;

    private MessageLoader messageLoader;

    @Override
    public void onLoad() {
        this.getLogger().info("[ProfessionalPunishments] Loading plugin...");
    }

    @Override
    public void onEnable() {
        this.saveDefaultConfig();

        // Initializes the provider and connects it
        this.setUpProvider();

        this.loadPlugin();
    }

    private void setUpProvider() {
        switch (this.getConfig().getString("settings.provider")) {
            case "Yaml":
                this.dataAccess = new YamlDataAccess(this);
                break;
            case "MongoDB":
                this.dataAccess = new MongoDBDataAccess(this);
                final Logger mongoLogger = Logger.getLogger("org.mongodb.driver");
                mongoLogger.setLevel(Level.OFF);
                break;
            case "MySQL":
                this.dataAccess = new MySQLDataAccess(this);
                break;
            default:
                this.getLogger().error("§4[ProfessionalPunishments] Please specify a valid provider: 'Yaml', 'MySQL', 'MongoDB'.");
                break;
        }
    }

    private void loadPlugin() {
        // utils
        this.messageLoader = new MessageLoader(this);

        // services
        this.banService = new BanService(this);
        this.muteService = new MuteService(this);
        this.warningService = new WarningService(this);
        this.dataService = new DataService(this);

        // api

        // listeners
        this.getServer().getPluginManager().registerEvents(new EventListener(this), this);

        // commands

        // tasks
        final int interval = this.getConfig().getInt("settings.update-interval");
        if (!(interval == 0)) this.getServer().getScheduler().scheduleDelayedRepeatingTask(new UpdateDataTask(this), interval * 60, interval * 60, true);
    }

    @Override
    public void onDisable() {
        this.getLogger().info("[ProfessionalPunishments] Plugin disabled.");
    }

    public String getRandomId(final int length) {
        final String chars = "abcdefgxyz1234567890";
        final StringBuilder stringBuilder = new StringBuilder();
        final Random rnd = new Random();
        while (stringBuilder.length() < length) {
            final int index = (int) (rnd.nextFloat() * chars.length());
            stringBuilder.append(chars.charAt(index));
        }
        return stringBuilder.toString();
    }

    public String getRandomId(final int length, final String prefix) {
        return prefix + "-" + this.getRandomId(length);
    }

    public String getDateWithTime() {
        final Date now = new Date();
        final DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        return dateFormat.format(now);
    }

    public String getDate() {
        final Date now = new Date();
        final DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        return dateFormat.format(now);
    }

    public String getRemainingTime(long duration) {
        if (duration == -1L) {
            return "Permanent";
        }

        long time = duration - System.currentTimeMillis();
        int days = (int) (time / 86400000L);
        int hours = (int) (time / 3600000L % 24L);
        int minutes = (int) (time / 60000L % 60L);

        String day = (days == 1) ? "Day" : "Days";
        String hour = (hours == 1) ? "Hour" : "Hours";
        String minute = (minutes == 1) ? "Minute" : "Minutes";

        if (minutes < 1 && days == 0 && hours == 0) {
            return "Some seconds";
        } else if (hours == 0 && days == 0) {
            return minutes + " " + minute;
        } else {
            return (days == 0) ? hours + " " + hour + " " + minutes + " " + minute :
                    days + " " + day + " " + hours + " " + hour + " " + minutes + " " + minute;
        }
    }
    }
}
