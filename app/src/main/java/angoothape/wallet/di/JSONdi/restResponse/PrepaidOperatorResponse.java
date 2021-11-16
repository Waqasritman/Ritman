package angoothape.wallet.di.JSONdi.restResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import angoothape.wallet.di.generic_response.ApiResponse;

public class PrepaidOperatorResponse extends ApiResponse<PrepaidOperatorResponse> {

    @SerializedName("objectid")
    @Expose
    private String objectid;
    @SerializedName("operatorseriesid")
    @Expose
    private String operatorseriesid;
    @SerializedName("billerid")
    @Expose
    private String billerid;
    @SerializedName("biller_name")
    @Expose
    private String billerName;
    @SerializedName("circle_name")
    @Expose
    private String circleName;
    @SerializedName("circleid")
    @Expose
    private String circleid;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("created_date")
    @Expose
    private String createdDate;
    @SerializedName("last_modified_date")
    @Expose
    private String lastModifiedDate;
    @SerializedName("biller_logo")
    @Expose
    private String billerLogo;
    @SerializedName("biller_bill_copy")
    @Expose
    private String billerBillCopy;
    @SerializedName("biller_type")
    @Expose
    private String billerType;
    @SerializedName("partial_pay")
    @Expose
    private String partialPay;
    @SerializedName("pay_after_duedate")
    @Expose
    private String payAfterDuedate;
    @SerializedName("paymentamount_validation")
    @Expose
    private String paymentamountValidation;
    @SerializedName("biller_fields")
    @Expose
    private List<BillerField> billerFields = null;

    public String getObjectid() {
        return objectid;
    }

    public void setObjectid(String objectid) {
        this.objectid = objectid;
    }

    public String getOperatorseriesid() {
        return operatorseriesid;
    }

    public void setOperatorseriesid(String operatorseriesid) {
        this.operatorseriesid = operatorseriesid;
    }

    public String getBillerid() {
        return billerid;
    }

    public void setBillerid(String billerid) {
        this.billerid = billerid;
    }

    public String getBillerName() {
        return billerName;
    }

    public void setBillerName(String billerName) {
        this.billerName = billerName;
    }

    public String getCircleName() {
        return circleName;
    }

    public void setCircleName(String circleName) {
        this.circleName = circleName;
    }

    public String getCircleid() {
        return circleid;
    }

    public void setCircleid(String circleid) {
        this.circleid = circleid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(String lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getBillerLogo() {
        return billerLogo;
    }

    public void setBillerLogo(String billerLogo) {
        this.billerLogo = billerLogo;
    }

    public String getBillerBillCopy() {
        return billerBillCopy;
    }

    public void setBillerBillCopy(String billerBillCopy) {
        this.billerBillCopy = billerBillCopy;
    }

    public String getBillerType() {
        return billerType;
    }

    public void setBillerType(String billerType) {
        this.billerType = billerType;
    }

    public String getPartialPay() {
        return partialPay;
    }

    public void setPartialPay(String partialPay) {
        this.partialPay = partialPay;
    }

    public String getPayAfterDuedate() {
        return payAfterDuedate;
    }

    public void setPayAfterDuedate(String payAfterDuedate) {
        this.payAfterDuedate = payAfterDuedate;
    }

    public String getPaymentamountValidation() {
        return paymentamountValidation;
    }

    public void setPaymentamountValidation(String paymentamountValidation) {
        this.paymentamountValidation = paymentamountValidation;
    }

    public List<BillerField> getBillerFields() {
        return billerFields;
    }

    public void setBillerFields(List<BillerField> billerFields) {
        this.billerFields = billerFields;
    }

    public class BillerField {

        @SerializedName("parameter_name")
        @Expose
        private String parameterName;
        @SerializedName("data_type")
        @Expose
        private String dataType;
        @SerializedName("optional")
        @Expose
        private String optional;
        @SerializedName("regex")
        @Expose
        private String regex;
        @SerializedName("error_message")
        @Expose
        private String errorMessage;
        @SerializedName("seq")
        @Expose
        private Object seq;

        public String getParameterName() {
            return parameterName;
        }

        public void setParameterName(String parameterName) {
            this.parameterName = parameterName;
        }

        public String getDataType() {
            return dataType;
        }

        public void setDataType(String dataType) {
            this.dataType = dataType;
        }

        public String getOptional() {
            return optional;
        }

        public void setOptional(String optional) {
            this.optional = optional;
        }

        public String getRegex() {
            return regex;
        }

        public void setRegex(String regex) {
            this.regex = regex;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

        public void setErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
        }

        public Object getSeq() {
            return seq;
        }

        public void setSeq(Object seq) {
            this.seq = seq;
        }

    }

}
