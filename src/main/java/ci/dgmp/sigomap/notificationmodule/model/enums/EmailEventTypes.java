package ci.dgmp.sigomap.notificationmodule.model.enums;

public enum EmailEventTypes
{
    ACTIVATE_ACCOUNT_REQUEST("ACTIVATE_ACCOUNT_REQUEST"),
    REINITIALISE_PASSWORD_REQUEST("REINITIALISE_PASSWORD_REQUEST"),
    EMAIL_SENT("EMAIL_SENT");
    String name;
    EmailEventTypes(){}
    EmailEventTypes(String name){this.name = name;}
}
