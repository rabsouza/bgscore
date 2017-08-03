package br.com.battista.bgscore.adpater;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.battista.bgscore.R;

public class PlayerViewHolder extends RecyclerView.ViewHolder {

    private TextView txtTitle;
    private ImageView imgAvatar;
    private ImageButton btnRemove;

    public PlayerViewHolder(View view) {
        super(view);

        txtTitle = view.findViewById(R.id.card_view_player_title);
        btnRemove = view.findViewById(R.id.card_view_player_remove);
        imgAvatar = view.findViewById(R.id.card_view_player_img);
    }

    public TextView getTxtTitle() {
        return txtTitle;
    }

    public ImageView getImgAvatar() {
        return imgAvatar;
    }

    public ImageButton getBtnRemove() {
        return btnRemove;
    }

}
