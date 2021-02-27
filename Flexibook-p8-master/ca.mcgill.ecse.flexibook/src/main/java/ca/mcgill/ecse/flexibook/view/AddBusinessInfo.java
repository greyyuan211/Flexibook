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
import ca.mcgill.ecse.flexibook.view.AddService.ColorfulLabel;

public class AddBusinessInfo extends JPanel implements ActionListener{
	private JLabel nameLabel;
	private JLabel warning;
	private JTextField nameTxt;
	private JLabel durLabel;
	private JTextField addressTxt;
	private JLabel dtDurLabel;
	private JTextField phoneNumberTxt;
	private JLabel dtStartLabel;
	private JTextField emailTxt;
	private JButton add;
	private JButton view;
	private FlexiAppPage app;
	private JLabel hint;
	
	/**
	 * @author Grey Yuan
	 * implement UI for setting up business information
	 */
	public AddBusinessInfo(FlexiAppPage app) {
		this.app = app;
		setLayout(null);
		setSize(750,800);
		Color background = new Color(232, 245, 233);
		setBackground(background);	//set background color
		Color green = new Color(200, 230, 201);
		JLabel addService = new ColorfulLabel("Set up business information",new Color(129, 199, 132),25);
		warning = null;
		nameLabel = new ColorfulLabel("Name:",green,15);
		nameTxt = new JTextField();
		durLabel = new ColorfulLabel("Address:",green,15);
		addressTxt = new JTextField();
		dtDurLabel = new ColorfulLabel("Phone Number:",green,15);
		phoneNumberTxt = new JTextField();
		dtStartLabel = new ColorfulLabel("Email:",green,15);
		emailTxt = new JTextField();
		
		view = new JButton("View");
		view.setActionCommand("View");
		view.addActionListener(this);
		view.setFont(new Font("Courier", Font.BOLD, 15));
		view.setBackground(new Color(241, 248, 233));
		add = new JButton("Add");
		add.setActionCommand("Add");
		add.addActionListener(this);
		add.setFont(new Font("Courier", Font.BOLD, 15));
		add.setBackground(new Color(241, 248, 233));
		
		addService.setBounds(150, 120, 450, 60);
		nameLabel.setBounds(150, 250, 200, 40);
		durLabel.setBounds(150, 300, 200, 40);
		dtDurLabel.setBounds(150, 350, 200, 40);
		dtStartLabel.setBounds(150, 400, 200, 40);
		nameTxt.setBounds(400, 250, 200, 40);
		addressTxt.setBounds(400, 300, 200, 40);
		phoneNumberTxt.setBounds(400, 350, 200, 40);
		emailTxt.setBounds(400, 400, 200, 40);
		view.setBounds(200, 500, 150, 40);
		add.setBounds(400, 500, 150, 40);
		this.add(addService);
		this.add(nameLabel);
		this.add(durLabel);
		this.add(dtDurLabel);
		this.add(dtStartLabel);
		this.add(nameTxt);
		this.add(addressTxt);
		this.add(phoneNumberTxt);
		this.add(emailTxt);
		this.add(add);
		this.add(view);

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
			setFont(new Font("Monospaced", Font.ITALIC,fontSizeToUse));
			setPreferredSize(new Dimension(50,200));
		}
	}
	
	private void addHint(String message) {
		hint = new ColorfulLabel(message,null,10);
		hint.setBounds(150, 180, 450, 40);
		this.add(hint);
		this.validate();
		this.revalidate();
		this.repaint();
	}

	/**
	 * @author Grey Yuan
	 * overwirte actionPerformed to set up business info, then it should be shown by clicking view button
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(add)) {
			try {
				if(warning!=null)	this.remove(warning);
				String name = nameTxt.getText();
				String address = addressTxt.getText();
				String phone = phoneNumberTxt.getText();
				String email = emailTxt.getText();
				FlexiBookController.setUpBasicBusinessInfo("owner", name, address, phone, email);
				addHint("Set up business information successfully");
			}catch(InvalidInputException IIexception) {
				addHint(IIexception.getMessage());
			}
		}
		
		if(e.getSource().equals(view)) {
		app.updateMainPanel(new BusinessMain(app));
	    }
		
	}
}
