package net.proomnes.professionalpunishments.util.messages;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.proomnes.configutils.Config;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class MessageLoader {

    private final Map<String, String> cachedMessages = new HashMap<>();
    private final MiniMessage miniMessage;

    public MessageLoader(final JavaPlugin plugin) {
        this.miniMessage = MiniMessage.miniMessage();

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

    public Component get(final MessageKeys messageKeys, final Object... toReplace) {
        String message = this.cachedMessages.getOrDefault(messageKeys.getKey(), messageKeys.getDefaultMessage());

        if (messageKeys.isPrefix()) {
            message = this.cachedMessages.getOrDefault(MessageKeys.SYSTEM_PREFIX.getKey(), MessageKeys.SYSTEM_PREFIX.getDefaultMessage()) + message;
        }

        int i = 0;
        for (final Object replacement : toReplace) {
            message = message.replace("{" + i + "}", String.valueOf(replacement));
            i++;
        }

        return miniMessage.deserialize(message);
    }

}
