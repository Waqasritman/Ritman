package tootipay.wallet.SendMoney;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import tootipay.wallet.BaseActivity;
import tootipay.wallet.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SendMoneyViaPaytmActivity extends BaseActivity {

    @BindView(R.id.send_now_layout)
    LinearLayout sendNowLayout;

    @Override
    protected int getContentResId() {
        return R.layout.activity_send_money_via_paytm;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbarWithBackButton("");
        ButterKnife.bind(this);
    }

    @OnClick(R.id.send_now_layout)
    public void onClick() {
        startActivity(new Intent(getApplicationContext(),TransferMoneyActivity.class));

    }
}
