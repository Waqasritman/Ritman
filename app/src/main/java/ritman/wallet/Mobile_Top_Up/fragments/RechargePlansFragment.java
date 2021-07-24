package ritman.wallet.Mobile_Top_Up.fragments;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ritman.wallet.Mobile_Top_Up.MobileTopUpMainActivity;
import ritman.wallet.Mobile_Top_Up.viewmodels.MobileTopUpViewModel;
import ritman.wallet.MoneyTransferModuleV.viewmodels.BankTransferViewModel;
import ritman.wallet.R;
import ritman.wallet.adapters.PlanNameAdapter;
import ritman.wallet.adapters.RechargePlanAdapter;
import ritman.wallet.databinding.RechargePlansFragmentLayoutBinding;
import ritman.wallet.di.JSONdi.Status;
import ritman.wallet.di.JSONdi.restRequest.BillDetailRequest;
import ritman.wallet.di.JSONdi.restRequest.PlanCategoriesRequest;
import ritman.wallet.di.JSONdi.restRequest.RechargePlansRequest;
import ritman.wallet.di.JSONdi.restResponse.PlanCategoriesResponse;
import ritman.wallet.di.JSONdi.restResponse.RechargePlansResponse;
import ritman.wallet.fragments.BaseFragment;
import ritman.wallet.interfaces.PlanName;
import ritman.wallet.interfaces.RechargePlanName;
import ritman.wallet.utils.IsNetworkConnection;
import ritman.wallet.utils.Utils;

import static android.content.Context.WIFI_SERVICE;

public class RechargePlansFragment extends BaseFragment<RechargePlansFragmentLayoutBinding> implements RechargePlanName {
    String billerid, circle_name, PlanCategory,MobileNumber,Field1Name,Field2Name,billerlogo,
            Field3Name,planid,Field3,IMEINumber,ipAddress;
    MobileTopUpViewModel viewModel;
   // BankTransferViewModel viewModel1;
    List<RechargePlansResponse> rechargePlansResponses;
    RechargePlanAdapter planNameAdapter;
    String payment_amount;


    @Override
    protected void injectView() {

    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        rechargePlansResponses = new ArrayList<>();
        viewModel = ((MobileTopUpMainActivity) getBaseActivity()).viewModel;
      //  viewModel1 = new ViewModelProvider(this).get(BankTransferViewModel.class);
        billerid = getArguments().getString("billerid");
        circle_name = getArguments().getString("circle_name");
        PlanCategory = getArguments().getString("PlanCategory");
        MobileNumber = getArguments().getString("MobileNumber");
        Field1Name = getArguments().getString("Field1Name");
        Field2Name = getArguments().getString("Field2Name");
        Field3Name = getArguments().getString("Field3Name");
        billerlogo = getArguments().getString("billerlogo");

        IMEINumber = getDeviceId(getContext());
        getIpAddressOfDevice();
        setupRecyclerView();
        getRechargePlan();

    }

    void getRechargePlan() {
        if (IsNetworkConnection.checkNetworkConnection(getContext())) {
            Utils.showCustomProgressDialog(getContext(), false);
            RechargePlansRequest request = new RechargePlansRequest();
            request.billerid = billerid;
            request.circle_name = circle_name;
            request.plan_category = PlanCategory;
            request.CountryCode = "IN";

            viewModel.getRechargePlanName(request, getSessionManager().getMerchantName()).observe(getViewLifecycleOwner()
                    , response -> {
                        Utils.hideCustomProgressDialog();
                        if (response.status == Status.ERROR) {
                            onMessage(getString(response.messageResourceId));
                        } else {
                            assert response.resource != null;
                            if (response.resource.responseCode.equals(101)) {
                                onRechargePlanName(response.resource.data);

//                                for (int i=0;i<rechargePlansResponses.size();i++){
//                                    payment_amount=rechargePlansResponses.get(i).amount;
//                                    planid=rechargePlansResponses.get(i).planid;
//                                }

                            } else {

                                onMessage(response.resource.description);

                            }
                        }
                    });
        } else {
            onMessage(getString(R.string.no_internet));
        }

    }

    @Override
    public int getLayoutId() {
        return R.layout.recharge_plans_fragment_layout;
    }

