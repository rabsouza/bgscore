package br.com.battista.bgscore.adpater;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.common.collect.Sets;

import java.text.MessageFormat;
import java.util.List;
import java.util.Set;

import br.com.battista.bgscore.MainApplication;
import br.com.battista.bgscore.R;
import br.com.battista.bgscore.model.User;
import br.com.battista.bgscore.model.dto.FriendDto;
import br.com.battista.bgscore.util.AndroidUtils;


public class FriendAdapter extends BaseAdapterAnimation<FriendViewHolder> {

    private static final String TAG = FriendAdapter.class.getSimpleName();

    private Context context;
    private List<FriendDto> friends;

    private Set<FriendDto> friendsSelected = Sets.newLinkedHashSet();
    private Boolean allowsDelete;
    private Boolean allowsSelect;

    public FriendAdapter(Context context, List<FriendDto> friends,
                         Boolean allowsDelete, Boolean allowsSelect) {
        super(context);
        this.context = context;
        this.friends = friends;
        this.allowsDelete = allowsDelete;
        this.allowsSelect = allowsSelect;
        friendsSelected.clear();
    }

    public FriendAdapter(Context context, List<FriendDto> friends) {
        this(context, friends, Boolean.TRUE, Boolean.FALSE);
    }

    @Override
    public FriendViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.adapter_friend, viewGroup, false);
        return new FriendViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FriendViewHolder holder, int position) {
        if (friends != null && !friends.isEmpty()) {
            View itemView = holder.itemView;
            setAnimationHolder(itemView, position);

            final FriendDto friendDto = friends.get(position);
            Log.i(TAG, String.format(
                    "onBindViewHolder: Fill to row position: %S with %s.", position, friendDto));

            holder.getTxtTitle().setText(friendDto.getUsername());
            holder.getImgAvatar().setImageResource(friendDto.getAvatar().getIdResDrawable());

            holder.getImgAvatar().setColorFilter(AndroidUtils.generateRandomColor());
            final int positionRemoved = holder.getAdapterPosition();
            if (allowsDelete) {
                holder.getBtnRemove().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        createDialogRemoveFriend(friendDto.getUsername(), positionRemoved);
                    }
                });
            } else {
                holder.getBtnRemove().setVisibility(View.GONE);
            }

            final CardView cardView = itemView.findViewById(R.id.card_view_friend);
            if (allowsSelect) {
                if (friendDto.isSelected()) {
                    cardView.setSelected(Boolean.TRUE);
                    friendsSelected.add(friendDto);
                }
                cardView.setOnClickListener(new View.OnClickListener() {
                    boolean cardSelected = friendDto.isSelected();

                    @Override
                    public void onClick(View view) {
                        cardSelected = !cardSelected;
                        cardView.setSelected(cardSelected);
                        friendDto.selected(cardSelected);
                        if (cardSelected) {
                            friendsSelected.add(friendDto);
                        } else {
                            friendsSelected.remove(friendDto);
                        }
                    }
                });
            } else {
                cardView.setClickable(false);
                cardView.setSelected(false);
            }
        } else {
            Log.w(TAG, "onBindViewHolder: No content to holder!");
        }

    }

    private void createDialogRemoveFriend(final String friend, final int position) {
        String msgDelete = context.getResources().getString(R.string.alert_confirmation_dialog_text_remove_friend);
        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setTitle(R.string.alert_confirmation_dialog_title_delete)
                .setMessage(MessageFormat.format(msgDelete, friend))
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(R.string.btn_confirmation_dialog_remove, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        final MainApplication instance = MainApplication.instance();
                        final User user = instance.getUser();
                        user.removeFriend(new FriendDto().username(friend));
                        instance.setUser(user);

                        friends.remove(position);
                        notifyItemRemoved(position);
                        notifyDataSetChanged();
                    }
                })
                .setNegativeButton(R.string.btn_confirmation_dialog_cancel, null).create();
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.animationAlert;
        alertDialog.show();
    }

    @Override
    public int getItemCount() {
        return friends != null ? friends.size() : 0;
    }

    public Set<FriendDto> getFriendsSelected() {
        return friendsSelected;
    }
}
