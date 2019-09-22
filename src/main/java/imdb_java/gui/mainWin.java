package imdb_java.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableModel;
import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;

import java.awt.Component;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import java.awt.Rectangle;

public class mainWin extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JTable table;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(
				            UIManager.getSystemLookAndFeelClassName());
					mainWin frame = new mainWin();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public mainWin() {
		setTitle("JIMDB");
		setIconImage(new ImageIcon(this.getClass().getResource("/imdb.png")).getImage());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 900, 700);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel upperPane = new JPanel();
		upperPane.setAlignmentY(Component.TOP_ALIGNMENT);
		contentPane.add(upperPane, BorderLayout.NORTH);
		
		textField = new JTextField();
		upperPane.add(textField);
		textField.setColumns(10);
		
		JButton btnNewButton = new JButton("");
		btnNewButton.setIcon(new ImageIcon(this.getClass().getResource("/close.png")));
		upperPane.add(btnNewButton);
		
		JCheckBox chckbxNewCheckBox = new JCheckBox("MRU");
		upperPane.add(chckbxNewCheckBox);
		
		JCheckBox chckbxNewCheckBox_1 = new JCheckBox("Todo");
		upperPane.add(chckbxNewCheckBox_1);
		
		JCheckBox chckbxNewCheckBox_2 = new JCheckBox("Pend. IMDB");
		upperPane.add(chckbxNewCheckBox_2);
		
		JCheckBox chckbxNewCheckBox_3 = new JCheckBox("Pend. OMDB");
		upperPane.add(chckbxNewCheckBox_3);
		
		JCheckBox chckbxNewCheckBox_4 = new JCheckBox("Duplicados");
		upperPane.add(chckbxNewCheckBox_4);
		
		JButton btnNewButton_2 = new JButton("");
		btnNewButton_2.setIcon(new ImageIcon(this.getClass().getResource("/refresca.png")));
		upperPane.add(btnNewButton_2);
		
		JButton btnNewButton_1 = new JButton("");
		btnNewButton_1.setIcon(new ImageIcon(this.getClass().getResource("/xml.png")));
		upperPane.add(btnNewButton_1);
		
		JPanel centerPane = new JPanel();
		contentPane.add(centerPane);
		
		table = new JTable();
		table.setFillsViewportHeight(true);
		centerPane.add(table);
		
		JPanel downPane = new JPanel();
		downPane.setAlignmentY(Component.TOP_ALIGNMENT);
		contentPane.add(downPane, BorderLayout.SOUTH);
		
		JButton btnNewButton_3 = new JButton("Change dir");
		btnNewButton_3.setIcon(new ImageIcon(this.getClass().getResource("/Home.png")));
		downPane.add(btnNewButton_3);
		
		JComboBox comboBox = new JComboBox();
		downPane.add(comboBox);
		
		JButton btnNewButton_4 = new JButton("");
		btnNewButton_4.setIcon(new ImageIcon(this.getClass().getResource("/carpeta.png")));
		downPane.add(btnNewButton_4);
		
		JLabel lblNewLabel = new JLabel("New label");
		downPane.add(lblNewLabel);
		
		JButton btnNewButton_5 = new JButton("Refresh");
		btnNewButton_5.setIcon(new ImageIcon(this.getClass().getResource("/refresca.png")));
		downPane.add(btnNewButton_5);
	}

	public void setTableModel(TableModel dataModel) {
		table.setModel(dataModel);
	}
	
}
