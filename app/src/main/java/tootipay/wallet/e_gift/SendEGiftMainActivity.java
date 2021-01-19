package tootipay.wallet.e_gift;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Bundle;

import tootipay.wallet.R;
import tootipay.wallet.base.TootiBaseActivity;
import tootipay.wallet.databinding.ActivitySendEGiftMainBinding;

public class SendEGiftMainActivity extends TootiBaseActivity<ActivitySendEGiftMainBinding> {

    private NavController navController;

    @Override
    public int getLayoutId() {
        return R.layout.activity_send_e_gift_main;
    }

    @Override
    protected void initUi(Bundle savedInstanceState) {
        navController = Navigation.findNavController(this, R.id.dashboard);
    }


    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp() || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        if(navController.getCurrentDestination().getId() == R.id.giftReceiverFragment) {
            finish();
        }else {
            navController.navigateUp();
        }
    }
}