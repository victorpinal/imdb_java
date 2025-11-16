package imdb_java.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.table.TableModel;

public class MainForm {

	public JFrame frmImdb;
	private JTable table;
	private JTextField textField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					MainForm window = new MainForm();
					window.frmImdb.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainForm() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmImdb = new JFrame();
		frmImdb.setTitle("IMDB");
		frmImdb.setIconImage(Toolkit.getDefaultToolkit().getImage(MainForm.class.getResource("/img/imdb.png")));
		frmImdb.setBounds(100, 100, 999, 646);
		frmImdb.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel topPanel = new JPanel();
		frmImdb.getContentPane().add(topPanel, BorderLayout.NORTH);
		topPanel.setLayout(new BorderLayout(0, 0));
		
		JPanel filtersPanel = new JPanel();
		topPanel.add(filtersPanel, BorderLayout.EAST);
		
		JButton btnClearSearch = new JButton("");
		btnClearSearch.setIcon(new ImageIcon(MainForm.class.getResource("/img/close.png")));
		filtersPanel.add(btnClearSearch);
		
		JCheckBox chkMRU = new JCheckBox("MRU");
		filtersPanel.add(chkMRU);
		
		JCheckBox chkTodo = new JCheckBox("Todo");
		filtersPanel.add(chkTodo);
		
		JCheckBox chkPendImdb = new JCheckBox("Pend. IMDB");
		filtersPanel.add(chkPendImdb);
		
		JCheckBox chkPendOmdb = new JCheckBox("Pend. OMDB");
		filtersPanel.add(chkPendOmdb);
		
		JCheckBox chkDuplicados = new JCheckBox("Duplicados");
		filtersPanel.add(chkDuplicados);
		
		JButton btnConfig = new JButton("");
		btnConfig.setIcon(new ImageIcon(MainForm.class.getResource("/img/refresca.png")));
		filtersPanel.add(btnConfig);
		
		JButton btnXml = new JButton("");
		btnXml.setIcon(new ImageIcon(MainForm.class.getResource("/img/xml.png")));
		filtersPanel.add(btnXml);
		
		JPanel searchPanel = new JPanel();
		topPanel.add(searchPanel, BorderLayout.CENTER);
		searchPanel.setLayout(new BorderLayout(0, 0));
		
		textField = new JTextField();
		textField.setColumns(10);
		searchPanel.add(textField, BorderLayout.CENTER);
		
		JPanel downPanel = new JPanel();
		frmImdb.getContentPane().add(downPanel, BorderLayout.SOUTH);
		downPanel.setLayout(new BorderLayout(0, 0));
		
		JPanel changeDirPanel = new JPanel();
		downPanel.add(changeDirPanel, BorderLayout.CENTER);
		changeDirPanel.setLayout(new BorderLayout(0, 0));
		
		JButton btnChangeDir = new JButton("Change dir");
		btnChangeDir.setIcon(new ImageIcon(MainForm.class.getResource("/img/Home.png")));
		changeDirPanel.add(btnChangeDir, BorderLayout.WEST);
		
		JComboBox<String> cbxChangeDir = new JComboBox<>();
		changeDirPanel.add(cbxChangeDir);
		
		JPanel statusPanel = new JPanel();
		downPanel.add(statusPanel, BorderLayout.EAST);
		
		JButton btnOpenDir = new JButton("");
		btnOpenDir.setIcon(new ImageIcon(MainForm.class.getResource("/img/carpeta.png")));
		statusPanel.add(btnOpenDir);
		
		JLabel lblStatus = new JLabel("New label");
		lblStatus.setPreferredSize(new Dimension(343, 13));
		statusPanel.add(lblStatus);
		
		JButton btnRefresh = new JButton("Refresh");
		btnRefresh.setIcon(new ImageIcon(MainForm.class.getResource("/img/refresca.png")));
		statusPanel.add(btnRefresh);
		
		JScrollPane scrollPane = new JScrollPane();
		frmImdb.getContentPane().add(scrollPane, BorderLayout.CENTER);
		
		table = new JTable();
		scrollPane.setViewportView(table);
	}

	public void setTableModel(TableModel dataModel) {
		table.setModel(dataModel);
	}

}
