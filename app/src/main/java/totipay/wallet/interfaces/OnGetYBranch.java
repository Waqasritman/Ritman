package totipay.wallet.interfaces;

import java.util.List;

import totipay.wallet.di.ResponseHelper.YBranchResponse;

public interface OnGetYBranch extends OnMessageInterface {
    void onGetYBranch(List<YBranchResponse> branchList);

    void onSelectBranch(YBranchResponse branch);
}
