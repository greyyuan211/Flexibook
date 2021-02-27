package ca.mcgill.ecse.flexibook.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import ca.mcgill.ecse.flexibook.application.FlexiBookApplication;
import ca.mcgill.ecse.flexibook.controller.FlexiBookController;
import ca.mcgill.ecse.flexibook.controller.InvalidInputException;
import ca.mcgill.ecse.flexibook.controller.TOAppointmentItem;

/** 
 * @author Jiatong Niu
 * customer can view the appointments they have made and cancel an appointment
 */
public class CancelAppointmentUser extends JPanel implements ActionListener {
	private FlexiAppPage app;
	private JLabel hint;
	private JTable table;
	private JButton back;
	private JButton cancel;
	private JLabel label;
	private JTextField aAppointment;
	private List<TOAppointmentItem> toAppointment; 
	private JScrollPane scroll;
	private String Username;

	
	public CancelAppointmentUser(FlexiAppPage app) {
		this.app = app;
		hint = null;
		toAppointment = FlexiBookController.getAppointments();
		Username=FlexiBookApplication.getCurrentUser().getUsername();
		
		setLayout(null);
		setSize(750,800);
		Color background = new Color(232, 234, 246);
		setBackground(background);	//set background color
		
		String[] columnNames = {"Number","Name","Service", "Start Date", "Start Time","End Time"};
		
         List<TOAppointmentItem> toAppointmentofUser = new ArrayList<TOAppointmentItem>();
		
		for(int i=0;i<toAppointment.size();i++) {
		 if(toAppointment.get(i).getCustomerName().equals(Username)) {
			 toAppointmentofUser.add(toAppointment.get(i));
		  }
		}
		
		String[][] data = new String[toAppointmentofUser.size()][6];
		for(int i=0; i<toAppointmentofUser.size(); i++) {
			data[i][0] = Integer.toString(i+1);
			data[i][1] = toAppointmentofUser.get(i).getCustomerName();
			data[i][2] = toAppointmentofUser.get(i).getServiceName();
			data[i][3] = toAppointmentofUser.get(i).getDate().toString();
			data[i][4] = toAppointmentofUser.get(i).getStartTime().toString();
			data[i][5] = toAppointmentofUser.get(i).getEndTime().toString();
			
		}
		
		table = new JTable(data, columnNames);
		table.setModel( new DefaultTableModel(data, columnNames));
		table.getTableHeader().setFont(new Font("SansSerif", Font.ITALIC, 12));
		table.setFont(new Font("Dialog", Font.BOLD, 15));
		table.setRowHeight(table.getRowHeight()+5);
		table.setPreferredScrollableViewportSize(new Dimension(500, 70));
        table.setFillsViewportHeight(true);
        
        scroll = new JScrollPane(table);
        scroll.setBounds(100, 200, 500, 250 ); // x, y, width, height
        this.add(scroll);
        
        JLabel title = new ColorfulLabel("Cancel an appointment", new Color(121, 134, 203), 25);
        title.setBounds(100, 100, 500, 60);
        this.add(title);
        label = new ColorfulLabel("Number:", null, 18);
        label.setBounds(100, 470, 150, 30);
        this.add(label);
        aAppointment = new JTextField();
        aAppointment.setBounds(300, 470, 200, 30);
        this.add(aAppointment);
        
        back = new JButton("Back");
		back.setActionCommand("Back");
		back.addActionListener(this);
		back.setFont(new Font("Courier", Font.BOLD, 15));
		back.setBackground(new Color(121, 134, 203));
		cancel = new JButton("Cancel");
		cancel.setActionCommand("Cancel");
		cancel.addActionListener(this);
		cancel.setFont(new Font("Courier", Font.BOLD, 15));
		cancel.setBackground(new Color(121, 134, 203));
		back.setBounds(180, 550, 150, 40);
		cancel.setBounds(380, 550, 150, 40);
		this.add(back);
		this.add(cancel);
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
	
	private void updateTable() {
		this.remove(scroll);
		toAppointment = FlexiBookController.getAppointments();
		String[] columnNames = {"Number","Name","Service", "Start Date", "Start Time","End Time"};
		 Username=FlexiBookApplication.getCurrentUser().getUsername();
         List<TOAppointmentItem> toAppointmentofUser = new ArrayList<TOAppointmentItem>();
		
		for(int i=0;i<toAppointment.size();i++) {
		 if(toAppointment.get(i).getCustomerName().equals(Username)) {
			 toAppointmentofUser.add(toAppointment.get(i));
		  }
		}
		
		String[][] data = new String[toAppointmentofUser.size()][6];
		for(int i=0; i<toAppointmentofUser.size(); i++) {
			data[i][0] = Integer.toString(i+1);
			data[i][1] = toAppointmentofUser.get(i).getCustomerName();
			data[i][2] = toAppointmentofUser.get(i).getServiceName();
			data[i][3] = toAppointmentofUser.get(i).getDate().toString();
			data[i][4] = toAppointmentofUser.get(i).getStartTime().toString();
			data[i][5] = toAppointmentofUser.get(i).getEndTime().toString();
			
		}
		
		table = new JTable(data, columnNames);
		table.setModel( new DefaultTableModel(data, columnNames));
		table.getTableHeader().setFont(new Font("SansSerif", Font.ITALIC, 12));
		table.setFont(new Font("Dialog", Font.BOLD, 15));
		table.setRowHeight(table.getRowHeight()+5);
		table.setPreferredScrollableViewportSize(new Dimension(500, 70));
        table.setFillsViewportHeight(true);
        
        scroll = new JScrollPane(table);
        scroll.setBounds(100, 200, 500, 250 ); // x, y, width, height
        this.add(scroll);
		this.validate();
		this.revalidate();
		this.repaint();
	}
	
	private void addHint(String message) {
		hint = new ColorfulLabel(message,null,15);
		hint.setBounds(100, 500, 450, 40);
		this.add(hint);
		this.validate();
		this.revalidate();
		this.repaint();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(cancel)) {
			FlexiBookApplication.setSystemDateAndTime();
			try {
				if(hint!=null)	
					this.remove(hint);
				if(this.aAppointment.getText().equals(""))
					addHint("Please choose the number of appointment ");
				
				else {
				Boolean a = false;
				int appointmentnum = Integer.parseInt(this.aAppointment.getText());
				toAppointment = FlexiBookController.getAppointments();
				for(int i=0; i<toAppointment.size(); i++) {
				   if ((i+1)==appointmentnum) {
					  a=FlexiBookController.cancelanAppointment(toAppointment.get(i).getServiceName(), toAppointment.get(i).getDate(), toAppointment.get(i).getStartTime());
				   }
				}
				
				
				if(a) {
					this.updateTable();
					addHint("Appointment canceled successfully");
				}else {
					this.updateTable();
					addHint("Appointment can not be canceled successfully on the day");
				}
				
				}
			}catch(InvalidInputException IIexception) {
				addHint(IIexception.getMessage());
			}
		}else if(e.getSource().equals(back)) {
			app.updateMainPanel(new ManageAppointmentsUser(app));
		}
		
	}
}

