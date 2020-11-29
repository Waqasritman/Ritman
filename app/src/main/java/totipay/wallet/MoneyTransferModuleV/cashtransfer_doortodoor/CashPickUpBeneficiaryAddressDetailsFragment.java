package totipay.wallet.MoneyTransferModuleV.cashtransfer_doortodoor;

import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import totipay.wallet.MoneyTransferModuleV.Helpers.CountryTypesHelper;
import totipay.wallet.MoneyTransferModuleV.MoneyTransferMainLayout;
import totipay.wallet.R;
import totipay.wallet.databinding.ActivityCashPickupSecondBinding;
import totipay.wallet.di.RequestHelper.BeneficiaryAddRequest;
import totipay.wallet.di.RequestHelper.GetCashNetworkListRequest;
import totipay.wallet.di.RequestHelper.GetYBranchRequest;
import totipay.wallet.di.RequestHelper.GetYCityRequest;
import totipay.wallet.di.RequestHelper.GetYLocationRequest;
import totipay.wallet.di.ResponseHelper.AddBeneficiaryResponse;
import totipay.wallet.di.ResponseHelper.GetCashNetworkListResponse;
import totipay.wallet.di.ResponseHelper.YBranchResponse;
import totipay.wallet.di.ResponseHelper.YCityResponse;
import totipay.wallet.di.ResponseHelper.YLocationResponse;
import totipay.wallet.di.apicaller.AddBeneficiaryTask;
import totipay.wallet.di.apicaller.GetCashNetworkListTask;
import totipay.wallet.di.apicaller.GetYBranchTask;
import totipay.wallet.di.apicaller.GetYCityTask;
import totipay.wallet.di.apicaller.GetYLocationTask;
import totipay.wallet.dialogs.YemenBranchDialog;
import totipay.wallet.dialogs.YemenCityDialog;
import totipay.wallet.dialogs.YemenLocationDialog;
import totipay.wallet.fragments.BaseFragment;
import totipay.wallet.interfaces.OnApiResponse;
import totipay.wallet.interfaces.OnGetCashNetworkList;
import totipay.wallet.interfaces.OnGetYBranch;
import totipay.wallet.interfaces.OnGetYCities;
import totipay.wallet.interfaces.OnGetYLocation;
import totipay.wallet.utils.IsNetworkConnection;

import java.util.ArrayList;
import java.util.List;


