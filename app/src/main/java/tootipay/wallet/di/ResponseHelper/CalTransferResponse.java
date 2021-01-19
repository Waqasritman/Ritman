package tootipay.wallet.di.ResponseHelper;

public class CalTransferResponse {
    public Double exchangeRate = 0.0;
    public Double commission = 0.0;
    public String payInCurrency = "";
    public String payOutCurrency = "";
    public Double payInAmount = 0.0;
    public Double payoutAmount = 0.0;
    public Double totalPayable = 0.0;
    public Double vatValue = 0.0;
    public Double vatPercentage = 0.0;
    public Integer recommendedAgent = 0;
    public Integer payoutBranchCode = 0;
    public String responseCode = "";
    public String description = "";
}
