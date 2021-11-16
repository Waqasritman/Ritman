package angoothape.wallet.refund_module;


import android.os.Bundle;
import android.util.Log;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;

import angoothape.wallet.R;
import angoothape.wallet.adapters.ViewStateAdapterRefund;
import angoothape.wallet.base.RitmanBaseActivity;
import angoothape.wallet.databinding.ActivityRefundBinding;

public class RefundActivity extends RitmanBaseActivity<ActivityRefundBinding> {

    // public NavController navController;

    @Override
    public int getLayoutId() {
        return R.layout.activity_refund;
    }


    @Override
    protected void initUi(Bundle savedInstanceState) {
        //  navController = Navigation.findNavController(this, R.id.dashboard_money_tranfser);
        binding.toolBar.titleTxt.setText(getString(R.string.refund_txt));
        binding.toolBar.backBtn.setOnClickListener(v -> onBackPressed());

        binding.toolBar.toolBarFinal
                .setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        binding.toolBar.backBtn
                .setColorFilter(ContextCompat.getColor(this,
                        R.color.colorWhite), android.graphics.PorterDuff.Mode.SRC_IN);

        binding.toolBar.crossBtn.setOnClickListener(v -> {
            onClose();
        });


        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Refund"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("History"));


        FragmentManager fm = getSupportFragmentManager();
        ViewStateAdapterRefund sa = new ViewStateAdapterRefund(fm, getLifecycle());
        binding.viewPager.setAdapter(sa);

        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        binding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                binding.tabLayout.selectTab(binding.tabLayout.getTabAt(position));
            }
        });
    }


//    @Override
//    public boolean onSupportNavigateUp() {
//        return navController.navigateUp() || super.onSupportNavigateUp();
//    }

    @Override
    public void onBackPressed() {
        Log.e("onBackPressed: ", "");
        //    if (navController.getCurrentDestination().getId() == R.id.refundTransactionNumberFragment) {
        finish();
        //   } else {
        //     navController.navigateUp();
        // }
    }
}