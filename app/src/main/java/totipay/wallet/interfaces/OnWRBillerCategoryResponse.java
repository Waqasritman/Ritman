package totipay.wallet.interfaces;

import java.util.List;

import totipay.wallet.di.ResponseHelper.WRBillerCategoryResponse;

public interface OnWRBillerCategoryResponse extends OnMessageInterface{
    void onWRCategory(List<WRBillerCategoryResponse> responseList);
    void onSelectCategory(WRBillerCategoryResponse category);
}
