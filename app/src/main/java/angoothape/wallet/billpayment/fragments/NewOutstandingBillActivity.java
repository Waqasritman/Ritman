package angoothape.wallet.billpayment.fragments;

import android.os.Bundle;
import android.view.View;

import angoothape.wallet.R;
import angoothape.wallet.databinding.ActivityNewOutstandingBillBinding;
import angoothape.wallet.fragments.BaseFragment;

public class NewOutstandingBillActivity extends BaseFragment<ActivityNewOutstandingBillBinding> {


    String payment_type="";

    @Override
    protected void injectView() {

    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        payment_type = getArguments().getString("payment");

        if(payment_type.equals("local")){
            binding.payableTv.setText("Payable: 7000INR");
        }else {
            binding.payableTv.setText("Payable: 100USD");

        }

        binding.proceedPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Navigation.findNavController(view)
//                        .navigate(R.id.action_newOutstandingBillActivity_to_selectBillPaymentModeActivity);
              //  startActivity(new Intent(getApplicationContext(), SelectBillPaymentModeActivity.class));
            }
        });

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_new_outstanding_bill;
    }


}