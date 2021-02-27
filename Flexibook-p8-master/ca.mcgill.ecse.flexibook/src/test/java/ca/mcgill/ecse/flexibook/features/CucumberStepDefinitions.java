package ca.mcgill.ecse.flexibook.features;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import ca.mcgill.ecse.flexibook.application.FlexiBookApplication;
import ca.mcgill.ecse.flexibook.controller.FlexiBookController;
import ca.mcgill.ecse.flexibook.controller.InvalidInputException;
import ca.mcgill.ecse.flexibook.controller.TOAppointment;
import ca.mcgill.ecse.flexibook.controller.TOBusiness;
import ca.mcgill.ecse.flexibook.controller.TOViewAppointment;
import ca.mcgill.ecse.flexibook.model.*;
import ca.mcgill.ecse.flexibook.model.Appointment.State;
import ca.mcgill.ecse.flexibook.model.BusinessHour.DayOfWeek;
import ca.mcgill.ecse.flexibook.persistence.FlexiBookPersistence;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class CucumberStepDefinitions {
	
  private FlexiBook flexibook;
  private String error;
  private int errorCounter;
  private int numCustomer;
  private String oldUsername;
  private String oldPassword;
  private User account; 
  private Owner owner;
  private Business business;
  private String oldComboname;
  private int position;
  private String username;
  private String result = "be";
  private String removeResult = "not";
  private ArrayList<TOViewAppointment> Allitems = new ArrayList<TOViewAppointment>();
  private TOBusiness toBusiness;
  private TimeSlot vacation;
  private TimeSlot holiday;
  private int numOfAppointments = 0;
  private TOAppointment toAppointment;
  private boolean isSuccessfulUpdated;
  private Appointment aptmt;
  private static String filename = "testdata.flexibook";
  

	/**
	 * @author Cecilia Jiang
	 */
	@Before
	public static void setUp() {
		FlexiBookPersistence.setFilename(filename);
		// remove test file
		File f = new File(filename);
		f.delete();
		// clear all data
		FlexiBookApplication.getFlexiBook().delete();
	}
    /**
     * @author Jack Wei
     */
	@Given("a Flexibook system exists")
	public void a_flexibook_system_exists() {
		flexibook = FlexiBookApplication.getFlexiBook();
		error = "";
		errorCounter = 0;
	}
	
	 /**
     * @author Jack Wei
     */
	@Given("there is no existing username {string}")
	public void there_is_no_existing_username(String string) {
		for (Customer customer : FlexiBookApplication.getFlexiBook().getCustomers()) {
			if (customer.getUsername().equals(string)) {
				customer.delete();
				break;
			}
		}
	}
	
	
	/**
    * @author Jack Wei
    */
	@Given("there is an existing username {string}")
	public void there_is_an_existing_username(String string) { 
		if(string.equals("owner")) {
			Owner owner = new Owner("owner","owner",flexibook);
			flexibook.setOwner(owner);
		} else {
	       try {
	            FlexiBookController.signUp(string, "password");
	        } catch (InvalidInputException e) {
	            error += e.getMessage();
	            errorCounter++;
	        }	
		}
	}

	 /**
     * @author Jack Wei
     */
	@Given("the account with username {string} has pending appointments")
	public void the_account_with_username_has_pending_appointments(String string) {
	    Service washService = new Service("wash",flexibook,30,0,0);
	    Service cutService = new Service("cut", flexibook,30,0,0);
	    Date startDate1 = new Date(0);
	    Date endDate1 = new Date(100000);
	    Date startDate2 = new Date(200000);
	    Date endDate2 = new Date(300000);
	    Time startTime = new Time(0);
	    Time endTime = new Time(100000);
	    TimeSlot myTimeSlot1 = new TimeSlot(startDate1,startTime,endDate1,endTime, flexibook);
	    TimeSlot myTimeSlot2 = new TimeSlot(startDate2,startTime,endDate2,endTime, flexibook);
		Customer customer = findCustomer(string);
	    if(customer != null) {
			if (customer.getAppointments().size() == 0) {
			    Appointment myAppointment1 = new Appointment(customer,washService,myTimeSlot1, flexibook);
			    Appointment myAppointment2 = new Appointment(customer,cutService,myTimeSlot2, flexibook);
		    }
	    }

	}
	
	 /**
     * @author Jack Wei
     */
	@Given("an owner account exists in the system with username {string} and password {string}")
	public void an_owner_account_exists_in_the_system_with_username_and_password(String string, String string2) {
	    Owner owner = new Owner(string,string2,flexibook);
	    flexibook.setOwner(owner);
	}
	
	 /**
     * @author Jack Wei
     */
	@Given("the user is logged in to an account with username {string}")
	public void the_user_is_logged_in_to_an_account_with_username(String string) {
		if(string.equals("owner")){
			FlexiBookApplication.setCurrentUser(flexibook.getOwner());
		} else {
			Customer customer = findCustomer(string);
			FlexiBookApplication.setCurrentUser(customer);
		}
	}
	
	 /**
     * @author Jack Wei
     */
	@When("the user provides a new username {string} and a password {string}")
	public void the_user_provides_a_new_username_and_a_password(String string, String string2) {
        numCustomer = flexibook.getCustomers().size();
		try {
            FlexiBookController.signUp(string, string2);
            account = findCustomer(string);
        } catch (InvalidInputException e) {
            error += e.getMessage();
            errorCounter++;
        }	    
	}
	
	 /**
     * @author Jack Wei
     */
	@When("the user tries to update account with a new username {string} and password {string}")
	public void the_user_tries_to_update_account_with_a_new_username_and_password(String string, String string2) {
	    try {
	    	oldUsername = FlexiBookApplication.getCurrentUser().getUsername();
	    	oldPassword = FlexiBookApplication.getCurrentUser().getPassword();
	    	account = FlexiBookApplication.getCurrentUser();
	    	FlexiBookController.updateAccount(string, string2);
	    	account = FlexiBookApplication.getCurrentUser();
	    } catch (InvalidInputException e) {
            error += e.getMessage();
            errorCounter++;
        }	    
	}
	
	 /**
     * @author Jack Wei
     */
	@When("the user tries to delete account with the username {string}")
	public void the_user_tries_to_delete_account_with_the_username(String string) {
	    try {
	    	FlexiBookController.deleteAccount(string);
	    } catch (InvalidInputException e){
            error += e.getMessage();
            errorCounter++;
	    }
	}

	 /**
     * @author Jack Wei
     */
	@Then("the account shall not be updated")
	public void the_account_shall_not_be_updated() {
	    assertEquals (oldUsername, FlexiBookApplication.getCurrentUser().getUsername());
	    assertEquals (oldPassword, FlexiBookApplication.getCurrentUser().getPassword());
	}
	
	 /**
     * @author Jack Wei
     */
	@Then("a new customer account shall be created")
	public void a_new_customer_account_shall_be_created() {
	    assertEquals (numCustomer + 1, flexibook.getCustomers().size());
	}
	
	 /**
     * @author JackWei
     */
	@Then("the account shall have username {string} and password {string}")
	public void the_account_shall_have_username_and_password(String string, String string2) {
		assertEquals (string, account.getUsername());
		assertEquals (string2, account.getPassword());
	}
	
	 /**
     * @author Jack Wei
     */
	@Then("no new account shall be created")
	public void no_new_account_shall_be_created() {
		assertEquals (numCustomer, flexibook.getCustomers().size());
	}
	
	 /**
     * @author Jack Wei
     */
	@Then("an error message {string} shall be raised")
	public void an_error_message_shall_be_raised(String string) {
		assertTrue(error.contains(string));
	}

	 /**
     * @author JackWei
     */
	@Then("the account with the username {string} does not exist")
	public void the_account_with_the_username_does_not_exist(String string) {
	    assertNull(findCustomer(string));
	}

	 /**
     * @author Jack Wei
     */
	@Then("all associated appointments of the account with the username {string} shall not exist")
	public void all_associated_appointments_of_the_account_with_the_username_shall_not_exist(String string) {
	    Customer customer = findCustomer(string);
		assertEquals (0, flexibook.getAppointments().size());
	}

	 /**
     * @author Jack Wei
     */
	@Then("the user shall be logged out")
	public void the_user_shall_be_logged_out() {
	    assertNull(FlexiBookApplication.getCurrentUser());
	}

	 /**
     * @author Jack Wei
     */
	@Then("the account with the username {string} exists")
	public void the_account_with_the_username_exists(String string) {
		if(string.equals("owner")) {
		    assertTrue(flexibook.getOwner()!= null);
		} else {
			Customer customer = findCustomer(string);
		    assertTrue(customer!= null);
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
	
	
//	 /**
//     * @author Jiatong Niu
//     */
//	@Given("a business exists in the system")
//	public void a_business_exists_in_the_system() {
//		//Business business = new Business("1", "1", "1", "1", flexibook);
//		Business business = FlexiBookApplication.getFlexiBook().getBusiness();
//	}
	
//	  /**
//     * @author Jiatong Niu
//     */
//	@Given("the following services exist in the system:")
//	public void the_following_services_exist_in_the_system(io.cucumber.datatable.DataTable dataTable) {
//		List<String> name = dataTable.column(0);
//		List<String> duration = dataTable.column(1);
//		List<String> downtimeStart = dataTable.column(2);
//		List<String> downtimeDuration = dataTable.column(3);
//		for(int i=1; i<dataTable.height();i++) {
//				//FlexiBookController.addService(name.get(i), Integer.parseInt(duration.get(i)), Integer.parseInt(downtimeDuration.get(i)), Integer.parseInt(downtimeStart.get(i)));
//			 new Service(name.get(i),flexibook,Integer.parseInt(duration.get(i)),Integer.parseInt(downtimeDuration.get(i)),Integer.parseInt(downtimeStart.get(i)));
//		}
//	}

	 /**
     * @author Jiatong Niu
     */
	@When("{string} initiates the definition of a service combo {string} with main service {string}, services {string} and mandatory setting {string}")
	public void initiates_the_definition_of_a_service_combo_with_main_service_services_and_mandatory_setting(String string, String string2, String string3, String string4, String string5) {
	    try {
	    	FlexiBookController.defineServiceCombo(string, string2, string3, string4, string5);
	    }catch (InvalidInputException e) {
            error += e.getMessage();
            errorCounter++;
        }
	}
	 /**
     * @author Jiatong Niu
     */
	@Then("the service combo {string} shall exist in the system")
	public void the_service_combo_shall_exist_in_the_system(String string) {
		String n = "";
	    for(BookableService servicecombo:flexibook.getBookableServices()) {
	    	if(servicecombo.getName().equals(string)&&servicecombo instanceof ServiceCombo) {
	    		n =((ServiceCombo)servicecombo).getName();
	    	}
	    }
	    assertEquals(n,string);
	}
	 /**
     * @author Jiatong Niu
     */
	@Then("the service combo {string} shall contain the services {string} with mandatory setting {string}")
	public void the_service_combo_shall_contain_the_services_with_mandatory_setting(String string, String string2, String string3) {
		ServiceCombo sc = null;
		  for(BookableService servicecombo:flexibook.getBookableServices()) {
		    	if(servicecombo instanceof ServiceCombo && servicecombo.getName().equals(string)) {
		    		sc=(ServiceCombo) servicecombo;
		    	}
		    }
		  int i;
		  List<String> services = new ArrayList<String>(sc.getServices().size());
		  List<String> mandatories= new ArrayList<String>(sc.getServices().size());
		  for(i=0;i<services.size();i++) {
			  services.set(i, sc.getService(i).getService().getName());
		  }
		  for(i=0;i<services.size();i++) {
			  mandatories.set(i, String.valueOf(sc.getService(i).getMandatory()));
		  }
		 
		  List<String> servicesl =new ArrayList<String>();
		  List<String> mandatoriesl =new ArrayList<String>();
		  servicesl = toList(string2);
		  mandatoriesl = toList(string3);
		  assertTrue(Listsame(services,servicesl));
		  assertTrue(Listsame(mandatories,mandatoriesl));
		  
	}

	/**
	 * compare two list
	 * @param list - a list
	 * @param listl - a list
	 * @return boolean
	 * @author Jiatong Niu
	 */
	private boolean Listsame(List<String> list, List<String> listl) {
		int i=0;
		boolean same = true;
		for (;i<list.size();i++) {
			if(list.get(i).equals(listl.get(i))) {
				//do nothing
			}else {
				same=false;
				return same;
			}
		}
		return same;
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
     * @author Jiatong Niu
     */
	@Then("the main service of the service combo {string} shall be {string}")
	public void the_main_service_of_the_service_combo_shall_be(String string, String string2) {
		ServiceCombo sc = null;
		  for(BookableService servicecombo:flexibook.getBookableServices()) {
		    	if(servicecombo instanceof ServiceCombo && servicecombo.getName().equals(string)) {
		    		sc=(ServiceCombo) servicecombo;
		    	}
		    }
		  assertEquals(sc.getMainService().getService().getName(),string2);
	}
	 /**
     * @author Jiatong Niu
     */
	@Then("the service {string} in service combo {string} shall be mandatory")
	public void the_service_in_service_combo_shall_be_mandatory(String string, String string2) {
		ServiceCombo sc = null;
		  for(BookableService servicecombo:flexibook.getBookableServices()) {
		    	if(servicecombo instanceof ServiceCombo && servicecombo.getName().equals(string2)) {
		    		sc=(ServiceCombo) servicecombo;
		    	}
		    }
		  ComboItem citem = null;
		  for (ComboItem ci:sc.getServices()) {
			  if(ci.getService().getName().equals(string)) {
				  citem=ci;
			  }
		  }
		  
		  assertTrue(citem.isMandatory());
	}

//	 /**
//     * @author Jiatong Niu
//     */
//	@Given("the following service combos exist in the system:")
//	public void the_following_service_combos_exist_in_the_system(io.cucumber.datatable.DataTable dataTable) {
//		List<String> comboname = dataTable.column(0);
//		List<String> mainservicename = dataTable.column(1);
//		List<String> services = dataTable.column(2);
//		List<String> mandatories = dataTable.column(3);
//		for(int i=1; i<dataTable.height();i++) {
//			createServiceCombo(owner.getUsername(), comboname.get(i));
//			 String mainservice=mainservicename.get(i);
//			 List<String> servicesstring = toList(services.get(i));
//			 List<String> mandatoriesstring = toList(mandatories.get(i));
//			for(BookableService bs:flexibook.getBookableServices()) {
//				if(bs.getName().equals(comboname.get(i))&&(bs instanceof ServiceCombo)) {
//					ComboItem aNewMainService=new ComboItem(true,toService(flexibook,mainservice),(ServiceCombo)bs);
//					((ServiceCombo)bs).setMainService(aNewMainService);
//					
//					
//					for(int m=0;m<servicesstring.size();m++) {
//						if(servicesstring.get(m).equals(mainservice)) {
//							// do nothing
//						}else {
//						ComboItem ci = new ComboItem(toBool(mandatoriesstring.get(m)),toService(flexibook,servicesstring.get(m)),(ServiceCombo) bs);
//						}
//					}
//				}
//			}
//		}
//       	
//	}
	
	/**
	 * @author Lizhiwei
	 */
	@Given("the following service combos exist in the system:")
	public void the_following_service_combos_exist_in_the_system(io.cucumber.datatable.DataTable dataTable) {
		  List<String> names = dataTable.column(0);
		  List<String> mainservices = dataTable.column(1);
		  List<String> services = dataTable.column(2);
		  List<String> mandatory= dataTable.column(3);
		  int isMain;
		  for (int i=1;i<names.size();i++) {
		      ServiceCombo combo=new ServiceCombo(names.get(i), flexibook);
		      String[] subser = services.get(i).split(",");
		      String[] mans = mandatory.get(i).split(",");
		      for(int j=0;j<subser.length;j++) {
		       combo.addService((mans[j].equals("true")),findService(subser[j]));
		       if (subser[j].equals(mainservices.get(i))) {
		    	   combo.setMainService(combo.getService(j));
		       }
		      }
		      
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
	 * return service combo
	 * @param username - user name
	 * @param comboname - name of the combo we want to create
	 * @return ServiceCombo
	 * @author Jiatong Niu
	 */
	private void createServiceCombo(String username, String comboname) {
		new ServiceCombo(comboname,flexibook);
		
	}
	 /**
     * @author Jiatong Niu
     */
	@Then("an error message with content {string} shall be raised")
	public void an_error_message_with_content_shall_be_raised(String string) {
		System.out.println(error);
		assertTrue(error.contains(error));
	}
	 /**
     * @author Jiatong Niu
     */
	@Then("the service combo {string} shall not exist in the system")
	public void the_service_combo_shall_not_exist_in_the_system(String string) {
		String n = "";
		ServiceCombo sc= null;
	    for(BookableService servicecombo:flexibook.getBookableServices()) {
	    	if(servicecombo instanceof ServiceCombo && servicecombo.getName().equals(string)) {
	    		sc=(ServiceCombo) servicecombo;
	    	}
	    }
	    if(sc!=null) {
	    n=sc.getName();
	    }
	    assertEquals("",n);
	}
	 /**
     * @author Jiatong Niu
     */
	@Then("the service combo {string} shall preserve the following properties:")
	public void the_service_combo_shall_preserve_the_following_properties(String string, io.cucumber.datatable.DataTable dataTable) {
		List<String> comboname = dataTable.column(0);
		List<String> mainservice = dataTable.column(1);
		List<String> services = dataTable.column(2);
		List<String> mandatories = dataTable.column(3);
		for(BookableService servicecombo:flexibook.getBookableServices()) {
			if(servicecombo instanceof ServiceCombo && servicecombo.getName().equals(string)) {
				assertEquals(comboname.get(1), servicecombo.getName());
				assertEquals(mainservice.get(1),((ServiceCombo)servicecombo).getMainService().getService().getName());
				 int i;
				  List<String> servicesl = new ArrayList<String>(((ServiceCombo)servicecombo).getServices().size());
				  List<String> mandatoriesl= new ArrayList<String>(((ServiceCombo)servicecombo).getServices().size());
				  for(i=0;i<servicesl.size();i++) {
					  servicesl.set(i, ((ServiceCombo)servicecombo).getService(i).getService().getName());
				  }
				  for(i=0;i<servicesl.size();i++) {
					  mandatoriesl.set(i, String.valueOf(((ServiceCombo)servicecombo).getService(i).getMandatory()));
				  }
				  List<String> serviceslist =new ArrayList<String>();
				  List<String> mandatorieslist =new ArrayList<String>();
				  servicesl = toList(services.get(1));
				  mandatoriesl = toList(mandatories.get(1));
				  assertTrue(Listsame(serviceslist,servicesl));
				  assertTrue(Listsame(mandatorieslist,mandatoriesl));
				  
			}
		}
	}

	 /**
     * @author Jiatong Niu
     */
	@When("{string} initiates the update of service combo {string} to name {string}, main service {string} and services {string} and mandatory setting {string}")
	public void initiates_the_update_of_service_combo_to_name_main_service_and_services_and_mandatory_setting(String string, String string2, String string3, String string4, String string5, String string6) {
		try {
			oldComboname=string2;
			position = findPosition(flexibook.getBookableServices(),string2);
	    	FlexiBookController.updateServiceCombo(string, string2, string3, string4, string5,string6);
	    }catch (InvalidInputException e) {
            error += e.getMessage();
            errorCounter++;
        }
	}
	/**
	 * return position of combo with name string2 in bookableServices
	 * @param bookableServices - the list of bookableServices
	 * @param string2 - name of the combo we want to find
	 * @return position(int)
	 * @author Jiatong Niu
	 */
	private int findPosition(List<BookableService> bookableServices, String string2) {
		int position=-1;
		for(int i=0;i<bookableServices.size();i++) {
			if(string2.equals(bookableServices.get(i).getName())&&(bookableServices.get(i) instanceof ServiceCombo)) {
				position=i;
				return position;
			}
		}
		return position;
	}
	 /**
     * @author Jiatong Niu
     */
	@Then("the service combo {string} shall be updated to name {string}")
	public void the_service_combo_shall_be_updated_to_name(String string, String string2) {
		    assertEquals(oldComboname,string);
	  	    assertTrue(position!=-1);
	  	    ServiceCombo sc = (ServiceCombo) flexibook.getBookableService(position);
	  	    assertEquals(string2,sc.getName());
	}


    /**
     * @author Cecilia Jiang
     */
	@Given("an owner account exists in the system")
	public void an_owner_account_exists_in_the_system() {
	    // Write code here that turns the phrase above into concrete actions
	    if (User.getWithUsername("owner") == null){
	    	Owner owner = new Owner("owner", "owner", flexibook);

	    }
	    
	}
	
    /**
     * @author Cecilia Jiang
     */
	@Given("a business exists in the system")
	public void a_business_exists_in_the_system() {
	    // Write code here that turns the phrase above into concrete actions
		if (!flexibook.hasBusiness()) {
			Business business = new Business("1", "1", "1", "1", flexibook);

		}

	}
	
    /**
     * @author Cecilia Jiang
     */
	@Given("the Owner with username {string} is logged in")
	public void the_owner_with_username_is_logged_in(String string) {
	    // Write code here that turns the phrase above into concrete actions
	    FlexiBookApplication.setCurrentUser(owner);
	}
	
    /**
     * @author Cecilia Jiang
     */
	@Given("the following services exist in the system:")
	public void the_following_services_exist_in_the_system(io.cucumber.datatable.DataTable dataTable) {

		List<String> name = dataTable.column(0);
		List<String> duration = dataTable.column(1);
		List<String> downtimeStart = dataTable.column(2);
		List<String> downtimeDuration = dataTable.column(3);
		for(int i=1; i<dataTable.height();i++) {
			Service s = new Service(name.get(i), flexibook, Integer.parseInt(duration.get(i)), Integer.parseInt(downtimeDuration.get(i)), Integer.parseInt(downtimeStart.get(i)));
		}	
	}

    /**
     * @author Cecilia Jiang
     */
	@Given("the following customers exist in the system:")
	public void the_following_customers_exist_in_the_system(io.cucumber.datatable.DataTable dataTable) throws InvalidInputException {
		List<String> usernames = dataTable.column(0);
		List<String> passwords = dataTable.column(1);
		for(int i=1; i<dataTable.height();i++) {
			Customer u = new Customer(usernames.get(i), passwords.get(i),0,flexibook);
		}
	}

    /**
     * @author Cecilia Jiang
     */
	@Given("Customer with username {string} is logged in")
	public void customer_with_username_is_logged_in(String string) {
	    // Write code here that turns the phrase above into concrete actions
		Customer customer = findCustomer(string);
		FlexiBookApplication.setCurrentUser(customer);
	}
	
    /**
     * @author Cecilia Jiang
     */
	@When("{string} initiates the addition of the service {string} with duration {string}, start of down time {string} and down time duration {string}")
	public void initiates_the_addition_of_the_service_with_duration_start_of_down_time_and_down_time_duration(String owner, String name, String duration, String downtimeStart, String downtimeDuration) {
	    // Write code here that turns the phrase above into concrete actions
		try {
			if(flexibook.getOwner().getUsername().equals("owner"))
			FlexiBookController.addService(name, duration, downtimeDuration, downtimeStart);
		} catch (InvalidInputException e) {
			error += e.getMessage();
			errorCounter++;
		}
	}
	
    /**
     * @author Cecilia Jiang
     */
	@Then("the service {string} shall exist in the system")
	public void the_service_shall_exist_in_the_system(String name) {
	    // Write code here that turns the phrase above into concrete actions
		String n = "";
	    for(BookableService service:flexibook.getBookableServices()) {
	    	if(service.getName().equals(name)) {
	    		n = service.getName();
	    	}
	    }
	    assertEquals(name, n);
	}
	
    /**
     * @author Cecilia Jiang
     */
	@Then("the service {string} shall have duration {string}, start of down time {string} and down time duration {string}")
	public void the_service_shall_have_duration_start_of_down_time_and_down_time_duration(String name, String duration, String downtimeStart, String downtimeDuration) {
	    // Write code here that turns the phrase above into concrete actions
		for(BookableService service:flexibook.getBookableServices()) {
			if(service instanceof Service&&service.getName().equals(name)) {
				assertEquals(Integer.parseInt(duration), ((Service) service).getDuration());
				assertEquals(Integer.parseInt(downtimeStart), ((Service) service).getDowntimeStart());
				assertEquals(Integer.parseInt(downtimeDuration), ((Service) service).getDowntimeDuration());
			}
		}
	}

    /**
     * @author Cecilia Jiang
     */
	@Then("the service {string} shall not exist in the system")
	public void the_service_shall_not_exist_in_the_system(String name) {
	    // Write code here that turns the phrase above into concrete actions
		String n = "";
	    for(BookableService service:flexibook.getBookableServices()) {
	    	if(service instanceof Service && service.getName().equals(name)) {
	    		n = service.getName();
	    	}
	    }
	    assertEquals("",n);
	}
	
    /**
     * @author Cecilia Jiang
     */
	@Then("the number of services in the system shall be {string}")
	public void the_number_of_services_in_the_system_shall_be(String num) {
	    // Write code here that turns the phrase above into concrete actions
	    int numOfServices = 0;
	    for(BookableService service: flexibook.getBookableServices()) {
	    	if(service instanceof Service) numOfServices++;
	    }
	    assertEquals(Integer.parseInt(num), numOfServices);
	}
	
    /**
     * @author Cecilia Jiang
     */
	@Then("the service {string} shall still preserve the following properties:")
	public void the_service_shall_still_preserve_the_following_properties(String serviceName, io.cucumber.datatable.DataTable dataTable) {
	    // Write code here that turns the phrase above into concrete actions
	    // For automatic transformation, change DataTable to one of
	    // E, List<E>, List<List<E>>, List<Map<K,V>>, Map<K,V> or
	    // Map<K, List<V>>. E,K,V must be a String, Integer, Float,
	    // Double, Byte, Short, Long, BigInteger or BigDecimal.
	    //
	    // For other transformations you can register a DataTableType.
		List<String> name = dataTable.column(0);
		List<String> duration = dataTable.column(1);
		List<String> downtimeStart = dataTable.column(2);
		List<String> downtimeDuration = dataTable.column(3);
		for(BookableService service:flexibook.getBookableServices()) {
			if(service instanceof Service && service.getName().equals(serviceName)) {
				assertEquals(name.get(1), service.getName());
				assertEquals(Integer.parseInt(duration.get(1)),((Service)service).getDuration());
				assertEquals(Integer.parseInt(downtimeStart.get(1)),((Service)service).getDowntimeStart());
				assertEquals(Integer.parseInt(downtimeDuration.get(1)),((Service)service).getDowntimeDuration());
			}
		}

	}
	
    /**
     * @author Cecilia Jiang
     */
	@When("{string} initiates the update of the service {string} to name {string}, duration {string}, start of down time {string} and down time duration {string}")
	public void initiates_the_update_of_the_service_to_name_duration_start_of_down_time_and_down_time_duration(String owner, String curName, String newName, String duration, String downtimeStart, String downtimeDuration) {
		try {
			if(flexibook.getOwner().getUsername().equals("owner")) {
				FlexiBookController.updateService(curName, newName, duration, downtimeDuration, downtimeStart);
			}
			
		} catch (InvalidInputException e) {
			error += e.getMessage();
			errorCounter++;
		}
	}

    /**
     * @author Cecilia Jiang
     */
	@Then("the service {string} shall be updated to name {string}, duration {string}, start of down time {string} and down time duration {string}")
	public void the_service_shall_be_updated_to_name_duration_start_of_down_time_and_down_time_duration(String curName, String newName, String duration, String downtimeStart, String downtimeDuration) {
	    // Write code here that turns the phrase above into concrete actions
	    for(BookableService service : flexibook.getBookableServices()) {
	    	if(service instanceof Service && service.getName().equals(newName)) {
				assertEquals(Integer.parseInt(duration), ((Service) service).getDuration());
				assertEquals(Integer.parseInt(downtimeStart), ((Service) service).getDowntimeStart());
				assertEquals(Integer.parseInt(downtimeDuration), ((Service) service).getDowntimeDuration());
	    	}
	    }
	}
	
    /**
     * @author Cecilia Jiang
     */
	@Given("the system's time and date is {string}")
	public void the_system_s_time_and_date_is(String systemTime){
		String date=systemTime.split("\\+")[0];
	    String time=systemTime.split("\\+")[1];
	    FlexiBookApplication.setCurrentDate(Date.valueOf(date));
	    FlexiBookApplication.setCurrentTime(Time.valueOf(time+":00"));
	}

    /**
     * @author Cecilia Jiang
     */
	@When("{string} initiates the deletion of service {string}")
	public void initiates_the_deletion_of_service(String owner, String service) throws Exception {
		try {
			//if(flexibook.getOwner().getUsername().equals("owner"))
			FlexiBookController.deleteService(service);
			
		} catch (InvalidInputException e) {
			error += e.getMessage();
			errorCounter++;
		}
	    
	}

    /**
     * @author Cecilia Jiang
     */
	@Then("the number of appointments in the system with service {string} shall be {string}")
	public void the_number_of_appointments_in_the_system_with_service_shall_be(String service, String numAppoint) {
	    // Write code here that turns the phrase above into concrete actions
		int num = 0;
		BookableService aBookableService = findService(service);
		if(aBookableService instanceof Service) {
		    for(Appointment appointment : flexibook.getAppointments()) {
		    	if(appointment.getBookableService() instanceof Service) {
		    		if(appointment.getBookableService().getName().equals(service))
		    		num++;
		    	}else {
		    		BookableService bc = appointment.getBookableService();
		    		ServiceCombo sc = (ServiceCombo) bc;
		    		for(ComboItem comboItem : sc.getServices()) {
		    			if(comboItem.getService().getName().equals(service)) num++;
		    		}
		    	}
		    }
		}else {
			for(Appointment appointment : flexibook.getAppointments()) {
				if(appointment.getBookableService().getName().equals(service)) {
					num++;
				}
			}
		}

	    assertEquals(Integer.parseInt(numAppoint),num);
	}


//    /**
//     * @author Cecilia Jiang
//     */
//	@Given("the following service combos exist in the system:")
//	public void the_following_service_combos_exist_in_the_system(io.cucumber.datatable.DataTable dataTable) {
//		List<String> names = dataTable.column(0);
//		List<String> mainservices = dataTable.column(1);
//		List<String> services = dataTable.column(2);
//		List<String> mandatory= dataTable.column(3);
//		for (int i=1;i<names.size();i++) {
//			ServiceCombo combo=new ServiceCombo(names.get(i), flexibook);
//		    combo.addService(true, findService(mainservices.get(i)));
//		    ComboItem ci = combo.getServices().get(0);
//		    combo.setMainService(ci);
//		    String[] subser = services.get(i).split(",");
//		    String[] mans = mandatory.get(i).split(",");
//		    for(int j=0;j<subser.length;j++) {
//		    	combo.addService((mans[j].equals("true")),findService(subser[j]));
//		    }
//		}
//	}
	

//    /**
//     * @author Cecilia Jiang
//     */
//	@Then("the service combo {string} shall not exist in the system")
//	public void the_service_combo_shall_not_exist_in_the_system(String string) {
//		String n = "";
//	    for(BookableService servicecombo:flexibook.getBookableServices()) {
//	    	if(servicecombo instanceof ServiceCombo && servicecombo.getName().equals(string)) {
//	    		n = servicecombo.getName();
//	    	}
//	    }
//	    assertEquals(n, "");
//	}

    /**
     * @author Cecilia Jiang
     */
	@Then("the service combos {string} shall not contain service {string}")
	public void the_service_combos_shall_not_contain_service(String serviceCombo, String service) {
	    // Write code here that turns the phrase above into concrete actions
		String hasService = "";
	    for(BookableService bookableService : FlexiBookApplication.getFlexiBook().getBookableServices()) {
	    	if(bookableService instanceof ServiceCombo && bookableService.getName().equals(serviceCombo)) {
	    		ServiceCombo sc = (ServiceCombo) bookableService;
	    		for(ComboItem ci:sc.getServices()) {
	    			if(ci.getService().getName().equals(service)) {
	    				hasService = ci.getService().getName();
	    			}
	    		}
	    	}
	    }
	    assertEquals("",hasService);
	}

    /**
     * @author Cecilia Jiang
     */
	@Then("the number of service combos in the system shall be {string}")
	public void the_number_of_service_combos_in_the_system_shall_be(String numSc) {
	    // Write code here that turns the phrase above into concrete actions
	    Integer num = 0;
	    for(BookableService sc:flexibook.getBookableServices()) {
	    	if(sc instanceof ServiceCombo) num++;
	    }
	    assertEquals(Integer.parseInt(numSc),num);
	}
	
    /**
     * @author Cecilia Jiang
     */
	@Then("the service combos {string} shall not exist in the system")
	public void the_service_combos_shall_not_exist_in_the_system(String name) {
	    // Write code here that turns the phrase above into concrete actions
		ServiceCombo sc = null;
	    for(BookableService serviceCombo : flexibook.getBookableServices()) {
	    	if(serviceCombo instanceof ServiceCombo && serviceCombo.getName().equals(name)) {
	    		sc = (ServiceCombo) serviceCombo;
	    	}
	    	
	    }
	    assertTrue(sc==null);
	}
	

	/**
	 * @author Grey Yuan
	 */
	@Given("no business exists")
	public void no_business_exists() {
		if (FlexiBookApplication.getFlexiBook().hasBusiness() == true) {
			FlexiBookApplication.getFlexiBook().getBusiness().delete();
		}
	}

	/**
	 * @author Grey Yuan
	 */
	@When("the user tries to set up the business information with new {string} and {string} and {string} and {string}")
	public void the_user_tries_to_set_up_the_business_information_with_new_and_and_and(String string, String string2,
			String string3, String string4) {
		try {
			username = FlexiBookApplication.getCurrentUser().getUsername();
			FlexiBookController.setUpBasicBusinessInfo(username, string, string2, string3, string4);
		} catch (InvalidInputException e) {
			error += e.getMessage();
			errorCounter++;
		}
		if (errorCounter != 0) {
			result = "not be";
		}
	}

	/**
	 * @author Grey Yuan
	 */
	@Then("a new business with new {string} and {string} and {string} and {string} shall {string} created")
	public void a_new_business_with_new_and_and_and_shall_created(String string, String string2, String string3,
			String string4, String string5) {
		if (flexibook.getBusiness() != null) {
			assertEquals(string, flexibook.getBusiness().getName());
			assertEquals(string2, flexibook.getBusiness().getAddress());
			assertEquals(string3, flexibook.getBusiness().getPhoneNumber());
			assertEquals(string4, flexibook.getBusiness().getEmail());
			assertEquals(string5, result);

		} else {
			assertEquals(string5, result);
		}

	}

	/**
	 * @author Grey Yuan
	 */
	@Then("an error message {string} shall {string} raised")
	public void an_error_message_shall_raised(String string, String string2) {
		System.out.println(error+"############################################" + string);
		assertTrue(error.contains(string));
		assertEquals(string2, invertErrorResult(result));
	}

	/**
	 * @author Grey Yuan
	 */
	@Given("a business exists with the following information:")
	public void a_business_exists_with_the_following_information(io.cucumber.datatable.DataTable dataTable) {
		List<String> names = dataTable.column(0);
		List<String> addresses = dataTable.column(1);
		List<String> phoneNumbers = dataTable.column(2);
		List<String> emails = dataTable.column(3);

		for (int i = 1; i < dataTable.height(); i++) {
			business = new Business(names.get(i), addresses.get(i), phoneNumbers.get(i), emails.get(i), flexibook);
			flexibook.setBusiness(business);
		}

	}

	/**
	 * @author Grey Yuan
	 */
	@Given("the business has a business hour on {string} with start time {string} and end time {string}")
	public void the_business_has_a_business_hour_on_with_start_time_and_end_time(String string, String string2,
			String string3) {
		Time startT = stringToTime(string2);
		Time endT = stringToTime(string3);
		DayOfWeek day = DayOfWeek.valueOf(string);
		BusinessHour aBusinessHour = new BusinessHour(day, startT, endT, flexibook);
		flexibook.getBusiness().addBusinessHour(aBusinessHour);

	}

	/**
	 * @author Grey Yuan
	 */
	@When("the user tries to add a new business hour on {string} with start time {string} and end time {string}")
	public void the_user_tries_to_add_a_new_business_hour_on_with_start_time_and_end_time(String string, String string2,
			String string3) {
		Time startT = stringToTime(string2);
		Time endT = stringToTime(string3);
		DayOfWeek day = DayOfWeek.valueOf(string);
		try {
			username = FlexiBookApplication.getCurrentUser().getUsername();
			FlexiBookController.setUpBusinessHour(username, day, startT, endT);
		} catch (InvalidInputException e) {
			error += e.getMessage();
			errorCounter++;
//		          result = "not be";
		}
		if (errorCounter != 0) {
			result = "not be";
		}

	}

	/**
	 * @author Grey Yuan
	 */
	@Then("a new business hour shall {string} created")
	public void a_new_business_hour_shall_created(String string) {
		assertEquals(string, result);
	}

	/**
	 * @author Grey Yuan
	 */
	@When("the user tries to access the business information")
	public void the_user_tries_to_access_the_business_information() {
		FlexiBookController.getBasicBusinessInfo();
      if (business != null) {
			toBusiness = new TOBusiness (business.getName(),business.getAddress(),business.getPhoneNumber(),business.getEmail());
		}
	}

	/**
	 * @author Grey Yuan
	 */
	@Then("the {string} and {string} and {string} and {string} shall be provided to the user")
	public void the_and_and_and_shall_be_provided_to_the_user(String string, String string2, String string3,
			String string4) {
		assertTrue(string.equals(toBusiness.getName()));
		assertTrue(string2.equals(toBusiness.getAddress()));
		assertTrue(string3.equals(toBusiness.getPhoneNumber()));
		assertTrue(string4.equals(toBusiness.getEmail()));
	}

	/**
	 * @author Grey Yuan
	 */
	@Given("a {string} time slot exists with start time {string} at {string} and end time {string} at {string}")
	public void a_time_slot_exists_with_start_time_at_and_end_time_at(String string, String string2, String string3,
			String string4, String string5) {
//		List<TimeSlot> holidays = flexibook.getBusiness().getHolidays();
//		List<TimeSlot> vacations = flexibook.getBusiness().getVacation();
		Time startT = stringToTime(string3);
		Time endT = stringToTime(string5);
		Date startD = stringToDate(string2);
		Date endD = stringToDate(string4);


		if (string.equalsIgnoreCase("vacation")) {
			vacation = new TimeSlot(startD,startT,endD,endT,flexibook);
			flexibook.getBusiness().addVacation(vacation);
			vacation.setEndDate(endD);
			vacation.setEndTime(endT);
			vacation.setStartDate(startD);
			vacation.setStartTime(startT);
//			for (TimeSlot vacation : vacations) {
//				vacation.setEndDate(endD);
//				vacation.setEndTime(endT);
//				vacation.setStartDate(startD);
//				vacation.setStartTime(startT);
//			}
		}
		if (string.equalsIgnoreCase("holiday")) {
			TimeSlot holiday = new TimeSlot(startD,startT,endD,endT,flexibook);
			flexibook.getBusiness().addHoliday(holiday);
//			for (TimeSlot holiday : holidays) {
//				holiday.setEndDate(endD);
//				holiday.setEndTime(endT);
//				holiday.setStartDate(startD);
//				holiday.setStartTime(startT);
//			}
			holiday.setEndDate(endD);
			holiday.setEndTime(endT);
			holiday.setStartDate(startD);
			holiday.setStartTime(startT);
		}
	}

	/**
	 * @author Grey Yuan
	 */
	@When("the user tries to add a new {string} with start date {string} at {string} and end date {string} at {string}")
	public void the_user_tries_to_add_a_new_with_start_date_at_and_end_date_at(String string, String string2,
			String string3, String string4, String string5) {
		Time startT = stringToTime(string3);
		Time endT = stringToTime(string5);
		Date startD = stringToDate(string2);
		Date endD = stringToDate(string4);
		try {
			username = FlexiBookApplication.getCurrentUser().getUsername();
			FlexiBookController.setUpTimeSlot(username, string, startD, endD, startT, endT);
		} catch (InvalidInputException e) {
			error += e.getMessage();
			System.out.println(error+"%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
			errorCounter++;
			result = "not be";
		}
	}

	/**
	 * @author Grey Yuan
	 */
	@Then("a new {string} shall {string} be added with start date {string} at {string} and end date {string} at {string}")
	public void a_new_shall_be_added_with_start_date_at_and_end_date_at(String string, String string2, String string3,
			String string4, String string5, String string6) {
		Time startT = stringToTime(string4);
		Time endT = stringToTime(string6);
		Date startD = stringToDate(string3);
		Date endD = stringToDate(string5);
		holiday = new TimeSlot(startD, startT, endD, endT, flexibook);
		flexibook.getBusiness().addHoliday(holiday);
		vacation = new TimeSlot(startD, startT, endD, endT, flexibook);
		flexibook.getBusiness().addVacation(vacation);
//		List<TimeSlot> vacations = flexibook.getBusiness().getVacation();
		if (string.equalsIgnoreCase("vacation")) {
//			for (TimeSlot vacation : vacations) {
//				if (vacation.getStartTime().equals(startT) && vacation.getEndTime().equals(endT)
//						&& vacation.getStartDate().equals(startD) && vacation.getEndDate().equals(endD)) {
//					System.out.println(vacation.toString());
//					System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
//
//					assertEquals(string, "vacation");
//					assertEquals(string2, result);
//					assertEquals(string3, vacation.getStartDate());
//					assertEquals(string4, vacation.getStartTime());
//					assertEquals(string5, vacation.getEndDate());
//					assertEquals(string6, vacation.getEndTime());
//				}
//			}
			assertEquals(string, "vacation");
			
			assertEquals(string2, result);
			assertEquals(string3, vacation.getStartDate().toString());
			assertEquals(string4, vacation.getStartTime().toString().substring(0,5));
			assertEquals(string5, vacation.getEndDate().toString());
			assertEquals(string6, vacation.getEndTime().toString().substring(0,5));
			
		}
		if (string.equalsIgnoreCase("holiday")) {
//			for (TimeSlot holiday : holidays) {
//				if (holiday.getStartTime().equals(startT) && holiday.getEndTime().equals(endT)
//						&& holiday.getStartDate().equals(startD) && holiday.getEndDate().equals(endD)) {
//					assertEquals(string, "vacation");
//					assertEquals(string2, result);
//					assertEquals(string3, holiday.getStartDate());
//					assertEquals(string4, holiday.getStartTime());
//					assertEquals(string5, holiday.getEndDate());
//					assertEquals(string6, holiday.getEndTime());
//				}
//			}
			assertEquals(string, "holiday");
			assertEquals(string2, result);
			assertEquals(string3, holiday.getStartDate().toString());
			assertEquals(string4, holiday.getStartTime().toString().substring(0,5));
			assertEquals(string5, holiday.getEndDate().toString());
			assertEquals(string6, holiday.getEndTime().toString().substring(0,5));
		}

	}

	/**
	 * @author Grey Yuan
	 */
	@When("the user tries to update the business information with new {string} and {string} and {string} and {string}")
	public void the_user_tries_to_update_the_business_information_with_new_and_and_and(String string, String string2,
			String string3, String string4) {
		try {
//			business = FlexiBookApplication.getFlexiBook().getBusiness();
//			username = FlexiBookApplication.getCurrentUser().getUsername();
//			FlexiBookController.setUpBasicBusinessInfo(username, string, string2, string3, string4);
//			business = FlexiBookApplication.getFlexiBook().getBusiness();
			username = FlexiBookApplication.getCurrentUser().getUsername();
			FlexiBookController.setUpBasicBusinessInfo(username, string, string2, string3, string4);
		} catch (InvalidInputException e) {
			error += e.getMessage();
			errorCounter++;
			result = "not be";
		}
	}

	/**
	 * @author Grey Yuan
	 */
	@Then("the business information shall {string} updated with new {string} and {string} and {string} and {string}")
	public void the_business_information_shall_updated_with_new_and_and_and(String string, String string2,
			String string3, String string4, String string5) {
		assertEquals(string2, business.getName());
		assertEquals(string3, business.getAddress());
		assertEquals(string4, business.getPhoneNumber());
		assertEquals(string5, business.getEmail());
		assertEquals(string, result);
	}

	/**
	 * @author Grey Yuan
	 */
	@When("the user tries to change the business hour {string} at {string} to be on {string} starting at {string} and ending at {string}")
	public void the_user_tries_to_change_the_business_hour_at_to_be_on_starting_at_and_ending_at(String string,
			String string2, String string3, String string4, String string5) {
		Time startT = stringToTime(string2);
		Time newStartT = stringToTime(string4);
		Time newEndT = stringToTime(string5);
		DayOfWeek newDay = DayOfWeek.valueOf(string3);
		DayOfWeek oldDay = DayOfWeek.valueOf(string);
		try {
			username = FlexiBookApplication.getCurrentUser().getUsername();
			FlexiBookController.updateBusinessHour(username, oldDay, startT, newDay, newStartT, newEndT);
		} catch (InvalidInputException e) {
			error += e.getMessage();
			errorCounter++;
			result = "not be";
		}
	}

	/**
	 * @author Grey Yuan
	 */
	@Then("the business hour shall {string} be updated")
	public void the_business_hour_shall_be_updated(String string) {
		assertEquals(string, result);
	}

	/**
	 * @author Grey Yuan
	 */
	@When("the user tries to remove the business hour starting {string} at {string}")
	public void the_user_tries_to_remove_the_business_hour_starting_at(String string, String string2) {
		Time startT = stringToTime(string2);
		DayOfWeek oldDay = DayOfWeek.valueOf(string);
		try {
			username = FlexiBookApplication.getCurrentUser().getUsername();
			FlexiBookController.removeBusinessHour(username, oldDay, startT);
		} catch (InvalidInputException e) {
			error += e.getMessage();
			errorCounter++;
			removeResult = "";
		}
	}

	/**
	 * @author Grey Yuan
	 */
	@Then("the business hour starting {string} at {string} shall {string} exist")
	public void the_business_hour_starting_at_shall_exist(String string, String string2, String string3) {
		assertEquals(string3, removeResult);
		Time startT = stringToTime(string2);
		FlexiBook flexibook = FlexiBookApplication.getFlexiBook();
		List<BusinessHour> businessHours = flexibook.getBusiness().getBusinessHours();
		if (businessHours.size() > 0) {
			for (BusinessHour bH : businessHours) {
				assertTrue(!bH.getDayOfWeek().equals(DayOfWeek.valueOf(string)) || !bH.getStartTime().equals(startT));
			}
		}
	}

	/**
	 * @author Grey Yuan
	 */
	@Then("an error message {string} shall {string} be raised")
	public void an_error_message_shall_be_raised(String string, String string2) {
		assertTrue(error.contains(string));
		assertEquals(string2, removeResult);
	}

	/**
	 * @author Grey Yuan
	 */
	@When("the user tries to change the {string} on {string} at {string} to be with start date {string} at {string} and end date {string} at {string}")
	public void the_user_tries_to_change_the_on_at_to_be_with_start_date_at_and_end_date_at(String string,
			String string2, String string3, String string4, String string5, String string6, String string7) {
		Date startD = stringToDate(string2);
		Time startT = stringToTime(string3);
		Date newStartD = stringToDate(string4);
		Time newStartT = stringToTime(string5);
		Date newEndD = stringToDate(string6);
		Time newEndT = stringToTime(string7);
		try {
			username = FlexiBookApplication.getCurrentUser().getUsername();
			if (string.equalsIgnoreCase("holiday")) {
				FlexiBookController.updateHoliday(username, startD, startT, newStartD, newStartT, newEndD, newEndT);
			}
			if (string.equalsIgnoreCase("vacation")) {
				FlexiBookController.updateVacation(username, startD, startT, newStartD, newStartT, newEndD, newEndT);
			}
		} catch (InvalidInputException e) {
			error += e.getMessage();
			errorCounter++;
			result = "not be";
		}
	}

	/**
	 * @author Grey Yuan
	 */
	@Then("the {string} shall {string} updated with start date {string} at {string} and end date {string} at {string}")
	public void the_shall_updated_with_start_date_at_and_end_date_at(String string, String string2, String string3,
			String string4, String string5, String string6){
		Time startT = stringToTime(string4);
		Time endT = stringToTime(string6);
		Date startD = stringToDate(string3);
		Date endD = stringToDate(string5);
		holiday = new TimeSlot(startD, startT, endD, endT, flexibook);
		flexibook.getBusiness().addHoliday(holiday);
		vacation = new TimeSlot(startD, startT, endD, endT, flexibook);
		flexibook.getBusiness().addVacation(vacation);
//		List<TimeSlot> vacations = flexibook.getBusiness().getVacation();
		if (string.equalsIgnoreCase("vacation")) {
//			for (TimeSlot vacation : vacations) {
//				if (vacation.getStartTime().equals(startT) && vacation.getEndTime().equals(endT)
//						&& vacation.getStartDate().equals(startD) && vacation.getEndDate().equals(endD)) {
//					System.out.println(vacation.toString());
//					System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
//
//					assertEquals(string, "vacation");
//					assertEquals(string2, result);
//					assertEquals(string3, vacation.getStartDate());
//					assertEquals(string4, vacation.getStartTime());
//					assertEquals(string5, vacation.getEndDate());
//					assertEquals(string6, vacation.getEndTime());
//				}
//			}
			assertEquals(string, "vacation");
			
			assertEquals(string2, result);
			assertEquals(string3, vacation.getStartDate().toString());
			assertEquals(string4, vacation.getStartTime().toString().substring(0,5));
			assertEquals(string5, vacation.getEndDate().toString());
			assertEquals(string6, vacation.getEndTime().toString().substring(0,5));
			
		}
		if (string.equalsIgnoreCase("holiday")) {
//			for (TimeSlot holiday : holidays) {
//				if (holiday.getStartTime().equals(startT) && holiday.getEndTime().equals(endT)
//						&& holiday.getStartDate().equals(startD) && holiday.getEndDate().equals(endD)) {
//					assertEquals(string, "vacation");
//					assertEquals(string2, result);
//					assertEquals(string3, holiday.getStartDate());
//					assertEquals(string4, holiday.getStartTime());
//					assertEquals(string5, holiday.getEndDate());
//					assertEquals(string6, holiday.getEndTime());
//				}
//			}
			assertEquals(string, "holiday");
			assertEquals(string2, result);
			assertEquals(string3, holiday.getStartDate().toString());
			assertEquals(string4, holiday.getStartTime().toString().substring(0,5));
			assertEquals(string5, holiday.getEndDate().toString());
			assertEquals(string6, holiday.getEndTime().toString().substring(0,5));
		}
//		Date startD = stringToDate(string3);
//		Time startT = stringToTime(string4);
//		Date endD = stringToDate(string5);
//		Time endT = stringToTime(string6);
//		FlexiBook flexibook = FlexiBookApplication.getFlexiBook();
//		if (string.equals("holiday")) {
//			List<TimeSlot> holidays = flexibook.getBusiness().getHolidays();
//			if (holidays.size() > 0) {
//				for (TimeSlot holiday : holidays) {
//					if (holiday.getStartDate().equals(startD) && holiday.getStartTime().equals(startT)
//							&& holiday.getEndDate().equals(endD) && holiday.getEndTime().equals(endT)) {
//						assertTrue(string.equals("holiday"));
//						assertEquals(string2, result);
//						assertEquals(string3, holiday.getStartDate().toString());
//						assertEquals(string4, holiday.getStartTime().toString().substring(0,5));
//						assertEquals(string5, holiday.getEndDate().toString());
//						assertEquals(string6, holiday.getEndTime().toString().substring(0,5));
//					}
//				}
//			}
//		}
//		if (string.equals("vacation")) {
//			List<TimeSlot> vacations = flexibook.getBusiness().getHolidays();
//			if (vacations.size() > 0) {
//				for (TimeSlot vacation : vacations) {
//					if (vacation.getStartDate().equals(startD) && vacation.getStartTime().equals(startT)
//							&& vacation.getEndDate().equals(endD) && vacation.getEndTime().equals(endT)) {
//						assertTrue(string.equals("holiday"));
//						assertEquals(string2, result);
//						assertEquals(string3, vacation.getStartDate().toString());
//						assertEquals(string4, vacation.getStartTime().toString().substring(0,5));
//						assertEquals(string5, vacation.getEndDate().toString());
//						assertEquals(string6, vacation.getEndTime().toString().substring(0,5));
//					}
//				}
//			}
//		}
	}

	
	/**
	 * @author Grey Yuan
	 */
	@When("the user tries to remove an existing {string} with start date {string} at {string} and end date {string} at {string}")
	public void the_user_tries_to_remove_an_existing_with_start_date_at_and_end_date_at(String string, String string2,
			String string3, String string4, String string5) throws InvalidInputException, java.text.ParseException {
		Date startD = stringToDate(string2);
		Time startT = stringToTime(string3);
		Date endD = stringToDate(string4);
		Time endT = stringToTime(string5);
		try {
			username = FlexiBookApplication.getCurrentUser().getUsername();
			FlexiBookController.removeTimeSlot(username, string, startD, endD, startT, endT);
		} catch (InvalidInputException e) {
			error += e.getMessage();
			errorCounter++;
			removeResult = "";
		}
	}

	/**
	 * @author Grey Yuan
	 */
	@Then("the {string} with start date {string} at {string} shall {string} exist")
	public void the_with_start_date_at_shall_exist(String string, String string2, String string3, String string4)
			throws InvalidInputException, java.text.ParseException {
		assertEquals(string4, removeResult);
		Date startD = stringToDate(string2);
		Time startT = stringToTime(string3);
		FlexiBook flexibook = FlexiBookApplication.getFlexiBook();
		List<TimeSlot> holidays = flexibook.getBusiness().getHolidays();
		List<TimeSlot> vacations = flexibook.getBusiness().getVacation();
		if (vacations.size() > 0) {
			if (string.equalsIgnoreCase("vacation")) {
				for (TimeSlot vacation : vacations) {
					assertTrue(!vacation.getStartDate().equals(startD) || !vacation.getStartTime().equals(startT));
				}
			}
		}
		if (holidays.size() > 0) {
			if (string.equalsIgnoreCase("holiday")) {
				for (TimeSlot holiday : holidays) {
					assertTrue(!holiday.getStartDate().equals(startD) || !holiday.getStartTime().equals(startT));
				}
			}
		}
	}
	
	/**
	 * @author lizhiwei
	 *
	 */
	@When("the user tries to log in with username {string} and password {string}")
	public void the_user_tries_to_log_in_with_username_and_password(String string, String string2) {
	    // Write code here that turns the phrase above into concrete actions
		try {
			if(string.equals("owner")) {
				Owner owner = new Owner(string,string2,flexibook);
				flexibook.setOwner(owner);
			}
			FlexiBookController.Login(string, string2);
			account = FlexiBookApplication.getCurrentUser();
		} catch (InvalidInputException e) {
			error += e.getMessage();
           
		}
	}
	
	/**
	 * @author lizhiwei
	 *
	 */
	@Then("the user should be successfully logged in")
	public void the_user_should_be_successfully_logged_in() {
	    // Write code here that turns the phrase above into concrete actions
		assertEquals(false,(FlexiBookApplication.getCurrentUser()==null));
	}
	
	/**
	 * @author lizhiwei
	 *
	 */
	@Then("the user should not be logged in")
	public void the_user_should_not_be_logged_in() {
	    // Write code here that turns the phrase above into concrete actions
	    assertEquals(true,(FlexiBookApplication.getCurrentUser()==null));
	}
	
	/**
	 * @author lizhiwei
	 *
	 */
	@Then("a new account shall be created")
	public void a_new_account_shall_be_created() {
	    // Write code here that turns the phrase above into concrete actions
		 assertEquals(true,(flexibook.hasOwner()));
	}
	
	/**
	 * @author lizhiwei
	 *
	 */
	@Then("the user shall be successfully logged in")
	public void the_user_shall_be_successfully_logged_in() {
	    // Write code here that turns the phrase above into concrete actions
		assertEquals(false,(FlexiBookApplication.getCurrentUser()==null));
	}
	
	/**
	 * @author lizhiwei
	 *
	 */
	@Given("the user is logged out")
	public void the_user_is_logged_out() {
	    // Write code here that turns the phrase above into concrete actions
	    FlexiBookApplication.setCurrentUser(null);
	}
	/**
	 * @author lizhiwei
	 *
	 */
	@When("the user tries to log out")
	public void the_user_tries_to_log_out() {
	    // Write code here that turns the phrase above into concrete actions
	    try {
			FlexiBookController.LogOut();
		} catch (InvalidInputException e) {
			error+=e.getMessage();
		}
	}

	
	@Given("the following appointments exist in the system:")
	 public void the_following_appointments_exist_in_the_system(io.cucumber.datatable.DataTable dataTable) {
		List<Map<String, String>> maps = dataTable.asMaps(String.class, String.class);
		ArrayList<Boolean> formValidationResults = new ArrayList<Boolean>();
		for (Map<String, String> row : maps) {
			  String customer = row.get("customer");
			  String serviceName = row.get("serviceName");
			  String selectedComboItems = row.get("selectedComboItems");	//can be null for service and appointment calendat
			  String optServices = row.get("optServices");		//can be null for service and service combo
			  String date = row.get("date");
			  String startTime = row.get("startTime");
			  String endTime = row.get("endTime");
			  // convert date, startTime and endTime into appropriate format
			  Date d = Date.valueOf(date);
			  Time st = Time.valueOf(startTime+":00");
			  Time et = Time.valueOf(endTime+":00");
			  //add different types of appintments according to the dataTable
			  FlexiBookApplication.setCurrentUser(findCustomer(customer));
			  Customer aCustomer = (Customer) FlexiBookApplication.getCurrentUser();
			  BookableService bc = findBookableService(serviceName);
			  Appointment aAppointment = aCustomer.addAppointment(bc, new TimeSlot(d, st, d, et, FlexiBookApplication.getFlexiBook()),FlexiBookApplication.getFlexiBook());
			  if(bc instanceof Service) {
				
			  }else if(bc instanceof ServiceCombo) {
				  String[] allSelectedComboItems = null;
				  String[] optComboItems = null;
				  List<ComboItem> c = ((ServiceCombo) bc).getServices();
				  if(selectedComboItems!=null) {
					  allSelectedComboItems = selectedComboItems.split(",");
				  }else if(optServices!=null) {
					  optComboItems = optServices.split(",");
				  }
				  if(optComboItems!=null) {
					  for(ComboItem ci:c) {		
						  if(ci.isMandatory()) {
					    	aAppointment.addChosenItem(((ServiceCombo)findServiceCombo(serviceName)).getMainService());
					    }
						    for(int j=0;j<optComboItems.length;j++) {

							    if(ci.getService().getName().equals(optComboItems[j])) {
							    	aAppointment.addChosenItem(ci);
						    	}
						    }
					  }
				  }
				  if(allSelectedComboItems!=null) {
					  for(ComboItem ci:c) {
						    for(int j=0;j<allSelectedComboItems.length;j++) {
							    if(ci.getService().getName().equals(allSelectedComboItems[j])) {
							    	aAppointment.addChosenItem(ci);
						    	}
						    }
					  }
				  }
				  
				  
				  
			  }
			  
			
		}
		
		numOfAppointments = FlexiBookApplication.getFlexiBook().getAppointments().size();
		
	}
	


	/**
	 * @author lizhiwei
	 *
	 */

	@Given("{string} is logged in to their account")
	public void is_logged_in_to_their_account(String string) {
	    // Write code here that turns the phrase above into concrete actions
	    FlexiBookApplication.setCurrentUser(findCustomer(string));
	}
	/**
	 * @author lizhiwei
	 *
	 */
	@When("{string} requests the appointment calendar for the week starting on {string}")
	public void requests_the_appointment_calendar_for_the_week_starting_on(String string, String string2) {
	    try {
	    	Allitems=(ArrayList<TOViewAppointment>) FlexiBookController.ViewAppointment_At_Week(string2);
		} catch (InvalidInputException e) {
			// TODO Auto-generated catch block
			error+=e.getMessage();
		}
	}
	/**
	 * @author lizhiwei
	 *
	 */
	@Then("the following slots shall be unavailable:")
	public void the_following_slots_shall_be_unavailable(io.cucumber.datatable.DataTable dataTable) {
		List<String> dates = dataTable.column(0);
		List<String> starttimes= dataTable.column(1);
		List<String> endtimes= dataTable.column(2);
//		ArrayList<TOViewAppointment> Tester=new ArrayList<TOViewAppointment>();
//		Tester=Allitems;			
		int i=1;
		for(TOViewAppointment ap:Allitems) {
			
				if (ap.getIsAvailable()==false) {
					Date date = stringToDate(dates.get(i));
				Time starttime=stringToTime(starttimes.get(i));
				Time endtime=stringToTime(endtimes.get(i));
				assertTrue(ap.getDate().equals(date));
				assertTrue(ap.getStarttime().equals(starttime));
				assertTrue(ap.getEndtime().equals(endtime));
				i++;
				}	
		}		
		assertEquals(i,dates.size());

	}
	
	/**
	 * @author lizhiwei
	 *
	 */
	@Then("the following slots shall be available:")
	public void the_following_slots_shall_be_available(io.cucumber.datatable.DataTable dataTable) {
		List<String> dates = dataTable.column(0);
		List<String> starttimes= dataTable.column(1);
		List<String> endtimes= dataTable.column(2);
//		ArrayList<TOViewAppointment> Tester=new ArrayList<TOViewAppointment>();
//		Tester=Allitems;			
		int i=1;
		for(TOViewAppointment ap:Allitems) {
			
				if (ap.getIsAvailable()==true) {
					Date date = stringToDate(dates.get(i));
				Time starttime=stringToTime(starttimes.get(i));
				Time endtime=stringToTime(endtimes.get(i));
				assertTrue(ap.getDate().equals(date));
				assertTrue(ap.getStarttime().equals(starttime));
				assertTrue(ap.getEndtime().equals(endtime));
				i++;
				}	
		}		
		assertEquals(i,dates.size());
	
	}
	/**
	 * @author lizhiwei
	 *
	 */
	@When("{string} requests the appointment calendar for the day of {string}")
	public void requests_the_appointment_calendar_for_the_day_of(String string, String string2) {
		try {
			Allitems=FlexiBookController.ViewAppointment_At_date(string2);
			
		} catch (InvalidInputException e) {
			error+=e.getMessage();
		}
	}
	/**
	 * @author lizhiwei
	 *
	 */
	@Then("the system shall report {string}")
	public void the_system_shall_report(String string) {
		assertTrue(error.contains(string));
	}
	 /**
     * @author Jack Wei
     */
    @After
    public void tearDown() {
    	flexibook.delete();
    	FlexiBookApplication.setCurrentUser(null);
    	aptmt = null;
    	result = "be";
    	removeResult = "not";
    	numCustomer = 0;
    	error = "";
    	numOfAppointments = 0;
    	

    }
    

	 /**
     * @author Jiatong Niu
     */
	@When("{string} initiates the deletion of service combo {string}")
	public void initiates_the_deletion_of_service_combo(String string, String string2) {
		try {
	    	FlexiBookController.deleteServiceCombo(string, string2);;
	    }catch (InvalidInputException e) {
            error += e.getMessage();
            errorCounter++;
        }
	}


    /**
     * @author Jiatong Niu
     */
	@Then("the number of appointments in the system shall be {string}")
	public void the_number_of_appointments_in_the_system_shall_be(String string) {
	    int i = flexibook.getAppointments().size();
	    assertEquals(i,Integer.parseInt(string));
	}
	

	/**
	 * @author Zhiwei Li
	 *
	 */
	@Given("the business has the following opening hours:")
	public void the_business_has_the_following_opening_hours(io.cucumber.datatable.DataTable dataTable) {
		List<String> day = dataTable.column(0);
		List<String> startTime = dataTable.column(1);
		List<String> endTime = dataTable.column(2);
		for (int i=1;i<day.size();i++) {
			Time startT= stringToTime(startTime.get(i));
			Time endT= stringToTime(endTime.get(i));
			for (DayOfWeek aday:BusinessHour.DayOfWeek.values()) {
				if (aday.toString().equals(day.get(i))) {
					BusinessHour aBusinessHour=new BusinessHour(aday, startT, endT,flexibook);
					flexibook.getBusiness().addBusinessHour(aBusinessHour);
			
				}
			}
	    
		}
	}
	/**
	 * @author lizhiwei
	 *
	 */
	@Given("the business has the following holidays:")
	public void the_business_has_the_following_holidays(io.cucumber.datatable.DataTable dataTable) {
		List<String> startdates = dataTable.column(0);
		List<String> enddates = dataTable.column(1);
		List<String> starttimes = dataTable.column(2);
		List<String> endtimes= dataTable.column(3);
		for (int i=1; i<startdates.size();i++) {
			Date enddate=stringToDate(enddates.get(i));
			Date startdate=stringToDate(startdates.get(i));
			Time startT= stringToTime(starttimes.get(i));
			Time endT= stringToTime(endtimes.get(i));
			
				TimeSlot holi=new TimeSlot(startdate,startT,enddate,endT,flexibook);
				flexibook.getBusiness().addHoliday(holi);
				}
		
	}
	/**
	 * @author Cecilia Jiang
	 */
	@When("{string} schedules an appointment on {string} for {string} at {string}")
	public void schedules_an_appointment_on_for_at(String customer, String date, String serviceName, String startTime) throws InvalidInputException, ParseException {
	   
	    Date aDate = stringToDate(date);
	    Time aTime = stringToTime(startTime);
	    User aUser = null;
	    if(customer.equals("owner")) {
	        aUser = flexibook.getOwner();
	       }else {
	        aUser = findCustomer(customer);
	       }
	    FlexiBookApplication.setCurrentUser(aUser);
		
		try {
		FlexiBookController.makeAppointment(serviceName, aDate, aTime);
		}catch(InvalidInputException e) {
			error+=e.getMessage();
			errorCounter++;
		}
		
	    
	}

	/**
	 * @author Cecilia Jiang
	 */
	@Then("{string} shall have a {string} appointment on {string} from {string} to {string}")
	public void shall_have_a_appointment_on_from_to(String customer, String serviceName, String date, String startTime, String endTime) {
	    List<Customer> customers = FlexiBookApplication.getFlexiBook().getCustomers();
	    Appointment aAppointment = null;
	    Date aDate = stringToDate(date);
	    Time aStartTime = stringToTime(startTime);
	    Time aEndTime = stringToTime(endTime);
	    for(Customer c:customers) {
	    	if(c.getUsername().equals(customer)) {
	    		List<Appointment> appointments = c.getAppointments();
	    		for(Appointment a:appointments) {
	    			if(a.getBookableService().getName().equals(serviceName) && a.getTimeSlot().getStartDate().equals(aDate)
	    					&& a.getTimeSlot().getEndTime().equals(aEndTime)&&a.getTimeSlot().getStartTime().equals(aStartTime)) {
	    				aAppointment = a;
	    			}
	    		}
	    	}
	    }
	    assertTrue(aAppointment!=null);

	}

	/**
	 * @author Cecilia Jiang
	 */
	@Then("there shall be {int} more appointment in the system")
	public void there_shall_be_more_appointment_in_the_system(Integer num) {
	    assertEquals(num,FlexiBookApplication.getFlexiBook().getAppointments().size()-numOfAppointments);
	}

	@When("{string} schedules an appointment on {string} for {string} with {string} at {string}")
	public void schedules_an_appointment_on_for_with_at(String user, String date, String serviceName, String optionalServices, String startTime) throws InvalidInputException{
	    User aUser = null;
		if(user.equals("owner")) {
	    	aUser = flexibook.getOwner();
	    }else {
	    	aUser = findCustomer(user);
	    }
		
	    Date aDate = stringToDate(date);
	    Time aTime = stringToTime(startTime);
	    FlexiBookApplication.setCurrentUser(aUser);
	    try {
	    	 
	 		FlexiBookController.makeAppointment(serviceName, aDate, aTime, optionalServices);
	    }catch(InvalidInputException e) {
			error += e.getMessage();
			errorCounter++;
	    }
	   
		
	}

	/**
	 * @author Cecilia Jiang
	 */
	@When("{string} attempts to update their {string} appointment on {string} at {string} to {string} at {string}")
	 public void attempts_to_update_their_appointment_on_at_to_at(String user,String serviceName, String curDate, String curTime, String newDate, String newTime) {
	     User aUser = null;
	  if(user.equals("owner")) {
	      aUser = flexibook.getOwner();
	     }else {
	      aUser = findCustomer(user);
	     }
	     Date aCurDate = stringToDate(curDate);
	     Time aCurTime = stringToTime(curTime);
	     Date aNewDate = stringToDate(newDate);
	     Time aNewTime = stringToTime(newTime);
	  FlexiBookApplication.setCurrentUser(aUser);
	  try {
	   toAppointment = FlexiBookController.updateAppointment(serviceName, aCurDate, aCurTime, aNewDate, aNewTime);
	  }catch(InvalidInputException e) {
	   error += e.getMessage();
	   errorCounter++;
	  }
	  

	 }

	/**
	 * @author Cecilia Jiang
	 */
	@Then("the system shall report that the update was {string}")
	 public void the_system_shall_report_that_the_update_was(String isSuccessful) {
	     if(toAppointment.getResult()){
	      assertEquals(isSuccessful,"successful");
	     }else {
	      assertEquals(isSuccessful,"unsuccessful");
	     }
	 }

	/**
	 * @author Cecilia Jiang
	 */
	@Given("{string} has a {string} appointment with optional sevices {string} on {string} at {string}")
	 public void has_a_appointment_with_optional_sevices_on_at(String user, String serviceName, String optionalServices, String startDate, String startTime) {
	     User aUser = null;
	  if(user.equals("owner")) {
	      aUser = flexibook.getOwner();
	     }else {
	      aUser = findCustomer(user);
	     }
	  
	  ServiceCombo aServiceCombo = findServiceCombo(serviceName);
	  String[] optionalComboItems = null;
	     Date aDate = stringToDate(startDate);
	     //String stringEndTime = startTime;
	     Time aStartTime = stringToTime(startTime);
	     Time aEndTime = stringToTime(startTime);
	     List<ComboItem> comboItems = new ArrayList<ComboItem>();

	  if(optionalServices!=null) {
	   optionalComboItems = optionalServices.split(",");
	  }
	  for(ComboItem aComboItem:aServiceCombo.getServices()) {
	   if(aComboItem.isMandatory()) {
	    comboItems.add(aComboItem);
	    int duration = aComboItem.getService().getDuration();
	    aEndTime = addDuration(aEndTime,duration);
	   }
	   for(int i=0; i<optionalComboItems.length;i++) {
	    if(optionalComboItems[i].equals(aComboItem.getService().getName())) {
	     comboItems.add(aComboItem);
	     int duration = aComboItem.getService().getDuration();
	     aEndTime = addDuration(aEndTime,duration);
	    }
	   }
	  }

	     if(aUser instanceof Customer) {
	      Appointment aAppointment = new Appointment((Customer)aUser, aServiceCombo, new TimeSlot(aDate, aStartTime, aDate, aEndTime, flexibook), flexibook);
	      for(ComboItem aComboItem:comboItems) {
	       aAppointment.addChosenItem(aComboItem);
	      }
	      ((Customer) aUser).addAppointment(aAppointment);
	     }
	     numOfAppointments+=1;
	     
	 }
	/**
	 * @author Cecilia Jiang
	 */
	@When("{string} attempts to {string} {string} from their {string} appointment on {string} at {string}")
	 public void attempts_to_from_their_appointment_on_at(String user, String action, String comboItem, String serviceName, String curDate, String curTime) {
	     User aUser = null;
	  if(user.equals("owner")) {
	      aUser = flexibook.getOwner();
	     }else {
	      aUser = findCustomer(user);
	     }
	     Date aCurDate = stringToDate(curDate);
	     Time aCurTime = stringToTime(curTime);
	  FlexiBookApplication.setCurrentUser(aUser);
	  try {
	   toAppointment = FlexiBookController.updateAppointment(action, comboItem, serviceName, aCurDate, aCurTime);
	  }catch(InvalidInputException e) {
	   error += e.getMessage();
	   errorCounter++;
	  }
	 }
	
	/**
	 * @author Cecilia Jiang
	 */
	@When("{string} attempts to update {string}'s {string} appointment on {string} at {string} to {string} at {string}")
	 public void attempts_to_update_s_appointment_on_at_to_at(String user, String customer, String serviceName, String curDate, String curTime, String newDate, String newTime) {
	     User aUser = null;
	  if(user.equals("owner")) {
	      aUser = flexibook.getOwner();
	     }else {
	      aUser = findCustomer(user);
	     }
	     Date aCurDate = stringToDate(curDate);
	     Time aCurTime = stringToTime(curTime);
	     Date aNewDate = stringToDate(newDate);
	     Time aNewTime = stringToTime(newTime);
	  FlexiBookApplication.setCurrentUser(aUser);
	  try {
	   toAppointment = FlexiBookController.updateAppointment(serviceName, aCurDate, aCurTime, aNewDate, aNewTime );
	  }catch(InvalidInputException e) {
	   error += e.getMessage();
	   errorCounter++;
	  }
	 }
	
	/**
	 * @author Cecilia Jiang
	 */
	@When("{string} attempts to cancel their {string} appointment on {string} at {string}")
	public void attempts_to_cancel_their_appointment_on_at(String customer, String serviceName, String startDate, String startTime) throws InvalidInputException {
		Date aDate = stringToDate(startDate);
	    //String stringEndTime = startTime;
	    Time aStartTime = stringToTime(startTime);
		FlexiBookApplication.setCurrentUser(findCustomer(customer));
		try {
			FlexiBookController.cancelAppointment(serviceName, aDate, aStartTime);
		}catch(InvalidInputException e) {
			error += e.getMessage();
			errorCounter++;
		}
	}

	/**
	 * @author Cecilia Jiang
	 */
	@Then("{string}'s {string} appointment on {string} at {string} shall be removed from the system")
	public void s_appointment_on_at_shall_be_removed_from_the_system(String customer, String serviceName, String startDate, String startTime) {
	    List<Customer> customers = FlexiBookApplication.getFlexiBook().getCustomers();
	    Appointment aAppointment = null;
	    Date aDate = stringToDate(startDate);
	    Time aStartTime = stringToTime(startTime);
	    for(Customer c:customers) {
	    	if(c.getUsername().equals(customer)) {
	    		List<Appointment> appointments = c.getAppointments();
	    		for(Appointment a:appointments) {
	    			if(a.getBookableService().getName().equals(serviceName) && a.getTimeSlot().getStartDate().equals(aDate)
	    					&&a.getTimeSlot().getStartTime().equals(aStartTime)) {
	    				aAppointment = a;
	    			}
	    		}
	    	}
	    }
	    assertTrue(aAppointment==null);
	}

	/**
	 * @author Cecilia Jiang
	 */
	@Then("there shall be {int} less appointment in the system")
	public void there_shall_be_less_appointment_in_the_system(Integer num) {
		assertEquals(num,numOfAppointments-FlexiBookApplication.getFlexiBook().getAppointments().size());
	}

	/**
	 * @author Cecilia Jiang
	 */
	@When("{string} attempts to cancel {string}'s {string} appointment on {string} at {string}")
	public void attempts_to_cancel_s_appointment_on_at(String user, String customer, String serviceName, String startDate, String startTime) throws InvalidInputException {
		Date aDate = stringToDate(startDate);
	    Time aStartTime = stringToTime(startTime);
		User aUser = null;
		if(user.equals("owner")) {
	    	aUser = flexibook.getOwner();
	    }else {
	    	aUser = findCustomer(user);
	    }
		FlexiBookApplication.setCurrentUser(aUser);
		try {
			FlexiBookController.cancelAppointment(serviceName, aDate, aStartTime);
		}catch(InvalidInputException e) {
			error += e.getMessage();
			errorCounter++;
		}
	}
	
/*
 * Start of steps for appointment booking process scenarios.
 */
	/**
	 * @author Grey Yuan
	 */
	@Given("{string} has {int} no-show records")
	public void has_no_show_records(String string, Integer int1) {
			  Customer customer = findCustomer(string);
			  customer.setNumOfNoShows(int1);
	}
	/**
	 * @author Jiatong Niu
	 *
	 */
	@When("{string} makes a {string} appointment for the date {string} and time {string} at {string}")
	public void makes_a_appointment_for_the_date_and_time_at(String string, String string2, String string3, String string4, String string5) {
	    User aUser = null;
	    Date curDate= DateOfString(string5);
	    Time curTime= TimeOfString(string5);
		Date date = stringToDate(string3);
	    Time startTime = stringToTime(string4);
	    FlexiBookApplication.setCurrentDate(curDate);
	    FlexiBookApplication.setCurrentTime(curTime);
	    
	    if(string.equals("owner")) {
	        aUser = flexibook.getOwner();
	       }else {
	        aUser = findCustomer(string);
	       }
	    
	    Service sv=findService(string2);
	    Time endTime = addDuration(startTime,sv.getDuration());
	    TimeSlot timeSlot = new TimeSlot(date,startTime,date,endTime,flexibook);
	  
	    if(aUser instanceof Customer) {

		aptmt = new Appointment((Customer)aUser,sv,timeSlot,flexibook); 
		aptmt.makeAppointment();
	    }
	
	    
	}


	/**
	 * @author Jiatong Niu
	 *
	 */
	@When("{string} attempts to change the service in the appointment to {string} at {string}")
	public void attempts_to_change_the_service_in_the_appointment_to_at(String string, String string2, String string3) {
	    User aUser =null;
	    Date curDate= DateOfString(string3);
	    Time curTime= TimeOfString(string3);
	    FlexiBookApplication.setCurrentDate(curDate);
	    FlexiBookApplication.setCurrentTime(curTime);
	    
	    if(string.equals("owner")) {
	        aUser = flexibook.getOwner();
	       }else {
	        aUser = findCustomer(string);
	       }

	    
	   aptmt.changeToAnotherService(string2, null, aptmt.getTimeSlot().getStartDate(), aptmt.getTimeSlot().getStartTime());

	    
	}

	/**
	 * @author Jiatong Niu
	 *
	 */
	@Then("the appointment shall be booked")
	public void the_appointment_shall_be_booked() {

	  Appointment aAppointment = this.findAppointment(aptmt.getCustomer().getUsername(), aptmt.getBookableService().getName(), aptmt.getTimeSlot().getStartDate().toString(),aptmt.getTimeSlot().getStartTime().toString().substring(0, 5));
      assertTrue(aAppointment!=null);  
	}
	
	/**
	 * @author Jiatong Niu
	 *
	 */
	@Then("the service in the appointment shall be {string}")
	public void the_service_in_the_appointment_shall_be(String string) {
	  assertEquals(string,aptmt.getBookableService().getName());
	  }
	
	/**
	 * @author Jiatong Niu
	 *
	 */
	@Then("the appointment shall be for the date {string} with start time {string} and end time {string}")
	public void the_appointment_shall_be_for_the_date_with_start_time_and_end_time(String string, String string2, String string3) {
	   String date = aptmt.getTimeSlot().getStartDate().toString();
	   String startTime=aptmt.getTimeSlot().getStartTime().toString();
	   String endTime= aptmt.getTimeSlot().getEndTime().toString();
	   assertEquals(string,date);
	   assertEquals(string2+":00",startTime);
	   assertEquals(string3+":00",endTime);
	   
	}
	/**
	 * @author Jiatong Niu
	 *
	 */
	@Then("the username associated with the appointment shall be {string}")
	public void the_username_associated_with_the_appointment_shall_be(String string) {
	   assertEquals(string,aptmt.getCustomer().getUsername());
	}

	/**
	 * @author Jiatong Niu
	 *
	 */
	@Then("the system shall have {int} appointments")
	public void the_system_shall_have_appointments(Integer int1) {
		
	    int i = flexibook.getAppointments().size();
	    assertEquals(int1,i);
	    

	}
	
	/**
	 * @author Jack Wei
	 *
	 */
	@When("{string} attempts to update the date to {string} and time to {string} at {string}")
	public void attempts_to_update_the_date_to_and_time_to_at(String string, String string2, String string3, String string4) {
		    User aUser =null;
		    Date curDate= DateOfString(string4);
		    Time curTime= TimeOfString(string4);
		    FlexiBookApplication.setCurrentDate(curDate);
		    FlexiBookApplication.setCurrentTime(curTime);
		    
		    if(string.equals("owner")) {
		        aUser = flexibook.getOwner();
		       }else {
		        aUser = findCustomer(string);
		       }
		    FlexiBookApplication.setCurrentUser(aUser);
		    aptmt.changeApptTime(stringToDate(string2), stringToTime(string3));
		    
	}
	
	/**
	 * @author Jiatong Niu
	 *
	 */
	@When("{string} attempts to cancel the appointment at {string}")
	public void attempts_to_cancel_the_appointment_at(String string, String string2) {
		 User aUser =null;
		 Date curDate= DateOfString(string2);
		    Time curTime= TimeOfString(string2);
		    FlexiBookApplication.setCurrentDate(curDate);
		    FlexiBookApplication.setCurrentTime(curTime);
		    
         if(string.equals("owner")) {
		        aUser = flexibook.getOwner();
		       }else {
		        aUser = findCustomer(string);
		  }
         FlexiBookApplication.setCurrentUser(aUser);
         /*Boolean canceled=*/ aptmt.cancelAppointment();
	}
	
	/**
	 * @author Grey Yuan
	 */
	@Then("the system shall have {int} appointment")
	public void the_system_shall_have_appointment(Integer int1) {

	    int i = flexibook.getAppointments().size();
	    assertEquals(int1,i);
	    
	}

	/**
	 * @author Jiatong Niu
	 *
	 */
	@When("{string} makes a {string} appointment without choosing optional services for the date {string} and time {string} at {string}")
	public void makes_a_appointment_without_choosing_optional_services_for_the_date_and_time_at(String string, String string2, String string3, String string4, String string5) {
	    User aUser = null;
	    Date curDate= DateOfString(string5);
	    Time curTime= TimeOfString(string5);
		Date date = stringToDate(string3);
	    Time startTime = stringToTime(string4);
	    FlexiBookApplication.setCurrentDate(curDate);
	    FlexiBookApplication.setCurrentTime(curTime);
	    
	    if(string.equals("owner")) {
	        aUser = flexibook.getOwner();
	       }else {
	        aUser = findCustomer(string);
	       }
	    
	    ServiceCombo svc=findServiceCombo(string2);
	    int duration =0;
	    for (ComboItem i:svc.getServices()) {
	    	if(i.getMandatory()==true) {
	    		duration+=i.getService().getDuration();
	    		
	    	}
	    }
	    Time endTime = addDuration(startTime,duration);
	    TimeSlot timeSlot = new TimeSlot(date,startTime,date,endTime,flexibook);
	  
	    if(aUser instanceof Customer) {
		aptmt = new Appointment((Customer)aUser,svc,timeSlot,flexibook);
		 for (ComboItem i:svc.getServices()) {
		    	if(i.getMandatory()==true) {
		    		aptmt.addChosenItem(i);
		    		
		    	}
		    }
		aptmt.makeAppointment();
	    }
	}
	/**
	 * @author Jiatong Niu
	 *
	 */
	@When("{string} attempts to add the optional service {string} to the service combo in the appointment at {string}")
	public void attempts_to_add_the_optional_service_to_the_service_combo_in_the_appointment_at(String string, String string2, String string3) {
		    User aUser = null;
		    Date curDate= DateOfString(string3);
		    Time curTime= TimeOfString(string3);
		    FlexiBookApplication.setCurrentDate(curDate);
		    FlexiBookApplication.setCurrentTime(curTime);  
		    if(string.equals("owner")) {
		        aUser = flexibook.getOwner();
		       }else {
		        aUser = findCustomer(string);
		       }		
		    
		    aptmt.updateApptOfServiceCombo("add", string2);
		   
	}
	/**
	 * @author Jiatong Niu
	 *
	 */
	@Then("the service combo in the appointment shall be {string}")
	public void the_service_combo_in_the_appointment_shall_be(String string) {
	    assertEquals(string,aptmt.getBookableService().getName());
	}
	/**
	 * @author Jiatong Niu
	 *
	 */
	@Then("the service combo shall have {string} selected services")
	public void the_service_combo_shall_have_selected_services(String string) {
	  int k=0;
	  List<String> selectedsv = new ArrayList<String>();
	  for(k=0;k<((ServiceCombo)aptmt.getBookableService()).getServices().size();k++) {
		  if(aptmt.getChosenItems().contains(((ServiceCombo)aptmt.getBookableService()).getService(k))==true) {
			  selectedsv.add(((ServiceCombo)aptmt.getBookableService()).getService(k).getService().getName());
		  }
	  }
	  String svname=String.join(",", selectedsv);
	  assertEquals(string,svname);
	}
	/**
	 * @author Jiatong Niu
	 *
	 */
	@When("the owner starts the appointment at {string}")
	public void the_owner_starts_the_appointment_at(String string) {
		Date curDate= DateOfString(string);
	    Time curTime= TimeOfString(string);
	    FlexiBookApplication.setCurrentDate(curDate);
	    FlexiBookApplication.setCurrentTime(curTime); 
	    aptmt.checkIn();
	}
	/**
	 * @author Grey Yuan
	 */
	@Then("the user {string} shall have {int} no-show records")
	public void the_user_shall_have_no_show_records(String string, Integer int1) {
		Customer customer = findCustomer(string);
	
		  assertEquals(customer.getUsername().toString(),string);
		  assertEquals(customer.getNumOfNoShows(),int1);
	}
	/**
	 * @author Jack Wei
	 *
	 */
	@When("the owner ends the appointment at {string}")
	public void the_owner_ends_the_appointment_at(String string) {
		Date curDate= DateOfString(string);
	    Time curTime= TimeOfString(string);
	    FlexiBookApplication.setCurrentDate(curDate);
	    FlexiBookApplication.setCurrentTime(curTime); 
	    aptmt.complete();
	}
	/**
	 * @author Jack Wei
	 *
	 */
	@Then("the appointment shall be in progress")
	public void the_appointment_shall_be_in_progress() {
	    assertTrue(aptmt.getState().equals(State.InProgress));
	}
	/**
	 * @author Jiatong Niu
	 *
	 */
	@When("the owner attempts to register a no-show for the appointment at {string}")
	public void the_owner_attempts_to_register_a_no_show_for_the_appointment_at(String string) {
		aptmt.notShow();
	}
	/**
	 * @author Jack Wei
	 *
	 */
	@When("the owner attempts to end the appointment at {string}")
	 public void the_owner_attempts_to_end_the_appointment_at(String string) {
	  Date curDate= DateOfString(string);
	     Time curTime= TimeOfString(string);
	     FlexiBookApplication.setCurrentDate(curDate);
	     FlexiBookApplication.setCurrentTime(curTime); 
	     aptmt.cancelAppointment();
	   
	 }
	
/*
* End of steps for appointment booking process scenarios.
*/

	
	/**
	 * Find the service in the Flexibook by the given service
	 * @param service
	 * @return
     * @author Cecilia Jiang
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
	
	private static ServiceCombo findServiceCombo (String servicecombo) {
		  ServiceCombo foundservicecombo = null;
		  
		  for (BookableService combo:FlexiBookApplication.getFlexiBook().getBookableServices() ) {
		  if (combo instanceof ServiceCombo) {
		   if (combo.getName().equals(servicecombo)) {
		    foundservicecombo = (ServiceCombo) combo ;
		    break;
		   }
		  }
		  }
		  return foundservicecombo;
		 }
	
	private static BookableService findBookableService (String bookableService) {
		BookableService bs = null;
		for (BookableService combo:FlexiBookApplication.getFlexiBook().getBookableServices() ) {
			 if(combo.getName().equals(bookableService)) {
				 bs = combo;
				 break;
			 }
		}
		
		return bs;
		
	}
	
	private static Appointment findAppointment(String customer, String serviceName, String startDate, String startTime) {
	    List<Customer> customers = FlexiBookApplication.getFlexiBook().getCustomers();
	    Appointment aAppointment = null;
	    Date aDate = stringToDate(startDate);
	    Time aStartTime = stringToTime(startTime);
	    for(Customer c:customers) {
	    	if(c.getUsername().equals(customer)) {
	    		List<Appointment> appointments = c.getAppointments();
	    		for(Appointment a:appointments) {
	    			if(a.getBookableService().getName().equals(serviceName) && a.getTimeSlot().getStartDate().equals(aDate)
	    					&&a.getTimeSlot().getStartTime().equals(aStartTime)) {
	    				aAppointment = a;
	    			}
	    		}
	    	}
	    }
		return aAppointment;
	}
	


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
	


	 /**
	  * @author lizhiwei
	  *
	  */private static Date stringToDate(String string)  {
	    Date date= Date.valueOf(string);
	    return date;
	 }

   /**
    * @author lizhiwei
    *
    */private static Time stringToTime(String string) {
	Time st = Time.valueOf(string+":00");

	   return st;
	 }

	/**
	 * invert the result parameter
	 * 
	 * @param string string to convert
	 * @return Time
	 * @author Grey Yuan
	 */
	private static String invertErrorResult(String string) {
		if (string.equals("be")) {
			return "not be";
		}
		return "be";
	}

	/**
	 * 
	 * @param string of date+time
	 * @return Time
	 * @author Jiatong Niu
	 */
	private Time TimeOfString(String string5) {

		String[] strings = string5.split("\\+");
		Time time= stringToTime(strings[1]);
		return time;
	}
	/**
	 * 
	 * @param string of date+time
	 * @return date
	 * @author Jiatong Niu
	 */
	private Date DateOfString(String string5) {

		String[] strings = string5.split("\\+");
		Date date= stringToDate(strings[0]);
		return date;
	}
}

