package net.proomnes.professionalpunishments.util.messages;

import lombok.Getter;

@Getter
public enum MessageKeys {

    /*
      SYSTEM MESSAGES
     */
    SYSTEM_PREFIX("system.prefix", "§8» §bPunishments §8| §7", false),

    /*
      PLUGIN MESSAGES
     */

    /*
      UI DISPLAYS
     */
    UI_BUTTON_BACK("ui.button.back", "§8» §cBack", false),


    ;

    private final String key;
    private final String defaultMessage;
    private final boolean prefix;

    MessageKeys(final String key, final String defaultMessage, final boolean prefix) {
        this.key = key;
        this.defaultMessage = defaultMessage;
        this.prefix = prefix;
    }

}
