package com.david.auth_layer_architecture.common.utils.constants;


public class CommonConstants {

    public static final String PUBLIC_URL = "/api/v1";
    public static final String SECURE_URL = PUBLIC_URL + "/app";
    public static final String ROLE_USER = "USER";

    public static final String SIGN_IN_FRONT_URL = "/auth/sign-in";
    public static final String AUTH_SOCIAL_MEDIA_FRONT_URL = "http://localhost:4200/auth/social-media";


    // Expiration token in minutes and days
    public static final Integer EXPIRATION_TOKEN_TO_ACCESS_APP = 60;
    public static final Integer EXPIRATION_TOKEN_TO_VERIFY_ACCOUNT = 60;
    public static final Integer EXPIRATION_TOKEN_TO_CHANGE_PASSWORD = 10;
    public static final Integer EXPIRATION_REFRESH_TOKEN_TO_ACCESS_APP = 7;
    public static final Integer EXPIRATION_REFRESH_TOKEN_TO_VERIFY_ACCOUNT = 10;
    public static final Integer EXPIRATION_ERROR_TOKEN = 10;

    // Type token
    public static final String TYPE_ACCESS_TOKEN_TO_ACCESS_APP = "access_app";
    public static final String TYPE_ACCESS_TOKEN_TO_CHANGE_PASSWORD = "change_password";
    public static final String TYPE_REFRESH_TOKEN_TO_ACCESS_APP = "refresh_token_to_access_app";
    public static final String TYPE_ERROR_TOKEN= "error";
    public static final String TYPE_ACCESS_TOKEN_TO_VERIFY_ACCOUNT = "verify_account";
    public static final String TYPE_REFRESH_TOKEN_TO_VERIFY_ACCOUNT = "refresh_token_to_verify_account";

}
