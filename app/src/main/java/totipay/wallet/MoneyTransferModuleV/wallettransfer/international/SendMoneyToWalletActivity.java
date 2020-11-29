package totipay.wallet.MoneyTransferModuleV.wallettransfer.international;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import totipay.wallet.R;
import totipay.wallet.SendMoney.SendMoneyFifthActivity;
import totipay.wallet.databinding.ActivitySendMoneyToWalletBinding;
import totipay.wallet.fragments.BaseFragment;

public class SendMoneyToWalletActivity extends BaseFragment<ActivitySendMoneyToWalletBinding> {

    @Override
    protected void injectView() {

    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        binding.nextLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), SendMoneyFifthActivity.class));
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_send_money_to_wallet;
    }




//    @OnClick({ R.id.next_layout,R.id.select_wallet})
//    public void onClick(View view) {
//        switch (view.getId()) {
//
//            case R.id.next_layout:
//                startActivity(new Intent(getApplicationContext(), SendMoneyFifthActivity.class));
//                break;
//
//            case R.id.select_wallet:
//                startActivity(new Intent(getApplicationContext(),SendMoneyFifthActivity.class));
//
//                break;
//        }
//    }
}
