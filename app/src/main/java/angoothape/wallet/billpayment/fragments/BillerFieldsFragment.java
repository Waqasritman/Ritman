package angoothape.wallet.billpayment.fragments;

import android.os.Bundle;

import androidx.lifecycle.ViewModelProvider;

import angoothape.wallet.MoneyTransferModuleV.viewmodels.BankTransferViewModel;
import angoothape.wallet.R;
import angoothape.wallet.billpayment.BillPaymentMainActivity;
import angoothape.wallet.databinding.FragmentBillerFieldDetailBinding;
import angoothape.wallet.di.JSONdi.Status;
import angoothape.wallet.di.JSONdi.restRequest.GetWRBillerFieldsRequestN;
import angoothape.wallet.fragments.BaseFragment;

public class BillerFieldsFragment extends BaseFragment<FragmentBillerFieldDetailBinding> {

    BankTransferViewModel viewModel;
    Integer billerId, SkuId;


    @Override
    protected void injectView() {

    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(BankTransferViewModel.class);


        billerId = getArguments().getInt("billerId");
        SkuId = getArguments().getInt("SkuId");

       /*binding.descriptionOne;

        binding.title.setText(getString(R.string.select_recharge_type));*/

        getWRBillerFields();

        //Lable-Lable Name
        //Field- mob no (Edit Text)
        binding.labelNameOne.setText("");
        binding.labelNameTwo.setText("");
        binding.labelNameThree.setText("");
        binding.descriptionOne.setText("");

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_biller_field_detail;
    }


    @Override
    public void onResponseMessage(String message) {
        onMessage(message);
    }


    private void getWRBillerFields() {
        GetWRBillerFieldsRequestN request = ((BillPaymentMainActivity) getBaseActivity()).request;
//        request.countryCode="IN";
//        request.BillerID= 107;//Integer.valueOf(billerId);
//        request.SkuID=94; //Integer.valueOf(SkuId);


//        viewModel.GetWRBillerFields(request ,getSessionManager().getMerchantName()).observe(getViewLifecycleOwner()
//
//                , response -> {
//                    if (response.status == Status.ERROR) {
//                        onError(getString(response.messageResourceId));
//                    } else {
//                        assert response.resource != null;
//                        if (response.resource.responseCode.equals(101)) {
//                           String S= response.resource.labelName;
//
//
//
//                     //       onBillerTypeList(response.resource.data);
//                        } else {
//                            onError(response.resource.description);
//                        }
//                    }
//                });
    }
}