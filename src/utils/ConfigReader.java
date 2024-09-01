package utils;

import settings.Constants;
import settings.Configurations;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class ConfigReader {

    private final List<String> sourceFileSearchModeOpts;
    private final List<String> targetPathSearchModeOpts;
    private final List<String> backupModeOpts;
    private final Path rootDir;

    public ConfigReader() {
        this.rootDir = Constants.rootDir;
        this.sourceFileSearchModeOpts = new ArrayList<>(Arrays.asList("autoSearch", "manualSearch"));
        this.targetPathSearchModeOpts = new ArrayList<>(Arrays.asList("setRootPath", "manualPath"));
        this.backupModeOpts = new ArrayList<>(Arrays.asList("dateSuffix1", "dateSuffix2", "backupToFolder", "noBackup"));
    }

    public void getConfig() {

        Configurations configurations = new Configurations();

        // 파일 읽기
        try (BufferedReader br = new BufferedReader(new FileReader(this.rootDir + "/Config.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                // 공백 제거
                line = line.trim();

                if (!line.startsWith("#") && !line.isEmpty()) { //#은 주석
                    if (line.startsWith("backup_mode")) {
                        String backupMode = line.split("=")[1].trim();
                        configurations.setBackupMode(backupMode);
                    } else if (line.startsWith("target_path_search_mode")) {
                        String searchMode = line.split("=")[1].trim();
                        configurations.setTargetPathSearchMode(searchMode);
                    } else if (line.startsWith("source_file_search_mode")) {
                        String searchMode = line.split("=")[1].trim();
                        configurations.setSourceFileSearchMode(searchMode);
                    }
                }
            }

        } catch (IOException e) {
            System.err.println("[error]\n파일을 찾을 수 없습니다: " + this.rootDir + "/Config.txt");
            System.out.println("프로그램을 종료합니다.");
            System.exit(1);
        }
    }

    public void checkConfig() {

        String backupMode = Configurations.getBackupMode();
        String targetPathSearchMode = Configurations.getTargetPathSearchMode();
        String sourceFileSearchMode = Configurations.getSourceFileSearchMode();

        if (!sourceFileSearchModeOpts.contains(sourceFileSearchMode)) {
            System.err.println("[error]\nConfig.txt의 source_file_search_mode 설정이 올바르지 않습니다.");
            System.out.println("프로그램을 종료합니다.");
            System.exit(1);
        } else if (!targetPathSearchModeOpts.contains(targetPathSearchMode)) {
            System.err.println("[error]\nConfig.txt의 target_path_search_mode 설정이 올바르지 않습니다.");
            System.out.println("프로그램을 종료합니다.");
            System.exit(1);
        } else if (!backupModeOpts.contains(backupMode)) {
            System.err.println("[error]\nConfig.txt의 backup_mode 설정이 올바르지 않습니다.");
            System.out.println("프로그램을 종료합니다.");
            System.exit(1);
        }
    }

    public void printConfig() {

        String SourceFileSearchMode = Configurations.getSourceFileSearchMode();
        String targetPathSearchMode = Configurations.getTargetPathSearchMode();
        String backupMode = Configurations.getBackupMode();

        System.out.println(Constants.GREEN + "설정사항을 확인해주세요." + Constants.RESET);
        System.out.println("소스 파일 탐색 : " + SourceFileSearchMode);
        System.out.println("타겟 경로 설정 : " + targetPathSearchMode);
        System.out.println("백업 설정 : " + backupMode);
        System.out.println();
    }

    public boolean getUserConfirm() {

        boolean isConfirmed;
        Scanner scanner = Constants.scanner;

        System.out.println(Constants.BLUE + "\n이대로 진행하시겠습니까? (y/n)" + Constants.RESET);

        while (true) {
            String userInput = scanner.nextLine().trim();

            if (userInput.equalsIgnoreCase("Y")) {
                isConfirmed = true;
                break;
            } else if (userInput.equalsIgnoreCase("N")) {
                isConfirmed = false;
                break;
            } else {
                System.err.println("[error]\n잘못된 입력입니다. y 또는 n으로 응답해주세요.");
                System.err.println("파일 복사를 진행하시겠습니까? (y/n)");
            }
        }

        return isConfirmed;
    }

}
