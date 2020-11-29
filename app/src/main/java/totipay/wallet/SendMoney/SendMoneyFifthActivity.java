package totipay.wallet.SendMoney;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import totipay.wallet.BaseActivity;
import totipay.wallet.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SendMoneyFifthActivity extends BaseActivity {

    @BindView(R.id.paytm_tv)
    TextView paytmTv;

    @Override
    protected int getContentResId() {
        return R.layout.activity_send_money_fifth;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbarWithBackButton("");
        ButterKnife.bind(this);
    }

    @OnClick(R.id.paytm_tv)
    public void onClick() {
        startActivity(new Intent(getApplicationContext(),SendMoneyViaPaytmActivity.class));
    }
}
