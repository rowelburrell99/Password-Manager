package manager;

import java.awt.Desktop;
import java.awt.EventQueue;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import loginsystem.Login;
import loginsystem.AES256;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPasswordField;
import javax.swing.JCheckBox;

public class PassManager extends Login{

	private JFrame frame;
	private JTextField usertext;
	private JTextField webtext;
	private JTextField idtext;
	private JTable table;
	private JFrame guiframe;
	private JPasswordField passtext;
	public String usersess = Login.usersess;
	int keypressed = 300;
	//private JMenuItem mnEncrypt;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PassManager window = new PassManager();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public PassManager() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent e) {
				ShowData();
				timeoutsess.start();
			}
		});
		
		frame.setBounds(100, 100, 950, 350);
	//	frame.setLocationRelativeTo(null); makes form have center position.
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblUsername = new JLabel("Username:");
		lblUsername.setBounds(23, 80, 66, 16);
		frame.getContentPane().add(lblUsername);
		
		usertext = new JTextField();
		usertext.setBounds(124, 75, 172, 26);
		frame.getContentPane().add(usertext);
		usertext.setColumns(10);
		
		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setBounds(23, 118, 74, 16);
		frame.getContentPane().add(lblPassword);
		
		JLabel lblWebsiteurl = new JLabel("Website (URL):");
		lblWebsiteurl.setToolTipText("");
		lblWebsiteurl.setBounds(23, 156, 89, 16);
		frame.getContentPane().add(lblWebsiteurl);
		
		webtext = new JTextField();
		webtext.setBounds(124, 151, 172, 26);
		frame.getContentPane().add(webtext);
		webtext.setColumns(10);
		
		JButton btnAdd = new JButton("Add");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				keypressed = 300;
				if (e.getSource() == btnAdd) {
					keypressed = 300;
				}
				SaveToDatabase();
				
			}
		});
		btnAdd.setBounds(6, 281, 74, 29);
		frame.getContentPane().add(btnAdd);
		
		JButton btnDelete = new JButton("Delete");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				keypressed = 300;
				Delete(idtext.getText());
			}
		});
		btnDelete.setBounds(80, 281, 74, 29);
		frame.getContentPane().add(btnDelete);
		
		JButton btnUpdate = new JButton("Update");
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				keypressed = 300;
				Update(idtext.getText());
			}
		});
		btnUpdate.setBounds(154, 281, 74, 29);
		frame.getContentPane().add(btnUpdate);
		
		JLabel lblId = new JLabel("ID:");
		lblId.setBounds(23, 42, 61, 16);
		frame.getContentPane().add(lblId);
		
		idtext = new JTextField();
		idtext.setBounds(124, 37, 172, 26);
		frame.getContentPane().add(idtext);
		idtext.setColumns(10);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(440, 23, 504, 278);
		frame.getContentPane().add(scrollPane);
		
		table = new JTable();
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String id = table.getValueAt(table.getSelectedRow(), 0).toString();
				SetTextField(id);
				keypressed = 300;
				
			}
		});
		scrollPane.setViewportView(table);
		
		passtext = new JPasswordField();
		passtext.setBounds(124, 113, 172, 26);
		passtext.putClientProperty("JPasswordField.cutCopyAllowed",true); //allows users to copy/cut from the password field, remove if necessary.
		frame.getContentPane().add(passtext);
		
		JCheckBox chckbxShowPassword = new JCheckBox("Show Password");
		chckbxShowPassword.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				keypressed = 300;
				if (chckbxShowPassword.isSelected()) {
					passtext.setEchoChar((char)0);
				}
				else {
					passtext.setEchoChar('*');
				}
				
			}
		});
		chckbxShowPassword.setBounds(300, 114, 128, 23);
		frame.getContentPane().add(chckbxShowPassword);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 944, 22);
		frame.getContentPane().add(menuBar);

		//menuBar.setBounds(0, 0, 50, 22);
		menuBar.setBounds(0, 0, 944, 22);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				keypressed = 300;
				guiframe = new JFrame("Exit");
				if (JOptionPane.showConfirmDialog(guiframe,  "Confirm if you want to exit", "Password Manager",
				JOptionPane.YES_NO_OPTION)== JOptionPane.YES_NO_OPTION){
				System.exit(0);
				}
				
			}
		});
		
		JMenuItem mntmSearch = new JMenuItem("Search");
		mntmSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				keypressed = 300;
				Search search = new Search();
			}
		});
		
		JMenuItem mntmRefresh = new JMenuItem("Refresh");
		mntmRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				keypressed = 300;
				ShowData();
			}
		});
		
		JMenuItem mntmSignout = new JMenuItem("Sign out");
		mntmSignout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				keypressed = 300;
				if (JOptionPane.showConfirmDialog(guiframe,  "Are you sure you want to sign out " + usersess + "?", "Password Manager",
				JOptionPane.YES_NO_OPTION)== JOptionPane.YES_NO_OPTION){
					timeoutsess.stop();
					Login sysLogin = new Login();
					sysLogin.main(null);
					frame.dispose();
				
				}
			}

		});
		
		mnFile.add(mntmSearch);
		mnFile.add(mntmRefresh);
		mnFile.add(mntmSignout);
		mnFile.add(mntmExit);
		
		JMenuItem mnRandpass = new JMenuItem("Generate Password");
		mnRandpass.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				keypressed = 300;
				passtext.setText(randompass(18));
				
			}
		});
		
		JMenuItem mnEncrypt = new JMenuItem("Encrypt");
		mnEncrypt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				keypressed = 300;
				Encrypt();
			}
		});
		//menuBar.add(mnEncrypt);
		
		JMenuItem mnDecrypt = new JMenuItem("Decrypt");
		mnDecrypt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				keypressed = 300;
				Decrypt();			
			}
		});
		//menuBar.add(mnDecrypt);
		
		
		JMenu file = new JMenu ("Encryption");
		menuBar.add(file);
		
		file.add(mnRandpass);
		file.add(mnEncrypt);
		file.add(mnDecrypt);
		
		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
		
		JMenuItem mntmFunctions = new JMenuItem("Functionality");
		mntmFunctions.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				keypressed = 300;
				instructions();
				
			}
		
		});
		mnHelp.add(mntmFunctions);
		
		JButton btnVisit = new JButton("Visit");
		btnVisit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				keypressed = 300;
				Visit();
			}
		});
		btnVisit.setBounds(300, 151, 61, 29);
		frame.getContentPane().add(btnVisit);
		
	}
