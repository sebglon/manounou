package org.manounou;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;

import com.appspot.mananou44sql.profileApi.ProfileApi;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;

import javax.annotation.Nullable;

/**
 * Created by sgl on 21.11.14.
 */
public class AppConstants {

    public static final String WEB_CLIENT_ID = "618238288494-ki0oogpn6en7a58j5vv821j4mklpfa05.apps.googleusercontent.com";
    public static final String AUDIENCE = "server:client_id:" + WEB_CLIENT_ID;
    /**
     * Class instance of the JSON factory.
     */
    public static final JsonFactory JSON_FACTORY = new AndroidJsonFactory();

    /**
     * Class instance of the HTTP transport.
     */
    public static final HttpTransport HTTP_TRANSPORT = AndroidHttp.newCompatibleTransport();

    /**
     * Count Google accounts on the device.
     */
    public static int countGoogleAccounts(Context context) {
        AccountManager am = AccountManager.get(context);
        Account[] accounts = am.getAccountsByType(GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE);
        if (accounts == null || accounts.length < 1) {
            return 0;
        } else {
            return accounts.length;
        }
    }

    /**
     * Retrieve a Helloworld api service handle to access the API.
     */
    public static ProfileApi getApiServiceHandle(@Nullable GoogleAccountCredential credential) {
        // Use a builder to help formulate the API request.
        ProfileApi.Builder helloWorld = new ProfileApi.Builder(AppConstants.HTTP_TRANSPORT,
                AppConstants.JSON_FACTORY, credential);

        // If running the Cloud Endpoint API locally then point the API stub there by un-commenting the
        // next line.
        // helloWorld.setRootUrl("http://192.168.1.100:8080/_ah/api/");

        return helloWorld.build();
    }

    /**
     * Check that Google Play services APK is installed and up to date.
     */
    public static boolean checkGooglePlayServicesAvailable(Activity activity) {
        final int connectionStatusCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity);
        if (GooglePlayServicesUtil.isUserRecoverableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(activity, connectionStatusCode);
            return false;
        }
        return true;
    }

    /**
     * Called if the device does not have Google Play Services installed.
     */
    public static void showGooglePlayServicesAvailabilityErrorDialog(final Activity activity,
                                                                     final int connectionStatusCode) {
        final int REQUEST_GOOGLE_PLAY_SERVICES = 0;
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Dialog dialog = GooglePlayServicesUtil.getErrorDialog(
                        connectionStatusCode, activity, REQUEST_GOOGLE_PLAY_SERVICES);
                dialog.show();
            }
        });
    }
}
