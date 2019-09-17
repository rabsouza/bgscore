package br.com.battista.bgscore.fragment.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.common.base.MoreObjects;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;

import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import br.com.battista.bgscore.R;
import br.com.battista.bgscore.adpater.PlayerAdapter;
import br.com.battista.bgscore.constants.BundleConstant;
import br.com.battista.bgscore.constants.CrashlyticsConstant.Actions;
import br.com.battista.bgscore.constants.CrashlyticsConstant.ValueActions;
import br.com.battista.bgscore.custom.RecycleEmptyErrorView;
import br.com.battista.bgscore.model.Game;
import br.com.battista.bgscore.model.Match;
import br.com.battista.bgscore.model.Player;
import br.com.battista.bgscore.service.ScreenShareService;
import br.com.battista.bgscore.util.AndroidUtils;
import br.com.battista.bgscore.util.AnswersUtils;
import br.com.battista.bgscore.util.DateUtils;
import br.com.battista.bgscore.util.ImageLoadUtils;
import br.com.battista.bgscore.util.LogUtils;

import static br.com.battista.bgscore.constants.DialogConstant.DIALOG_SHARE_MATCH_FULL_FRAGMENT;

public class ShareMatchFullDialog extends DialogFragment {

    private static final String DIALOG_SHARE_MATCH_FULL = "dialog_share_match_full";
    private static final String TAG = ShareMatchFullDialog.class.getSimpleName();
    private final List<Player> players = Lists.newLinkedList();
    private Button btnCancel;
    private Button btnShare;
    private Match match;
    private ImageView imgInfoGame;
    private TextView txtInfoTime;
    private TextView txtInfoPlayers;
    private TextView txtInfoAges;
    private TextView txtInfoYear;
    private TextView txtInfoAlias;
    private ImageView imgInfoFeedback;
    private TextView txtInfoFeedback;
    private TextView txtInfoNameGame;
    private TextView txtInfoMatchDate;
    private TextView txtInfoDuration;
    private TextView txtFeedbackObs;
    private CardView cardContent;
    private RecycleEmptyErrorView recycleViewPlayers;

    public ShareMatchFullDialog() {
    }

