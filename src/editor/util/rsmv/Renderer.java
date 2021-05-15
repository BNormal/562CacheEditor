package editor.util.rsmv;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ColorModel;
import java.awt.image.DirectColorModel;
import java.awt.image.ImageConsumer;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;

public final class Renderer implements ImageObserver, ImageProducer {
   private int[] screen;
   private int[] pixels;
   private final int width;
   private final int height;
   private final ColorModel colorModel = new DirectColorModel(32, 16711680, '\uff00', 255);
   private ImageConsumer imageProducer;
   private final Image generated3DModelImage;

   public Renderer(int width, int height, Component component) {
      ModelCanvas.setLighting(0.8D);
      //ModelCanvas.method1();
      ModelCanvas.setDimensions(width, height);
      this.screen = ModelCanvas.screen;
      this.width = width;
      this.height = height;
      this.pixels = new int[width * height];
      this.generated3DModelImage = component.createImage(this);
      //this.generatePixels();
      component.prepareImage(this.generated3DModelImage, this);
      this.initScreenProperties();
   }

   private void initScreenProperties() {
      int[] pixels = this.pixels;
      int width = this.width;
      int height = this.height;
      panelProperties.pixels = pixels;
      panelProperties.width = width;
      panelProperties.height = height;
      if(width > panelProperties.width) {
         width = panelProperties.width;
      }
      if(height > panelProperties.height) {
         height = panelProperties.height;
      }
      panelProperties.width2 = width;
      panelProperties.height2 = height;
      panelProperties.field65 = panelProperties.width2 - 1;
   }

   public final void updatePreview(int var1, int var2, Graphics g) {
      this.generatePixels();
      g.drawImage(this.generated3DModelImage, 0, 0, this);
   }

   public final synchronized void addConsumer(ImageConsumer c) {
      this.imageProducer = c;
      c.setDimensions(this.width, this.height);
      c.setProperties(null);
      c.setColorModel(this.colorModel);
      c.setHints(14);
   }

   public final synchronized boolean isConsumer(ImageConsumer var1) {
      return this.imageProducer == var1;
   }

   public final synchronized void removeConsumer(ImageConsumer var1) {
      if(this.imageProducer == var1) {
         this.imageProducer = null;
      }

   }

   public final void startProduction(ImageConsumer var1) {
      this.addConsumer(var1);
   }

   public final void requestTopDownLeftRightResend(ImageConsumer var1) {
      System.out.println("TDLR");
   }

   private synchronized void generatePixels() {
         this.imageProducer.setPixels(0, 0, this.width, this.height, this.colorModel, this.pixels, 0, this.width);
         this.imageProducer.imageComplete(2);
   }
   
   public final boolean imageUpdate(Image arg0, int arg1, int arg2, int arg3, int arg4, int arg5) {
      return true;
   }

   public final void updateModel(ModelRSMV model, int x, int axisX, int y, int axisY, int axisZ) {
      this.initScreenProperties();
      ModelCanvas.screen = this.screen;
	   if (axisY < 0)
		   axisY = 0;
	   if (axisX < 0)
		   axisX = 0;
      int var7 = ModelCanvas.field16[axisX] * axisZ >> 16;
      axisZ = ModelCanvas.field17[axisX] * axisZ >> 16;
      if(model != null) {
         model.setViewPoint(0, axisY, 0, axisX, 0, var7, axisZ);
         ModelCanvas.screenPosX = x;
         ModelCanvas.screenPosY = y;
      } else {
         throw new NullPointerException("You cant render a null model");
      }
   }

   public final void clear() {
      this.initScreenProperties();
      int screen = panelProperties.width * panelProperties.height;
      //set background color
      for(int i = 0; i < screen; ++i) {
         panelProperties.pixels[i] = 31928;
      }

   }
}
