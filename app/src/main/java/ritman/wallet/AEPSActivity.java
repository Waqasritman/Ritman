package ritman.wallet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;

import ritman.wallet.base.RitmanBaseActivity;
import ritman.wallet.databinding.ActivityAEPSBinding;

public class AEPSActivity extends RitmanBaseActivity<ActivityAEPSBinding> {

    @Override
    public int getLayoutId() {
        return R.layout.activity_a_e_p_s;
    }

    @Override
    protected void initUi(Bundle savedInstanceState) {

        binding.toolBar.titleTxt.setText("AEPS");

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