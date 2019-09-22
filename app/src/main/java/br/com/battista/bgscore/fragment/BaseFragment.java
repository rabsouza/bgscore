package br.com.battista.bgscore.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageButton;

import br.com.battista.bgscore.R;
import br.com.battista.bgscore.util.AnswersUtils;
import br.com.battista.bgscore.util.LogUtils;

public class BaseFragment extends Fragment {

    private static final String TAG = BaseFragment.class.getSimpleName();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String nameView = this.getClass().getSimpleName();
        AnswersUtils.onOpenFragment(nameView);

        ImageButton btnSortList = getActivity().findViewById(R.id.btn_sort_list);
        if (btnSortList != null) {
            btnSortList.setVisibility(View.GONE);
        }

        ImageButton btnBrokenImg = getActivity().findViewById(R.id.btn_broken_img);
        if (btnBrokenImg != null) {
            btnBrokenImg.setVisibility(View.GONE);
        }

    }

    protected void replaceDetailFragment(Fragment fragment, int containerResID) {
        if (fragment != null) {
            LogUtils.d(TAG, "replaceFragment: Change to detail fragment!");
            final FragmentTransaction transaction = getActivity().
                    getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(
                    R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
            transaction.replace(containerResID, fragment);
            transaction.commit();
        }
    }
}
