package br.com.battista.bgscore.adpater;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import br.com.battista.bgscore.R;
import br.com.battista.bgscore.model.dto.AvatarDto;
import br.com.battista.bgscore.repository.AvatarRepository;


public class AvatarAdapter extends BaseAdapterAnimation<AvatarViewHolder> {
    public static final int OFFSET_RANGE_HEIGHT = 50;
    private static final String TAG = AvatarAdapter.class.getSimpleName();
    private Context context;
    private List<AvatarDto> avatars;
    @DrawableRes
    private int currentAvatar;
    private View lastSelectionView;


    public AvatarAdapter(Context context, @DrawableRes int currentAvatar) {
        super(context);
        this.context = context;
        this.currentAvatar = currentAvatar;
        this.avatars = new AvatarRepository().findAll();
    }

    @Override
    public AvatarViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.adapter_avatar, viewGroup, false);
        return new AvatarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AvatarViewHolder holder, int position) {
        if (avatars != null && !avatars.isEmpty()) {
            View itemView = holder.itemView;
            setAnimationHolder(itemView, position);
            final CardView cardView = itemView.findViewById(R.id.card_view_avatar);

            final AvatarDto avatarDto = avatars.get(position);
            Log.i(TAG, String.format(
                    "onBindViewHolder: Fill to row position: %S with %s.", position, avatarDto));

            holder.getTxtTitle().setText(avatarDto.getNameAvatar());
            holder.getImgAvatar().setImageResource(avatarDto.getIdResAvatar());
            if (currentAvatar == avatarDto.getIdResAvatar()) {
                lastSelectionView = cardView;
                cardView.setSelected(true);
            } else {
                cardView.setSelected(false);
            }

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (lastSelectionView != null) {
                        lastSelectionView.setSelected(false);
                    }
                    lastSelectionView = cardView;
                    cardView.setSelected(true);
                    currentAvatar = avatarDto.getIdResAvatar();
                }
            });
        } else {
            Log.w(TAG, "onBindViewHolder: No content to holder!");
        }

    }

    public int getCurrentAvatar() {
        return currentAvatar;
    }

    @Override
    public int getItemCount() {
        return avatars != null ? avatars.size() : 0;
    }
}
