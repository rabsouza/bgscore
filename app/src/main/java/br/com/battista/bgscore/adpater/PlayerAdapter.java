package br.com.battista.bgscore.adpater;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import com.google.common.base.Strings;
import com.google.common.collect.Sets;

import java.text.MessageFormat;
import java.util.List;
import java.util.Set;

import br.com.battista.bgscore.R;
import br.com.battista.bgscore.model.Player;
import br.com.battista.bgscore.model.enuns.TypePlayerEnum;
import br.com.battista.bgscore.util.AndroidUtils;
import br.com.battista.bgscore.util.LogUtils;

public class PlayerAdapter extends BaseAdapterAnimation<PlayerViewHolder> {

    private static final String TAG = PlayerAdapter.class.getSimpleName();

    private Context context;
    private List<Player> players;

    private Set<Player> playersWinners = Sets.newLinkedHashSet();
    private Boolean allowsDelete;
    private Boolean allowsSelect;
    private Boolean showWinner;
    private Boolean showPunctuation;
    private Boolean editPunctuation;

    public PlayerAdapter(Context context, List<Player> players,
                         Boolean allowsDelete, Boolean allowsSelect,
                         Boolean showWinner, Boolean showPunctuation,
                         Boolean editPunctuation) {
        super(context);
        this.context = context;
        this.players = players;
        this.allowsDelete = allowsDelete;
        this.allowsSelect = allowsSelect;
        this.showWinner = showWinner;
        this.showPunctuation = showPunctuation;
        this.editPunctuation = editPunctuation;
        playersWinners.clear();
    }

    public PlayerAdapter(Context context, List<Player> players,
                         Boolean allowsDelete, Boolean allowsSelect,
                         Boolean showWinner) {
        this(context, players, allowsDelete, allowsSelect,
                showWinner, Boolean.FALSE, Boolean.FALSE);

    }

    public PlayerAdapter(Context context, List<Player> players) {
        this(context, players, Boolean.TRUE, Boolean.FALSE, Boolean.FALSE);
    }

    @Override
    public PlayerViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.adapter_player, viewGroup, false);
        return new PlayerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PlayerViewHolder holder, int position) {
        if (players != null && !players.isEmpty()) {
            View itemView = holder.itemView;
            setAnimationHolder(itemView, position);

            final Player player = players.get(position);
            LogUtils.i(TAG, String.format(
                    "onBindViewHolder: Fill to row position: %S with %s.", position, player.getName()));

            holder.getTxtTitle().setText(player.getName());
            int colorPlayer = AndroidUtils.generateRandomColor();
            TypePlayerEnum typePlayer = player.getTypePlayer();
            if (showWinner && player.isWinner()) {
                holder.getImgWinner().setImageResource(R.drawable.ic_winner);
                holder.getImgWinner().setColorFilter(colorPlayer);
                holder.getImgAvatar().setVisibility(View.GONE);
            } else {
                holder.getImgWinner().setVisibility(View.GONE);
                if (typePlayer == null || TypePlayerEnum.PLAYER.equals(typePlayer)) {
                    holder.getImgAvatar().setImageResource(R.drawable.ic_player);
                } else if (TypePlayerEnum.FRIEND.equals(typePlayer)) {
                    holder.getImgAvatar().setImageResource(R.drawable.ic_friend);
                } else if (TypePlayerEnum.USER.equals(typePlayer)) {
                    holder.getImgAvatar().setImageResource(R.drawable.ic_username);
                }
                holder.getImgAvatar().setColorFilter(colorPlayer);
            }

            if (editPunctuation) {
                holder.getPunctuationContainer().setVisibility(View.VISIBLE);
                holder.getTxtPunctuation().setText(player.getPunctuation());
                if (!Strings.isNullOrEmpty(player.getPunctuation())) {
                    holder.getTxtPunctuation()
                            .setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_player_check, 0);
                }
                holder.getTxtPunctuation().setOnEditorActionListener((textView, actionId, keyEvent) -> {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        fillPunctuationPlayer(player, holder.getTxtPunctuation());
                    } else {
                        holder.getTxtPunctuation()
                                .setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    }
                    return false;
                });
                holder.getTxtPunctuation().addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        boolean isPunctuationDone = !Strings.isNullOrEmpty(player.getPunctuation());
                        if (isPunctuationDone) {
                            holder.getTxtPunctuation()
                                    .setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                            player.punctuation(null);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        fillPunctuationPlayer(player, holder.getTxtPunctuation());
                    }
                });
            } else if (showPunctuation && !Strings.isNullOrEmpty(player.getPunctuation())) {
                holder.getTxtTitle().setText(
                        context.getString(R.string.text_player_with_punctuation,
                                player.getName(), player.getPunctuation()));
            }

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
            LogUtils.w(TAG, "onBindViewHolder: No content to holder!");
        }

    }

    private void fillPunctuationPlayer(Player player, EditText txtPunctuation) {
        String punctuation = txtPunctuation.getText().toString().trim();
        player.punctuation(punctuation);
        if (!Strings.isNullOrEmpty(punctuation)) {
            txtPunctuation
                    .setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_player_check, 0);
        } else {
            txtPunctuation
                    .setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
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
