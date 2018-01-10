package br.com.battista.bgscore.adpater;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import br.com.battista.bgscore.R;

public class GameViewHolder extends RecyclerView.ViewHolder {

    private ImageView imgInfoGame;
    private ImageView imgInfoBadgeGame;
    private TextView txtInfoName;
    private TextView txtInfoTime;
    private TextView txtInfoPlayers;
    private TextView txtInfoAges;
    private TextView txtInfoYear;
    private RatingBar rtbInfoRating;
    private ImageView imgMoreActions;
    private ImageView imgMyGame;
    private ImageView imgFavorite;
    private ImageView imgWantGame;

    public GameViewHolder(View view) {
        super(view);

        imgInfoGame = view.findViewById(R.id.card_view_games_info_image);
        imgInfoBadgeGame = view.findViewById(R.id.card_view_games_info_badge_game);
        txtInfoName = view.findViewById(R.id.card_view_games_info_name);
        txtInfoPlayers = view.findViewById(R.id.card_view_games_info_players);
        txtInfoTime = view.findViewById(R.id.card_view_games_info_time);
        txtInfoAges = view.findViewById(R.id.card_view_games_info_ages);
        txtInfoYear = view.findViewById(R.id.card_view_games_info_year);
        rtbInfoRating = view.findViewById(R.id.card_view_games_info_rating);
        imgMoreActions = view.findViewById(R.id.card_view_games_more_actions);
        imgMyGame = view.findViewById(R.id.card_view_games_info_my_game);
        imgFavorite = view.findViewById(R.id.card_view_games_info_favorite);
        imgWantGame = view.findViewById(R.id.card_view_games_info_want_game);
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

    public TextView getTxtInfoYear() {
        return txtInfoYear;
    }

    public ImageView getImgMyGame() {
        return imgMyGame;
    }

    public ImageView getImgFavorite() {
        return imgFavorite;
    }

    public ImageView getImgInfoBadgeGame() {
        return imgInfoBadgeGame;
    }

    public ImageView getImgWantGame() {
        return imgWantGame;
    }
}
