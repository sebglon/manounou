/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.manounou;

import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.User;
import java.util.logging.Logger;
import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;
import javax.jdo.annotations.Transactional;
import org.manounou.domain.AppEngineUser;
import org.manounou.domain.PMF;
import org.manounou.domain.Profile;
import org.manounou.domain.ProfileType;

/**
 *
 * @author sgl
 */
public final class ServiceUtils {

    private static final Logger LOG = Logger.getLogger(ServiceUtils.class.getName());

    public static String extractDefaultDisplayNameFromEmail(String email) {
        return email == null ? null : email.substring(0, email.indexOf("@"));
    }

    public static Profile getProfileFromUser(User user, String userId) {
        // First fetch it from the datastore.
        Profile profile = PMF.get().getPersistenceManager().getObjectById(Profile.class,
                KeyFactory.createKeyString(Profile.class.getSimpleName(), userId));
        if (profile == null) {
            // Create a new Profile if not exist.
            String email = user.getEmail();
            profile = new Profile(userId,
                    extractDefaultDisplayNameFromEmail(email), email, ProfileType.NOT_SPECIFIED);
        }
        return profile;
    }

    /**
     * This is an ugly workaround for null userId for Android clients.
     *
     * @param user A User object injected by the cloud endpoints.
     * @return the App Engine userId for the user.
     */
    @Transactional
    public static String getUserId(User user) {
        String userId = user.getUserId();
        if (userId == null) {
            LOG.info("userId is null, so trying to obtain it from the datastore.");
            PersistenceManager pm = PMF.get().getPersistenceManager();
            AppEngineUser appEngineUser = new AppEngineUser(user);
            Transaction tx = pm.currentTransaction();
            try {
                tx.begin();
                pm.makePersistent(appEngineUser);
                pm.flush();
                tx.commit();
            } finally {
                if (tx.isActive()) {
                    tx.rollback(); // Error occurred so rollback the PM transaction
                }
            }
            AppEngineUser savedUser = pm.getObjectById(AppEngineUser.class, AppEngineUser.createKey(user.getEmail()));
            userId = savedUser.getUser().getUserId();
            LOG.info("Obtained the userId: " + userId);
        }
        return userId;
    }

}
