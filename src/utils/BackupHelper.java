package utils;

import settings.Constants;
import settings.FinalResult;
import settings.Configurations;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.regex.Pattern;

public class BackupHelper {
    private final Path rootDir;
    private final String backupMode;
    private final String targetPathSearchMode;
    public BackupHelper() {
        this.rootDir = Constants.rootDir;
        this.backupMode = Configurations.getBackupMode();
        this.targetPathSearchMode = Configurations.getTargetPathSearchMode();
    }

    public void startBackup() {

        if (backupMode.equals("noBackup")) {
            System.out.println("[status] 설정에 따라 백업을 생략합니다.");

        } else {
            System.out.println("[status] 기존 파일 백업을 시작합니다.");
            TargetFileScanner scanner = new TargetFileScanner();
            List<Path> existingTargetFiles;

            // target path 설정에 따라 백업할 파일 목록 선정
            if(targetPathSearchMode.equals("setRootPath")) {
                existingTargetFiles = scanner.getExistingTargetFilesByRootPath();
            } else {
                existingTargetFiles = scanner.getExistingTargetFilesByManualPath();
            }

            if (existingTargetFiles.isEmpty()) {
                System.out.println("[status] 백업할 기존 파일이 없습니다.");
                
            } else {
                if (backupMode.equals("dateSuffix1")) {
                    addSuffixToFileName(existingTargetFiles);

                } else if (backupMode.equals("dateSuffix2")) {
                    removeOldBackupFiles(existingTargetFiles);
                    addSuffixToFileName(existingTargetFiles);

                } else if (backupMode.equals("backupToFolder")) {
                    copyOriginalFile(existingTargetFiles);
                }
                System.out.println("[status] 기존 파일 백업 성공.");
            }
        }
    }

    private void addSuffixToFileName(List<Path> existingTargetFiles) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        String today = formatter.format(new Date());

        for (Path file : existingTargetFiles) {
            String fileName = file.getFileName().toString(); // 파일 이름
            String filePath = file.getParent().toString(); // 파일 경로

            // 새로운 파일 이름 생성 (접미사 추가)
            String newFileName = fileName.replaceFirst("(\\.[^\\.]+)$", "_" + today + "$1"); // 접미사 추가
            Path newFilePath = Paths.get(filePath, newFileName); // 새로운 파일 경로

            // 파일 이름 변경
            try {
                Files.move(file, newFilePath, StandardCopyOption.REPLACE_EXISTING); // 파일 이름 변경
                System.out.println("[status] 기존 파일 이름 변경: " + newFilePath);
                FinalResult.backupCnt++;
            } catch (IOException e) {
                System.err.println("[error]\n백업 중 오류가 발생하였습니다. 파일 이름 변경 실패: " + e.getMessage());
                System.out.println("프로그램을 종료합니다.");
                System.exit(1);
            }
        }
    }

    private void removeOldBackupFiles(List<Path> existingTargetFiles) {
        System.out.println("[status] _yyyyMMdd형식의 예전 백업파일을 모두 삭제합니다.");

        // _yyyyMMdd.확장자 형식의 기존 백업파일을 찾는 정규표현식
        Pattern pattern = Pattern.compile("_(\\d{8})\\.(\\w+)$");
        
        // 삭제된 파일 개수
        int deletedCnt = 0;
        
        for (Path targetFilePath : existingTargetFiles) {
            File file = new File(targetFilePath.toString());
            String parentDir = file.getParent(); // 파일의 경로
            String fileName = file.getName(); // 파일 이름
            String baseName = fileName.substring(0, fileName.lastIndexOf('.')); // 확장자를 뺀 파일의 이름
            String extension = fileName.substring(fileName.lastIndexOf('.') + 1); // 원본 파일의 확장자

            // 파일이 있는 디렉토리에서 모든 파일을 확인
            File[] filesInDir = new File(parentDir).listFiles();
            if(filesInDir != null) {
                for (File f : filesInDir) {
                    // 정규 표현식으로 파일 이름 검사
                    if (f.getName().startsWith(baseName) && pattern.matcher(f.getName()).find()) {
                        boolean deleted = f.delete();
                        if (deleted) {
                            deletedCnt++;
                            System.out.println("[status] 예전 백업파일 삭제 : " + f.getAbsolutePath());
                        } else {
                            System.err.println("[status] 예전 백업파일 삭제 실패 : " + f.getAbsolutePath());
                        }
                    }
                }
            }
        }
        if (deletedCnt > 0) {
            System.out.println("[status] 예전 백업파일 삭제 성공.");
        } else {
            System.out.println("[status] 삭제할 예전 백업파일이 없습니다.");
        }
        
    }

    private void copyOriginalFile(List<Path> existingTargetFiles) {

        Path backupFolder = Common.mergePath(rootDir, Paths.get("/BackupFiles"));

        // 백업 폴더가 존재하지 않으면 생성
        if (Files.notExists(backupFolder)) {
            try {
                Files.createDirectories(backupFolder);
            } catch (IOException e) {
                System.err.println("[error]\n백업 폴더 생성 중 오류가 발생하였습니다. : " + e.getMessage());
                System.out.println("프로그램을 종료합니다.");
                System.exit(1);
            }
        }

        Path commonPath;
        
        if (targetPathSearchMode.equals("setRootPath")) {
            // rootPath 설정을 통해 rootPath 가져오기
            TargetPathReader targetPathReader = new TargetPathReader();
            commonPath = targetPathReader.readTargetRootPathSetting();
            
        } else {
            // 수동으로 공통경로 찾기
            commonPath = Common.findCommonPath(existingTargetFiles);
        }
        
        for (Path file : existingTargetFiles) {
            Path relativePath = commonPath.relativize(file);
            // 최종 백업 경로
            Path finalBackupPath = Common.mergePath(backupFolder, relativePath);

            try {
                // 부모 디렉토리가 존재하지 않으면 생성
                if (Files.notExists(finalBackupPath.getParent())) {
                    Files.createDirectories(finalBackupPath.getParent());
                }
                // 파일 복사
                Files.copy(file, finalBackupPath, StandardCopyOption.REPLACE_EXISTING);
                System.out.println("[status] 기존 파일 복사하여 백업 : " + finalBackupPath);
                FinalResult.backupCnt++;
            } catch (IOException e) {
                System.err.println("[error]\n기존 파일을 복사하여 백업 중 오류가 발생하였습니다. : " + e.getMessage());
                System.out.println("프로그램을 종료합니다.");
                System.exit(1);
            }

        }
        
    }

}
