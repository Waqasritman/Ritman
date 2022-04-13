package angoothape.wallet.billpayment.updated_fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import angoothape.wallet.R;
import angoothape.wallet.adapters.WRBillerCategoryListAdapter;
import angoothape.wallet.billpayment.BillPaymentMainActivity;
import angoothape.wallet.billpayment.viewmodel.BillPaymentViewModel;
import angoothape.wallet.databinding.TransferDialogPurposeBinding;
import angoothape.wallet.di.AESHelper;
import angoothape.wallet.di.JSONdi.Status;
import angoothape.wallet.di.JSONdi.restRequest.AERequest;
import angoothape.wallet.di.JSONdi.restRequest.GetWRBillerCategoryRequestC;
import angoothape.wallet.di.JSONdi.restRequest.GetWRBillerNamesRequestC;
import angoothape.wallet.di.JSONdi.restResponse.GetWRBillerCategoryResponseC;
import angoothape.wallet.di.JSONdi.restResponse.VerifyBeneficiary;
import angoothape.wallet.di.JSONdi.retrofit.KeyHelper;
import angoothape.wallet.di.JSONdi.retrofit.RestClient;
import angoothape.wallet.fragments.BaseFragment;
import angoothape.wallet.interfaces.OnWRBillerCategoryResponse;
import angoothape.wallet.utils.Utils;
import angoothape.wallet.wallet.ItemOffsetDecoration;


public class UtilityCategoryBFragment extends BaseFragment<TransferDialogPurposeBinding>
        implements OnWRBillerCategoryResponse {

    // BankTransferViewModel viewModel;
    BillPaymentViewModel viewModel;

    WRBillerCategoryListAdapter adapter;
    ImageView close_dialog;
    List<GetWRBillerCategoryResponseC> wrBillerCategoryList;
    public String billerCategoryId = "";

    @Override
    protected void injectView() {

    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        viewModel = ((BillPaymentMainActivity) getBaseActivity()).viewModel;
        String gKey = KeyHelper.getKey(getSessionManager().getMerchantName()).trim() + KeyHelper.getSKey(KeyHelper
                .getKey(getSessionManager().getMerchantName())).trim();
        GetWRBillerCategoryRequestC requestc = new GetWRBillerCategoryRequestC();
        binding.toolBarLayout.setVisibility(View.GONE);
        ((BillPaymentMainActivity) getBaseActivity()).binding.toolBar.titleTxt.setText(
                "Bill Payments"
        );
        close_dialog = getView().findViewById(R.id.close_dialog);

        requestc.CountryCode = "IN";
        requestc.BillerTypeId = 1;

        String body = RestClient.makeGSONString(requestc);

        AERequest request = new AERequest();
        request.body = AESHelper.encrypt(body.trim(), gKey.trim());
        Utils.showCustomProgressDialog(getContext(), false);
        viewModel.GetWRBillerCategory(request, KeyHelper.getKey(getSessionManager().getMerchantName()).trim(),
                KeyHelper.getSKey(KeyHelper
                        .getKey(getSessionManager().getMerchantName())))
                .observe(getViewLifecycleOwner(), response -> {
                    Utils.hideCustomProgressDialog();
                    if (response.status == Status.ERROR) {
                        onError(getString(response.messageResourceId));
                    } else {
                        assert response.resource != null;
                        if (response.resource.responseCode.equals(101)) {
                            String bodyy = AESHelper.decrypt(response.resource.data.body
                                    , gKey);
                            Log.e("setUp: ", bodyy);
                            try {
                                Gson gson = new Gson();
                                Type type = new TypeToken<List<GetWRBillerCategoryResponseC>>() {
                                }.getType();
                                List<GetWRBillerCategoryResponseC> data = gson.fromJson(bodyy, type);

                                wrBillerCategoryList = new ArrayList<>();
                                wrBillerCategoryList.addAll(data);
                                setupRecyclerView();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {
                            Utils.hideCustomProgressDialog();
                            if (response.resource.data != null) {
                                String bodyy = AESHelper.decrypt(response.resource.data.body
                                        , gKey);
                                if (!body.isEmpty()) {
                                    onError(bodyy);
                                } else {
                                    onError(response.resource.description);
                                }
                            } else {
                                onError(response.resource.description);
                            }
                        }
                    }
                });
        close_dialog.setOnClickListener(v -> getBaseActivity().finish());


    }


    //Venkat
    //split
    @Override
    public int getLayoutId() {
        return R.layout.transfer_dialog_purpose;
    }

    private void setupRecyclerView() {

        Collections.sort(wrBillerCategoryList, (o1, o2) ->
                o1.getName().compareToIgnoreCase(o2.getName()));
        /* RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());*/
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 3);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getBaseActivity(), R.dimen.margin_5dp);
        binding.transferPurposeList.addItemDecoration(itemDecoration);
        adapter = new
                WRBillerCategoryListAdapter(getContext(), wrBillerCategoryList, this);
        binding.transferPurposeList.setLayoutManager(mLayoutManager);
        binding.transferPurposeList.setHasFixedSize(true);
        binding.transferPurposeList.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }


    @Override
    public void onWRCategory(List<GetWRBillerCategoryResponseC> responseList) {

        wrBillerCategoryList.clear();
        wrBillerCategoryList.addAll(responseList);
    }

    @Override
    public void onSelectCategory(GetWRBillerCategoryResponseC category) {
        billerCategoryId = String.valueOf(category.getID());
        Bundle bundle = new Bundle();
        bundle.putString("billerName", category.Name);
        bundle.putString("billerCategoryId", billerCategoryId);
        Navigation.findNavController(binding.getRoot()).navigate(R.id
                .action_utilityCategoryBFragment_to_utilityBillPaymentPlanFragment, bundle);

    }
}

