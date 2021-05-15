package editor.util.rsmv.ui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import editor.util.rsmv.Main;

public final class RSMVMouseAdapter extends MouseAdapter {
   // $FF: synthetic field
   private Main main;

   public RSMVMouseAdapter(Main main) {
      this.main = main;
   }

   public final void mousePressed(MouseEvent var1) {
   }

   public final void mouseReleased(MouseEvent var1) {
      Main.mouseReleasedCallback(this.main, var1);
   }
}
