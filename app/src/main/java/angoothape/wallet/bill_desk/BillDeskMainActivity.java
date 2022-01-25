package angoothape.wallet.bill_desk;

import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Bundle;

import angoothape.wallet.R;
import angoothape.wallet.base.RitmanBaseActivity;
import angoothape.wallet.databinding.ActivityBillDeskMainBinding;

public class BillDeskMainActivity extends RitmanBaseActivity<ActivityBillDeskMainBinding> {

    NavController navController;

    @Override
    public int getLayoutId() {
        return R.layout.activity_bill_desk_main;
    }

    @Override
    protected void initUi(Bundle savedInstanceState) {
        navController = Navigation.findNavController(this, R.id.dashboard);

        binding.titleTxt.setText(getString(R.string.bill_desk));
        binding.backBtn.setOnClickListener(v -> onBackPressed());
        binding.toolBarFinal
                .setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        binding.backBtn
                .setColorFilter(ContextCompat.getColor(this,
                        R.color.colorWhite), android.graphics.PorterDuff.Mode.SRC_IN);

    }


    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp() || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        if (navController.getCurrentDestination().getId() == R.id.billDeskOptionsFragment) {
            finish();
        } else {
            navController.navigateUp();
        }
    }
}