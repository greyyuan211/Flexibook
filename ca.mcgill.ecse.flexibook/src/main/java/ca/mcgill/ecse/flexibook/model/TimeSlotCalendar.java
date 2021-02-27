/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.30.1.5099.60569f335 modeling language!*/

package ca.mcgill.ecse.flexibook.model;
import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;

// line 81 "../../../../../FlexiBookPersistence.ump"
// line 96 "../../../../../FlexiBook.ump"
public class TimeSlotCalendar implements Serializable
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //TimeSlotCalendar Attributes
  private Date date;
  private Time starttime;
  private Time endtime;
  private boolean isAvailable;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public TimeSlotCalendar(Date aDate, Time aStarttime, Time aEndtime, boolean aIsAvailable)
  {
    date = aDate;
    starttime = aStarttime;
    endtime = aEndtime;
    isAvailable = aIsAvailable;
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setDate(Date aDate)
  {
    boolean wasSet = false;
    date = aDate;
    wasSet = true;
    return wasSet;
  }

  public boolean setStarttime(Time aStarttime)
  {
    boolean wasSet = false;
    starttime = aStarttime;
    wasSet = true;
    return wasSet;
  }

  public boolean setEndtime(Time aEndtime)
  {
    boolean wasSet = false;
    endtime = aEndtime;
    wasSet = true;
    return wasSet;
  }

  public boolean setIsAvailable(boolean aIsAvailable)
  {
    boolean wasSet = false;
    isAvailable = aIsAvailable;
    wasSet = true;
    return wasSet;
  }

  public Date getDate()
  {
    return date;
  }

  public Time getStarttime()
  {
    return starttime;
  }

  public Time getEndtime()
  {
    return endtime;
  }

  public boolean getIsAvailable()
  {
    return isAvailable;
  }
  /* Code from template attribute_IsBoolean */
  public boolean isIsAvailable()
  {
    return isAvailable;
  }

  public void delete()
  {}


  public String toString()
  {
    return super.toString() + "["+
            "isAvailable" + ":" + getIsAvailable()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "date" + "=" + (getDate() != null ? !getDate().equals(this)  ? getDate().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "starttime" + "=" + (getStarttime() != null ? !getStarttime().equals(this)  ? getStarttime().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "endtime" + "=" + (getEndtime() != null ? !getEndtime().equals(this)  ? getEndtime().toString().replaceAll("  ","    ") : "this" : "null");
  }  
  //------------------------
  // DEVELOPER CODE - PROVIDED AS-IS
  //------------------------
  
  // line 84 "../../../../../FlexiBookPersistence.ump"
  private static final long serialVersionUID = 20989467534524533L ;

  
}