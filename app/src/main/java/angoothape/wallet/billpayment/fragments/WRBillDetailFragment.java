package angoothape.wallet.billpayment.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import angoothape.wallet.MoneyTransferModuleV.viewmodels.BankTransferViewModel;
import angoothape.wallet.R;
import angoothape.wallet.adapters.WRBillerOperatorAdapter;
import angoothape.wallet.billpayment.BillPaymentMainActivity;
import angoothape.wallet.databinding.ActivityBillPaymentCountryBinding;
import angoothape.wallet.di.JSONdi.Status;
import angoothape.wallet.di.JSONdi.restRequest.GetWRBillerNamesRequestC;
import angoothape.wallet.di.JSONdi.restResponse.GetWRBillerNamesResponseC;
import angoothape.wallet.fragments.BaseFragment;
import angoothape.wallet.interfaces.OnWRBillerNames;

public class WRBillDetailFragment extends BaseFragment<ActivityBillPaymentCountryBinding>
        implements OnWRBillerNames {

    List<GetWRBillerNamesResponseC> wrBillerNamesResponseList;
    WRBillerOperatorAdapter adapter;
    BankTransferViewModel viewModel;


    @Override
    protected void injectView() {

    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(BankTransferViewModel.class);
        wrBillerNamesResponseList = new ArrayList<>();
        setSearchView();
        setupRecyclerView();
        binding.title.setText(getString(R.string.plz_select_bill_comppany));

        GetWRBillerNames();

       /* GetWRBillerNamesRequest request = new GetWRBillerNamesRequest();
        request.countryCode = ((BillPaymentMainActivity) getBaseActivity()).plansRequest.countryCode;
        request.billerCategoryID = ((BillPaymentMainActivity) getBaseActivity()).plansRequest.billerCategoryId;
        request.billerTypeID = ((BillPaymentMainActivity) getBaseActivity()).plansRequest.billerTypeID;
        request.languageId = getSessionManager().getlanguageselection();
        if (IsNetworkConnection.checkNetworkConnection(getContext())) {
            GetWRBillerNamesTask task = new GetWRBillerNamesTask(getActivity(), this);
            task.execute(request);
        } else {
            onMessage(getString(R.string.no_internet));
        }*/
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_bill_payment_country;
    }


    /**
     * method will init the search box
     */
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


    /**
     * Method will set the recycler view
     */
    private void setupRecyclerView() {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        adapter = new
                WRBillerOperatorAdapter(getContext() , wrBillerNamesResponseList, this);
        binding.countryRecyclerView.setLayoutManager(mLayoutManager);
        binding.countryRecyclerView.setHasFixedSize(true);
        binding.countryRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onBillerNamesList(List<GetWRBillerNamesResponseC> responseList) {
        wrBillerNamesResponseList.clear();
        wrBillerNamesResponseList.addAll(responseList);
        adapter.notifyDataSetChanged();
        binding.searchView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSelectBillerName(GetWRBillerNamesResponseC billerName) {
        ((BillPaymentMainActivity) getBaseActivity()).plansRequest.billerID = billerName.Biller_ID;

    }

    @Override
    public void onResponseMessage(String message) {
        onMessage(message);
    }


    private void GetWRBillerNames() {


        GetWRBillerNamesRequestC request = new GetWRBillerNamesRequestC();
        request.CountryCode="IN";
      //  request.CountryCode="";
        /*request.BillerTypeId= Integer.valueOf("");
        request.BillerCategoryId= Integer.valueOf("");*/

        request.BillerTypeId= 1;
        request.BillerCategoryId= 9;


//        viewModel.GetWRBillerNames(request ,getSessionManager().getMerchantName()).observe(getViewLifecycleOwner()
//
//                , response -> {
//                    if (response.status == Status.ERROR) {
//                        onError(getString(response.messageResourceId));
//                    } else {
//                        assert response.resource != null;
//                        if (response.resource.responseCode.equals(101)) {
//                            onBillerNamesList(response.resource.data);
//                        } else {
//                            onError(response.resource.description);
//                        }
//                    }
//                });
    }
}
