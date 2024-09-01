package settings;

public class Configurations {

    private static String sourceFileSearchMode; // "autoSearch", "manualSearch"
    private static String targetPathSearchMode; // "setRootPath", "manualPath"
    private static String backupMode; // "dateSuffix", "backupToFolder", "noBackup"

    public Configurations() {
    }

    public static String getSourceFileSearchMode() {
        return sourceFileSearchMode;
    }

    public void setSourceFileSearchMode(String sourceFileSearchMode) {
        Configurations.sourceFileSearchMode = sourceFileSearchMode;
    }

    public static String getTargetPathSearchMode() {
        return targetPathSearchMode;
    }

    public void setTargetPathSearchMode(String targetPathSearchMode) {
        Configurations.targetPathSearchMode = targetPathSearchMode;
    }

    public static String getBackupMode() {
        return backupMode;
    }

    public void setBackupMode(String backupMode) {
        Configurations.backupMode = backupMode;
    }
}
