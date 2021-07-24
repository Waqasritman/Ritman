package ritman.wallet.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import ritman.wallet.R;
import ritman.wallet.RegistrationActivity;
import ritman.wallet.base.BaseDialogFragment;
import ritman.wallet.databinding.AlertDialogLayoutBinding;
import ritman.wallet.insurance.InsuranceActivity;
import ritman.wallet.insurance.RegistrationFragment2;
import ritman.wallet.interfaces.OnDecisionMade;

public class SingleButtonMessageDialog extends BaseDialogFragment<AlertDialogLayoutBinding> {

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


    public SingleButtonMessageDialog(String title, String message, OnDecisionMade onDecisionMade ,
                                      boolean isCancelable) {
        this.title = title;
        this.message = message;
        this.onDecisionMade = onDecisionMade;
        this.isCancelable  = isCancelable;
    }

    public SingleButtonMessageDialog(String title, String message, OnDecisionMade onDecisionMade ,
                                     boolean isCancelable , boolean isError) {
        this.title = title;
        this.message = message;
        this.onDecisionMade = onDecisionMade;
        this.isCancelable  = isCancelable;
        this.isError = isError;
    }


    @Override
    public int getLayoutId() {
        return R.layout.alert_dialog_layout;
    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        binding.cancelButton.setVisibility(View.GONE);
        if (title != "") {
            if (getBaseActivity() instanceof RegistrationActivity){
                binding.title.setText(title);
                binding.title.setTextColor(getResources().getColor(R.color.posGreen));
                binding.yes.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.posGreen));
                binding.message.setText(message);
            }
            else  if (getBaseActivity() instanceof InsuranceActivity){
                binding.title.setText(title);
                binding.title.setTextColor(getResources().getColor(R.color.posGreen));
                binding.yes.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.posGreen));

                binding.message.setText(message);
            }
            else {
                binding.title.setText(title);
                binding.title.setTextColor(getResources().getColor(R.color.posBlue));
                binding.message.setText(message);
            }

        }

        binding.yes.setOnClickListener(v -> {
            cancelUpload();
            onDecisionMade.onProceed();
        });


        if(isError) {
            binding.title.setTextColor(getResources().getColor(R.color.Red));
            binding.yes.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.colorPrimary));
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
