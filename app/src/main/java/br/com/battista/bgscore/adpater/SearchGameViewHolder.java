package br.com.battista.bgscore.adpater;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.battista.bgscore.R;

public class SearchGameViewHolder extends RecyclerView.ViewHolder {

    private ImageView imgInfoGame;
    private TextView txtInfoName;
    private TextView txtInfoYear;

    public SearchGameViewHolder(View view) {
        super(view);

        imgInfoGame = view.findViewById(R.id.card_view_games_info_image);
        txtInfoName = view.findViewById(R.id.card_view_games_info_name);
        txtInfoYear = view.findViewById(R.id.card_view_games_info_year);
    }

    public ImageView getImgInfoGame() {
        return imgInfoGame;
    }

    public TextView getTxtInfoName() {
        return txtInfoName;
    }

    public TextView getTxtInfoYear() {
        return txtInfoYear;
    }

}
