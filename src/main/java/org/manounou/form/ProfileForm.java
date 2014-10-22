/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.manounou.form;

import org.manounou.domain.ProfileType;

/**
 *
 * @author sgl
 */
public class ProfileForm {
    /**
     * Any string user wants us to display him/her on this system.
     */
    private String displayName;
    
        /**
     * The user's ProfileType 
     * Options are defined as an Enum in ProfileForm
     */
    private ProfileType profileType;
    
    private ProfileForm () {}

    public ProfileForm(String displayName, ProfileType profileType) {
        this.displayName = displayName;
        this.profileType = profileType;
    }
    
        public String getDisplayName() {
        return displayName;
    }

    public ProfileType getProfileType() {
        return profileType;
    }
        
        
}
