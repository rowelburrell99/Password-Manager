package loginsystem;



import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JSeparator;
import javax.swing.JTextField;


public class Reset extends JFrame implements ActionListener{
    JLabel lblUser,lblPass;
    JTextField usertext;
    private JPasswordField passtext;
    private JPasswordField confirmtext;
    private JTextField pintext;
	public static String usersess;

      public Reset(){
          super("Reset");
          setVisible(true);
          setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); //changing this from exit to dispose on close keeps the main app open.
          setLocationRelativeTo(null);
          setSize(525,301);
          
          lblUser = new JLabel("Username:");
          lblUser.setBounds(80, 28, 66, 20);
          usertext = new JTextField(20);
          usertext.setBounds(148, 28, 150, 20);
          lblPass = new JLabel("New Password:");
          lblPass.setBounds(52, 63, 94, 20);
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
          lblPin.setBounds(52, 133, 99, 16);
          getContentPane().add(lblPin);
          
          JSeparator separator = new JSeparator();
          separator.setBounds(41, 212, 433, 12);
          getContentPane().add(separator);
          
          JButton regsubmit = new JButton("submit");
          regsubmit.addActionListener(new ActionListener() {
          	public void actionPerformed(ActionEvent e) {
          		Reset();
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


/* --------------- RESET FEATURE ---------------- */
/* - Updates the password by locating the row where the username and pin are matching.
 *
 */
      
      
    private void Reset() {
    	Connection conreset = conreset();
    	String pass = String.valueOf(passtext.getPassword());
    	String conpass = String.valueOf(confirmtext.getPassword());
	try {	
		if (usertext.getText().trim().length() >= 1 && passtext.getText().trim().length() >= 1 && pintext.getText().trim().length() >= 6 && (conpass.equals(pass)) ) {
			int close = 1;
			String query = "UPDATE mlogin_table SET password = ? WHERE username = ? and pin = ?";
			
			String secretKey = "pleaseencryptme!!!!";
			String originalString = passtext.getText();
		    String encryptedString = AES256.encrypt(originalString, secretKey) ;
			
			PreparedStatement ps = conreset.prepareStatement(query);
			ps.setString(1, encryptedString);
			ps.setString(2, usertext.getText());
			ps.setString(3, pintext.getText());
			ps.execute();
			JOptionPane.showMessageDialog(null, "Password updated!");
			conreset.close();
			switch (close) {
			case 1: //if successful close frame.
				setVisible(false);
				dispose();
				break;
			}
		}
		else if (!conpass.equals(pass)) {
			JOptionPane.showMessageDialog(null, "Ensure both passwords are the same.");
			 usertext.setText(null);
			 passtext.setText(null);
			 pintext.setText(null);
			 confirmtext.setText(null);
		}
		else if (pintext.getText().trim().length() < 6) {
			JOptionPane.showMessageDialog(null, "Ensure the pin is at least 6 digits.");
			 usertext.setText(null);
			 passtext.setText(null);
			 pintext.setText(null);
			 confirmtext.setText(null);
		}
		else {
	         JOptionPane.showMessageDialog(null, "Error occured. Ensure textfields are filled correctly. ");
			 usertext.setText(null);
			 passtext.setText(null);
			 confirmtext.setText(null);
			 pintext.setText(null);
		}
		}
	 catch(Exception e) {
		JOptionPane.showMessageDialog(null, "Account cannot be located.");
		System.out.println("Error: " + e);
	 	}
    }
    
 /*
  * Connection to database.
  */
	static Connection conreset() {
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
  new Reset();
 }

@Override
public void actionPerformed(ActionEvent e) {
	Reset reset = new Reset();
	// TODO Auto-generated method stub
	
}
}
