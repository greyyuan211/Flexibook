package ca.mcgill.ecse.flexibook.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.sql.Time;
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
import ca.mcgill.ecse.flexibook.controller.TOService;
import ca.mcgill.ecse.flexibook.view.ManageAppointmentsUser.ColorfulLabel;
/** 
 * @author Jiatong Niu
 * allow user to make appointment on services provided by owner
 */
public class AppointmentonService extends JPanel implements ActionListener {
	private FlexiAppPage app;
	private JLabel hint;
	private JTable table;
    private JButton confirm;
	private JButton back;
	private List<TOService> toServices;
	private JLabel ServicenameLabel;
	private JTextField ServicenameTxt;
	private JLabel DateLabel;
	private JTextField DateTxt;
	private JLabel StarttimeLabel;
	private JTextField StarttimeTxt;
	
	public AppointmentonService(FlexiAppPage app) {
		this.app = app;

		hint = null;
		toServices = FlexiBookController.getServices();
		setLayout(null);
		setSize(750,800);
		Color blue = new Color(232, 234, 246);
		setBackground(blue);	//set background color
		
		String[] columnNames = {"Service Name", "Duration","Downtime Duration", "Start of Downtime"};
		
		String[][] data = new String[toServices.size()][4];
		for(int i=0; i<toServices.size(); i++) {
			data[i][0] = toServices.get(i).getName();
			data[i][1] = Integer.toString(toServices.get(i).getDuration());
			data[i][2] = Integer.toString(toServices.get(i).getDowntimeDuration());
			data[i][3] = Integer.toString(toServices.get(i).getDowntimeStart());
		}
		
		
		table = new JTable(data, columnNames);
		table.setModel( new DefaultTableModel(data, columnNames));
		table.getTableHeader().setFont(new Font("SansSerif", Font.ITALIC, 12));
		table.setFont(new Font("Dialog", Font.BOLD, 15));
		table.setRowHeight(table.getRowHeight()+5);
		table.setPreferredScrollableViewportSize(new Dimension(500, 70));
        table.setFillsViewportHeight(true);
        
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBounds(100, 200, 500, 150 ); // x, y, width, height
        this.add(scroll);
        
        JLabel title = new ColorfulLabel("Make an Appointment", new Color(121, 134, 203), 25);
        title.setBounds(100, 100, 500, 60);
        this.add(title);
        
        ServicenameLabel = new ColorfulLabel("Service:",new Color(121, 134, 203),15);
		ServicenameTxt = new JTextField();
		DateLabel = new ColorfulLabel("Date(YYYY-MM-DD):",new Color(121, 134, 203),15);
		DateTxt = new JTextField();
		StarttimeLabel = new ColorfulLabel("Start time(HH:MM):",new Color(121, 134, 203),15);
		StarttimeTxt = new JTextField();
		
		ServicenameLabel.setBounds(100, 400, 200, 40);
		DateLabel.setBounds(100, 450, 200, 40);
		StarttimeLabel.setBounds(100, 500, 200, 40);
		
		ServicenameTxt.setBounds(400, 400, 200, 40);
		DateTxt.setBounds(400, 450, 200, 40);
		StarttimeTxt.setBounds(400, 500, 200, 40);
		
		
		
        back = new JButton("Back");
      	back.setActionCommand("Back");
      	back.addActionListener(this);
      	back.setFont(new Font("Courier", Font.BOLD, 15));
      	back.setBackground(new Color(121, 134, 203));
      	confirm = new JButton("Confirm");
      	confirm.setActionCommand("Confirm");
      	confirm.addActionListener(this);
      	confirm.setFont(new Font("Courier", Font.BOLD, 15));
      	confirm.setBackground(new Color(121, 134, 203));
      	back.setBounds(200, 600, 100, 40);
      	confirm.setBounds(400, 600, 100, 40);
      	
      	this.add(ServicenameLabel);
      	this.add(DateLabel);
      	this.add(StarttimeLabel);
     	this.add(ServicenameTxt);	
     	this.add(DateTxt);
      	this.add(StarttimeTxt);
      	
      	this.add(back);
      	this.add(confirm);
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
		hint.setBounds(100, 160, 500, 40);
		this.add(hint);
		this.validate();
		this.revalidate();
		this.repaint();
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(confirm)) {
			
			try {
				if(hint!=null)	this.remove(hint);
				String serviceName = null;
				Date date = null;
				Time starttime= null;
				Time endtime =null;
				FlexiBookApplication.setSystemDateAndTime();
				if(!ServicenameTxt.getText().equals("")) {
				 serviceName = ServicenameTxt.getText();
				}
				if(!DateTxt.getText().equals("")) {
				 date = Date.valueOf(DateTxt.getText());
				
				}
				if(!StarttimeTxt.getText().equals("")) {
			      starttime = Time.valueOf(StarttimeTxt.getText()+":00");
				}
				
				if(FlexiBookController.makeanAppointment(serviceName, date, starttime)) {
				addHint("Make an appointment successfully");
				}else {
				addHint("Fail. Please check your serviceName,date and time");
				}
				
			}catch(InvalidInputException IIexception) {
				addHint(IIexception.getMessage());
			}
		}else if(e.getSource().equals(back)) {
			app.updateMainPanel(new MakeAppointment(app));
		}
		
	}
	
}

