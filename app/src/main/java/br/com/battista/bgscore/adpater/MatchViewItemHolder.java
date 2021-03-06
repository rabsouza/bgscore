package br.com.battista.bgscore.adpater;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.battista.bgscore.R;

public class MatchViewItemHolder extends RecyclerView.ViewHolder {

    private ImageView imgInfoGame;
    private TextView txtInfoAlias;
    private ImageView imgInfoAlarm;
    private ImageView imgInfoFeedback;
    private TextView txtInfoNameGame;
    private TextView txtInfoMatchDate;
    private TextView txtInfoPlayers;
    private TextView txtInfoDuration;
    private ImageView imgMoreActions;
    private TextView txtIWon;

    public MatchViewItemHolder(View view) {
        super(view);

        imgInfoGame = view.findViewById(R.id.card_view_match_info_image);
        txtInfoAlias = view.findViewById(R.id.card_view_match_info_alias);
        imgInfoAlarm = view.findViewById(R.id.card_view_match_info_alarm);
        imgInfoFeedback = view.findViewById(R.id.card_view_match_info_feedback);
        txtInfoNameGame = view.findViewById(R.id.card_view_match_info_name_game);
        txtInfoMatchDate = view.findViewById(R.id.card_view_match_info_date);
        txtInfoPlayers = view.findViewById(R.id.card_view_match_info_players);
        txtInfoDuration = view.findViewById(R.id.card_view_match_info_duration);
        imgMoreActions = view.findViewById(R.id.card_view_match_more_actions);
        txtIWon = view.findViewById(R.id.card_view_match_i_won);
    }

    public ImageView getImgInfoGame() {
        return imgInfoGame;
    }

    public TextView getTxtInfoAlias() {
        return txtInfoAlias;
    }

    public TextView getTxtInfoPlayers() {
        return txtInfoPlayers;
    }

    public TextView getTxtInfoNameGame() {
        return txtInfoNameGame;
    }

    public TextView getTxtInfoMatchDate() {
        return txtInfoMatchDate;
    }

    public ImageView getImgMoreActions() {
        return imgMoreActions;
    }

    public ImageView getImgInfoFeedback() {
        return imgInfoFeedback;
    }

    public TextView getTxtInfoDuration() {
        return txtInfoDuration;
    }

    public ImageView getImgInfoAlarm() {
        return imgInfoAlarm;
    }

    public TextView getTxtIWon() {
        return txtIWon;
    }
}
