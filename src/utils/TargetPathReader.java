package utils;

import settings.Constants;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class TargetPathReader {
    private final Path rootDir;
    public TargetPathReader() {
        this.rootDir = Constants.rootDir;
    }

    public Path readTargetRootPathSetting() {
        Path rootPath = null;

        // 파일 읽기
        try (BufferedReader br = new BufferedReader(new FileReader(this.rootDir + "/TargetPath.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                // 공백 제거
                line = line.trim();

                if(!line.startsWith("#") && !line.isEmpty()) { //#은 주석
                    rootPath = Paths.get(line);
                }
            }

        } catch (IOException e) {
            System.err.println("[error]\n파일을 찾을 수 없습니다: " + this.rootDir + "/TargetPath.txt");
            System.out.println("프로그램을 종료합니다.");
            System.exit(1);
        }

        if (rootPath == null) {
            System.err.println("[error]\n루트경로 설정을 찾을 수 없습니다. TargetPath.txt파일 설정을 확인해주세요.");
            System.out.println("프로그램을 종료합니다.");
            System.exit(1);
        }

        return rootPath;
    }

    public List<Path> readTargetManualPathSetting() {
        List<Path> manualPaths = new ArrayList<>();

        // 파일 읽기
        try (BufferedReader br = new BufferedReader(new FileReader(this.rootDir + "/TargetPath.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                // 공백 제거
                line = line.trim();

                if(!line.startsWith("#") && !line.isEmpty()) { //#은 주석
                    manualPaths.add(Paths.get(line));
                }
            }

        } catch (IOException e) {
            System.err.println("[error]\n파일을 찾을 수 없습니다: " + this.rootDir + "/SourcePath.txt");
            System.out.println("프로그램을 종료합니다.");
            System.exit(1);
        }

        return manualPaths;
    }
}