    private void setupRecyclerView() {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        planNameAdapter = new
                RechargePlanAdapter(getContext(), rechargePlansResponses, this);
        binding.recyPlanName.setLayoutManager(mLayoutManager);
        binding.recyPlanName.setHasFixedSize(true);
        binding.recyPlanName.setAdapter(planNameAdapter);
    }

    @Override
    public void onRechargePlanName(List<RechargePlansResponse> responseList) {
        rechargePlansResponses.clear();
        rechargePlansResponses.addAll(responseList);
        planNameAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSelectRechargePlanName(RechargePlansResponse PlanName) {
        payment_amount=PlanName.amount;
        planid=PlanName.planid;
        getPrepaidBillDetails();

    }
    void getPrepaidBillDetails() {
        Utils.showCustomProgressDialog(getContext(), false);
        //binding.progressBar.setVisibility(View.VISIBLE);
        BillDetailRequest request = new BillDetailRequest();
        request.countryCode = "IN";
        request.BillerID = billerid;
        request.MobileNumber = MobileNumber;
        if ((Field1Name!=null && MobileNumber!=null) && (Field2Name!=null && planid!=null)
                &&(Field3Name!=null && Field3!=null)){
            request.Field1Name = Field1Name;
            request.Field1 = MobileNumber;
            request.Field2Name = Field2Name;
            request.Field2 = planid;
            request.Field3Name = Field3Name;
            request.Field3 = Field3;
        }
        else if ((Field1Name!=null && MobileNumber!=null) && (Field2Name!=null && planid!=null)){
            request.Field1Name = Field1Name;
            request.Field1 = MobileNumber;
            request.Field2Name = Field2Name;
            request.Field2 = planid;
            request.Field3Name = "";
            request.Field3 = "";
        }
        else {
            request.Field1Name = Field1Name;
            request.Field1 = MobileNumber;
            request.Field2Name = "";
            request.Field2 = "";
            request.Field3Name = "";
            request.Field3 = "";
        }

        request.imei = IMEINumber;//"5468748458458454";
        request.ip = ipAddress;
        request.payment_amount = payment_amount;
        request.currency = "356";


        viewModel.getBillDetails(request, getSessionManager().getMerchantName()).observe(getViewLifecycleOwner()

                , response -> {
                    if (response.status == Status.ERROR) {
                        onMessage(getString(response.messageResourceId));
                    } else {
                        assert response.resource != null;
                        if (response.resource.responseCode.equals(101)) {
                            //  binding.progressBar.setVisibility(View.GONE);
                            Utils.hideCustomProgressDialog();
                            Bundle bundle=new Bundle();
                            bundle.putString("billerid",billerid);
                            bundle.putString("payment_amount",payment_amount);
                            bundle.putString("MobileNumber",MobileNumber);
                            bundle.putString("Field1Name",Field1Name);
                            bundle.putString("Field2Name",Field2Name);
                            bundle.putString("Field3Name",Field3Name);
                            bundle.putString("planid",planid);
                            bundle.putString("billerlogo",billerlogo);
                            Navigation.findNavController(binding.getRoot()).navigate(R.id.action_rechargePlansFragment_to_mobileTopUpPrepaidBillDetailsFragment,bundle);

                        } else if (response.resource.responseCode.equals(100)) {
                            onMessage(response.resource.data.message);
                            Utils.hideCustomProgressDialog();
                            // binding.progressBar.setVisibility(View.GONE);
                        } else {
                            // binding.progressBar.setVisibility(View.GONE);
                            Utils.hideCustomProgressDialog();
                            onMessage(response.resource.description);

                        }
                    }
                });

    }

    public static String getDeviceId(Context context) {

        String deviceId;

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            deviceId = Settings.Secure.getString(
                    context.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
        } else {
            final TelephonyManager mTelephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (mTelephony.getDeviceId() != null) {
                deviceId = mTelephony.getDeviceId();
            } else {
                deviceId = Settings.Secure.getString(
                        context.getContentResolver(),
                        Settings.Secure.ANDROID_ID);
            }
        }

        return deviceId;
    }

    public void getIpAddressOfDevice() {
        WifiManager wifiManager = (WifiManager) getContext().getApplicationContext().getSystemService(WIFI_SERVICE);
        ipAddress = Formatter.formatIpAddress(wifiManager.getConnectionInfo().getIpAddress());
    }
}
