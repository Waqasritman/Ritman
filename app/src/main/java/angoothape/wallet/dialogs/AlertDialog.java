package angoothape.wallet.dialogs;

import android.os.Bundle;

import angoothape.wallet.R;
import angoothape.wallet.base.BaseDialogFragment;
import angoothape.wallet.databinding.AlertDialogLayoutBinding;
import angoothape.wallet.home_module.NewDashboardActivity;
import angoothape.wallet.interfaces.OnDecisionMade;

public class AlertDialog extends BaseDialogFragment<AlertDialogLayoutBinding> {

    String title;
    String message;
    OnDecisionMade onDecisionMade;

    public AlertDialog(String title, String message, OnDecisionMade onDecisionMade) {
        this.title = title;
        this.message = message;
        this.onDecisionMade = onDecisionMade;
    }

    @Override
    public int getLayoutId() {
        return R.layout.alert_dialog_layout;
    }

    @Override
    protected void setUp(Bundle savedInstanceState) {

        if (title != "") {
            binding.title.setText(title);
            binding.message.setText(message);

            if (getActivity() instanceof NewDashboardActivity) {
                binding.yes.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.colorPrimary));
            }
        }

        binding.cancelButton.setOnClickListener(v -> {
            cancelUpload();
        });

        binding.yes.setOnClickListener(v -> {
            cancelUpload();
            onDecisionMade.onProceed();
        });
    }
}
