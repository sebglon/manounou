package org.manounou;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.appspot.mananou44sql.profileApi.model.Profile;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAuthIOException;

import org.manounou.fragment.AuthenticationFragment;
import org.manounou.fragment.ProfileFragment;

import java.io.IOException;

import static org.manounou.AppConstants.getProfileApiServiceHandle;

public class MainActivity extends Activity implements AuthenticationFragment.Authenticationlistener {
    private static final String LOG_TAG = "MainActivity";


    private ProfileApiTask mProfileTask;
    private Profile profile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (((App) getApplication()).getAccountName() == null) {
            // Authentification de l'utilisateur
            getFragmentManager().beginTransaction()
                    .replace(R.id.container, AuthenticationFragment.newInstance())
                    .commit();
        } else {
            if (savedInstanceState == null) {
                // Récupération du profile de l'utilisateur (sur le net si réseau; sinon en cache)
                // TODO Traiter le cas sans réseau
                onAuthSuccess();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mProfileTask != null) {
            mProfileTask.cancel(true);
            mProfileTask = null;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {

            //noinspection SimplifiableIfStatement
            case R.id.action_settings:
                return true;
            case R.id.action_logout:
                ((App) getApplication()).setAccountName(null);
                this.recreate();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onAuthSuccess() {

        //TODO si internet refresh du profil; sinon utilisatition du cache
        Toast.makeText(getApplicationContext(), "Chargement de vos données personnelles", Toast.LENGTH_SHORT).show();

        getProfile(((App) getApplication()).getAccountName());

    }

    public void onGetProfileSuccess() {
        getFragmentManager().beginTransaction()
                .replace(R.id.container, new PlaceholderFragment())
                .commit();
    }

    public void onGetProfileFail() {
        getFragmentManager().beginTransaction()
                .replace(R.id.container, new ProfileFragment())
                .commit();
        Toast.makeText(getApplicationContext(), "Aucun profile utilisateur trouvé", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAuthFail() {
        Toast.makeText(getApplicationContext(), "Erreur d'authentification", Toast.LENGTH_SHORT).show();
    }

    public void getProfile(String emailAccount) {
        // Cancel previously running tasks.
        if (mProfileTask != null) {
            try {
                mProfileTask.cancel(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }

        // start task to get Profile
        mProfileTask = new ProfileApiTask();
        mProfileTask.execute(emailAccount);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            TextView username = (TextView) rootView.findViewById(R.id.userName);
            username.setText(((App) getActivity().getApplication()).getAccountName());
            return rootView;
        }
    }

    class ProfileApiTask extends AsyncTask<String, Integer, Profile> {

        protected Profile doInBackground(String... emailAccounts) {
            mProfileTask = this;

            try {
                Profile profile = getProfileApiServiceHandle(((App) getApplication()).getCredential()).getProfile().execute();
                return profile;
            } catch (GoogleAuthIOException authE) {
                Log.e(LOG_TAG, "No profile found", authE);
            } catch (IOException e) {
                Log.e(LOG_TAG, "Exception during API call", e);
            }
            return null;
        }

        protected void onProgressUpdate(Integer... stringIds) {

        }

        protected void onPreExecute() {
            mProfileTask = this;
        }

        protected void onPostExecute(Profile profile) {
            TextView txtProfile = (TextView) MainActivity.this.findViewById(R.id.profileUser);
            if (profile != null) {
                MainActivity.this.profile = profile;
                onGetProfileSuccess();
            } else {
                MainActivity.this.profile = null;
                onGetProfileFail();
                Log.e(LOG_TAG, "No profile were returned by the API.");
            }

        }

        @Override
        protected void onCancelled() {
            mProfileTask = null;
        }
    }
}
