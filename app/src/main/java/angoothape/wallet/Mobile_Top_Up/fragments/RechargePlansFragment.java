package angoothape.wallet.Mobile_Top_Up.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import angoothape.wallet.Mobile_Top_Up.MobileTopUpMainActivity;
import angoothape.wallet.Mobile_Top_Up.viewmodels.MobileTopUpViewModel;
import angoothape.wallet.R;
import angoothape.wallet.adapters.RechargePlanAdapter;
import angoothape.wallet.databinding.RechargePlansFragmentLayoutBinding;
import angoothape.wallet.di.AESHelper;
import angoothape.wallet.di.JSONdi.Status;
import angoothape.wallet.di.JSONdi.restRequest.AERequest;
import angoothape.wallet.di.JSONdi.restRequest.BillDetailRequest;
import angoothape.wallet.di.JSONdi.restRequest.RechargePlansRequest;
import angoothape.wallet.di.JSONdi.restResponse.BillDetailResponse;
import angoothape.wallet.di.JSONdi.restResponse.BillDetailsErrorResponse;
import angoothape.wallet.di.JSONdi.restResponse.BillDetailsMainResponse;
import angoothape.wallet.di.JSONdi.restResponse.PlanCategoriesResponse;
import angoothape.wallet.di.JSONdi.restResponse.RechargePlansResponse;
import angoothape.wallet.di.JSONdi.retrofit.KeyHelper;
import angoothape.wallet.di.JSONdi.retrofit.RestClient;
import angoothape.wallet.fragments.BaseFragment;
import angoothape.wallet.interfaces.RechargePlanName;
import angoothape.wallet.utils.IsNetworkConnection;
import angoothape.wallet.utils.Utils;

import static android.content.Context.WIFI_SERVICE;

public class RechargePlansFragment extends BaseFragment<RechargePlansFragmentLayoutBinding> implements RechargePlanName {
    String billerid, circle_name, PlanCategory, MobileNumber, Field1Name, Field2Name, billerlogo,
            Field3Name, planid, Field3, IMEINumber = "", ipAddress = "";
    MobileTopUpViewModel viewModel;

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
        assert getArguments() != null;
        billerid = getArguments().getString("billerid");
        circle_name = getArguments().getString("circle_name");
        PlanCategory = getArguments().getString("PlanCategory");
        MobileNumber = getArguments().getString("MobileNumber");
        Field1Name = getArguments().getString("Field1Name");
        Field2Name = getArguments().getString("Field2Name");
        Field3Name = getArguments().getString("Field3Name");
        billerlogo = getArguments().getString("billerlogo");


