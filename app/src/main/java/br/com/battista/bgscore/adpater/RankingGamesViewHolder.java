package br.com.battista.bgscore.adpater;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import br.com.battista.bgscore.R;

public class RankingGamesViewHolder extends RecyclerView.ViewHolder {

    private ImageView imgMyGame;
    private ImageView imgInfoGame;
    private ImageView imgInfoBadgeGame;
    private TextView txtInfoName;
    private TextView txtInfoCountMatches;
    private TextView txtInfoSumTime;
    private TextView txtInfoLastPlayed;
    private RatingBar rtbInfoRating;

    public RankingGamesViewHolder(View view) {
        super(view);

        imgInfoBadgeGame = view.findViewById(R.id.card_view_games_info_badge_game);
        imgMyGame = view.findViewById(R.id.card_view_ranking_games_info_my_game);
        imgInfoGame = view.findViewById(R.id.card_view_ranking_games_info_image);
        txtInfoName = view.findViewById(R.id.card_view_ranking_games_info_name);
        txtInfoSumTime = view.findViewById(R.id.card_view_ranking_games_info_sum_time);
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

    public ImageView getImgMyGame() {
        return imgMyGame;
    }

    public TextView getTxtInfoSumTime() {
        return txtInfoSumTime;
    }

    public ImageView getImgInfoBadgeGame() {
        return imgInfoBadgeGame;
    }
}
