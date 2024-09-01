package utils;

import settings.Constants;
import settings.Configurations;

import java.nio.file.Path;
import java.util.List;

public class PreviewHelper {

    private final Path rootDir;
    private final String targetPathSearchMode;
    public PreviewHelper() {
        this.rootDir = Constants.rootDir;
        this.targetPathSearchMode = Configurations.getTargetPathSearchMode();
    }

    public void showPreview(List<Path> existingSourceFiles) {
        System.out.println(Constants.GREEN + "소스파일이 최종적으로 반영될 경로를 확인해주세요." + Constants.RESET);

        if (targetPathSearchMode.equals("setRootPath")) {
            TargetPathReader targetPathReader = new TargetPathReader();
            // 타겟의 루트 경로
            Path targetRootPath = targetPathReader.readTargetRootPathSetting();

            for (Path path : existingSourceFiles) {
                // 파일이 복사될 최종 경로
                Path finalTargetPath = Common.mergePath(targetRootPath, path);
                System.out.println(finalTargetPath);
            }

        } else {
            TargetPathReader targetPathReader = new TargetPathReader();
            // 수동으로 지정한 타겟의 경로들
            List<Path> targetPath = targetPathReader.readTargetManualPathSetting();

            if(existingSourceFiles.size() == targetPath.size()) {
                for (int i = 0; i < existingSourceFiles.size(); i++) {
                    // 파일이 복사될 최종 경로
                    Path finalTargetPath = Common.mergePath(targetPath.get(i), existingSourceFiles.get(i).getFileName());
                    System.out.println(finalTargetPath);
                }
            } else {
                System.err.println("[error]\n소스파일의 개수와 TargetPath.txt의 경로개수가 일치하지 않습니다.");
                System.out.println("프로그램을 종료합니다.");
                System.exit(1);
            }

        }

        System.out.println();
    }
    
}
