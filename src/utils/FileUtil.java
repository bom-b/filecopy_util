package utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileUtil {
    public static boolean areFilesEqual(Path path1, Path path2) {
        try {
            long size1 = Files.size(path1);
            long size2 = Files.size(path2);
            long time1 = Files.getLastModifiedTime(path1).toMillis();
            long time2 = Files.getLastModifiedTime(path2).toMillis();

            return size1 == size2 && time1 == time2;
        } catch (IOException e) {
            System.err.println("[error]\n검증 중 오류가 발생하였습니다. : " + e.getMessage());
            return false;
        }
    }
}