/*-------------------------------------------------------------------------------------------------------------------------------------------------------------------
 * FUNCTIONS
 -------------------------------------------------------------------------------------------------------------------------------------------------------------------*/
	static Connection con() {
		try {
			String driver = "org.sqlite.JDBC";
			String url = "jdbc:sqlite:mlogin.db";
			Class.forName(driver);
			return DriverManager.getConnection(url, "root", "root");
			
		} catch(Exception e) {
			System.out.println("Connection Failed! " + e);
		}
		
		return null;
	}
	

/* ----------- SAVING TO THE DATABASE --------------- */
/* - It will only run execute these statements if the input fields have one or more characters in, therefore, can't be null.											
 * - It will save into the 'usersess' which is the users sesssion which is identified through their username.
 * - The setstring functions are used to insert the text from the text fields into the database.
 * - Executes showdata function that refreshes the table, so it shows their nearly added data.
 * */	
	
	private void SaveToDatabase() {
		Connection con = con(); 
		try {
			if (idtext.getText().trim().length() >= 1 && usertext.getText().trim().length() >= 1 && passtext.getText().trim().length() >= 1 && webtext.getText().trim().length() >= 1) {
			
			

			String query = ("INSERT INTO "+usersess+" " + "values (?,?,?,?)");
			PreparedStatement ps = con.prepareStatement(query);
			ps.setString(1, idtext.getText());
			ps.setString(2, usertext.getText()); 
			ps.setString(3, passtext.getText()); 
			ps.setString(4, webtext.getText()); 
			ps.execute();
			
			JOptionPane.showMessageDialog(null, "Saved!");
			ShowData(); 
			con.close(); 
			}
			else {
		         JOptionPane.showMessageDialog(null, "Ensure all text fields are filled. Please try again ");
		        
			}
		} catch(Exception e) {
			System.out.println("Error: " + e);
			JOptionPane.showMessageDialog(null, "Error: ID is already in use.");
		}
	}
	
	
