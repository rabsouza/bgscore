package br.com.battista.bgscore.fragment.dialog;

import static br.com.battista.bgscore.constants.DialogConstant.DIALOG_SEARCH_GAME_FRAGMENT;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

import br.com.battista.bgscore.R;
import br.com.battista.bgscore.adpater.SearchGameAdapter;
import br.com.battista.bgscore.constants.BundleConstant;
import br.com.battista.bgscore.custom.RecycleEmptyErrorView;
import br.com.battista.bgscore.model.response.GameResponse;
import br.com.battista.bgscore.util.LogUtils;

public class SearchGameDialog extends DialogFragment {

    public static final String DIALOG_SEARCH_GAME = "dialog_search_game";
    private static final String TAG = SearchGameDialog.class.getSimpleName();
    private RecycleEmptyErrorView recycleViewGames;
    private List<GameResponse> games = Lists.newArrayList();

    private ImageView btnCancel;

    public SearchGameDialog() {
    }

    public static SearchGameDialog newInstance(ArrayList<GameResponse> games) {
        SearchGameDialog fragment = new SearchGameDialog();
        Bundle args = new Bundle();
        args.putParcelableArrayList(BundleConstant.DATA, games);
        fragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);
        fragment.setArguments(args);
        return fragment;
    }

    public void showDialog(@NonNull Fragment fragment) {
        LogUtils.i(TAG, "showAbout: Show dialog search game!");

        setTargetFragment(fragment, DIALOG_SEARCH_GAME_FRAGMENT);

        FragmentManager fm = fragment.getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment prev = fm.findFragmentByTag(DIALOG_SEARCH_GAME);
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        show(ft, DIALOG_SEARCH_GAME);
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        FragmentTransaction ft = manager.beginTransaction();
        ft.add(this, tag);
        ft.commitAllowingStateLoss();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View viewFragment = inflater.inflate(R.layout.dialog_search_game, container, false);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        setupRecycleGames(viewFragment);
        loadViews(viewFragment);

        processDataFragment(viewFragment, getArguments());
        return viewFragment;
    }

    private void processDataFragment(View viewFragment, Bundle bundle) {
        LogUtils.d(TAG, "processDataFragment: Process bundle data Fragment!");
        if (bundle != null && bundle.containsKey(BundleConstant.DATA)) {
            games.clear();
            final ArrayList<GameResponse> parcelables = bundle.getParcelableArrayList(BundleConstant.DATA);
            games.addAll(parcelables);
        }
        loadAllGames();
    }

    private void loadAllGames() {
        LogUtils.i(TAG, "loadAllMatches: Load all Games in Server!");
        recycleViewGames.setAdapter(new SearchGameAdapter(getContext(), games));
    }

    private void setupRecycleGames(View view) {
        recycleViewGames = view.findViewById(R.id.search_game_recycler_view);
        recycleViewGames.setLayoutManager(new GridLayoutManager(getContext(), 1));
        recycleViewGames.setItemAnimator(new DefaultItemAnimator());
        recycleViewGames.setHasFixedSize(true);
    }

    private void loadViews(View viewFragment) {
        LogUtils.i(TAG, "loadViews: load all views!");

        btnCancel = viewFragment.findViewById(R.id.search_game_btn_close);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setTitle(R.string.dialog_title_search_game);
        dialog.getWindow().getAttributes().windowAnimations = R.style.animationPopup;
        return dialog;
    }

}
