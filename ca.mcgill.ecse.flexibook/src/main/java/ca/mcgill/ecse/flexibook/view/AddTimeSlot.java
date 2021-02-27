package ca.mcgill.ecse.flexibook.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.sql.Time;

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

public class AddTimeSlot extends JPanel implements ActionListener{
	private JLabel typeLabel;
	private JLabel warning;
	private JTextField typeTxt;
	private JLabel nsdLabel;
	private JTextField nsdTxt;
	private JLabel nstLabel;
	private JTextField nstTxt;
	private JLabel nedLabel;
	private JTextField nedTxt;
	private JLabel netLabel;
	private JTextField netTxt;
//	private JButton back;
	private JButton add;
	private FlexiAppPage app;
	private JLabel hint;
	
	/**
	 * @author Grey Yuan
	 * This feature include holdiday and vacation whcih are not required, please ignore
	 */
	public AddTimeSlot(FlexiAppPage app) {
		this.app = app;
		setLayout(null);
		setSize(750,800);
		Color background = new Color(232, 245, 233);
		setBackground(background);	//set background color
		Color green = new Color(200, 230, 201);
		JLabel addService = new ColorfulLabel("Add a Time Slot",new Color(129, 199, 132),25);
		warning = null;
		typeLabel = new ColorfulLabel("Type:",green,15);
		typeTxt = new JTextField();
		nsdLabel = new ColorfulLabel("Start Date:",green,15);
		nsdTxt = new JTextField();
		nstLabel = new ColorfulLabel("Stat Time:",green,15);
		nstTxt = new JTextField();
		nedLabel = new ColorfulLabel("End Date:",green,15);
		nedTxt = new JTextField();
		netLabel = new ColorfulLabel("End Time:",green,15);
		netTxt = new JTextField();
		
//		back = new JButton("Back");
//		back.setActionCommand("Back");
//		back.addActionListener(this);
//		back.setFont(new Font("Courier", Font.BOLD, 15));
//		back.setBackground(new Color(241, 248, 233));
		add = new JButton("Add");
		add.setActionCommand("Add");
		add.addActionListener(this);
		add.setFont(new Font("Courier", Font.BOLD, 15));
		add.setBackground(new Color(241, 248, 233));
		
		addService.setBounds(150, 120, 450, 60);
		typeLabel.setBounds(150, 250, 200, 40);
		nsdLabel.setBounds(150, 300, 200, 40);
		nstLabel.setBounds(150, 350, 200, 40);
		nedLabel.setBounds(150, 400, 200, 40);
		netLabel.setBounds(150, 450, 200, 40);
		typeTxt.setBounds(400, 250, 200, 40);
		nsdTxt.setBounds(400, 300, 200, 40);
		nstTxt.setBounds(400, 350, 200, 40);
		nedTxt.setBounds(400, 400, 200, 40);
		netTxt.setBounds(400, 450, 200, 40);
//		back.setBounds(200, 500, 150, 40);
		add.setBounds(400, 500, 150, 40);
		this.add(addService);
		this.add(typeLabel);
		this.add(nsdLabel);
		this.add(nstLabel);
		this.add(nedLabel);
		this.add(netLabel);
		this.add(typeTxt);
		this.add(nsdTxt);
		this.add(nstTxt);
		this.add(nedTxt);
		this.add(netTxt);
//		this.add(back);
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
	
	private static Time stringToTime(String string) {
		Time st = Time.valueOf(string + ":00");

		return st;
	}

	private static Date stringToDate(String string) {
		Date date = Date.valueOf(string);
		return date;
	}

	/**
	 * @author Grey Yuan
	 * This feature include holdiday and vacation whcih are not required, please ignore
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(add)) {
			try {
				if(warning!=null)	this.remove(warning);
				String type = typeTxt.getText();
				String sd = nsdTxt.getText();
				String st = nstTxt.getText();
				String ed = nedTxt.getText();
				String et = netTxt.getText();
				Date startD = stringToDate(sd);
				Time startT = stringToTime(st);
				Date endD = stringToDate(ed);
				Time endT = stringToTime(et);
				
				FlexiBookController.setUpTimeSlot("owner", type, startD, endD, startT, endT);
				addHint("Add a time slot successfully");
			}catch(InvalidInputException IIexception) {
				addHint(IIexception.getMessage());
			}
		}
		
	}
}
