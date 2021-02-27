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
 * A control panel for the owner to add desired services.
 */
public class AddService extends JPanel implements ActionListener{
	private JLabel nameLabel;
	private JLabel hint;
	private JTextField nameTxt;
	private JLabel durLabel;
	private JTextField durTxt;
	private JLabel dtDurLabel;
	private JTextField dtDurTxt;
	private JLabel dtStartLabel;
	private JTextField dtStartTxt;
	private JButton back;
	private JButton add;
	private FlexiAppPage app;
	
	public AddService(FlexiAppPage app) {
		this.app = app;
		setLayout(null);
		setSize(750,800);
		Color background = new Color(232, 245, 233);
		setBackground(background);	//set background color
		Color green = new Color(200, 230, 201);
		JLabel addService = new ColorfulLabel("Add a New Service",new Color(129, 199, 132),25);
		hint = null;
		nameLabel = new ColorfulLabel("Name:",green,15);
		nameTxt = new JTextField();
		durLabel = new ColorfulLabel("Duration:",green,15);
		durTxt = new JTextField();
		dtDurLabel = new ColorfulLabel("Downtime Duration:",green,15);
		dtDurTxt = new JTextField();
		dtStartLabel = new ColorfulLabel("Start of Downtime:",green,15);
		dtStartTxt = new JTextField();
		
		back = new JButton("Back");
		back.setActionCommand("Back");
		back.addActionListener(this);
		back.setFont(new Font("Courier", Font.BOLD, 15));
		back.setBackground(new Color(241, 248, 233));
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
		durTxt.setBounds(400, 300, 200, 40);
		dtDurTxt.setBounds(400, 350, 200, 40);
		dtStartTxt.setBounds(400, 400, 200, 40);
		back.setBounds(200, 500, 150, 40);
		add.setBounds(400, 500, 150, 40);
		this.add(addService);
		this.add(nameLabel);
		this.add(durLabel);
		this.add(dtDurLabel);
		this.add(dtStartLabel);
		this.add(nameTxt);
		this.add(durTxt);
		this.add(dtDurTxt);
		this.add(dtStartTxt);
		this.add(back);
		this.add(add);
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
		hint = new ColorfulLabel(message,null,15);
		hint.setBounds(150, 180, 450, 40);
		this.add(hint);
		this.validate();
		this.revalidate();
		this.repaint();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(add)) {
			try {
				if(hint!=null)	this.remove(hint);
				String serviceName = nameTxt.getText();
				String dur = durTxt.getText();
				String dtDur = dtDurTxt.getText();
				String dtStart = dtStartTxt.getText();
				FlexiBookController.addService(serviceName, dur, dtDur, dtStart);
				addHint("Add a service successfully");
			}catch(InvalidInputException IIexception) {
				addHint(IIexception.getMessage());
			}
		}else if(e.getSource().equals(back)) {
			app.updateMainPanel(new ServiceMain(app));
		}
		
	}
}
