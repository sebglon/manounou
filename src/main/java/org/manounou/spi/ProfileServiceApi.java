/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.manounou.spi;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiMethod.HttpMethod;
import com.google.api.server.spi.response.UnauthorizedException;
import com.google.appengine.api.users.User;
import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;
import org.manounou.Constants;
import org.manounou.ServiceUtils;
import org.manounou.domain.PMF;
import org.manounou.domain.Profile;
import org.manounou.domain.ProfileType;
import org.manounou.form.ProfileForm;

/**
 *
 * @author sgl
 */
@Api(name = "profileApi", version = "v1", description = "An API to manage user profile",
        scopes = {Constants.EMAIL_SCOPE},
        clientIds = {Constants.WEB_CLIENT_ID, Constants.API_EXPLORER_CLIENT_ID},
        audiences = {Constants.ANDROID_AUDIENCE})
public class ProfileServiceApi {

    /**
     * Returns a Profile object associated with the given user object. The cloud
     * endpoints system automatically inject the User object.
     *
     * @param user A User object injected by the cloud endpoints.
     * @return Profile object.
     * @throws UnauthorizedException when the User object is null.
     */
    @ApiMethod(name = "getProfile", path = "profile", httpMethod = HttpMethod.GET)
    public Profile getProfile(final User user) throws UnauthorizedException {
        if (user == null) {
            throw new UnauthorizedException("Authorization required");
        }
        PersistenceManager pm = PMF.get().getPersistenceManager();
        return pm.getObjectById(Profile.class, ServiceUtils.getUserId(user));
    }

    /**
     * Creates or updates a Profile object associated with the given user
     * object.
     *
     * @param user A User object injected by the cloud endpoints.
     * @param profileForm A ProfileForm object sent from the client form.
     * @return Profile object just created.
     * @throws UnauthorizedException when the User object is null.
     */
    @ApiMethod(name = "saveProfile", path = "profile", httpMethod = HttpMethod.POST)
    public Profile saveProfile(final User user, final ProfileForm profileForm)
            throws UnauthorizedException {
        if (user == null) {
            throw new UnauthorizedException("Authorization required");
        }
        String displayName = profileForm.getDisplayName();
        ProfileType profileType = profileForm.getProfileType();
        PersistenceManager pm = PMF.get().getPersistenceManager();
        Transaction tx = pm.currentTransaction();
        Profile profile = null;
        try {
            profile = pm.getObjectById(Profile.class, ServiceUtils.getUserId(user));

            if (profile == null) {
                // Populate displayName and profileType with the default values is null.
                if (displayName == null) {
                    displayName = ServiceUtils.extractDefaultDisplayNameFromEmail(user.getEmail());
                }
                if (profileType == null) {
                    throw new IllegalArgumentException("Profile type can not be null");
                }
                profile = new Profile(ServiceUtils.getUserId(user), displayName, user.getEmail(), profileType);
                pm.makePersistent(profile);
            } else {
                profile.update(displayName);
            }
            tx.commit();
        } finally {
            pm.close();
        }
        return profile;
    }
}
