package ritman.wallet.login_signup_module.viewmodels;

import androidx.lifecycle.ViewModel;

import ritman.wallet.di.XMLdi.RequestHelper.ForgotPinSetNewRequest;
import ritman.wallet.login_signup_module.helper.RegisterUserRequest;

public class LoginRegisterViewModel extends ViewModel {
   public RegisterUserRequest registerUserRequest = new RegisterUserRequest();
   public ForgotPinSetNewRequest forgotPinSetNewRequest = new ForgotPinSetNewRequest();
}
