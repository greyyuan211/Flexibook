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
 * @author jiang
 * This panel visulizes all the services in the system and allows the owner to choose his/her next actions(add, update or delete services)
 */
public class ServiceMain extends JPanel implements ActionListener{
	private FlexiAppPage app;
	private JLabel warning;
	private JTable table;
	private JButton add;
	private JButton update;
	private JButton delete;
	private List<TOService> toServices; 
	public ServiceMain(FlexiAppPage app) {
		this.app = app;
		warning = null;
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
        
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBounds(100, 200, 500, 250 ); // x, y, width, height
        this.add(scroll);
        
        JLabel title = new ColorfulLabel("Services", new Color(129, 199, 132), 25);
        title.setBounds(100, 100, 500, 60);
        this.add(title);
        
        add = new JButton("Add");
		add.setActionCommand("Add");
		add.addActionListener(this);
		add.setFont(new Font("Courier", Font.BOLD, 15));
		add.setBackground(new Color(241, 248, 233));
		update = new JButton("Update");
		update.setActionCommand("Update");
		update.addActionListener(this);
		update.setFont(new Font("Courier", Font.BOLD, 15));
		update.setBackground(new Color(241, 248, 233));
		delete = new JButton("Delete");
		delete.setActionCommand("Delete");
		delete.addActionListener(this);
		delete.setFont(new Font("Courier", Font.BOLD, 15));
		delete.setBackground(new Color(241, 248, 233));
		add.setBounds(100, 470, 140, 40);
		update.setBounds(280, 470, 140, 40);
		delete.setBounds(460, 470, 140, 40);
		this.add(add);
		this.add(update);
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
		this.remove(table);
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
        
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBounds(100, 200, 500, 250 ); // x, y, width, height
        this.add(scroll);
		this.validate();
		this.revalidate();
		this.repaint();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(delete)) {
			app.updateMainPanel(new DeleteService(app));
		}else if(e.getSource().equals(update)) {
			app.updateMainPanel(new UpdateService(app));
		}else if(e.getSource().equals(add)) {
			app.updateMainPanel(new AddService(app));
		}
		
	}
}
