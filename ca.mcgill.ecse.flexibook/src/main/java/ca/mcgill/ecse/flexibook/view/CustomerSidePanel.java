package ca.mcgill.ecse.flexibook.view;

import java.awt.Color;
import java.awt.Font;
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
 * @author Cecilia Jiang
 * Allows the customer to switch between different panels to do different actions
 */
public class CustomerSidePanel extends JPanel implements ActionListener{
	private FlexiAppPage app;
	public CustomerSidePanel(FlexiAppPage app) {
		this.app = app;
		
		setLayout(null);
		Color blue = new Color(121, 134, 203);
		this.setBackground(blue);
		int height = 360;
		
		// add a head protrait
		URL url = getClass().getResource("head_portrait2.png");
		Icon icon = new ImageIcon(url);
		JLabel image = new JLabel();
		image.setIcon(icon);
		image.setBounds(85, 150, 80, 80);
		this.add(image);
		
		// add four buttons
		String[] buttons = {"Appointment Calendar", "Logout","Manage Appointment","Delete"};
		for(int i=0;i<buttons.length;i++) {
			JButton aButton = new JButton(buttons[i]);
			aButton.setActionCommand(buttons[i]);
			aButton.addActionListener(this);
			aButton.setFont(new Font("Courier", Font.BOLD, 15));
			aButton.setBackground(Color.white);
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
		// TODO Auto-generated method stub

		JButton button = (JButton) e.getSource();
		String command = button.getActionCommand();
		if (command.equals("Logout")) {
			app.updateMainPanel(new LogoutCustomer(app));
		} else if (command.equals("Delete")) {
			app.updateMainPanel(new DeleteAccount(app));
		} else if (command.equals("Manage Appointment")) {
			app.updateMainPanel(new ManageAppointmentsUser(app));
		}
		else if (command.equals("Appointment Calendar")) {
			app.updateMainPanel(new ViewCalendar(app));
		}

	}
}
