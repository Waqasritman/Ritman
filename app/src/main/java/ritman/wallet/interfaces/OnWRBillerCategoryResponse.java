package ritman.wallet.interfaces;

import java.util.List;

import ritman.wallet.di.JSONdi.restResponse.GetWRBillerCategoryResponseC;
import ritman.wallet.di.XMLdi.ResponseHelper.WRBillerCategoryResponse;

public interface OnWRBillerCategoryResponse extends OnMessageInterface{
    void onWRCategory(List<GetWRBillerCategoryResponseC> responseList);
    void onSelectCategory(GetWRBillerCategoryResponseC category);
}
