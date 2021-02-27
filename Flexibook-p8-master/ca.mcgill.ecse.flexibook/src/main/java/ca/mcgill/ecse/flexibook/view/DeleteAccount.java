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
 * Show a warning message to the customer before deleting the account
 */
public class DeleteAccount extends JPanel implements ActionListener{
	private JTextField delete;
	private JButton yes;
	private FlexiAppPage app;
	private JLabel hint;
	
	public DeleteAccount(FlexiAppPage app) {
		this.app = app;
		setLayout(null);
		setSize(750,800);
		hint = null;
		Color background = new Color(232, 234, 246);
		setBackground(background);	//set background color
		JLabel warning1 = new ColorfulLabel("Once you delete your account, there's no getting it back.",null,20);
		JLabel warning2 = new ColorfulLabel("Confirm by typing DELETE.",null,20);
		delete = new JTextField();
		
		yes = new JButton("YEP, DELETE IT");
		yes.setActionCommand("YEP, DELETE IT");
		yes.addActionListener(this);
		yes.setFont(new Font("Courier", Font.BOLD, 15));
		yes.setBackground(new Color(121, 134, 203));
		
		warning1.setBounds(130, 170, 590, 30);
		warning2.setBounds(130, 230, 590, 30);
		delete.setBounds(200, 300, 300, 40);
		yes.setBounds(200, 360, 300, 40);
		this.add(warning1);
		this.add(warning2);
		this.add(delete);
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

	private void addHint(String message) {
		hint = new ColorfulLabel(message,null,15);
		hint.setBounds(150, 180, 450, 40);
		this.add(hint);
		this.validate();
		this.revalidate();
		this.repaint();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(yes)) {
			try {
				if(hint!=null)	this.remove(hint);
				if(delete.getText().equals("DELETE")) {
					FlexiBookController.deleteAccount(FlexiBookApplication.getCurrentUser().getUsername());
					app.updateToSPage(new Login(app));
				}
			}catch(InvalidInputException IIexception) {
				addHint(IIexception.getMessage());
			}
		}
		
	}
}

