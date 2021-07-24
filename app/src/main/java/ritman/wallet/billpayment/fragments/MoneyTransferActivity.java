package ritman.wallet.billpayment.fragments;

import android.os.Bundle;

import android.view.View;

import ritman.wallet.R;
import ritman.wallet.databinding.ActivityMoneyTransferBinding;
import ritman.wallet.fragments.BaseFragment;

public class MoneyTransferActivity extends BaseFragment<ActivityMoneyTransferBinding> {


    @Override
    protected void injectView() {

    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        binding.localTransafer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("payment","local");


            }
        });


    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_money_transfer;
    }

}