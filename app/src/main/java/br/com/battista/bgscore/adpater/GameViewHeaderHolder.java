package br.com.battista.bgscore.adpater;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import br.com.battista.bgscore.R;
import br.com.battista.bgscore.custom.ScoreboardView;

public class GameViewHeaderHolder extends RecyclerView.ViewHolder {

    private ImageView imgHelpGame;

    private ScoreboardView scoreMyGame;
    private ScoreboardView scoreWantGame;
    private ScoreboardView scoreFavorite;

    public GameViewHeaderHolder(View view) {
        super(view);
        scoreMyGame = view.findViewById(R.id.card_view_games_score_my_game);
        scoreFavorite = view.findViewById(R.id.card_view_games_score_favorite);
        scoreWantGame = view.findViewById(R.id.card_view_games_score_want_game);

        imgHelpGame = view.findViewById(R.id.card_view_games_help);
    }

    public ImageView getImgHelpGame() {
        return imgHelpGame;
    }

    public void setImgHelpGame(ImageView imgHelpGame) {
        this.imgHelpGame = imgHelpGame;
    }

    public ScoreboardView getScoreMyGame() {
        return scoreMyGame;
    }

    public void setScoreMyGame(ScoreboardView scoreMyGame) {
        this.scoreMyGame = scoreMyGame;
    }

    public ScoreboardView getScoreWantGame() {
        return scoreWantGame;
    }

    public void setScoreWantGame(ScoreboardView scoreWantGame) {
        this.scoreWantGame = scoreWantGame;
    }

    public ScoreboardView getScoreFavorite() {
        return scoreFavorite;
    }

    public void setScoreFavorite(ScoreboardView scoreFavorite) {
        this.scoreFavorite = scoreFavorite;
    }
}
