package ritman.wallet.home_module.fragments;

import android.os.Bundle;

import ritman.wallet.R;
import ritman.wallet.databinding.ActivityReferToFriendBinding;
import ritman.wallet.fragments.BaseFragment;
import ritman.wallet.home_module.ClassChangerHelper;
import ritman.wallet.home_module.NewDashboardActivity;

public class ReferToFriendFragment extends BaseFragment<ActivityReferToFriendBinding> {


    @Override
    public void onResume() {
        super.onResume();
        ((NewDashboardActivity)getActivity()).hideHumBurger(ClassChangerHelper.SAVE_BANK);
    }

    @Override
    protected void injectView() {

    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        binding.toolBar.backBtn.setOnClickListener(v -> {
            ((NewDashboardActivity) getBaseActivity())
                    .moveToFragment(new HomeFragment());
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_refer_to_friend;
    }

}