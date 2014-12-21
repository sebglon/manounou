package org.manounou.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAuthIOException;

import org.manounou.App;
import org.manounou.R;
import org.manounou.profileApi.model.Profile;
import org.manounou.profileApi.model.ProfileForm;

import java.io.IOException;

import static org.manounou.AppConstants.getProfileApiServiceHandle;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment.ProfileListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    private static final String LOG_TAG = "ProfileFragment";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private ProfileUpdateApiTask mProfileTask;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ProfileListener mListener;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);


        final Button button = (Button) v.findViewById(R.id.profile_button_valider);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (mProfileTask != null) {
                    try {
                        mProfileTask.cancel(true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return;
                }

                // Start task to check authorization.
                mProfileTask = new ProfileUpdateApiTask();
                ProfileForm form = new ProfileForm();
                TextView givenName = (TextView) getView().findViewById(R.id.givenname);
                form.setDisplayName(givenName.getText().toString());
                mProfileTask.execute(form);
            }
        });


        // Inflate the layout for this fragment
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            //   mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (ProfileListener) activity;
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

    public interface ProfileListener {
        void onUpdateProfileSuccess();

        void onUpdateProfileFail();
    }

    class ProfileUpdateApiTask extends AsyncTask<ProfileForm, Integer, Profile> {

        protected Profile doInBackground(ProfileForm... form) {
            mProfileTask = this;

            try {
                Profile profile = getProfileApiServiceHandle(((App) getActivity().getApplication()).getCredential()).saveProfile(form[0]).execute();
                return profile;
            } catch (GoogleAuthIOException authE) {
                Log.e(LOG_TAG, "Fail to save profil", authE);
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
            if (profile != null) {
                ((App) ProfileFragment.this.getActivity().getApplication()).setProfile(profile);
                mListener.onUpdateProfileSuccess();
            } else {
                ((App) ProfileFragment.this.getActivity().getApplication()).setProfile(null);
                mListener.onUpdateProfileFail();
                Log.e(LOG_TAG, "No profile were returned by the API.");
            }

        }

        @Override
        protected void onCancelled() {
            mProfileTask = null;
        }
    }
}
