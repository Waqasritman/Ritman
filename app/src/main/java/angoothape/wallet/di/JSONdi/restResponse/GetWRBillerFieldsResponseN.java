package angoothape.wallet.di.JSONdi.restResponse;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import angoothape.wallet.di.generic_response.ApiResponse;

public class GetWRBillerFieldsResponseN extends ApiResponse<List<GetWRBillerFieldsResponseN>> {

    @SerializedName("id")
    public String id;
    @SerializedName("fieldName")
    public String fieldName ;
    @SerializedName("labelName")
    public String labelName ;
    @SerializedName("description")
    public String description ;
    @SerializedName("type")
    public String type ;
    @SerializedName("maxLength")
    public Integer maxLength;
    @SerializedName("minLength")
    public Integer minLength ;

    @SerializedName("authenticator_optional")
    public String authenticator_optional ;
    @SerializedName("authenticator_regex")
    public String authenticator_regex ;
    @SerializedName("authenticator_seq")
    public String authenticator_seq ;
    @SerializedName("authenticator_encryption_required")
    public String authenticator_encryption_required ;
    @SerializedName("authenticator_user_input")
    public String authenticator_user_input ;

    public String getAuthenticator_optional() {
        return authenticator_optional;
    }

    public void setAuthenticator_optional(String authenticator_optional) {
        this.authenticator_optional = authenticator_optional;
    }

    public String getAuthenticator_regex() {
        return authenticator_regex;
    }

    public void setAuthenticator_regex(String authenticator_regex) {
        this.authenticator_regex = authenticator_regex;
    }

    public String getAuthenticator_seq() {
        return authenticator_seq;
    }

    public void setAuthenticator_seq(String authenticator_seq) {
        this.authenticator_seq = authenticator_seq;
    }

    public String getAuthenticator_encryption_required() {
        return authenticator_encryption_required;
    }

    public void setAuthenticator_encryption_required(String authenticator_encryption_required) {
        this.authenticator_encryption_required = authenticator_encryption_required;
    }

    public String getAuthenticator_user_input() {
        return authenticator_user_input;
    }

    public void setAuthenticator_user_input(String authenticator_user_input) {
        this.authenticator_user_input = authenticator_user_input;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getLabelName() {
        return labelName;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(Integer maxLength) {
        this.maxLength = maxLength;
    }

    public Integer getMinLength() {
        return minLength;
    }

    public void setMinLength(Integer minLength) {
        this.minLength = minLength;
    }
}
