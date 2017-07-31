package br.com.battista.bgscore.adpater;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import br.com.battista.bgscore.R;

public class RankingGamesViewHolder extends RecyclerView.ViewHolder {

    private ImageView imgInfoGame;
    private TextView txtInfoName;
    private TextView txtInfoCountMatches;
    private TextView txtInfoLastPlayed;
    private RatingBar rtbInfoRating;

    public RankingGamesViewHolder(View view) {
        super(view);

        imgInfoGame = view.findViewById(R.id.card_view_ranking_games_info_image);
        txtInfoName = view.findViewById(R.id.card_view_ranking_games_info_name);
        txtInfoLastPlayed = view.findViewById(R.id.card_view_ranking_games_info_last_played);
        txtInfoCountMatches = view.findViewById(R.id.card_view_ranking_games_info_count_matches);
        rtbInfoRating = view.findViewById(R.id.card_view_ranking_games_info_rating);
    }

    public ImageView getImgInfoGame() {
        return imgInfoGame;
    }

    public TextView getTxtInfoName() {
        return txtInfoName;
    }

    public TextView getTxtInfoLastPlayed() {
        return txtInfoLastPlayed;
    }

    public TextView getTxtInfoCountMatches() {
        return txtInfoCountMatches;
    }

    public RatingBar getRtbInfoRating() {
        return rtbInfoRating;
    }

}
