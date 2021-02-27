package ca.mcgill.ecse.flexibook.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ca.mcgill.ecse.flexibook.application.FlexiBookApplication;
import ca.mcgill.ecse.flexibook.model.*;


public class FlexiBookControllerTest {

	@BeforeEach
	public void setUp() {
		FlexiBook flexibook = FlexiBookApplication.getFlexiBook();
		flexibook.delete();
		Owner owner = new Owner("owner", "owner",flexibook);
		flexibook.setOwner(owner);
		FlexiBookApplication.setCurrentUser(null);
	}
	
	/**
	 * @author Jack Wei
	 */
	@Test
	public void testSignUpSuccess() {
		FlexiBook flexibook = FlexiBookApplication.getFlexiBook();
		String username = "username";
		String password = "password";
		try {
			FlexiBookController.signUp(username, password);
		} catch (InvalidInputException e) {
			fail();
		}
		checkResultCustomer(flexibook,username,password,1);
	}
	
	/**
	 * @author Jack Wei
	 */
	@Test
	public void testMultipleSignUp() {
		FlexiBook flexibook = FlexiBookApplication.getFlexiBook();
		String username1 = "username1";
		String username2 = "username2";
		String username3 = "username3";
		String password = "password";
		try {
			FlexiBookController.signUp(username1, password);
			FlexiBookController.signUp(username2, password);
			FlexiBookController.signUp(username3, password);
		} catch (InvalidInputException e) {
			fail();
		}
		checkResultCustomer(flexibook,username1,password,3);
	}
	
	/**
	 * @author Jack Wei
	 */
	@Test
	public void testSignUpUsernameEmpty() {
		FlexiBook flexibook = FlexiBookApplication.getFlexiBook();
		String username = "";
		String password = "password";
		String error = "";
		try {
			FlexiBookController.signUp(username, password);
		} catch (InvalidInputException e) {
			error = e.getMessage();
		}
		
		assertEquals("The user name cannot be empty", error);
		checkResultCustomer(flexibook,username,password,0);
	}
	
	/**
	 * @author Jack Wei
	 */
	@Test
	public void testSignUpPasswordEmpty() {
		FlexiBook flexibook = FlexiBookApplication.getFlexiBook();
		String username = "username";
		String password = "";
		String error = "";
		try {
			FlexiBookController.signUp(username, password);
		} catch (InvalidInputException e) {
			error = e.getMessage();
		}
		
		assertEquals("The password cannot be empty", error);
		checkResultCustomer(flexibook,username,password,0);
	}
	
	/**
	 * @author Jack Wei
	 */
	@Test
	public void testSignUpDuplicate() {
		FlexiBook flexibook = FlexiBookApplication.getFlexiBook();
		String username1 = "username";
		String username2 = "username";
		String password = "password";
		String error = "";
		try {
			FlexiBookController.signUp(username1, password);
			FlexiBookController.signUp(username2, password);
		} catch (InvalidInputException e) {
			error = e.getMessage();
		}
		
		assertEquals("The username already exists", error);
		checkResultCustomer(flexibook,username1,password,1);
	}
	
	/**
	 * @author Jack Wei
	 */
	@Test
	public void testSignUpAsOwner() {
		FlexiBook flexibook = FlexiBookApplication.getFlexiBook();
		String username = "username";
		String password = "password";
		String error = "";
		FlexiBookApplication.setCurrentUser(flexibook.getOwner());
		try {
			FlexiBookController.signUp(username, password);
		} catch (InvalidInputException e) {
			error = e.getMessage();
		}
		
		assertEquals("You must log out of the owner account before creating a customer account", error);
		checkResultCustomer(flexibook,username,password,0);
	}
	
	/**
	 * @author Jack Wei
	 */
	@Test
	public void testDeleteAccount() {
		FlexiBook flexibook = FlexiBookApplication.getFlexiBook();
		String username = "username";
		String password = "password";
		try {
			FlexiBookController.signUp(username, password);
			FlexiBookController.deleteAccount(username);
		} catch (InvalidInputException e) {
			fail();
		}
		checkResultCustomer(flexibook,username,password,0);
		
	}
	
	/**
	 * @author Jack Wei
	 */
	@Test
	public void testDeleteOwner() {
		FlexiBook flexibook = FlexiBookApplication.getFlexiBook();
		String username = "username";
		String password = "password";
		String error = "";
		FlexiBookApplication.setCurrentUser(flexibook.getOwner());
		try {
			FlexiBookController.deleteAccount(username);
		} catch (InvalidInputException e) {
			error = e.getMessage();
		}
		
		assertEquals("You do not have permission to delete this account", error);
		checkResultCustomer(flexibook,username,password,0);
	}
	
	/**
	 * @author Jack Wei
	 */
	@Test
	public void testDeleteOtherAccount() {
		FlexiBook flexibook = FlexiBookApplication.getFlexiBook();
		String username1 = "username1";
		String username2 = "username2";
		String password = "password";
		String error = "";
		try {
			FlexiBookController.signUp(username1,password);
			FlexiBookController.signUp(username2,password);
			FlexiBookApplication.setCurrentUser(flexibook.getCustomer(0));
			FlexiBookController.deleteAccount(username2);
		} catch (InvalidInputException e) {
			error = e.getMessage();
		}
		
		assertEquals("You do not have permission to delete this account", error);
		checkResultCustomer(flexibook,username1,password,2);
	}
	
