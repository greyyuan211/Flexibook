/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.30.1.5099.60569f335 modeling language!*/

package ca.mcgill.ecse.flexibook.model;
import java.sql.Date;
import java.sql.Time;
import java.text.*;
import ca.mcgill.ecse.flexibook.application.FlexiBookApplication;
import java.io.Serializable;
import java.util.*;

// line 1 "../../../../../FlexiBookStates.ump"
// line 14 "../../../../../FlexiBookPersistence.ump"
// line 88 "../../../../../FlexiBook.ump"
public class Appointment implements Serializable
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Appointment State Machines
  public enum State { Pending, Final, Scheduled, InProgress }
  private State state;

  //Appointment Associations
  private Customer customer;
  private BookableService bookableService;
  private List<ComboItem> chosenItems;
  private TimeSlot timeSlot;
  private FlexiBook flexiBook;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Appointment(Customer aCustomer, BookableService aBookableService, TimeSlot aTimeSlot, FlexiBook aFlexiBook)
  {
    boolean didAddCustomer = setCustomer(aCustomer);
    if (!didAddCustomer)
    {
      throw new RuntimeException("Unable to create appointment due to customer. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    boolean didAddBookableService = setBookableService(aBookableService);
    if (!didAddBookableService)
    {
      throw new RuntimeException("Unable to create appointment due to bookableService. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    chosenItems = new ArrayList<ComboItem>();
    if (!setTimeSlot(aTimeSlot))
    {
      throw new RuntimeException("Unable to create Appointment due to aTimeSlot. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    boolean didAddFlexiBook = setFlexiBook(aFlexiBook);
    if (!didAddFlexiBook)
    {
      throw new RuntimeException("Unable to create appointment due to flexiBook. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    setState(State.Pending);
  }

  //------------------------
  // INTERFACE
  //------------------------

  public String getStateFullName()
  {
    String answer = state.toString();
    return answer;
  }

  public State getState()
  {
    return state;
  }

  public boolean makeAppointment()
  {
    boolean wasEventProcessed = false;
    
    State aState = state;
    switch (aState)
    {
      case Pending:
        if (!(isCurApptAvailable()&&isBeforeApptDateAndTime()))
        {
          setState(State.Final);
          wasEventProcessed = true;
          break;
        }
        if (isCurApptAvailable()&&isBeforeApptDateAndTime())
        {
          setState(State.Scheduled);
          wasEventProcessed = true;
          break;
        }
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean cancelAppointment()
  {
    boolean wasEventProcessed = false;
    
    State aState = state;
    switch (aState)
    {
      case Scheduled:
        if (isBeforeApptDate())
        {
          setState(State.Final);
          wasEventProcessed = true;
          break;
        }
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean updateApptOfServiceCombo(String action,String comboItem)
  {
    boolean wasEventProcessed = false;
    
    State aState = state;
    switch (aState)
    {
      case Scheduled:
        if (isUpdateable(action,comboItem)&&isServiceCombo()&&isBeforeApptDate())
        {
        // line 14 "../../../../../FlexiBookStates.ump"
          doUpdateOptServices(action,comboItem);
          setState(State.Scheduled);
          wasEventProcessed = true;
          break;
        }
        break;
      case InProgress:
        if (isUpdateable(action,comboItem)&&isServiceCombo())
        {
        // line 22 "../../../../../FlexiBookStates.ump"
          doUpdateOptServices(action,comboItem);
          setState(State.InProgress);
          wasEventProcessed = true;
          break;
        }
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean changeApptTime(Date newDate,Time newTime)
  {
    boolean wasEventProcessed = false;
    
    State aState = state;
    switch (aState)
    {
      case Scheduled:
        if (isTimeChangeable(newDate,newTime)&&isBeforeApptDate())
        {
        // line 15 "../../../../../FlexiBookStates.ump"
          doChangeApptTime(newDate, newTime);
          setState(State.Scheduled);
          wasEventProcessed = true;
          break;
        }
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean changeToAnotherService(String serviceName,String optServices,Date newDate,Time newTime)
  {
    boolean wasEventProcessed = false;
    
    State aState = state;
    switch (aState)
    {
      case Scheduled:
        if (isAppointmentChangeable(serviceName,optServices,newDate,newTime)&&isBeforeApptDate())
        {
        // line 16 "../../../../../FlexiBookStates.ump"
          doChangeToAnotherService(serviceName, optServices, newDate, newTime);
          setState(State.Scheduled);
          wasEventProcessed = true;
          break;
        }
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean checkIn()
  {
    boolean wasEventProcessed = false;
    
    State aState = state;
    switch (aState)
    {
      case Scheduled:
        setState(State.InProgress);
        wasEventProcessed = true;
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean notShow()
  {
    boolean wasEventProcessed = false;
    
    State aState = state;
    switch (aState)
    {
      case Scheduled:
        // line 18 "../../../../../FlexiBookStates.ump"
        updateNoShowRecords();
        setState(State.Final);
        wasEventProcessed = true;
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean complete()
  {
    boolean wasEventProcessed = false;
    
    State aState = state;
    switch (aState)
    {
      case InProgress:
        setState(State.Final);
        wasEventProcessed = true;
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  private void setState(State aState)
  {
    state = aState;

    // entry actions and do activities
    switch(state)
    {
      case Final:
        delete();
        break;
    }
  }
  /* Code from template association_GetOne */
  public Customer getCustomer()
  {
    return customer;
  }
  /* Code from template association_GetOne */
  public BookableService getBookableService()
  {
    return bookableService;
  }
  /* Code from template association_GetMany */
  public ComboItem getChosenItem(int index)
  {
    ComboItem aChosenItem = chosenItems.get(index);
    return aChosenItem;
  }

  public List<ComboItem> getChosenItems()
  {
    List<ComboItem> newChosenItems = Collections.unmodifiableList(chosenItems);
    return newChosenItems;
  }

  public int numberOfChosenItems()
  {
    int number = chosenItems.size();
    return number;
  }

  public boolean hasChosenItems()
  {
    boolean has = chosenItems.size() > 0;
    return has;
  }

  public int indexOfChosenItem(ComboItem aChosenItem)
  {
    int index = chosenItems.indexOf(aChosenItem);
    return index;
  }
  /* Code from template association_GetOne */
  public TimeSlot getTimeSlot()
  {
    return timeSlot;
  }
  /* Code from template association_GetOne */
  public FlexiBook getFlexiBook()
  {
    return flexiBook;
  }
  /* Code from template association_SetOneToMany */
  public boolean setCustomer(Customer aCustomer)
  {
    boolean wasSet = false;
    if (aCustomer == null)
    {
      return wasSet;
    }

    Customer existingCustomer = customer;
    customer = aCustomer;
    if (existingCustomer != null && !existingCustomer.equals(aCustomer))
    {
      existingCustomer.removeAppointment(this);
    }
    customer.addAppointment(this);
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_SetOneToMany */
  public boolean setBookableService(BookableService aBookableService)
  {
    boolean wasSet = false;
    if (aBookableService == null)
    {
      return wasSet;
    }

    BookableService existingBookableService = bookableService;
    bookableService = aBookableService;
    if (existingBookableService != null && !existingBookableService.equals(aBookableService))
    {
      existingBookableService.removeAppointment(this);
    }
    bookableService.addAppointment(this);
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfChosenItems()
  {
    return 0;
  }
  /* Code from template association_AddUnidirectionalMany */
  public boolean addChosenItem(ComboItem aChosenItem)
  {
    boolean wasAdded = false;
    if (chosenItems.contains(aChosenItem)) { return false; }
    chosenItems.add(aChosenItem);
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeChosenItem(ComboItem aChosenItem)
  {
    boolean wasRemoved = false;
    if (chosenItems.contains(aChosenItem))
    {
      chosenItems.remove(aChosenItem);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addChosenItemAt(ComboItem aChosenItem, int index)
  {  
    boolean wasAdded = false;
    if(addChosenItem(aChosenItem))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfChosenItems()) { index = numberOfChosenItems() - 1; }
      chosenItems.remove(aChosenItem);
      chosenItems.add(index, aChosenItem);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveChosenItemAt(ComboItem aChosenItem, int index)
  {
    boolean wasAdded = false;
    if(chosenItems.contains(aChosenItem))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfChosenItems()) { index = numberOfChosenItems() - 1; }
      chosenItems.remove(aChosenItem);
      chosenItems.add(index, aChosenItem);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addChosenItemAt(aChosenItem, index);
    }
    return wasAdded;
  }
  /* Code from template association_SetUnidirectionalOne */
  public boolean setTimeSlot(TimeSlot aNewTimeSlot)
  {
    boolean wasSet = false;
    if (aNewTimeSlot != null)
    {
      timeSlot = aNewTimeSlot;
      wasSet = true;
    }
    return wasSet;
  }
  /* Code from template association_SetOneToMany */
  public boolean setFlexiBook(FlexiBook aFlexiBook)
  {
    boolean wasSet = false;
    if (aFlexiBook == null)
    {
      return wasSet;
    }

    FlexiBook existingFlexiBook = flexiBook;
    flexiBook = aFlexiBook;
    if (existingFlexiBook != null && !existingFlexiBook.equals(aFlexiBook))
    {
      existingFlexiBook.removeAppointment(this);
    }
    flexiBook.addAppointment(this);
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    Customer placeholderCustomer = customer;
    this.customer = null;
    if(placeholderCustomer != null)
    {
      placeholderCustomer.removeAppointment(this);
    }
    BookableService placeholderBookableService = bookableService;
    this.bookableService = null;
    if(placeholderBookableService != null)
    {
      placeholderBookableService.removeAppointment(this);
    }
    chosenItems.clear();
    timeSlot = null;
    FlexiBook placeholderFlexiBook = flexiBook;
    this.flexiBook = null;
    if(placeholderFlexiBook != null)
    {
      placeholderFlexiBook.removeAppointment(this);
    }
  }

  // line 26 "../../../../../FlexiBookStates.ump"
   private boolean isBeforeApptDateAndTime(){
    Date apptDate = this.getTimeSlot().getStartDate();
  Time apptTime = this.getTimeSlot().getStartTime();
  Date systemDate = FlexiBookApplication.getCurrentDate();
  Time systemTime = FlexiBookApplication.getCurrentTime();
  if (systemDate.before(apptDate)) {
   return true;
  }else if(systemDate.equals(apptDate)){
   if(systemTime.compareTo(apptTime)<0) {
    return true;
   }else {
    return false;
   }
  }else {
   return false;
  }
  }

  // line 43 "../../../../../FlexiBookStates.ump"
   private boolean isCurApptAvailable(){
    return this.isAvailable(timeSlot.getStartDate(), timeSlot.getStartTime(), bookableService, chosenItems);
  }

  // line 46 "../../../../../FlexiBookStates.ump"
   private boolean isBeforeApptDate(){
    Date apptDate = this.getTimeSlot().getStartDate();
	  Date systemDate = FlexiBookApplication.getCurrentDate();
	  if(systemDate.before(apptDate)) {
		  return true;
	  }else { 
		  return false; 
	  }
  }

  // line 56 "../../../../../FlexiBookStates.ump"
   private void updateNoShowRecords(){
    int aNumOfNoShows = customer.getNumOfNoShows() + 1;
	   customer.setNumOfNoShows(aNumOfNoShows);
  }

  // line 61 "../../../../../FlexiBookStates.ump"
   private boolean isServiceCombo(){
    if(this.getBookableService() instanceof ServiceCombo) {
		   return true;
	   }else {
		   return false;
	   }
  }

  // line 69 "../../../../../FlexiBookStates.ump"
   private void deleteAppt(){
    this.delete();
  }


  /**
   * @author Cecilia Jiang
   * Add a duration to a java.sql.Time object
   * @param startTime  start time
   * @param aDuration  duration
   * @return a new time
   */
  // line 78 "../../../../../FlexiBookStates.ump"
   private java.sql.Time addDurationToTime(Time startTime, int aDuration){
    SimpleDateFormat sdf=new SimpleDateFormat("HH:mm:ss");
	 Calendar cal = Calendar.getInstance();
	 cal.setTime(startTime);
	 cal.add(Calendar.MINUTE, aDuration);
	 java.util.Date dateWithTime = cal.getTime();
	 String dayAsstring = sdf.format(dateWithTime);
	 Time endTime = Time.valueOf(dayAsstring);
	 return endTime;
  }


  /**
   * @author lizhiwei
   * A helper method for getTimeslotCalendarAtdate; Change the calendar when we add a bookableservice
   * @param date	date of the appointment
   * @param StartTime start time of the appointment
   * @param EndTime end time of the appointment
   * @param services service name
   * @param Unavilitems unavailable time slots
   * @return new calendar
   */
  // line 98 "../../../../../FlexiBookStates.ump"
   private static  ArrayList<TimeSlotCalendar> addAllkindService(Date date, Time StartTime, Time EndTime, Service services, ArrayList<TimeSlotCalendar> Unavilitems){
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


  /**
   * @author lizhiwei
   * helper method for addAllService, Change the calendar when we add a service
   * @param date date of the appointment
   * @param startTime start time of the appointment
   * @param endTime end time of the appointment
   * @param Unavilitems unavailable time slots
   * @return new calendar
   */
  // line 141 "../../../../../FlexiBookStates.ump"
   private static  ArrayList<TimeSlotCalendar> addService(Date date, Time startTime, Time endTime, ArrayList<TimeSlotCalendar> Unavilitems){
    if(!startTime.equals(endTime)) {
	   boolean flag=false;
	   for (TimeSlotCalendar item:Unavilitems) {
	    if(item.getEndtime().equals(startTime)){
	     TimeSlotCalendar unavil=new TimeSlotCalendar (date,item.getStarttime(),endTime,false);
	     Unavilitems.remove(item);
	     Unavilitems.add(unavil);
	     flag=true;
	     break;
	    }
	    if(item.getStarttime().equals(endTime)){
	     TimeSlotCalendar unavil=new TimeSlotCalendar (date,startTime,item.getEndtime(),false);
	     Unavilitems.remove(item);
	     Unavilitems.add(unavil);
	     flag=true;
	     break;
	    }
	      
	   }
	   if(!flag) {
	   TimeSlotCalendar unavil=new TimeSlotCalendar (date,startTime,endTime,false);
	   Unavilitems.add(unavil);
	   } 
	   return Unavilitems;}
	   return Unavilitems;
  }

  // line 168 "../../../../../FlexiBookStates.ump"
   private static  boolean aServiceisAvailable(Time startTime, Service service, Date date, FlexiBook flexibook, ArrayList<TimeSlotCalendar> Avilitems){
    boolean isavil=true;
	   if (service.getDowntimeDuration()==0 || service.getDuration()==service.getDowntimeStart()+service.getDowntimeDuration() ) {
	    if(!isAvailableWithoutDownTimeDuration(startTime, service, date, flexibook, Avilitems)) {
	     isavil=false;
	    }
	   }
	   else {
	    if(!isAvailableWithDownTimeDuration(startTime, service, date, flexibook, Avilitems)) {
	     isavil=false;
	    }
	   }
	   return isavil;
  }


  /**
   * @author lizhiwei
   * check if a service without downtime duration is possible to be added, a helper method for isAvilable
   * @param startTime start time of the appointment
   * @param service service name
   * @param date start date
   * @param flexibook
   * @param Avilitems available time slots
   * @return true if it can be added, otherwise false
   */
  // line 192 "../../../../../FlexiBookStates.ump"
   private static  boolean isAvailableWithoutDownTimeDuration(Time startTime, Service service, Date date, FlexiBook flexibook, ArrayList<TimeSlotCalendar> Avilitems){
    Time Start=startTime;
	   SimpleDateFormat sdf=new SimpleDateFormat("HH:mm:ss");
	   
	   int duration =service.getDuration()-service.getDowntimeDuration();
	   Calendar cal = Calendar.getInstance();
	   cal.setTime(Start);
	   cal.add(Calendar.MINUTE, duration);
	   java.util.Date da = cal.getTime();
	   String dayAsstring=sdf.format(da);
	   Time End=Time.valueOf(dayAsstring);
	   boolean isAvailable=false;
	     for (TimeSlotCalendar item:Avilitems) {
	      if ((item.getStarttime().compareTo(Start)<=0) & (item.getEndtime().compareTo(End)>=0)) {
	      isAvailable = true;
	      }
	     }
	   return isAvailable;
  }

  // line 211 "../../../../../FlexiBookStates.ump"
   private static  boolean isAvailableWithDownTimeDuration(Time startTime, Service service, Date date, FlexiBook flexibook, ArrayList<TimeSlotCalendar> Avilitems){
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
	    
	    
	    int isAvailable=0;
	      for (TimeSlotCalendar item:Avilitems) {
	       if ((item.getStarttime().compareTo(ts.getStartTime())<=0) & (item.getEndtime().compareTo(ts.getEndTime())>=0)) {
	       isAvailable++;
	       break;
	       }
	      }
	      for (TimeSlotCalendar item:Avilitems) {
	        if ((item.getStarttime().compareTo(ts1.getStartTime())<=0) & (item.getEndtime().compareTo(ts1.getEndTime())>=0)) {
	        isAvailable++;
	        break;
	        }
	       }
	      return (isAvailable==2);
  }


  /**
   * @author lizhiwei
   * get what day it is the date, a helper method for getTimeSlotCalendarAtdate
   * @param date	specific date
   * @return the day of the date
   */
  // line 260 "../../../../../FlexiBookStates.ump"
   private static  String getWeekday(Date date){
    String[] weeks = {  "Sunday","Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday" };
      Calendar cal = Calendar.getInstance();
      cal.setTime(date);
      int week_index = cal.get(Calendar.DAY_OF_WEEK) - 1;
      if(week_index<0){
      week_index = 0;
      }
      return weeks[week_index];
  }


  /**
   * @author Cecilia Jiang
   * Perform an add or remove action to the current service combo appointment
   * @param action, add or remove
   * @param comboItem, the item to be changed
   */
  // line 274 "../../../../../FlexiBookStates.ump"
   private void doUpdateOptServices(String action, String comboItem){
    List<ComboItem> curComboItems = this.getChosenItems();
	List<ComboItem> allComboItems = ((ServiceCombo)this.getBookableService()).getServices();
	int aDuration = 0;
	if(action.equals("add")) {
		int index = 0;
		for(ComboItem aComboItem:allComboItems) {
			if(aComboItem.getService().getName().equals(comboItem)){
				aDuration = aComboItem.getService().getDuration();
				this.addChosenItemAt(aComboItem, index);
				break;
			}
			if(curComboItems.contains(aComboItem)) {
				index++;
			}
		}
	}else if(action.equals("remove")) {
		for(ComboItem aComboItem:curComboItems) {
			if(aComboItem.getService().getName().equals(comboItem)) {
				aDuration = aComboItem.getService().getDuration() * (-1);
				this.removeChosenItem(aComboItem);
				break;
			}
		}
	}
	Time curEndTime = this.getTimeSlot().getEndTime();
	Time newEndTime = this.addDurationToTime(curEndTime, aDuration);
	this.getTimeSlot().setEndTime(newEndTime);
  }


  /**
   * @author Cecilia Jiang
   * Change the timeslot of the current service combo appointment
   * @param newDate
   * @param newTime
   */
  // line 307 "../../../../../FlexiBookStates.ump"
   private void doChangeApptTime(Date newDate, Time newTime){
    BookableService aBookableService = this.getBookableService();
  if(aBookableService instanceof Service) {
   TimeSlot preTimeSlot = this.getTimeSlot();
   int aDuration = ((Service)aBookableService).getDuration();
   Time endTime = addDurationToTime(newTime, aDuration);
   TimeSlot aTimeSlot = new TimeSlot(newDate, newTime, newDate, endTime, flexiBook);
   this.setTimeSlot(aTimeSlot);
   preTimeSlot.delete();
  }else {
   TimeSlot preTimeSlot = this.getTimeSlot();
   Time endTime = newTime;
   for (ComboItem aComboItem : this.getChosenItems()) {
    int duration = aComboItem.getService().getDuration();
    endTime = addDurationToTime(endTime, duration);
   }
   TimeSlot aTimeSlot = new TimeSlot(newDate, newTime, newDate, endTime, flexiBook);
   this.setTimeSlot(aTimeSlot);
   preTimeSlot.delete();
  }
  }


  /**
   * @author Cecilia Jiang
   * Change the original appointment service to a new service/servicecombo with serviceName and optServices; update new timeslot
   * @param serviceName, new service name
   * @param optServices, new optServices, null if it is a service
   * @param newDate
   * @param newTime
   */
  // line 334 "../../../../../FlexiBookStates.ump"
   private void doChangeToAnotherService(String serviceName, String optServices, Date newDate, Time newTime){
    BookableService aBookableService = BookableService.getWithName(serviceName);
		if (aBookableService instanceof ServiceCombo && ((ServiceCombo) aBookableService).getServices()!=null) {
			  this.chosenItems.clear();
		}
		this.setBookableService(aBookableService);
		if (aBookableService instanceof ServiceCombo) {
			  ServiceCombo aServiceCombo=(ServiceCombo) aBookableService;
			  //List<ComboItem> comboItems=new ArrayList<ComboItem>();
			  for(ComboItem item:aServiceCombo.getServices()) {
				  if (optServices.contains(item.getService().getName()) || item.getMandatory()) {
					  //comboItems.add(item);
					  this.addChosenItem(item);
			      }
			  }
		}
		if(aBookableService instanceof Service) {
			TimeSlot preTimeSlot = this.getTimeSlot();
			int aDuration = ((Service)aBookableService).getDuration();
			Time endTime = addDurationToTime(newTime, aDuration);
			TimeSlot aTimeSlot = new TimeSlot(newDate, newTime, newDate, endTime, flexiBook);
			this.setTimeSlot(aTimeSlot);
			preTimeSlot.delete();
		}else {
			TimeSlot preTimeSlot = this.getTimeSlot();
			Time endTime = newTime;
			for (ComboItem aComboItem : this.getChosenItems()) {
				int duration = aComboItem.getService().getDuration();
				endTime = addDurationToTime(endTime, duration);
			}
			TimeSlot aTimeSlot = new TimeSlot(newDate, newTime, newDate, endTime, flexiBook);
			this.setTimeSlot(aTimeSlot);
			preTimeSlot.delete();
		}
  }


  /**
   * @author lizhiwei
   * check if it is possible to add or remove an optional service of this appointment
   * @param action customer action(can be "add" or "remove")
   * @param comboitem the combo item that the customer want to remove or add
   * @return true if is possible, otherwise false
   */
  // line 376 "../../../../../FlexiBookStates.ump"
   private boolean isUpdateable(String action, String comboitem){
    ServiceCombo combo=(ServiceCombo)this.getBookableService();
        boolean isMandantory = false;
        for (ComboItem item:combo.getServices()) {
         if (item.getService().getName().equals(comboitem)) {
          isMandantory=item.getMandatory();
          break;
         }
        }
        if (isMandantory) {
          return false;
        }
        else {
         Time starttime=this.getTimeSlot().getStartTime();
         Date startdate=this.getTimeSlot().getStartDate();
         List<ComboItem> comboitems=this.getChosenItems();
         List<ComboItem> newComboItems = new ArrayList<ComboItem>();
         for(ComboItem aComboItem:comboitems) {
        	 newComboItems.add(aComboItem);
         }
         
         int index=0;
         for (ComboItem item:combo.getServices()) {
          if (item.getService().getName().equals(comboitem)) {
           newComboItems.add(index, item);
          }
          if (comboitems.contains(item)) {
           index++;
           
          }
         }
         if (isAvailable((java.sql.Date) startdate,starttime,this.getBookableService(),newComboItems)) {
          return true;
         }
         else {
          return false;
         }
          
        }
  }


  /**
   * @author lizhiwei
   * check the new timeslot of the given newdate and newStartTime
   * @param newDate
   * @param newTime
   * @return true if is possible, otherwise false
   */
  // line 421 "../../../../../FlexiBookStates.ump"
   private boolean isTimeChangeable(Date newdate, Time newstartTime){
    return isAvailable(newdate, newstartTime, bookableService, chosenItems);
  }

  // line 426 "../../../../../FlexiBookStates.ump"
   private boolean isAppointmentChangeable(String serviceName, String optServices, Date newdate, Time newstartTime){
    BookableService aBookableService=BookableService.getWithName(serviceName);
 if ( aBookableService instanceof Service) {
  return isAvailable(newdate, newstartTime, aBookableService, null);
 }else {
  ServiceCombo aCombo=(ServiceCombo) aBookableService;
  List<ComboItem> comboItems=new ArrayList<ComboItem>();
  for(ComboItem item:aCombo.getServices()) {
   if (optServices.contains(item.getService().getName()) || item.getMandatory()) {
    comboItems.add(item);
   }
  }

  return isAvailable(newdate, newstartTime, aBookableService, comboItems);
 }
  }


  /**
   * 
   * @author lizhiwei
   * check if is available to have the appointment in the system
   * @param date
   * @param startTime
   * @param aBookableService
   * @param comboItems
   * @return true if it is available, otherwise false
   */
  // line 451 "../../../../../FlexiBookStates.ump"
   private boolean isAvailable(Date date, Time startTime, BookableService aBookableService, List<ComboItem> comboItems){
    ArrayList<TimeSlotCalendar>Avilitems=new ArrayList<TimeSlotCalendar>();
    Avilitems=getTimeSlotCalendarAtDate(date);
    for (TimeSlotCalendar item:Avilitems) {
     if (!item.getIsAvailable()) {
      Avilitems.remove(item);
     }
    }
    boolean isavil=true;
    if (aBookableService instanceof Service) {
     isavil=aServiceisAvailable(startTime,(Service) aBookableService, date, flexiBook, Avilitems);
    }else {
     for (ComboItem item: comboItems) {
      if (!aServiceisAvailable(startTime,item.getService(), date, flexiBook, Avilitems)) {
        isavil=false;
        break;
      }
      startTime=addDurationToTime(startTime,item.getService().getDuration());
     }
    }
    return isavil;
  }


  /**
   * author lizhiwei
   * Find the calendar with available and unavailable time slots of a specific date
   * @param date the date of the calendar
   * @return a timeslotcalendar
   */
  // line 479 "../../../../../FlexiBookStates.ump"
   public ArrayList<TimeSlotCalendar> getTimeSlotCalendarAtDate(Date date){
    FlexiBook flexibook = FlexiBookApplication.getFlexiBook();
    ArrayList<TimeSlotCalendar> Unavilitems = new ArrayList<TimeSlotCalendar>();
    ArrayList<TimeSlotCalendar> Avilitems = new ArrayList<TimeSlotCalendar>();
    ArrayList<TimeSlotCalendar> Allitems = new ArrayList<TimeSlotCalendar>();
    List<Appointment> Appointments=flexibook.getAppointments();
    ArrayList<Appointment> AppointmentsAtDate=new ArrayList<Appointment>();
    List<BusinessHour> buisnesshours=flexibook.getBusiness().getBusinessHours();
    BusinessHour buisnesshourAtdate=null;
    List<TimeSlot> holidays=flexibook.getBusiness().getHolidays();
    ArrayList<BusinessHour> buisnesshourcollection=new ArrayList<BusinessHour>();
    
    String WeekDay=getWeekday(date);
    //consider the impact of holidays and vacations on avilable timeslot
    List<TimeSlot> vacations=flexibook.getBusiness().getVacation();
    for (BusinessHour buisnesshour:buisnesshours) {
       if (buisnesshour.getDayOfWeek().toString().equals(WeekDay)) {
        buisnesshourAtdate=buisnesshour;
        buisnesshourcollection.add(buisnesshourAtdate);
        Boolean isHoliday=false;
        for (TimeSlot holiday:holidays) {
         if ((holiday.getEndDate().compareTo(date)>0) && (holiday.getStartDate().compareTo(date)<0)) {
          TimeSlotCalendar item=new TimeSlotCalendar (date,buisnesshourAtdate.getStartTime(),buisnesshourAtdate.getEndTime(),false);
          Unavilitems.add(item);
          return Unavilitems;
         }
         if (holiday.getStartDate().compareTo(date)==0) {
          if (holiday.getStartTime().compareTo(buisnesshourAtdate.getStartTime())>=0) {
          TimeSlotCalendar avil=new TimeSlotCalendar (date,buisnesshourAtdate.getStartTime(),holiday.getStartTime(),true);
          Avilitems.add(avil);
          isHoliday=true;
          continue;
          }else {
           TimeSlotCalendar item=new TimeSlotCalendar (date,buisnesshourAtdate.getStartTime(),buisnesshourAtdate.getEndTime(),false);
           Unavilitems.add(item);
           return Unavilitems;
          }
         }
         if (holiday.getEndDate().compareTo(date)==0) {
          if (holiday.getEndTime().compareTo(buisnesshourAtdate.getEndTime())<=0) {
          TimeSlotCalendar avil=new TimeSlotCalendar (date,buisnesshourAtdate.getStartTime(),holiday.getStartTime(),true);
          Avilitems.add(avil);
          isHoliday=true;
          continue;
          }else {
           TimeSlotCalendar item=new TimeSlotCalendar (date,buisnesshourAtdate.getStartTime(),buisnesshourAtdate.getEndTime(),false);
           Unavilitems.add(item);
           return Unavilitems;
          }
         }
        }
        for (TimeSlot holiday:vacations) {
          if ((holiday.getEndDate().compareTo(date)>0) && (holiday.getStartDate().compareTo(date)<0)) {
           TimeSlotCalendar item=new TimeSlotCalendar (date,buisnesshourAtdate.getStartTime(),buisnesshourAtdate.getEndTime(),false);
           Unavilitems.add(item);
           return Unavilitems;
          }
          if (holiday.getStartDate().compareTo(date)==0) {
           if (holiday.getStartTime().compareTo(buisnesshourAtdate.getStartTime())>=0) {
           TimeSlotCalendar avil=new TimeSlotCalendar (date,buisnesshourAtdate.getStartTime(),holiday.getStartTime(),true);
           Avilitems.add(avil);
           isHoliday=true;
           break;
           }else {
            TimeSlotCalendar item=new TimeSlotCalendar (date,buisnesshourAtdate.getStartTime(),buisnesshourAtdate.getEndTime(),false);
            Unavilitems.add(item);
            return Unavilitems;
           }
          }
          if (holiday.getEndDate().compareTo(date)==0) {
           if (holiday.getEndTime().compareTo(buisnesshourAtdate.getEndTime())<=0) {
           TimeSlotCalendar avil=new TimeSlotCalendar (date,buisnesshourAtdate.getStartTime(),holiday.getStartTime(),true);
           Avilitems.add(avil);
           isHoliday=true;
           break;
           }else {
            TimeSlotCalendar item=new TimeSlotCalendar (date,buisnesshourAtdate.getStartTime(),buisnesshourAtdate.getEndTime(),false);
            Unavilitems.add(item);
            return Unavilitems;
           }
          }
         }
        if (!isHoliday) {
        TimeSlotCalendar item=new TimeSlotCalendar (date,buisnesshourAtdate.getStartTime(),buisnesshourAtdate.getEndTime(),true);
        Avilitems.add(item);
        }
        }
        
      }
    
    //Consider the appointment on avilable timeslot
    for(Appointment appointment:Appointments) {
     TimeSlot Ts=appointment.getTimeSlot();
     
     if (Ts.getStartDate().equals(date)& !appointment.equals(this)){
      AppointmentsAtDate.add(appointment);
     }
    }
    
    
    for(Appointment appointment:AppointmentsAtDate) {
     BookableService services=appointment.getBookableService();
     TimeSlot Ts=appointment.getTimeSlot();
     Time EndTime=Ts.getEndTime();
     Time StartTime=Ts.getStartTime();
     
     if (services instanceof Service) {
      Unavilitems=addAllkindService(date,StartTime,EndTime,(Service)services,Unavilitems);   
     }
     else {
      
      Time Start=StartTime;
      Time End;
      for(ComboItem item:appointment.getChosenItems()) {
       SimpleDateFormat sdf=new SimpleDateFormat("HH:mm:ss");
       Service service=item.getService();
       int duration =service.getDuration();
       Calendar cal = Calendar.getInstance();
       cal.setTime(Start);
       cal.add(Calendar.MINUTE, duration);
       java.util.Date da = cal.getTime();
       String dayAsstring=sdf.format(da);
       End=Time.valueOf(dayAsstring);
       
       Unavilitems=addAllkindService(date,Start,End,service,Unavilitems);
       
       Start=End;
      }
     }
    }
    
    
    //substract unavil from avil timeslot
    
    
    for (TimeSlotCalendar unavil:Unavilitems) {
     for (TimeSlotCalendar avil:Avilitems) {
      if ((avil.getStarttime().compareTo(unavil.getStarttime())<=0) & (avil.getEndtime().compareTo(unavil.getEndtime())>=0)) {
       TimeSlotCalendar avil1=new TimeSlotCalendar (date,avil.getStarttime(),unavil.getStarttime(),true);
       TimeSlotCalendar avil2=new TimeSlotCalendar (date,unavil.getEndtime(),avil.getEndtime(),true);
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
    Allitems=Avilitems;
    
    
    return Allitems;
  }
  
  //------------------------
  // DEVELOPER CODE - PROVIDED AS-IS
  //------------------------
  
  // line 17 "../../../../../FlexiBookPersistence.ump"
  private static final long serialVersionUID = 2315072607928790501L ;

  
}