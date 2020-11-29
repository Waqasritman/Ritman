package totipay.wallet.interfaces;

import totipay.wallet.di.ResponseHelper.GetIdTypeResponse;

import java.util.List;

public interface OnSelectIdType extends OnMessageInterface{
    void onSelectIdType(GetIdTypeResponse response);
    void onGetIdTypes(List<GetIdTypeResponse> responses);
}
