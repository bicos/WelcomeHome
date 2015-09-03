package com.rhpark.welcomehome.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rhpark.welcomehome.R;
import com.rhpark.welcomehome.adapter.MainListAdapter;
import com.rhpark.welcomehome.data.User;

/**
 *
 */
public class MainListFragment extends Fragment {

    private static final String ARG_USER = "user";

    private User mUser;

    public static MainListFragment newInstance(User user) {
        MainListFragment fragment = new MainListFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_USER, user);
        fragment.setArguments(args);
        return fragment;
    }

    public MainListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUser = getArguments().getParcelable(ARG_USER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_list, container, false);

        RecyclerView mainList = (RecyclerView) view.findViewById(R.id.main_list);
        mainList.setLayoutManager(new LinearLayoutManager(getActivity()));

        MainListAdapter adapter = new MainListAdapter(mUser);
        mainList.setAdapter(adapter);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

}
