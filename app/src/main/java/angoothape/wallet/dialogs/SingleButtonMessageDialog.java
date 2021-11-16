package angoothape.wallet.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import angoothape.wallet.R;
import angoothape.wallet.base.BaseDialogFragment;
import angoothape.wallet.databinding.AlertDialogLayoutBinding;
import angoothape.wallet.insurance.InsuranceActivity;
import angoothape.wallet.interfaces.OnDecisionMade;

public class SingleButtonMessageDialog extends BaseDialogFragment<AlertDialogLayoutBinding>
        implements OnDecisionMade {

    String title;
    String message;
    OnDecisionMade onDecisionMade;
    boolean isCancelable = true;
    boolean isError = false;


    String okaytxt = "";
    String cancelTxt = "";

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new Dialog(getActivity(), getTheme()) {
            @Override
            public void onBackPressed() {
                //do your stuff
            }
        };
    }


    public SingleButtonMessageDialog(String title, String message, String okaytxt, String cancelTxt, OnDecisionMade onDecisionMade,
                                     boolean isCancelable) {
        this.title = title;
        this.message = message;
        this.okaytxt = okaytxt;
        this.cancelTxt = cancelTxt;
        this.onDecisionMade = onDecisionMade;
        this.isCancelable = isCancelable;
    }

    public SingleButtonMessageDialog(String title, String message, OnDecisionMade onDecisionMade,
                                     boolean isCancelable) {
        this.title = title;
        this.message = message;
        this.onDecisionMade = onDecisionMade;
        this.isCancelable = isCancelable;
    }

    public SingleButtonMessageDialog(String title, String message, OnDecisionMade onDecisionMade,
                                     boolean isCancelable, boolean isError) {
        this.title = title;
        this.message = message;
        this.onDecisionMade = onDecisionMade;
        this.isCancelable = isCancelable;
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
            if (getBaseActivity() instanceof InsuranceActivity) {
                binding.title.setText(title);
                binding.title.setTextColor(getResources().getColor(R.color.posGreen));
                binding.yes.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.posGreen));

                binding.message.setText(message);
            } else {
                binding.title.setText(title);
                binding.title.setTextColor(getResources().getColor(R.color.posBlue));
                binding.message.setText(message);
            }

        }

        if (!okaytxt.isEmpty()) {
            binding.yes.setText(okaytxt);
            binding.cancelButton.setText(cancelTxt);
            binding.cancelButton.setVisibility(View.VISIBLE);
        }

        binding.yes.setOnClickListener(v -> {
            cancelUpload();
            if (isError) {
                onDecisionMade.onCancel(true);
            } else {
                onDecisionMade.onProceed();
            }
        });


        binding.cancelButton.setOnClickListener(v -> {
            cancelUpload();
            if (isError) {
                onCancel(true);
            } else {
                onCancel(false);
            }

        });


        if (isError) {
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

    @Override
    public void onProceed() {
        getActivity().finish();
    }

    @Override
    public void onCancel(boolean goBack) {
        getActivity().finish();
    }

}