	/**
	 * @author Jack Wei
	 */
	@Test
	public void testUpdateAccountSuccess() {
		FlexiBook flexibook = FlexiBookApplication.getFlexiBook();
		String username1 = "username1";
		String password = "password";
		String username_updated = "username2";
		String password_updated = "password_updated";
		String error = "";
		try {
			FlexiBookController.signUp(username1,password);
			FlexiBookApplication.setCurrentUser(flexibook.getCustomer(0));
			FlexiBookController.updateAccount(username_updated,password_updated);
		} catch (InvalidInputException e) {
			fail();
		}
		checkResultCustomer(flexibook,username_updated,password_updated,1);
	}
	
	/**
	 * @author Jack Wei
	 */
	@Test
	public void testUpdateAccountAsOwner() {
		FlexiBook flexibook = FlexiBookApplication.getFlexiBook();
		FlexiBookApplication.setCurrentUser(flexibook.getOwner());
		String username_updated = "username";
		String password_updated = "password";
		String error = "";
		try {
			FlexiBookController.updateAccount(username_updated,password_updated);
		} catch (InvalidInputException e) {
			error = e.getMessage();
		}
		
		assertEquals("Changing username of owner is not allowed", error);
		checkResultCustomer(flexibook,username_updated,password_updated,0);
	}
	
	/**
	 * @author Jack Wei
	 */
	@Test
	public void testUpdateAccountDuplicate() {
		FlexiBook flexibook = FlexiBookApplication.getFlexiBook();
		String username1 = "username1";
		String username2 = "username2";
		String password = "password";
		String error = "";
		try {
			FlexiBookController.signUp(username1,password);
			FlexiBookController.signUp(username2,password);
			FlexiBookApplication.setCurrentUser(flexibook.getCustomer(0));
			FlexiBookController.updateAccount(username2,password);
		} catch (InvalidInputException e) {
			error = e.getMessage();
		}
		
		assertEquals("Username not available", error);
	
		checkResultCustomer(flexibook,username1,password,2);
	}
	
	/**
	 * @author Jack Wei
	 */
	@Test
	public void testUpdateAccountUsernameEmpty() {
		FlexiBook flexibook = FlexiBookApplication.getFlexiBook();
		String username = "username";
		String password = "password";
		String usernameEmpty = "";
		String error = "";
		try {
			FlexiBookController.signUp(username,password);
			FlexiBookApplication.setCurrentUser(flexibook.getCustomer(0));
			FlexiBookController.updateAccount(usernameEmpty,password);
		} catch (InvalidInputException e) {
			error = e.getMessage();
		}
		
		assertEquals("The user name cannot be empty", error);
	
		checkResultCustomer(flexibook,username,password,1);
	}
	
	/**
	 * @author Jack Wei
	 */
	@Test
	public void testUpdateAccountPasswordEmpty() {
		FlexiBook flexibook = FlexiBookApplication.getFlexiBook();
		String username = "username";
		String password = "password";
		String PasswordEmpty = "";
		String error = "";
		try {
			FlexiBookController.signUp(username,password);
			FlexiBookApplication.setCurrentUser(flexibook.getCustomer(0));
			FlexiBookController.updateAccount(username,PasswordEmpty);
		} catch (InvalidInputException e) {
			error = e.getMessage();
		}
		
		assertEquals("The password cannot be empty", error);
	
		checkResultCustomer(flexibook,username,password,1);
	}
	

	
	
	/**
	 * @author Jack Wei
	 */
	private void checkResultCustomer(FlexiBook flexibook, String username, String password, int numberCustomer) {
		assertEquals(numberCustomer, flexibook.getCustomers().size());
		if (numberCustomer > 0) {
			assertEquals(username, flexibook.getCustomer(0).getUsername());
			assertEquals(password, flexibook.getCustomer(0).getPassword());
			assertEquals(flexibook, flexibook.getCustomer(0).getFlexiBook());
			assertEquals(0, flexibook.getCustomer(0).getAppointments().size());
		}
		assertEquals(0, flexibook.getAppointments().size());
		assertEquals(0, flexibook.getBookableServices().size());
		assertEquals(0, flexibook.getHours().size());
		assertEquals(0, flexibook.getTimeSlots().size());
	}
	

	private void checkResultService(String name, FlexiBook flexibook, int numServices, int duration, int downtimeDuration, int downtimeStart) {
		assertEquals(numServices, flexibook.getBookableServices().size());
		if(numServices > 0) {
			assertEquals(name, flexibook.getBookableService(0).getName());
			assertEquals(duration, ((Service)flexibook.getBookableService(0)).getDuration());
			assertEquals(downtimeDuration, ((Service)flexibook.getBookableService(0)).getDowntimeDuration());
			assertEquals(downtimeStart, ((Service)flexibook.getBookableService(0)).getDowntimeStart());
		}
	}

	private void checkResultUpdatedService(String name, FlexiBook flexibook,int index, int numServices, int duration, int downtimeDuration, int downtimeStart) {
		assertEquals(numServices, flexibook.getBookableServices().size());
		if(numServices > 0) {
			assertEquals(name, flexibook.getBookableService(index).getName());
			assertEquals(duration, ((Service)flexibook.getBookableService(index)).getDuration());
			assertEquals(downtimeDuration, ((Service)flexibook.getBookableService(index)).getDowntimeDuration());
			assertEquals(downtimeStart, ((Service)flexibook.getBookableService(index)).getDowntimeStart());
		}
	}
}
