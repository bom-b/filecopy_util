package utils;

import settings.Constants;
import settings.FinalResult;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;

public class ValidationHelper {

    public void startValidation(List<Path> existingSourceFiles, List<Path> finalTargetPaths) {

        for (int i = 0; i < existingSourceFiles.size(); i++) {
            // 소스파일의 경로
            Path sourcePath = existingSourceFiles.get(i);
            Path targetPath = finalTargetPaths.get(i);

            // 파일이 동일한지 확인
            try {
                long size1 = Files.size(sourcePath);
                long size2 = Files.size(targetPath);
                long time1 = Files.getLastModifiedTime(sourcePath).toMillis();
                long time2 = Files.getLastModifiedTime(targetPath).toMillis();

                if (size1 == size2 && time1 == time2) {
                    System.out.println("[status] 반영된 파일이 소스 파일과 동일 합니다. : "  + targetPath);
                    FinalResult.copySuccessCnt++;
                } else {
                    System.err.println("[status] 반영된 파일이 소스 파일과 동일하지 않습니다. : "  + targetPath);
                    FinalResult.copyFailCnt++;
                }

            } catch (IOException e) {
                System.err.println("[status] 파일이 반영되지 않았습니다. : "  + targetPath);
                FinalResult.copyFailCnt++;
            }
        }
    }
}
