package angoothape.wallet.interfaces;

import java.util.List;

import angoothape.wallet.di.JSONdi.restResponse.GetWRBillerCategoryResponseC;

public interface OnWRBillerCategoryResponse extends OnMessageInterface{
    void onWRCategory(List<GetWRBillerCategoryResponseC> responseList);
    void onSelectCategory(GetWRBillerCategoryResponseC category);
}
