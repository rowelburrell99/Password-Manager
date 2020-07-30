package loginsystem;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Hint extends JFrame implements ActionListener{
    JLabel lblUser,lblHint,lblPin;
    JTextField usertext,pintext;
    JButton btnSubmit;
    private JButton btnVisitWebpage;
	private JTextArea hinttext2;

      public Hint(){
          super("Hint");
          lblPin = new JLabel("Pin:");
          lblPin.setBounds(86, 80, 23, 20);
          pintext = new JTextField(20);
          pintext.setBounds(121, 80, 66, 20);
          btnSubmit = new JButton("Submit");
          btnSubmit.setBounds(337, 196, 80, 29);
          btnSubmit.addActionListener(this);
          setVisible(true);
          setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); //changing this from exit to dispose on close keeps the main app open.
          setLocationRelativeTo(null);
          setSize(482,267);
          
          lblUser = new JLabel("Username:");
          lblUser.setBounds(43, 48, 66, 20);
          usertext = new JTextField(20);
          usertext.setBounds(121, 48, 150, 20);
          lblHint = new JLabel("Hint:");
          lblHint.setBounds(73, 122, 36, 20);
  		
          
          JScrollPane scrollPane = new JScrollPane();
  		  scrollPane.setBounds(121, 112, 340, 43);

  		
          getContentPane().setLayout(null);
          getContentPane().add(btnSubmit);
          getContentPane().add(lblUser);
          getContentPane().add(usertext);
          getContentPane().add(lblHint);
          getContentPane().add(lblPin);
          getContentPane().add(pintext);
          getContentPane().add(scrollPane);
          
          hinttext2 = new JTextArea();
          hinttext2.setBounds(121, 112, 340, 43);
          scrollPane.setViewportView(hinttext2);
    }

      
      
/* --------------- SEARCH FEATURE ---------------- */
/* - User inputs their pin and username and their hint is located from the database. 
 * - This may be an ideal alternative for some users who don't want to reset their password.
 * 
 */
      
    @Override
    public void actionPerformed(ActionEvent e) { 
    if (pintext.getText().trim().length() >= 6 && usertext.getText().trim().length() >= 1) {
	    Function f = new Function();
	    Functiontest ft = new Functiontest();
	    ResultSet rs = null;
	    ResultSet rs2 = null;
	    
	    rs = f.find(pintext.getText());
	    rs2 = ft.find(usertext.getText());
	    try{
	      if(rs.next() && rs2.next() ){
	            hinttext2.setText(rs.getString("hint")); 
	            
	      }  else{
	          JOptionPane.showMessageDialog(null, "Hint cannot be located.");
	      }
	    }catch(Exception ex){
	           JOptionPane.showMessageDialog(null, ex.getMessage());
	            }
    	}
    else if (usertext.getText().trim().length() < 1 || pintext.getText().trim().length() < 6) {
    	JOptionPane.showMessageDialog(null, "Ensure all fields are filled in correctly.");
    	}
    }
   public class Function{
       Connection conn = null;
       ResultSet rs = null;
       PreparedStatement ps = null;
       public ResultSet find(String s){
           try{
           conn = DriverManager.getConnection("jdbc:sqlite:mlogin.db","root","root");
           ps = conn.prepareStatement("SELECT * FROM mlogin_table WHERE pin = ?");
           ps.setString(1,s);
           rs = ps.executeQuery();
           }catch(Exception ex){
              JOptionPane.showMessageDialog(null, ex.getMessage());
           }
           return rs;
       }
       
   }
 
   public class Functiontest{
       Connection conn = null;
       ResultSet rs = null;
       PreparedStatement ps = null;
       public ResultSet find(String s){
           try{
           conn = DriverManager.getConnection("jdbc:sqlite:mlogin.db","root","root");
           ps = conn.prepareStatement("SELECT * FROM mlogin_table WHERE username = ?");
           ps.setString(1,s);
           rs = ps.executeQuery();
           }catch(Exception ex){
              JOptionPane.showMessageDialog(null, ex.getMessage());
           }
           return rs;
       }
       
   }
   
   
 public static void main(String[] args){
  new Hint();
 }
 
 
 
 
}