        setupRecyclerView();
        getRechargePlan();
        int permissionCheck = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_PHONE_STATE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getBaseActivity(), new String[]{Manifest.permission.READ_PHONE_STATE},
                    1);
        } else {
            IMEINumber = getDeviceId(getContext());
            getIpAddressOfDevice();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    //TODO
                    IMEINumber = getDeviceId(getContext());
                    getIpAddressOfDevice();
                } else {
                    Log.e("TAG", "Permission Needs: ");
                }
                break;

            default:
                break;
        }
    }

    void getRechargePlan() {
        if (IsNetworkConnection.checkNetworkConnection(getContext())) {
            Utils.showCustomProgressDialog(getContext(), false);
            String gKey = KeyHelper.getKey(getSessionManager().getMerchantName()).trim() + KeyHelper.getSKey(KeyHelper
                    .getKey(getSessionManager().getMerchantName())).trim();
            RechargePlansRequest request = new RechargePlansRequest();
            request.billerid = billerid;
            request.circle_name = circle_name;
            request.plan_category = PlanCategory;
            request.CountryCode = "IN";
            String body = RestClient.makeGSONString(request);
            Log.e("getRechargePlan: ", body);
            AERequest aeRequest = new AERequest();
            aeRequest.body = AESHelper.encrypt(body.trim(), gKey.trim());
            viewModel.getRechargePlanName(aeRequest, KeyHelper.getKey(getSessionManager().getMerchantName()).trim(),
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
                                Log.e("getPlanName: ", bodyy);
                                try {
                                    Gson gson = new Gson();
                                    Type type = new TypeToken<List<RechargePlansResponse>>() {
                                    }.getType();
                                    List<RechargePlansResponse> data = gson.fromJson(bodyy, type);
                                    onRechargePlanName(data);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            } else if (response.resource.responseCode.equals(305)) {
                                onMessage(response.resource.description + "\nTry again later");
                                Navigation.findNavController(binding.getRoot()).navigateUp();
                            } else {

                                Utils.hideCustomProgressDialog();
                                if (response.resource.data != null) {
                                    String bodyy = AESHelper.decrypt(response.resource.data.body
                                            , gKey);
                                    Log.e("getBillDetails: ", bodyy);
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
        payment_amount = PlanName.amount;
        planid = PlanName.planid;
        getPrepaidBillDetails();
    }

    void getPrepaidBillDetails() {
        Utils.showCustomProgressDialog(getContext(), false);
        //binding.progressBar.setVisibility(View.VISIBLE);
        String gKey = KeyHelper.getKey(getSessionManager().getMerchantName()).trim() + KeyHelper.getSKey(KeyHelper
                .getKey(getSessionManager().getMerchantName())).trim();
        BillDetailRequest request = new BillDetailRequest();
        request.countryCode = "IN";
        request.BillerID = billerid;
        request.MobileNumber = MobileNumber;
        if ((Field1Name != null && MobileNumber != null) && (Field2Name != null && planid != null)
                && (Field3Name != null && Field3 != null)) {
            request.Field1Name = Field1Name;
            request.Field1 = MobileNumber;
            request.Field2Name = Field2Name;
            request.Field2 = planid;
            request.Field3Name = Field3Name;
            request.Field3 = Field3;
        } else if ((Field1Name != null && MobileNumber != null) && (Field2Name != null && planid != null)) {
            request.Field1Name = Field1Name;
            request.Field1 = MobileNumber;
            request.Field2Name = Field2Name;
            request.Field2 = planid;
            request.Field3Name = "";
            request.Field3 = "";
        } else {
            request.Field1Name = Field1Name;
            request.Field1 = MobileNumber;
            request.Field2Name = "";
            request.Field2 = "";
            request.Field3Name = "";
            request.Field3 = "";
        }

        if (IMEINumber.isEmpty() || ipAddress.isEmpty()) {
            IMEINumber = getDeviceId(getContext());
            getIpAddressOfDevice();
        }

        request.imei = IMEINumber;//"5468748458458454";
        request.ip = ipAddress;
        request.payment_amount = payment_amount;
        request.currency = "356";
        String body = RestClient.makeGSONString(request);
        AERequest requestc = new AERequest();
        requestc.body = AESHelper.encrypt(body.trim(), gKey.trim());
        Log.e("getPrepaidBillDetails: ", body);
        viewModel.getBillDetails(requestc, KeyHelper.getKey(getSessionManager().getMerchantName()).trim(),
                KeyHelper.getSKey(KeyHelper
                        .getKey(getSessionManager().getMerchantName())))
                .observe(getViewLifecycleOwner(), response -> {
                    if (response.status == Status.ERROR) {
                        onError(getString(response.messageResourceId));
                    } else {
                        assert response.resource != null;
                        if (response.resource.responseCode.equals(101)) {
                            //  binding.progressBar.setVisibility(View.GONE);
                            Utils.hideCustomProgressDialog();

                            String bodyy = AESHelper.decrypt(response.resource.data.body
                                    , gKey);
                            Log.e("getPlanName: ", bodyy);
                            try {
                                Gson gson = new Gson();
                                Type type = new TypeToken<BillDetailsMainResponse>() {
                                }.getType();
                                BillDetailsMainResponse data = gson.fromJson(bodyy, type);
                                Bundle bundle = new Bundle();
                                bundle.putString("billerid", billerid);
                                bundle.putString("payment_amount", payment_amount);
                                bundle.putString("MobileNumber", MobileNumber);
                                bundle.putString("Field1Name", Field1Name);
                                bundle.putString("Field2Name", Field2Name);
                                bundle.putString("Field3Name", Field3Name);
                                bundle.putString("planid", planid);
                                bundle.putString("billerlogo", billerlogo);

                                Navigation.findNavController(binding.getRoot()).navigate(R.id.action_rechargePlansFragment_to_mobileTopUpPrepaidBillDetailsFragment, bundle);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        } else if (response.resource.responseCode.equals(100)) {
                            Utils.hideCustomProgressDialog();
                            if (response.resource.data != null) {
                                String bodyy = AESHelper.decrypt(response.resource.data.body
                                        , gKey);
                                Log.e("getBillDetails: ", bodyy);

                                if (!body.isEmpty()) {
                                    try {
                                        Gson gson = new Gson();
                                        Type type = new TypeToken<BillDetailsErrorResponse>() {
                                        }.getType();
                                        BillDetailsErrorResponse data = gson.fromJson(bodyy, type);

                                        onError(data.message.replace("ERR_1", ""));
                                    } catch (Exception e) {
                                        onError(response.resource.description);
                                    }
                                } else {
                                    onError(response.resource.description);
                                }


                            } else {
                                onError(response.resource.description);
                            }
                            // binding.progressBar.setVisibility(View.GONE);
                        } else if (response.resource.responseCode.equals(305)) {
                            onMessage(response.resource.description + "\nTry again later");
                            Navigation.findNavController(binding.getRoot()).navigateUp();
                        } else {
                            // binding.progressBar.setVisibility(View.GONE);
                            Utils.hideCustomProgressDialog();
                            if (response.resource.data != null) {
                                String bodyy = AESHelper.decrypt(response.resource.data.body
                                        , gKey);
                                Log.e("getBillDetails: ", bodyy);
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
