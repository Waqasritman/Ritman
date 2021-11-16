package angoothape.wallet.login_signup_module.viewmodels;

import androidx.lifecycle.ViewModel;

import angoothape.wallet.di.XMLdi.RequestHelper.ForgotPinSetNewRequest;
import angoothape.wallet.login_signup_module.helper.RegisterUserRequest;

public class LoginRegisterViewModel extends ViewModel {
   public RegisterUserRequest registerUserRequest = new RegisterUserRequest();
   public ForgotPinSetNewRequest forgotPinSetNewRequest = new ForgotPinSetNewRequest();
}