    public static ShareMatchFullDialog newInstance(Match match) {
        ShareMatchFullDialog fragment = new ShareMatchFullDialog();
        Bundle args = new Bundle();
        args.putSerializable(BundleConstant.DATA, match);
        fragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);
        fragment.setArguments(args);
        return fragment;
    }

    public void showDialog(@NonNull Fragment fragment) {
        LogUtils.i(TAG, "showAbout: Show dialog share match!");

        setTargetFragment(fragment, DIALOG_SHARE_MATCH_FULL_FRAGMENT);

        FragmentManager fm = fragment.getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment prev = fm.findFragmentByTag(DIALOG_SHARE_MATCH_FULL);
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        show(ft, DIALOG_SHARE_MATCH_FULL);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View viewFragment = inflater.inflate(R.layout.dialog_share_match_full, container, false);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        loadViews(viewFragment);
        setupRecycleViewPlayers(viewFragment);

        processDataFragment(viewFragment, getArguments());
        return viewFragment;
    }

    private void processDataFragment(View viewFragment, Bundle bundle) {
        LogUtils.d(TAG, "processDataFragment: Process bundle data Fragment!");
        if (bundle != null && bundle.containsKey(BundleConstant.DATA)) {
            match = (Match) bundle.getSerializable(BundleConstant.DATA);
        } else {
            match = new Match();
            match.initEntity();
            final Game game = new Game();
            game.initEntity();
            match.game(game);
        }
        loadDataMatch();
    }

    private void setupRecycleViewPlayers(View view) {
        recycleViewPlayers = view.findViewById(R.id.card_view_players_recycler_view);
        recycleViewPlayers.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recycleViewPlayers.setItemAnimator(new DefaultItemAnimator());
        recycleViewPlayers.setHasFixedSize(true);
    }

    private void loadDataMatch() {
        LogUtils.i(TAG, "loadDataMatch: Load data match");

        final Game game = match.getGame();
        String urlThumbnail = game.getUrlThumbnail();
        if (Strings.isNullOrEmpty(urlThumbnail)) {
            ImageLoadUtils.loadImage(getContext(),
                    R.drawable.boardgame_game,
                    imgInfoGame);
        } else {
            ImageLoadUtils.loadImage(getContext(),
                    urlThumbnail,
                    imgInfoGame);
        }

        players.addAll(match.getPlayers());
        Collections.sort(players, new Ordering<Player>() {
            @Override
            public int compare(Player left, Player right) {
                return left.compareTo(right);
            }
        });
        recycleViewPlayers.setAdapter(new PlayerAdapter(getContext(),
                players, false, false, true, true, false));

        txtInfoNameGame.setText(game.getName());
        txtInfoYear.setText(
                MoreObjects.firstNonNull(Strings.emptyToNull(game.getYearPublished()), " ****"));

        if (Strings.isNullOrEmpty(game.getMaxPlayers())) {
            txtInfoPlayers.setText(MessageFormat.format("{0}",
                    MoreObjects.firstNonNull(Strings.emptyToNull(game.getMinPlayers()), "1")));
        } else {
            txtInfoPlayers.setText(MessageFormat.format("{0}-{1}",
                    MoreObjects.firstNonNull(Strings.emptyToNull(game.getMinPlayers()), "1"),
                    MoreObjects.firstNonNull(Strings.emptyToNull(game.getMaxPlayers()), "*")));
        }

        if (Strings.isNullOrEmpty(game.getMaxPlayTime())) {
            txtInfoTime.setText(MessageFormat.format("{0}´",
                    MoreObjects.firstNonNull(Strings.emptyToNull(game.getMinPlayTime()), "∞")));
        } else {
            txtInfoTime.setText(MessageFormat.format("{0}-{1}´",
                    MoreObjects.firstNonNull(Strings.emptyToNull(game.getMinPlayTime()), "*"),
                    MoreObjects.firstNonNull(Strings.emptyToNull(game.getMaxPlayTime()), "∞")));
        }

        if (Strings.isNullOrEmpty(game.getAge())) {
            txtInfoAges.setText("-");
        } else {
            txtInfoAges.setText(MessageFormat.format("{0}+", game.getAge()));
        }

        txtInfoAlias.setText(match.getAlias());

        final Calendar createdAt = Calendar.getInstance();
        createdAt.setTime(match.getCreatedAt());
        txtInfoMatchDate.setText(DateUtils.format(createdAt));

        if (match.getDuration() == null) {
            txtInfoDuration.setText("00:00´");
        } else {
            txtInfoDuration.setText(
                    DateUtils.formatTime(match.getDuration()));
        }

        imgInfoFeedback.setImageResource(match.getFeedback().getIdResDrawable());
        final int colorFeedbackDissatisfied = ContextCompat.getColor(getContext(), R.color.colorImgFeedbackDissatisfied);
        final int colorFeedbackNeutral = ContextCompat.getColor(getContext(), R.color.colorImgFeedbackNeutral);
        final int colorFeedbackSatisfied = ContextCompat.getColor(getContext(), R.color.colorImgFeedbackSatisfied);
        switch (match.getFeedback().getIdResDrawable()) {
            case R.drawable.ic_feedback_very_dissatisfied:
                imgInfoFeedback.setColorFilter(
                        colorFeedbackDissatisfied);
                txtInfoFeedback.setText(R.string.feedback_very_dissatisfied);
                txtInfoFeedback.setTextColor(colorFeedbackDissatisfied);
                break;
            case R.drawable.ic_feedback_dissatisfied:
                imgInfoFeedback.setColorFilter(
                        colorFeedbackDissatisfied);
                txtInfoFeedback.setText(R.string.feedback_dissatisfied);
                txtInfoFeedback.setTextColor(colorFeedbackDissatisfied);
                break;
            case R.drawable.ic_feedback_neutral:
                imgInfoFeedback.setColorFilter(
                        colorFeedbackNeutral);
                txtInfoFeedback.setText(R.string.feedback_neutral);
                txtInfoFeedback.setTextColor(colorFeedbackNeutral);
                break;
            case R.drawable.ic_feedback_satisfied:
                imgInfoFeedback.setColorFilter(
                        colorFeedbackSatisfied);
                txtInfoFeedback.setText(R.string.feedback_satisfied);
                txtInfoFeedback.setTextColor(colorFeedbackSatisfied);
                break;
            case R.drawable.ic_feedback_very_satisfied:
                imgInfoFeedback.setColorFilter(
                        colorFeedbackSatisfied);
                txtInfoFeedback.setText(R.string.feedback_very_satisfied);
                txtInfoFeedback.setTextColor(colorFeedbackSatisfied);
                break;
        }

        if (!Strings.isNullOrEmpty(match.getObs())) {
            txtFeedbackObs.setText(match.getObs());
        } else {
            txtFeedbackObs.setVisibility(View.GONE);
        }
    }

    private void loadViews(final View viewFragment) {
        LogUtils.i(TAG, "loadViews: load all views!");

        cardContent = viewFragment.findViewById(R.id.card_view_share_match_full_info);

        btnCancel = viewFragment.findViewById(R.id.share_match_full_btn_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        btnShare = viewFragment.findViewById(R.id.share_match_full_btn_share);
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnswersUtils.onActionMetric(Actions.ACTION_CLICK_BUTTON,
                        ValueActions.VALUE_ACTION_CLICK_BUTTON_SHARE_MATCH_FULL);

                String textShare = MessageFormat.format(
                        viewFragment.getContext().getString(R.string.hint_share_match_full),
                        match.getAlias(), match.getGame().getName());

                new ScreenShareService(viewFragment.getContext()).shareScreen(cardContent, textShare);
                AndroidUtils.toast(viewFragment.getContext(), R.string.msg_share_detail_match);

                dismiss();
            }
        });

        txtInfoAlias = viewFragment.findViewById(R.id.card_view_share_match_full_info_alias);
        imgInfoGame = viewFragment.findViewById(R.id.card_view_share_match_full_info_image);
        imgInfoFeedback = viewFragment.findViewById(R.id.card_view_share_match_full_info_feedback);
        txtInfoFeedback = viewFragment.findViewById(R.id.card_view_share_match_full_info_feedback_text);
        txtFeedbackObs = viewFragment.findViewById(R.id.card_view_share_match_full_obs_value);
        txtInfoNameGame = viewFragment.findViewById(R.id.card_view_share_match_full_info_name_game);
        txtInfoMatchDate = viewFragment.findViewById(R.id.card_view_share_match_full_info_date);
        txtInfoPlayers = viewFragment.findViewById(R.id.card_view_share_match_full_info_players);
        txtInfoDuration = viewFragment.findViewById(R.id.card_view_share_match_full_info_duration);
        txtInfoTime = viewFragment.findViewById(R.id.card_view_share_match_full_info_time);
        txtInfoAges = viewFragment.findViewById(R.id.card_view_share_match_full_info_ages);
        txtInfoYear = viewFragment.findViewById(R.id.card_view_share_match_full_info_year);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setTitle(R.string.dialog_title_share_match);
        dialog.getWindow().getAttributes().windowAnimations = R.style.animationPopup;
        return dialog;
    }

}
