package br.com.battista.bgscore.adpater;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.battista.bgscore.R;

public class AvatarViewHolder extends RecyclerView.ViewHolder {

    private TextView txtTitle;

    private ImageView imgAvatar;

    public AvatarViewHolder(View view) {
        super(view);

        txtTitle = view.findViewById(R.id.card_view_avatar_title);

        imgAvatar = view.findViewById(R.id.card_view_avatar_img);
    }

    public TextView getTxtTitle() {
        return txtTitle;
    }

    public ImageView getImgAvatar() {
        return imgAvatar;
    }

}
