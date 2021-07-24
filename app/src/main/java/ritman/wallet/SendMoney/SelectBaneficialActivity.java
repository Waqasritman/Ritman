package ritman.wallet.SendMoney;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import ritman.wallet.BaseActivity;
import ritman.wallet.MoneyTransferModuleV.wallettransfer.international.SendMoneyToWalletActivity;
import ritman.wallet.R;

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
