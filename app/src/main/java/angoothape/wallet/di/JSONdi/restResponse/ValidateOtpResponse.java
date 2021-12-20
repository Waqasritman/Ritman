package angoothape.wallet.di.JSONdi.restResponse;

import com.google.gson.annotations.SerializedName;

import angoothape.wallet.di.generic_response.ApiResponse;

public class ValidateOtpResponse extends ApiResponse<ValidateOtpResponse> {

    @SerializedName("requestId_")
    public String requestId_;
    @SerializedName("status_")
    public String status_;
    @SerializedName("wadh_")
    public String wadh_;
    @SerializedName("kycToken_")
    public String kycToken_;

    public String getRequestId_() {
        return requestId_;
    }

    public void setRequestId_(String requestId_) {
        this.requestId_ = requestId_;
    }

    public String getStatus_() {
        return status_;
    }

    public void setStatus_(String status_) {
        this.status_ = status_;
    }

    public String getWadh_() {
        return wadh_;
    }

    public void setWadh_(String wadh_) {
        this.wadh_ = wadh_;
    }

    public String getKycToken_() {
        return kycToken_;
    }

    public void setKycToken_(String kycToken_) {
        this.kycToken_ = kycToken_;
    }
}
