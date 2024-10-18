package sertyo.events.ui.csgui.component;

public class Component {
   public float x;
   public float y;
   public float width;
   public float height;

   public Component(float x, float y, float width, float height) {
      this.x = x;
      this.y = y;
      this.width = width;
      this.height = height;
   }

   public void render(int mouseX, int mouseY) {
   }

   public void mouseClicked(double mouseX, double mouseY, int mouseButton) {
   }

   public void mouseReleased(double mouseX, double mouseY, int state) {
   }

   public void keyTyped(int keyCode) {
   }

   public boolean isVisible() {
      return true;
   }

   public void setX(float x) {
      this.x = x;
   }

   public void setY(float y) {
      this.y = y;
   }

   public void setWidth(float width) {
      this.width = width;
   }

   public void setHeight(float height) {
      this.height = height;
   }
}
