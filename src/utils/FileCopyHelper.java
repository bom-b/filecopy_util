package utils;

import settings.Constants;
import settings.Configurations;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class FileCopyHelper {
    private final Path rootDir;
    private final String targetPathSearchMode;
    public FileCopyHelper() {
        this.rootDir = Constants.rootDir;
        this.targetPathSearchMode = Configurations.getTargetPathSearchMode();
    }

    public List<Path> startFileCopy(List<Path> existingSourceFiles) {
        System.out.println("[status] 소스파일 반영을 시작합니다. ");

        // 최종적으로 생성된 반영 경로들
        List<Path> finalTargetPaths = new ArrayList<>();

        if (targetPathSearchMode.equals("setRootPath")) {
            TargetPathReader targetPathReader = new TargetPathReader();
            // 타겟의 루트 경로
            Path targetRootPath = targetPathReader.readTargetRootPathSetting();

            for (Path path : existingSourceFiles) {
                // 소스파일의 경로
                Path fullSourcePath = Common.mergePath(this.rootDir, Common.mergePath("/SourceFile/", path.toString()));
                // 파일이 복사될 최종 경로
                Path finalTargetPath = Common.mergePath(targetRootPath, path);
                finalTargetPaths.add(finalTargetPath);
                try {
                    // 부모 디렉토리가 존재하지 않으면 생성
                    if (Files.notExists(finalTargetPath.getParent())) {
                        Files.createDirectories(finalTargetPath.getParent());
                    }
                    // 파일 복사(옵션: 덮어쓰기, 파일속성유지)
                    Files.copy(fullSourcePath, finalTargetPath, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);
                    System.out.println("[status] 소스파일 반영 : " + finalTargetPath);
                } catch (IOException e) {
                    System.err.println("[error]\n소스파일 반영 중 오류가 발생하였습니다. : " + e.getMessage());
                }
            }

        } else {
            TargetPathReader targetPathReader = new TargetPathReader();
            // 수동으로 지정한 타겟의 경로들
            List<Path> targetPath = targetPathReader.readTargetManualPathSetting();

            for (int i = 0; i < existingSourceFiles.size(); i++) {
                // 소스파일의 경로
                Path fullSourcePath = Common.mergePath(this.rootDir, Common.mergePath("/SourceFile/", existingSourceFiles.get(i).toString()));
                // 파일이 복사될 최종 경로
                Path finalTargetPath = Common.mergePath(targetPath.get(i), existingSourceFiles.get(i).getFileName());
                finalTargetPaths.add(finalTargetPath);
                try {
                    // 부모 디렉토리가 존재하지 않으면 생성
                    if (Files.notExists(finalTargetPath.getParent())) {
                        Files.createDirectories(finalTargetPath.getParent());
                    }
                    // 파일 복사(옵션: 덮어쓰기, 파일속성유지)
                    Files.copy(fullSourcePath, finalTargetPath, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);
                    System.out.println("[status] 소스파일 반영 : " + finalTargetPath);
                } catch (IOException e) {
                    System.err.println("[error]\n소스파일 반영 중 오류가 발생하였습니다. : " + e.getMessage());
                }
            }
        }
        System.out.println("[status] 소스파일 반영 성공. ");
        return finalTargetPaths;
    }


}
