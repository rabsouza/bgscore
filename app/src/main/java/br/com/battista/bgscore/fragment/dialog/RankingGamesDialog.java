package br.com.battista.bgscore.fragment.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;

import java.util.Collections;
import java.util.List;

import br.com.battista.bgscore.MainApplication;
import br.com.battista.bgscore.R;
import br.com.battista.bgscore.adpater.RankingGamesAdapter;
import br.com.battista.bgscore.custom.RecycleEmptyErrorView;
import br.com.battista.bgscore.model.User;
import br.com.battista.bgscore.model.dto.RankingGamesDto;
import br.com.battista.bgscore.util.LogUtils;

import static br.com.battista.bgscore.constants.DialogConstant.DIALOG_RANKING_GAMES_FRAGMENT;

public class RankingGamesDialog extends DialogFragment {

    public static final String DIALOG_RANKING_GAMES = "dialog_ranking_games";
    private static final String TAG = RankingGamesDialog.class.getSimpleName();

    private RecycleEmptyErrorView recycleViewRankingGames;
    private TextView emptyMsgRankingGames;
    private TextView errorMsgRankingGames;
    private ImageView imgClose;

    public RankingGamesDialog() {
    }

    public static RankingGamesDialog newInstance() {
        RankingGamesDialog fragment = new RankingGamesDialog();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        fragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);
        return fragment;
    }

    public void showDialog(@NonNull Fragment fragment) {
        LogUtils.i(TAG, "showAbout: Show dialog ranking games!");

        setTargetFragment(fragment, DIALOG_RANKING_GAMES_FRAGMENT);

        FragmentManager fm = fragment.getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment prev = fm.findFragmentByTag(DIALOG_RANKING_GAMES);
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        show(ft, DIALOG_RANKING_GAMES);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View viewFragment = inflater.inflate(R.layout.dialog_ranking_games, container, false);

        loadViews(viewFragment);
        loadAllRankingGames();
        return viewFragment;
    }

    private void loadAllRankingGames() {
        LogUtils.i(TAG, "loadAllRankingGames: Load all Ranking Games in BD!");
        User user = MainApplication.instance().getUser();

        List<RankingGamesDto> matches = Lists.newLinkedList(user.getRankingGames());
        Collections.sort(matches, new Ordering<RankingGamesDto>() {
            @Override
            public int compare(RankingGamesDto left, RankingGamesDto right) {
                return left.compareTo(right);
            }
        });
        Collections.reverse(matches);

        final RankingGamesAdapter rankingGamesAdapter = new RankingGamesAdapter(getContext(), matches);
        recycleViewRankingGames.setAdapter(rankingGamesAdapter);

    }

    private void loadViews(final View viewFragment) {
        LogUtils.i(TAG, "loadViews: load all views!");

        imgClose = viewFragment.findViewById(R.id.card_view_ranking_games_close);
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        recycleViewRankingGames = viewFragment.findViewById(R.id.card_view_ranking_games_recycler_view);
        emptyMsgRankingGames = viewFragment.findViewById(R.id.card_view_ranking_games_empty_view);
        errorMsgRankingGames = viewFragment.findViewById(R.id.card_view_ranking_games_error_view);
        recycleViewRankingGames.setEmptyView(emptyMsgRankingGames);
        recycleViewRankingGames.setErrorView(errorMsgRankingGames);

        recycleViewRankingGames.setLayoutManager(new LinearLayoutManager(getContext()));
        recycleViewRankingGames.setItemAnimator(new DefaultItemAnimator());
        recycleViewRankingGames.setHasFixedSize(true);
        recycleViewRankingGames.setItemViewCacheSize(30);
        recycleViewRankingGames.setDrawingCacheEnabled(true);
        recycleViewRankingGames.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setTitle(R.string.dialog_title_ranking_games);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);

        dialog.getWindow().getAttributes().windowAnimations = R.style.animationPopup;
        return dialog;
    }

}
