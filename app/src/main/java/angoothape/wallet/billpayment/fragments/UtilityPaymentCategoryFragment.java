package angoothape.wallet.billpayment.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import angoothape.wallet.R;
import angoothape.wallet.adapters.WRBillerCategoryListAdapter;
import angoothape.wallet.billpayment.BillPaymentMainActivity;
import angoothape.wallet.billpayment.viewmodel.BillPaymentViewModel;
import angoothape.wallet.databinding.ActivityBillPaymentCountryBinding;
import angoothape.wallet.di.JSONdi.Status;
import angoothape.wallet.di.JSONdi.restRequest.GetWRBillerCategoryRequestC;
import angoothape.wallet.di.JSONdi.restResponse.GetWRBillerCategoryResponseC;
import angoothape.wallet.fragments.BaseFragment;
import angoothape.wallet.interfaces.OnWRBillerCategoryResponse;
import angoothape.wallet.utils.IsNetworkConnection;

public class UtilityPaymentCategoryFragment extends BaseFragment<ActivityBillPaymentCountryBinding>
        implements OnWRBillerCategoryResponse {

    WRBillerCategoryListAdapter adapter;
    List<GetWRBillerCategoryResponseC> wrBillerCategoryResponseList;

   // BankTransferViewModel viewModel;
    BillPaymentViewModel viewModel;

    @Override
    protected void injectView() {

    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(BillPaymentViewModel.class);
        wrBillerCategoryResponseList = new ArrayList<>();
        setSearchView();
        setupRecyclerView();

        binding.title.setText(getString(R.string.select_utility_bill));




        if (IsNetworkConnection.checkNetworkConnection(getContext())) {

            utilityPaymentCategory();

           /* GetWRBillerCategoryRequestC request = new GetWRBillerCategoryRequestC();
            request.BillerTypeId = ((BillPaymentMainActivity) getBaseActivity())
                    .plansRequest.billerID;
            request.Credentials.LanguageID = Integer.valueOf(getSessionManager().getlanguageselection());*/

          /*  GetWRBillerCategoryTask task = new GetWRBillerCategoryTask(getContext(), this);
            task.execute(request);*/
        } else {
            onMessage(getString(R.string.no_internet));
        }
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
                WRBillerCategoryListAdapter(getContext() , wrBillerCategoryResponseList, this);
        binding.countryRecyclerView.setLayoutManager(mLayoutManager);
        binding.countryRecyclerView.setHasFixedSize(true);
        binding.countryRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onWRCategory(List<GetWRBillerCategoryResponseC> responseList) {
        wrBillerCategoryResponseList.clear();
        wrBillerCategoryResponseList.addAll(responseList);
        adapter.notifyDataSetChanged();
        binding.searchView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSelectCategory(GetWRBillerCategoryResponseC category) {
        ((BillPaymentMainActivity)getBaseActivity())
                .plansRequest.billerCategoryId = String.valueOf(category.getID());

    }

    @Override
    public void onResponseMessage(String message) {
        onMessage(message);
    }


    private void utilityPaymentCategory() {


        GetWRBillerCategoryRequestC request = new GetWRBillerCategoryRequestC();
        request.CountryCode="IN";

//
//        viewModel.GetWRBillerCategory(request ,getSessionManager().getMerchantName()).observe(getViewLifecycleOwner()
//
//                , response -> {
//                    if (response.status == Status.ERROR) {
//                        onError(getString(response.messageResourceId));
//                    } else {
//                        assert response.resource != null;
//                        if (response.resource.responseCode.equals(101)) {
//                            onWRCategory(response.resource.data);
//                        } else {
//                            onError(response.resource.description);
//                        }
//                    }
//                });
    }
}
