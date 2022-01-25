package angoothape.wallet.bus_booking;

import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import angoothape.wallet.R;
import angoothape.wallet.base.RitmanBaseActivity;
import angoothape.wallet.databinding.ActivityBusBookingMainBinding;
import angoothape.wallet.viewmodels.BusBookingViewModel;

public class BusBookingMainActivity extends RitmanBaseActivity<ActivityBusBookingMainBinding> {

    public BusBookingViewModel viewModel;
    public NavController navController;

    @Override
    public int getLayoutId() {
        return R.layout.activity_bus_booking_main;
    }

    @Override
    protected void initUi(Bundle savedInstanceState) {
        navController = Navigation.findNavController(this, R.id.dashboard_money_tranfser);
        viewModel = new ViewModelProvider(this).get(BusBookingViewModel.class);
        binding.toolBar.titleTxt.setText(getString(R.string.bus_book_txt));
        binding.toolBar.backBtn.setOnClickListener(v -> onBackPressed());
        binding.toolBar.toolBarFinal
                .setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        binding.toolBar.backBtn
                .setColorFilter(ContextCompat.getColor(this,
                        R.color.colorWhite), android.graphics.PorterDuff.Mode.SRC_IN);
        binding.toolBar.crossBtn.setOnClickListener(v -> {
            onClose();
        });

    }


    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp() || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        if (navController.getCurrentDestination().getId() == R.id.busSeatConfirmedFragment
                || navController.getCurrentDestination().getId() == R.id.busStationsFragment) {
            finish();
        } else {
            navController.navigateUp();
        }

    }
}