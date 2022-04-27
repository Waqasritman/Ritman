package angoothape.wallet.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import angoothape.wallet.R;
import angoothape.wallet.base.BaseDialogFragment;
import angoothape.wallet.databinding.LayoutDialogShareBinding;
import angoothape.wallet.interfaces.OnReceiptGenerator;

public class ShareDialog extends BottomSheetDialogFragment {

    LayoutDialogShareBinding binding;
    OnReceiptGenerator onReceiptGenerator;

    public ShareDialog(OnReceiptGenerator onReceiptGenerator) {
        this.onReceiptGenerator = onReceiptGenerator;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.layout_dialog_share_, container, false);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.saveAsPdf.setOnClickListener(v -> {
            cancelUpload();
            onReceiptGenerator.onSaveAsPdf();
        });

        binding.sendAsOthers.setOnClickListener(v -> {
            cancelUpload();
            onReceiptGenerator.onSentAsOthers();
        });


        binding.sendAsWhatspp.setOnClickListener(v -> {
            cancelUpload();
            onReceiptGenerator.onSentAsWhatsApp();
        });


        binding.sendAsImage.setOnClickListener(v -> {
            cancelUpload();
            onReceiptGenerator.onSaveAsImage();
        });
    }

//
//    @Override
//    public int getTheme() {
//        return R.style.AppTheme_NoActionBar_FullScreenDialog;
//    }

    public void cancelUpload() {
        Dialog dialog = getDialog();
        dialog.dismiss();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
    }
}