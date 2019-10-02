package br.com.battista.bgscore.fragment.collection;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;

import com.google.common.base.Strings;

import java.text.DecimalFormat;
import java.util.List;

import br.com.battista.bgscore.R;
import br.com.battista.bgscore.activity.HomeActivity;
import br.com.battista.bgscore.constants.CrashlyticsConstant;
import br.com.battista.bgscore.custom.ProgressApp;
import br.com.battista.bgscore.fragment.BaseFragment;
import br.com.battista.bgscore.model.Game;
import br.com.battista.bgscore.model.dto.ActionCollection;
import br.com.battista.bgscore.model.enuns.ActionCollectionEnum;
import br.com.battista.bgscore.service.Inject;
import br.com.battista.bgscore.service.server.CollectionService;
import br.com.battista.bgscore.util.AndroidUtils;
import br.com.battista.bgscore.util.AnswersUtils;
import br.com.battista.bgscore.util.LogUtils;

import static br.com.battista.bgscore.constants.BundleConstant.NAVIGATION_TO;
import static br.com.battista.bgscore.constants.BundleConstant.NavigationTo.GAME_FRAGMENT;


public class NewImportCollectionFragment extends BaseFragment {

    private static final String TAG = NewImportCollectionFragment.class.getSimpleName();

    private ImageButton btnSearchUsername;
    private EditText txtSearchUsername;

    private List<Game> gamesOwnResponses;
    private EditText txtGamesOwn;
    private Switch swtGamesOwn;

    private List<Game> gamesWishlistResponses;
    private EditText txtGamesWishlist;
    private Switch swtGamesWishlist;

    private List<Game> gamesPlayedResponses;
    private EditText txtGamesPlayed;
    private Switch swtGamesPlayed;

    public NewImportCollectionFragment() {
    }

    public static NewImportCollectionFragment newInstance() {
        NewImportCollectionFragment fragment = new NewImportCollectionFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LogUtils.d(TAG, "onCreateView: Create new NewImportCollectionFragment!");

        final View view = inflater.inflate(R.layout.fragment_new_import_collection, container, false);

        FloatingActionButton fab = view.findViewById(R.id.fab_done_import_collection);
        fab.setOnClickListener(view1 -> fillDataAndSave(view));

        setupDataForm(view);

        return view;
    }

