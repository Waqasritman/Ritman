package angoothape.wallet.di.JSONdi.restResponse;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import angoothape.wallet.di.generic_response.ApiResponse;

public class PrepaidPlanResponse extends ApiResponse<PrepaidPlanResponse> {

  //  @SerializedName("responseCode")
   // public Integer responseCode;

    @SerializedName("errorMessage")
    public String errorMessage;

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getOperatingCurrency() {
        return operatingCurrency;
    }

    public void setOperatingCurrency(String operatingCurrency) {
        this.operatingCurrency = operatingCurrency;
    }

    public String getLocalCurrency() {
        return localCurrency;
    }

    public void setLocalCurrency(String localCurrency) {
        this.localCurrency = localCurrency;
    }

    public ArrayList<String> getRechargesubtypes() {
        return rechargesubtypes;
    }

    public void setRechargesubtypes(ArrayList<String> rechargesubtypes) {
        this.rechargesubtypes = rechargesubtypes;
    }

    public Count getCount() {
        return count;
    }

    public void setCount(Count count) {
        this.count = count;
    }

    public Plans getPlans() {
        return plans;
    }

    public void setPlans(Plans plans) {
        this.plans = plans;
    }

    @SerializedName("operatingCurrency")
    public String operatingCurrency;

    @SerializedName("localCurrency")
    public String localCurrency;

    @SerializedName("rechargeSubTypes")
    public ArrayList<String> rechargesubtypes;

    @SerializedName("count")
    public Count count;
    @SerializedName("plans")
    public Plans plans;



    public static class Count{
        @SerializedName("total")
        public String total;

        @SerializedName("TOPUP")
        public String TOPUP;

        @SerializedName("DATA_3G")
        public String DATA_3G;

        @SerializedName("SPECIAL")
        public String SPECIAL;
    }

    public static class Plans{
        @SerializedName("TOPUP")
        public List<Plan> TOPUP = new ArrayList<>();
        @SerializedName("DATA_3G")
        public List<Plan> DATA_3G = new ArrayList<>();
        @SerializedName("SPECIAL")
        public List<Plan> SPECIAL = new ArrayList<>();

    }



    public class Plan{
        @SerializedName("planId")
        public String planId = "";

        @SerializedName("rechargeSubType")
        public String rechargeSubType="";

        @SerializedName("MRP")
        public Double MRP;

        @SerializedName("rechargeAmount")
        public Double rechargeAmount;

        @SerializedName("benefits")
        public String benefits;

        @SerializedName("validity")
        public String validity;

        @SerializedName("talktime")
        public String talktime;

        @SerializedName("dataMB")
        public String dataMB;
    }

}
