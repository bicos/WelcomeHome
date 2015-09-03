package com.rhpark.welcomehome;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.rhpark.welcomehome.data.Constants;
import com.rhpark.welcomehome.data.Pref;
import com.rhpark.welcomehome.data.User;
import com.rhpark.welcomehome.fragment.IntroFragment;
import com.rhpark.welcomehome.fragment.MainListFragment;

import java.util.concurrent.TimeUnit;


public class MainActivity extends AppCompatActivity {

    public static final int REQ_CODE_GET_HOME_LOCATION = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        User user = Pref.getUser();
        if (user == null) {
            startIntroFragment();
        } else {
            setMainListFragment(user);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_GET_HOME_LOCATION:

                User user = Pref.getUser();
                setMainListFragment(user);
                break;
        }
    }

    private void startIntroFragment() {
        new AcyncStartIntroFragment(getApplicationContext()).execute();
    }

    private void setIntroFragment(Location lastLocation){
        IntroFragment fragment = IntroFragment.getInstance(lastLocation);
        replaceFragment(fragment);
    }

    private void setMainListFragment(User user){
        MainListFragment fragment = MainListFragment.newInstance(user);
        replaceFragment(fragment);
    }

    public void replaceFragment(Fragment fragment) {
        getFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    public static Intent getLaunchIntent(Context context){
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }

    private class AcyncStartIntroFragment extends AsyncTask<Void, Void, ConnectionResult>{

        private Context context;
        private GoogleApiClient googleApiClient;

        public AcyncStartIntroFragment(Context context) {
            super();
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            googleApiClient = new GoogleApiClient.Builder(context)
                    .addApi(LocationServices.API)
                    .build();
        }

        @Override
        protected ConnectionResult doInBackground(Void... params) {
            // It's OK to use blockingConnect() here as we are running in an
            // IntentService that executes work on a separate (background) thread.
            ConnectionResult connectionResult = googleApiClient.blockingConnect(
                    Constants.GOOGLE_API_CLIENT_TIMEOUT_S, TimeUnit.SECONDS);

            return connectionResult;
        }

        @Override
        protected void onPostExecute(ConnectionResult connectionResult) {
            if (connectionResult.isSuccess() && googleApiClient.isConnected()) {
                Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                setIntroFragment(lastLocation);
            }
        }
    }
}
