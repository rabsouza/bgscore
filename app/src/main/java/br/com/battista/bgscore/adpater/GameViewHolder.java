package br.com.battista.bgscore.adpater;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import br.com.battista.bgscore.R;

public class GameViewHolder extends RecyclerView.ViewHolder {

    private ImageView imgInfoGame;
    private TextView txtInfoName;
    private TextView txtInfoTime;
    private TextView txtInfoPlayers;
    private TextView txtInfoAges;
    private RatingBar rtbInfoRating;
    private ImageView imgMoreActions;

    public GameViewHolder(View view) {
        super(view);

        imgInfoGame = view.findViewById(R.id.card_view_games_info_image);
        txtInfoName = view.findViewById(R.id.card_view_games_info_name);
        txtInfoPlayers = view.findViewById(R.id.card_view_games_info_players);
        txtInfoTime = view.findViewById(R.id.card_view_games_info_time);
        txtInfoAges = view.findViewById(R.id.card_view_games_info_ages);
        rtbInfoRating = view.findViewById(R.id.card_view_games_info_rating);
        imgMoreActions = view.findViewById(R.id.card_view_games_more_actions);
    }

    public ImageView getImgInfoGame() {
        return imgInfoGame;
    }

    public TextView getTxtInfoName() {
        return txtInfoName;
    }

    public TextView getTxtInfoPlayers() {
        return txtInfoPlayers;
    }

    public TextView getTxtInfoTime() {
        return txtInfoTime;
    }

    public TextView getTxtInfoAges() {
        return txtInfoAges;
    }

    public RatingBar getRtbInfoRating() {
        return rtbInfoRating;
    }

    public ImageView getImgMoreActions() {
        return imgMoreActions;
    }
}
