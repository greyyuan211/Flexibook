package ca.mcgill.ecse.flexibook.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ca.mcgill.ecse.flexibook.application.FlexiBookApplication;
import ca.mcgill.ecse.flexibook.controller.FlexiBookController;
import ca.mcgill.ecse.flexibook.controller.InvalidInputException;

/**
 * @author Cecilia Jiang
 * Show a confirmed message to customer before logout
 */
public class LogoutCustomer extends JPanel implements ActionListener{
	private JButton yes;
	private FlexiAppPage app;
	
	public LogoutCustomer(FlexiAppPage app) {
		this.app = app;
		setLayout(null);
		setSize(750,800);
		Color background = new Color(232, 234, 246);
		setBackground(background);	//set background color
		JLabel warning = new ColorfulLabel("Are you sure you would like to log out?",null,20);
		
		yes = new JButton("OK");
		yes.setActionCommand("OK");
		yes.addActionListener(this);
		yes.setFont(new Font("Courier", Font.BOLD, 15));
		yes.setBackground(new Color(121, 134, 203));
		
		warning.setBounds(180, 230, 590, 30);
		yes.setBounds(200, 300, 300, 40);
		this.add(warning);
		this.add(yes);
	}
	
	public FlexiAppPage getAppPage() {
		return this.app;
	}
	
	public static class ColorfulLabel extends JLabel{
	//Create a label class to prevent code duplication
		public ColorfulLabel(String Name,Color color,int fontSizeToUse) {
			super(Name);
			setOpaque(true); //Set opacity
			setBackground(color); //Set the background color
			setFont(new Font("Serif", Font.PLAIN,fontSizeToUse));
			setPreferredSize(new Dimension(50,200));
		}
	}

	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(yes)) {
			try {
				FlexiBookController.LogOut();
			} catch (InvalidInputException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			app.updateToSPage(new Login(app));
		}

	}
}
