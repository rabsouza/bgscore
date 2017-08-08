package br.com.battista.bgscore.adpater;

import com.google.common.collect.Sets;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.MessageFormat;
import java.util.List;
import java.util.Set;

import br.com.battista.bgscore.R;
import br.com.battista.bgscore.model.Player;
import br.com.battista.bgscore.util.AndroidUtils;

import static br.com.battista.bgscore.constants.ViewConstant.SPACE_DRAWABLE;


public class PlayerAdapter extends BaseAdapterAnimation<PlayerViewHolder> {

    private static final String TAG = PlayerAdapter.class.getSimpleName();

    private Context context;
    private List<Player> players;

    private Set<Player> playersWinners = Sets.newLinkedHashSet();
    private Boolean allowsDelete;
    private Boolean allowsSelect;

    public PlayerAdapter(Context context, List<Player> players,
                         Boolean allowsDelete, Boolean allowsSelect) {
        super(context);
        this.context = context;
        this.players = players;
        this.allowsDelete = allowsDelete;
        this.allowsSelect = allowsSelect;
        playersWinners.clear();
    }

    public PlayerAdapter(Context context, List<Player> players) {
        this(context, players, Boolean.TRUE, Boolean.FALSE);
    }

    @Override
    public PlayerViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.adapter_player, viewGroup, false);
        return new PlayerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PlayerViewHolder holder, int position) {
        if (players != null && !players.isEmpty()) {
            View itemView = holder.itemView;
            setAnimationHolder(itemView, position);

            final Player player = players.get(position);
            Log.i(TAG, String.format(
                    "onBindViewHolder: Fill to row position: %S with %s.", position, player));

            holder.getTxtTitle().setText(SPACE_DRAWABLE + player.getName());
            holder.getImgAvatar().setColorFilter(AndroidUtils.generateRandomColor());

            if (allowsDelete) {
                final int positionRemoved = holder.getAdapterPosition();
                holder.getBtnRemove().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        createDialogRemovePlayer(player.getName(), positionRemoved);
                    }
                });
            } else {
                holder.getBtnRemove().setVisibility(View.GONE);
            }

            final CardView cardView = itemView.findViewById(R.id.card_view_player);
            if (allowsSelect) {
                if (player.isWinner()) {
                    cardView.setSelected(Boolean.TRUE);
                    playersWinners.add(player);
                }
                cardView.setOnClickListener(new View.OnClickListener() {
                    boolean cardSelected = player.isWinner();

                    @Override
                    public void onClick(View view) {
                        cardSelected = !cardSelected;
                        cardView.setSelected(cardSelected);
                        player.winner(cardSelected);
                        if (cardSelected) {
                            playersWinners.add(player);
                        } else {
                            playersWinners.remove(player);
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

    private void createDialogRemovePlayer(final String friend, final int position) {
        String msgDelete = context.getResources().getString(R.string.alert_confirmation_dialog_text_remove_player);
        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setTitle(R.string.alert_confirmation_dialog_title_delete)
                .setMessage(MessageFormat.format(msgDelete, friend))
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(R.string.btn_confirmation_dialog_remove, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        players.remove(position);
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
        return players != null ? players.size() : 0;
    }

    public Set<Player> getPlayersWinners() {
        return playersWinners;
    }
}
