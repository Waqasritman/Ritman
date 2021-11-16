package angoothape.wallet.dialogs;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import angoothape.wallet.R;
import angoothape.wallet.base.BaseDialogFragment;
import angoothape.wallet.databinding.ShowCardDetailsBinding;
import angoothape.wallet.di.XMLdi.ResponseHelper.GetCardDetailsResponse;
import angoothape.wallet.utils.StringHelper;

public class ShowCardDetailsDialog extends BaseDialogFragment<ShowCardDetailsBinding> {

    GetCardDetailsResponse cardDetails;


    @Override
    public int getTheme() {
        return R.style.AppTheme_NoActionBar_FullScreenDialog;
    }

    @Override
    protected void injectView() {
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
    }

    public ShowCardDetailsDialog(GetCardDetailsResponse cardDetails) {
        this.cardDetails = cardDetails;
    }

    @Override
    public int getLayoutId() {
        return R.layout.show_card_details;
    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        binding.txtCardholdername.setText(cardDetails.cardName);
        binding.txtExpireMonthyear.setText(cardDetails.cardExpireDate);
        binding.txtNumber.setText(StringHelper.addDishes(cardDetails.cardNumber));


        binding.toolBar.backBtn.setOnClickListener(v -> {
            cancelUpload();
        });
    }
}
