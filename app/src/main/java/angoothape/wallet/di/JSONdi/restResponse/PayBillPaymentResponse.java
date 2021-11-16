package angoothape.wallet.di.JSONdi.restResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import angoothape.wallet.di.generic_response.ApiResponse;

public class PayBillPaymentResponse extends ApiResponse<PayBillPaymentResponse> {
    @SerializedName("RIT_TransactionNumber")
    @Expose
    private String rITTransactionNumber;
    @SerializedName("bill_pay_resp_data")
    @Expose
    private BillPayRespData billPayRespData;

    public String getRITTransactionNumber() {
        return rITTransactionNumber;
    }

    public void setRITTransactionNumber(String rITTransactionNumber) {
        this.rITTransactionNumber = rITTransactionNumber;
    }

    public BillPayRespData getBillPayRespData() {
        return billPayRespData;
    }

    public void setBillPayRespData(BillPayRespData billPayRespData) {
        this.billPayRespData = billPayRespData;
    }

    public class BillPayRespData {

        @SerializedName("sourceid")
        @Expose
        private String sourceid;
        @SerializedName("customerid")
        @Expose
        private String customerid;
        @SerializedName("paymentid")
        @Expose
        private String paymentid;
        @SerializedName("objectid")
        @Expose
        private String objectid;
        @SerializedName("currency")
        @Expose
        private String currency;
        @SerializedName("billerid")
        @Expose
        private String billerid;
        @SerializedName("validationid")
        @Expose
        private String validationid;
        @SerializedName("source_ref_no")
        @Expose
        private String sourceRefNo;
        @SerializedName("payment_amount")
        @Expose
        private String paymentAmount;
        @SerializedName("payment_remarks")
        @Expose
        private String paymentRemarks;
        @SerializedName("biller_name")
        @Expose
        private String billerName;
        @SerializedName("biller_category")
        @Expose
        private String billerCategory;
        @SerializedName("authenticators")
        @Expose
        private List<Authenticator> authenticators = null;
        @SerializedName("payment_type")
        @Expose
        private String paymentType;
        @SerializedName("txn_date_time")
        @Expose
        private String txnDateTime;
        @SerializedName("biller_status")
        @Expose
        private String billerStatus;
        @SerializedName("payment_account")
        @Expose
        private PaymentAccount paymentAccount;
        @SerializedName("billlist")
        @Expose
        private List<Bill> billlist = null;
        @SerializedName("payment_status")
        @Expose
        private String paymentStatus;
        @SerializedName("debit_amount")
        @Expose
        private String debitAmount;
        @SerializedName("mobileno")
        @Expose
        private String mobileno;
        @SerializedName("cou_conv_fee")
        @Expose
        private String couConvFee;
        @SerializedName("bou_conv_fee")
        @Expose
        private String bouConvFee;
        @SerializedName("bbps_ref_no")
        @Expose


        private String bbpsRefNo;

        public String getSourceid() {
            return sourceid;
        }

        public void setSourceid(String sourceid) {
            this.sourceid = sourceid;
        }

        public String getCustomerid() {
            return customerid;
        }

        public void setCustomerid(String customerid) {
            this.customerid = customerid;
        }

        public String getPaymentid() {
            return paymentid;
        }

        public void setPaymentid(String paymentid) {
            this.paymentid = paymentid;
        }

        public String getObjectid() {
            return objectid;
        }

        public void setObjectid(String objectid) {
            this.objectid = objectid;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public String getBillerid() {
            return billerid;
        }

        public void setBillerid(String billerid) {
            this.billerid = billerid;
        }

        public String getValidationid() {
            return validationid;
        }

        public void setValidationid(String validationid) {
            this.validationid = validationid;
        }

        public String getSourceRefNo() {
            return sourceRefNo;
        }

        public void setSourceRefNo(String sourceRefNo) {
            this.sourceRefNo = sourceRefNo;
        }

        public String getPaymentAmount() {
            return paymentAmount;
        }

        public void setPaymentAmount(String paymentAmount) {
            this.paymentAmount = paymentAmount;
        }

        public String getPaymentRemarks() {
            return paymentRemarks;
        }

        public void setPaymentRemarks(String paymentRemarks) {
            this.paymentRemarks = paymentRemarks;
        }

        public String getBillerName() {
            return billerName;
        }

        public void setBillerName(String billerName) {
            this.billerName = billerName;
        }

        public String getBillerCategory() {
            return billerCategory;
        }

        public void setBillerCategory(String billerCategory) {
            this.billerCategory = billerCategory;
        }

        public List<Authenticator> getAuthenticators() {
            return authenticators;
        }

        public void setAuthenticators(List<Authenticator> authenticators) {
            this.authenticators = authenticators;
        }

        public String getPaymentType() {
            return paymentType;
        }

        public void setPaymentType(String paymentType) {
            this.paymentType = paymentType;
        }

        public String getTxnDateTime() {
            return txnDateTime;
        }

        public void setTxnDateTime(String txnDateTime) {
            this.txnDateTime = txnDateTime;
        }

        public String getBillerStatus() {
            return billerStatus;
        }

        public void setBillerStatus(String billerStatus) {
            this.billerStatus = billerStatus;
        }

        public PaymentAccount getPaymentAccount() {
            return paymentAccount;
        }

        public void setPaymentAccount(PaymentAccount paymentAccount) {
            this.paymentAccount = paymentAccount;
        }

        public List<Bill> getBilllist() {
            return billlist;
        }

        public void setBilllist(List<Bill> billlist) {
            this.billlist = billlist;
        }

        public String getPaymentStatus() {
            return paymentStatus;
        }

        public void setPaymentStatus(String paymentStatus) {
            this.paymentStatus = paymentStatus;
        }

        public String getDebitAmount() {
            return debitAmount;
        }

        public void setDebitAmount(String debitAmount) {
            this.debitAmount = debitAmount;
        }

        public String getMobileno() {
            return mobileno;
        }

        public void setMobileno(String mobileno) {
            this.mobileno = mobileno;
        }

        public String getCouConvFee() {
            return couConvFee;
        }

        public void setCouConvFee(String couConvFee) {
            this.couConvFee = couConvFee;
        }

        public String getBouConvFee() {
            return bouConvFee;
        }

        public void setBouConvFee(String bouConvFee) {
            this.bouConvFee = bouConvFee;
        }

        public String getBbpsRefNo() {
            return bbpsRefNo;
        }

        public void setBbpsRefNo(String bbpsRefNo) {
            this.bbpsRefNo = bbpsRefNo;
        }

    }

