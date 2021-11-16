package angoothape.wallet.SendMoney;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import angoothape.wallet.BaseActivity;
import angoothape.wallet.R;
import angoothape.wallet.dialogs.AddCardDialog;
import angoothape.wallet.mywalletmoduleV.fragments.AddedMoneyWalletActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SelectCardActivity extends BaseActivity {



    @BindView(R.id.add_new_card)
    TextView add_new_card;

    @BindView(R.id.wallet_layout)
    LinearLayout wallet_layout;

    Boolean check;


    @Override
    protected int getContentResId() {
        return R.layout.activity_select_card;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setToolbarWithBackButton("");
        ButterKnife.bind(this);

        TextView getAdd_new_card = findViewById(R.id.add_new_card);
        getAdd_new_card.setOnClickListener(v -> {
            startActivity(new Intent(this, AddCardDialog.class));
        });


        ImageView back_btn = findViewById(R.id.back_btn);
        back_btn.setOnClickListener(v -> {
            finish();
        });
        TextView through_bank = findViewById(R.id.through_bank);
        through_bank.setOnClickListener(v -> {
//            BankDepositDialog depositDialog = new BankDepositDialog();
//            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//            depositDialog.show(transaction, "");
        });

    }

    @OnClick({R.id.add_new_card, R.id.wallet_layout})
    public void onClick(View view) {
        switch (view.getId()) {




            case R.id.wallet_layout:
                Intent intent2 = new Intent(getApplicationContext(), AddedMoneyWalletActivity.class);
                intent2.putExtra("type", "hgfhggg");

                startActivity(intent2);
                // startActivity(new Intent(getApplicationContext(), AddedMoneyWalletActivity.class));

                break;


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
