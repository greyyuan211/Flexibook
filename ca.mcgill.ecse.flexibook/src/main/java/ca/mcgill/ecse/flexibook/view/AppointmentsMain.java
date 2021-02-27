package ca.mcgill.ecse.flexibook.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import ca.mcgill.ecse.flexibook.controller.FlexiBookController;
import ca.mcgill.ecse.flexibook.controller.InvalidInputException;
import ca.mcgill.ecse.flexibook.controller.TOAppointmentItem;
import ca.mcgill.ecse.flexibook.view.ServiceMain.ColorfulLabel;

/**
 * UI Panel for owner appointment management.This UI feature displays the currently available appointments.
 * It allows the owner to start, end, or register no show for an appointment by selecting the appointments displayed in the table.
 * 
 * @author Jack Wei
 *
 */
public class AppointmentsMain extends JPanel implements ActionListener{
	private FlexiAppPage app;
	private JTable table;
	private JLabel warning;
	private JButton start;
	private JButton end;
	private JButton noShow;
	private String error = "";
	private TOAppointmentItem selectedAppointment;
	private List<TOAppointmentItem> toAppointments;
	
	public AppointmentsMain (FlexiAppPage app) {
		this.app = app;
		toAppointments = FlexiBookController.getAppointments();
		setLayout(null);
		setSize(750,800);
		Color bgColor = new Color(232, 245, 233);
		setBackground(bgColor);
		
		String[] columnNames = {"Customer",
                "Service",
                "Date",
                "Start",
                "End",
                "State"
                };
		
		String[][] data = new String[toAppointments.size()][6];
		for(int i=0; i<toAppointments.size(); i++) {
			data[i][0] = toAppointments.get(i).getCustomerName();
			data[i][1] = toAppointments.get(i).getServiceName();
			data[i][2] = toAppointments.get(i).getDate().toString();
			data[i][3] = toAppointments.get(i).getStartTime().toString();
			data[i][4] = toAppointments.get(i).getEndTime().toString();
			data[i][5] = toAppointments.get(i).getState();
		}
		
		table = new JTable(data, columnNames);
		table.setModel( new DefaultTableModel(data, columnNames));
		table.getTableHeader().setFont(new Font("SansSerif", Font.ITALIC, 12));
		table.setFont(new Font("Dialog", Font.BOLD, 15));
		table.setRowHeight(table.getRowHeight()+5);
		table.setPreferredScrollableViewportSize(new Dimension(500, 70));
        table.setFillsViewportHeight(true);
        
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBounds(60, 200, 650, 250 ); // x, y, width, height
        this.add(scroll);
        
        JLabel title = new ColorfulLabel("Appointments", new Color(129, 199, 132), 25);
        title.setBounds(100, 100, 500, 60);
        this.add(title);
        
        start = new JButton("Start Appointment");
        start.setActionCommand("Start");
        start.addActionListener(this);
        start.setFont(new Font("Courier", Font.BOLD, 15));
        start.setBackground(new Color(241, 248, 233));
		end = new JButton("End Appointment");
		end.setActionCommand("End");
		end.addActionListener(this);
		end.setFont(new Font("Courier", Font.BOLD, 15));
		end.setBackground(new Color(241, 248, 233));
		noShow = new JButton("Register No Show");
		noShow.setActionCommand("NoShow");
		noShow.addActionListener(this);
		noShow.setFont(new Font("Courier", Font.BOLD, 15));
		start.setBounds(100, 470, 170, 40);
		end.setBounds(320, 470, 150, 40);
		noShow.setBounds(520, 470, 170, 40);
		this.add(start);
		this.add(noShow);
		this.add(end);
	}
	
	
	private void addWarning(String message) {
		warning = new ColorfulLabel(message,null,15);
		warning.setBounds(100, 500, 520, 40);
		this.add(warning);
		this.validate();
		this.revalidate();
		this.repaint();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		int row = table.getSelectedRow();
		if(row < 0) {
			error = "Need to select appointment";
		}
		if (error.length() == 0) {
			if(e.getSource().equals(start)) {
				// call the controller
				try {
					selectedAppointment = toAppointments.get(row);
					FlexiBookController.startAppointment(selectedAppointment.getServiceName(),
							selectedAppointment.getDate(), 
							selectedAppointment.getStartTime());
					app.updateMainPanel(new AppointmentsMain(app));
				} catch (InvalidInputException ie) {
					addWarning(ie.getMessage());
				}
			}else if(e.getSource().equals(end)) {
				// call the controller
				try {
					selectedAppointment = toAppointments.get(row);
					FlexiBookController.endAppointment(selectedAppointment.getServiceName(),
							selectedAppointment.getDate(), 
							selectedAppointment.getStartTime());
					app.updateMainPanel(new AppointmentsMain(app));
				} catch (InvalidInputException ie) {
					addWarning(ie.getMessage());
				}
			}else if(e.getSource().equals(noShow)) {
				// call the controller
				try {
					System.out.println();
					selectedAppointment = toAppointments.get(row);
					FlexiBookController.registerNoShow(selectedAppointment.getServiceName(),
							selectedAppointment.getDate(), 
							selectedAppointment.getStartTime());
					app.updateMainPanel(new AppointmentsMain(app));
				} catch (InvalidInputException ie) {
					addWarning(ie.getMessage());
				}
			}
		} else {
			addWarning(error);
		}
	}
}
