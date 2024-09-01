package utils;

import settings.Constants;
import settings.Configurations;
import vo.ExistingSourceFiles;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class SourcePathReader {

    private final Path rootDir;
    private final String sourceFileSearchMode;

    public SourcePathReader() {
        this.rootDir = Constants.rootDir;
        this.sourceFileSearchMode = Configurations.getSourceFileSearchMode();
    }

    public ExistingSourceFiles getExistingFileList() {

        ExistingSourceFiles fileList = new ExistingSourceFiles();

        if (sourceFileSearchMode.equals("autoSearch")) { // autoSearch일 경우 SourcePath.txt읽지 않고 자동으로 탐색
            Path rootPath = Common.mergePath(this.rootDir, "/SourceFile/");
            List<Path> paths = getAllFiles(rootPath);
            for (Path path: paths) {
                fileList.addSettingPath(rootPath.relativize(path));
                fileList.addFullPath(path);
            }

        } else { // SourcePath.txt를 통해 수동 탐색
            // 파일 읽기
            try (BufferedReader br = new BufferedReader(new FileReader(this.rootDir + "/SourcePath.txt"))) {
                String line;
                while ((line = br.readLine()) != null) {
                    // 공백 제거
                    line = line.trim();

                    if(!line.startsWith("#") && !line.isEmpty()) { //#은 주석
                        // 각 경로에 대해 존재 여부 확인
                        Path fullPath = Common.mergePath(Common.mergePath(this.rootDir, "/SourceFile/"), line);

                        if (Files.exists(fullPath)) { // 파일 존재 여부 확인
                            fileList.addSettingPath(Paths.get(line)); // 존재하면 리스트에 추가
                            fileList.addFullPath(fullPath);
                        } else {
                            fileList.incrementNotFoundCount(); // 찾지 못한 경우 카운트 증가
                        }
                    }

                }

            } catch (IOException e) {
                System.err.println("[error]\n파일을 찾을 수 없습니다: " + this.rootDir + "/SourcePath.txt");
                System.out.println("프로그램을 종료합니다.");
                System.exit(1);
            }
        }

        return fileList;
    }

    public List<Path> readSourcePathSetting() {
        List<Path> sourcePathList = new ArrayList<>();

        // 파일 읽기
        try (BufferedReader br = new BufferedReader(new FileReader(this.rootDir + "/SourcePath.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                // 공백 제거
                line = line.trim();

                if(!line.startsWith("#") && !line.isEmpty()) { //#은 주석
                    sourcePathList.add(Paths.get(line));
                }
            }

        } catch (IOException e) {
            System.err.println("[error]\n파일을 찾을 수 없습니다: " + this.rootDir + "/SourcePath.txt");
            System.out.println("프로그램을 종료합니다.");
            System.exit(1);
        }

        return sourcePathList;
    }

    private List<Path> getAllFiles(Path rootPath) {

        List<Path> filePaths = new ArrayList<>();

        try(DirectoryStream<Path> stream = Files.newDirectoryStream(rootPath)) {
            for (Path entry : stream) {
                if (Files.isDirectory(entry)) {
                    // 디렉토리라면 재귀호출
                    filePaths.addAll(getAllFiles(entry));
                } else {
                    filePaths.add(entry);
                }
            }
        } catch (IOException e) {
            System.err.println("[error]\n소스파일을 탐색하는 중 오류가 발생하였습니다.: " + e.getMessage());
            System.out.println("프로그램을 종료합니다.");
            System.exit(1);
        }

        return filePaths;
    }

}
