package ritman.wallet.mywalletmoduleV;

import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Bundle;
import android.util.Log;

import ritman.wallet.R;
import ritman.wallet.base.RitmanBaseActivity;
import ritman.wallet.databinding.ActivityMyWalletMainBinding;

public class MyWalletMainActivity extends RitmanBaseActivity<ActivityMyWalletMainBinding> {

    private NavController navController;

    @Override
    public int getLayoutId() {
        return R.layout.activity_my_wallet_main;
    }


    @Override
    protected void initUi(Bundle savedInstanceState) {
        binding.toolBar.backBtn
                .setColorFilter(ContextCompat.getColor(this,
                        R.color.colorWhite), android.graphics.PorterDuff.Mode.SRC_IN);
        binding.toolBar.toolBarFinal.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        binding.toolBar.titleTxt.setText(getString(R.string.tooti_wallet_txt));

        navController = Navigation.findNavController(this, R.id.dashboard_my_wallet);
        binding.toolBar.backBtn.setOnClickListener(v -> onBackPressed());


        binding.toolBar.crossBtn.setOnClickListener(v->{
            onClose();
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp() || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        Log.e("onBackPressed: ", "");
        if (navController.getCurrentDestination().getId() == R.id.MyWalletActivity
         || navController.getCurrentDestination().getId() == R.id.choosePaymentMethodForWalletLoadFragment) {
            super.onBackPressed();
        } else {
            navController.navigateUp();
        }
    }

}