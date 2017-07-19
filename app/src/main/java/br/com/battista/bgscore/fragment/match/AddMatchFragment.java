package br.com.battista.bgscore.fragment.match;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.battista.bgscore.R;
import br.com.battista.bgscore.fragment.BaseFragment;

public class AddMatchFragment extends BaseFragment {
    private static final String TAG = AddMatchFragment.class.getSimpleName();

    public AddMatchFragment() {
    }

    public static AddMatchFragment newInstance() {
        AddMatchFragment fragment = new AddMatchFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: Create new fragment Profile!");

        final View view = inflater.inflate(R.layout.fragment_add_match, container, false);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab_next_edit_match);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        return view;
    }

}
