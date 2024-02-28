package net.proomnes.professionalpunishments.util.messages;

import net.proomnes.configutils.Config;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class MessageLoader {

    private final Map<String, String> cachedMessages = new HashMap<>();

    public MessageLoader(final JavaPlugin plugin) {
        Config.saveResource("lang/" + plugin.getConfig().getString("settings.lang") + ".yml", plugin);
        Config.saveResource("lang/en-us.yml", plugin);
        Config.saveResource("lang/de-de.yml", plugin);

        final Config config = new Config(plugin.getDataFolder() + "/lang/" + plugin.getConfig().getString("settings.lang") + ".yml", Config.YAML);
        config.getAll().forEach((key, value) -> {
            if (value instanceof String) {
                this.cachedMessages.put(key, (String) value);
            }
        });
    }

    public String get(final MessageKeys messageKeys, final Object... toReplace) {
        String message = this.cachedMessages.getOrDefault(messageKeys.getKey(), messageKeys.getDefaultMessage());

        if (messageKeys.isPrefix()) {
            message = this.cachedMessages.get("system.prefix") + message;
        }

        int i = 0;
        for (final Object replacement : toReplace) {
            message = message.replace("{" + i + "}", String.valueOf(replacement));
            i++;
        }

        message = message.replace("&", "§");

        return message;
    }

}