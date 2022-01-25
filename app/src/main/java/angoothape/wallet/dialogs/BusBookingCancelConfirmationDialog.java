package angoothape.wallet.dialogs;

import android.os.Bundle;

import angoothape.wallet.R;
import angoothape.wallet.base.BaseDialogFragment;
import angoothape.wallet.databinding.AlertForCancelBookingBusBinding;
import angoothape.wallet.di.JSONdi.restResponse.BusBookingPreCancelResponse;
import angoothape.wallet.interfaces.OnDecisionMade;

public class BusBookingCancelConfirmationDialog extends BaseDialogFragment<AlertForCancelBookingBusBinding> {


    public BusBookingPreCancelResponse response;
    public OnDecisionMade onDecisionMade;

    public BusBookingCancelConfirmationDialog(BusBookingPreCancelResponse response,
                                              OnDecisionMade onDecisionMade) {
        this.response = response;
        this.onDecisionMade = onDecisionMade;
    }

    @Override
    public int getLayoutId() {
        return R.layout.alert_for_cancel_booking_bus;
    }

    @Override
    protected void setUp(Bundle savedInstanceState) {

        binding.returnAmount.setText("INR ".concat(response.returnAmount));

        binding.cancelAmountl.setText("INR ".concat(response.cancellationCharges));

        binding.cancelButton.setOnClickListener(v -> {
            cancelUpload();
        });

        binding.yes.setOnClickListener(v -> {
            cancelUpload();
            onDecisionMade.onProceed();
        });
    }
}