/* ----------- DISPLAYING TABLE DATA --------------- */
/* - Adds the columns for the table and formats them (sets the ideal width).
 * - Selects the data from the users database and displays it in their respected columns.
 */
	
	private void ShowData() {
		Connection con = con(); //connects to the database
		DefaultTableModel model = new DefaultTableModel();
		table.setDefaultEditor(Object.class, null); //makes the table no longer editable.
		model.addColumn("ID");	
		model.addColumn("Username"); 
		model.addColumn("Password"); 
		model.addColumn("Website"); 
		try {
			String query = "select * from "+ usersess+" "; 
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(query);
			while (rs.next()) {
				model.addRow(new Object [] {
						rs.getString("id"), 
						rs.getString("username"),
						rs.getString("password"),
						rs.getString("website"),
				
				});
			}
			
			rs.close();
			st.close();
			con.close();
			
			table.setModel(model);
			table.setAutoResizeMode(0);
			table.getColumnModel().getColumn(0).setPreferredWidth(50); 
			table.getColumnModel().getColumn(1).setPreferredWidth(150); 
			table.getColumnModel().getColumn(2).setPreferredWidth(150); 
			table.getColumnModel().getColumn(3).setPreferredWidth(250); 
		} catch(Exception e) {
			System.out.println("Error: " + e);
		}
		
	}
	
/* ------------- TEXT FIELD AUTO COMPLETE ------------- */
/* - It selects data from the same row as the selected ID and input it into the designated fields. 
 * - This is used to autofill the text fields when the user selects on a row in the table.
 */

	private void SetTextField(String id) {
		Connection con = con(); //connects to database
		
		try {
			String query = "SELECT * FROM "+usersess+" " + "WHERE id = ?";  
			PreparedStatement ps = con.prepareStatement(query);
			ps.setString(1, id);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				idtext.setText(rs.getString("ID"));
				usertext.setText(rs.getString("username"));
				passtext.setText(rs.getString("password"));
				webtext.setText(rs.getString("website"));
				keypressed = 300;
			}
			
			rs.close();
			ps.close();
			con.close();
		} catch(Exception e) {
			System.out.println("Error: " + e);
		}
			
	}
	
/* --------------- UPDATE DATA ---------------- */
/* - Will only run execute these statements if the input fields have one or more characters in, therefore, can't be null.
 * - Gets the input from the text fields and uses that to update the fields in the database.
 */
	
	private void Update(String id) {
		Connection con = con();
		try {
			if (idtext.getText().trim().length() >= 1 && usertext.getText().trim().length() >= 1 && passtext.getText().trim().length() >= 1 && webtext.getText().trim().length() >= 1) {
			
			
			String query = "UPDATE "+usersess+" " + "SET username = ?, password = ?, website = ? where id = ?";
			PreparedStatement ps = con.prepareStatement(query);
			ps.setString(1, usertext.getText());
			ps.setString(2, passtext.getText());
			ps.setString(3, webtext.getText());
			ps.setString(4, id);
			ps.execute();
			
			
			ps.close();
			con.close();
			JOptionPane.showMessageDialog(null, "Update Successful!");
			ShowData();
			}
			else {
		         JOptionPane.showMessageDialog(null, "Ensure all text fields are filled. Try again. ");
			}
		} catch(Exception e) {
			System.out.println("Error: " + e);
		}
	}
	
/* --------------- DELETE DATA ---------------- */
/* - The row associated to the number entered in the 'ID' textfield will be removed.
 */
	
	private void Delete (String id) {
		Connection con = con();
		try {
			if (JOptionPane.showConfirmDialog(guiframe,  "Are you sure you want to remove the data attached to ID: " + idtext.getText().toString(), "Delete Data",
			JOptionPane.YES_NO_OPTION)== JOptionPane.YES_NO_OPTION){
				String query = "DELETE FROM "+usersess+" " + "WHERE id = ?"; 
				PreparedStatement ps = con.prepareStatement(query);
				ps.setString(1,  id);
				ps.execute();
				
				ps.close();
				con.close();
				JOptionPane.showMessageDialog(null, "Data has been removed!");
				ShowData();
			}
		} catch(Exception e) {
			System.out.println("Error: " + e);
		}
	}
	
/* --------------- AES ENCRYPTION ---------------- */
/* - implements the AES encryption method using a secretkey, which is applied to the text located in the password text field.
 */
	
	private void Encrypt() {
		if (passtext.getText().trim().length() >= 1) {
		String secretKey = "pleaseencryptme!!!!";
		String originalString = passtext.getText();
	    String encryptedString = AES256.encrypt(originalString, secretKey) ;
	    if (passtext.getText().length() <= 22) {
	    	passtext.setText(encryptedString);
	    }
	    else {
	    	JOptionPane.showMessageDialog(null, "Password is already encrypted.");
	    }
	    }
		else {
			JOptionPane.showMessageDialog(null, "Ensure password text field has input.");
		}

	}
	

