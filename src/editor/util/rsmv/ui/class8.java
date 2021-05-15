package editor.util.rsmv.ui;

import editor.util.rsmv.Main;
import editor.util.rsmv.ModelRSMV;

public final class class8 implements Runnable {
   public final void run() {
      Main main;
      (main = new Main()).setTitle("Runescape Model Viewer v0.3");
      main.loadFiles();
      main.setVisible(true);
      main.render();
      (new Thread(main)).start();
      ModelRSMV.load();
   }
}
