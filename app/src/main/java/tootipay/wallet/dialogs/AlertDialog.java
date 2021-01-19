package tootipay.wallet.dialogs;

import android.os.Bundle;

import tootipay.wallet.R;
import tootipay.wallet.base.BaseDialogFragment;
import tootipay.wallet.databinding.AlertDialogLayoutBinding;
import tootipay.wallet.interfaces.OnDecisionMade;

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

        if(title != "") {
            binding.title.setText(title);
            binding.message.setText(message);
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
