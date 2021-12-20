package angoothape.wallet.billpayment.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import angoothape.wallet.MoneyTransferModuleV.viewmodels.BankTransferViewModel;
import angoothape.wallet.R;

import angoothape.wallet.databinding.ActivityBillPaymentCountryBinding;
import angoothape.wallet.di.JSONdi.Status;
import angoothape.wallet.di.JSONdi.restRequest.BillPayRequest;
import angoothape.wallet.di.JSONdi.restResponse.GetWRBillDetailResponseN;
import angoothape.wallet.dialogs.SingleButtonMessageDialog;
import angoothape.wallet.fragments.BaseFragment;
import angoothape.wallet.home_module.NewDashboardActivity;
import angoothape.wallet.interfaces.OnDecisionMade;

public class PayBillFragment extends BaseFragment<ActivityBillPaymentCountryBinding>  implements OnDecisionMade {

    GetWRBillDetailResponseN response;
    BankTransferViewModel viewModel;
    String billerId,SkuId,Mob_Acc_No,Mob_Acc_No1,Mob_Acc_No2,PayOutAmount;

    @Override
    protected void injectView() {

    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(BankTransferViewModel.class);

        Mob_Acc_No = getArguments().getString("Field1");
        Mob_Acc_No1 = getArguments().getString("Field2");
        Mob_Acc_No2 = getArguments().getString("Field3");
        billerId = getArguments().getString("BillerID");
        SkuId = getArguments().getString("SkuID");
        PayOutAmount = getArguments().getString("PayOutAmount");
        getPrepaidOperator();

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_pay_bill;
    }


    void getPrepaidOperator() {
        BillPayRequest request = new BillPayRequest();
        request.PayOutCurrency = "INR";
        request.PayinCurrency = "INR";
        request.countryCode = "IN";
        request.BillerID = Integer.valueOf(billerId);
        request.SkuID = Integer.valueOf(SkuId);
        request.PayOutAmount = Double.valueOf((PayOutAmount));
        request.Mob_Acc_No = Mob_Acc_No;
        request.Mob_Acc_No1 =Mob_Acc_No1;
        request.Mob_Acc_No2 = Mob_Acc_No2;

        viewModel.getBillPay(request ,getSessionManager().getMerchantName()).observe(getViewLifecycleOwner()
                , response -> {
                    if (response.status == Status.ERROR) {
                        onMessage(getString(response.messageResourceId));
                    } else {
                        assert response.resource != null;
                        if (response.resource.responseCode.equals(101)) {

                            showSucces(response.resource.description, getString(R.string.your_transaction_was_successful)
                                    ,false);
                            //payCustomerDialog();
                        } else {

                            showSucces(response.resource.description, getString(R.string.transaction_failed) , true);

                        }
                    }
                });
    }


    @Override
    public void showProgress() {
        super.showProgress();
        binding.progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        super.hideProgress();
        binding.progressBar.setVisibility(View.GONE);
    }


    private void payCustomerDialog(){
        LayoutInflater factory = LayoutInflater.from(getActivity());
        final View deleteDialogView = factory.inflate(R.layout.dialog_payment_success, null);

        final EditText descripTxt=deleteDialogView.findViewById(R.id.transfer_now_finish);


        final AlertDialog deleteDialog = new AlertDialog.Builder(getActivity()).create();
        deleteDialog.setView(deleteDialogView);



        deleteDialogView.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), NewDashboardActivity.class);
                startActivity(intent);
                getBaseActivity().finish();
            }
        });

        deleteDialog.show();
    }


    private void showSucces(String message, String title , boolean isError) {
        SingleButtonMessageDialog dialog = new
                SingleButtonMessageDialog(title
                , message, this,
                false , isError);
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        dialog.show(transaction, "");
    }

    @Override
    public void onProceed() {
        Intent intent = new Intent(getActivity(), NewDashboardActivity.class);
        startActivity(intent);
        getBaseActivity().finish();
    }

    @Override
    public void onCancel(boolean goBack)  {

    }
}
