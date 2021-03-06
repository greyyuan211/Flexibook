/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package ca.mcgill.ecse.flexibook.application;
import java.awt.EventQueue;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import ca.mcgill.ecse.flexibook.model.FlexiBook;
import ca.mcgill.ecse.flexibook.model.User;
import ca.mcgill.ecse.flexibook.persistence.FlexiBookPersistence;
import ca.mcgill.ecse.flexibook.view.FlexiAppPage;

public class FlexiBookApplication {
	
	private static FlexiBook flexibook;
	private static User currentUser;
	private static Date date;
	private static Time time;
	
    public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FlexiAppPage frame = new FlexiAppPage();
					frame.setVisible(true);
					frame.addWindowListener(new WindowAdapter() {
						@Override
						public void windowClosing(WindowEvent e) {
							
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
    }
    
	public static FlexiBook getFlexiBook() {
		if (flexibook == null) {
			flexibook = FlexiBookPersistence.load();
		} 
 		return flexibook;
	}
	
	public static User getCurrentUser() {
		return currentUser;
	}
	
	public static void setCurrentUser(User current) {
		currentUser = current;
	}
	
    public String getGreeting() {
        return "Hello world.";
    }
	
    public static Date getCurrentDate() {
    	return date;
    }
    
    public static void setCurrentDate(Date curDate) {
    	date = curDate;
    }
    
    public static Time getCurrentTime() {
    	return time;
    }
    
    public static void setCurrentTime(Time curTime) {
    	time = curTime;
    }
    
    public static void setSystemDateAndTime() {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateAndTime = formatter.format(calendar.getTime());
		Date curDate = Date.valueOf(dateAndTime.substring(0, 10));
		Time curTime = Time.valueOf(dateAndTime.substring(11));
		FlexiBookApplication.setCurrentDate(curDate);
		FlexiBookApplication.setCurrentTime(curTime);
    }

}
