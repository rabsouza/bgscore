package br.com.battista.bgscore.fragment.dialog;

import static br.com.battista.bgscore.constants.DialogConstant.DIALOG_SHARE_MATCH_FRAGMENT;
import static br.com.battista.bgscore.constants.ViewConstant.SPACE_DRAWABLE;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.common.base.MoreObjects;
import com.google.common.base.Strings;

import java.text.MessageFormat;
import java.util.Calendar;

import br.com.battista.bgscore.R;
import br.com.battista.bgscore.constants.BundleConstant;
import br.com.battista.bgscore.constants.CrashlyticsConstant.Actions;
import br.com.battista.bgscore.constants.CrashlyticsConstant.ValueActions;
import br.com.battista.bgscore.model.Game;
import br.com.battista.bgscore.model.Match;
import br.com.battista.bgscore.service.ScreenShareService;
import br.com.battista.bgscore.util.AndroidUtils;
import br.com.battista.bgscore.util.AnswersUtils;
import br.com.battista.bgscore.util.DateUtils;
import br.com.battista.bgscore.util.ImageLoadUtils;

public class ShareMatchDialog extends DialogFragment {

    private static final String TAG = ShareMatchDialog.class.getSimpleName();
    public static final String DIALOG_SHARE_MATCH = "dialog_share_match";

    private Button btnCancel;
    private Button btnShare;
    private Match match;

    private ImageView imgInfoGame;
    private TextView txtInfoTime;
    private TextView txtInfoPlayers;
    private TextView txtInfoPlayersMatch;
    private TextView txtInfoAges;
    private TextView txtInfoYear;
    private TextView txtInfoAlias;
    private ImageView imgInfoFeedback;
    private TextView txtInfoFeedback;
    private TextView txtInfoNameGame;
    private TextView txtInfoMatchDate;
    private TextView txtInfoDuration;

    private CardView cardContent;

    public ShareMatchDialog() {
    }

    public static ShareMatchDialog newInstance(Match match) {
        ShareMatchDialog fragment = new ShareMatchDialog();
        Bundle args = new Bundle();
        args.putSerializable(BundleConstant.DATA, match);
        fragment.setArguments(args);
        return fragment;
    }

    public void showDialog(@NonNull Fragment fragment) {
        Log.i(TAG, "showAbout: Show dialog share match!");

        setTargetFragment(fragment, DIALOG_SHARE_MATCH_FRAGMENT);

        FragmentManager fm = fragment.getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment prev = fm.findFragmentByTag(DIALOG_SHARE_MATCH);
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        show(ft, DIALOG_SHARE_MATCH);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View viewFragment = inflater.inflate(R.layout.dialog_share_simple_match, container, false);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        loadViews(viewFragment);

        processDataFragment(viewFragment, getArguments());
        return viewFragment;
    }

