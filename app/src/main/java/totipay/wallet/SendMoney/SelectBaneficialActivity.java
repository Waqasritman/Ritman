package totipay.wallet.SendMoney;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import totipay.wallet.BaseActivity;
import totipay.wallet.MoneyTransferModuleV.wallettransfer.international.SendMoneyToWalletActivity;
import totipay.wallet.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SelectBaneficialActivity extends BaseActivity {

    @BindView(R.id.send_mony_btn)
    Button sendMonyBtn;

    @Override
    protected int getContentResId() {
        return R.layout.activity_select_baneficial;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbarWithBackButton("");
        ButterKnife.bind(this);
    }

    @OnClick(R.id.send_mony_btn)
    public void onClick() {
        startActivity(new Intent(getApplicationContext(), SendMoneyToWalletActivity.class));

    }
}