public class CashPickUpBeneficiaryAddressDetailsFragment
        extends BaseFragment<ActivityCashPickupSecondBinding>
        implements OnGetCashNetworkList, OnApiResponse, OnGetYCities
        , OnGetYLocation, OnGetYBranch {

    BeneficiaryAddRequest addBeneficiaryRequest;
    List<GetCashNetworkListResponse> networkLists;

    List<YCityResponse> cityList;

    @Override

    public void onResume() {
        super.onResume();
        ((MoneyTransferMainLayout) getBaseActivity()).binding.toolBar.titleTxt
                .setText(getString(R.string.cash_beneficiary));
    }

    @Override
    protected void injectView() {

    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        this.cityList = new ArrayList<>();
        addBeneficiaryRequest = ((MoneyTransferMainLayout) getBaseActivity())
                .bankTransferViewModel.beneficiaryAddRequest;
        addBeneficiaryRequest.languageId = getSessionManager().getlanguageselection();

        if (addBeneficiaryRequest.countryID == CountryTypesHelper.YEMEN) {
            binding.yemenLayout.setVisibility(View.VISIBLE);
            binding.localCityId.setVisibility(View.GONE);
            binding.cityName.setVisibility(View.GONE);
            binding.agentsForCashId.setVisibility(View.GONE);
            binding.agentsForCash.setVisibility(View.GONE);
        }

        if (IsNetworkConnection.checkNetworkConnection(getContext())) {
            GetCashNetworkListRequest receiptRequest = new GetCashNetworkListRequest();
            receiptRequest.countryCode = addBeneficiaryRequest.PayoutCountryCode;
            receiptRequest.languageId = getSessionManager().getlanguageselection();

            GetCashNetworkListTask task = new GetCashNetworkListTask(getContext(), this);
            task.execute(receiptRequest);
        } else {
            onMessage(getString(R.string.no_internet));
        }

        binding.agentsForCash.setOnClickListener(v -> {
            if (networkLists != null) {
                if (networkLists.size() > 1) {

                }
            }
        });

        binding.nextLayout.setOnClickListener(v -> {
            if (IsNetworkConnection.checkNetworkConnection(getContext())) {
                AddBeneficiaryTask task = new AddBeneficiaryTask(getActivity(), this);
                task.execute(addBeneficiaryRequest);
            } else {
                onMessage(getString(R.string.no_internet));
            }
        });

        binding.yemenCity.setOnClickListener(v -> {
            if (cityList.isEmpty()) {
                if (IsNetworkConnection.checkNetworkConnection(getContext())) {
                    GetYCityRequest request = new GetYCityRequest();
                    request.languageId = getSessionManager().getlanguageselection();

                    GetYCityTask task = new GetYCityTask(getContext(), this);
                    task.execute(request);
                } else {
                    onMessage(getString(R.string.no_internet));
                }
            } else {
                showYemenCountryDialog(cityList);
            }

        });

        binding.yemenLocation.setOnClickListener(v -> {

            if (addBeneficiaryRequest.cityID != -1) {
                if (IsNetworkConnection.checkNetworkConnection(getContext())) {
                    GetYLocationRequest request = new GetYLocationRequest();
                    request.cityID = addBeneficiaryRequest.cityID;
                    request.languageID = getSessionManager().getlanguageselection();

                    GetYLocationTask task = new GetYLocationTask(getContext(),
                            this);
                    task.execute(request);

                } else {
                    onMessage(getString(R.string.loading_txt));
                }
            } else {
                onMessage(getString(R.string.select_city_error));
            }

        });

        binding.yemenBranch.setOnClickListener(v -> {
            if (IsNetworkConnection.checkNetworkConnection(getContext())) {
                GetYBranchRequest request = new GetYBranchRequest();
                request.locationID = addBeneficiaryRequest.locationID;
                request.cityID = addBeneficiaryRequest.cityID;
                request.languageID = getSessionManager().getlanguageselection();

                GetYBranchTask task = new GetYBranchTask(getContext(), this);
                task.execute(request);

            } else {
            onMessage(getString(R.string.no_internet));
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_cash_pickup_second;
    }

    @Override
    public void onGetNetworkList(List<GetCashNetworkListResponse> networkLists) {
        this.networkLists = new ArrayList<>();
        if (networkLists.size() == 1) {
            addBeneficiaryRequest.PaymentMode = networkLists.get(0).paymentMode;
            addBeneficiaryRequest.PayOutBranchCode = networkLists.get(0).payOutBranchCode;
            binding.agentsForCash.setText(networkLists.get(0).payOutAgent);
        }
        this.networkLists.addAll(networkLists);
    }

    @Override
    public void onResponseMessage(String message) {
        onMessage(message);
    }

    @Override
    public void onSuccessResponse(Object response) {
        if (response instanceof AddBeneficiaryResponse) {
            onMessage(getString(R.string.bene_added_successfully));
            addBeneficiaryRequest.beneficiaryNo = ((AddBeneficiaryResponse) response).beneficiaryNo;
            ((MoneyTransferMainLayout) getBaseActivity())
                    .bankTransferViewModel.fillBeneficiaryDetails(addBeneficiaryRequest);
            Navigation.findNavController(getView())
                    .navigate(R.id.action_cashPickupSecondActivity_to_cashPaymentThirdActivity);
        }
    }

    @Override
    public void onError(String message) {
        onMessage(message);
    }

    @Override
    public void onGetCities(List<YCityResponse> citiesList) {
        this.cityList.addAll(citiesList);
        showYemenCountryDialog(this.cityList);
    }

    @Override
    public void onSelectYCity(YCityResponse city) {
        addBeneficiaryRequest.cityID = city.cityId;
        binding.yemenCity.setText(city.cityName);
        hideBranch();
    }

    @Override
    public void onGetYLocations(List<YLocationResponse> yLocations) {
        showYemenLocationDialog(yLocations);
    }

    @Override
    public void onSelectYLocation(YLocationResponse location) {
        addBeneficiaryRequest.locationID = location.locationID;
        binding.yemenLocation.setText(location.locationName);
        showBranch();
    }

    public void hideBranch() {
        binding.yemenBranch.setText("");
        binding.yemenBranchLayout.setVisibility(View.GONE);
    }

    public void showBranch() {
        binding.yemenBranch.setText("");
        binding.yemenBranchLayout.setVisibility(View.VISIBLE);
    }

    public void showYemenCountryDialog(List<YCityResponse> list) {
        YemenCityDialog dialog = new YemenCityDialog(list, this);
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        dialog.show(transaction, "");
    }


    public void showYemenLocationDialog(List<YLocationResponse> list) {
        YemenLocationDialog dialog = new YemenLocationDialog(list, this);
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        dialog.show(transaction, "");
    }


    public void showYemenBranchDialog(List<YBranchResponse> list) {
        YemenBranchDialog dialog = new YemenBranchDialog(list, this);
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        dialog.show(transaction, "");
    }

    @Override
    public void onGetYBranch(List<YBranchResponse> branchList) {
        if (branchList.size() == 1) {
            binding.yemenBranch.setText(branchList.get(0).branchName);
            addBeneficiaryRequest.PaymentMode = "cash";
            addBeneficiaryRequest.PayOutBranchCode = String.valueOf(branchList.get(0).branchID);
        } else {
            showYemenBranchDialog(branchList);
        }
    }

    @Override
    public void onSelectBranch(YBranchResponse branch) {
        addBeneficiaryRequest.PaymentMode = "cash";
        addBeneficiaryRequest.PayOutBranchCode = String.valueOf(branch.branchID);
        binding.yemenBranch.setText(branch.branchName);
    }
}
