package angoothape.wallet.SendMoney;

import android.os.Bundle;

import angoothape.wallet.BaseActivity;
import angoothape.wallet.R;

public class DonePaymentActivity extends BaseActivity {

    @Override
    protected int getContentResId() {
        return R.layout.activity_done_payment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_done_payment);
    }
}
