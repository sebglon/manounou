package org.manounou;

/**
 * Contains the client IDs and scopes for allowed clients consuming the
 * helloworld API.
 */
public class Constants {

    public static final String API_EXPLORER_CLIENT_ID = com.google.api.server.spi.Constant.API_EXPLORER_CLIENT_ID;
    public static final String WEB_CLIENT_ID = "618238288494-8ovsi7nfbslkiifku6ksbfgd82a1evlt.apps.googleusercontent.com";
    public static final String ANDROID_AUDIENCE = WEB_CLIENT_ID;
    public static final String ANDROID_CLIENT_ID = "618238288494-k7i521v8516ua49j8d3hbrpcogttrpt5.apps.googleusercontent.com";
    public static final String IOS_CLIENT_ID = "replace this with your iOS client ID";
    public static final String EMAIL_SCOPE = com.google.api.server.spi.Constant.API_EMAIL_SCOPE;
    public static final String PROFILE_SCOPE = "https://www.googleapis.com/auth/userinfo.profile";
}
