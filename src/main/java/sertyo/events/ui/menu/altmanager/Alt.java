package sertyo.events.ui.menu.altmanager;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import sertyo.events.utility.render.RenderUtil;
import sertyo.events.utility.font.Fonts;


import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
public class Alt extends Element {
    private String name;
    private String date;
    private boolean edit = false;
    public RandomString randomString = new RandomString(12);
    protected TextFieldWidget textfield;
    public float animation = 0;
    public float animation2 = 0;
    public boolean toRemove;
    private String waring = "Ник слишком короткий. Минимум 3 символа!";

    public Alt(){
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        Date date = new Date();
        this.name = randomString.nextString();
        this.date = formatter.format(date);
        this.width = 250;
        this.height = 30;
        textfield = new TextFieldWidget(Minecraft.getInstance().fontRenderer, (int) (this.width / 2), 50, 100, 20, new TranslationTextComponent("FLEX")) {};
        textfield.setMaxStringLength(14);
        textfield.setVisible(true);
        textfield.setEnabled(true);
        textfield.setText(name);
        textfield.setEnableBackgroundDrawing(false);
    }

    public Alt(String name){
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        Date date = new Date();
        this.name = name;
        this.date = formatter.format(date);
        this.width = 250;
        this.height = 30;
        textfield = new TextFieldWidget(Minecraft.getInstance().fontRenderer, (int) (this.width / 2), 50, 100, 20, new TranslationTextComponent("FLEX")) {};
        textfield.setMaxStringLength(14);
        textfield.setVisible(true);
        textfield.setEnabled(true);
        textfield.setText(name);
        textfield.setEnableBackgroundDrawing(false);
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        GlStateManager.pushMatrix();
        GL11.glTranslated(this.x + width / 2, this.y + height / 2, 0);
        GL11.glScaled(animation, animation, 1);
        GL11.glTranslated((-this.x) -width / 2, (-this.y) -height / 2, 0);

        if (toRemove){
            GL11.glTranslated(0, -animation2, 0);
                AltManagerScreen.alts.remove(this);
                toRemove = false;

        }

        RenderUtil.Render2D.drawRoundedRect((float) this.x, (float) this.y, (float) this.width, (float) this.height, 5, new Color(30, 30, 30, 200).getRGB());
        int color = textfield.getText().length() > 2 ? -1 : new Color(255, 0, 0).getRGB();

        boolean isWarn = this.textfield.getText().equals(waring);

        RenderUtil.Render2D.drawRect((float) (this.x + 10), (float) (this.y + 13), 4, 4, Color.RED.getRGB());

        Fonts.msBold[16].drawString(new MatrixStack(), !edit ? this.textfield.getText() : this.textfield.getText() + TextFormatting.DARK_GRAY + " (typing)", this.x + 20, this.y + this.height / 2 - 5, color);



        textfield.setFocused2(edit);

        //RenderUtil.drawRound(textfield.x, textfield.y, textfield.getWidth(), textfield.getHeightRealms(), 3, new Color(10, 10, 10).getRGB());
        textfield.x = (int) (this.x + this.width / 2) - textfield.getWidth() / 2;
        textfield.y = (int) ((int) this.y + this.height / 2) - textfield.getHeightRealms() / 2;

        if(mc.getSession().getUsername().toLowerCase().equals(textfield.getText().toLowerCase())){
            String active = "Active";
            Fonts.msBold[8].drawString(new MatrixStack(), active, this.x + this.width - Fonts.msBold[8].getWidth(active) - 10, this.y + 12, new Color(115, 255, 0).getRGB());
        }

        GlStateManager.popMatrix();
    }

    public void keyPressed(int keyCode, int scanCode, int modifiers) {
        if (textfield.getText().contains(waring)){
            textfield.setText("");
            textfield.setMaxStringLength(15);
        }
        if (keyCode == GLFW.GLFW_KEY_ENTER && edit){
            apply();
        }
        textfield.keyPressed(keyCode, scanCode, modifiers);
    }

    public void charTyped(char codePoint, int modifiers) {
        if (textfield.getText().contains(waring)){
            textfield.setText("");
            textfield.setMaxStringLength(15);
        }
        textfield.charTyped(codePoint, modifiers);
    }

    @Override
    public void mouseClicked(int x, int y, int button) {
        if (RenderUtil.isHovered((float) (this.x + 20), (float) (this.y + this.height / 2 - 5),Fonts.msBold[16].getWidth(this.textfield.getText()),12, x, y) && button == 0){
            edit = true;
        }
        if (RenderUtil.isHovered((float) (this.x + 10), (float) (this.y + 13), 4, 4, x, y) && button == 0){
            toRemove = true;
        }
        if (RenderUtil.isHovered((float) ((float) this.x + this.width - 31), (float) (float) this.y + 3, 8, 8, x, y) && button == 0){
           apply();
        }
        if (button == 0 && collided(x, y) && !edit){
            mc.getSession().username = textfield.getText();
        }
    }

    private void apply(){
        if (textfield.getText().length() > 2 && !textfield.getText().equals(waring)) {
            edit = false;
            mc.getSession().username = textfield.getText();
        }else{
            textfield.setMaxStringLength(100);
            textfield.setText(waring);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
