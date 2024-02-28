package net.proomnes.professionalpunishments;

import lombok.Getter;
import net.proomnes.professionalpunishments.commands.ban.*;
import net.proomnes.professionalpunishments.commands.mute.*;
import net.proomnes.professionalpunishments.dataaccess.IDataAccess;
import net.proomnes.professionalpunishments.dataaccess.MongoDBDataAccess;
import net.proomnes.professionalpunishments.dataaccess.MySQLDataAccess;
import net.proomnes.professionalpunishments.dataaccess.YamlDataAccess;
import net.proomnes.professionalpunishments.listeners.EventListener;
import net.proomnes.professionalpunishments.services.BanService;
import net.proomnes.professionalpunishments.services.DataService;
import net.proomnes.professionalpunishments.services.MuteService;
import net.proomnes.professionalpunishments.services.WarningService;
import net.proomnes.professionalpunishments.util.messages.MessageKeys;
import net.proomnes.professionalpunishments.util.messages.MessageLoader;
import net.proomnes.professionalpunishments.util.tasks.UpdateDataTask;
import org.bukkit.plugin.java.JavaPlugin;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
public class ProfessionalPunishments extends JavaPlugin {

    private IDataAccess dataAccess;

    private BanService banService;
    private MuteService muteService;
    private WarningService warningService;
    private DataService dataService;

    private MessageLoader messageLoader;

    @Override
    public void onLoad() {
        this.getLogger().info("Loading plugin...");
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
                this.getLogger().info("§4Please specify a valid provider: 'Yaml', 'MySQL', 'MongoDB'.");
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
        this.getServer().getCommandMap().register("professionalpunishments", new BanCommand(this));
        this.getServer().getCommandMap().register("professionalpunishments", new BanlogCommand(this));
        this.getServer().getCommandMap().register("professionalpunishments", new CheckbanCommand(this));
        this.getServer().getCommandMap().register("professionalpunishments", new EditbanCommand(this));
        this.getServer().getCommandMap().register("professionalpunishments", new TempbanCommand(this));
        this.getServer().getCommandMap().register("professionalpunishments", new UnbanCommand(this));

        this.getServer().getCommandMap().register("professionalpunishments", new CheckmuteCommand(this));
        this.getServer().getCommandMap().register("professionalpunishments", new EditmuteCommand(this));
        this.getServer().getCommandMap().register("professionalpunishments", new MuteCommand(this));
        this.getServer().getCommandMap().register("professionalpunishments", new MutelogCommand(this));
        this.getServer().getCommandMap().register("professionalpunishments", new TempmuteCommand(this));
        this.getServer().getCommandMap().register("professionalpunishments", new UnmuteCommand(this));

        // tasks
        final int interval = this.getConfig().getInt("settings.update-interval");
        if (!(interval == 0)) this.getServer().getScheduler().runTaskTimerAsynchronously(this, new UpdateDataTask(this), interval * 60L, interval * 60L);

        this.getLogger().info("Plugin loaded and enabled.");
        this.getLogger().info("ProfessionalPunishments is a moderation tool for server staff.");
        this.getLogger().info("This plugin was developed by Jan Pretzer.");
        this.getLogger().info("Have fun using this plugin!");
    }

    @Override
    public void onDisable() {
        this.getLogger().info("Plugin disabled.");
    }

    public String getRandomId(final int length) {
        final String chars = "abcdefgrqpxyz1234567890";
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
            return this.getMessageLoader().get(MessageKeys.SYSTEM_TIME_PERMANENT).toString();
        }

        final long time = duration - System.currentTimeMillis();
        final int days = (int) (time / 86400000L);
        final int hours = (int) (time / 3600000L % 24L);
        final int minutes = (int) (time / 60000L % 60L);

        final String day = (days == 1) ? this.getMessageLoader().get(MessageKeys.SYSTEM_TIME_DAY).toString() :
                this.getMessageLoader().get(MessageKeys.SYSTEM_TIME_DAYS).toString();
        final String hour = (hours == 1) ? this.getMessageLoader().get(MessageKeys.SYSTEM_TIME_HOUR).toString() :
                this.getMessageLoader().get(MessageKeys.SYSTEM_TIME_HOURS).toString();
        final String minute = (minutes == 1) ? this.getMessageLoader().get(MessageKeys.SYSTEM_TIME_MINUTE).toString() :
                this.getMessageLoader().get(MessageKeys.SYSTEM_TIME_MINUTES).toString();

        if (minutes < 1 && days == 0 && hours == 0) {
            return this.getMessageLoader().get(MessageKeys.SYSTEM_TIME_SECONDS).toString();
        } else if (hours == 0 && days == 0) {
            return minutes + " " + minute;
        } else {
            return (days == 0) ? hours + " " + hour + " " + minutes + " " + minute :
                    days + " " + day + " " + hours + " " + hour + " " + minutes + " " + minute;
        }
    }

    /**
     * @param timeFormat Examples: 2d6h30m or 12h...
     * @return Returns the minutes as Integer
     */
    public int timeFormatToMinutes(String timeFormat) {
        int totalMinutes = 0;

        // Regular expression pattern to match days, hours, and minutes
        final Pattern pattern = Pattern.compile("(\\d+d)?(\\d+h)?(\\d+m)?");
        final Matcher matcher = pattern.matcher(timeFormat);

        while (matcher.find()) {
            final String daysString = matcher.group(1);
            final String hoursString = matcher.group(2);
            final String minutesString = matcher.group(3);

            // Convert days to minutes
            if (daysString != null) {
                final int days = Integer.parseInt(daysString.substring(0, daysString.length() - 1));
                totalMinutes += days * 24 * 60;
            }

            // Convert hours to minutes
            if (hoursString != null) {
                final int hours = Integer.parseInt(hoursString.substring(0, hoursString.length() - 1));
                totalMinutes += hours * 60;
            }

            // Convert minutes
            if (minutesString != null) {
                final int minutes = Integer.parseInt(minutesString.substring(0, minutesString.length() - 1));
                totalMinutes += minutes;
            }
        }

        return totalMinutes;
    }

}
