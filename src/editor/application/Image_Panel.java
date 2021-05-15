package editor.application;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JTree;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.event.TreeSelectionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.swing.tree.DefaultTreeModel;

import editor.loaders.ImagesFile;
import editor.store.Store;
import editor.utils.Constants;

import javax.swing.tree.DefaultMutableTreeNode;

import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class Image_Panel extends JPanel {

	/**
	 * Create the panel.
	 */
	public Image_Panel() {
		setLayout(null);
		setSize(464, 478);
		setPreferredSize(new Dimension(464, 500));

		JScrollPane scrollPaneTreeS = new JScrollPane();
		scrollPaneTreeS.setBounds(15, 11, 139, 380);
		add(scrollPaneTreeS);

		treeS.setShowsRootHandles(false);
        treeS.setRootVisible(false);
		treeS.setModel(treeModelS);
		scrollPaneTreeS.setViewportView(treeS);
		treeS.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent evt) {
				if (stopListeners)
					return;
				frameTreeValueChanged(evt, treeS, panelImage, CacheEditor.STORE, true);
			}
		});

		JScrollPane scrollPaneImage = new JScrollPane();
		scrollPaneImage.setBounds(164, 215, 283, 176);
		add(scrollPaneImage);
		panelImage.setName("panelD");

		scrollPaneImage.setViewportView(panelImage);

		JButton btnDump = new JButton("Dump");
		btnDump.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dumpImage(treeS, CacheEditor.STORE);
			}
		});
		btnDump.setBounds(313, 126, 129, 18);
		add(btnDump);

		JButton btnAddSprite = new JButton("Add Sprite");
		btnAddSprite.setEnabled(false);
		btnAddSprite.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

			}
		});
		btnAddSprite.setBounds(313, 80, 129, 18);
		add(btnAddSprite);

		JButton btnDeleteSprite = new JButton("Delete Sprite");
		btnDeleteSprite.setEnabled(false);
		btnDeleteSprite.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

			}
		});
		btnDeleteSprite.setBounds(313, 103, 129, 18);
		add(btnDeleteSprite);

		JButton btnAddFrame = new JButton("Add Frame");
		btnAddFrame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addFrameActionPerformed(e);
			}
		});
		btnAddFrame.setBounds(313, 11, 129, 18);
		add(btnAddFrame);

		JButton btnDeleteFrame = new JButton("Delete Frame");
		btnDeleteFrame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				removeFrameActionPerformed(e);
			}
		});
		btnDeleteFrame.setBounds(313, 34, 129, 18);
		add(btnDeleteFrame);

		JButton btnReplaceFrame = new JButton("Replace Frame");
		btnReplaceFrame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				replaceFrameActionPerformed(e);
			}
		});
		btnReplaceFrame.setBounds(313, 57, 129, 18);
		add(btnReplaceFrame);

	}

	private void dumpImage(JTree t, Store store) {
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			File file = chooser.getSelectedFile();
			if (file.isDirectory()) {
				try {
					ImagesFile oldImagesFile = new ImagesFile(store, selectedId, 0);
					BufferedImage[] images = oldImagesFile.getImages();
					BufferedImage image = images[selectedFrameId];
					if (image != null) {
						File newFile = new File(file.getAbsolutePath() + "/S" + selectedId + " - F" + selectedFrameId + ".png");
						if (!newFile.exists())
							newFile.createNewFile();
						ImageIO.write(image, "PNG", newFile);
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				JOptionPane.showMessageDialog(this, "Make sure you select a folder.");
			}
		}

	}

	private void addFrame(Store store, int id, File file) {
		ImagesFile oldImagesFile = new ImagesFile(store, id, 0);
		BufferedImage[] imagesOriginal = oldImagesFile.getImages();
		BufferedImage[] images = new BufferedImage[imagesOriginal.length + 1];
		for (int i = 0; i < imagesOriginal.length; i++)
			images[i] = imagesOriginal[i];
		try {
			images[imagesOriginal.length] = ImageIO.read(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		ImagesFile newImageFile = new ImagesFile(images);
		newImageFile.setBiggestWidth(oldImagesFile.getBiggestWidth());
		newImageFile.setBiggestHeight(oldImagesFile.getBiggestHeight());
		store.getIndexes()[Constants.IMAGES_INDEX].putFile(id, 0, newImageFile.encodeContainer());

	}

	private void removeFrame(Store store, int id, int frame) {
		ImagesFile oldImagesFile = new ImagesFile(store, id, 0);
		BufferedImage[] imagesOriginal = oldImagesFile.getImages();
		BufferedImage[] images = new BufferedImage[imagesOriginal.length - 1];
		int count = 0;
		for (int i = 0; i < imagesOriginal.length; i++) {
			if (i != frame) {
				images[count] = imagesOriginal[i];
				count++;
			}
		}
		ImagesFile newImageFile = new ImagesFile(images);
		newImageFile.setBiggestWidth(oldImagesFile.getBiggestWidth());
		newImageFile.setBiggestHeight(oldImagesFile.getBiggestHeight());

		store.getIndexes()[Constants.IMAGES_INDEX].putFile(id, 0, newImageFile.encodeContainer());

	}

	private void replaceFrame(Store store, int id, int frame, File file) {
		ImagesFile oldImagesFile = new ImagesFile(store, id, 0);
		BufferedImage[] images = oldImagesFile.getImages();
		if (frame >= images.length)
			return;
		try {
			images[frame] = ImageIO.read(file);
		} catch (IOException e) {
			e.printStackTrace();
		}

		ImagesFile newImageFile = new ImagesFile(images);
		newImageFile.setBiggestWidth(oldImagesFile.getBiggestWidth());
		newImageFile.setBiggestHeight(oldImagesFile.getBiggestHeight());

		store.getIndexes()[Constants.IMAGES_INDEX].putFile(id, 0, newImageFile.encodeContainer());

	}

	private void addFrameActionPerformed(ActionEvent evt) {
		chooser.setMultiSelectionEnabled(false);
		chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			try {
				addFrame(CacheEditor.STORE, selectedId, chooser.getSelectedFile());

				ImagesFile oldImagesFile = new ImagesFile(CacheEditor.STORE, selectedId, 0);
				BufferedImage[] images = oldImagesFile.getImages();
				selectedNode.removeAllChildren();
				for (int i = 0; i < images.length; i++) {
					selectedNode.add(new DefaultMutableTreeNode(i));
				}
				treeModelS.reload(selectedNode);

			} catch (Exception e) {
				JOptionPane.showMessageDialog(this, "Error loading image, please make sure it's an image file.");
			}
		}
	}

	private void removeFrameActionPerformed(ActionEvent evt) {
		removeFrame(CacheEditor.STORE, selectedId, selectedFrameId);

		ImagesFile oldImagesFile = new ImagesFile(CacheEditor.STORE, selectedId, 0);
		BufferedImage[] images = oldImagesFile.getImages();
		selectedNode.removeAllChildren();
		for (int i = 0; i < images.length; i++) {
			selectedNode.add(new DefaultMutableTreeNode(i));
		}
		treeModelS.reload(selectedNode);

	}

	private void replaceFrameActionPerformed(ActionEvent evt) {
		chooser.setMultiSelectionEnabled(false);
		chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			try {
				replaceFrame(CacheEditor.STORE, selectedId, selectedFrameId, chooser.getSelectedFile());
				ImagesFile oldImagesFile = new ImagesFile(CacheEditor.STORE, selectedId, 0);
				BufferedImage[] images = oldImagesFile.getImages();
				selectedNode.removeAllChildren();
				for (int i = 0; i < images.length; i++) {
					selectedNode.add(new DefaultMutableTreeNode(i));
				}
				treeModelS.reload(selectedNode);

			} catch (Exception e) {
				JOptionPane.showMessageDialog(this, "Error loading image, please make sure it's an image file.");
				e.printStackTrace();
			}
		}
	}

	private void frameTreeValueChanged(TreeSelectionEvent evt, JTree treeSprite, JPanel p, Store store, boolean update) {
		try {
			int depth = evt.getPath().getPathCount();
			int type = evt.getPath().getPath()[1].toString().equalsIgnoreCase("sprites") ? 0 : 1;
			if (depth <= 2) {
				if (depth == 2) {
					selectedNode = (DefaultMutableTreeNode) evt.getPath().getPath()[evt.getPath().getPathCount() - 1];
					if (type == 0)
						selectedId = storeSpriteSize;
					else
						selectedId = storeTextureSize;
					selectedFrameId = 0;
					//System.out.println(selectedNode.toString());
				}
				return;
			}
			ImagesFile oldImagesFile = null;
			BufferedImage[] images = null;
			selectedFrameId = 0;
			if (depth == 3) {
				selectedNode = (DefaultMutableTreeNode) evt.getPath().getPath()[evt.getPath().getPathCount() - 1];
				selectedId = Integer.parseInt(evt.getPath().getPath()[evt.getPath().getPathCount() - 1].toString());
				if (type == 0) {
					oldImagesFile = new ImagesFile(store, selectedId, 0);
					images = oldImagesFile.getImages();
				} else {
					//textures go here
				}
			} else if (depth == 4) {
				selectedNode = (DefaultMutableTreeNode) evt.getPath().getPath()[evt.getPath().getPathCount() - 2];
				selectedId = Integer.parseInt(evt.getPath().getPath()[evt.getPath().getPathCount() - 2].toString());
				selectedFrameId = Integer.parseInt(evt.getPath().getPath()[evt.getPath().getPathCount() - 1].toString());
				if (type == 0) {
					oldImagesFile = new ImagesFile(store, selectedId, 0);
					images = oldImagesFile.getImages();
				} else {
					//textures go here
				}
			}
			sprite = images[selectedFrameId];
			if (sprite.getWidth() > panelImage.getWidth() || sprite.getHeight() > panelImage.getHeight())
				p.setPreferredSize(new Dimension(sprite.getWidth(), sprite.getHeight()));
			p.revalidate();
			p.repaint();
			
			/*int id = Integer.parseInt(treeSprite.getLastSelectedPathComponent().toString());
			ImagesFile oldImagesFile = new ImagesFile(store, id, 0);
			BufferedImage[] images = oldImagesFile.getImages();
			if (update) {
				treeNodeFrame.removeAllChildren();
				for (int i = 0; i < images.length; i++)
					treeNodeFrame.add(new DefaultMutableTreeNode(i));
				treeModelFrame.reload(treeNodeFrame);
				treeFrame.expandRow(0);
			}
			if (treeFrame.getLastSelectedPathComponent() == null)
				treeFrame.setSelectionRow(1);
			int frame = Integer.parseInt(treeFrame.getLastSelectedPathComponent().toString());
			sprite = images[frame];
			if (sprite.getWidth() > panelImage.getWidth() || sprite.getHeight() > panelImage.getHeight())
				p.setPreferredSize(new Dimension(sprite.getWidth(), sprite.getHeight()));
			p.revalidate();
			p.repaint();*/
		} catch (Exception e) {

		}
	}
	
	public void loadPanes() {
		stopListeners = true;
		imageNode.removeAllChildren();
		Store store = CacheEditor.STORE;
		double progress = 0;
		/*** Sprites ***/
		storeSpriteSize = CacheEditor.STORE.getIndexes()[Constants.IMAGES_INDEX].getValidArchivesCount();
		for (int i = 0; i < storeSpriteSize; i++) {
			DefaultMutableTreeNode tempNode = new DefaultMutableTreeNode(i);
			spriteNode.add(tempNode);
			ImagesFile oldImagesFile = new ImagesFile(store, i, 0);
			BufferedImage[] images = oldImagesFile.getImages();
			if (images.length > 1)
				for (int j = 0; j < images.length; j++)
					tempNode.add(new DefaultMutableTreeNode(j));
			progress = 50.0 / ((double) storeSpriteSize) * ((double) i);
			CacheEditor.setProgress((int) progress);
		}
		/***************/

		
		/*** Textures ***/
		storeTextureSize = CacheEditor.STORE.getIndexes()[Constants.TEXTURES_INDEX].getValidArchivesCount();
		for (int i = 0; i < storeTextureSize; i++) {
			DefaultMutableTreeNode tempNode = new DefaultMutableTreeNode(i);
			textureNode.add(tempNode);
			ImagesFile oldImagesFile = new ImagesFile(store, i, 0);
			BufferedImage[] images = oldImagesFile.getImages();
			if (images.length > 1)
				for (int j = 0; j < images.length; j++)
					tempNode.add(new DefaultMutableTreeNode(j));
			progress = 50.0 / ((double) storeTextureSize) * ((double) i);
			CacheEditor.setProgress((int) progress + 50);
		}
		/***************/
		imageNode.add(spriteNode);
		imageNode.add(textureNode);
		treeModelS.reload(imageNode);
		treeS.setSelectionRow(0);
		stopListeners = false;

	}

	private int storeSpriteSize;
	private int storeTextureSize;
	private int selectedId = 0;
	private int selectedFrameId = 0;
	private BufferedImage sprite = null;
	private JTree treeS = new JTree();
	private DefaultMutableTreeNode selectedNode = null;
	private DefaultMutableTreeNode imageNode = new DefaultMutableTreeNode("Images");
	private DefaultMutableTreeNode spriteNode = new DefaultMutableTreeNode("Sprites");
	private DefaultMutableTreeNode textureNode = new DefaultMutableTreeNode("Textures");
	private DefaultTreeModel treeModelS = new DefaultTreeModel(imageNode);
	private boolean stopListeners = false;
	private JPanel panelImage = new JPanel() {
		@Override
		public void paintComponent(Graphics g) {
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, panelImage.getWidth(), panelImage.getHeight());
			g.drawImage(sprite, 0, 0, null);
		}
	};

	private JFileChooser chooser = new JFileChooser();
}
