package ritman.wallet.pancard;

import android.os.Bundle;

import androidx.core.content.ContextCompat;

import ritman.wallet.R;
import ritman.wallet.base.RitmanBaseActivity;
import ritman.wallet.databinding.PancardActivityLayoutBinding;

public class PanCardActivity extends RitmanBaseActivity<PancardActivityLayoutBinding> {
    @Override
    public int getLayoutId() {
        return R.layout.pancard_activity_layout;
    }

    @Override
    protected void initUi(Bundle savedInstanceState) {
        binding.toolBar.titleTxt.setText("Pancard");

        binding.toolBar.backBtn.setOnClickListener(v -> onBackPressed());

        binding.toolBar.toolBarFinal
                .setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        binding.toolBar.backBtn
                .setColorFilter(ContextCompat.getColor(this,
                        R.color.colorWhite), android.graphics.PorterDuff.Mode.SRC_IN);


        binding.toolBar.crossBtn.setOnClickListener(v -> {
            //   if (SelectBeneficiaryFragment!=null){
            //     Intent intent=new Intent(RegistrationActivity.this,NewDashboardActivity.class);
            //   startActivity(intent);

            //onClose();
            //}
            //else {
            onClose();
            //}

        });
    }
}
