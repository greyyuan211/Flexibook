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
import ca.mcgill.ecse.flexibook.controller.TOAppointmentItem;
import ca.mcgill.ecse.flexibook.controller.TOService;
import ca.mcgill.ecse.flexibook.view.ServiceMain.ColorfulLabel;
/** 
 * @author Jiatong Niu
 * this feature allows customer to view the appointments he previously booked and make a new one or cancel a previous one
 */
public class ManageAppointmentsUser extends JPanel implements ActionListener {
	private FlexiAppPage app;
	private JLabel warning;
	private JTable table;
    private JButton make;
	private JButton cancel;
	private List<TOAppointmentItem> toAppointment; 
	private String Username;
	
	public ManageAppointmentsUser(FlexiAppPage app) {
		this.app = app;
		Username=FlexiBookApplication.getCurrentUser().getUsername();
		warning = null;
		toAppointment = FlexiBookController.getAppointments();
		
		setLayout(null);
		setSize(750,800);
		Color blue = new Color(232, 234, 246);
		setBackground(blue);	//set background color
		
		String[] columnNames = {"Number","Name","Servive", "Start Date", "Start Time","End Time"};
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
        
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBounds(100, 200, 500, 250 ); // x, y, width, height
        this.add(scroll);
        
        JLabel title = new ColorfulLabel("Manage Appointment", new Color(121, 134, 203), 25);
        title.setBounds(100, 100, 500, 60);
        this.add(title);
        make = new JButton("Make an Appointment");
      	make.setActionCommand("Make");
      	make.addActionListener(this);
      	make.setFont(new Font("Courier", Font.BOLD, 15));
      	make.setBackground(new Color(121, 134, 203));
      	cancel = new JButton("Cancel an Appointment");
      	cancel.setActionCommand("Cancel");
      	cancel.addActionListener(this);
      	cancel.setFont(new Font("Courier", Font.BOLD, 15));
      	cancel.setBackground(new Color(121, 134, 203));
      	make.setBounds(200, 470, 300, 40);
      	cancel.setBounds(200, 520, 300, 40);
      	this.add(make);
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
		this.remove(table);

		String[] columnNames = {"Number","Name","Servive", "Start Date", "Start Time","End Time"};
		Username=FlexiBookApplication.getCurrentUser().getUsername();
		List<TOAppointmentItem> toAppointmentofUser =new ArrayList<TOAppointmentItem>();;
		
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
        
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBounds(100, 200, 500, 250 ); // x, y, width, height
        this.add(scroll);
		this.validate();
		this.revalidate();
		this.repaint();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(cancel)) {
			app.updateMainPanel(new CancelAppointmentUser(app));
		}else if(e.getSource().equals(make)) {
			app.updateMainPanel(new MakeAppointment(app));
		}
		
	}
}

