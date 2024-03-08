package net.proomnes.professionalpunishments;

import lombok.Getter;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.proomnes.professionalpunishments.api.PunishmentAPI;
import net.proomnes.professionalpunishments.commands.ban.*;
import net.proomnes.professionalpunishments.commands.data.CheckpunishmentCommand;
import net.proomnes.professionalpunishments.commands.data.ClearlogCommand;
import net.proomnes.professionalpunishments.commands.data.DeletePunishmentCommand;
import net.proomnes.professionalpunishments.commands.mute.*;
import net.proomnes.professionalpunishments.commands.warning.*;
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

    @Getter
    private static PunishmentAPI punishmentAPI;

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

                // disable MongoDB Logger
                final Logger mongoLogger = Logger.getLogger("org.mongodb.driver");
                mongoLogger.setLevel(Level.OFF);
                break;
            case "MySQL":
                this.dataAccess = new MySQLDataAccess(this);
                break;
            default:
                this.getLogger().warning("§4Please specify a valid provider: 'Yaml', 'MySQL', 'MongoDB'.");
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
        punishmentAPI = new PunishmentAPI(this.banService, this.muteService, this.warningService, this.messageLoader);

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

        this.getServer().getCommandMap().register("professionalpunishments", new EditwarningCommand(this));
        this.getServer().getCommandMap().register("professionalpunishments", new TempwarnCommand(this));
        this.getServer().getCommandMap().register("professionalpunishments", new UnwarnCommand(this));
        this.getServer().getCommandMap().register("professionalpunishments", new WarnCommand(this));
        this.getServer().getCommandMap().register("professionalpunishments", new WarningsCommand(this));
        this.getServer().getCommandMap().register("professionalpunishments", new WarnlogCommand(this));

        this.getServer().getCommandMap().register("professionalpunishments", new CheckpunishmentCommand(this));
        this.getServer().getCommandMap().register("professionalpunishments", new ClearlogCommand(this));
        this.getServer().getCommandMap().register("professionalpunishments", new DeletePunishmentCommand(this));

        // tasks
        final int interval = this.getConfig().getInt("settings.update-interval");
        if (!(interval <= 29)) this.getServer().getScheduler().runTaskTimerAsynchronously(this, new UpdateDataTask(this), interval * 60L, interval * 60L);

        // start-up messages
        this.getLogger().info("Plugin " + this.getPluginMeta().getName() + " " + this.getPluginMeta().getVersion() + " successfully loaded and enabled.");
        this.getLogger().info(this.getPluginMeta().getDescription());
        this.getLogger().info("This plugin was developed by " + this.getPluginMeta().getAuthors().get(0));
        this.getLogger().info("Report issues on our Github repository: https://github.com/ProOmnes/ProfessionalPunishments/issues");
    }

    @Override
    public void onDisable() {
        this.getLogger().info("Plugin unloaded and disabled.");
    }

    public String getRandomId(final int length) {
        final String chars = "abcdefgrxyz1234567890";
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
            return PlainTextComponentSerializer.plainText().serialize(this.getMessageLoader().get(MessageKeys.SYSTEM_TIME_PERMANENT));
        }

        final long time = duration - System.currentTimeMillis();
        final int days = (int) (time / 86400000L);
        final int hours = (int) (time / 3600000L % 24L);
        final int minutes = (int) (time / 60000L % 60L);

        final String day = (days == 1) ? PlainTextComponentSerializer.plainText().serialize(this.getMessageLoader().get(MessageKeys.SYSTEM_TIME_DAY)) :
                PlainTextComponentSerializer.plainText().serialize(this.getMessageLoader().get(MessageKeys.SYSTEM_TIME_DAYS));
        final String hour = (hours == 1) ? PlainTextComponentSerializer.plainText().serialize(this.getMessageLoader().get(MessageKeys.SYSTEM_TIME_HOUR)) :
                PlainTextComponentSerializer.plainText().serialize(this.getMessageLoader().get(MessageKeys.SYSTEM_TIME_HOURS));
        final String minute = (minutes == 1) ? PlainTextComponentSerializer.plainText().serialize(this.getMessageLoader().get(MessageKeys.SYSTEM_TIME_MINUTE)) :
                PlainTextComponentSerializer.plainText().serialize(this.getMessageLoader().get(MessageKeys.SYSTEM_TIME_MINUTES));

        if (minutes < 1 && days == 0 && hours == 0) {
            return PlainTextComponentSerializer.plainText().serialize(this.getMessageLoader().get(MessageKeys.SYSTEM_TIME_SECONDS));
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
        int totalMinutes = -1;

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
