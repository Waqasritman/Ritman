package angoothape.wallet.billpayment.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import angoothape.wallet.R;
import angoothape.wallet.adapters.WRCountryListAdapter;
import angoothape.wallet.billpayment.BillPaymentMainActivity;
import angoothape.wallet.databinding.ActivityBillPaymentCountryBinding;
import angoothape.wallet.di.JSONdi.restResponse.GetWRCountryListResponseC;
import angoothape.wallet.fragments.BaseFragment;
import angoothape.wallet.interfaces.OnWRCountryList;

public class BillPaymentCountryActivity extends BaseFragment<ActivityBillPaymentCountryBinding>
        implements OnWRCountryList {


    WRCountryListAdapter adapter;
    List<GetWRCountryListResponseC> countryList;


    @Override
    protected void injectView() {

    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        countryList = new ArrayList<>();
        setSearchView();
        setupRecyclerView();


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

        Collections.sort(countryList, (o1, o2) ->
                o1.getCountry_Name().compareToIgnoreCase(o2.getCountry_Name()));
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        adapter = new
                WRCountryListAdapter(getContext() , countryList, this);
        binding.countryRecyclerView.setLayoutManager(mLayoutManager);
        binding.countryRecyclerView.setHasFixedSize(true);
        binding.countryRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onWRCountryList(List<GetWRCountryListResponseC> list) {
        countryList.clear();
        countryList.addAll(list);
        binding.searchView.setVisibility(View.VISIBLE);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onWRSelectCountry(GetWRCountryListResponseC country) {
        ((BillPaymentMainActivity) getBaseActivity())
                .plansRequest.countryCode = country.getCountry_Code();

    }

    @Override
    public void onResponseMessage(String message) {
        onMessage(message);
    }
}