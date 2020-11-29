package totipay.wallet.MoneyTransferModuleV.wallettransfer;

import android.os.Bundle;
import android.view.View;

import androidx.navigation.Navigation;

import totipay.wallet.R;
import totipay.wallet.databinding.ActivitySendMoneyTypeBinding;
import totipay.wallet.fragments.BaseFragment;

public class SendMoneyTypeActivity extends BaseFragment<ActivitySendMoneyTypeBinding> {

    @Override
    protected void injectView() {

    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        binding.sendMoneyBtn.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putBoolean("to_own_wallet", false);
            Navigation.findNavController(v)
                    .navigate(R.id.action_sendMoneyTypeActivity_to_walletTransferActivity, bundle);
            //   startActivity(new Intent(getContext(), WalletTransferActivity.class));
        });


        binding.requestMoneyBtn.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_sendMoneyTypeActivity_to_requestMoneyActivity);
            // startActivity(new Intent(getContext(), RequestMoneyActivity.class));
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_send_money_type;
    }

}
