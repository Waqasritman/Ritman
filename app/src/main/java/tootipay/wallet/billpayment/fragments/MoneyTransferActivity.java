package tootipay.wallet.billpayment.fragments;

import android.os.Bundle;

import android.view.View;

import tootipay.wallet.R;
import tootipay.wallet.databinding.ActivityMoneyTransferBinding;
import tootipay.wallet.fragments.BaseFragment;

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