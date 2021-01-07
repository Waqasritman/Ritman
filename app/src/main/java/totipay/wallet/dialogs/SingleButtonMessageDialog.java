package totipay.wallet.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import totipay.wallet.R;
import totipay.wallet.base.BaseDialogFragment;
import totipay.wallet.databinding.AlertDialogLayoutBinding;
import totipay.wallet.interfaces.OnDecisionMade;

public class SingleButtonMessageDialog extends BaseDialogFragment<AlertDialogLayoutBinding> {

    String title;
    String message;
    OnDecisionMade onDecisionMade;
    boolean isCancelable = true;

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


    public SingleButtonMessageDialog(String title, String message, OnDecisionMade onDecisionMade ,
                                      boolean isCancelable) {
        this.title = title;
        this.message = message;
        this.onDecisionMade = onDecisionMade;
        this.isCancelable  = isCancelable;
    }

    @Override
    public int getLayoutId() {
        return R.layout.alert_dialog_layout;
    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        binding.cancelButton.setVisibility(View.GONE);
        if (title != "") {
            binding.title.setText(title);
            binding.message.setText(message);
        }

        binding.yes.setOnClickListener(v -> {
            cancelUpload();
            onDecisionMade.onProceed();
        });



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
