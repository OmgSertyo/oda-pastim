package sertyo.events.manager.dragging;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import sertyo.events.module.Module;
import sertyo.events.utility.render.RenderUtil;


public class Draggable {
   @Expose
   @SerializedName("x")
   private int x;
   @Expose
   @SerializedName("y")
   private int y;
   public int initialXVal;
   public int initialYVal;
   private int startX;
   private int startY;
   private boolean dragging;
   private float width;
   private float height;
   @Expose
   @SerializedName("name")
   private String name;
   private final Module module;

   public Draggable(Module module, String name, int initialXVal, int initialYVal) {
      this.module = module;
      this.name = name;
      this.x = initialXVal;
      this.y = initialYVal;
      this.initialXVal = initialXVal;
      this.initialYVal = initialYVal;
   }

   public final void onDraw(int mouseX, int mouseY) {
      if (this.dragging) {
         this.x = mouseX - this.startX;
         this.y = mouseY - this.startY;
      }

   }

   public final void onClick(int mouseX, int mouseY, int button) {
      if (button == 0 && RenderUtil.isHovered((float)mouseX, (float)mouseY, (float)this.x, (float)this.y, this.width, this.height)) {
         this.dragging = true;
         this.startX = mouseX - this.x;
         this.startY = mouseY - this.y;
      }

   }

   public final void onRelease(int button) {
      if (button == 0) {
         this.dragging = false;
      }

   }

   public int getX() {
      return this.x;
   }

   public void setX(int x) {
      this.x = x;
   }

   public int getY() {
      return this.y;
   }

   public void setY(int y) {
      this.y = y;
   }

   public float getWidth() {
      return this.width;
   }

   public float getHeight() {
      return this.height;
   }

   public void setWidth(float width) {
      this.width = width;
   }

   public void setHeight(float height) {
      this.height = height;
   }

   public String getName() {
      return this.name;
   }

   public Module getModule() {
      return this.module;
   }
}
