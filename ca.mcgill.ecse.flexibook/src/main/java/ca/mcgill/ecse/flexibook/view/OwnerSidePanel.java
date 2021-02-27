package ca.mcgill.ecse.flexibook.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ca.mcgill.ecse.flexibook.application.FlexiBookApplication;
import ca.mcgill.ecse.flexibook.controller.FlexiBookController;
import ca.mcgill.ecse.flexibook.controller.InvalidInputException;


/**
 * @author jiang
 * Allows the owner to switch between different panels to do different actions
 */
public class OwnerSidePanel extends JPanel implements ActionListener{
	private FlexiAppPage app;
	public OwnerSidePanel(FlexiAppPage app) {
		this.app = app;
		
		setLayout(null);
		Color green = new Color(165, 214, 167);
		this.setBackground(green);
		int height = 300;
		
		// add a head protrait
		URL url = getClass().getResource("head_portrait.png");
		Icon icon = new ImageIcon(url);
		JLabel image = new JLabel();
		image.setIcon(icon);
		image.setBounds(85, 120, 80, 80);
		this.add(image);
		
		// add four buttons
		String[] buttons = {"Business Information", "Business Hour", "Calendar", "Service", "Appointment", "Logout"};
		for(int i=0;i<buttons.length;i++) {
			JButton aButton = new JButton(buttons[i]);
			aButton.setActionCommand(buttons[i]);
			aButton.addActionListener(this);
			aButton.setFont(new Font("Courier", Font.BOLD, 15));
			aButton.setBackground(new Color(241, 248, 233));
			aButton.setBounds(0, height, 250, 40);
			height += 50;
			aButton.setVisible(true);
			this.add(aButton);
		}
		
		setSize(250, 800);
	}
	
	public FlexiAppPage getAppPage() {
		return this.app;
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
	    JButton button = (JButton) e.getSource();
	    String command = button.getActionCommand();
		if(command.equals("Service")) {
			app.updateMainPanel(new ServiceMain(app));
		}else if(command.equals("Business Information")) {
			app.updateMainPanel(new AddBusinessInfo(app));
		}else if(command.equals("Business Hour")) {
			app.updateMainPanel(new AddBusinessHour(app));
		}else if(command.equals("Time Slot")) {
			app.updateMainPanel(new AddTimeSlot(app));
		}else if(command.equals("Appointment")) {
			app.updateMainPanel(new AppointmentsMain(app));
		}else if(command.equals("Logout")) {
			app.updateMainPanel(new LogoutOwner(app));
		}else if(command.equals("Calendar")) {
			app.updateMainPanel(new OwnerCalendar(app));
		}
	}

}
