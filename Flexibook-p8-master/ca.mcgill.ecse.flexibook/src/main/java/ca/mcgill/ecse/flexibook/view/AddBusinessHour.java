package ca.mcgill.ecse.flexibook.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

public class AddBusinessHour extends JPanel implements ActionListener{
	private JLabel dayLabel;
	private JLabel warning;
	private JTextField dayTxt;
	private JLabel nstLabel;
	private JTextField nstTxt;
	private JLabel netLabel;
	private JTextField netTxt;
	private JButton add;
	private FlexiAppPage app;
	private JLabel hint;
	
	/**
	 * @author Grey Yuan
	 * add business hour to the current business, and then it should be shown in the canlendar
	 */
	public AddBusinessHour(FlexiAppPage app) {
		this.app = app;
		setLayout(null);
		setSize(750,800);
		Color background = new Color(232, 245, 233);
		setBackground(background);	//set background color
		Color green = new Color(200, 230, 201);
		JLabel addService = new ColorfulLabel("Add a new business hour",new Color(129, 199, 132),25);
		warning = null;
		dayLabel = new ColorfulLabel("Day of Week:",green,15);
		dayTxt = new JTextField();
		nstLabel = new ColorfulLabel("New Start Time:",green,15);
		nstTxt = new JTextField();
		netLabel = new ColorfulLabel("New End Time:",green,15);
		netTxt = new JTextField();
		
		add = new JButton("Add");
		add.setActionCommand("Add");
		add.addActionListener(this);
		add.setFont(new Font("Courier", Font.BOLD, 15));
		add.setBackground(new Color(241, 248, 233));
		
		addService.setBounds(150, 120, 450, 60);
		dayLabel.setBounds(150, 250, 200, 40);
		nstLabel.setBounds(150, 300, 200, 40);
		netLabel.setBounds(150, 350, 200, 40);
		dayTxt.setBounds(400, 250, 200, 40);
		nstTxt.setBounds(400, 300, 200, 40);
		netTxt.setBounds(400, 350, 200, 40);
		add.setBounds(400, 500, 150, 40);
		this.add(addService);
		this.add(dayLabel);
		this.add(nstLabel);
		this.add(netLabel);
		this.add(dayTxt);
		this.add(nstTxt);
		this.add(netTxt);
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

	/**
	 * @author Grey Yuan
	 * overwrite actionPerformed into action that takes business hour input and add business hours to calendar
	 * show error if there is any
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(add)) {
			try {
				if(warning!=null)	this.remove(warning);
				hint=null;
				String newDay = dayTxt.getText();
				String nst = nstTxt.getText();
				String net = netTxt.getText();
				Time startT = stringToTime(nst);
				Time endT = stringToTime(net);
				FlexiBookController.setUpBusinessHour("owner", FlexiBookController.stringToDayOfWeek(newDay), startT, endT);
				addHint("Add a business hour successfully" );


			}catch(InvalidInputException IIexception) {
				addHint(IIexception.getMessage());
			}
		}
		
	}
}
