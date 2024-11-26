//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package sertyo.events.ui.csgui.component.impl;

import net.minecraft.util.text.TextFormatting;
import org.apache.commons.lang3.time.DurationFormatUtils;
import sertyo.events.Main;
import sertyo.events.ui.csgui.component.Component;
import sertyo.events.utility.font.Fonts;
import sertyo.events.utility.render.RenderUtility;

import java.awt.Color;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;

public class ProfileComponent extends Component {
    private final String name;

    public ProfileComponent(String name, float width, float height) {
        super(0.0F, 0.0F, width, height);
        this.name = name;
    }

    public void render(int mouseX, int mouseY) {
        int elementsColor = Color.decode("#1E1F30").getRGB();
        String subText = "Подписка заканчивается через ";
        String subFrom = subText.replace(" ", "i");
        String subString = "14:88";

        String[] subSplit = subString.split(":");
        String subExpiration = subSplit[0] + " Дней " + subSplit[1] + " Часов";
        if (subString.equalsIgnoreCase("00:HH")
                || Float.parseFloat(subSplit[0]) > 1000) subExpiration = "Никогда";
        RenderUtility.drawRoundedRect((float)(this.x), (float)(this.y), 300, 50, 10, elementsColor);
        Fonts.msBold[17].drawString(TextFormatting.GRAY + "[" + Main.cheatProfile.getRoleName() + "] " + TextFormatting.WHITE + Main.cheatProfile.getName(), this.x + 54, this.y + 20, -1);
        Fonts.msBold[17].drawString("UID: " + Main.cheatProfile.getId(), this.x + 54, this.y + 30, -1);
        Fonts.msBold[17].drawString("Ваша подписка закончиться: " + subExpiration, this.x + 54, this.y + 38, -1);
        RenderUtility.applyRound(45, 45, 12.0F, 1.0F, () -> {
            RenderUtility.drawProfile((float)this.x + 4, (float)this.y + 3, 45, 45);
        });
    }



    public String getName() {
        return this.name;
    }
}
