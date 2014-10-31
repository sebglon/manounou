/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.manounou.domain;

import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.User;
import javax.jdo.annotations.PrimaryKey;

/**
 *
 * @author sgl
 */
public class AppEngineUser {

    private String email;
    private User user;

    private AppEngineUser() {
    }

    public AppEngineUser(User user) {
        this.user = user;
        this.email = user.getEmail();
    }

    public User getUser() {
        return user;
    }

    @PrimaryKey
    public String getKey() {
        return createKey(email);
    }

    public static String createKey(String email) {
        return KeyFactory.createKeyString(AppEngineUser.class.getSimpleName(), email);
    }
}
