package angoothape.wallet.di.JSONdi.restResponse;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import angoothape.wallet.di.generic_response.ApiResponse;

public class RechargePlansResponse extends ApiResponse<List<RechargePlansResponse>> {

    @SerializedName("objectid")
    public String objectid;

    @SerializedName("billerid")
    public String billerid;

    @SerializedName("biller_category")
    public String biller_category;

    public String getObjectid() {
        return objectid;
    }

    public void setObjectid(String objectid) {
        this.objectid = objectid;
    }

    public String getBillerid() {
        return billerid;
    }

    public void setBillerid(String billerid) {
        this.billerid = billerid;
    }

    public String getBiller_category() {
        return biller_category;
    }

    public void setBiller_category(String biller_category) {
        this.biller_category = biller_category;
    }

    public String getBiller_name() {
        return biller_name;
    }

    public void setBiller_name(String biller_name) {
        this.biller_name = biller_name;
    }

    public String getPlanid() {
        return planid;
    }

    public void setPlanid(String planid) {
        this.planid = planid;
    }

    public String getTalktime() {
        return talktime;
    }

    public void setTalktime(String talktime) {
        this.talktime = talktime;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getValidity() {
        return validity;
    }

    public void setValidity(String validity) {
        this.validity = validity;
    }

    public String getPlan_description() {
        return plan_description;
    }

    public void setPlan_description(String plan_description) {
        this.plan_description = plan_description;
    }

    public String getCircle_name() {
        return circle_name;
    }

    public void setCircle_name(String circle_name) {
        this.circle_name = circle_name;
    }

    public String getPlan_status() {
        return plan_status;
    }

    public void setPlan_status(String plan_status) {
        this.plan_status = plan_status;
    }

    public String getPlan_category_name() {
        return plan_category_name;
    }

    public void setPlan_category_name(String plan_category_name) {
        this.plan_category_name = plan_category_name;
    }

    public String getCircleid() {
        return circleid;
    }

    public void setCircleid(String circleid) {
        this.circleid = circleid;
    }

    public String getTop_plan() {
        return top_plan;
    }

    public void setTop_plan(String top_plan) {
        this.top_plan = top_plan;
    }

    @SerializedName("biller_name")
    public String biller_name;

    @SerializedName("planid")
    public String planid;

    @SerializedName("talktime")
    public String talktime;

    @SerializedName("amount")
    public String amount;


    @SerializedName("validity")
    public String validity;

    @SerializedName("plan_description")
    public String plan_description;

    @SerializedName("circle_name")
    public String circle_name;

    @SerializedName("plan_status")
    public String plan_status;

    @SerializedName("plan_category_name")
    public String plan_category_name;

    @SerializedName("circleid")
    public String circleid;

    @SerializedName("top_plan")
    public String top_plan;
}
