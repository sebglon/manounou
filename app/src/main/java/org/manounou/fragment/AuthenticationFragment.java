package org.manounou.fragment;

import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.util.Strings;

import org.manounou.App;
import org.manounou.AppConstants;
import org.manounou.R;

import java.io.IOException;

import static org.manounou.AppConstants.PREF_ACCOUNT_NAME;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AuthenticationFragment.Authenticationlistener} interface
 * to handle interaction events.
 * Use the {@link AuthenticationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AuthenticationFragment extends Fragment {
    private static final String LOG_TAG = AuthenticationFragment.class.getName();
    /**
     * Activity result indicating a return from the Google account selection intent.
     */
    private static final int ACTIVITY_RESULT_FROM_ACCOUNT_SELECTION = 2222;

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_CREDENTIAL = "credential";

    private AuthorizationCheckTask mAuthTask;
    private GoogleAccountCredential credential;


    private Authenticationlistener mListener;

    public AuthenticationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AuthenticationFragment.
     */
    public static AuthenticationFragment newInstance() {
        AuthenticationFragment fragment = new AuthenticationFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        credential = ((App) getActivity().getApplication()).getCredential();

        if (!AppConstants.checkGooglePlayServicesAvailable(this.getActivity())) {
            return;
        }
        // Invoke an {@code Intent} to allow the user to select a Google account.
        Intent accountSelector = credential.newChooseAccountIntent();
        startActivityForResult(accountSelector,
                ACTIVITY_RESULT_FROM_ACCOUNT_SELECTION);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mAuthTask != null) {
            mAuthTask.cancel(true);
            mAuthTask = null;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ACTIVITY_RESULT_FROM_ACCOUNT_SELECTION && resultCode == Activity.RESULT_OK) {
            // This path indicates the account selection activity resulted in the user selecting a
            // Google account and clicking OK.

            // Set the selected account.
            String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
            // Fire off the authorization check for this account and OAuth2 scopes.
            performAuthCheck(accountName);
            setAccountName(accountName);
        }

    }

    private void setAccountName(String accountName) {
        ((App) getActivity().getApplication()).setAccountName(accountName);
        credential.setSelectedAccountName(accountName);
    }

    /**
     * Schedule the authorization check in an {@code Tasks}.
     */
    public void performAuthCheck(String emailAccount) {
        // Cancel previously running tasks.
        if (mAuthTask != null) {
            try {
                mAuthTask.cancel(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }

        // Start task to check authorization.
        mAuthTask = new AuthorizationCheckTask();
        mAuthTask.execute(emailAccount);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_authentication, container, false);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (Authenticationlistener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface Authenticationlistener {
        public void onAuthSuccess();

        public void onAuthFail();
    }

    /**
     * Verifies OAuth2 token access for the application and Google account combination with
     * the {@code AccountManager} and the Play Services installed application. If the appropriate
     * OAuth2 access hasn't been granted (to this application) then the task may fire an
     * {@code Intent} to request that the user approve such access. If the appropriate access does
     * exist then the button that will let the user proceed to the next activity is enabled.
     */
    class AuthorizationCheckTask extends AsyncTask<String, Integer, Boolean> {
        @Override
        protected Boolean doInBackground(String... emailAccounts) {
            Log.i(LOG_TAG, "Background task started.");

            if (!AppConstants.checkGooglePlayServicesAvailable(getActivity())) {
                return false;
            }

            String emailAccount = emailAccounts[0];
            // Ensure only one task is running at a time.
            mAuthTask = this;

            // Ensure an email was selected.
            if (Strings.isNullOrEmpty(emailAccount)) {
                publishProgress(R.string.toast_no_google_account_selected);
                // Failure.
                return false;
            }

            try {
                // If the application has the appropriate access then a token will be retrieved, otherwise
                // an error will be thrown.
                GoogleAccountCredential credential = GoogleAccountCredential.usingAudience(
                        getActivity(), AppConstants.AUDIENCE);
                credential.setSelectedAccountName(emailAccount);

                String accessToken = credential.getToken();
                SharedPreferences.Editor editor = ((App) getActivity().getApplication()).getPreferences().edit();
                editor.putString(PREF_ACCOUNT_NAME, emailAccount);
                editor.commit();
                credential.setSelectedAccountName(emailAccount);

                // Success.
                return true;
            } catch (GoogleAuthException unrecoverableException) {
                Log.e(LOG_TAG, "Exception checking OAuth2 authentication.", unrecoverableException);
                publishProgress(R.string.toast_exception_checking_authorization);
                // Failure.
                return false;
            } catch (IOException ioException) {
                Log.e(LOG_TAG, "Exception checking OAuth2 authentication.", ioException);
                publishProgress(R.string.toast_exception_checking_authorization);
                // Failure or cancel request.
                return false;
            }
        }

        @Override
        protected void onProgressUpdate(Integer... stringIds) {
            // Toast only the most recent.
            Integer stringId = stringIds[0];
            Toast.makeText(getActivity(), stringId, Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPreExecute() {
            mAuthTask = this;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            TextView txtUserName = (TextView) getActivity().findViewById(R.id.userName);
            if (success) {
                // Authorization check successful, set internal variable.
                mListener.onAuthSuccess();
            } else {
                // Authorization check unsuccessful, reset TextView to empty.
                mListener.onAuthFail();
            }
            mAuthTask = null;
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
        }
    }
}
