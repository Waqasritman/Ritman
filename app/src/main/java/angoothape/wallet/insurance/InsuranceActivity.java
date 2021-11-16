package angoothape.wallet.insurance;

import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import angoothape.wallet.R;
import angoothape.wallet.base.RitmanBaseActivity;
import angoothape.wallet.databinding.ActivityInsuranceBinding;

public class InsuranceActivity extends RitmanBaseActivity<ActivityInsuranceBinding> {
    public NavController navController;
    @Override
    public int getLayoutId() {
        return R.layout.activity_insurance;
    }

    @Override
    protected void initUi(Bundle savedInstanceState) {
        binding.toolBar.backBtn
                .setColorFilter(ContextCompat.getColor(this,
                        R.color.colorWhite), android.graphics.PorterDuff.Mode.SRC_IN);
        binding.toolBar.toolBar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        binding.toolBar.titleTxt.setText(getString(R.string.create_customer_tool_title));

        binding.toolBar.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.toolBar.crossBtn.setOnClickListener(v->{
            onClose();
        });
        navController = Navigation.findNavController(this, R.id.dashboard);
        binding.toolBar.backBtn.setOnClickListener(v -> onBackPressed());
    }

    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp() || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        Log.e("onBackPressed: ", "");
        if (navController.getCurrentDestination().getId() == R.id.selectOptionsFragment) {
            finish();
        } else {
            navController.navigateUp();
        }

    }
}