package ca.mcgill.ecse.flexibook.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import ca.mcgill.ecse.flexibook.application.FlexiBookApplication;
import ca.mcgill.ecse.flexibook.controller.FlexiBookController;

public class FlexiAppPage extends JFrame{
	private JPanel mainPanel = null;

	public FlexiAppPage() {
		this.setSize(300, 450);
		this.updateToSPage(new Login(this));
	}
	
	/**
	 * Update to the smaller page
	 * @param panel: can be login page or signup page
	 */
	public void updateToSPage(JPanel panel) {
		mainPanel = null;
		//remove all the components of the current page
		this.getContentPane().removeAll();
		this.repaint();
		//resize
		this.setSize(300,450);
		this.setLayout(null);
		this.add(panel);
		this.validate();
		this.revalidate();
		this.repaint();
	}
	
	/**
	 * Update to the bigger page
	 */
	public void updateToLPage() {
		//remove all the components of the current page
		this.getContentPane().removeAll();
		this.repaint();
		//resize
		this.setSize(1000,800); 
		this.setLayout(null);
	}
	
	/**
	 * Initiate owner page with a welcome message
	 */
	public void initOwnerPage() {
		updateToLPage();
		// Add Owner Panel to the left
		OwnerSidePanel ownerSidePanel = new OwnerSidePanel(this);
		ownerSidePanel.setBounds(0, 0, 250, 800);
		this.add(new OwnerSidePanel(this));
		// Add greeting message to the right
		JPanel greeting = makeGreetingPanel();
		Color green = new Color(232, 245, 233);
		greeting.setBackground(green);	//set background color
		greeting.setBounds(250, 0, 750, 800);	//set location and size
		mainPanel = greeting;
		this.add(greeting);
		this.setVisible(true);

	}
	
	/**
	 * Initiate customer page with a welcome message
	 */
	public void initCustomerPage() {
		updateToLPage();
		// Add customer panel to the left
		CustomerSidePanel customerSidePanle = new CustomerSidePanel(this);
		customerSidePanle.setBounds(0, 0, 250, 800);
		this.add(customerSidePanle);
		// Add greeting message to the right
		JPanel greeting = makeGreetingPanel();
		Color blue = new Color(232, 234, 246);
		greeting.setBackground(blue);	//set background color
		greeting.setBounds(250, 0, 750, 800);	//set location and size
		mainPanel = greeting;
		this.add(greeting);
		this.setVisible(true);
	}
	
	/**
	 * Update the main panel (the panel on the right) of the owner page or the customer page
	 * @param newPanel: new panel
	 */
	public void updateMainPanel(JPanel newPanel) {
		this.remove(mainPanel);
		mainPanel = newPanel;
		newPanel.setBounds(250, 0, 750, 800);
		this.add(newPanel);
		this.validate();
		this.revalidate();
		this.repaint();
	}
	
	/**
	 * This method makes a greeting panel
	 * @return the greeting panel
	 */
	public JPanel makeGreetingPanel() {
		JPanel mainPanel = new JPanel();
		// add the welcome message to the center of the panel
		mainPanel.setLayout(new GridLayout());
		JLabel greeting = new JLabel("Welcome!",SwingConstants.CENTER);
		greeting.setForeground(Color.gray);
		int fontSizeToUse = 30;
		greeting.setFont(new Font("Serif", Font.PLAIN,fontSizeToUse));
		mainPanel.add(greeting);
		return mainPanel;
	}

}
