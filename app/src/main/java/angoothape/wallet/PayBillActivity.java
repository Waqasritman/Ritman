package angoothape.wallet;

import android.os.Bundle;

import androidx.lifecycle.ViewModelProvider;
import angoothape.wallet.MoneyTransferModuleV.viewmodels.BankTransferViewModel;
import angoothape.wallet.base.RitmanBaseActivity;
import angoothape.wallet.databinding.ActivityPayBillBinding;
import angoothape.wallet.di.JSONdi.Status;
import angoothape.wallet.di.JSONdi.restRequest.BillPayRequest;
import angoothape.wallet.utils.IsNetworkConnection;

public class PayBillActivity extends RitmanBaseActivity<ActivityPayBillBinding> {
    BankTransferViewModel viewModel;

String billerId,SkuId,Mob_Acc_No,Mob_Acc_No1,Mob_Acc_No2,PayOutAmount;


    @Override
    public int getLayoutId() {
        return R.layout.activity_pay_bill;
    }

    @Override
    protected void initUi(Bundle savedInstanceState) {


        billerId = getIntent().getStringExtra("billerId");
        SkuId = getIntent().getStringExtra("SkuId");
        Mob_Acc_No = getIntent().getStringExtra("Field1");
        Mob_Acc_No1 = getIntent().getStringExtra("Field2");
        Mob_Acc_No2 = getIntent().getStringExtra("Field3");
        PayOutAmount = getIntent().getStringExtra("PayOutAmount");

        viewModel = new ViewModelProvider(this).get(BankTransferViewModel.class);

        getPrepaidOperator();
    }


    void getPrepaidOperator() {
        if (IsNetworkConnection.checkNetworkConnection(getApplicationContext())) {

            BillPayRequest request = new BillPayRequest();
            request.PayOutCurrency = "INR";
            request.PayinCurrency = "INR";
            request.countryCode = "IN";
            request.BillerID = Integer.valueOf(billerId);
            request.SkuID = Integer.valueOf(SkuId);
            request.PayOutAmount = Double.valueOf(Integer.valueOf(PayOutAmount));
            request.Mob_Acc_No = Mob_Acc_No;
            request.Mob_Acc_No1 =Mob_Acc_No1;
            request.Mob_Acc_No2 = Mob_Acc_No2;

            viewModel.getBillPay(request, sessionManager.getMerchantName()).observe(this
                    , response -> {
                        if (response.status == Status.ERROR) {
                            onMessage(getString(response.messageResourceId));
                        } else {
                            assert response.resource != null;
                            if (response.resource.responseCode.equals(101)) {
                                onMessage(response.resource.description);


                            } else {
                                onMessage(response.resource.description);
                            }
                        }
                    });


        } else {
            onMessage(getString(R.string.no_internet));
        }
    }


}