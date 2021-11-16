package angoothape.wallet.billpayment.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import angoothape.wallet.R;
import angoothape.wallet.adapters.WRBillerCategoryListAdapter;
import angoothape.wallet.billpayment.viewmodel.BillPaymentViewModel;
import angoothape.wallet.databinding.TransferDialogPurposeBinding;
import angoothape.wallet.di.JSONdi.Status;
import angoothape.wallet.di.JSONdi.restRequest.GetWRBillerCategoryRequestC;
import angoothape.wallet.di.JSONdi.restRequest.GetWRBillerNamesRequestC;
import angoothape.wallet.di.JSONdi.restResponse.GetWRBillerCategoryResponseC;
import angoothape.wallet.fragments.BaseFragment;
import angoothape.wallet.interfaces.OnWRBillerCategoryResponse;
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

        viewModel = new ViewModelProvider(this).get(BillPaymentViewModel.class);
        GetWRBillerCategoryRequestC request = new GetWRBillerCategoryRequestC();
        binding.toolBarLayout.setVisibility(View.GONE);
        close_dialog = getView().findViewById(R.id.close_dialog);

        request.CountryCode = "IN";
        request.BillerTypeId = 1;

        //   binding.closeDialog.setOnClickListener(v -> onBackPressed());
        binding.toolBarLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        binding.titleOfPage.setText("Bill Payment");

        viewModel.GetWRBillerCategory(request, getSessionManager().getMerchantName()).observe(this

                , response -> {
                    if (response.status == Status.ERROR) {
                        onMessage(getString(response.messageResourceId));
                    } else {
                        assert response.resource != null;
                        if (response.resource.responseCode.equals(101)) {

                            String s = response.resource.getName();

                            //  onWRCategory(response.resource.data);
                            wrBillerCategoryList = new ArrayList<>();
                            wrBillerCategoryList.addAll(response.resource.data);
                            setupRecyclerView();
                        } else {
                            onMessage(response.resource.description);
                        }
                    }
                });
        close_dialog.setOnClickListener(v -> getBaseActivity().finish());


    }

    private void setSearchView() {
        binding.searchView.setMaxWidth(Integer.MAX_VALUE);
        binding.searchView.requestFocus();
        binding.searchView.setFocusable(true);
        binding.searchView.setHint(getString(R.string.search));


        binding.searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (adapter != null) {
                    adapter.getFilter().filter(s);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.transfer_dialog_purpose;
    }

    private void setupRecyclerView() {

        Collections.sort(wrBillerCategoryList, (o1, o2) ->
                o1.getName().compareToIgnoreCase(o2.getName()));
        /* RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());*/
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 3);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getContext(), R.dimen.item_offset);
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
        bundle.putString("billerCategoryId", billerCategoryId);
        Navigation.findNavController(binding.getRoot()).navigate(R.id
                .action_utilityCategoryBFragment_to_utilityBillPaymentPlanFragment, bundle);

    }

    public void getOperator() {
//        binding.selectOperator.setOnClickListener(v -> {
//
//            if (billerCategoryId.isEmpty()) {
//                onMessage(getString(R.string.select_category));
//            } else if (countryCode.isEmpty()) {
//                onMessage(getString(R.string.select_country));
//            } else {


        GetWRBillerNamesRequestC request = new GetWRBillerNamesRequestC();
        request.CountryCode = "IN";
        request.BillerTypeId = 1;//Integer.valueOf(billerTypeId);
        request.BillerCategoryId = Integer.valueOf(billerCategoryId);


        viewModel.GetWRBillerNames(request, getSessionManager().getMerchantName()).observe(getViewLifecycleOwner()

                , response -> {
                    if (response.status == Status.ERROR) {
                        onMessage(getString(response.messageResourceId));
                    } else {
                        assert response.resource != null;
                        if (response.resource.responseCode.equals(101)) {

                            String s = response.resource.Biller_Name;

                            //onBillerNamesList(response.resource.data);

                        } else {
                            onMessage(response.resource.description);
                        }
                    }
                });


               /* GetWRBillerNamesRequest request = new GetWRBillerNamesRequest();
                request.countryCode = countryCode;
                request.billerCategoryID = billerCategoryId;
                request.billerTypeID = billerTypeId;
                request.languageId = getSessionManager().getlanguageselection();
                if (IsNetworkConnection.checkNetworkConnection(getContext())) {
                    GetWRBillerNamesTask task = new GetWRBillerNamesTask(getActivity(), this);
                    task.execute(request);
                } else {
                    onMessage(getString(R.string.no_internet));
                }*/
    }


}

