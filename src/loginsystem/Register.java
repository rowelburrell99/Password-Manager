package loginsystem;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JSeparator;
import javax.swing.JTextField;

public class Register extends JFrame implements ActionListener{
    JLabel lblUser,lblPass;
    JTextField usertext;
    private JPasswordField passtext;
    private JPasswordField confirmtext;
    private JTextField pintext;
    private JTextField hinttext;
	public static String usersess;
	public static String regipass;
	public static String aespass;

      public Register(){
          super("Register");
          setVisible(true);
          setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); //changing this from exit to dispose on close keeps the main app open.
          setLocationRelativeTo(null);
          setSize(525,301);
          
          lblUser = new JLabel("Username:");
          lblUser.setBounds(80, 28, 66, 20);
          usertext = new JTextField(20);
          usertext.setBounds(148, 28, 150, 20);
          lblPass = new JLabel("Password:");
          lblPass.setBounds(80, 63, 63, 20);
          getContentPane().setLayout(null);
          getContentPane().add(lblUser);
          getContentPane().add(usertext);
          getContentPane().add(lblPass);
          
          passtext = new JPasswordField();
          passtext.setBounds(148, 60, 150, 26);
          getContentPane().add(passtext);
          
          JLabel lblConfirmPassword = new JLabel("Confirm Password:");
          lblConfirmPassword.setBounds(25, 98, 126, 20);
          getContentPane().add(lblConfirmPassword);
          
          confirmtext = new JPasswordField();
          confirmtext.setBounds(148, 95, 150, 26);
          getContentPane().add(confirmtext);
          
          pintext = new JTextField();
          pintext.setBounds(148, 128, 82, 26);
          getContentPane().add(pintext);
          pintext.setColumns(10);
          
          JLabel lblPin = new JLabel("Pin (6-digits):");
          lblPin.setBounds(53, 133, 93, 16);
          getContentPane().add(lblPin);
          
          JLabel lblHint = new JLabel("Hint:");
          lblHint.setBounds(110, 166, 33, 16);
          getContentPane().add(lblHint);
          
          hinttext = new JTextField();
          hinttext.setBounds(147, 161, 340, 26);
          getContentPane().add(hinttext);
          hinttext.setColumns(10);
          
          JSeparator separator = new JSeparator();
          separator.setBounds(41, 212, 433, 12);
          getContentPane().add(separator);
          
          JButton regsubmit = new JButton("submit");
          regsubmit.addActionListener(new ActionListener() {
          	public void actionPerformed(ActionEvent e) {
          		register();
          	}
          });
          regsubmit.setBounds(392, 225, 82, 29);
          getContentPane().add(regsubmit);
          
          JCheckBox chckbxShowPassword = new JCheckBox("Show password");
  		  chckbxShowPassword.addActionListener(new ActionListener() {
  			public void actionPerformed(ActionEvent e) {
  				if (chckbxShowPassword.isSelected()) {
  					passtext.setEchoChar((char)0);
  					confirmtext.setEchoChar((char)0);
  				}
  				else {
  					passtext.setEchoChar('*');
  					confirmtext.setEchoChar('*');
  				}
  			}
  		});
          chckbxShowPassword.setBounds(310, 61, 134, 23);
          getContentPane().add(chckbxShowPassword);

    }


/* --------------- REGISTRATION ---------------- */
/* - User needs to enter at least one character for the process to actually begin and the passwords need to be identical (password and confirmation password)
 * - The inputted data gets stored into the user login table. 
 * - It will then create a table (if it doesn't exist already) based off the users username,
 * - Enables the 'user session' feature throughout the application - storing data to each individuals own table.      
 */
    private void register() {
    	Connection conregi = conregi();
    	String pass = String.valueOf(passtext.getPassword());
    	String conpass = String.valueOf(confirmtext.getPassword());
	try {	
		if (usertext.getText().trim().length() >= 1 && passtext.getText().trim().length() >= 1 && pintext.getText().trim().length() >= 6 && (conpass.equals(pass)) ) {
		int close = 1;
		
		String secretKey = "pleaseencryptme!!!!";
		String originalString = passtext.getText();
	    String encryptedString = AES256.encrypt(originalString, secretKey) ; //encrypt text within the password textfield.
		
		String query = "insert into mlogin_table values (?,?,?,?)";
	    Statement stmt = conregi.createStatement();
		PreparedStatement ps = conregi.prepareStatement(query);
		ps.setString(1, usertext.getText());
		ps.setString(2, encryptedString); //store encrypted password into database.
		ps.setString(3, pintext.getText());
		ps.setString(4, hinttext.getText());
		ps.execute();		
		JOptionPane.showMessageDialog(null, "Registered!");
		usersess = usertext.getText();
		stmt.executeUpdate("CREATE TABLE IF NOT EXISTS "+usersess+" " + "(id INTEGER not NULL, " + " username VARCHAR(255), " + " password VARCHAR(255), " + " website VARCHAR(255), " + " PRIMARY KEY ( id ))");
		usertext.setText(null); //clear text fields.
		passtext.setText(null);
		pintext.setText(null);
		confirmtext.setText(null);
		hinttext.setText(null);
		conregi.close();
		switch (close) {  //close frame if successful
		case 1:	
			setVisible(false);
			dispose();
			break;
		}
		}
		else if (pintext.getText().trim().length() < 6) {
			JOptionPane.showMessageDialog(null, "Ensure the pin is at least 6 digits.");
			 usertext.setText(null);
			 passtext.setText(null);
			 pintext.setText(null);
			 confirmtext.setText(null);
			 hinttext.setText(null);
		}
		else if (!conpass.equals(pass)) {
			JOptionPane.showMessageDialog(null, "Ensure both passwords are the same.");
			 usertext.setText(null);
			 passtext.setText(null);
			 pintext.setText(null);
			 confirmtext.setText(null);
			 hinttext.setText(null);
		}
		else {
	         JOptionPane.showMessageDialog(null, "Fill in the text fields. Try again. ");
			 usertext.setText(null);
			 passtext.setText(null);
			 pintext.setText(null);
			 confirmtext.setText(null);
			 hinttext.setText(null);
		}
	} catch(Exception e) {
		JOptionPane.showMessageDialog(null, "Account already exists.");
		System.out.println("Error: " + e);
	}
   }
    
/* --------------- DATABASE CONNECTION ---------------- */
/*  Connects to the database.
 */
	static Connection conregi() {
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
   
   
 public static void main(String[] args){
  new Register();
 }

@Override
public void actionPerformed(ActionEvent e) {
	Register register = new Register();
	// TODO Auto-generated method stub
	
}
}