    private void setupDataForm(final View view) {
        LogUtils.i(TAG, "setupDataForm: Load all form fields!");

        txtGamesOwn = view.findViewById(R.id.card_view_new_import_collection_own_value);
        swtGamesOwn = view.findViewById(R.id.card_view_new_import_collection_own);

        txtGamesWishlist = view.findViewById(R.id.card_view_new_import_collection_wishlist_value);
        swtGamesWishlist = view.findViewById(R.id.card_view_new_import_collection_wishlist);

        txtGamesPlayed = view.findViewById(R.id.card_view_new_import_collection_played_value);
        swtGamesPlayed = view.findViewById(R.id.card_view_new_import_collection_played);

        btnSearchUsername = view.findViewById(R.id.card_view_import_collection_button_search);
        btnSearchUsername.setOnClickListener(viewClicked -> processDataSearchCollection(view));

        txtSearchUsername = view.findViewById(R.id.card_view_import_collection_search_name);
        txtSearchUsername.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                processDataSearchCollection(view);
            }
            return false;
        });

    }

    private void processDataSearchCollection(View viewContainer) {
        LogUtils.i(TAG, "processDataSearchCollection: search collection username in server!");

        if (Strings.isNullOrEmpty(txtSearchUsername.getText().toString())) {
            String msgErrorUsername = getContext().getString(R.string.msg_username_bgg_required);
            AndroidUtils.changeErrorEditText(txtSearchUsername, msgErrorUsername, true);
            return;
        }
        AndroidUtils.changeErrorEditText(txtSearchUsername);
        AndroidUtils.hideKeyboard(txtSearchUsername, getActivity());
        final String username = txtSearchUsername.getText().toString().trim();

        AnswersUtils.onActionMetric(CrashlyticsConstant.Actions.ACTION_CLICK_BUTTON,
                CrashlyticsConstant.ValueActions.VALUE_ACTION_CLICK_BUTTON_SEARCH_COLLECTION_BGG);

        new ProgressApp(this.getActivity(), R.string.msg_action_searching, false) {

            @Override
            protected void onPostExecute(Boolean result) {

                fillGamesResponse(txtGamesOwn, gamesOwnResponses, swtGamesOwn);
                fillGamesResponse(txtGamesWishlist, gamesWishlistResponses, swtGamesWishlist);
                fillGamesResponse(txtGamesPlayed, gamesPlayedResponses, swtGamesPlayed);

                if (gamesPlayedResponses.isEmpty() && gamesWishlistResponses.isEmpty() && gamesOwnResponses.isEmpty()) {
                    AndroidUtils.snackbar(viewContainer, R.string.msg_collection_dont_found_search);
                }

                dismissProgress();
            }

            private void fillGamesResponse(EditText txtGames, List<Game> gamesResponses, Switch swtGames) {
                DecimalFormat decimalFormatScore = new DecimalFormat("#00");
                txtGames.setText(decimalFormatScore.format(gamesResponses.size()));
                if (gamesResponses.isEmpty()) {
                    swtGames.setChecked(Boolean.FALSE);
                    swtGames.setEnabled(Boolean.FALSE);
                } else {
                    swtGames.setChecked(Boolean.TRUE);
                    swtGames.setEnabled(Boolean.TRUE);
                }
            }

            @Override
            protected Boolean doInBackground(Void... params) {
                final CollectionService collectionService = Inject.provideCollectionService();
                gamesOwnResponses = collectionService.searchCollectionOwn(username);
                gamesWishlistResponses = collectionService.searchCollectionWishlist(username);
                gamesPlayedResponses = collectionService.searchCollectionPlayed(username);
                return true;
            }
        }.execute();

    }

    private void fillDataAndSave(View viewContainer) {
        LogUtils.i(TAG, "fillDataAndSave: ");
        if (gamesPlayedResponses.isEmpty() && gamesWishlistResponses.isEmpty() && gamesOwnResponses.isEmpty()) {
            AndroidUtils.snackbar(viewContainer, R.string.msg_collection_dont_found_search);
            return;
        }

        if (!swtGamesWishlist.isChecked() && !swtGamesOwn.isChecked() && !swtGamesPlayed.isChecked()) {
            AndroidUtils.snackbar(viewContainer, R.string.msg_collection_dont_found_import);
            return;
        }

        AnswersUtils.onActionMetric(CrashlyticsConstant.Actions.ACTION_CLICK_BUTTON,
                CrashlyticsConstant.ValueActions.VALUE_ACTION_CLICK_BUTTON_IMPORT_COLLECTION_BGG);

        AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                .setTitle(R.string.alert_confirmation_dialog_title_import)
                .setMessage(R.string.alert_confirmation_dialog_text_import)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(R.string.btn_confirmation_dialog_import, (dialog, whichButton) -> {
                    ActionCollection actionCollection = new ActionCollection();
                    actionCollection.action(ActionCollectionEnum.LOAD_BGG_COLLECTION);
                    actionCollection.gamesOwn(gamesOwnResponses);
                    actionCollection.gamesWishlist(gamesWishlistResponses);
                    actionCollection.gamesPlayed(gamesPlayedResponses);
                    AndroidUtils.postActionCollection(actionCollection);

                    AndroidUtils.toast(getContext(), R.string.toast_start_import_collection);
                    dialog.dismiss();
                    finishFormAndProcessData();
                })
                .setNegativeButton(R.string.btn_confirmation_dialog_cancel, null).create();
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.animationAlert;
        alertDialog.show();

    }

    private void finishFormAndProcessData() {
        Bundle args = new Bundle();
        args.putInt(NAVIGATION_TO, GAME_FRAGMENT);
        Intent intent = new Intent(getContext(), HomeActivity.class);
        intent.putExtras(args);

        getContext().startActivity(intent);
    }

}
