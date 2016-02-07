package com.poject.dalithub.Utils;

public interface AppConstants {

    String baseUrl = "http://api.dalithub.com/";

    int LINKED_IN_LOGIN_REQUEST_CODE = 2;
    String LINKEDINDATA = "linkedInData";

    int BITE_POSTED = 101;

    int EVENT_CREATED = 102;

    int RESULT_USER_COMMENTED = 123;
    int LOCATION_DATA = 1111;

    /**
     * credentials for linkedin connectivity
     * public static final String CONSUMER_KEY = "755royi6jxpqbz"; // your KEY
     * public static final String CONSUMER_SECRET = "ZTTksDLxUA1NCMcS"; // your
     * // SECRET
     * public static final String OAUTH_CALLBACK_SCHEME = "x-oauthflow-linkedin";
     * public static final String OAUTH_CALLBACK_HOST = "litestcalback";
     * public static final String OAUTH_CALLBACK_URL = OAUTH_CALLBACK_SCHEME
     * + "://" + OAUTH_CALLBACK_HOST;
     **/

    public interface actions {
        int DO_LOGIN_MANUAL = 1;
        int DO_SIGNUP = 2;
        int DO_LOGIN_GOOGLE = 3;
        int DO_LOGIN_FB = 4;
        int DO_LOGIN_LINKEDIN = 5;
        int FORGOT_PASSWORD = 12;
        int VERIFY_ACCOUNT = 35;

        //actions for Bites Screen
        int GET_ALL_BITES = 6;
        int LIKE_POST = 7;
        int POST_COMMENT = 8;
        int POST_A_BITE = 9;
        int GET_ALL_COMMENTS = 10;
        int SET_BITE_STATUS = 11;
        int DEL_BITE_COMMENT = 23;

        //actions for Events Screem
        int CREATE_EVENT = 20;
        int GET_ALL_EVENTS = 21;
        int GET_EVENT_DETAIL = 22;
        int DEL_EVENT_COMMENT = 24;
        int GET_EVENT_COMMENT = 25;
        int ATTEND_EVENT = 28;

        //actions for Profile screen
        int GET_PERSONAL_INFO = 26;
        int UPDATE_PERSONAL_INFO = 27;

        //actions for Members screen
        int GET_ALL_MEMBERS = 29;
        int GET_MEMBER_DETAIL = 30;
        int INVITE_MEMBER = 31;

        // actions for settings screen
        int SEND_FEEDBACK = 32;
        int UPDATE_PRIVACY = 33;
        int CHANGE_PASSWORD = 34;
        int GET_USER_LIES = 35;
    }
}
