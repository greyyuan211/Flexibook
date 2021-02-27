package ca.mcgill.ecse.flexibook.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import ca.mcgill.ecse.flexibook.controller.FlexiBookController;
import ca.mcgill.ecse.flexibook.controller.InvalidInputException;
import ca.mcgill.ecse.flexibook.controller.TOService;
import ca.mcgill.ecse.flexibook.view.AddService.ColorfulLabel;

/**
 * @author Cecilia Jiang
 * A control panel for the owner to update services.
 */
public class UpdateService extends JPanel implements ActionListener{
	private FlexiAppPage app;
	private JLabel hint;
	private JTable table;
	private JButton back;
	private JButton update;
	private JLabel curService;
	private JLabel newService;
	private JLabel duration; 
	private JLabel downtimeDuration; 
	private JLabel downtimeStart;
	private JTextField curServiceTxt;
	private JTextField newServiceTxt;
	private JTextField durTxt;
	private JTextField downtimeDurTxt;
	private JTextField downtimeStartTxt;
	private List<TOService> toServices; 
	private JScrollPane scroll;
	public UpdateService(FlexiAppPage app) {
		this.app = app;
		hint = null;
		toServices = FlexiBookController.getServices();
		setLayout(null);
		setSize(750,800);
		Color background = new Color(232, 245, 233);
		setBackground(background);	//set background color
		
		String[] columnNames = {"Service Name",
                "Duration",
                "Downtime Duration",
                "Start of Downtime"};
		
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
        
        scroll = new JScrollPane(table);
        scroll.setBounds(100, 150, 500, 250); // x, y, width, height
        this.add(scroll);
        
        JLabel title = new ColorfulLabel("Update a Service", new Color(129, 199, 132), 25);
        title.setBounds(100, 50, 500, 60);
        this.add(title);
        curService = new ColorfulLabel("Service name:", null, 14);
        curService.setBounds(100, 420, 200, 30);
        this.add(curService);
        curServiceTxt = new JTextField();
        curServiceTxt.setBounds(300, 420, 200, 30);
        this.add(curServiceTxt);
        newService = new ColorfulLabel("New service name:", null, 14);
        newService.setBounds(100, 460, 200, 30);
        this.add(newService);
        newServiceTxt = new JTextField();
        newServiceTxt.setBounds(300, 460, 200, 30);
        this.add(newServiceTxt);
        duration = new ColorfulLabel("New duration:", null, 14);
        duration.setBounds(100, 500, 200, 30);
        this.add(duration);
        durTxt = new JTextField();
        durTxt.setBounds(300, 500, 200, 30);
        this.add(durTxt);
        downtimeDuration = new ColorfulLabel("New downtime duration:", null, 14);
        downtimeDuration.setBounds(100, 540, 200, 30);
        this.add(downtimeDuration);
        downtimeDurTxt = new JTextField();
        downtimeDurTxt.setBounds(300, 540, 200, 30);
        this.add(downtimeDurTxt);
        downtimeStart = new ColorfulLabel("New start of downtime:", null, 14);
        downtimeStart.setBounds(100, 580, 200, 30);
        this.add(downtimeStart);
        downtimeStartTxt = new JTextField();
        downtimeStartTxt.setBounds(300, 580, 200, 30);
        this.add(downtimeStartTxt);
        
        back = new JButton("Back");
		back.setActionCommand("Back");
		back.addActionListener(this);
		back.setFont(new Font("Courier", Font.BOLD, 15));
		back.setBackground(new Color(241, 248, 233));
		update = new JButton("Update");
		update.setActionCommand("Update");
		update.addActionListener(this);
		update.setFont(new Font("Courier", Font.BOLD, 15));
		update.setBackground(new Color(241, 248, 233));
		back.setBounds(180, 620, 150, 40);
		update.setBounds(380, 620, 150, 40);
		this.add(back);
		this.add(update);
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
		toServices = FlexiBookController.getServices();
		String[] columnNames = {"Service Name",
                "Duration",
                "Downtime Duration",
                "Start of Downtime"};
		
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
        
        scroll = new JScrollPane(table);
        scroll.setBounds(100, 150, 500, 250 ); // x, y, width, height
        this.add(scroll);
		this.validate();
		this.revalidate();
		this.repaint();
	}
	
	private void addHint(String message) {
		hint = new ColorfulLabel(message,null,15);
		hint.setBounds(100, 100, 550, 40);
		this.add(hint);
		this.validate();
		this.revalidate();
		this.repaint();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(update)) {
			try {
				if(hint!=null)	this.remove(hint);
				String aCurservice = this.curServiceTxt.getText();
				String aNewService = this.newServiceTxt.getText();
				String aDur = this.durTxt.getText();
				String aDowntimeDur = this.downtimeDurTxt.getText();
				String aDowntimeStart = this.downtimeStartTxt.getText();
				FlexiBookController.updateService(aCurservice, aNewService, aDur, aDowntimeDur, aDowntimeStart);
				this.updateTable();
				addHint("Update a service successfully");
			}catch(InvalidInputException IIexception) {
				addHint(IIexception.getMessage());
			}
		}else if(e.getSource().equals(back)) {
			app.updateMainPanel(new ServiceMain(app));
		}
		
	}
	
	public static boolean isNumeric(String str) { 
		  try {  
		    Double.parseDouble(str);  
		    return true;
		  } catch(NumberFormatException e){  
		    return false;  
		  }  
		}
}

