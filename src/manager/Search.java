package manager;



import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import loginsystem.Login;


public class Search extends JFrame implements ActionListener{
    JLabel lblUser,lblPass,lblWeb;
    JTextField usertext,passtext,webtext;
    JButton btnSearch;
    private JButton btnVisitWebpage;
	public String usersess = Login.usersess;

      public Search(){
          super("Search");
          lblWeb = new JLabel("Website (URL):");
          lblWeb.setBounds(179, 26, 100, 20);
          webtext = new JTextField(20);
          webtext.setBounds(117, 53, 228, 20);
          btnSearch = new JButton("Search");
          btnSearch.setBounds(282, 171, 94, 43);
          btnSearch.addActionListener(this);
          setVisible(true);
          setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); //changing this from exit to dispose on close keeps the main app open.
          setLocationRelativeTo(null);
          setSize(482,267);
          
          lblUser = new JLabel("Username:");
          lblUser.setBounds(76, 102, 66, 20);
          usertext = new JTextField(20);
          usertext.setBounds(43, 124, 150, 20);
          lblPass = new JLabel("Password:");
          lblPass.setBounds(304, 102, 63, 20);
          passtext = new JTextField(20);
          passtext.setBounds(267, 124, 150, 20);
          getContentPane().setLayout(null);
          getContentPane().add(btnSearch);
          getContentPane().add(lblUser);
          getContentPane().add(usertext);
          getContentPane().add(lblPass);
          getContentPane().add(passtext);
          getContentPane().add(lblWeb);
          getContentPane().add(webtext);
          
          btnVisitWebpage = new JButton("Visit Website");
          btnVisitWebpage.addActionListener(new ActionListener() {
          	public void actionPerformed(ActionEvent e) {
          		Visit();
          	}
          });
          btnVisitWebpage.setBounds(76, 171, 94, 43);
          getContentPane().add(btnVisitWebpage);

    }

      
/* --------------- SEARCH FEATURE ---------------- */      
/* - Allows the user to efficiently locate their login credentials for websites.
 * - By entering a website, it will search the database and display the username and password that correlate to that website.
 *
 */
      
    @Override
    public void actionPerformed(ActionEvent e) { 
    Function f = new Function();
    ResultSet rs = null;
    
    rs = f.find(webtext.getText());
    try{
      if(rs.next()){ //based on the input within the website textfield locate the username and password for it.
          usertext.setText(rs.getString("username"));
            passtext.setText(rs.getString("password"));
            
      }  else{
          JOptionPane.showMessageDialog(null, "Website does not exist in database.");
      }
    }catch(Exception ex){
           JOptionPane.showMessageDialog(null, ex.getMessage());
            }
    }
    
    
   public class Function{
       Connection con = null;
       ResultSet rs = null;
       PreparedStatement ps = null;
       public ResultSet find(String website){
           try{
           con = DriverManager.getConnection("jdbc:sqlite:mlogin.db","root","root");
           ps = con.prepareStatement("SELECT * FROM "+usersess+" " +"WHERE website = ?");
           ps.setString(1, website);
           rs = ps.executeQuery();
           }catch(Exception ex){
              JOptionPane.showMessageDialog(null, ex.getMessage());
           }
           return rs;
       }
       
   }


	private void Visit() {
		   if (webtext.getText().trim().length() >= 1) {
			   try {
		   
		        String myurl = "https://" + webtext.getText();   // won't work unless https:// is at the beginning.
		        
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
   
   
 public static void main(String[] args){
  new Search();
 }
 
}
 
 
