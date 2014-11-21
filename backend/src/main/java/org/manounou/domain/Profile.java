/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.manounou.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.PrimaryKey;

/**
 * @author sgl
 */
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Profile {

    /**
     * Use userId as the datastore key.
     */
    @PrimaryKey
    private String userId;

    /**
     * Any string user wants us to display him/her on this system.
     */
    private String displayName;

    /**
     * User's main e-mail address.
     */
    private String mainEmail;

    /**
     * The user's ProfileType Options are defined as an Enum in ProfileForm
     */
    private ProfileType profileType;

    /**
     * Keys of the conferences that this user registers to attend.
     */
    private List<String> timeSheetKeys = new ArrayList<String>(0);

    /**
     * Just making the default constructor private.
     */
    private Profile() {
    }

    /**
     * Public constructor for Profile.
     *
     * @param userId      The datastore key.
     * @param displayName Any string user wants us to display him/her on this
     *                    system.
     * @param mainEmail   User's main e-mail address.
     * @param profileType User's profileType (Enum is in ProfileForm)
     */
    public Profile(String userId, String displayName, String mainEmail, ProfileType profileType) {
        this.userId = userId;
        this.displayName = displayName;
        this.mainEmail = mainEmail;
        this.profileType = profileType;
    }

    /**
     * Getter for userId.
     *
     * @return userId.
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Getter for displayName.
     *
     * @return displayName.
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Getter for mainEmail.
     *
     * @return mainEmail.
     */
    public String getMainEmail() {
        return mainEmail;
    }

    /**
     * Getter for profileType.
     *
     * @return profileType.
     */
    public ProfileType getProfileType() {
        return profileType;
    }

    /**
     * Getter for timeSheetKeys.
     *
     * @return an immutable copy of timeSheetKeys.
     */
    public List<String> getTimesheetkeys() {
        return Collections.unmodifiableList(timeSheetKeys);
    }

    /**
     * Update the Profile with the given displayName and profileType
     *
     * @param displayName
     */
    public void update(String displayName) {
        if (displayName != null) {
            this.displayName = displayName;
        }
    }

    /**
     * Adds a ConferenceId to conferenceIdsToAttend.
     * <p/>
     * The method initConferenceIdsToAttend is not thread-safe, but we need a
     * transaction for calling this method after all, so it is not a practical
     * issue.
     *
     * @param conferenceKey a websafe String representation of the Conference
     *                      Key.
     */
    public void addToConferenceKeysToAttend(String conferenceKey) {
        timeSheetKeys.add(conferenceKey);
    }

    /**
     * Remove the conferenceId from conferenceIdsToAttend.
     *
     * @param conferenceKey a websafe String representation of the Conference
     *                      Key.
     */
    public void unregisterFromConference(String conferenceKey) {
        if (timeSheetKeys.contains(conferenceKey)) {
            timeSheetKeys.remove(conferenceKey);
        } else {
            throw new IllegalArgumentException("Invalid conferenceKey: " + conferenceKey);
        }
    }
}
