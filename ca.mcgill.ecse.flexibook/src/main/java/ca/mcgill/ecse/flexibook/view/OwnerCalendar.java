package ca.mcgill.ecse.flexibook.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Time;
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
import ca.mcgill.ecse.flexibook.controller.TOViewAppointment;
import ca.mcgill.ecse.flexibook.view.AddService.ColorfulLabel;

public class OwnerCalendar extends JPanel implements ActionListener{
	private FlexiAppPage app;
	private JLabel warning;
	private JTable table;
	private JButton view;
	private JTextField dateTxt;
	private JLabel dateLabel;
	private List<TOViewAppointment> toViewAppointment; 
	private JLabel hint;
	private JScrollPane scroll;

	public OwnerCalendar(FlexiAppPage app) {
		this.app = app;
		warning = null;
		setLayout(null);
		setSize(750,800);
		Color background = new Color(232, 245, 233);
		Color green = new Color(129, 199, 132);
		setBackground(background);	//set background color
		dateLabel = new ColorfulLabel("Date:",green,15);
		dateTxt = new JTextField();
		
		dateLabel.setBounds(100, 500, 200, 40);
		dateTxt.setBounds(400, 500, 200, 40);
		this.add(dateLabel);
		this.add(dateTxt);
		
		String[] columnNames = {"Date",
                "Start Time",
                "End Time",
                "Is Available"};
		
		String[][] data = new String[20][4];
		for(int i=0; i<20; i++) {
//			data[i][0] = toViewAppointment.get(i).getDate().toString();
//			data[i][1] = toViewAppointment.get(i).getStarttime().toString();
//			data[i][2] = toViewAppointment.get(i).getEndtime().toString();
//			data[i][3] = Boolean.toString(toViewAppointment.get(i).isIsAvailable());
			data[i][0] = null;
			data[i][1] = null;
			data[i][2] = null;
			data[i][3] = null;
		}
		
		table = new JTable(data, columnNames);
		table.setModel( new DefaultTableModel(data, columnNames));
		table.getTableHeader().setFont(new Font("SansSerif", Font.ITALIC, 12));
		table.setFont(new Font("Dialog", Font.BOLD, 15));
		table.setRowHeight(table.getRowHeight()+5);
		table.setPreferredScrollableViewportSize(new Dimension(500, 70));
        table.setFillsViewportHeight(true);
        
        scroll = new JScrollPane(table);
        scroll.setBounds(100, 250, 500, 250 ); // x, y, width, height
        this.add(scroll);
        
        JLabel title = new ColorfulLabel("View Calendar", new Color(129, 199, 132), 25);
        title.setBounds(100, 100, 500, 60);
        this.add(title);
        
        view = new JButton("View date");
		view.setActionCommand("View date");
		view.addActionListener(this);
		view.setFont(new Font("Courier", Font.BOLD, 15));
		view.setBackground(new Color(241, 248, 233));
		view.setBounds(450, 620, 150, 40);
		this.add(view);

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
	
	private void updateTable() {
		this.remove(scroll);		
		String date = dateTxt.getText();
		try {
			toViewAppointment = FlexiBookController.ViewAppointment_At_date(date);
		}catch(InvalidInputException IIexception) {
			addHint(IIexception.getMessage());
		}

		String[] columnNames = {"Date",
                "Start Time",
                "End Time",
                "Is Available"};
		System.out.println(toViewAppointment.size());
		String[][] data = new String[toViewAppointment.size()][4];
		for(int i=0; i<toViewAppointment.size(); i++) {
			data[i][0] = toViewAppointment.get(i).getDate().toString();
			data[i][1] = toViewAppointment.get(i).getStarttime().toString();
			data[i][2] = toViewAppointment.get(i).getEndtime().toString();
			data[i][3] = Boolean.toString(toViewAppointment.get(i).isIsAvailable());
		}
		table = new JTable(data, columnNames);
		table.setModel( new DefaultTableModel(data, columnNames));
		table.getTableHeader().setFont(new Font("SansSerif", Font.ITALIC, 12));
		table.setFont(new Font("Dialog", Font.BOLD, 15));
		table.setRowHeight(table.getRowHeight()+5);
		table.setPreferredScrollableViewportSize(new Dimension(500, 70));
        table.setFillsViewportHeight(true);
        
        scroll = new JScrollPane(table);
        scroll.setBounds(100, 250, 500, 250 ); // x, y, width, height
        this.add(scroll);
		this.validate();
		this.revalidate();
		this.repaint();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		updateTable();

	}
}
