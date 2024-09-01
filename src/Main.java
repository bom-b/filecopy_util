import settings.Constants;
import settings.FinalResult;
import utils.*;
import vo.ExistingSourceFiles;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        if(args.length > 0) {
            Constants.rootDir = Paths.get(args[0].trim()); //실제 환경에서 실행시 .sh 스크립트를 통해 인자로 받는 경로
        }

        // 상수 설정
        setConstants();

        // 프로그램 시작 메시지 출력
        printStartMessage();

        // 설정 읽기
        ConfigReader configReader = new ConfigReader();
        configReader.getConfig();
        // 설정 유효성 검사
        configReader.checkConfig();
        // 설정 사항 출력
        configReader.printConfig();

        // 소스파일 검사
        SourcePathReader sourcePathReader = new SourcePathReader();
        ExistingSourceFiles existingSourceFiles = sourcePathReader.getExistingFileList();
        // 검사결과 출력
        existingSourceFiles.printExistingFiles();

        // 최종 반영 경로 확인
        PreviewHelper previewHelper = new PreviewHelper();
        previewHelper.showPreview(existingSourceFiles.getSettingPaths());
        
        // 설정대로 진행여부 확인
        if(!configReader.getUserConfirm()){
            System.out.println("프로그램을 종료합니다.");
            Constants.scanner.close();
            return;
        }

        System.out.println(Constants.GREEN + "\n작업을 시작합니다.." + Constants.RESET);
        // 백업시작
        BackupHelper backupHelper = new BackupHelper();
        backupHelper.startBackup();
        
        // 파일복사 시작
        FileCopyHelper fileCopyHelper = new FileCopyHelper();
        List<Path> finalTargetPaths = fileCopyHelper.startFileCopy(existingSourceFiles.getSettingPaths());

        // 검증 시작
        System.out.println(Constants.GREEN + "\n검증을 시작합니다.." + Constants.RESET);
        ValidationHelper validationHelper = new ValidationHelper();
        validationHelper.startValidation(existingSourceFiles.getFullPaths(), finalTargetPaths);

        // 최종결과 출력
        FinalResult.printFinalResult(existingSourceFiles.getFullPaths());

        // 프로그램 종료
        exitProgram();
    }

    private static void printStartMessage() {
        System.out.println();
        System.out.println(Constants.LINE2);
        System.out.println(Constants.GREEN + "반영 도우미를 시작합니다." + Constants.RESET);
        System.out.println(Constants.LINE2);
        System.out.println();
    }

    private static void setConstants() {
        String os = System.getProperty("os.name").toLowerCase();

        // 윈도우 콘솔 글자색 지원 삭제
        if (os.contains("win")) {
            Constants.RED = "";
            Constants.GREEN = "";
            Constants.BLUE = "";
            Constants.RESET = "";
        }
    }

    public static void exitProgram() {
        System.out.println(Constants.BLUE + "\n프로그램을 종료하려면 아무 키나 입력하세요..." + Constants.RESET);

        Scanner scanner = Constants.scanner;
        scanner.nextLine();

        System.out.println("프로그램을 종료합니다.");
        scanner.close(); // Scanner 리소스 해제
        System.exit(0); // 프로그램 종료
    }

}