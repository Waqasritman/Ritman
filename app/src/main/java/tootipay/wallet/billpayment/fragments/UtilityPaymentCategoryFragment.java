package tootipay.wallet.billpayment.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import tootipay.wallet.R;
import tootipay.wallet.adapters.WRBillerCategoryListAdapter;
import tootipay.wallet.billpayment.BillPaymentMainActivity;
import tootipay.wallet.databinding.ActivityBillPaymentCountryBinding;
import tootipay.wallet.di.RequestHelper.GetWRBillerCategoryRequest;
import tootipay.wallet.di.ResponseHelper.WRBillerCategoryResponse;
import tootipay.wallet.di.apicaller.GetWRBillerCategoryTask;
import tootipay.wallet.fragments.BaseFragment;
import tootipay.wallet.interfaces.OnWRBillerCategoryResponse;
import tootipay.wallet.utils.IsNetworkConnection;

public class UtilityPaymentCategoryFragment extends BaseFragment<ActivityBillPaymentCountryBinding>
        implements OnWRBillerCategoryResponse {

    WRBillerCategoryListAdapter adapter;
    List<WRBillerCategoryResponse> wrBillerCategoryResponseList;

    @Override
    protected void injectView() {

    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        wrBillerCategoryResponseList = new ArrayList<>();
        setSearchView();
        setupRecyclerView();

        binding.title.setText(getString(R.string.select_utility_bill));
        if (IsNetworkConnection.checkNetworkConnection(getContext())) {
            GetWRBillerCategoryRequest request = new GetWRBillerCategoryRequest();
            request.billerTypeId = ((BillPaymentMainActivity) getBaseActivity())
                    .plansRequest.billerTypeID;
            request.languageId = getSessionManager().getlanguageselection();

            GetWRBillerCategoryTask task = new GetWRBillerCategoryTask(getContext(), this);
            task.execute(request);
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
    public void onWRCategory(List<WRBillerCategoryResponse> responseList) {
        wrBillerCategoryResponseList.clear();
        wrBillerCategoryResponseList.addAll(responseList);
        adapter.notifyDataSetChanged();
        binding.searchView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSelectCategory(WRBillerCategoryResponse category) {
        ((BillPaymentMainActivity)getBaseActivity())
                .plansRequest.billerCategoryId = category.id;

    }

    @Override
    public void onResponseMessage(String message) {
        onMessage(message);
    }
}
