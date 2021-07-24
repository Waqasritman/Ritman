package ritman.wallet.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import ritman.wallet.R;
import ritman.wallet.base.BaseDialogFragment;
import ritman.wallet.databinding.BankTransferAlertDesignBinding;
import ritman.wallet.interfaces.OnDecisionMade;

public class BankDepositDialog extends BaseDialogFragment<BankTransferAlertDesignBinding> {


    String referenceNo;
    OnDecisionMade onDecisionMade;
    boolean isYemenClient = false;

    public BankDepositDialog(String referenceNo, OnDecisionMade onDecisionMade , boolean isYemenClient) {
        this.referenceNo = referenceNo;
        this.onDecisionMade = onDecisionMade;
        this.isYemenClient = isYemenClient;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new Dialog(getActivity(), getTheme()){
            @Override
            public void onBackPressed() {
                //do your stuff
            }
        };
    }

    @Override
    public int getLayoutId() {
        return R.layout.bank_transfer_alert_design;
    }

    @Override
    protected void setUp(Bundle savedInstanceState) {

        binding.referenceNo.setText(referenceNo);

        if (isYemenClient) {
            binding.bankDetailLayout.setVisibility(View.GONE);
            binding.yemenBankLayout.setVisibility(View.VISIBLE);
        } else {
            binding.bankDetailLayout.setVisibility(View.VISIBLE);
            binding.yemenBankLayout.setVisibility(View.GONE);
        }

        binding.yes.setOnClickListener(v -> {
            onDecisionMade.onProceed();
            cancelUpload();
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        dialog.setCanceledOnTouchOutside(false);
        if (dialog != null) {
            DisplayMetrics dm = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
            int width = dm.widthPixels;
            int height = dm.heightPixels;
            dialog.getWindow().setLayout((int) (width * .8), WindowManager.LayoutParams.WRAP_CONTENT);
        }
    }



}
