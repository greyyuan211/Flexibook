package ca.mcgill.ecse.flexibook.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;

import ca.mcgill.ecse.flexibook.application.FlexiBookApplication;
import ca.mcgill.ecse.flexibook.controller.FlexiBookController;
import ca.mcgill.ecse.flexibook.controller.InvalidInputException;
import ca.mcgill.ecse.flexibook.controller.TOAppointmentItem;
import ca.mcgill.ecse.flexibook.view.ManageAppointmentsUser.ColorfulLabel;
/** 
 * @author Jiatong Niu
 * allow customer to choose make an appointment on service or service combo
 */
public class MakeAppointment extends JPanel implements ActionListener {
	private FlexiAppPage app;
    private JButton service;
	private JButton serviceCombo;
	private JButton back;
	

	public MakeAppointment(FlexiAppPage app) {
		this.app = app;
		setLayout(null);
		setSize(750,800);
		Color blue = new Color(232, 234, 246);
		setBackground(blue);
		JLabel title = new ColorfulLabel("Make an Appointment", new Color(121, 134, 203), 25);
        title.setBounds(100, 100, 500, 60);
        this.add(title);
        
        back = new JButton("Back");
      	back.setActionCommand("Back");
      	back.addActionListener(this);
    	back.setFont(new Font("Courier", Font.BOLD, 15));
    	back.setBackground(new Color(121, 134, 203));
    	
        service = new JButton("Choose Service");
      	service.setActionCommand("service");
      	service.addActionListener(this);
      	service.setFont(new Font("Courier", Font.BOLD, 15));
      	service.setBackground(new Color(121, 134, 203));
      	
      	serviceCombo = new JButton("Choose ServiceCombo");
      	serviceCombo.setActionCommand("servicecombo");
      	serviceCombo.addActionListener(this);
      	serviceCombo.setFont(new Font("Courier", Font.BOLD, 15));
      	serviceCombo.setBackground(new Color(121, 134, 203));
      	
      	back.setBounds(500, 440, 100, 40);
      	service.setBounds(100, 220, 300, 40);
      	serviceCombo.setBounds(100, 330, 300, 40);
      	this.add(service);
      	this.add(serviceCombo);
      	this.add(back);
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(service)) {
			app.updateMainPanel(new AppointmentonService(app));
		}else if(e.getSource().equals(serviceCombo)) {
			app.updateMainPanel(new AppointmentonServiceCombo(app));
		}else if(e.getSource().equals(back)) {
			app.updateMainPanel(new ManageAppointmentsUser(app));
		}
		
	}

}
