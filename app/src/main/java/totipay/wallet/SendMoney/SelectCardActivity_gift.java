package totipay.wallet.SendMoney;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import totipay.wallet.BaseActivity;
import totipay.wallet.R;
import totipay.wallet.dialogs.AddCardDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SelectCardActivity_gift extends BaseActivity {


    Boolean check;
    @BindView(R.id.add_new_card)
    TextView add_new_card;

    @BindView(R.id.wallet_layout)
    LinearLayout wallet_layout;

    @Override
    protected int getContentResId() {
        return R.layout.activity_select_card;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    //    setToolbarWithBackButton("");
        ButterKnife.bind(this);
    }

    @OnClick(R.id.add_new_card)
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.add_new_card:
                startActivity(new Intent(getApplicationContext(), AddCardDialog.class));

                break;
        }
    }

  /*  @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 01){
            check = Boolean.TRUE;

        }
    }*/
}