    private void processDataFragment(View viewFragment, Bundle bundle) {
        Log.d(TAG, "processDataFragment: Process bundle data Fragment!");
        if (bundle.containsKey(BundleConstant.DATA)) {
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

    private void loadDataMatch() {
        Log.i(TAG, "loadDataMatch: Load data match");

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

        txtInfoNameGame.setText(game.getName());
        txtInfoYear.setText(
                MoreObjects.firstNonNull(SPACE_DRAWABLE + Strings.emptyToNull(game.getYearPublished()), " ****"));

        if (Strings.isNullOrEmpty(game.getMaxPlayers())) {
            txtInfoPlayers.setText(MessageFormat.format(" {0}",
                    MoreObjects.firstNonNull(Strings.emptyToNull(game.getMinPlayers()), "1")));
        } else {
            txtInfoPlayers.setText(MessageFormat.format(" {0}-{1}",
                    MoreObjects.firstNonNull(Strings.emptyToNull(game.getMinPlayers()), "1"),
                    MoreObjects.firstNonNull(Strings.emptyToNull(game.getMaxPlayers()), "*")));
        }

        if (Strings.isNullOrEmpty(game.getMaxPlayTime())) {
            txtInfoTime.setText(MessageFormat.format(" {0}´",
                    MoreObjects.firstNonNull(Strings.emptyToNull(game.getMinPlayTime()), "∞")));
        } else {
            txtInfoTime.setText(MessageFormat.format(" {0}-{1}´",
                    MoreObjects.firstNonNull(Strings.emptyToNull(game.getMinPlayTime()), "*"),
                    MoreObjects.firstNonNull(Strings.emptyToNull(game.getMaxPlayTime()), "∞")));
        }

        if (Strings.isNullOrEmpty(game.getAge())) {
            txtInfoAges.setText(" -");
        } else {
            txtInfoAges.setText(MessageFormat.format(" {0}+", game.getAge()));
        }

        txtInfoAlias.setText(SPACE_DRAWABLE + match.getAlias());

        final Calendar createdAt = Calendar.getInstance();
        createdAt.setTime(match.getCreatedAt());
        txtInfoMatchDate.setText(SPACE_DRAWABLE + DateUtils.format(createdAt));
        txtInfoPlayersMatch.setText(SPACE_DRAWABLE + match.getPlayers().size());

        if (match.getDuration() == null) {
            txtInfoDuration.setText("00:00´");
        } else {
            txtInfoDuration.setText(SPACE_DRAWABLE +
                    DateUtils.formatTime(match.getDuration()));
        }

        imgInfoFeedback.setImageResource(match.getFeedbackIdRes());
        switch (match.getFeedbackIdRes()) {
            case R.drawable.ic_feedback_very_dissatisfied:
                txtInfoFeedback.setText(R.string.feedback_very_dissatisfied);
                break;
            case R.drawable.ic_feedback_dissatisfied:
                txtInfoFeedback.setText(R.string.feedback_dissatisfied);
                break;
            case R.drawable.ic_feedback_neutral:
                txtInfoFeedback.setText(R.string.feedback_neutral);
                break;
            case R.drawable.ic_feedback_satisfied:
                txtInfoFeedback.setText(R.string.feedback_satisfied);
                break;
            case R.drawable.ic_feedback_very_satisfied:
                txtInfoFeedback.setText(R.string.feedback_very_satisfied);
                break;
        }
    }

    private void loadViews(final View viewFragment) {
        Log.i(TAG, "loadViews: load all views!");

        cardContent = viewFragment.findViewById(R.id.card_view_share_match_info);

        btnCancel = viewFragment.findViewById(R.id.share_match_btn_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        btnShare = viewFragment.findViewById(R.id.share_match_btn_share);
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnswersUtils.onActionMetric(Actions.ACTION_CLICK_BUTTON,
                        ValueActions.VALUE_ACTION_CLICK_BUTTON_SHARE_MATCH);

                String textShare = MessageFormat.format(
                        viewFragment.getContext().getString(R.string.hint_share_match),
                        match.getAlias());

                new ScreenShareService(viewFragment.getContext()).shareScreen(cardContent, textShare);
                AndroidUtils.toast(viewFragment.getContext(), R.string.msg_share_detail_match);

                dismiss();
            }
        });

        txtInfoAlias = viewFragment.findViewById(R.id.card_view_share_match_info_alias);
        imgInfoGame = viewFragment.findViewById(R.id.card_view_share_match_info_image);
        imgInfoFeedback = viewFragment.findViewById(R.id.card_view_share_match_info_feedback);
        txtInfoFeedback = viewFragment.findViewById(R.id.card_view_share_match_info_feedback_text);
        txtInfoNameGame = viewFragment.findViewById(R.id.card_view_share_match_info_name_game);
        txtInfoMatchDate = viewFragment.findViewById(R.id.card_view_share_match_info_date);
        txtInfoPlayers = viewFragment.findViewById(R.id.card_view_share_match_info_players);
        txtInfoPlayersMatch = viewFragment.findViewById(R.id.card_view_share_match_info_players_match);
        txtInfoDuration = viewFragment.findViewById(R.id.card_view_share_match_info_duration);
        txtInfoTime = viewFragment.findViewById(R.id.card_view_share_match_info_time);
        txtInfoAges = viewFragment.findViewById(R.id.card_view_share_match_info_ages);
        txtInfoYear = viewFragment.findViewById(R.id.card_view_share_match_info_year);
    }

}
