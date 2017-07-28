package br.com.battista.bgscore.adpater;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import br.com.battista.bgscore.R;
import br.com.battista.bgscore.model.dto.AvatarDto;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;


public class AvatarAdapter extends BaseAdapterAnimation<AvatarViewHolder> {
    private static final String TAG = AvatarAdapter.class.getSimpleName();
    public static final int OFFSET_RANGE_HEIGHT = 50;

    private Context context;
    private List<AvatarDto> avatars;
    @DrawableRes
    private int currentAvatar;
    private View lastSelectionView;


    public AvatarAdapter(Context context, @DrawableRes int currentAvatar) {
        super(context);
        this.context = context;
        this.currentAvatar = currentAvatar;

        this.avatars = new LinkedList<>();
        avatars.add(new AvatarDto()
                .idResAvatar(R.drawable.avatar_profile)
                .nameAvatar(context.getString(R.string.text_default_username)));
        avatars.add(new AvatarDto()
                .idResAvatar(R.drawable.avatar_boba_fett)
                .nameAvatar(context.getString(R.string.avatar_boba_fett)));
        avatars.add(new AvatarDto()
                .idResAvatar(R.drawable.avatar_fett_jango)
                .nameAvatar(context.getString(R.string.avatar_feet_jango)));
        avatars.add(new AvatarDto()
                .idResAvatar(R.drawable.avatar_jawa)
                .nameAvatar(context.getString(R.string.avatar_jawa)));
        avatars.add(new AvatarDto()
                .idResAvatar(R.drawable.avatar_leia_princess)
                .nameAvatar(context.getString(R.string.avatar_leia_princess)));
        avatars.add(new AvatarDto()
                .idResAvatar(R.drawable.avatar_lobot)
                .nameAvatar(context.getString(R.string.avatar_lobot)));
        avatars.add(new AvatarDto()
                .idResAvatar(R.drawable.avatar_r2d2)
                .nameAvatar(context.getString(R.string.avatar_r2d2)));
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
            itemView.measure(WRAP_CONTENT, WRAP_CONTENT);
            itemView.getLayoutParams().height = itemView.getMeasuredHeight()
                    + getRandomIntInRange(OFFSET_RANGE_HEIGHT);
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

    private int getRandomIntInRange(int offset) {
        return new Random().nextInt(offset);
    }

    public int getCurrentAvatar() {
        return currentAvatar;
    }

    @Override
    public int getItemCount() {
        return avatars != null ? avatars.size() : 0;
    }
}
