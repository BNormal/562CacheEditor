package editor.util.rsmv.ui;

import javax.swing.AbstractListModel;

import editor.util.rsmv.Main;

@SuppressWarnings({ "rawtypes", "serial" })
public final class RSMVAbstractListModel extends AbstractListModel {
   private String[] field24;
   private String[] field26;

   public RSMVAbstractListModel(Main var1, int var2, String[] var3) {
      this.field26 = var3;
      this.field24 = this.field26;
   }

   public final int getSize() {
      return this.field24.length;
   }

   public final Object getElementAt(int var1) {
      return this.field24[var1];
   }
}
