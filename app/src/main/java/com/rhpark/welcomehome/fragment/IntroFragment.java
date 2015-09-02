package com.rhpark.welcomehome.fragment;

import android.app.Fragment;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.rhpark.welcomehome.MainActivity;
import com.rhpark.welcomehome.R;
import com.rhpark.welcomehome.SelectLocationActivity;

/**
 * Created by rhpark on 2015. 9. 2..
 */
public class IntroFragment extends Fragment {

    private final static String PARAM_LAST_LOCATION = "last_location";

    private Button goSelectLocation;
    private Location mLastLocation;

    public IntroFragment() {
    }

    public static IntroFragment getInstance(Location location){

        IntroFragment fragment = new IntroFragment();

        Bundle b = new Bundle();
        b.putParcelable(PARAM_LAST_LOCATION, location);
        fragment.setArguments(b);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mLastLocation = getArguments().getParcelable(PARAM_LAST_LOCATION);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_intro, container, false);

        goSelectLocation = (Button) view.findViewById(R.id.btnSelectLocation);
        goSelectLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSelectLocation();
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void startSelectLocation() {
        SelectLocationActivity.startSelectLocationActivity(getActivity(),
                mLastLocation,
                MainActivity.REQ_CODE_GET_HOME_LOCATION);
    }
}
