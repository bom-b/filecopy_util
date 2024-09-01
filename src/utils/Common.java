package utils;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Pattern;

public class Common {
    public static Path mergePath(String path1, String path2) {

        if (path1.equals(".")) {
            return Paths.get("." + path2);
        }

        // 각 경로를 Path 객체로 변환
        Path p1 = Paths.get(path1);
        Path p2 = Paths.get(path2);

        // 두 경로를 합쳐서 새로운 Path 객체 생성
        return p1.resolve(p2.subpath(0, p2.getNameCount()));
    }

    public static Path mergePath(Path path1, Path path2) {
        if (path1.toString().equals(".")) {
            return Paths.get("." + path2);
        }

        return path1.resolve(path2.subpath(0, path2.getNameCount()));
    }

    public static Path mergePath(Path path1, String path2) {
        if (path1.toString().equals(".")) {
            return Paths.get("." + path2);
        }

        Path p2 = Paths.get(path2);

        return path1.resolve(p2.subpath(0, p2.getNameCount()));
    }

    public static Path mergePath(String path1, Path path2) {
        if (path1.equals(".")) {
            return Paths.get("." + path2);
        }

        Path p1 = Paths.get(path1);

        return p1.resolve(path2.subpath(0, path2.getNameCount()));
    }

    public static Path findCommonPath(List<Path> paths) {
        if (paths.isEmpty()) {
            return null;
        }

        String commonPath = paths.get(0).toString();

        for(Path path : paths) {
            commonPath = getCommonPrefix(Paths.get(commonPath), path);
            if (commonPath.isEmpty()) {
                break;
            }
        }

        return Paths.get(commonPath);
    }

    public static String getCommonPrefix(Path path1, Path path2) {
        String[] parts1 = path1.toString().split(Pattern.quote(File.separator));
        String[] parts2 = path2.toString().split(Pattern.quote(File.separator));
        StringBuilder commonPrefix = new StringBuilder();

        int minLength = Math.min(parts1.length, parts2.length);
        for (int i = 0; i < minLength; i++) {
            if (parts1[i].equals(parts2[i])) {
                commonPrefix.append(parts1[i]).append(File.separator);
            } else {
                break;
            }
        }

        // 마지막에 추가된 File.separator 제거
        if (commonPrefix.length() > 0) {
            commonPrefix.setLength(commonPrefix.length() - File.separator.length());
        }

        return commonPrefix.toString();
    }
}
