package com.spently.config;

public class Constant {
    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_USER = "USER";

    public static final String TYPE_ACCESS = "access";
    public static final String TYPE_REFRESH = "refresh";

    public static final String ERROR_AUTH_UNAUTHORIZED = "err.auth.unauthorized";
    public static final String ERROR_AUTH_INVALID_CREDENTIALS = "err.auth.invalid-credentials";
    public static final String ERROR_AUTH_INVALID_REFRESH_TOKEN = "err.auth.invalid-refresh-token";
    public static final String ERROR_ACCOUNT_LOCKED = "err.account-locked";

    // User
    public static final String ERROR_REQUIRED_NAME = "err.required.name";
    public static final String ERROR_USER_NOT_FOUND = "err.user.notfound";
    public static final String ERROR_POST_NOT_FOUND = "err.post.notfound";
    public static final String ERROR_MENU_NOT_FOUND = "err.menu.notfound";
    public static final String ERROR_USER_USERNAME_EXITED = "err.existing.username";
    public static final String ERROR_POST_DATE_AVAILABLE = "err.post.date_not_available";

    public static final String ERROR_REQUIRED_EMAIL = "err.required.email";
    public static final String ERROR_EXITED_EMAIL = "err.existing.email";
    public static final String ERROR_INVALID_EMAIL = "err.invalid.email";
    public static final String ERROR_EXISTED_PHONE = "err.existing.phone";

    public static final String ERROR_REQUIRED_PASSWORD = "err.required.password";
    public static final String ERROR_MIN_PASSWORD = "err.min.password";
    public static final String ERROR_MATCH_PASSWORD = "err.match.password";
    public static final String ERROR_REQUIRED_RE_PASSWORD = "err.required.re_password";

    // COMMON ACTION
    public static final String SUCCESS_UPDATE_SUCCESS = "suc.update-success" ;
    public static final String SUCCESS_CREATE_SUCCESS = "suc.create-success";
    public static final String SUCCESS_GET_LIST_SUCCESS = "suc.get-list-success";
    public static final String SUCCESS_GET_DETAIL_SUCCESS = "suc.get-detail-success";

    public static final String SUCCESS_DELETE_SUCCESS = "suc.delete-success" ;
    public static final String ERROR_CREATE_FAILED = "err.create-failed";
    public static final String ERROR_DELETE_FAILED = "err.delete-failed";
    public static final String ERROR_EXCEL_EXPORT_FAILED = "err.excel-export-failed";
    public static final String ERROR_PDF_EXPORT_FAILED = "err.pdf-export-failed";
    public static final String ERROR_GET_LIST_FAILED = "err.get-list-failed";
    public static final String ERROR_GET_DETAIL_FAILED = "err.get-detail-failed";

    //mail
    public static final String SUCCESS_EMAIL_SENT = "suc.email.sent";
    public static final String ERROR_EMAIL_SEND = "err.email.sent";
    public static final String SUCCESS_PASSWORD_RESET_VERIFIED = "suc.password.reset.verified";
    public static final String ERROR_VERIFY_FAILED = "err.password.reset.failed";
    public static final String SUC_PASSWORD_UPDATED = "suc.password.updated";

    // Update
    public static final String ERROR_PASSWORD_NOT_MATCH = "err.password-not-match";
    public static final String ERROR_NEW_PASSWORD_SAME_AS_OLD = "err.new-password-same-as-old";
    public static final String ERROR_CONFIRM_PASSWORD_NOT_MATCH = "err.confirm-password-not-match";


    public static final String DEFAULT_MESSAGE = "default message";
}
