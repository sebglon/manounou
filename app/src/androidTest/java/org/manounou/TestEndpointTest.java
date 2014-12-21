package org.manounou;

import android.test.AndroidTestCase;
import android.util.Log;

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;

import org.manounou.profileApi.ProfileApi;
import org.manounou.profileApi.model.Profile;

import java.io.IOException;

/**
 * Created by sgl on 20.12.14.
 */
public class TestEndpointTest extends AndroidTestCase {
    private static final String DEV_SERVER = "http://10.0.3.2:8080/_ah/api/";
    private static final String TAG = TestEndpointTest.class.getSimpleName();
    private ProfileApi endpoint;

    private GoogleAccountCredential credential;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        credential = GoogleAccountCredential.usingAudience(
                getContext(), AppConstants.AUDIENCE);
        credential.setSelectedAccountName("sebglon@gmail.com");

        final ProfileApi.Builder bld = new ProfileApi.Builder(
                AppConstants.HTTP_TRANSPORT,
                AppConstants.JSON_FACTORY, credential);
        endpoint = bld.setRootUrl(DEV_SERVER).setGoogleClientRequestInitializer(
                new GoogleClientRequestInitializer() {
                    @Override
                    public void initialize(AbstractGoogleClientRequest request)
                            throws IOException {
                        request.setDisableGZipContent(true);
                    }
                }
        ).build();
    }

    @Override
    protected void tearDown() throws Exception {
        endpoint = null;
        super.tearDown();
    }

    public void testListEntities() throws Exception {
        final Profile profile = endpoint.getProfile().setOauthToken(credential.getToken()).execute();
        if (profile != null) {
            Log.d(TAG, "id: " + profile.getDisplayName());
        }
    }
}
