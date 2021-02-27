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
 * A control panel for the owner to delete services.
 */
public class DeleteService extends JPanel implements ActionListener{
	private FlexiAppPage app;
	private JLabel hint;
	private JTable table;
	private JButton back;
	private JButton delete;
	private JLabel label;
	private JTextField aService;
	private List<TOService> toServices; 
	private JScrollPane scroll;
	public DeleteService(FlexiAppPage app) {
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
        scroll.setBounds(100, 200, 500, 250 ); // x, y, width, height
        this.add(scroll);
        
        JLabel title = new ColorfulLabel("Delete a Service", new Color(129, 199, 132), 25);
        title.setBounds(100, 100, 500, 60);
        this.add(title);
        label = new ColorfulLabel("Service Name:", null, 18);
        label.setBounds(100, 470, 150, 30);
        this.add(label);
        aService = new JTextField();
        aService.setBounds(300, 470, 200, 30);
        this.add(aService);
        
        back = new JButton("Back");
		back.setActionCommand("Back");
		back.addActionListener(this);
		back.setFont(new Font("Courier", Font.BOLD, 15));
		back.setBackground(new Color(241, 248, 233));
		delete = new JButton("Delete");
		delete.setActionCommand("Delete");
		delete.addActionListener(this);
		delete.setFont(new Font("Courier", Font.BOLD, 15));
		delete.setBackground(new Color(241, 248, 233));
		back.setBounds(180, 550, 150, 40);
		delete.setBounds(380, 550, 150, 40);
		this.add(back);
		this.add(delete);
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
		if(e.getSource().equals(delete)) {
			try {
				if(hint!=null)	this.remove(hint);
				String serviceName = this.aService.getText();
				FlexiBookController.deleteService(serviceName);
				this.updateTable();
				addHint("Delete a service successfully");
			}catch(InvalidInputException IIexception) {
				addHint(IIexception.getMessage());
			}
		}else if(e.getSource().equals(back)) {
			app.updateMainPanel(new ServiceMain(app));
		}
		
	}
}