/* --------------- AES DECRYPTION ---------------- */
/* - Decrypts the previously encrypted method using the same secretkey, which is applied to the text located in the password text field.
 */
	
	private void Decrypt() {
		if (passtext.getText().trim().length() >= 1) {
		String secretKey = "pleaseencryptme!!!!";
		String decryptedString = AES256.decrypt(passtext.getText(), secretKey);
		if (passtext.getText().length() <= 19) {
			JOptionPane.showMessageDialog(null, "Password has not been encrypted.");
		}
		else {
			passtext.setText(decryptedString);
		}
		}
		else {
			JOptionPane.showMessageDialog(null, "Ensure password text field has input.");
		}
	}
	
/* --------------- VISIT PAGE ---------------- */
/*  Opens a browser with the text located in the website text field as a webpage.
 */		

	private void Visit() {
	   if (webtext.getText().trim().length() >= 1) {
		   try {
	   
	        String myurl = "https://" + webtext.getText();   // won't work unless 'https://' is at the beginning.
	        
	        java.awt.Desktop.getDesktop().browse(java.net.URI.create(myurl));
	    }
	    catch (Exception e2) {
	        e2.printStackTrace();
	     }
	   }
	   else {
		   JOptionPane.showMessageDialog(null, "Please ensure the website text field has an input.");
	   }
	
	}
	
/* --------------- TIME OUT SESSION ---------------- */
/*  Starts a timer when the program opens, if the timer reaches 0. It signs the user out, i.e. closes the application.
 *  Every time an action is performed on the application, the timer is reset to 300 seconds (5 minutes).
 */		
	
	Thread timeoutsess = new Thread() {
	    public void run() {
	        try {
				do {
					keypressed--;
					Thread.sleep(1000);
					System.out.println(keypressed);
				} while (keypressed > 0);
					Login sysLogin = new Login();
					sysLogin.main(null);
					frame.dispose();
					JOptionPane.showMessageDialog(null, "You have been signed out.");
					keypressed = 300;
	        } catch(InterruptedException v) {
	            System.out.println("timeout error.");
	        }
	    }  
	};
	
/*--------------- RANDOM PASSWORD GENERATOR ---------------- */
/* 	 Uses the strings from alphabet, numbers and symbol to create an array of random letters according to the length set (this case being 18).
 */
	
	public String randompass (int length) {
		String alphabet = "abcdefghijklmnopqrstuvwxyz";
		String numbers = "0123456789";
		String symbols = "!#$%()*+-/<>=?@^_~";
		char[] randompass = (alphabet + alphabet.toUpperCase() + numbers + symbols).toCharArray();
		
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < length; i++) {
			result.append(randompass[new Random().nextInt(randompass.length)]);
		}
		return result.toString();	
	} 
	
/*--------------- INSTRUCTIONS ---------------- */
/* 	 Instructions on how the application works.
 */
	public void instructions() {
		JOptionPane.showMessageDialog(null, "<html> <b> <font size='4'> Buttons: </font> </b> </html>" + "\n" + 
				"Add: Once all text fields have data input, this button will store this information into the database." + "\n" +
				"Delete: Data that is in the text field gets removed from the database." + "\n" +
				"Update: Using the ID in the text field, the username, password and website can be changed so that the row in the database is updated." + "\n" +
				"Visit: Opens user's default browser with the address stored in the website text field. Ensure the URL address is correct!" + "\n" + "\n" +
				"<html> <b> <font size='4'> Toolbar: </font> </b> </html>" + "\n" +
				"File: Has two menu  items - Search & Exit." + "\n" +
				"\t" + "\t" + "\t" + "\t" + "\t" + "\t" + "Search: Searches for the website in the database, if found provides the user with their username and password for it." + "\n" +
				"\t" + "\t" + "\t" + "\t" + "\t" + "\t" + "Refresh: Refreshes the database." + "\n" +
				"\t" + "\t" + "\t" + "\t" + "\t" + "\t" + "Sign out: Logs the user out by returning to the login window." + "\n" +
				"\t" + "\t" + "\t" + "\t" + "\t" + "\t" + "Exit: Prompts the user on whether they want to close the application or not." + "\n" +
				"Encryption: Has three menu items - Encrypt, Decrypt & Hash." + "\n" +
				"\t" + "\t" + "\t" + "\t" + "\t" + "\t" + "Generate Password: Creates a random string of 18 characters, making use of letters, numbers and symbols." + "\n" +
				"\t" + "\t" + "\t" + "\t" + "\t" + "\t" + "Encrypt: The text input in the password field will be encrypted through the use of AES algorithm." + "\n" +
				"\t" + "\t" + "\t" + "\t" + "\t" + "\t" + "Decrypt: Returns the plaintext from the encrypted text.");
	}
	
	
}
