package br.com.battista.bgscore.adpater;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import br.com.battista.bgscore.R;
import br.com.battista.bgscore.custom.ScoreboardView;

public class MatchViewHeaderHolder extends RecyclerView.ViewHolder {

    private ImageView imgHelpMatch;

    private ScoreboardView scoreMatchVeryDissatisfied;
    private ScoreboardView scoreMatchDissatisfied;
    private ScoreboardView scoreMatchNeutral;
    private ScoreboardView scoreMatchSatisfied;
    private ScoreboardView scoreMatchVerySatisfied;

    public MatchViewHeaderHolder(View view) {
        super(view);

        imgHelpMatch = view.findViewById(R.id.card_view_matches_help);

        scoreMatchVeryDissatisfied = view.findViewById(R.id.card_view_matches_score_very_dissatisfied);
        scoreMatchDissatisfied = view.findViewById(R.id.card_view_matches_score_dissatisfied);
        scoreMatchNeutral = view.findViewById(R.id.card_view_matches_score_neutral);
        scoreMatchSatisfied = view.findViewById(R.id.card_view_matches_score_satisfied);
        scoreMatchVerySatisfied = view.findViewById(R.id.card_view_matches_score_very_satisfied);
    }

    public ImageView getImgHelpMatch() {
        return imgHelpMatch;
    }

    public void setImgHelpMatch(ImageView imgHelpMatch) {
        this.imgHelpMatch = imgHelpMatch;
    }

    public ScoreboardView getScoreMatchVeryDissatisfied() {
        return scoreMatchVeryDissatisfied;
    }

    public void setScoreMatchVeryDissatisfied(ScoreboardView scoreMatchVeryDissatisfied) {
        this.scoreMatchVeryDissatisfied = scoreMatchVeryDissatisfied;
    }

    public ScoreboardView getScoreMatchDissatisfied() {
        return scoreMatchDissatisfied;
    }

    public void setScoreMatchDissatisfied(ScoreboardView scoreMatchDissatisfied) {
        this.scoreMatchDissatisfied = scoreMatchDissatisfied;
    }

    public ScoreboardView getScoreMatchNeutral() {
        return scoreMatchNeutral;
    }

    public void setScoreMatchNeutral(ScoreboardView scoreMatchNeutral) {
        this.scoreMatchNeutral = scoreMatchNeutral;
    }

    public ScoreboardView getScoreMatchSatisfied() {
        return scoreMatchSatisfied;
    }

    public void setScoreMatchSatisfied(ScoreboardView scoreMatchSatisfied) {
        this.scoreMatchSatisfied = scoreMatchSatisfied;
    }

    public ScoreboardView getScoreMatchVerySatisfied() {
        return scoreMatchVerySatisfied;
    }

    public void setScoreMatchVerySatisfied(ScoreboardView scoreMatchVerySatisfied) {
        this.scoreMatchVerySatisfied = scoreMatchVerySatisfied;
    }
}
