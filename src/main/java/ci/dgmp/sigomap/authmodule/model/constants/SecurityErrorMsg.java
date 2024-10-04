package ci.dgmp.sigomap.authmodule.model.constants;

public class SecurityErrorMsg
{
    public static final String INCORRECT_OLD_PASSWORD_ERROR_MSG = "Vous avez fourni un ancien mot de passe incorrect";
    public static final String DIFFERENT_PASSWORD_AND_CONFIRM_PASSWORD_ERROR_MSG = "Mot de passe de confirmation différent du mot de passe";
    public static final String SHORT_PASSWORD_ERROR_MSG = "Le mot de passe doit contenir au moins 4 caractères";
    public static final String USERNAME_NOT_FOUND_ERROR_MSG = "Login incorrect";
    public static final String INCORRECT_USERNAME_OR_PASSWORD_ERROR_MSG = "Login ou mot de passe incorrect";
    public static final String NONE_ACTIVATED_ACCOUNT_ERROR_MSG = "Compte bloqué ou non activé";
    public static final String ACCESS_DENIED_ERROR_MSG = "Vous n'êtes pas autorisé(e)s à faire cette action";
    public static final String TOKEN_BAD_PREFIX_ERROR_MSG = "The token does not have the required prefix";

    public static final String INVALID_ACTIVATION_TOKEN_ERROR_MSG = "Lien d'activation invalide";
    public static final String CORRUPTED_ACTIVATION_TOKEN_ERROR_MSG = "Requête falsifiée";
    public static final String EXPIRED_ACCESS_TOKEN = "Expired access token";
    public static final String EXPIRED_ACTIVATION_TOKEN_ERROR_MSG = "Lien d'activation expiré";
    public static final String EXPIRED_REINITIALISATION_TOKEN_ERROR_MSG = "Lien de réinitialisation expiré";
    public static final String ALREADY_USED_ACTIVATION_TOKEN_ERROR_MSG = "Lien d'activation déjà utilisé";
    public static final String CORRUPTED_LINK_ERROR_MSG = "Lien d'activation de compte corrompu";
    public static final String INVALID_USERNAME_OR_EMAIL_ERROR_MSG = "Login ou email incorrect";
    public static final String INVALID_USERNAME_ERROR_MSG = "Login invalide";
    public static final String LOCKED_ACCOUNT_ERROR_MSG = "Compte non activé";
    public static final String DISABLED_ACCOUNT_ERROR_MSG = "Compte non activé";
    public static final String ALREADY_ACTIVATED_ACCOUNT_ERROR_MSG = "Ce compte est déjà activé. L'utilisateur peut essayer de se connecter.";
    public static final String INVALID_FUNCTION_ID_ERROR_MSG = "L'identifiant de la fonction est incorrect";
    public static final String USER_ID_NOT_FOUND_ERROR_MSG = "Identifiant de l'utilisateur non valid";
    public static final String NOT_CONCORDANT_USER_ID_AND_USERNAME_ERROR_MSG = "Nom d'utilisateur et identifiant non concordant";
    public static final String FUNCTION_NAME_NOT_PROVIDED_ERROR_MSG = "Le nom de la fonction ne peut être nul";
    public static final String FUNCTION_NAME_ALREADY_EXISTS_ERROR_MSG = "Ce nom de fonction existe déjà";
    public static final String FUNCTION_CODE_NOT_PROVIDED_ERROR_MSG = "Le code la fonction ne peut être nul";
    public static final String FUNCTION_CODE_ALREADY_EXISTS_ERROR_MSG = "Ce code de fonction existe déjà";
    public static final String INVALID_STRUCTURE_ID_ERROR_MSG = "Identifiant de la structure non valide";
    public static final String ROLE_NAME_NOT_PROVIDED_ERROR_MSG = "Nom de role non fourni";
    public static final String ROLE_NAME_ALREADY_EXISTS_ERROR_MSG = "Nom de role existant";
    public static final String ROLE_CODE_NOT_PROVIDED_ERROR_MSG="Code de role non fourni";
    public static final String ROLE_CODE_ALREADY_EXISTS_ERROR_MSG = "Code de role existant";
    public static final String ENDING_DATE_LESS_THAN_STARTING_DATE_ERROR_MSG = "La date de fin ne peut être inférieure à la date de début";
    public static final String ROLE_ID_NOT_FOUND_ERROR_MSG = "L'identifiant du rôle est invalide";
    public static final String PRIVILEGE_ID_NOT_FOUND_ERROR_MSG = "L'identifiant du privilege est invalide";
}
