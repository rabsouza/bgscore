package br.com.battista.bgscore.adpater;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.battista.bgscore.R;

public class PlayerViewHolder extends RecyclerView.ViewHolder {

    private TextView txtTitle;
    private ImageView imgAvatar;
    private ImageView imgWinner;
    private ImageButton btnRemove;
    private EditText txtPunctuation;
    private ViewGroup punctuationContainer;

    public PlayerViewHolder(View view) {
        super(view);

        punctuationContainer = view.findViewById(R.id.card_view_player_punctuation_container);
        txtPunctuation = view.findViewById(R.id.card_view_player_punctuation);
        txtTitle = view.findViewById(R.id.card_view_player_title);
        btnRemove = view.findViewById(R.id.card_view_player_remove);
        imgAvatar = view.findViewById(R.id.card_view_player_img);
        imgWinner = view.findViewById(R.id.card_view_player_winner_img);
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

    public ImageView getImgWinner() {
        return imgWinner;
    }

    public EditText getTxtPunctuation() {
        return txtPunctuation;
    }

    public ViewGroup getPunctuationContainer() {
        return punctuationContainer;
    }
}
