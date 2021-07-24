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
import ritman.wallet.databinding.AlertDialogBankLayoutBinding;
import ritman.wallet.databinding.AlertDialogLayoutBinding;
import ritman.wallet.interfaces.OnDecisionMade;

public class SingleButtonMessageBankDialog extends BaseDialogFragment<AlertDialogBankLayoutBinding> {

    String title;
    String message;
    OnDecisionMade onDecisionMade;
    boolean isCancelable = true;
    boolean isError = false;

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


    public SingleButtonMessageBankDialog(String title, String message, OnDecisionMade onDecisionMade ,
                                         boolean isCancelable) {
        this.title = title;
        this.message = message;
        this.onDecisionMade = onDecisionMade;
        this.isCancelable  = isCancelable;
    }

    public SingleButtonMessageBankDialog(String title, String message, OnDecisionMade onDecisionMade ,
                                         boolean isCancelable , boolean isError) {
        this.title = title;
        this.message = message;
        this.onDecisionMade = onDecisionMade;
        this.isCancelable  = isCancelable;
        this.isError = isError;
    }


    @Override
    public int getLayoutId() {
        return R.layout.alert_dialog_bank_layout;
    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        binding.cancelButton.setVisibility(View.GONE);
        if (title != "") {
            binding.title.setText(title);
            binding.title.setTextColor(getResources().getColor(R.color.colorPrimary));
            binding.message.setText(message);
        }

        binding.yes.setOnClickListener(v -> {
            cancelUpload();
            onDecisionMade.onProceed();
        });


        if(isError) {
            binding.title.setTextColor(getResources().getColor(R.color.Red));
        }



    }


    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        dialog.setCanceledOnTouchOutside(isCancelable);
        if (dialog != null) {
            DisplayMetrics dm = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
            int width = dm.widthPixels;
            int height = dm.heightPixels;
            dialog.getWindow().setLayout((int) (width * .8), WindowManager.LayoutParams.WRAP_CONTENT);
        }
    }
}
