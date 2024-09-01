package utils;

import settings.Constants;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class TargetFileScanner {

    private final Path rootDir;
    public TargetFileScanner() {
        this.rootDir = Constants.rootDir;
    }

    public List<Path> getExistingTargetFilesByRootPath() {
        List<Path> existingTargetFiles = new ArrayList<>();

        TargetPathReader targetPathReader = new TargetPathReader();
        Path rootPath = targetPathReader.readTargetRootPathSetting();

        SourcePathReader sourcePathReader = new SourcePathReader();
        List<Path> sourcePathList = sourcePathReader.getExistingFileList().getSettingPaths();

        for (Path sourcePath : sourcePathList) {
            Path target = Common.mergePath(rootPath, sourcePath);
            if (Files.exists(target)) { // 파일 존재 여부 확인
                existingTargetFiles.add(target); // 존재하면 리스트에 추가
            }
        }

        return existingTargetFiles;
    }

    public List<Path> getExistingTargetFilesByManualPath() {
        List<Path> existingTargetFiles = new ArrayList<>();

        TargetPathReader targetPathReader = new TargetPathReader();
        List<Path> manualTargetPaths = targetPathReader.readTargetManualPathSetting();

        SourcePathReader sourcePathReader = new SourcePathReader();
        List<Path> sourcePathList = sourcePathReader.getExistingFileList().getSettingPaths();

        if (manualTargetPaths.size() != sourcePathList.size()) {
            System.err.println("[error]\n 'target_path_search_mode'를 'manualPath'로 설정하였으나 존재하는 소스파일 개수와 TargetPath.txt의 경로 개수가 일치하지 않습니다.");
            System.out.println("프로그램을 종료합니다.");
            System.exit(1);
        }

        for (int i = 0; i < sourcePathList.size(); i++) {
            Path sourcePath = sourcePathList.get(i); // 소스 파일 경로
            Path sourcefileName = sourcePath.getFileName(); // 소스 파일 이름
            // 타겟 경로 + 소스파일의 이름을 조합하여 경로 생성
            Path fullPathToTargetFile = Common.mergePath(manualTargetPaths.get(i), sourcefileName);
            if (Files.exists(fullPathToTargetFile)) { // 파일 존재 여부 확인
                existingTargetFiles.add(fullPathToTargetFile); // 존재하면 리스트에 추가
            }
        }

        return existingTargetFiles;
    }
}
