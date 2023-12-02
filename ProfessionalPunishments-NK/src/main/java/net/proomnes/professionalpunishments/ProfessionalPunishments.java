package net.proomnes.professionalpunishments;

import cn.nukkit.plugin.PluginBase;
import lombok.Getter;
import net.proomnes.professionalpunishments.dataaccess.IDataAccess;
import net.proomnes.professionalpunishments.dataaccess.MongoDBDataAccess;
import net.proomnes.professionalpunishments.dataaccess.MySQLDataAccess;
import net.proomnes.professionalpunishments.dataaccess.YamlDataAccess;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

@Getter
public class ProfessionalPunishments extends PluginBase {

    private IDataAccess dataAccess;

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

        // services

        // api

        // listeners

        // commands
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
}
