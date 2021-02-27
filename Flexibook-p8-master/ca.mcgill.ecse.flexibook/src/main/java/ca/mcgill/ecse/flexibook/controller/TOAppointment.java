/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.30.1.5099.60569f335 modeling language!*/

package ca.mcgill.ecse.flexibook.controller;
import java.sql.Date;
import java.sql.Time;

// line 24 "../../../../../FlexiBookTransferObjects.ump"
public class TOAppointment
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //TOAppointment Attributes
  private boolean result;
  private Date startdate;
  private Time startTime;
  private Time endtime;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public TOAppointment(boolean aResult, Date aStartdate, Time aStartTime, Time aEndtime)
  {
    result = aResult;
    startdate = aStartdate;
    startTime = aStartTime;
    endtime = aEndtime;
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setResult(boolean aResult)
  {
    boolean wasSet = false;
    result = aResult;
    wasSet = true;
    return wasSet;
  }

  public boolean setStartdate(Date aStartdate)
  {
    boolean wasSet = false;
    startdate = aStartdate;
    wasSet = true;
    return wasSet;
  }

  public boolean setStartTime(Time aStartTime)
  {
    boolean wasSet = false;
    startTime = aStartTime;
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

  public boolean getResult()
  {
    return result;
  }

  public Date getStartdate()
  {
    return startdate;
  }

  public Time getStartTime()
  {
    return startTime;
  }

  public Time getEndtime()
  {
    return endtime;
  }
  /* Code from template attribute_IsBoolean */
  public boolean isResult()
  {
    return result;
  }

  public void delete()
  {}


  public String toString()
  {
    return super.toString() + "["+
            "result" + ":" + getResult()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "startdate" + "=" + (getStartdate() != null ? !getStartdate().equals(this)  ? getStartdate().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "startTime" + "=" + (getStartTime() != null ? !getStartTime().equals(this)  ? getStartTime().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "endtime" + "=" + (getEndtime() != null ? !getEndtime().equals(this)  ? getEndtime().toString().replaceAll("  ","    ") : "this" : "null");
  }
}