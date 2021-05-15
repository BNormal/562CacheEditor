package editor.util.rsmv;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.event.WindowEvent;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import javax.swing.GroupLayout;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.event.ListSelectionEvent;

import editor.util.rsmv.ui.RSMVAbstractListModel;
import editor.util.rsmv.ui.RSMVAbstractListModel2;
import editor.util.rsmv.ui.RSMVComponentAdapter;
import editor.util.rsmv.ui.RSMVMouseAdapter;
import editor.util.rsmv.ui.RSMVMouseWheelListener;
import editor.util.rsmv.ui.RSMVWindowAdapter;
import editor.util.rsmv.ui.class8;

@SuppressWarnings("serial")
public class Main extends JFrame implements Runnable {
	public static boolean isOptimizedSelected = false;
	private int posY = 159;
	private int posX = 268;
	private int axisX = 0;
	private int axisY = 0;
	private int axisZ = 500;
	private int screenX = -1;
	private int screenY = -1;
	private String directoryPath = "";
	private static long gcDelay = 0L;
	private ModelRSMV model;
	private JFileChooser fileChooser1;
	private JFileChooser fileChooser2;
	private static Renderer renderer;
	private JList<String> field44;
	private JMenu field45;
	private JMenuBar field46;
	private JMenuItem field47;
	private JScrollPane field48;
	private JMenuItem field49;
	private JMenuItem field50;
	private JCheckBox optimizedRendering;
	private JPanel modelViewerPanel;

