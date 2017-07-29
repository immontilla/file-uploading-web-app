package eu.immontilla.poc.secureupload.helper;

/**
 * Constants helper class
 * 
 * @author immontilla
 */
public class Constants {

    public final static String CSV_EXTENSION = "csv";
    public final static String DOT_CSV_EXTENSION = "." + CSV_EXTENSION;
    public final static int MAX_FILENAME_CHARS = 32;
    public final static String UNIX = "UNIX";
    public final static String WINDOWS = "WINDOWS";
    public static final String INVALID_EXTENSION = "Invalid file extension ";
    public static final String ACCEPTED_FILE_EXTENSION = "validFileExtension";
    public static final String ACCEPTED_FILE_SIZE = "validFileSize";
    public static final String VIRUS_FREE = "virusFree";
    public static final String VALID_CONTENT_TYPE = "validContentType";
    public static final String MSG_SAVE_ERROR = " was not saved at ";
    public static final String FAILED = "failed";
    public static final String CSV_CONTENT_TYPE = "text/csv";
    public static final String MSG_SCAN_RESULT = "scanResult: ";
    public static final String MSG_REAL_TYPE = "type: ";
    public static final String MSG_DELETED = " deleted? ";
    public static final String MSG_SAVED_AT = "Saved at ";

    private Constants() {
        super();
    }

}