    public class PaymentAccount {

        @SerializedName("objectid")
        @Expose
        private String objectid;
        @SerializedName("payment_method")
        @Expose
        private String paymentMethod;
        @SerializedName("mobileno")
        @Expose
        private String mobileno;
        @SerializedName("wallet_name")
        @Expose
        private String walletName;

        public String getObjectid() {
            return objectid;
        }

        public void setObjectid(String objectid) {
            this.objectid = objectid;
        }

        public String getPaymentMethod() {
            return paymentMethod;
        }

        public void setPaymentMethod(String paymentMethod) {
            this.paymentMethod = paymentMethod;
        }

        public String getMobileno() {
            return mobileno;
        }

        public void setMobileno(String mobileno) {
            this.mobileno = mobileno;
        }

        public String getWalletName() {
            return walletName;
        }

        public void setWalletName(String walletName) {
            this.walletName = walletName;
        }

    }


    public class Authenticator {

        @SerializedName("parameter_name")
        @Expose
        private String parameterName;
        @SerializedName("value")
        @Expose
        private String value;

        public String getParameterName() {
            return parameterName;
        }

        public void setParameterName(String parameterName) {
            this.parameterName = parameterName;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

    }

    public class Authenticator__1 {

        @SerializedName("parameter_name")
        @Expose
        private String parameterName;
        @SerializedName("value")
        @Expose
        private String value;

        public String getParameterName() {
            return parameterName;
        }

        public void setParameterName(String parameterName) {
            this.parameterName = parameterName;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

    }
    public class Bill {

        @SerializedName("objectid")
        @Expose
        private String objectid;
        @SerializedName("billid")
        @Expose
        private String billid;
        @SerializedName("billerid")
        @Expose
        private String billerid;
        @SerializedName("sourceid")
        @Expose
        private String sourceid;
        @SerializedName("customerid")
        @Expose
        private String customerid;
        @SerializedName("billstatus")
        @Expose
        private String billstatus;
        @SerializedName("authenticators")
        @Expose
        private List<Authenticator__1> authenticators = null;
        @SerializedName("billnumber")
        @Expose
        private String billnumber;
        @SerializedName("net_billamount")
        @Expose
        private String netBillamount;
        @SerializedName("validationid")
        @Expose
        private String validationid;
        @SerializedName("customer_name")
        @Expose
        private String customerName;
        @SerializedName("billamount")
        @Expose
        private String billamount;
        @SerializedName("billdate")
        @Expose
        private String billdate;
        @SerializedName("billduedate")
        @Expose
        private String billduedate;

        public String getObjectid() {
            return objectid;
        }

        public void setObjectid(String objectid) {
            this.objectid = objectid;
        }

        public String getBillid() {
            return billid;
        }

        public void setBillid(String billid) {
            this.billid = billid;
        }

        public String getBillerid() {
            return billerid;
        }

        public void setBillerid(String billerid) {
            this.billerid = billerid;
        }

        public String getSourceid() {
            return sourceid;
        }

        public void setSourceid(String sourceid) {
            this.sourceid = sourceid;
        }

        public String getCustomerid() {
            return customerid;
        }

        public void setCustomerid(String customerid) {
            this.customerid = customerid;
        }

        public String getBillstatus() {
            return billstatus;
        }

        public void setBillstatus(String billstatus) {
            this.billstatus = billstatus;
        }

        public List<Authenticator__1> getAuthenticators() {
            return authenticators;
        }

        public void setAuthenticators(List<Authenticator__1> authenticators) {
            this.authenticators = authenticators;
        }

        public String getBillnumber() {
            return billnumber;
        }

        public void setBillnumber(String billnumber) {
            this.billnumber = billnumber;
        }

        public String getNetBillamount() {
            return netBillamount;
        }

        public void setNetBillamount(String netBillamount) {
            this.netBillamount = netBillamount;
        }

        public String getValidationid() {
            return validationid;
        }

        public void setValidationid(String validationid) {
            this.validationid = validationid;
        }

        public String getCustomerName() {
            return customerName;
        }

        public void setCustomerName(String customerName) {
            this.customerName = customerName;
        }

        public String getBillamount() {
            return billamount;
        }

        public void setBillamount(String billamount) {
            this.billamount = billamount;
        }

        public String getBilldate() {
            return billdate;
        }

        public void setBilldate(String billdate) {
            this.billdate = billdate;
        }

        public String getBillduedate() {
            return billduedate;
        }

        public void setBillduedate(String billduedate) {
            this.billduedate = billduedate;
        }

    }




}