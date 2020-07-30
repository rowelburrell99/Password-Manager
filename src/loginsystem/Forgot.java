package loginsystem;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

public class Forgot extends JFrame implements ActionListener{
    private JButton btnResetPassword;
    private JButton btnNeedAHint;

      public Forgot(){
          super("Forgotten Password");
          setVisible(true);
          setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); //changing this from exit to 'dispose_on_close' keeps the main app open.
          setLocationRelativeTo(null);
          setSize(320,181);

  		
          getContentPane().setLayout(null);
          
          btnResetPassword = new JButton("Reset Password");
          btnResetPassword.addActionListener(new ActionListener() {
          	public void actionPerformed(ActionEvent e) {
          		Reset reset = new Reset();
    			setVisible(false);
    			dispose();
          		
          	}
          });
          btnResetPassword.setBounds(79, 36, 130, 29);
          getContentPane().add(btnResetPassword);
          
          btnNeedAHint = new JButton("Need A Hint?");
          btnNeedAHint.addActionListener(new ActionListener() {
          	public void actionPerformed(ActionEvent e) {
          		Hint hint = new Hint();
    			setVisible(false);
    			dispose();
          	}
          });
          btnNeedAHint.setBounds(79, 77, 130, 29);
          getContentPane().add(btnNeedAHint);

    }


   
   
 public static void main(String[] args){
  new Forgot();
 }




@Override
public void actionPerformed(ActionEvent e) {
	// TODO Auto-generated method stub
	
}
 
 
 
 
}
