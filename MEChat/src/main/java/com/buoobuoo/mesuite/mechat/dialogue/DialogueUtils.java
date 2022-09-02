package com.buoobuoo.mesuite.mechat.dialogue;

import com.buoobuoo.mesuite.mechat.ChatManager;
import com.buoobuoo.mesuite.meutils.Colour;
import com.buoobuoo.mesuite.meutils.unicode.CharRepo;
import com.buoobuoo.mesuite.meutils.unicode.UnicodeSpaceUtil;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class DialogueUtils {

    public static void sendDialogueBox(Player player, CharRepo icon, String str){
        sendDialogueBox(player, icon, str, null);
    }

    public static void sendDialogueBox(Player player, CharRepo icon, String str, TextComponent nextButton) {
        int charsPerLine = 54;

        String spacing = "            ";
        String[] words = str.split(" ");
        List<String> lines = new ArrayList<>();

        //ignore spacing on first line
        String line = "";
        for (String word : words) {
            if ((line + " " + word).length() <= charsPerLine && !word.contains("\n")) {
                line = line + " " + word;
            } else {
                lines.add(line.replace("\n", ""));

                line = spacing;
                line = line + " " + word;
            }
        }
        lines.add(line.replace("\n", ""));

        for (int i = 0; i < (nextButton == null ? 6 : 5); i++) {
            if (lines.size() - 1 < i)
                lines.add(" ");
        }

        for (int i = 0; i < lines.size(); i++) {
            String out = lines.get(i);
            if (i == 0) {
                out = out.trim();
                player.sendMessage(ChatManager.OVERRIDE_TAG + Colour.format(CharRepo.UI_DIALOGUE_BORDER + UnicodeSpaceUtil.getNeg(47) + icon + UnicodeSpaceUtil.getPos(1) + " " + out));
            } else {
                player.sendMessage(ChatManager.OVERRIDE_TAG + Colour.format(out));
            }
        }

        if (nextButton != null) {
            nextButton.setText(ChatManager.OVERRIDE_TAG + nextButton.getText());

            player.spigot().sendMessage(nextButton);
        }
    }
}
