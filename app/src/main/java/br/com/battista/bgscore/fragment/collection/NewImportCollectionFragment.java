package br.com.battista.bgscore.fragment.collection;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.battista.bgscore.R;
import br.com.battista.bgscore.fragment.BaseFragment;
import br.com.battista.bgscore.util.LogUtils;


public class NewImportCollectionFragment extends BaseFragment {

    private static final String TAG = NewImportCollectionFragment.class.getSimpleName();

    public NewImportCollectionFragment() {
    }

    public static NewImportCollectionFragment newInstance() {
        NewImportCollectionFragment fragment = new NewImportCollectionFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LogUtils.d(TAG, "onCreateView: Create new NewImportCollectionFragment!");

        final View view = inflater.inflate(R.layout.fragment_new_import_collection, container, false);

        FloatingActionButton fab = view.findViewById(R.id.fab_next_import_collection);
        fab.setOnClickListener(view1 -> fillDataAndSave());

        return view;
    }

    private void fillDataAndSave() {
    }

}
