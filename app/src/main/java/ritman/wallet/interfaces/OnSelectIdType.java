package ritman.wallet.interfaces;

import ritman.wallet.di.XMLdi.ResponseHelper.GetIdTypeResponse;

import java.util.List;

public interface OnSelectIdType extends OnMessageInterface{
    void onSelectIdType(GetIdTypeResponse response);
    void onGetIdTypes(List<GetIdTypeResponse> responses);
}
