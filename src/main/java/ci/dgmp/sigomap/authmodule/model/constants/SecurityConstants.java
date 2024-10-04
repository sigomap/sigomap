package ci.dgmp.sigomap.authmodule.model.constants;

public class SecurityConstants
{
    public static final String AUTH_HEADER = "Authorization";
    public static final String SEC_KEY = "244226452948404D6251655468576D5A7134743777217A25432A462D4A614E645266556A586E3272357538782F413F4428472B4B6250655368566B5970337336";
    //public static final long ACCESS_TOKEN_DURATION = 1000*60*30; // 30 min
    public static final long ACCESS_TOKEN_DURATION = 1000l * 60l * 60l * 24l * 30l; // 30 jours
    public static final long REFRESH_TOKEN_DURATION = 1000l * 60l * 60l * 24l * 30l * 12l; // 1 an
    //public static final long REFRESH_TOKEN_DURATION = 1000l * 30 ; // 30 s

    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String ACCESS_TOKEN_KEY = "accessToken";
    public static final String REFRESH_TOKEN_KEY = "refreshToken";

    public static final String ERROR_MSG_KEY = "error-msg";
    public static final String CONFIRMATION_LINK = "/users/account-confirmation?confirmationToken=";
    public static final String ACTIVATION_LINK = "/users/account-activation?activationToken=";
    public static final String REINITIALISE_PASSWORD_LINK = "/password-reinitialisation?pwdReinitToken=";
    public static final long PASSWORD_DURATION = 60; //60 jours

    public static String ACCOUNT_ACTIVATION_REQUEST_OBJECT = "Activation de votre compte";
    public static String PASSWORD_REINITIALISATION_REQUEST_OBJECT = "Réinitialisation de votre mot de passe";
}
