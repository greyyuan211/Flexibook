package ca.mcgill.ecse.flexibook.controller;


import java.util.ArrayList;
import java.util.List;
import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import ca.mcgill.ecse.flexibook.application.FlexiBookApplication;
import ca.mcgill.ecse.flexibook.model.*;
import ca.mcgill.ecse.flexibook.model.BusinessHour.DayOfWeek;
import ca.mcgill.ecse.flexibook.persistence.FlexiBookPersistence;


/**
 * @author lizhiwei
 *
 */
public class FlexiBookController {
	
	public FlexiBookController() {
	}
	

	/**@author lizhiwei
	 * @param username
	 * @param password
	 * @throws InvalidInputException
	 */
	public static void Login(String username, String password) throws InvalidInputException {
		
		FlexiBook flexibook = FlexiBookApplication.getFlexiBook();
		String error="";
		if(username == null || username.isEmpty()) {
			error = "The user name cannot be empty";
		} else if (password == null || password.isEmpty()){
			error = "The password cannot be empty";
		}
		if (error.length() > 0) {
			throw new InvalidInputException(error.trim());
		}
		if (username.equals("owner")){
			if (flexibook.hasOwner()) {
				Owner owner=flexibook.getOwner();
				if (password.equals(owner.getPassword())){
					FlexiBookApplication.setCurrentUser(flexibook.getOwner());
				}
				else {
					FlexiBookApplication.setCurrentUser(null);
					throw new InvalidInputException("Username/password not found");
				}
			}
			else {
				Owner owner=new Owner(username,password,flexibook);
				flexibook.setOwner(owner);
				FlexiBookApplication.setCurrentUser(owner);
			}
		}
		
		else {
			if(User.hasWithUsername(username)) {
				if(password.equals(User.getWithUsername(username).getPassword())) {
					FlexiBookApplication.setCurrentUser(User.getWithUsername(username));
				}
				else
				{
					FlexiBookApplication.setCurrentUser(null);
					throw new InvalidInputException("Username/password not found");
				}
			}
			else {
				FlexiBookApplication.setCurrentUser(null);
				throw new InvalidInputException("Username/password not found");
			}
		}
		
		
	}
	
	/**@author lizhiwei
	 * @throws InvalidInputException
	 */
	public static void LogOut() throws InvalidInputException {
		if (FlexiBookApplication.getCurrentUser()==null) {
			throw new InvalidInputException("The user is already logged out");
		}
		else {
			FlexiBookApplication.setCurrentUser(null);
		}
	}
	

