package net.hockeyapp.javafx.simple;

/**
 * <h3>Description</h3>
 *
 * Various constants and meta information loaded from the context.
 **/
public class Constants {

    /**
     * HockeyApp API URL.
     */
    public static final String BASE_URL = "https://sdk.hockeyapp.net/";
    /**
     * Name of this SDK.
     */
    public static final String SDK_NAME = "HockeySDK";

    public static final String FILES_DIRECTORY_NAME = "HockeyApp";


    /**
     * Permissions request for the update task.
     */
    public static final int UPDATE_PERMISSIONS_REQUEST = 1;
    public static final String BUNDLE_BUILD_NUMBER = "buildNumber";
    /**
     * Path where crash logs and temporary files are stored.
     */
    public static String APP_VERSION = "1";
    /**
     * The app's version name.
     */
    public static String APP_PACKAGE = "de.tum.in.www1";

    /**
     * Helper method to convert a byte array to the hex string.
     * Based on http://stackoverflow.com/questions/9655181/convert-from-byte-array-to-hex-string-in-java
     *
     * @param bytes a byte array
     */
    public static String bytesToHex(byte[] bytes) {
        final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
        char[] hex = new char[bytes.length * 2];
        for (int index = 0; index < bytes.length; index++) {
            int value = bytes[index] & 0xFF;
            hex[index * 2] = HEX_ARRAY[value >>> 4];
            hex[index * 2 + 1] = HEX_ARRAY[value & 0x0F];
        }
        String result = new String(hex);
        return result.replaceAll("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5");
    }
}
