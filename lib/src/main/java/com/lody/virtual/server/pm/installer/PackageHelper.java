package com.lody.virtual.server.pm.installer;
import android.annotation.TargetApi;
import android.content.pm.PackageInstaller;
import android.os.Build;
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class PackageHelper {
    public static final int INSTALL_SUCCEEDED = 1;
    public static final int INSTALL_FAILED_ALREADY_EXISTS = -1;
    public static final int INSTALL_FAILED_INVALID_APK = -2;
    public static final int INSTALL_FAILED_INVALID_URI = -3;
    public static final int INSTALL_FAILED_INSUFFICIENT_STORAGE = -4;
    public static final int INSTALL_FAILED_DUPLICATE_PACKAGE = -5;
    public static final int INSTALL_FAILED_NO_SHARED_USER = -6;
    public static final int INSTALL_FAILED_UPDATE_INCOMPATIBLE = -7;
    public static final int INSTALL_FAILED_SHARED_USER_INCOMPATIBLE = -8;
    public static final int INSTALL_FAILED_MISSING_SHARED_LIBRARY = -9;
    public static final int INSTALL_FAILED_REPLACE_COULDNT_DELETE = -10;
    public static final int INSTALL_FAILED_DEXOPT = -11;
    public static final int INSTALL_FAILED_OLDER_SDK = -12;
    public static final int INSTALL_FAILED_CONFLICTING_PROVIDER = -13;
    public static final int INSTALL_FAILED_NEWER_SDK = -14;
    public static final int INSTALL_FAILED_TEST_ONLY = -15;
    public static final int INSTALL_FAILED_CPU_ABI_INCOMPATIBLE = -16;
    public static final int INSTALL_FAILED_MISSING_FEATURE = -17;
    public static final int INSTALL_FAILED_CONTAINER_ERROR = -18;
    public static final int INSTALL_FAILED_INVALID_INSTALL_LOCATION = -19;
    public static final int INSTALL_FAILED_MEDIA_UNAVAILABLE = -20;
    public static final int INSTALL_FAILED_VERIFICATION_TIMEOUT = -21;
    public static final int INSTALL_FAILED_VERIFICATION_FAILURE = -22;
    public static final int INSTALL_FAILED_PACKAGE_CHANGED = -23;
    public static final int INSTALL_FAILED_UID_CHANGED = -24;
    public static final int INSTALL_FAILED_VERSION_DOWNGRADE = -25;
    public static final int INSTALL_FAILED_PERMISSION_MODEL_DOWNGRADE = -26;
    public static final int INSTALL_PARSE_FAILED_NOT_APK = -100;
    public static final int INSTALL_PARSE_FAILED_BAD_MANIFEST = -101;
    public static final int INSTALL_PARSE_FAILED_UNEXPECTED_EXCEPTION = -102;
    public static final int INSTALL_PARSE_FAILED_NO_CERTIFICATES = -103;
    public static final int INSTALL_PARSE_FAILED_INCONSISTENT_CERTIFICATES = -104;
    public static final int INSTALL_PARSE_FAILED_CERTIFICATE_ENCODING = -105;
    public static final int INSTALL_PARSE_FAILED_BAD_PACKAGE_NAME = -106;
    public static final int INSTALL_PARSE_FAILED_BAD_SHARED_USER_ID = -107;
    public static final int INSTALL_PARSE_FAILED_MANIFEST_MALFORMED = -108;
    public static final int INSTALL_PARSE_FAILED_MANIFEST_EMPTY = -109;
    public static final int INSTALL_FAILED_INTERNAL_ERROR = -110;
    public static final int INSTALL_FAILED_USER_RESTRICTED = -111;
    public static final int INSTALL_FAILED_DUPLICATE_PERMISSION = -112;
    public static final int INSTALL_FAILED_NO_MATCHING_ABIS = -113;
    public static final int NO_NATIVE_LIBRARIES = -114;
    public static final int INSTALL_FAILED_ABORTED = -115;
    public static final int INSTALL_FAILED_EPHEMERAL_INVALID = -116;
    public static final int DELETE_SUCCEEDED = 1;
    public static final int DELETE_FAILED_INTERNAL_ERROR = -1;
    public static final int DELETE_FAILED_DEVICE_POLICY_MANAGER = -2;
    public static final int DELETE_FAILED_USER_RESTRICTED = -3;
    public static final int DELETE_FAILED_OWNER_BLOCKED = -4;
    public static final int DELETE_FAILED_ABORTED = -5;
    public static final int MOVE_SUCCEEDED = -100;
    public static final int MOVE_FAILED_INSUFFICIENT_STORAGE = -1;
    public static final int MOVE_FAILED_DOESNT_EXIST = -2;
    public static final int MOVE_FAILED_SYSTEM_PACKAGE = -3;
    public static final int MOVE_FAILED_FORWARD_LOCKED = -4;
    public static final int MOVE_FAILED_INVALID_LOCATION = -5;
    public static final int MOVE_FAILED_INTERNAL_ERROR = -6;
    public static final int MOVE_FAILED_OPERATION_PENDING = -7;
    public static final int MOVE_FAILED_DEVICE_ADMIN = -8;
    public static String installStatusToString(int status, String msg) {
        final String str = installStatusToString(status);
        if (msg != null) {
            return str + ": " + msg;
        } else {
            return str;
        }
    }
    public static String installStatusToString(int status) {
        switch (status) {
            case INSTALL_SUCCEEDED: return "INSTALL_SUCCEEDED";
            case INSTALL_FAILED_ALREADY_EXISTS: return "INSTALL_FAILED_ALREADY_EXISTS";
            case INSTALL_FAILED_INVALID_APK: return "INSTALL_FAILED_INVALID_APK";
            case INSTALL_FAILED_INVALID_URI: return "INSTALL_FAILED_INVALID_URI";
            case INSTALL_FAILED_INSUFFICIENT_STORAGE: return "INSTALL_FAILED_INSUFFICIENT_STORAGE";
            case INSTALL_FAILED_DUPLICATE_PACKAGE: return "INSTALL_FAILED_DUPLICATE_PACKAGE";
            case INSTALL_FAILED_NO_SHARED_USER: return "INSTALL_FAILED_NO_SHARED_USER";
            case INSTALL_FAILED_UPDATE_INCOMPATIBLE: return "INSTALL_FAILED_UPDATE_INCOMPATIBLE";
            case INSTALL_FAILED_SHARED_USER_INCOMPATIBLE: return "INSTALL_FAILED_SHARED_USER_INCOMPATIBLE";
            case INSTALL_FAILED_MISSING_SHARED_LIBRARY: return "INSTALL_FAILED_MISSING_SHARED_LIBRARY";
            case INSTALL_FAILED_REPLACE_COULDNT_DELETE: return "INSTALL_FAILED_REPLACE_COULDNT_DELETE";
            case INSTALL_FAILED_DEXOPT: return "INSTALL_FAILED_DEXOPT";
            case INSTALL_FAILED_OLDER_SDK: return "INSTALL_FAILED_OLDER_SDK";
            case INSTALL_FAILED_CONFLICTING_PROVIDER: return "INSTALL_FAILED_CONFLICTING_PROVIDER";
            case INSTALL_FAILED_NEWER_SDK: return "INSTALL_FAILED_NEWER_SDK";
            case INSTALL_FAILED_TEST_ONLY: return "INSTALL_FAILED_TEST_ONLY";
            case INSTALL_FAILED_CPU_ABI_INCOMPATIBLE: return "INSTALL_FAILED_CPU_ABI_INCOMPATIBLE";
            case INSTALL_FAILED_MISSING_FEATURE: return "INSTALL_FAILED_MISSING_FEATURE";
            case INSTALL_FAILED_CONTAINER_ERROR: return "INSTALL_FAILED_CONTAINER_ERROR";
            case INSTALL_FAILED_INVALID_INSTALL_LOCATION: return "INSTALL_FAILED_INVALID_INSTALL_LOCATION";
            case INSTALL_FAILED_MEDIA_UNAVAILABLE: return "INSTALL_FAILED_MEDIA_UNAVAILABLE";
            case INSTALL_FAILED_VERIFICATION_TIMEOUT: return "INSTALL_FAILED_VERIFICATION_TIMEOUT";
            case INSTALL_FAILED_VERIFICATION_FAILURE: return "INSTALL_FAILED_VERIFICATION_FAILURE";
            case INSTALL_FAILED_PACKAGE_CHANGED: return "INSTALL_FAILED_PACKAGE_CHANGED";
            case INSTALL_FAILED_UID_CHANGED: return "INSTALL_FAILED_UID_CHANGED";
            case INSTALL_FAILED_VERSION_DOWNGRADE: return "INSTALL_FAILED_VERSION_DOWNGRADE";
            case INSTALL_PARSE_FAILED_NOT_APK: return "INSTALL_PARSE_FAILED_NOT_APK";
            case INSTALL_PARSE_FAILED_BAD_MANIFEST: return "INSTALL_PARSE_FAILED_BAD_MANIFEST";
            case INSTALL_PARSE_FAILED_UNEXPECTED_EXCEPTION: return "INSTALL_PARSE_FAILED_UNEXPECTED_EXCEPTION";
            case INSTALL_PARSE_FAILED_NO_CERTIFICATES: return "INSTALL_PARSE_FAILED_NO_CERTIFICATES";
            case INSTALL_PARSE_FAILED_INCONSISTENT_CERTIFICATES: return "INSTALL_PARSE_FAILED_INCONSISTENT_CERTIFICATES";
            case INSTALL_PARSE_FAILED_CERTIFICATE_ENCODING: return "INSTALL_PARSE_FAILED_CERTIFICATE_ENCODING";
            case INSTALL_PARSE_FAILED_BAD_PACKAGE_NAME: return "INSTALL_PARSE_FAILED_BAD_PACKAGE_NAME";
            case INSTALL_PARSE_FAILED_BAD_SHARED_USER_ID: return "INSTALL_PARSE_FAILED_BAD_SHARED_USER_ID";
            case INSTALL_PARSE_FAILED_MANIFEST_MALFORMED: return "INSTALL_PARSE_FAILED_MANIFEST_MALFORMED";
            case INSTALL_PARSE_FAILED_MANIFEST_EMPTY: return "INSTALL_PARSE_FAILED_MANIFEST_EMPTY";
            case INSTALL_FAILED_INTERNAL_ERROR: return "INSTALL_FAILED_INTERNAL_ERROR";
            case INSTALL_FAILED_USER_RESTRICTED: return "INSTALL_FAILED_USER_RESTRICTED";
            case INSTALL_FAILED_DUPLICATE_PERMISSION: return "INSTALL_FAILED_DUPLICATE_PERMISSION";
            case INSTALL_FAILED_NO_MATCHING_ABIS: return "INSTALL_FAILED_NO_MATCHING_ABIS";
            case INSTALL_FAILED_ABORTED: return "INSTALL_FAILED_ABORTED";
            default: return Integer.toString(status);
        }
    }
    public static int installStatusToPublicStatus(int status) {
        switch (status) {
            case INSTALL_SUCCEEDED: return PackageInstaller.STATUS_SUCCESS;
            case INSTALL_FAILED_ALREADY_EXISTS: return PackageInstaller.STATUS_FAILURE_CONFLICT;
            case INSTALL_FAILED_INVALID_APK: return PackageInstaller.STATUS_FAILURE_INVALID;
            case INSTALL_FAILED_INVALID_URI: return PackageInstaller.STATUS_FAILURE_INVALID;
            case INSTALL_FAILED_INSUFFICIENT_STORAGE: return PackageInstaller.STATUS_FAILURE_STORAGE;
            case INSTALL_FAILED_DUPLICATE_PACKAGE: return PackageInstaller.STATUS_FAILURE_CONFLICT;
            case INSTALL_FAILED_NO_SHARED_USER: return PackageInstaller.STATUS_FAILURE_CONFLICT;
            case INSTALL_FAILED_UPDATE_INCOMPATIBLE: return PackageInstaller.STATUS_FAILURE_CONFLICT;
            case INSTALL_FAILED_SHARED_USER_INCOMPATIBLE: return PackageInstaller.STATUS_FAILURE_CONFLICT;
            case INSTALL_FAILED_MISSING_SHARED_LIBRARY: return PackageInstaller.STATUS_FAILURE_INCOMPATIBLE;
            case INSTALL_FAILED_REPLACE_COULDNT_DELETE: return PackageInstaller.STATUS_FAILURE_CONFLICT;
            case INSTALL_FAILED_DEXOPT: return PackageInstaller.STATUS_FAILURE_INVALID;
            case INSTALL_FAILED_OLDER_SDK: return PackageInstaller.STATUS_FAILURE_INCOMPATIBLE;
            case INSTALL_FAILED_CONFLICTING_PROVIDER: return PackageInstaller.STATUS_FAILURE_CONFLICT;
            case INSTALL_FAILED_NEWER_SDK: return PackageInstaller.STATUS_FAILURE_INCOMPATIBLE;
            case INSTALL_FAILED_TEST_ONLY: return PackageInstaller.STATUS_FAILURE_INVALID;
            case INSTALL_FAILED_CPU_ABI_INCOMPATIBLE: return PackageInstaller.STATUS_FAILURE_INCOMPATIBLE;
            case INSTALL_FAILED_MISSING_FEATURE: return PackageInstaller.STATUS_FAILURE_INCOMPATIBLE;
            case INSTALL_FAILED_CONTAINER_ERROR: return PackageInstaller.STATUS_FAILURE_STORAGE;
            case INSTALL_FAILED_INVALID_INSTALL_LOCATION: return PackageInstaller.STATUS_FAILURE_STORAGE;
            case INSTALL_FAILED_MEDIA_UNAVAILABLE: return PackageInstaller.STATUS_FAILURE_STORAGE;
            case INSTALL_FAILED_VERIFICATION_TIMEOUT: return PackageInstaller.STATUS_FAILURE_ABORTED;
            case INSTALL_FAILED_VERIFICATION_FAILURE: return PackageInstaller.STATUS_FAILURE_ABORTED;
            case INSTALL_FAILED_PACKAGE_CHANGED: return PackageInstaller.STATUS_FAILURE_INVALID;
            case INSTALL_FAILED_UID_CHANGED: return PackageInstaller.STATUS_FAILURE_INVALID;
            case INSTALL_FAILED_VERSION_DOWNGRADE: return PackageInstaller.STATUS_FAILURE_INVALID;
            case INSTALL_FAILED_PERMISSION_MODEL_DOWNGRADE: return PackageInstaller.STATUS_FAILURE_INVALID;
            case INSTALL_PARSE_FAILED_NOT_APK: return PackageInstaller.STATUS_FAILURE_INVALID;
            case INSTALL_PARSE_FAILED_BAD_MANIFEST: return PackageInstaller.STATUS_FAILURE_INVALID;
            case INSTALL_PARSE_FAILED_UNEXPECTED_EXCEPTION: return PackageInstaller.STATUS_FAILURE_INVALID;
            case INSTALL_PARSE_FAILED_NO_CERTIFICATES: return PackageInstaller.STATUS_FAILURE_INVALID;
            case INSTALL_PARSE_FAILED_INCONSISTENT_CERTIFICATES: return PackageInstaller.STATUS_FAILURE_INVALID;
            case INSTALL_PARSE_FAILED_CERTIFICATE_ENCODING: return PackageInstaller.STATUS_FAILURE_INVALID;
            case INSTALL_PARSE_FAILED_BAD_PACKAGE_NAME: return PackageInstaller.STATUS_FAILURE_INVALID;
            case INSTALL_PARSE_FAILED_BAD_SHARED_USER_ID: return PackageInstaller.STATUS_FAILURE_INVALID;
            case INSTALL_PARSE_FAILED_MANIFEST_MALFORMED: return PackageInstaller.STATUS_FAILURE_INVALID;
            case INSTALL_PARSE_FAILED_MANIFEST_EMPTY: return PackageInstaller.STATUS_FAILURE_INVALID;
            case INSTALL_FAILED_INTERNAL_ERROR: return PackageInstaller.STATUS_FAILURE;
            case INSTALL_FAILED_USER_RESTRICTED: return PackageInstaller.STATUS_FAILURE_INCOMPATIBLE;
            case INSTALL_FAILED_DUPLICATE_PERMISSION: return PackageInstaller.STATUS_FAILURE_CONFLICT;
            case INSTALL_FAILED_NO_MATCHING_ABIS: return PackageInstaller.STATUS_FAILURE_INCOMPATIBLE;
            case INSTALL_FAILED_ABORTED: return PackageInstaller.STATUS_FAILURE_ABORTED;
            default: return PackageInstaller.STATUS_FAILURE;
        }
    }
    public static String deleteStatusToString(boolean status) {
        return status ? "DELETE_SUCCEEDED" : "DELETE_FAILED";
    }
}
