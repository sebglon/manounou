/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.manounou.spi;

import com.google.api.server.spi.response.NotFoundException;
import com.google.api.server.spi.response.UnauthorizedException;
import com.google.appengine.api.users.User;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.manounou.domain.PMF;
import org.manounou.domain.Profile;
import org.manounou.domain.ProfileType;
import org.manounou.form.ProfileForm;

/**
 *
 * @author sgl
 */
public class ProfileServiceApiTest {

    private static final String EMAIL = "testuser@example.com";

    private static final String USER_ID = "123456789";

    private static final String DISPLAY_NAME = "Test User";

    private User user;

    private ProfileServiceApi profileServiceApi;

    private final LocalServiceTestHelper helper
            = new LocalServiceTestHelper(
                    new LocalDatastoreServiceTestConfig().
                    setDefaultHighRepJobPolicyUnappliedJobPercentage(100));

    private PersistenceManager pm;

    @Before
    public void setUp() throws Exception {
        pm = PMF.get().getPersistenceManager();
        helper.setUp();
        user = new User(EMAIL, "gmail.com", USER_ID);
        profileServiceApi = new ProfileServiceApi();
    }

    @After
    public void tearDown() throws Exception {
        pm = null;
        helper.tearDown();
    }

    @Test(expected = UnauthorizedException.class)
    public void testGetProfileWithoutUser() throws Exception {
        profileServiceApi.getProfile(null);
    }

    @Test
    public void testGetProfileFirstTime() throws Exception {

        Profile profile = null;
        try {
            profile = pm.getObjectById(
                    Profile.class, user.getUserId());
        } catch (JDOObjectNotFoundException ex) {
            try {
                profile = profileServiceApi.getProfile(user);
                fail("Exception can be throw");
            } catch (NotFoundException e) {
                assertNotNull(e);
            }
        } finally {
            pm.close();
        }
    }

    @Test
    public void testSaveProfile() throws Exception {
        try {
            // Save the profile for the first time.
            Profile profile = profileServiceApi.saveProfile(
                    user, new ProfileForm(DISPLAY_NAME, ProfileType.EMPLOYEE));
            // Check the return value first.
            assertEquals(USER_ID, profile.getUserId());
            assertEquals(EMAIL, profile.getMainEmail());
            assertEquals(ProfileType.EMPLOYEE, profile.getProfileType());
            assertEquals(DISPLAY_NAME, profile.getDisplayName());
            profile = pm.getObjectById(Profile.class, user.getUserId());
            assertEquals(USER_ID, profile.getUserId());
            assertEquals(EMAIL, profile.getMainEmail());
            assertEquals(ProfileType.EMPLOYEE, profile.getProfileType());
            assertEquals(DISPLAY_NAME, profile.getDisplayName());
        } finally {
            pm.close();
        }
    }
}
