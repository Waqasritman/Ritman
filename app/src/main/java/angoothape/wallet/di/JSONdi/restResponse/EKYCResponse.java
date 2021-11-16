package angoothape.wallet.di.JSONdi.restResponse;

import com.google.gson.annotations.SerializedName;

public class EKYCResponse {
    @SerializedName("Is_eKyc_Verified")
    public boolean isKycApproved;
}
