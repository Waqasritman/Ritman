package angoothape.wallet.di.JSONdi.restResponse;

import com.google.gson.annotations.SerializedName;

import angoothape.wallet.di.generic_response.ApiResponse;

public class CaltransferResponse extends ApiResponse<CaltransferResponse> {

    @SerializedName("ExchangeRate")
    public Double ExchangeRate;
    @SerializedName("Commission")
    public Double Commission;
    @SerializedName("PayInCurrency")
    public String PayInCurrency;
    @SerializedName("PayoutCurrency")
    public String PayoutCurrency;
    @SerializedName("PayInAmount")
    public Double PayInAmount;
    @SerializedName("PayoutAmount")
    public Double PayoutAmount;
    @SerializedName("TotalPayable")
    public Double TotalPayable;

    @SerializedName("RecommendAgent ")
    public Integer RecommendAgent ;

    public Integer getRecommendAgent() {
        return RecommendAgent;
    }

    public void setRecommendAgent(Integer recommendAgent) {
        RecommendAgent = recommendAgent;
    }

    public Double getExchangeRate() {
        return ExchangeRate;
    }

    public void setExchangeRate(Double exchangeRate) {
        ExchangeRate = exchangeRate;
    }

    public Double getCommission() {
        return Commission;
    }

    public void setCommission(Double commission) {
        Commission = commission;
    }

    public String getPayInCurrency() {
        return PayInCurrency;
    }

    public void setPayInCurrency(String payInCurrency) {
        PayInCurrency = payInCurrency;
    }

    public String getPayoutCurrency() {
        return PayoutCurrency;
    }

    public void setPayoutCurrency(String payoutCurrency) {
        PayoutCurrency = payoutCurrency;
    }

    public Double getPayInAmount() {
        return PayInAmount;
    }

    public void setPayInAmount(Double payInAmount) {
        PayInAmount = payInAmount;
    }

    public Double getPayoutAmount() {
        return PayoutAmount;
    }

    public void setPayoutAmount(Double payoutAmount) {
        PayoutAmount = payoutAmount;
    }

    public Double getTotalPayable() {
        return TotalPayable;
    }

    public void setTotalPayable(Double totalPayable) {
        TotalPayable = totalPayable;
    }
}
