package ca.mcgill.ecse.flexibook.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ca.mcgill.ecse.flexibook.application.FlexiBookApplication;
import ca.mcgill.ecse.flexibook.controller.FlexiBookController;
import ca.mcgill.ecse.flexibook.controller.InvalidInputException;
import ca.mcgill.ecse.flexibook.view.AddBusinessInfo.ColorfulLabel;
/**
 * @author lizhiwei
 * create the login overview and sign up option
 * enter the username and the password
 * chose to login or sign up 
 */
public class Login extends JPanel implements ActionListener{
	private FlexiAppPage app;
	private JLabel usernameLabel;
	private JLabel warning;
	private JTextField usernameTxt;
	private JLabel passwordLabel;
	private JTextField passwordTxt;
	private JButton login;
	private JButton signup;
	
	public Login(FlexiAppPage app) {
		this.app = app;
		setLayout(null);
		setSize(300,450);
		Color background = new Color(227, 242, 253);
		setBackground(background);	//set background color
		
		JLabel loginfo = new ColorfulLabel("Login",null,20);
		loginfo.setBounds(110, 30, 60, 50);
		this.add(loginfo);
		
		usernameLabel = new ColorfulLabel("username:",null,12);
		usernameLabel.setBounds(15, 120, 90, 24);
		this.add(usernameLabel);
		
		passwordLabel = new ColorfulLabel("password:",null,12);
		passwordLabel.setBounds(15, 160, 90, 24);
		this.add(passwordLabel);
		
		usernameTxt = new JTextField();
		usernameTxt.setBounds(105, 123, 150, 24);
		this.add(usernameTxt);
		
		passwordTxt = new JTextField();
		passwordTxt.setBounds(105, 162, 150, 24);
		this.add(passwordTxt);
		
		login = new JButton("Login");
		login.setActionCommand("login");
		login.addActionListener(this);
		login.setFont(new Font("Courier", Font.BOLD, 15));
		login.setBackground(new Color(241, 248, 233));
		login.setBounds(35, 220, 90, 40);
		this.add(login);
		
		signup = new JButton("Sign Up");
		signup.setActionCommand("Signn Up");
		signup.addActionListener(this);
		signup.setFont(new Font("Courier", Font.BOLD, 15));
		signup.setBackground(new Color(241, 248, 233));
		signup.setBounds(155, 220, 90, 40);
		this.add(signup);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(login)) {
			try {
				if(warning!=null)	this.remove(warning);
				String username = usernameTxt.getText();
				String password = passwordTxt.getText();
				FlexiBookController.Login(username, password);
				if (username.equals("owner")) {
					app.initOwnerPage();
				}
				else {
					app.initCustomerPage();
				}
			}catch(InvalidInputException IIexception) {
				warning = new ColorfulLabel(IIexception.getMessage(),null,12);
				warning.setBounds(0, 70, 250, 40);
				this.add(warning);
				this.validate();
				this.revalidate();
				this.repaint();
			}
		}
		if(e.getSource().equals(signup)) {
			if(warning!=null)	this.remove(warning);
			String username = usernameTxt.getText();
			String password = passwordTxt.getText();
			try {
				FlexiBookController.signUp(username, password);
				warning = new ColorfulLabel("Sign up successfully",null,12);
				warning.setBounds(0, 70, 250, 40);
				this.add(warning);
				this.validate();
				this.revalidate();
				this.repaint();
			} catch (InvalidInputException e1) {
				warning = new ColorfulLabel(e1.getMessage(),null,12);
				warning.setBounds(0, 70, 250, 40);
				this.add(warning);
				this.validate();
				this.revalidate();
				this.repaint();
			}
		}
		
	}

}
