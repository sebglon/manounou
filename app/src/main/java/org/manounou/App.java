package org.manounou;

import android.app.Application;
import android.content.SharedPreferences;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;

import static org.manounou.AppConstants.PREF_ACCOUNT_NAME;

/**
 * Created by sgl on 02.12.14.
 */
public class App extends Application {

    private static SharedPreferences preferences;

    private static GoogleAccountCredential credential;

    /**
     * Tracker Google Analytics.
     */
    private Tracker tracker;

    @Override
    public void onCreate() {
        super.onCreate();

        // Récupération des préférence de l'appli.
        preferences = getSharedPreferences(AppConstants.TAG, 0);
        // Create a Google credential since this is an authenticated request to the API.
        credential = GoogleAccountCredential.usingAudience(getApplicationContext()
                , AppConstants.ANDROID_CLIENT_ID);
        if (getAccountName() != null) {
            credential.setSelectedAccountName(getAccountName());
        }
    }

    public GoogleAccountCredential getCredential() {
        return credential;
    }

    /**
     * Retourne une instance de tacker google analytics.
     *
     * @return une instance de tacker google analytics.
     */
    public synchronized Tracker getTracker() {
        if (tracker == null) {

            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            tracker = analytics.newTracker("???");
            //analytics.
        }
        return tracker;
    }


    public SharedPreferences getPreferences() {
        return preferences;
    }

    public String getAccountName() {
        return getPreferences().getString(PREF_ACCOUNT_NAME, null);
    }

    public void setAccountName(String accountName) {
        getPreferences().edit().putString(PREF_ACCOUNT_NAME, accountName).commit();
    }
}

