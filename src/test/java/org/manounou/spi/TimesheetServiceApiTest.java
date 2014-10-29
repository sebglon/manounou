/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.manounou.spi;

import org.manounou.spi.TimesheetServiceApi;
import com.google.appengine.api.users.User;
import com.google.appengine.repackaged.org.joda.time.MonthDay;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.manounou.domain.Status;
import org.manounou.domain.Timesheet;

/**
 *
 * @author sgl
 */
public class TimesheetServiceApiTest {
    private final LocalServiceTestHelper helper =  
        new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());  
    
    private static final String EMAIL = "testuser@example.com";
    private static final String USER_ID = "123456789";
    
     private User user;
    
    private Timesheet defaultTimesheet = null;
    
    private TimesheetServiceApi timesheetServiceApi;
    public TimesheetServiceApiTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        helper.setUp();
        user = new User(EMAIL, "gmail.com", USER_ID);
        defaultTimesheet = new Timesheet("TEST", MonthDay.parse("1900-01"));
        defaultTimesheet.setStatus(Status.ON_EDIT);
    }
    
    @After
    public void tearDown() {
        helper.tearDown();
        defaultTimesheet=null;
    }

    /**
     * Test of getTimeSheet method, of class TimesheetServiceApi.
     */
    @Test
    public void testGetTimeSheet() throws Exception {
        System.out.println("Test Success");
        String contract = defaultTimesheet.getContract();
        MonthDay monthDay = defaultTimesheet.getMonthDay();
        helper.setEnvIsLoggedIn(true);
        TimesheetServiceApi instance = new TimesheetServiceApi();
        Timesheet expResult = null;
        Timesheet result = instance.getTimeSheet(contract, monthDay, user);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of update method, of class TimesheetServiceApi.
     */
    @Test
    public void testUpdate() throws Exception {
        System.out.println("update");
        Timesheet timesheet = null;
        User user = null;
        TimesheetServiceApi instance = new TimesheetServiceApi();
        Timesheet expResult = null;
        Timesheet result = instance.update(timesheet, user);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of validate method, of class TimesheetServiceApi.
     */
    @Test
    public void testValidate() throws Exception {
        System.out.println("validate");
        String contract = "";
        MonthDay monthDay = null;
        User user = null;
        TimesheetServiceApi instance = new TimesheetServiceApi();
        Timesheet expResult = null;
        Timesheet result = instance.validate(contract, monthDay, user);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of reject method, of class TimesheetServiceApi.
     */
    @Test
    public void testReject() throws Exception {
        System.out.println("reject");
        String contract = "";
        MonthDay monthDay = null;
        User user = null;
        TimesheetServiceApi instance = new TimesheetServiceApi();
        Timesheet expResult = null;
        Timesheet result = instance.reject(contract, monthDay, user);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
