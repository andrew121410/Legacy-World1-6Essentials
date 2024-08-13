package com.andrew121410.mc.world16utils.chat;

@Deprecated
public class MinimotdTranslator {

    public static String translate(String message) {
        String[][] formatMappings = {
                {"<black>", "&0"},
                {"<dark_blue>", "&1"},
                {"<dark_green>", "&2"},
                {"<dark_aqua>", "&3"},
                {"<dark_red>", "&4"},
                {"<dark_purple>", "&5"},
                {"<gold>", "&6"},
                {"<gray>", "&7"},
                {"<dark_gray>", "&8"},
                {"<blue>", "&9"},
                {"<green>", "&a"},
                {"<aqua>", "&b"},
                {"<red>", "&c"},
                {"<light_purple>", "&d"},
                {"<yellow>", "&e"},
                {"<white>", "&f"},
                {"<magic>", "&k"},
                {"<bold>", "&l"},
                {"<strikethrough>", "&m"},
                {"<underline>", "&n"},
                {"<italic>", "&o"},
                {"<reset>", "&r"}
        };

        for (String[] mapping : formatMappings) {
            message = message.replace(mapping[0], mapping[1]);
        }

        return message;
    }
}
