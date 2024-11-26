package net.minecraft.client.gui.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import net.minecraft.client.gui.DialogTexts;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class ConfirmOpenLinkScreen extends ConfirmScreen
{


    public ConfirmOpenLinkScreen(BooleanConsumer p_i51121_1_, String p_i51121_2_, boolean p_i51121_3_)
    {
        super(p_i51121_1_, new TranslationTextComponent(p_i51121_3_ ? "chat.link.confirmTrusted" : "chat.link.confirm"), new StringTextComponent(p_i51121_2_));
        this.confirmButtonText = (ITextComponent)(p_i51121_3_ ? new TranslationTextComponent("chat.link.open") : DialogTexts.GUI_YES);
        this.cancelButtonText = p_i51121_3_ ? DialogTexts.GUI_CANCEL : DialogTexts.GUI_NO;
    }

    protected void init()
    {
            this.callbackFunction.accept(true);

    }

    /**
     * Copies the link to the system clipboard.
     */


}
