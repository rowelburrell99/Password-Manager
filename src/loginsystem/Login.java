package loginsystem;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import manager.PassManager;

import javax.swing.JPasswordField;
import javax.swing.JSeparator;

public class Login {

	private JFrame frame;
	public JTextField usertext;
	private JPasswordField passtext;
	private JFrame loginsystem;
	public static String usersess;
	private JTextField pintext;
	int totalattempts = 3;
	int timeout = 60;
	
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login window = new Login();
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
	public Login() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 500, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblmUsername = new JLabel("Username:");
		lblmUsername.setBounds(48, 41, 76, 16);
		frame.getContentPane().add(lblmUsername);
		
		JLabel lblmPassword = new JLabel("Password:");
		lblmPassword.setBounds(48, 79, 76, 16);
		frame.getContentPane().add(lblmPassword);
		
		usertext = new JTextField();
		usertext.setBounds(156, 36, 130, 26);
		frame.getContentPane().add(usertext);
		usertext.setColumns(10);
		
		passtext = new JPasswordField();
		passtext.setBounds(156, 74, 130, 26);
		frame.getContentPane().add(passtext);
	
	
		JButton btnLogin = new JButton("Login");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (totalattempts == 0) {
					JOptionPane.showMessageDialog(null, "Time until session is unlocked: " + timeout + "s."); //user has ran out of attempts - print message.
				}
				Connection conlogin = conlogin();
				try {
				    String queryString = "SELECT username, password, pin FROM mlogin_table where username=? and password=? and pin=?"; //finds corresponding username, password and pin in database.
				    PreparedStatement ps = conlogin.prepareStatement(queryString);
				    
					String secretKey = "pleaseencryptme!!!!";
					String originalString = passtext.getText();
				    String encryptedString = AES256.encrypt(originalString, secretKey) ;
				    
				    ps.setString(1, usertext.getText());
				    ps.setString(2, encryptedString); //gathers the string input into the text field and encrypts it before matching it with the data in the database.
				    ps.setString(3, pintext.getText());
				    ResultSet results = ps.executeQuery();
				    if (totalattempts != 0) { //ensures user still has available attempts.
					     if (results.next()) {
					    	 usersess = usertext.getText();
					    	JOptionPane.showMessageDialog(null, "Welcome " + usersess);
					    	frame.dispose(); //close login system.
					    	PassManager login = new PassManager();
					    	PassManager.main(null); //open the password manager.
					    }
					    else {
					    	totalattempts--;
					         JOptionPane.showMessageDialog(null, "Invalid login details. Please try again. \nAttempts left: " + totalattempts);
								usertext.setText(null); //clears text fields.
								passtext.setText(null);
								pintext.setText(null);
								System.out.println(totalattempts);
								if (totalattempts == 0) {
								JOptionPane.showMessageDialog(null, "Your are currently locked out. Please try again in 60s.");
								timeoutsess.start(); //start lockout countdown.
								} 
					    }
				    }
				    
				    results.close(); //stop executing queries.
				    conlogin.close(); //stop connection
				} catch(Exception evt) {
				}
			}
				
		});
		btnLogin.setBounds(34, 228, 93, 29);
		frame.getContentPane().add(btnLogin);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(34, 204, 433, 12);
		frame.getContentPane().add(separator);
		
/* --------------- REGISTER ---------------- */
/*  Opens the registration window.
 * 	If the user has been locked out a message will appear instead, informing them how long left.
 */		
		
		JButton btnRegister = new JButton("Register");
		btnRegister.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			if (totalattempts == 0) {
				JOptionPane.showMessageDialog(null, "Time until session is unlocked: " + timeout + "s.");
			}
			{
				Register register = new Register();
			}
			}
		});
		btnRegister.setBounds(139, 228, 83, 29);
		frame.getContentPane().add(btnRegister);
		
		pintext = new JTextField();
		pintext.setBounds(156, 112, 76, 26);
		frame.getContentPane().add(pintext);
		pintext.setColumns(10);
		
		JLabel lblPin = new JLabel("Pin (6-digits):");
		lblPin.setBounds(48, 117, 96, 16);
		frame.getContentPane().add(lblPin);
		
/* --------------- FORGOTTEN PASSWORD ---------------- */
/*  Opens the forgotten password window.
 * 	If the user has been locked out a message will appear instead, informing them how long left.
 */
		
		JButton btnForgottenPassword = new JButton("Forgotten Password ?");
		btnForgottenPassword.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			if (totalattempts == 0) {
				JOptionPane.showMessageDialog(null, "Time until session is unlocked: " + timeout + "s.");
			}
			else {
				Forgot forgot = new Forgot();
			}
			}
		});
		btnForgottenPassword.setBounds(310, 228, 157, 29);
		frame.getContentPane().add(btnForgottenPassword);
		}
		
/* --------------- DATABASE CONNECTION ---------------- */
/*  Connects to the database.
 */
	
		static Connection conlogin() {
			try {
				String driver = "org.sqlite.JDBC";
				String url = "jdbc:sqlite:mlogin.db";
				Class.forName(driver);
				return DriverManager.getConnection(url, "root", "root");

				
			} catch(Exception e) {
				System.out.println("Connection Failed! " + e); //if database cannot be connected to print message.
			}
			
			return null;
		}
		
/* --------------- TIME OUT SESSION ---------------- */
/*  If the user fails their three attempts to login, lock them out of the application for 60 seconds.
 */		
		Thread timeoutsess = new Thread() {
		    public void run() {
		        try {
					do {
						usertext.setEditable(false); //Disabled textbox input whilst locked out.
						passtext.setEditable(false);
						pintext.setEditable(false);
						timeout--;
						Thread.sleep(1000);
						System.out.println(timeout);
					} while (timeout > 0);
						usertext.setEditable(true); //Enables textbox input.
						passtext.setEditable(true);
						pintext.setEditable(true);
						timeout = 60;
						totalattempts = 3; 
		        } catch(InterruptedException v) {
		            System.out.println("timeout error.");
		        }
		    }  
		};
		
}

