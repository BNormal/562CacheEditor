package editor.util.rsmv.ui;

import javax.swing.AbstractListModel;

import editor.util.rsmv.Main;

@SuppressWarnings({ "serial", "rawtypes" })
public final class RSMVAbstractListModel2 extends AbstractListModel {
   private String[] field2;
   private String[] field4;

   public RSMVAbstractListModel2(Main var1, int var2, String[] var3) {
      this.field4 = var3;
      this.field2 = this.field4;
   }

   public final int getSize() {
      return this.field2.length;
   }

   public final Object getElementAt(int var1) {
      return this.field2[var1];
   }
}
