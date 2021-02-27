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
import ca.mcgill.ecse.flexibook.controller.TOBusiness;
import ca.mcgill.ecse.flexibook.view.AddService.ColorfulLabel;

public class BusinessMain extends JPanel implements ActionListener{
	private FlexiAppPage app;
	private JLabel warning;
	private JTable table;
	private List<TOBusiness> toBusiness; 
	
	/**
	 * @author Grey Yuan
	 * implement view business info in UI
	 */
	public BusinessMain(FlexiAppPage app) {
		this.app = app;
		warning = null;
		toBusiness = FlexiBookController.getBasicBusinessInfo();;
		setLayout(null);
		setSize(750,800);
		Color background = new Color(232, 245, 233);
		setBackground(background);	//set background color
		
		String[] columnNames = {"Name",
                "Address",
                "Phone Number",
                "Email"};
		
		String[][] data = new String[1][4];
		data[0][0] = toBusiness.get(0).getName();
		data[0][1] = toBusiness.get(0).getAddress();
		data[0][2] = toBusiness.get(0).getPhoneNumber();
		data[0][3] = toBusiness.get(0).getEmail();
		
		
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
        
        JLabel title = new ColorfulLabel("Business Information", new Color(129, 199, 132), 25);
        title.setBounds(100, 100, 500, 60);
        this.add(title);
       

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
	
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
//		if(e.getSource().equals(back)) {
//			app.updateMainPanel(new AddBusinessInfo(app));
//		}
		
	}
}
