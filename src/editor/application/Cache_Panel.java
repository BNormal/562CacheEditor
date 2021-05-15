package editor.application;

import javax.swing.JPanel;
import javax.swing.JLabel;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.awt.event.ActionEvent;
import javax.swing.text.DefaultCaret;

import editor.store.Store;
import javax.swing.JProgressBar;

@SuppressWarnings("serial")
public class Cache_Panel extends JPanel {
	private JTextField textFieldCache;

	/**
	 * Create the panel.
	 */
	public Cache_Panel() {
		setLayout(null);
		
		JLabel lblCache = new JLabel("Cache:");
		lblCache.setBounds(10, 14, 53, 14);
		add(lblCache);
		
		textFieldCache = new JTextField();
		textFieldCache.setText(loadLastOpened());
		textFieldCache.setColumns(10);
		textFieldCache.setBounds(57, 11, 311, 20);
		add(textFieldCache);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 84, 457, 346);
		add(scrollPane);
		txtStatus.setToolTipText("");
		DefaultCaret caret = (DefaultCaret) txtStatus.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		scrollPane.setViewportView(txtStatus);
		
		JButton btnBrowse = new JButton("Browse");
		btnBrowse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String loc = browseDirectory();
				if (loc != null)
					textFieldCache.setText(loc);
			}
		});
		btnBrowse.setBounds(378, 11, 89, 20);
		add(btnBrowse);
		
		JButton button = new JButton("Load");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				txtStatus.append("Loading cache files...\n");
				File file = new File(textFieldCache.getText());
				if (file.isDirectory()) {
					try {
						if (file.list().length == 0) {
							txtStatus.append("Empty directory - ./" + file.getName() + "/\n");
							CacheEditor.setStatus("Empty directory - ./" + file.getName() + "/");
						} else {
							try {
								CacheEditor.STORE = new Store(textFieldCache.getText());
								button.setEnabled(false);
							} catch (IOException e) {
							}
						}
						CacheEditor.loadCacheTabData();
					} catch (Error e) {
						txtStatus.append("Error: " + e.getMessage() + "\n");
						CacheEditor.setStatus("Failed to load caches.");
						CacheEditor.enableTabs(false);
					}
				} else {
					txtStatus.append("Error: Not a directory selected.\n");
					CacheEditor.setStatus("Failed to load cache.");
					CacheEditor.enableTabs(false);
				}
			}
		});
		button.setBounds(378, 35, 89, 23);
		add(button);
		
		JLabel lblMessage = new JLabel("Browse each cache then click load.");
		lblMessage.setBounds(10, 41, 210, 14);
		add(lblMessage);
		progressBar.setStringPainted(true);
		progressBar.setMaximum(600);
		
		progressBar.setBounds(10, 64, 457, 14);
		add(progressBar);

	}
	
	private String loadLastOpened() {
		try {
			BufferedReader reader = new BufferedReader(new FileReader("./data/lastopened.txt"));
			String s;
			while ((s = reader.readLine()) != null) {
				break;
			}
			reader.close();
			return s;
		} catch (IOException e) {
			try {
				File file = new File("./data/lastopened.txt");
				if (file.getParentFile().mkdir()) {
					file.createNewFile();
				} else {
					txtStatus.append("Failed to create directory " + file.getParent() + file.getName() + "\n");
					//throw new IOException("Failed to create directory " + file.getParent() + file.getName());
				}
			} catch (IOException e2) {
				System.out.println(e2.toString());
			}
		}
		return "./cache/";
	}

	private void addLastOpened(String absolutePath) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter("./data/lastopened.txt"));
			writer.write(absolutePath);
			writer.newLine();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	private String browseDirectory() {
		JFileChooser chooser = new JFileChooser(loadLastOpened());
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			File file = chooser.getSelectedFile();
			if (file.isDirectory()) {
				try {
					addLastOpened(file.getAbsolutePath() + "\\");
					if (file.list().length == 0) {
						CacheEditor.setStatus("Empty directory - ./" + file.getName() + "/");
						return null;
					}
					return file.getAbsolutePath() + "\\";
				} catch (Exception e) {
					//e.printStackTrace();
					CacheEditor.setStatus("Bad directory - ./" + file.getName() + "/");
					return null;
				}
			}
		} else {
			return null;
		}
		return null;
	}
	
	public JTextArea txtStatus = new JTextArea();
	public JProgressBar progressBar = new JProgressBar();
}