	/**@author lizhiwei
	 * @param dateString
	 * @return
	 * @throws InvalidInputException
	 */
	public static ArrayList<TOViewAppointment> ViewAppointment_At_date(String dateString) throws InvalidInputException {
		FlexiBook flexibook = FlexiBookApplication.getFlexiBook();
		ArrayList<TOViewAppointment> Unavilitems = new ArrayList<TOViewAppointment>();
		ArrayList<TOViewAppointment> Avilitems = new ArrayList<TOViewAppointment>();
		ArrayList<TOViewAppointment> Allitems = new ArrayList<TOViewAppointment>();
		List<Appointment> Appointments = flexibook.getAppointments();
		ArrayList<Appointment> AppointmentsAtDate = new ArrayList<Appointment>();
		List<BusinessHour> buisnesshours = flexibook.getBusiness().getBusinessHours();
		BusinessHour buisnesshourAtdate = null;
		List<TimeSlot> holidays = flexibook.getBusiness().getHolidays();
		int i;
		i = buisnesshours.size();

		Date date;
		try {
			date = Date.valueOf(dateString);
		} catch (java.lang.IllegalArgumentException e) {
			throw new InvalidInputException(dateString + " is not a valid date");
		}

		String WeekDay = getWeek(date);

		List<TimeSlot> vacations = flexibook.getBusiness().getVacation();

		for (BusinessHour buisnesshour : buisnesshours) {
			if (buisnesshour.getDayOfWeek().toString().equals(WeekDay)) {
				buisnesshourAtdate = buisnesshour;
				Boolean isHoliday = false;
				for (TimeSlot holiday : holidays) {
					if ((holiday.getEndDate().compareTo(date) > 0) && (holiday.getStartDate().compareTo(date) < 0)) {
						TOViewAppointment item = new TOViewAppointment(date, buisnesshourAtdate.getStartTime(),
								buisnesshourAtdate.getEndTime(), false);
						Unavilitems.add(item);
						return Unavilitems;
					}
					if (holiday.getStartDate().compareTo(date) == 0) {
						if (holiday.getStartTime().compareTo(buisnesshourAtdate.getStartTime()) >= 0) {
							TOViewAppointment avil = new TOViewAppointment(date, buisnesshourAtdate.getStartTime(),
									holiday.getStartTime(), true);
							Avilitems.add(avil);
							isHoliday = true;
							continue;
						} else {
							TOViewAppointment item = new TOViewAppointment(date, buisnesshourAtdate.getStartTime(),
									buisnesshourAtdate.getEndTime(), false);
							Unavilitems.add(item);
							return Unavilitems;
						}
					}
					if (holiday.getEndDate().compareTo(date) == 0) {
						if (holiday.getEndTime().compareTo(buisnesshourAtdate.getEndTime()) <= 0) {
							TOViewAppointment avil = new TOViewAppointment(date, buisnesshourAtdate.getStartTime(),
									holiday.getStartTime(), true);
							Avilitems.add(avil);
							isHoliday = true;
							continue;
						} else {
							TOViewAppointment item = new TOViewAppointment(date, buisnesshourAtdate.getStartTime(),
									buisnesshourAtdate.getEndTime(), false);
							Unavilitems.add(item);
							return Unavilitems;
						}
					}
				}
				for (TimeSlot holiday : vacations) {
					if ((holiday.getEndDate().compareTo(date) > 0) && (holiday.getStartDate().compareTo(date) < 0)) {
						TOViewAppointment item = new TOViewAppointment(date, buisnesshourAtdate.getStartTime(),
								buisnesshourAtdate.getEndTime(), false);
						Unavilitems.add(item);
						return Unavilitems;
					}
					if (holiday.getStartDate().compareTo(date) == 0) {
						if (holiday.getStartTime().compareTo(buisnesshourAtdate.getStartTime()) >= 0) {
							TOViewAppointment avil = new TOViewAppointment(date, buisnesshourAtdate.getStartTime(),
									holiday.getStartTime(), true);
							Avilitems.add(avil);
							isHoliday = true;
							break;
						} else {
							TOViewAppointment item = new TOViewAppointment(date, buisnesshourAtdate.getStartTime(),
									buisnesshourAtdate.getEndTime(), false);
							Unavilitems.add(item);
							return Unavilitems;
						}
					}
					if (holiday.getEndDate().compareTo(date) == 0) {
						if (holiday.getEndTime().compareTo(buisnesshourAtdate.getEndTime()) <= 0) {
							TOViewAppointment avil = new TOViewAppointment(date, buisnesshourAtdate.getStartTime(),
									holiday.getStartTime(), true);
							Avilitems.add(avil);
							isHoliday = true;
							break;
						} else {
							TOViewAppointment item = new TOViewAppointment(date, buisnesshourAtdate.getStartTime(),
									buisnesshourAtdate.getEndTime(), false);
							Unavilitems.add(item);
							return Unavilitems;
						}
					}
				}
				if (!isHoliday) {
					TOViewAppointment item = new TOViewAppointment(date, buisnesshourAtdate.getStartTime(),
							buisnesshourAtdate.getEndTime(), true);
					Avilitems.add(item);
				}
			}

		}

		// Consider the appointment

		for (Appointment appointment : Appointments) {
			TimeSlot Ts = appointment.getTimeSlot();

			if (Ts.getStartDate().equals(date)) {
				AppointmentsAtDate.add(appointment);
			}
		}

		for (Appointment appointment : AppointmentsAtDate) {
			BookableService services = appointment.getBookableService();
			TimeSlot Ts = appointment.getTimeSlot();
			Time EndTime = Ts.getEndTime();
			Time StartTime = Ts.getStartTime();

			if (services instanceof Service) {
				Unavilitems = addAllkindService(date, StartTime, EndTime, (Service) services, Unavilitems);
			} else {

				Time Start = StartTime;
				Time End;
				for (ComboItem item : appointment.getChosenItems()) {
					SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
					Service service = item.getService();
					int duration = service.getDuration();
					Calendar cal = Calendar.getInstance();
					cal.setTime(Start);
					cal.add(Calendar.MINUTE, duration);
					java.util.Date da = cal.getTime();
					String dayAsstring = sdf.format(da);
					End = Time.valueOf(dayAsstring);

					Unavilitems = addAllkindService(date, Start, End, service, Unavilitems);

					Start = End;
				}
			}
		}

		// substract unavil from avil

		for (TOViewAppointment unavil : Unavilitems) {
			for (TOViewAppointment avil : Avilitems) {
				if ((avil.getStarttime().compareTo(unavil.getStarttime()) <= 0)
						& (avil.getEndtime().compareTo(unavil.getEndtime()) >= 0)) {
					TOViewAppointment avil1 = new TOViewAppointment(date, avil.getStarttime(), unavil.getStarttime(),
							true);
					TOViewAppointment avil2 = new TOViewAppointment(date, unavil.getEndtime(), avil.getEndtime(), true);
					Avilitems.remove(avil);
					Avilitems.add(avil1);
					Avilitems.add(avil2);
					if (avil1.getStarttime().equals(avil1.getEndtime())) {
						Avilitems.remove(avil1);
					}
					if (avil2.getStarttime().equals(avil2.getEndtime())) {
						Avilitems.remove(avil2);
					}
					break;
				}
			}

		}

		Avilitems.addAll(Unavilitems);
		Allitems = Avilitems;

		return Allitems;
	}
//	public static ArrayList<TOViewAppointment>  ViewAppointment_At_date(String dateString) throws InvalidInputException {
//		   FlexiBook flexibook = FlexiBookApplication.getFlexiBook();
//		   ArrayList<TOViewAppointment> Unavilitems = new ArrayList<TOViewAppointment>();
//		   ArrayList<TOViewAppointment> Avilitems = new ArrayList<TOViewAppointment>();
//		   ArrayList<TOViewAppointment> Allitems = new ArrayList<TOViewAppointment>();
//		   List<Appointment> Appointments=flexibook.getAppointments();
//		   ArrayList<Appointment> AppointmentsAtDate=new ArrayList<Appointment>();
//		   List<BusinessHour> buisnesshours=flexibook.getBusiness().getBusinessHours();
//
//		   ArrayList<BusinessHour> buisnesshourcollection=new ArrayList<BusinessHour>();
//		   
//		   List<TimeSlot> holidays=flexibook.getBusiness().getHolidays();
//		   
//		   
//		   Date date;
//		   try {
//		    date = Date.valueOf(dateString);
//		   } catch(java.lang.IllegalArgumentException e){
//		    throw new InvalidInputException(dateString+" is not a valid date");
//		   }
//		   
//		   String WeekDay=getWeek(date);
//		   
//		  List<TimeSlot> vacations = flexibook.getBusiness().getVacation();
//
//		  for (BusinessHour buisnesshour : buisnesshours) {
//		   if (buisnesshour.getDayOfWeek().toString().equals(WeekDay)) {
//		    BusinessHour buisnesshourAtdate=null;
//		       buisnesshourcollection.add(buisnesshourAtdate);
//		    buisnesshourAtdate = buisnesshour;
//		    Boolean isHoliday = false;
//		    for (TimeSlot holiday : holidays) {
//		     if ((holiday.getEndDate().compareTo(date) > 0) && (holiday.getStartDate().compareTo(date) < 0)) {
//		      TOViewAppointment item = new TOViewAppointment(date, buisnesshourAtdate.getStartTime(),
//		        buisnesshourAtdate.getEndTime(), false);
//		      Unavilitems.add(item);
//		      return Unavilitems;
//		     }
//		     if (holiday.getStartDate().compareTo(date) == 0) {
//		      if (holiday.getStartTime().compareTo(buisnesshourAtdate.getStartTime()) >= 0) {
//		       TOViewAppointment avil = new TOViewAppointment(date, buisnesshourAtdate.getStartTime(),
//		         holiday.getStartTime(), true);
//		       Avilitems.add(avil);
//		       isHoliday = true;
//		       continue;
//		      } else {
//		       TOViewAppointment item = new TOViewAppointment(date, buisnesshourAtdate.getStartTime(),
//		         buisnesshourAtdate.getEndTime(), false);
//		       Unavilitems.add(item);
//		       return Unavilitems;
//		      }
//		     }
//		     if (holiday.getEndDate().compareTo(date) == 0) {
//		      if (holiday.getEndTime().compareTo(buisnesshourAtdate.getEndTime()) <= 0) {
//		       TOViewAppointment avil = new TOViewAppointment(date, buisnesshourAtdate.getStartTime(),
//		         holiday.getStartTime(), true);
//		       Avilitems.add(avil);
//		       isHoliday = true;
//		       continue;
//		      } else {
//		       TOViewAppointment item = new TOViewAppointment(date, buisnesshourAtdate.getStartTime(),
//		         buisnesshourAtdate.getEndTime(), false);
//		       Unavilitems.add(item);
//		       return Unavilitems;
//		      }
//		     }
//		    }
//		    for (TimeSlot holiday : vacations) {
//		     if ((holiday.getEndDate().compareTo(date) > 0) && (holiday.getStartDate().compareTo(date) < 0)) {
//		      TOViewAppointment item = new TOViewAppointment(date, buisnesshourAtdate.getStartTime(),
//		        buisnesshourAtdate.getEndTime(), false);
//		      Unavilitems.add(item);
//		      return Unavilitems;
//		     }
//		     if (holiday.getStartDate().compareTo(date) == 0) {
//		      if (holiday.getStartTime().compareTo(buisnesshourAtdate.getStartTime()) >= 0) {
//		       TOViewAppointment avil = new TOViewAppointment(date, buisnesshourAtdate.getStartTime(),
//		         holiday.getStartTime(), true);
//		       Avilitems.add(avil);
//		       isHoliday = true;
//		       break;
//		      } else {
//		       TOViewAppointment item = new TOViewAppointment(date, buisnesshourAtdate.getStartTime(),
//		         buisnesshourAtdate.getEndTime(), false);
//		       Unavilitems.add(item);
//		       return Unavilitems;
//		      }
//		     }
//		     if (holiday.getEndDate().compareTo(date) == 0) {
//		      if (holiday.getEndTime().compareTo(buisnesshourAtdate.getEndTime()) <= 0) {
//		       TOViewAppointment avil = new TOViewAppointment(date, buisnesshourAtdate.getStartTime(),
//		         holiday.getStartTime(), true);
//		       Avilitems.add(avil);
//		       isHoliday = true;
//		       break;
//		      } else {
//		       TOViewAppointment item = new TOViewAppointment(date, buisnesshourAtdate.getStartTime(),
//		         buisnesshourAtdate.getEndTime(), false);
//		       Unavilitems.add(item);
//		       return Unavilitems;
//		      }
//		     }
//		    }
//		    if (!isHoliday) {
//		     TOViewAppointment item = new TOViewAppointment(date, buisnesshourAtdate.getStartTime(),
//		       buisnesshourAtdate.getEndTime(), true);
//		     Avilitems.add(item);
//		    }
//		   }
//
//		  }
//
//		   //Consider the appointment 
//		   
//		   for(Appointment appointment:Appointments) {
//		    TimeSlot Ts=appointment.getTimeSlot();
//		    
//		    if (Ts.getStartDate().equals(date)){
//		     AppointmentsAtDate.add(appointment);
//		    }
//		   }
//		   
//		   
//		   for(Appointment appointment:AppointmentsAtDate) {
//		    BookableService services=appointment.getBookableService();
//		    TimeSlot Ts=appointment.getTimeSlot();
//		    Time EndTime=Ts.getEndTime();
//		    Time StartTime=Ts.getStartTime();
//		    
//		    if (services instanceof Service) {
//		     Unavilitems=addAllkindService(date,StartTime,EndTime,(Service)services,Unavilitems);   
//		    }
//		    else {
//		     
//		     Time Start=StartTime;
//		     Time End;
//		     for(ComboItem item:appointment.getChosenItems()) {
//		      SimpleDateFormat sdf=new SimpleDateFormat("HH:mm:ss");
//		      Service service=item.getService();
//		      int duration =service.getDuration();
//		      Calendar cal = Calendar.getInstance();
//		      cal.setTime(Start);
//		      cal.add(Calendar.MINUTE, duration);
//		      java.util.Date da = cal.getTime();
//		      String dayAsstring=sdf.format(da);
//		      End=Time.valueOf(dayAsstring);
//		      
//		      Unavilitems=addAllkindService(date,Start,End,service,Unavilitems);
//		      
//		      Start=End;
//		     }
//		    }
//		   }
//		   
//		   
//		   //substract unavil from avil
//		   for (BusinessHour buisnesshourAtdate:buisnesshourcollection ) {
//		   for (TOViewAppointment unavil:Unavilitems) {
//		    if (unavil.getStarttime().equals(buisnesshourAtdate.getStartTime())){
//		     TOViewAppointment avil=new TOViewAppointment (date,unavil.getEndtime(),buisnesshourAtdate.getEndTime(),true);
//		     Avilitems.clear();
//		     Avilitems.add(avil);
//		    }
//		    if (unavil.getEndtime().equals(buisnesshourAtdate.getEndTime())){
//		     TOViewAppointment avil=new TOViewAppointment (date,buisnesshourAtdate.getStartTime(),unavil.getStarttime(),true);
//		     Avilitems.clear();
//		     Avilitems.add(avil);
//		    }
//		   }
//		   
//		   for (TOViewAppointment unavil:Unavilitems) {
//		    for (TOViewAppointment avil:Avilitems) {
//		     if ((avil.getStarttime().compareTo(unavil.getStarttime())<=0) & (avil.getEndtime().compareTo(unavil.getEndtime())>=0)) {
//		      TOViewAppointment avil1=new TOViewAppointment (date,avil.getStarttime(),unavil.getStarttime(),true);
//		      TOViewAppointment avil2=new TOViewAppointment (date,unavil.getEndtime(),avil.getEndtime(),true);
//		      Avilitems.remove(avil);
//		      Avilitems.add(avil1);
//		      Avilitems.add(avil2);
//		      break;
//		     }
//		    }
//		    
//		   }
//		   }
//
//		   Avilitems.addAll(Unavilitems);
//		   Allitems=Avilitems;
//		   
//		   
//		   return Allitems; 
//		  }
//
	/**@author lizhiwei
	 * @param dateString
	 * @return
	 * @throws InvalidInputException
	 */
	public static ArrayList<TOViewAppointment>  ViewAppointment_At_Week(String dateString) throws InvalidInputException {
		ArrayList<TOViewAppointment> Allitems = new ArrayList<TOViewAppointment>();
		ArrayList<TOViewAppointment> Additems = new ArrayList<TOViewAppointment>();
		
		Date date;
		try {
			date = Date.valueOf(dateString);
		} catch(java.lang.IllegalArgumentException e){
			throw new InvalidInputException(dateString+" is not a valid date");
		}
		for(int i=0;i<=6;i++) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.add(cal.DATE,i);
			java.util.Date da = cal.getTime();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			String current=format.format(da);
			
			Additems=(ArrayList<TOViewAppointment>) ViewAppointment_At_date(current);
			Allitems.addAll(Additems);
		}
		return Allitems;
		
	}
	
	/**
	 * Creates a new customer account with the provided username and password in the flexibook.
	 * @param username the username of the new customer account
	 * @param password the password of the new customer account
	 * @throws InvalidInputException if owner is logged-in
	 * @throws InvalidInputException if username is empty or null
	 * @throws InvalidInputException if password is empty or null
	 * @throws InvalidInputException if another account with the provided username exists
	 * @author Jack Wei
	 */
	public static void signUp(String username, String password) throws InvalidInputException {
		String error = "";
		User current = FlexiBookApplication.getCurrentUser();
		if (current != null) {
			if(FlexiBookApplication.getCurrentUser().getUsername().equals("owner")) {
				error = "You must log out of the owner account before creating a customer account";
			}
		}
		if(username.equals("owner") ){
			error = "You cannot sign up as a owner";
			throw new InvalidInputException(error.trim());
		}
		if(username == null || username.isEmpty()) {
			error = "The user name cannot be empty";
		} else if (password == null || password.isEmpty()){
			error = "The password cannot be empty";
		}
		if (error.length() > 0) {
			throw new InvalidInputException(error.trim());
		}
		FlexiBook flexibook = FlexiBookApplication.getFlexiBook();
		try {
			flexibook.addCustomer(username, password,0);
			FlexiBookPersistence.save(flexibook);
		} 
		catch (RuntimeException e) {
			error = e.getMessage();
			if(error.equals("Cannot create due to duplicate username. See http://manual.umple.org?RE003ViolationofUniqueness.html")){
				error = "The username already exists";
			}
			throw new InvalidInputException(error);
		}
	}
	
	/**
	 * Deletes the customer account and all associated appointments of the logged-in customer. 
	 * @param username - the username of the target customer account
	 * @throws InvalidInputException if "owner" is given as username
	 * @author Jack Wei
	 */
	public static void deleteAccount(String username) throws InvalidInputException {
		String error = "";
		Customer customer = findCustomer(username);
		User current = FlexiBookApplication.getCurrentUser();
		if (current != null) {
			if((!current.getUsername().equals(username)) || username.equals("owner")){
				error = "You do not have permission to delete this account";
			}
		}
		if (error.length() > 0) {
			throw new InvalidInputException(error.trim());
		}
		if (customer != null) {
			customer.delete();
			FlexiBookApplication.setCurrentUser(null);
		}
	}
	
	/**
	 * Updates the username and password of the logged-in user. 
	 * @param username - the username of the target account
	 * @param password - the password of the target account
	 * @throws InvalidInputException if owner 
	 * @author Jack Wei
	 */
	public static void updateAccount(String username, String password) throws InvalidInputException {
		String error = "";
		if(username == null || username.isEmpty()) {
			error = "The user name cannot be empty";
		} else if (password == null || password.isEmpty()){
			error = "The password cannot be empty";
		}
		if (error.length() > 0) {
			throw new InvalidInputException(error.trim());
		}
		if (FlexiBookApplication.getCurrentUser().getUsername().equals("owner")) {
			if(!username.equals("owner")) {
				error = "Changing username of owner is not allowed";
			}
		}
		if (error.length() > 0) {
			throw new InvalidInputException(error.trim());
		}
		for(Customer customer : FlexiBookApplication.getFlexiBook().getCustomers()) {
			if(customer.getUsername().equals(username)) {
				error = "Username not available";
			}
		}
		if (error.length() > 0) {
			throw new InvalidInputException(error.trim());
		}
		try {
			FlexiBookApplication.getCurrentUser().setUsername(username);
			FlexiBookApplication.getCurrentUser().setPassword(password);
			FlexiBookPersistence.save(FlexiBookApplication.getFlexiBook());

		} 
		catch (RuntimeException e) {
			error = e.getMessage();
			throw new InvalidInputException(error);
		}
	}
	
	/**
	 * Finds the customer in the flexibook by the given username.
	 * @param username username of the target customer
	 * @return foundCustomer 
	 * @author Jack Wei
	 */
	private static Customer findCustomer(String username) {
		Customer foundCustomer = null;
		for (Customer customer : FlexiBookApplication.getFlexiBook().getCustomers()) {
			if (customer.getUsername().equals(username)) {
				foundCustomer = customer;
				break;
			}
		}
		return foundCustomer;
	}
	/**
	 * define a ServiceCombo in the flexibook system
	 * @param username - the username of current user
	 * @param name - name of the define combo
	 * @param mainservice - name of the service that want to be set as the mainservice of the service combo
	 * @param service - a string of services,separate each service with ","
	 * @param mandatory - a string of mandatories, separate each mandatory with ","
	 * @throws InvalidInputException if input is invalid
	 * @author Jiatong Niu
	 */
	public static void defineServiceCombo (String username,String name, String mainservice,String service, String mandatory ) throws InvalidInputException {
		String error ="";
	     List<String> services = toList(service);
	     List<String> mandatories = toList(mandatory);
	
		FlexiBook flexi = FlexiBookApplication.getFlexiBook();
		
		
		
	    if (username.equals(flexi.getOwner().getUsername())==false) {
			error="You are not authorized to perform this operation";
		}
		
	    if (services.size()<2) {
	    	error="A service Combo must contain at least 2 services";
	    }
	    
	    if(!findMainService(services,mainservice)) {// if main service is not contained in the service list
	    	error="Main service must be included in the services";
	    }
	    
	    if(findServiceCombo(name)!=null) {
	    	error=" Service combo "+name+" already exists";
	    }
	
	   if(hasServices(services)!=null) {
	    	error="Service "+hasServices(services)+" does not exist";
	    }
	  
	  
	    
	    int k;
	    for(k=0;k<services.size();k++) {
	    	if (services.get(k).equals(mainservice)&&mandatories.get(k).equals("false")) {
	    		error="Main service must be mandatory";
	    	}
	    }
	    
	    
	    
	    
	    if (error.length() > 0) {
			throw new InvalidInputException(error.trim());
		}
	    
		ServiceCombo sv = new ServiceCombo(name,flexi);
		boolean man = true;// since we have check the main service is mandatory
		ComboItem aNewService= new ComboItem(man,toService(flexi,mainservice),sv);
		int i;
		for (i=0;i<services.size();i++) {
			if(services.get(i).equals(mainservice)) {
				//do not add
			}else {
			sv.addService(toBool(mandatories.get(i)),toService(flexi,services.get(i)));//we have add all the services and mandatories into sv
			}
		}
		

		
		// now we want to set main service for the combo
		sv.setMainService(aNewService);
		flexi.addBookableService(sv);// now we add the new defined servicecombo into flexi
		try {
			FlexiBookPersistence.save(flexi);
		} catch (RuntimeException e) {
			throw new InvalidInputException (e.getMessage());
		}
		
		
	}
	
	/**
	 * update a ServiceCombo in the flexibook system
	 * @param username - the username of current user
	 * @param oldname - the name of the servicecombo to be updated
	 * @param newname - newname for the update combo
	 * @param newmainservice - name of the service that want to be set as the newmainservice of the service combo
	 * @param newservice - a string of services that should be updated to combo,separate each service with ","
	 * @param newmandatory - a string of mandatories that should be updated to combo, separate each mandatory with ","
	 * @throws InvalidInputException if input is invalid
	 * @author Jiatong Niu
	 */
	 public static void updateServiceCombo (String username,String oldname,String newname, String newmainservice, String newservice, String newmandatory ) throws InvalidInputException {
		   List<String> newservices = toList(newservice);
		   List<String> newmandatories = toList(newmandatory);
		
		String error ="";
		
		FlexiBook flexi = FlexiBookApplication.getFlexiBook();
		
		if (username.equals(flexi.getOwner().getUsername())==false) {
			error="You are not authorized to perform this operation ";
		}
		
	    if (newservices.size()<2) {
	    	error=" A service Combo must contain at least 2 services ";
	    }
	    
	    if(!findMainService(newservices,newmainservice)) {// if main service is not contained in the service list
	    	error="Main service must be included in the services";
	    }
	    if(newservices.size()!=newmandatories.size()) {
	    	error="Numbers of services and mandatories do not match";
	    }
	    if(newname!=oldname&&findServiceCombo(newname)!=null) {// check if there already exists a service combo with the new name and this service combo is not the one we are updating
	    	error=" Service combo"+newname+" already exists";
	    }
	    if(hasServices(newservices)!=null) {
	    	error="Service "+hasServices(newservices)+" does not exist";
	    }
	    int k;
	    for(k=0;k<newservices.size();k++) {
	    	if (newservices.get(k).equals(newmainservice)&&newmandatories.get(k).equals("false")) {
	    		error="Main service must be mandatory";
	    	}
	    }
	    
	    if (error.length() > 0) {
			throw new InvalidInputException(error.trim());
		}
	    
	 
		
		for (int j=0;j<flexi.getBookableServices().size();j++) {
			if(flexi.getBookableService(j).getName().equals(oldname)&&(flexi.getBookableService(j) instanceof ServiceCombo) ) {
				
				((ServiceCombo)flexi.getBookableService(j)).delete();
				ServiceCombo sc = new ServiceCombo (newname,flexi);
				
				ComboItem aNewMainService=new ComboItem(true,toService(flexi,newmainservice),sc);
				((ServiceCombo)flexi.getBookableService(j)).setMainService(aNewMainService);
				
				
				for(int m=0;m<newservices.size();m++) {
					if(newservices.get(m).equals(newmainservice)) {
						// do nothing
					}else {
					 new ComboItem(toBool(newmandatories.get(m)),toService(flexi,newservices.get(m)),sc);
					}
				}
				flexi.addBookableServiceAt(sc, j);
				try {
					FlexiBookPersistence.save(flexi);
				} catch (RuntimeException e) {
					throw new InvalidInputException (e.getMessage());
				}
			
			}
		}
		
		
	}
	
		
		/**
		 * delete a ServiceCombo in the flexibook system
		 * @param username - the username of current user
		 * @param name - the name of the servicecombo that we want to delete
		 * @throws InvalidInputException if input is invalid
		 * @author Jiatong Niu
		 */
	
	public static void deleteServiceCombo (String username,String name) throws InvalidInputException {
		String error ="";
		Date currentDate = FlexiBookApplication.getCurrentDate();
		FlexiBook flexi = FlexiBookApplication.getFlexiBook();
		Time currentTime = FlexiBookApplication.getCurrentTime();
		if (username.equals(flexi.getOwner().getUsername())==false) {
			error="You are not authorized to perform this operation ";
		}
		
		if (findServiceCombo(name)==null) {
			error="the servicecombo does not exist, invalid name.";
		}
		
		for(Appointment appointment : flexi.getAppointments()) {
			Boolean isFutureAppoint = false;
			if(appointment.getTimeSlot().getEndDate().equals(currentDate)) {
				if(appointment.getTimeSlot().getEndTime().after(currentTime)) {
					isFutureAppoint = true;
				}
			}else if(appointment.getTimeSlot().getEndDate().after(currentDate)) {
				isFutureAppoint = true;
			}
			
			if(isFutureAppoint) {
				//if the appointment only contains one service and has the specific service
					error = "The service contains future appointments";
				
			}
		}
		
		if (error.length()>0) {
			throw new InvalidInputException(error.trim());
		}
		findServiceCombo(name).delete();
		try {
			FlexiBookPersistence.save(FlexiBookApplication.getFlexiBook());
		} catch (RuntimeException e) {
			throw new InvalidInputException (e.getMessage());
		}
	}


	/**
	 * Add a service to the Flexibook
	 * @param name name of the service
	 * @param duration
	 * @param downtimeDuration
	 * @param downtimeStart
	 * @throws InvalidInputException
	 * @author Cecilia Jiang
	 */
	public static void addService(String name, String duration_Str, String downtimeDuration_Str, String downtimeStart_Str) 
			throws InvalidInputException {
		String error = "";
		FlexiBook flexibook = FlexiBookApplication.getFlexiBook();
		User currentUser = FlexiBookApplication.getCurrentUser();
		int duration = 0;
		int downtimeDuration = 0;
		int downtimeStart = 0;
		
		try{
			duration = Integer.parseInt(duration_Str);
			downtimeDuration = Integer.parseInt(downtimeDuration_Str);
			downtimeStart = Integer.parseInt(downtimeStart_Str);
		} catch (NumberFormatException e) {
			throw new InvalidInputException("Invalid input");
		}
		
		if(name.equals("")) {
			error ="You must enter a service name";
			throw new InvalidInputException(error.trim());
		}
		
		if(currentUser instanceof Customer) {
			error = "You are not authorized to perform this operation";
			throw new InvalidInputException(error.trim());
		}
		
		for(BookableService service : flexibook.getBookableServices()) {
			if(service instanceof Service && name.equals(service.getName())) {
				error = "Service " + service.getName()+ " already exists";
				throw new InvalidInputException(error.trim());
			}
		}
		
		if(duration <= 0) {
			error = "Duration must be positive";
			throw new InvalidInputException(error.trim());
		}
		
		if(downtimeStart < 0) {
			error = "Downtime must not start before the beginning of the service";
		}else if(downtimeStart == 0) {
			if(downtimeDuration < 0) {
				error = "Downtime duration must be 0";
			}else if(downtimeDuration > 0) {
				error ="Downtime must not start at the beginning of the service";
			}
		}else if(downtimeStart > 0) {
			if(downtimeStart > duration) {
				error = "Downtime must not start after the end of the service";
			}else {			
				if(downtimeDuration > duration-downtimeStart) {
					error = "Downtime must not end after the service";
				}else if(downtimeDuration==0) {
					error = "Downtime duration must be positive";
				}
			}
		}
		
		if (error.length() > 0) {
			throw new InvalidInputException(error.trim());
		}
		
		try {
			Service aService = new Service(name, flexibook, duration, downtimeDuration, downtimeStart);
			flexibook.addBookableService(aService);
			FlexiBookPersistence.save(FlexiBookApplication.getFlexiBook());

		}catch (RuntimeException e) {
			throw new InvalidInputException(e.getMessage());
		}
		
	}
	

	/**
	 * update a service in the Flexibook by indicating the current name of the service
	 * @param curName the service that will be updated
	 * @param newName new name of the service
	 * @param duration
	 * @param downtimeDuration
	 * @param downtimeStart
	 * @throws InvalidInputException
	 * @author Cecilia Jiang
	 */
	public static void updateService(String curName, String newName, String duration_Str, String downtimeDuration_Str, String downtimeStart_Str) 
			throws InvalidInputException {
		String error = "";
		FlexiBook flexibook = FlexiBookApplication.getFlexiBook();
		User currentUser = FlexiBookApplication.getCurrentUser();
		int duration = 0;
		int downtimeDuration = 0;
		int downtimeStart = 0;
		
		try{
			duration = Integer.parseInt(duration_Str);
			downtimeDuration = Integer.parseInt(downtimeDuration_Str);
			downtimeStart = Integer.parseInt(downtimeStart_Str);
		} catch (NumberFormatException e) {
			throw new InvalidInputException("Invalid input");
		}
		
		if(curName.equals(null)) {
			error = "You must enter a service name";
			throw new InvalidInputException(error.trim());
		}
		
		if(newName.equals(null)) {
			error = "You must enter a new service name";
			throw new InvalidInputException(error.trim());
		}
		
		if(currentUser instanceof Customer) {
			error = "You are not authorized to perform this operation";
			throw new InvalidInputException(error.trim());
		}
		

		if(!curName.equals(newName)) {
			for(BookableService service: flexibook.getBookableServices()) {
				if(service instanceof Service) {
					if(service.getName().equals(newName)) {
						error = "Service " + newName + " already exists";
						throw new InvalidInputException(error.trim());
					}
				}
			}
		}else {
			for(BookableService service: flexibook.getBookableServices()) {
				if(service instanceof Service && service.getName().equals(newName) && ((Service) service).getDuration()==duration&&
						((Service) service).getDowntimeDuration()==downtimeDuration && ((Service) service).getDowntimeStart()==downtimeStart) {
					error = "Updated service must be different from the previous one";
					throw new InvalidInputException(error.trim());
				}
			}
		}
		
		if(duration <= 0) {
			error = "Duration must be positive";
		}
		if(downtimeStart < 0) {
			error = "Downtime must not start before the beginning of the service";
		}else if(downtimeStart == 0) {
			if(downtimeDuration < 0) {
				error = "Downtime duration must be 0";
			}else if(downtimeDuration > 0) {
				error ="Downtime must not start at the beginning of the service";
			}
		}else if(downtimeStart > 0) {
			if(downtimeStart > duration) {
				error = "Downtime must not start after the end of the service";
			}else {			
				if(downtimeDuration > duration-downtimeStart) {
					error = "Downtime must not end after the service";
				}else if(downtimeDuration==0) {
					error = "Downtime duration must be positive";
				}
			}
		}
		if (error.length() > 0) {
			throw new InvalidInputException(error.trim());
		}
		
		try {
			List<BookableService> bookableServices = flexibook.getBookableServices();
			for(BookableService service : bookableServices) {
				if(service instanceof Service && service.getName().equals(curName)) {
					service.setName(newName);
					((Service) service).setDuration(duration);
					((Service) service).setDowntimeDuration(downtimeDuration);
					((Service) service).setDowntimeStart(downtimeStart);
				}
				
			}
			FlexiBookPersistence.save(FlexiBookApplication.getFlexiBook());

		}catch (RuntimeException e) {
			throw new InvalidInputException(e.getMessage());
		}
	}
	
	/**
	 * find the Service with name in the system
	 * @param flexi - flexibook system
	 * @param name - the name of the service that we want
	 * @return (Service)s - service with name
	 * @author Jiatong Niu
	 */
	private static Service toService (FlexiBook flexi,String name) {
		BookableService s = null;
		for (BookableService j :flexi.getBookableServices()) {
			if (j.getName().equals(name)) {
				s=j;
				return (Service)s;
			}
		}
		return (Service)s;
	}
	

	/**
	 * delete a service in the Flexibook
	 * @param name name of the service that will be deleted
	 * @throws InvalidInputException
	 * @author Cecilia Jiang
	 */
	public static void deleteService(String name) throws InvalidInputException{
		String error = "";
		FlexiBook flexibook = FlexiBookApplication.getFlexiBook();
		User currentUser = FlexiBookApplication.getCurrentUser();
		Date currentDate = FlexiBookApplication.getCurrentDate();
		Time currentTime = FlexiBookApplication.getCurrentTime();
		List<Appointment> appointmentsToMove = new ArrayList<Appointment>();
		
		if(name.equals("")) {
			error ="You must enter a service name";
			throw new InvalidInputException(error.trim());
		}
		
		if(currentUser instanceof Customer) {
			error = "You are not authorized to perform this operation";
			throw new InvalidInputException(error.trim());
		}
		
		if(!BookableService.hasWithName(name)) {
			error = "\'" + name + "\'" + " does not exist in the system";
			throw new InvalidInputException(error.trim());
		}
		for(Appointment appointment : flexibook.getAppointments()) {
			Boolean isFutureAppoint = false;
			if(appointment.getTimeSlot().getEndDate().equals(currentDate)) {
				if(appointment.getTimeSlot().getEndTime().after(currentTime)) {
					isFutureAppoint = true;
				}
			}else if(appointment.getTimeSlot().getEndDate().after(currentDate)) {
				isFutureAppoint = true;
			}
			
			if(isFutureAppoint) {
				//if the appointment only contains one service and has the specific service
				if((appointment.getBookableService() instanceof Service) && (appointment.getBookableService().getName().equals(name))) {
					error = "The service contains future appointments";
					throw new InvalidInputException(error.trim());
				}else if(appointment.getBookableService() instanceof ServiceCombo){
					List<ComboItem> services = ((ServiceCombo) appointment.getBookableService()).getServices();
					for(ComboItem comboItem : services) {
						if(comboItem.getService().getName().equals(name)&&comboItem.isMandatory()) {
							error = "The service contains future appointments";
							throw new InvalidInputException(error.trim());
						}
					}
				}
							
			}else {
				if((appointment.getBookableService() instanceof Service) && (appointment.getBookableService().getName().equals(name))) {
					appointmentsToMove.add(appointment);
				}else if(appointment.getBookableService() instanceof ServiceCombo){
					List<ComboItem> services = ((ServiceCombo) appointment.getBookableService()).getServices();
					for(ComboItem comboItem : services) {
						if(comboItem.getService().getName().equals(name)) {
							appointmentsToMove.add(appointment);
						}
					}
				}
				try {
					FlexiBookPersistence.save(FlexiBookApplication.getFlexiBook());
				} catch (RuntimeException e) {
					throw new InvalidInputException (e.getMessage());
				}
			}

		}
	
		
		try {
			for(int i=0;i<FlexiBookApplication.getFlexiBook().getBookableServices().size();i++) {
				BookableService service=FlexiBookApplication.getFlexiBook().getBookableServices().get(i);
			    if(service instanceof ServiceCombo) {
			    	ServiceCombo aServiceCombo=(ServiceCombo) FlexiBookApplication.getFlexiBook().getBookableServices().get(i);
			    	if(aServiceCombo.getMainService().getService().getName().equals(name)) {
			    		aServiceCombo.delete();
			    		i--;
			    		}else {
			    			for(int j=0; j<aServiceCombo.getServices().size();j++) {
			    				ComboItem item=aServiceCombo.getService(j);
			    				if(item.getService().getName().equals(name)) {
			    					if(aServiceCombo.getServices().size()==2) {
			    						aServiceCombo.delete();
			    						i--;
			    					}else {
				    					item.delete();
				    					j--;
			    					}

			    					}
			    				}
			    			}
			    	
			    	}
			    if(name.equals(service.getName())) {
			    	service.delete();
			    	i--;
			    	}
			    }
			FlexiBookPersistence.save(FlexiBookApplication.getFlexiBook());
		}catch(RuntimeException e) {
			throw new InvalidInputException(e.getMessage());
		}


	}
	
	/**
	 * Set up basic business information in the flexibook with given input
	 * 
	 * @param username    username for the owner
	 * @param name        name of the business owner
	 * @param address     address of owner
	 * @param phoneNumber phone number of owner
	 * @param email       email of owner
	 * @throws InvalidInputException if input is invalid
	 * @author Grey Yuan
	 */
	public static void setUpBasicBusinessInfo(String username, String name, String address, String phoneNumber,
			String email) throws InvalidInputException {
		String error = "";
		if (!username.equals("owner")) {
			error = "No permission to set up business information";
		}
		if (error.length() > 0) {
			throw new InvalidInputException(error.trim());
		}
		if (!isValidEmailAddress(email)) {
			error = "Invalid email";

		}
		if (error.length() > 0) {
			throw new InvalidInputException(error.trim());
		}
		// add basic info
		try {

			Business newBusiness = new Business(name, address, phoneNumber, email, FlexiBookApplication.getFlexiBook());

			FlexiBookApplication.getFlexiBook().setBusiness(newBusiness);
			FlexiBookPersistence.save(FlexiBookApplication.getFlexiBook());
		} catch (RuntimeException e) {
			error = e.getMessage();
			throw new InvalidInputException(error);
		}
	}
	
	/**
	 * Set up basic business hours in the flexibook with given input
	 * 
	 * @param username     username for the owner
	 * @param dayOfWeek    day of the week
	 * @param newStartTime new start time of business hour
	 * @param newEndTime   new end time of business hour
	 * @throws InvalidInputException if input is invalid
	 * @author Grey Yuan
	 */
	public static void setUpBusinessHour(String username, DayOfWeek dayOfWeek, Time newStartTime, Time newEndTime)
			throws InvalidInputException {
		int counter = 0;
		String error = "";
		FlexiBook flexibook = FlexiBookApplication.getFlexiBook();
		List<BusinessHour> businessHours = flexibook.getBusiness().getBusinessHours();
		for (BusinessHour bH : businessHours) {

			if (bH.getDayOfWeek().toString().equals(dayOfWeek.toString())) {
				// end1 >= start2 and end2 >= start1
//				if (bH.getEndTime().after(newStartTime) || newEndTime.after(bH.getStartTime())) {
//					counter++;
//					
//				}
				if (compareTime(bH.getEndTime(),newStartTime)>0 && compareTime(newEndTime,bH.getStartTime())>0) {
				counter++;
				
			}
			}
		}
		if (!username.equals("owner")) {
			error = "No permission to update business information";
			throw new InvalidInputException(error.trim());
		}
		
		if (counter != 0) {
			error = "The business hours cannot overlap";
			throw new InvalidInputException(error.trim());
		}
		
		if (newStartTime.compareTo(newEndTime) >= 0) {
			error = "Start time must be before end time";
			throw new InvalidInputException(error.trim());
		}
				
		try {
			BusinessHour businessHour = flexibook.addHour(dayOfWeek,newStartTime,newEndTime);
			flexibook.getBusiness().addBusinessHour(businessHour);
			FlexiBookPersistence.save(FlexiBookApplication.getFlexiBook());
		} catch (RuntimeException e) {
			error = e.getMessage();
			throw new InvalidInputException(error);
		}
	}
	

	public static int compareTime(java.sql.Time time1, java.sql.Time time2) {
		LocalTime localtime1 = time1.toLocalTime();
		LocalTime localtime2 = time2.toLocalTime();
		return localtime1.compareTo(localtime2);
	}
	
	/**
	 * pass the info to let the user view the business info
	 * 
	 * @author Grey Yuan
	 */
	public static List<TOBusiness> getBasicBusinessInfo() {
		ArrayList<TOBusiness> businessList = new ArrayList<TOBusiness>();
		FlexiBook flexibook = FlexiBookApplication.getFlexiBook();
		Business business = flexibook.getBusiness();
		TOBusiness toBusiness = new TOBusiness(business.getName(), business.getAddress(), business.getPhoneNumber(),
					business.getEmail());
		businessList.add(toBusiness);
		return businessList;
		

	}

	/**
	 * set up a time slot for holiday or for vacation
	 * 
	 * @param username  username for the owner
	 * @param type      holiday or vacation
	 * @param startDate start date of time slot
	 * @param endDate   end date of time slot
	 * @param startTime start time of time slot
	 * @param endTime   end time of time slot
	 * @throws InvalidInputException
	 * @author Grey Yuan
	 */
	public static void setUpTimeSlot(String username, String type, Date startDate, Date endDate, Time startTime,
			Time endTime) throws InvalidInputException {
		String error = "";
		FlexiBook flexibook = FlexiBookApplication.getFlexiBook();
		LocalDateTime startDateTime = dateAndTime(startDate, startTime);
		LocalDateTime endDateTime = dateAndTime(endDate, endTime);
		List<TimeSlot> holidays = flexibook.getBusiness().getHolidays();
		List<TimeSlot> vacations = flexibook.getBusiness().getVacation();
		TimeSlot timeslot = new TimeSlot(startDate, startTime, endDate, endTime, flexibook);
		if (!username.equals("owner")) {
			error = "No permission to update business information";
			throw new InvalidInputException(error.trim());
		}
		
		if (type.equalsIgnoreCase("vacation")) {
			for (TimeSlot vacation : vacations) {
				if (isOverlap(vacation, timeslot)) {
					error = "Vacation times cannot overlap";
					throw new InvalidInputException(error.trim());
				}
			}
			for (TimeSlot holiday : holidays) {
				if (isOverlap(holiday, timeslot)) {
					error = "Holiday and vacation times cannot overlap";
					throw new InvalidInputException(error.trim());
				}
			}
			if (dateAndTime(FlexiBookApplication.getCurrentDate(),FlexiBookApplication.getCurrentTime()).isAfter(startDateTime)) {
				error = "Vacation cannot start in the past";
				throw new InvalidInputException(error.trim());
			}
		}
		if (type.equalsIgnoreCase("holiday")) {
			for (TimeSlot holiday : holidays) {
				if (isOverlap(holiday, timeslot)) {
					error = "Holiday times cannot overlap";
					throw new InvalidInputException(error.trim());
				}
			}
			for (TimeSlot vacation : vacations) {
				if (isOverlap(vacation, timeslot)) {
					error = "Holiday and vacation times cannot overlap";
					throw new InvalidInputException(error.trim());
				}
			}
			if (dateAndTime(FlexiBookApplication.getCurrentDate(),FlexiBookApplication.getCurrentTime()).isAfter(startDateTime)) {
				error = "Holiday cannot start in the past";
				throw new InvalidInputException(error.trim());
			}
		}

		if (endDateTime.isBefore(startDateTime)) {
			error = "Start time must be before end time";
			throw new InvalidInputException(error.trim());
		}


		try {
			if (type.equalsIgnoreCase("vacation")) {
				for (TimeSlot vacation : vacations) {
					vacation.setEndDate(endDate);
					vacation.setEndTime(endTime);
					vacation.setStartDate(startDate);
					vacation.setStartTime(startTime);
				}
			}
			if (type.equalsIgnoreCase("holiday")) {
				for (TimeSlot holiday : holidays) {
					holiday.setEndDate(endDate);
					holiday.setEndTime(endTime);
					holiday.setStartDate(startDate);
					holiday.setStartTime(startTime);
				}
			}
			FlexiBookPersistence.save(flexibook);
		} catch (RuntimeException e) {
			error = e.getMessage();
			throw new InvalidInputException(error);
		}
	}

	/**
	 * remove a business hour
	 * 
	 * @param username  for the owner
	 * @param dayOfWeek day of the week
	 * @param startTime start time of business hour
	 * @param endDate   endDate of time slot
	 * @throws InvalidInputException
	 * @author Grey Yuan
	 */
	public static void removeBusinessHour(String username, DayOfWeek dayOfWeek, Time startTime)
			throws InvalidInputException {
		String error = "";
		if (!username.equals("owner")) {
			error = "No permission to update business information";
		}
		if (error.length() > 0) {
			throw new InvalidInputException(error.trim());
		}
		FlexiBook flexibook = FlexiBookApplication.getFlexiBook();
		List<BusinessHour> businessHours = flexibook.getBusiness().getBusinessHours();
		if (businessHours.size() > 0) {
			for (BusinessHour bH : businessHours) {
				if (bH.getDayOfWeek().equals(dayOfWeek) && bH.getStartTime().equals(startTime)) {
					bH.delete();
					try {
						FlexiBookPersistence.save(FlexiBookApplication.getFlexiBook());
					} catch (RuntimeException e) {
						throw new InvalidInputException (e.getMessage());
					}
				}
			}
		}

	}

	/**
	 * remove a business hour
	 * 
	 * @param username     for the owner
	 * @param dayOfWeek    day of the week
	 * @param startTime    start time of business hour
	 * @param newDayOfWeek new day of week for the new business hour
	 * @param newStartTime new start time
	 * @param newEndTime   new end time
	 * @throws InvalidInputException
	 * @author Grey Yuan
	 */
	public static void updateBusinessHour(String username, DayOfWeek dayOfWeek, Time startTime, DayOfWeek newDayOfWeek,
			Time newStartTime, Time newEndTime) throws InvalidInputException {
		removeBusinessHour(username, dayOfWeek, startTime);
		setUpBusinessHour(username, newDayOfWeek, newStartTime, newEndTime);
	}

	/**
	 * update a vacation
	 * 
	 * @param username     for the owner
	 * @param startDate    start date of old vacation
	 * @param startTime    start time of old vacation
	 * @param newStartDate new start date
	 * @param newStartTime new start time
	 * @param newEndDate   new end date
	 * @param newEndTime   new end time
	 * @throws InvalidInputException
	 * @author Grey Yuan
	 */
	public static void updateVacation(String username, Date startDate, Time startTime, Date newStartDate,
			Time newStartTime, Date newEndDate, Time newEndTime) throws InvalidInputException {
		String error = "";
		if (!username.equals("owner")) {
			error = "No permission to update business information";
			throw new InvalidInputException(error.trim());
		}
		FlexiBook flexibook = FlexiBookApplication.getFlexiBook();

		List<TimeSlot> vacations = flexibook.getBusiness().getVacation();
		if (vacations.size() > 0) {
			for (TimeSlot vacation : vacations) {
				if (vacation.getStartDate().equals(startDate) && vacation.getStartTime().equals(startTime)) {
					vacation.delete();
					try {
						FlexiBookPersistence.save(FlexiBookApplication.getFlexiBook());
					} catch (RuntimeException e) {
						throw new InvalidInputException (e.getMessage());
					}
				}
			}
		}

		setUpTimeSlot(username, "vacation", newStartDate, newEndDate, newStartTime, newEndTime);
	}

	/**
	 * update a holiday
	 * 
	 * @param username     for the owner
	 * @param startDate    start date of old holiday
	 * @param startTime    start time of old holiday
	 * @param newStartDate new start date
	 * @param newStartTime new start time
	 * @param newEndDate   new end date
	 * @param newEndTime   new end time
	 * @throws InvalidInputException
	 * @author Grey Yuan
	 */
	public static void updateHoliday(String username, Date startDate, Time startTime, Date newStartDate,
			Time newStartTime, Date newEndDate, Time newEndTime) throws InvalidInputException {
		String error = "";
		if (!username.equals("owner")) {
			error = "No permission to update business information";
			throw new InvalidInputException(error.trim());
		}
		FlexiBook flexibook = FlexiBookApplication.getFlexiBook();
		List<TimeSlot> holidays = flexibook.getBusiness().getHolidays();
		if (holidays.size() > 0) {
			for (TimeSlot holiday : holidays) {
				if (holiday.getStartDate().equals(startDate) && holiday.getStartTime().equals(startTime)) {
					holiday.delete();
					try {
						FlexiBookPersistence.save(FlexiBookApplication.getFlexiBook());
					} catch (RuntimeException e) {
						throw new InvalidInputException (e.getMessage());
					}
				}
			}
		}
		setUpTimeSlot(username, "holiday", newStartDate, newEndDate, newStartTime, newEndTime);
	}

	/**
	 * remove a time slot from holiday or vacation
	 * 
	 * @param username  for the owner
	 * @param type      holiday or vacation
	 * @param startDate start date to remove
	 * @param endDate   end date to remove
	 * @param startTime start time to remove
	 * @param endTime   end time to remove
	 * @throws InvalidInputException
	 * @author Grey Yuan
	 */
	public static void removeTimeSlot(String username, String type, Date startDate, Date endDate, Time startTime,
			Time endTime) throws InvalidInputException {
		String error = "";
		if (!username.equals("owner")) {
			error = "No permission to update business information";
		}
		if (error.length() > 0) {
			throw new InvalidInputException(error.trim());
		}
		FlexiBook flexibook = FlexiBookApplication.getFlexiBook();
		List<TimeSlot> holidays = flexibook.getBusiness().getHolidays();
		List<TimeSlot> vacations = flexibook.getBusiness().getVacation();
		if (vacations.size() > 0) {
			if (type.equalsIgnoreCase("vacation")) {
				for (TimeSlot vacation : vacations) {
					if (vacation.getStartDate().equals(startDate) && vacation.getStartTime().equals(startTime)
							&& vacation.getEndDate().equals(endDate) && vacation.getEndTime().equals(endTime)) {
						vacation.delete();
						try {
							FlexiBookPersistence.save(FlexiBookApplication.getFlexiBook());
						} catch (RuntimeException e) {
							throw new InvalidInputException (e.getMessage());
						}
					}
				}
			}
		}
		if (holidays.size() > 0) {
			if (type.equalsIgnoreCase("holiday")) {
				for (TimeSlot holiday : holidays) {
					if (holiday.getStartDate().equals(startDate) && holiday.getStartTime().equals(startTime)
							&& holiday.getEndDate().equals(endDate) && holiday.getEndTime().equals(endTime)) {
						holiday.delete();
						try {
							FlexiBookPersistence.save(FlexiBookApplication.getFlexiBook());
						} catch (RuntimeException e) {
							throw new InvalidInputException (e.getMessage());
						}
					}
				}
			}
		}
	}

	
	
	/**
	 * @param serviceName
	 * @param date
	 * @param startTime
	 * @param OptServices
	 * @throws InvalidInputException
	 * @author lizhiwei
	 */
	public static void makeAppointment(String serviceName, Date date, Time startTime,String OptServices) throws InvalidInputException{
		 String error = "";
		   
		   FlexiBook flexibook = FlexiBookApplication.getFlexiBook();
		   User currentUser = FlexiBookApplication.getCurrentUser();
		   int totalDuration=0;
		   if (currentUser.getUsername().equals("owner")) {
		    error += "An owner cannot make an appointment.";
		   throw new InvalidInputException(error.trim());
		   }
		   
		   
		   if (serviceName == null || date == null || startTime == null || !Service.hasWithName(serviceName)) {
		     error += "The parameters cannot be empty.";
		     throw new InvalidInputException(error);
		   }
		   
		   String[] optComboItems = null;
		    ServiceCombo aServiceCombo = (ServiceCombo) ServiceCombo.getWithName(serviceName);
		    List<ComboItem> comboItems = aServiceCombo.getServices();
		    if(OptServices!=null) {
		     optComboItems = OptServices.split(",");
		    }
		    List<ComboItem> chosenitems = new ArrayList<ComboItem>();
		    for(ComboItem ci:comboItems) {
		    	if(ci.getService().getName().equals(aServiceCombo.getMainService().getService().getName())) {
			         chosenitems.add(((ServiceCombo)findServiceCombo(serviceName)).getMainService());
			         totalDuration+=ci.getService().getDuration();
			        }
		       for(int j=0;j<optComboItems.length;j++) {
		        if(ci.getService().getName().equals(optComboItems[j])) {
		         chosenitems.add(ci);
		         totalDuration+=ci.getService().getDuration();
		        }
		       }
		    }
		   
		   ArrayList<TOViewAppointment> Avilitems = new ArrayList<TOViewAppointment>();
		   ArrayList<TOViewAppointment> Allitems =ViewAppointment_At_date(date.toString());
		   for (TOViewAppointment item:Allitems) {
		    if (item.getIsAvailable()) {
		     Avilitems.add(item);
		    	}
		   	}
		   if (Avilitems.size()==0 ) {
			   String stringStartTime = startTime.toString();
				if(stringStartTime.substring(0, 1).equals("0")) {
					stringStartTime = startTime.toString().substring(1, 5);
				}
				else {
					stringStartTime = startTime.toString().substring(0, 5);
				}
				error+="There are no available slots for "+ serviceName+ " on " + date.toString()+" at "+stringStartTime;
				 throw new InvalidInputException(error);
		   }
		
		Time Start=startTime;
		Time End;
		boolean isavil=true;
		for(ComboItem item:chosenitems) {
			SimpleDateFormat sdf=new SimpleDateFormat("HH:mm:ss");
			Service service=item.getService();
			int duration =service.getDuration();
			Calendar cal = Calendar.getInstance();
			cal.setTime(Start);
			cal.add(Calendar.MINUTE, duration);
			java.util.Date da = cal.getTime();
			String dayAsstring=sdf.format(da);
			End=Time.valueOf(dayAsstring);
			
			if (service.getDowntimeDuration()==0 || service.getDuration()==service.getDowntimeStart()+service.getDowntimeDuration() ) {
				if(!isAbailablewithoutDuration(Start, service, date, flexibook, Avilitems)) {
					isavil=false;
					break;
				}
			}
			else {
				if(!isAbailablewithDuration(Start, service, date, flexibook, Avilitems)) {
					isavil=false;
					break;
				}
			}
			Start=End;
		}
		
		if (isavil && !(FlexiBookApplication.getCurrentDate().compareTo(date)>0)) {
			SimpleDateFormat sdf=new SimpleDateFormat("HH:mm:ss");
				Calendar cal3 = Calendar.getInstance();
				cal3.setTime(startTime);
				cal3.add(Calendar.MINUTE, totalDuration);
				java.util.Date da3 = cal3.getTime();
				String dayAsstring3=sdf.format(da3);
				Time endtime=Time.valueOf(dayAsstring3);
				TimeSlot ts3=new TimeSlot(date,startTime,date,endtime,flexibook); 
			  
			  
			  Appointment aAppointment=new Appointment((Customer)currentUser,aServiceCombo,ts3,flexibook);
			  for (ComboItem aChosenItem: chosenitems ) {
			  aAppointment.addChosenItem(aChosenItem);
			  }
			  
			  flexibook.addAppointment(aAppointment);
			  try {
					FlexiBookPersistence.save(FlexiBookApplication.getFlexiBook());
				} catch (RuntimeException e) {
					throw new InvalidInputException (e.getMessage());
				}
		}
		else {
			
			String stringStartTime = startTime.toString();
			if(stringStartTime.substring(0, 1).equals("0")) {
				stringStartTime = startTime.toString().substring(1, 5);
			}
			else {
				stringStartTime = startTime.toString().substring(0, 5);
			}
			error+="There are no available slots for "+ serviceName+ " on " + date.toString()+" at "+stringStartTime;
			 throw new InvalidInputException(error);
		}
	
	}
	
	/**
	 * @param startTime
	 * @param service
	 * @param date
	 * @param flexibook
	 * @param Avilitems
	 * @return
	 */
	private static boolean isAbailablewithoutDuration(Time startTime, Service service, Date date,FlexiBook flexibook,ArrayList<TOViewAppointment>Avilitems) {
		Time Start=startTime;
		SimpleDateFormat sdf=new SimpleDateFormat("HH:mm:ss");
		
		int duration =service.getDuration()-service.getDowntimeDuration();
		Calendar cal = Calendar.getInstance();
		cal.setTime(Start);
		cal.add(Calendar.MINUTE, duration);
		java.util.Date da = cal.getTime();
		String dayAsstring=sdf.format(da);
		Time End=Time.valueOf(dayAsstring);
		TimeSlot ts=new TimeSlot(date,Start,date,End,flexibook); 
		boolean isAvilable=false;
		  for (TOViewAppointment item:Avilitems) {
		   if ((item.getStarttime().compareTo(Start)<=0) & (item.getEndtime().compareTo(End)>=0)) {
		   isAvilable = true;
		   }
		  }
		return isAvilable;
	}
	
	/**
	 * @param startTime
	 * @param service
	 * @param date
	 * @param flexibook
	 * @param Avilitems
	 * @author lizhiwei
	 * @return boolean
	 */
	private static boolean isAbailablewithDuration(Time startTime, Service service, Date date,FlexiBook flexibook,ArrayList<TOViewAppointment>Avilitems) {
		Time Start=startTime;
		   SimpleDateFormat sdf=new SimpleDateFormat("HH:mm:ss");
			
			int firstduration =service.getDowntimeStart();
			Calendar cal = Calendar.getInstance();
			cal.setTime(Start);
			cal.add(Calendar.MINUTE, firstduration);
			java.util.Date da = cal.getTime();
			String dayAsstring=sdf.format(da);
			Time End=Time.valueOf(dayAsstring);
			TimeSlot ts=new TimeSlot(date,Start,date,End,flexibook); 
			
			
			Calendar cal1 = Calendar.getInstance();
			cal1.setTime(End);
			cal1.add(Calendar.MINUTE,service.getDowntimeDuration());
			java.util.Date da1 = cal1.getTime();
			String dayAsstring1=sdf.format(da1);
			Start=Time.valueOf(dayAsstring1);
			
			Calendar cal2 = Calendar.getInstance();
			cal2.setTime(startTime);
			cal2.add(Calendar.MINUTE,service.getDuration() );
			java.util.Date da2 = cal2.getTime();
			String dayAsstring2=sdf.format(da2);
			End=Time.valueOf(dayAsstring2);
			TimeSlot ts1=new TimeSlot(date,Start,date,End,flexibook); 
			
			
			int isAvilable=0;
			  for (TOViewAppointment item:Avilitems) {
			   if ((item.getStarttime().compareTo(ts.getStartTime())<=0) & (item.getEndtime().compareTo(ts.getEndTime())>=0)) {
			   isAvilable++;
			   break;
			   }
			  }
			  for (TOViewAppointment item:Avilitems) {
				   if ((item.getStarttime().compareTo(ts1.getStartTime())<=0) & (item.getEndtime().compareTo(ts1.getEndTime())>=0)) {
				   isAvilable++;
				   break;
				   }
				  }
			  return (isAvilable==2);
	}
	 
	/**
	 * @param serviceName
	 * @param date
	 * @param startTime
	 * @throws InvalidInputException
	 * @author lizhiwei
	 */
	public static void makeAppointment(String serviceName, Date date, Time startTime) throws InvalidInputException {
	   String error = "";
	   
	   FlexiBook flexibook = FlexiBookApplication.getFlexiBook();
	   User currentUser = FlexiBookApplication.getCurrentUser();
	   Service service=(Service) Service.getWithName(serviceName);
	   if (currentUser.getUsername().equals("owner")) {
	    error += "An owner cannot make an appointment.";
	   throw new InvalidInputException(error.trim());
	   }
	   
	   
	   if (serviceName == null || date == null || startTime == null || !Service.hasWithName(serviceName)) {
	     error += "The parameters cannot be empty.";
	     throw new InvalidInputException(error);
	   }
	   
	   ArrayList<TOViewAppointment> Avilitems = new ArrayList<TOViewAppointment>();
	   ArrayList<TOViewAppointment> Allitems =ViewAppointment_At_date(date.toString());
	   for (TOViewAppointment item:Allitems) {
	    if (item.getIsAvailable()) {
	     Avilitems.add(item);
	    	}
	   	}
	   if (Avilitems.size()==0) {
		   String stringStartTime = startTime.toString();
			if(stringStartTime.substring(0, 1).equals("0")) {
				stringStartTime = startTime.toString().substring(1, 5);
			}
			else {
				stringStartTime = startTime.toString().substring(0, 5);
			}
			error+="There are no available slots for "+ serviceName+ " on " + date.toString()+" at "+stringStartTime;
			 throw new InvalidInputException(error);
	   }
	   	
	   if (service.getDowntimeDuration()==0 || service.getDuration()==service.getDowntimeStart()+service.getDowntimeDuration()) {
		   Boolean isAvilable=isAbailablewithoutDuration(startTime, service, date, flexibook, Avilitems);
		   if (!isAvilable|| FlexiBookApplication.getCurrentDate().compareTo(date)>0) {
				  String stringStartTime = startTime.toString();
					if(stringStartTime.substring(0, 1).equals("0")) {
						stringStartTime = startTime.toString().substring(1, 5);
					}
					else {
						stringStartTime = startTime.toString().substring(0, 5);
					}
					error+="There are no available slots for "+ serviceName+ " on " + date.toString()+" at "+stringStartTime;
					 throw new InvalidInputException(error);
			  }
		  Time Start=startTime;
			SimpleDateFormat sdf=new SimpleDateFormat("HH:mm:ss");
			
			int duration =service.getDuration()-service.getDowntimeDuration();
			Calendar cal = Calendar.getInstance();
			cal.setTime(Start);
			cal.add(Calendar.MINUTE, duration);
			java.util.Date da = cal.getTime();
			String dayAsstring=sdf.format(da);
			Time End=Time.valueOf(dayAsstring);
			TimeSlot ts=new TimeSlot(date,Start,date,End,flexibook); 
			
		   Appointment aAppointment=new Appointment((Customer)currentUser,service,ts,flexibook);
		   flexibook.addAppointment(aAppointment);
	   }
	   else {
		      boolean isAvilable=isAbailablewithDuration(startTime, service, date, flexibook, Avilitems);
			  if (!isAvilable|| FlexiBookApplication.getCurrentDate().compareTo(date)>0) {
				  String stringStartTime = startTime.toString();
					if(stringStartTime.substring(0, 1).equals("0")) {
						stringStartTime = startTime.toString().substring(1, 5);
					}
					else {
						stringStartTime = startTime.toString().substring(0, 5);
					}
					error+="There are no available slots for "+ serviceName+ " on " + date.toString()+" at "+stringStartTime;
					 throw new InvalidInputException(error);
			  }
			  SimpleDateFormat sdf=new SimpleDateFormat("HH:mm:ss");
			  int duration =service.getDuration();
				Calendar cal3 = Calendar.getInstance();
				cal3.setTime(startTime);
				cal3.add(Calendar.MINUTE, duration);
				java.util.Date da3 = cal3.getTime();
				String dayAsstring3=sdf.format(da3);
				Time endtime=Time.valueOf(dayAsstring3);
				TimeSlot ts3=new TimeSlot(date,startTime,date,endtime,flexibook); 
			  
			  
			  Appointment aAppointment=new Appointment((Customer)currentUser,service,ts3,flexibook);
			  flexibook.addAppointment(aAppointment);
	   }
	   try {
			FlexiBookPersistence.save(FlexiBookApplication.getFlexiBook());
		} catch (RuntimeException e) {
			throw new InvalidInputException (e.getMessage());
		}
	  
	  
   
	   }

	  
 	/**
 	 * @param appointmentname
 	 * @param appointmentdate
 	 * @param appointmenttime
 	 * @param startdate
 	 * @param startTime
 	 * @return
 	 * @throws InvalidInputException
 	 * @author lizhiwei
 	 * 
 	 */
 	public static TOAppointment updateAppointment(String appointmentname,Date appointmentdate,Time appointmenttime, Date startdate, Time startTime) throws InvalidInputException {
 		String error = "";

 		FlexiBook flexibook = FlexiBookApplication.getFlexiBook();
 		User currentUser = FlexiBookApplication.getCurrentUser();
 		Appointment chosenAppointment;
 		int duration = 0;
 		
 		boolean result;

 		if (appointmentname == null || appointmentdate == null ||  appointmenttime == null || startdate == null ||startTime==null) {
 				error += "The parameters cannot be empty.";
 				throw new InvalidInputException(error);
 		}
 		
 		if (currentUser.getUsername().equals("owner")) {
 		    error += "Error: An owner cannot update a customer's appointment";
 		   throw new InvalidInputException(error.trim());
 		}
	
 		chosenAppointment=findAppointment(appointmentname,appointmentdate,appointmenttime);
 		if(!chosenAppointment.getCustomer().getUsername().equals(FlexiBookApplication.getCurrentUser().getUsername())) {
		     error = "Error: A customer can only update their own appointments";
		     throw new InvalidInputException(error.trim());
		}
 		
 		
 		if (Service.getWithName(appointmentname) instanceof Service) {
 			Service service=(Service)(chosenAppointment.getBookableService());
 			duration=(service.getDuration());
 			try {
 				cancelAppointment(appointmentname,appointmentdate,appointmenttime);
 	 			makeAppointment(service.getName(), startdate, startTime);
 	 		}catch(InvalidInputException e) {
 	 			makeAppointment(appointmentname,appointmentdate,appointmenttime);
 	 			result=false;
 	 			Time endtime=addXminute(appointmenttime,duration);
 	 			TOAppointment app= new TOAppointment(result, startdate, appointmenttime, endtime);
 	 			return app;
 	 		}
 			
 			result=true;
	 		Time endtime=addXminute(startTime,duration);
	 		TOAppointment app= new TOAppointment(result, startdate, startTime, endtime);
	 		return app;
 			
 		}
 		else {
 			String optService="";
 			ServiceCombo combo=(ServiceCombo)chosenAppointment.getBookableService();
 			for (ComboItem item:(chosenAppointment.getChosenItems())) {
 				
 			if (!item.getService().getName().equals(combo.getMainService().getService().getName())) {
 				optService+=","+item.getService().getName();
 			}
 			duration+=item.getService().getDuration();
 			}
 			
 			optService=optService.substring(1);
 			try {
 				cancelAppointment(appointmentname,appointmentdate,appointmenttime);
 	 			makeAppointment(combo.getMainService().getService().getName(), startdate, startTime, optService);
 	 		}catch(InvalidInputException e) {
 	 			makeAppointment(appointmentname,appointmentdate,appointmenttime);
 	 			result=false;
 	 			Time endtime=addXminute(appointmenttime,duration);
 	 			TOAppointment app= new TOAppointment(result, startdate, appointmenttime, endtime);
 	 			return app;
 	 		}
 			
 			result=true;
	 		Time endtime=addXminute(startTime,duration);
	 		TOAppointment app= new TOAppointment(result, startdate, startTime, endtime);
	 		return app;		
 		}
 		
 		
 		   

// 		try {
// 			for (Appointment a : appointments) {
// 				if (a.getTimeSlot().getStartDate() == date && a.getTimeSlot().getStartTime() == startTime) {
//
// 					if (a.getCustomer() != currentUser){
// 						error += "A customer can only update their own appointments.";
// 						throw new InvalidInputException(error.trim());
// 					}
//
// 					else {
// 						if(action == "add") {
// 							for (ComboItem citem : a.getChosenItems()) {
// 								if(citem.getService().getName() == item) {
// 									a.getChosenItems().add(citem);
// 								}
// 							}
// 						}
// 						else if(action == "delete") {
// 							for (ComboItem citem : a.getChosenItems()) {
// 								if(citem.getService().getName() == item) {
// 									a.getChosenItems().remove(citem);
// 								}
// 							}
// 						}
// 					}
// 				}
// 			}
// 		}
// 		catch (RuntimeException e) {
// 			error = e.getMessage();
// 			throw new InvalidInputException(error);
// 		}
 		}
 	
 	/**
 	 * @param action
 	 * @param comboitem
 	 * @param appointmentname
 	 * @param appointmentdate
 	 * @param appointmenttime
 	 * @return TOAppointment
 	 * @throws InvalidInputException
 	 * @author lizhiwei
 	 */
 	public static TOAppointment updateAppointment(String action,String comboitem,String appointmentname, Date appointmentdate, Time appointmenttime) throws InvalidInputException {
 		Appointment chosenAppointment;
 		chosenAppointment=findComboAppointment(appointmentname,appointmentdate,appointmenttime);
 		int duration=0;
 		boolean result;
 		User currentUser = FlexiBookApplication.getCurrentUser();
 		ServiceCombo combo=(ServiceCombo)chosenAppointment.getBookableService();
 		TOAppointment app;
 		if(currentUser.getUsername().equals("owner") ) {
 		  String error = "Error: An owner cannot update an appointment";
 		   throw new InvalidInputException(error.trim());
 		  }
 		if(!chosenAppointment.getCustomer().getUsername().equals(FlexiBookApplication.getCurrentUser().getUsername())) {
 		      String error = "Error: A customer can only update their own appointments";
 		      throw new InvalidInputException(error.trim());
 		}
 		
 		for (ComboItem item:(chosenAppointment.getChosenItems())) {
 			duration+=item.getService().getDuration();
 		}
 		if (action.equals("remove")) {
 			Service removeitem=(Service) Service.getWithName(comboitem);
 			boolean isMandantory = false;
 			for (ComboItem item:combo.getServices()) {
 				if (item.getService().getName().equals(comboitem)) {
 					isMandantory=item.getMandatory();
 				}
 			}
 			if (isMandantory) {
 				result=false;
 	 			Time endtime=addXminute(appointmenttime,duration);
 	 			app= new TOAppointment(result, appointmentdate, appointmenttime, endtime);
 	 			return app;
 			}
 			else {
 				String optService="";
 				duration=duration-removeitem.getDuration();
 		 		for (ComboItem item:(chosenAppointment.getChosenItems())) {
 	 			if (!item.getService().getName().equals(combo.getMainService().getService().getName())) {
 	 				if (!item.getService().getName().equals(comboitem)) {optService+=","+item.getService().getName(); }	 				
 	 				}
 		 		}
 	 			if (optService.length()!=0) {optService=optService.substring(1);}
 	 			cancelAppointment(appointmentname, appointmentdate, appointmenttime);
 	 			makeAppointment(appointmentname, appointmentdate, appointmenttime, optService);
 	 			result=true;
 	 			Time endtime=addXminute(appointmenttime,duration);
 	 			app= new TOAppointment(result, appointmentdate, appointmenttime, endtime);
 	 			return app;
 			}
 		}
 		else {
 			Service additem=(Service) Service.getWithName(comboitem);
 			String optService="";
 			String newoptService="";
 			try {
 		 		for (ComboItem item:(chosenAppointment.getChosenItems())) {
 					
 	 			if (!item.getService().getName().equals(combo.getMainService().getService().getName())) {
 	 				optService+=","+item.getService().getName();
 	 			}
 		 		}
 		 		if (optService.length()!=0) {optService=optService.substring(1);}
 		 		duration=duration+additem.getDuration();
 		 		
 				if (optService.length()!=0) {
 					newoptService=optService+","+comboitem;
 				}else {
 					newoptService=comboitem;
 				}
 				cancelAppointment(appointmentname, appointmentdate, appointmenttime);
 				makeAppointment(appointmentname, appointmentdate, appointmenttime, newoptService);
 			}catch(InvalidInputException e) {
 				makeAppointment(appointmentname, appointmentdate, appointmenttime, optService);
 				result=false;
 	 			Time endtime=addXminute(appointmenttime,duration);
 	 			app= new TOAppointment(result, appointmentdate, appointmenttime, endtime);
 	 			return app;
 			}
 			result=true;
	 		Time endtime=addXminute(appointmenttime,duration);
	 		app= new TOAppointment(result, appointmentdate, appointmenttime, endtime);
	 		return app;
 		
 		}
 		
 	}
 	
 	/**
 	 * @param serviceName
 	 * @param startDate
 	 * @param startTime
 	 * @author lizhiwei
 	 * @return
 	 */
 	private static Appointment findComboAppointment(String serviceName, Date startDate, Time startTime) {
 		  List<Appointment> appointments = FlexiBookApplication.getFlexiBook().getAppointments();
 		  Appointment aAppointment = null;
 		  for(Appointment a:appointments) {
 		   if(a.getBookableService().getName().equals(serviceName)&&a.getTimeSlot().getStartDate().equals(startDate)
 		             &&a.getTimeSlot().getStartTime().equals(startTime)) {
 		    aAppointment = a;
 		   }
 		  }
 		  return aAppointment;
 		 }


	/**
	 * @param startTime
	 * @param duration
	 * @author lizhiwei
	 * @return
	 */
	private static Time addXminute(Time startTime, int duration) {
 		SimpleDateFormat sdf=new SimpleDateFormat("HH:mm:ss");
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(startTime);
		cal1.add(Calendar.MINUTE, duration);
		java.util.Date da1 = cal1.getTime();
		String dayAsstring1=sdf.format(da1);
		Time endtime=Time.valueOf(dayAsstring1);
		return endtime;
	}


	/**
	 * @param appointmentname
	 * @param appointmentdate
	 * @param appointmenttime
	 * @author lizhiwei
	 * @return
	 */
	private static Appointment findAppointment(String appointmentname, Date appointmentdate, Time appointmenttime) {
 		List<Appointment> appointments =Service.getWithName(appointmentname).getAppointments();
 		Appointment findAppointment = null;
 		for (Appointment aAppointment: appointments) {
 			if (aAppointment.getTimeSlot().getStartTime().equals(appointmenttime)&&aAppointment.getTimeSlot().getStartDate().equals(appointmentdate)) {
 				findAppointment=aAppointment;
 				break;
 			}
 		}
		return findAppointment;
	}



	/**
	 * @param serviceName
	 * @param date
	 * @param startTime
	 * @throws InvalidInputException
	 * @author Cecilia Jiang
	 */
	public static void cancelAppointment(String serviceName, Date date, Time startTime) throws InvalidInputException {
		String error = "";
		FlexiBook flexibook = FlexiBookApplication.getFlexiBook();
		User currentUser = FlexiBookApplication.getCurrentUser();
		List<Appointment> appointments = flexibook.getAppointments();
		 Date curDate = FlexiBookApplication.getCurrentDate();
		 Time curTime = FlexiBookApplication.getCurrentTime();
//		Date curDate = Date.valueOf("2020-11-26");
//		Time curTime = Time.valueOf("22:00:00");
		if (currentUser instanceof Owner) {
			error += "An owner cannot cancel an appointment";
			throw new InvalidInputException(error.trim());
		}
		try {
			for (Appointment a : appointments) {

				if (a.getBookableService().getName().equals(serviceName) && a.getTimeSlot().getStartDate().equals(date)
						&& a.getTimeSlot().getStartTime().equals(startTime)) {
					if (a.getCustomer().getUsername().equals(currentUser.getUsername())) {
						if (curDate.after(date) || curDate.equals(date)) {
							error = "Cannot cancel an appointment on the appointment date";
							throw new InvalidInputException(error.trim());
						}
					} else {
						error = "A customer can only cancel their own appointments";
						throw new InvalidInputException(error.trim());
					}

					a.delete();
					FlexiBookPersistence.save(flexibook);
					break;

				}
			}
		} catch (InvalidInputException e) {
			error = e.getMessage();
			throw new InvalidInputException(error);
		}
	}



	
	/**
	 * return boolean of mandatory string
	 * @param s - the string of mandatory
	 * @return boolean
	 * @author Jiatong Niu
	 */
	private static Boolean toBool (String s) {
		if(s.equals("true"))
			return true;
		
			return false;
	} 
	
	/**
	 * check if the main service is contained in the service list
	 * @param service - a string of services,separate each service with ","
	 * @param mainservice - name of mainservice
	 * @return boolean
	 * @author Jiatong Niu
	 */
	private static Boolean findMainService (List<String> service, String mainservice) {
		
		for (String sc : service) {
			if (sc.equals(mainservice)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * find the servicecombo with name in flexi
	 * @param name - name of the service combo
	 * @return ServiceCombo
	 * @author Jiatong Niu
	 */
	private static ServiceCombo findServiceCombo (String name) {
		ServiceCombo foundCombo = null;
	     for(BookableService bs: FlexiBookApplication.getFlexiBook().getBookableServices()) {
	    	 if (bs.getName().equals(name) ) 
	    		 foundCombo = (ServiceCombo) bs;
	     }
		return foundCombo;
	}
	/**
	 * check if all the service in the service list is in flexibook system
	 * @param service - a list of all services that we want to put into combo
	 * @return the name of service that is not found in the system
	 * @author Jiatong Niu
	 */
	private static String hasServices (List<String> service) {
		for(String name:service) {
			Service s=toService(FlexiBookApplication.getFlexiBook(),name);
			if (s==null) {
				return name;
			}
		}
		return null;
  }
	/**
	 * convert a string separate with "," into a list
	 * @param str - a string separated with ","
	 * @return List<String>
	 * @author Jiatong Niu
	 */
	private static List<String> toList (String str){
		String[] strings = str.split(",");
		List<String> result2 = new ArrayList<String>();
		for (String string : strings) {
		    result2.add(string);
		}
		return result2;
	}
	

	/**
	 * determine if input is a valid email or not
	 * 
	 * @param email email to verify
	 * @return Boolean
	 * @author Grey Yuan
	 */
	private static boolean isValidEmailAddress(String email) {
		String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
		java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
		java.util.regex.Matcher m = p.matcher(email);
		return m.matches();
	}

	/**
	 * determine if two time slots overlap or not
	 * 
	 * @param ts1 first time slot
	 * @param ts2 second time slot
	 * @return isOverlap
	 * @author Grey Yuan
	 */
	private static boolean isOverlap(TimeSlot ts1, TimeSlot ts2) {
		boolean isOverlap = false;
		LocalDateTime startDateTime1 = dateAndTime(ts1.getStartDate(), ts1.getStartTime());
		LocalDateTime endDateTime1 = dateAndTime(ts1.getEndDate(), ts1.getEndTime());
		LocalDateTime startDateTime2 = dateAndTime(ts2.getStartDate(), ts2.getStartTime());
		LocalDateTime endDateTime2 = dateAndTime(ts2.getEndDate(), ts2.getEndTime());
		//bH.getEndTime().after(newStartTime) || newEndTime.after(bH.getStartTime())
		if (((startDateTime1.compareTo(startDateTime2) >= 0) && (startDateTime1.compareTo(endDateTime2)<=0))
				||(endDateTime1.compareTo(startDateTime2)>=0 && endDateTime1.compareTo(endDateTime2)<=0)) {
			isOverlap = true;
		}
		return isOverlap;
	}

	/**
	 * determine the sum of date and time in order to compare them
	 * 
	 * @param date date to sum
	 * @param time time to sum
	 * @return Date
	 * @author Grey Yuan
	 */
	private static LocalDateTime dateAndTime(Date date, Time time) {
//		Long dateLong = date.getTime();
//		Long timeLong = time.getTime();
//		Date date1 = new Date(dateLong + timeLong);
		LocalDate datePart = LocalDate.parse(date.toString());
		LocalTime timePart = LocalTime.parse(time.toString());
		LocalDateTime localDateTime = LocalDateTime.of(datePart, timePart);
		
		return localDateTime;

	}

	/**
	 * get the current system date
	 * @return Date
	 * @author Grey Yuan
	 */
	private static java.util.Date getCurrentDate() {
		java.util.Calendar cal = java.util.Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		java.util.Date temDate = cal.getTime();
		java.sql.Date date = new java.sql.Date(temDate.getTime());
		return date;
	}
	
	/**@author lizhiwei
	 * @param date
	 * @param StartTime
	 * @param EndTime
	 * @param services
	 * @param Unavilitems
	 * @return
	 * add service/combo
	 */
	private static ArrayList<TOViewAppointment> addAllkindService(Date date,Time StartTime,Time EndTime, Service services,ArrayList<TOViewAppointment>Unavilitems) {
		if(!StartTime.equals(EndTime)) {
		
		if (((Service)services).getDowntimeDuration()==0) {
			Unavilitems=addService(date,StartTime,EndTime,Unavilitems);
		}
		else {
			int downtimeStart =((Service)services).getDowntimeStart();
			Calendar cal = Calendar.getInstance();
			Time StartTime2=StartTime;
			Time EndTime2;
			cal.setTime(StartTime2);
			cal.add(Calendar.MINUTE, downtimeStart);
			java.util.Date da = cal.getTime();
			SimpleDateFormat sdf=new SimpleDateFormat("HH:mm:ss");
			String dayAsstring=sdf.format(da);
			EndTime2=Time.valueOf(dayAsstring);
			//now format this time
			
			Unavilitems=addService(date,StartTime2,EndTime2,Unavilitems);
			
			int downtimeEnd=downtimeStart+((Service)services).getDowntimeDuration();
			Calendar cal1 = Calendar.getInstance();
			cal1.setTime(StartTime);
			cal1.add(Calendar.MINUTE, downtimeEnd);
			java.util.Date da1 = cal1.getTime();
			String dayAsstring1=sdf.format(da1);
			Time StartTime1=Time.valueOf(dayAsstring1);
			Time EndTime1=EndTime;
			Unavilitems=addService(date,StartTime1,EndTime1,Unavilitems);
			}
		return Unavilitems;
		}
		return Unavilitems;
	}
	/**@author lizhiwei
	 * @param date
	 * @param startTime
	 * @param endTime
	 * @param Unavilitems
	 * @return
	 */
	private static ArrayList<TOViewAppointment> addService(Date date,Time startTime, Time endTime,ArrayList<TOViewAppointment> Unavilitems) {
		if(!startTime.equals(endTime)) {
		boolean flag=false;
		for (TOViewAppointment item:Unavilitems) {
			if(item.getEndtime().equals(startTime)){
				TOViewAppointment unavil=new TOViewAppointment (date,item.getStarttime(),endTime,false);
				Unavilitems.remove(item);
				Unavilitems.add(unavil);
				flag=true;
				break;
			}
			if(item.getStarttime().equals(endTime)){
				TOViewAppointment unavil=new TOViewAppointment (date,startTime,item.getEndtime(),false);
				Unavilitems.remove(item);
				Unavilitems.add(unavil);
				flag=true;
				break;
			}
					
		}
		if(!flag) {
		TOViewAppointment unavil=new TOViewAppointment (date,startTime,endTime,false);
		Unavilitems.add(unavil);
		}	
		return Unavilitems;}
		return Unavilitems;
	}
	
	/**@author lizhiwei
	 * @param date
	 * @return
	 */
	private static String getWeek(Date date) {
		 String[] weeks = {  "Sunday","Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday" };
				 Calendar cal = Calendar.getInstance();
				 cal.setTime(date);
				 int week_index = cal.get(Calendar.DAY_OF_WEEK) - 1;
				 if(week_index<0){
				 week_index = 0;
				 }
				 return weeks[week_index];
	}
	
	/**@author lizhiwei
	 * @param date
	 * @return
	 */
	private static int getWeekIndex(Date date) {
		 String[] weeks = {  "Sunday","Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday" };
				 Calendar cal = Calendar.getInstance();
				 cal.setTime(date);
				 int week_index = cal.get(Calendar.DAY_OF_WEEK) - 1;
				 if(week_index<0){
				 week_index = 0;
				 }
				 return week_index;
	}
	

	public static List<TOService> getServices(){
		ArrayList<TOService> services = new ArrayList<TOService>();
		for(BookableService aBookableService : FlexiBookApplication.getFlexiBook().getBookableServices()) {
			if(aBookableService instanceof Service) {
				TOService aTOService = new TOService(aBookableService.getName(), ((Service) aBookableService).getDuration(), ((Service) aBookableService).getDowntimeDuration(), ((Service) aBookableService).getDowntimeStart());
				services.add(aTOService);
			}
		}
		return services;
		
	}
	
	/**
	 * @author Jack Wei
	 * @return list of TOAppointments
	 */
	public static List<TOAppointmentItem> getAppointments(){
		ArrayList<TOAppointmentItem> appointments = new ArrayList<TOAppointmentItem>();
		for(Appointment myAppointment : FlexiBookApplication.getFlexiBook().getAppointments()) {
			if(myAppointment instanceof Appointment) {
				TOAppointmentItem myTOAppointment = new TOAppointmentItem(myAppointment.getStateFullName(),myAppointment.getCustomer().getUsername(),myAppointment.getBookableService().getName(),myAppointment.getTimeSlot().getStartDate(),myAppointment.getTimeSlot().getStartTime(),myAppointment.getTimeSlot().getEndTime());
				appointments.add(myTOAppointment);
			}
		}
		return appointments;
		
	}
	

	/**
	 * @author Jack Wei
	 * @param serviceName
	 * @param date
	 * @param startTime
	 * @throws InvalidInputException
	 */
	public static void startAppointment(String serviceName, Date date, Time startTime) throws InvalidInputException{
		FlexiBookApplication.setSystemDateAndTime();
		if(FlexiBookApplication.getCurrentDate().compareTo(date) == 0) {
			Appointment selectedApnmt = findAppointment(serviceName,date,startTime);
			selectedApnmt.checkIn();
		} else {
			String error = "Cannot start before appointment start date.";
			throw new InvalidInputException(error.trim());
		}
	}

	/**
	 * @author Jack Wei
	 * @param serviceName
	 * @param date
	 * @param startTime
	 * @throws InvalidInputException
	 */
	public static void endAppointment(String serviceName, Date date, Time startTime) throws InvalidInputException{
		Appointment selectedApnmt = findAppointment(serviceName,date,startTime);
		selectedApnmt.complete();
	}
	
	/**
	 * @author Jack Wei
	 * @param serviceName
	 * @param date
	 * @param startTime
	 * @throws InvalidInputException
	 */
	public static void registerNoShow(String serviceName, Date date, Time startTime) throws InvalidInputException{
		FlexiBookApplication.setSystemDateAndTime();
		if(FlexiBookApplication.getCurrentDate().compareTo(date) == 0 && FlexiBookApplication.getCurrentTime().compareTo(startTime) >= 0) {
			Appointment selectedApnmt = findAppointment(serviceName,date,startTime);
			selectedApnmt.notShow();
		} else {
			String error = "Cannot register noshow before appointment start time.";
			throw new InvalidInputException(error.trim());
		}
	}
	
	public static DayOfWeek stringToDayOfWeek (String string) {
		DayOfWeek day = DayOfWeek.valueOf(string);
		return day;
	}
	/** 
	 * @author Jiatong Niu
	 */
	public static boolean makeanAppointment(String serviceName, Date date, Time startTime) throws InvalidInputException {
		User curuser= null;
		Time endTime = null;


		
		curuser=FlexiBookApplication.getCurrentUser();
		
		FlexiBook flexibook = FlexiBookApplication.getFlexiBook();
		
		
		Service sv = findService(serviceName);
		endTime =addDuration(startTime,sv.getDuration());
		
		
		TimeSlot ts = new TimeSlot(date, startTime, date, endTime, flexibook);
		Appointment newAptmt =null;
		if(curuser instanceof Customer) {
		    newAptmt = new Appointment((Customer)curuser,sv,ts,flexibook);
		    
			newAptmt.makeAppointment();
		}
	
		
		
		if(newAptmt.getStateFullName().contentEquals("Scheduled")) {
			FlexiBookPersistence.save(FlexiBookApplication.getFlexiBook());
			return true;
		}else {
			return false;
		}
   
	}
	/** 
	 * @author Jiatong Niu
	 */
	public static boolean cancelanAppointment(String serviceName, Date date, Time startTime)throws InvalidInputException{
		User curuser= null;
		Time endTime = null;
		Boolean iscancelled =false;
		curuser=FlexiBookApplication.getCurrentUser();
		FlexiBook flexibook = FlexiBookApplication.getFlexiBook();
		Appointment captmt = findAppointment(serviceName, date, startTime);
		if(captmt.cancelAppointment()) {
			iscancelled=true;
			flexibook.removeAppointment(captmt);
			FlexiBookPersistence.save(FlexiBookApplication.getFlexiBook());
		}
		
	    return iscancelled;
	}
	/** 
	 * @author Jiatong Niu
	 */
	private static Service findService(String service) {
		Service foundService = null;
		for (BookableService s : FlexiBookApplication.getFlexiBook().getBookableServices()) {
			if (s instanceof Service&&s.getName().equals(service)) {
				foundService = (Service) s ;
				break;
			}
		}
		return foundService;
	}
	
	/** 
	 * @author Jiatong Niu
	 */
	private static Time addDuration(Time time, int duration) {
		SimpleDateFormat sdf=new SimpleDateFormat("HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		cal.setTime(time);
		cal.add(Calendar.MINUTE, duration);
		java.util.Date da = cal.getTime();
		String timeAsString=sdf.format(da);
		Time End=Time.valueOf(timeAsString);
		
		return End;
		
	}

}