	public Main() {
		this.fileChooser1 = new JFileChooser();
		this.fileChooser2 = new JFileChooser();
		this.modelViewerPanel = new JPanel();
		this.field48 = new JScrollPane();
		this.field44 = new JList<String>();
		this.optimizedRendering = new JCheckBox();
		this.field46 = new JMenuBar();
		this.field45 = new JMenu();
		this.field50 = new JMenuItem();
		this.field49 = new JMenuItem();
		this.field47 = new JMenuItem();
		this.setDefaultCloseOperation(3);
		this.setTitle("Runescape Model Viewer");
		Main main = this;
		this.addWindowListener(new RSMVWindowAdapter(this));
		this.modelViewerPanel.setBackground(new Color(0, 126, 200));
		this.modelViewerPanel.addMouseWheelListener(new RSMVMouseWheelListener(this));
		this.modelViewerPanel.addMouseListener(new RSMVMouseAdapter(this));
		this.modelViewerPanel.addComponentListener(new RSMVComponentAdapter(this));
		this.modelViewerPanel.addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent event) {
				main.mouseDragged(main, event);
			}
		});
		GroupLayout var2 = new GroupLayout(this.modelViewerPanel);
		this.modelViewerPanel.setLayout(var2);
		var2.setHorizontalGroup(var2.createParallelGroup(Alignment.LEADING).addGap(0, 536, 32767));
		var2.setVerticalGroup(var2.createParallelGroup(Alignment.LEADING).addGap(0, 318, 32767));
		this.field44.addListSelectionListener((event) -> {
			listSelectionValueChanged(main, event);
		});
		this.field48.setViewportView(this.field44);
		this.optimizedRendering.setText("Optimized Rendering");
		this.optimizedRendering.addActionListener((event) -> {
			actionListenerCallback3(main, event);
		});
		this.field45.setText("File");
		this.field50.setText("Load");
		this.field50.addActionListener((event) -> {
			actionListenerCallback(main, event);
		});
		this.field45.add(this.field50);
		this.field49.setText("Load Folder");
		this.field49.addActionListener((event) -> {
			actionListenerCallback2(main, event);
		});
		this.field45.add(this.field49);
		this.field47.setText("Exit");
		this.field45.add(this.field47);
		this.field46.add(this.field45);
		this.setJMenuBar(this.field46);
		var2 = new GroupLayout(this.getContentPane());
		this.getContentPane().setLayout(var2);
		var2.setHorizontalGroup(
				var2.createParallelGroup(Alignment.LEADING).addGroup(var2.createSequentialGroup().addContainerGap().addComponent(this.field48, -2, 133, -2).addGap(18, 18, 18).addGroup(var2.createParallelGroup(Alignment.LEADING).addComponent(this.optimizedRendering).addComponent(this.modelViewerPanel, -1, -1, 32767)).addContainerGap())
		);
		var2.setVerticalGroup(
				var2.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING, var2.createSequentialGroup().addContainerGap().addGroup(var2.createParallelGroup(Alignment.TRAILING).addGroup(Alignment.LEADING, var2.createSequentialGroup().addComponent(this.optimizedRendering).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.modelViewerPanel, -1, -1, 32767)).addComponent(this.field48, -1, 343, 32767)).addContainerGap())
		);
		this.pack();
		SwingUtilities.updateComponentTreeUI(this);
	}

	public static void main(String[] var0) {
		EventQueue.invokeLater(new class8());
	}

	public void run() {
		while (true) {
			if (this.model != null) {
				try {// defualt 268 : 159
					renderer.updateModel(this.model, posX, this.axisY, posY, this.axisX, this.axisZ);
					renderer.updatePreview(0, 0, this.modelViewerPanel.getGraphics());
					renderer.clear();
				} catch (Exception var5) {
					var5.printStackTrace();
				}
			}

			try {
				Thread.sleep(10L);
			} catch (InterruptedException var3) {
			}
			if (System.currentTimeMillis() - gcDelay > 60000L) {//garbage collect every minute
				System.gc();
				gcDelay = System.currentTimeMillis();
			}
		}
	}

	public final void render() {
		renderer = new Renderer(
				this.modelViewerPanel.getWidth(), this.modelViewerPanel.getHeight(), this.modelViewerPanel
		);
	}

	@SuppressWarnings("unchecked")
	public final void loadFiles() {
		try {
			FileInputStream var1 = new FileInputStream(
					System.getProperty("user.home") + System.getProperty("file.separator") + "RSMV.settings"
			);
			Properties var2;
			(var2 = new Properties()).load(var1);
			String var3 = var2.getProperty("path");
			String var8;
			if ((var8 = var2.getProperty("optimize")) != null) {
				isOptimizedSelected = Boolean.valueOf(var8).booleanValue();
			}

			this.optimizedRendering.setSelected(isOptimizedSelected);
			if (var3 != null) {
				this.directoryPath = var3;
				File var9;
				if ((var9 = new File(this.directoryPath)).isDirectory()) {
					System.out.println("" + this.directoryPath);
					int var6;
					File[] var10;
					String[] var4 = new String[var6 = (var10 = var9.listFiles()).length];

					for (int var5 = 0; var5 < var6; ++var5) {
						var4[var5] = var10[var5].getName();
					}

					this.field44.setModel(new RSMVAbstractListModel2(this, var6, var4));
				}

				var1.close();
			}
		} catch (Exception var7) {
			;
		}
	}

	private void method11() {
		try {
			Properties var1;
			(var1 = new Properties()).setProperty("path", this.directoryPath);
			var1.setProperty("optimize", Boolean.toString(isOptimizedSelected));
			var1.store(
					new FileOutputStream(System.getProperty("user.home") + System.getProperty("file.separator") + "RSMV.settings"), "RSMV"
			);
		} catch (IOException var2) {
			var2.printStackTrace();
		}
	}

	private static byte[] getBytesFromFile(String var0) {
		try {
			int var2;
			byte[] var1 = new byte[var2 = (int) (new File(var0)).length()];
			DataInputStream var4;
			(var4 = new DataInputStream(new BufferedInputStream(new FileInputStream(var0)))).readFully(var1, 0, var2);
			var4.close();
			return var1;
		} catch (Exception var3) {
			return null;
		}
	}

	// $FF: synthetic method
	public static void windowClosingCallback(Main var0, WindowEvent var1) {
		var0.method11();
	}

	// $FF: synthetic method
	public static void mouseWheelMoved(Main var0, MouseWheelEvent var1) {
		int var2 = var1.getWheelRotation();
		var0.axisZ += var2 * 50;
	}

	// $FF: synthetic method
	@SuppressWarnings("static-access")
	public static void mouseReleasedCallback(Main main, MouseEvent event) {
		main.screenY = main.screenX = -1;
		int mouseType = event.getButton();
		if (mouseType == 3) {
			main.posY = 159;
			main.posX = 268;
			main.axisX = 0;
			main.axisY = 0;
			main.axisZ = 500;
		}
	}

	// $FF: synthetic method
	public static void componentResizedCallback(Main var0, ComponentEvent var1) {
		if (var0.modelViewerPanel.getWidth() > 0 && var0.modelViewerPanel.getHeight() > 0) {
			var0.render();
		}
	}

	@SuppressWarnings("static-access")
	public void mouseDragged(Main main, MouseEvent event) {
		int x = event.getX();
		int y = event.getY();
		String button = event.getMouseModifiersText(event.getModifiersEx());
		int mouseType = Character.getNumericValue(button.charAt(button.length() - 1));
		if (mouseType == 1) {
			if (main.screenX != -1 || main.screenY != -1) {
				main.axisX += (main.screenX - x) * 3;
				if (main.axisX < 0) {
					main.axisX += 2048;
				} else if (main.axisX >= 2048) {
					main.axisX = 2048 - main.axisX;
				}

				main.axisY -= (main.screenY - y) * 3;
				if (main.axisY < 0) {
					main.axisY += 2048;
				} else if (main.axisY >= 2048) {
					main.axisY = 2048 - main.axisY;
				}
			}
		} else if (mouseType == 2) {
			if (main.screenX != -1 || main.screenY != -1) {
				main.posX += x - main.screenX;
				if (main.posX < 0) {
					main.posX += 2048;
				} else if (main.posX >= 2048) {
					main.posX = 2048 - main.posX;
				}

				main.posY -= main.screenY - y;
				if (main.posY < 0) {
					main.posY += 2048;
				} else if (main.posY >= 2048) {
					main.posY = 2048 - main.posY;
				}
			}
		}

		main.screenX = x;
		main.screenY = y;
	}

	// $FF: synthetic method
	public static void listSelectionValueChanged(Main main, ListSelectionEvent var1) {
		if (!var1.getValueIsAdjusting()) {
			String fileName = main.field44.getModel().getElementAt(main.field44.getAnchorSelectionIndex()).toString();
			byte[] modelBytes;
			ModelRSMV model;
			if ((modelBytes = getBytesFromFile(main.directoryPath + System.getProperty("file.separator") + fileName))[modelBytes.length - 1] == -1 && modelBytes[modelBytes.length - 2] == -1) {
				if ((model = ModelRSMV.loadModel(modelBytes)).isNewHeader) {
					model.downScaleModel(4);
				}
			} else {
				ModelRSMV.initModel(modelBytes);
				model = ModelRSMV.createEmptyModel();
			}

			model.method38(64, 768, -50, -10, -50, true);
			main.model = model;
		}

	}

	// $FF: synthetic method
	public static void actionListenerCallback3(Main var0, ActionEvent var1) {
		isOptimizedSelected = var0.optimizedRendering.isSelected();
	}

	// $FF: synthetic method
	public static void actionListenerCallback(Main var0, ActionEvent var1) {
		var0.fileChooser1.setFileSelectionMode(0);
		File var2;
		if (var0.fileChooser1.showOpenDialog(var0) == 0 && (var2 = var0.fileChooser1.getSelectedFile()).isFile()) {
			byte[] modelBytes;
			ModelRSMV model;
			if ((modelBytes = getBytesFromFile(var2.getAbsolutePath()))[modelBytes.length - 1] == -1 && modelBytes[modelBytes.length - 2] == -1) {
				if ((model = ModelRSMV.loadModel(modelBytes)).isNewHeader) {
					model.downScaleModel(4);
				}
			} else {
				ModelRSMV.initModel(modelBytes);
				model = ModelRSMV.createEmptyModel();
			}

			model.method38(64, 768, -50, -10, -50, true);
			var0.model = model;
		}

	}

	@SuppressWarnings("unchecked")
	public static void actionListenerCallback2(Main var0, ActionEvent var1) {
		var0.fileChooser2.setFileSelectionMode(1);
		File var5;
		if (var0.fileChooser2.showOpenDialog(var0) == 0 && (var5 = var0.fileChooser2.getSelectedFile()).isDirectory()) {
			var0.directoryPath = var5.getAbsolutePath();
			System.out.println("" + var0.directoryPath);
			int var2;
			File[] var6;
			String[] var3 = new String[var2 = (var6 = var5.listFiles()).length];

			for (int var4 = 0; var4 < var2; ++var4) {
				var3[var4] = var6[var4].getName();
			}

			var0.field44.setModel(new RSMVAbstractListModel(var0, var2, var3));
			var0.method11();
		}

	}
}
