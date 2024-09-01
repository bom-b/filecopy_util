package vo;

import settings.Configurations;
import settings.Constants;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ExistingSourceFiles {
    private List<Path> settingPaths = new ArrayList<>();
    private List<Path> fullPaths = new ArrayList<>();
    private int notFoundCount = 0;

    public ExistingSourceFiles() {
    }

    public void addSettingPath(Path file) {
        settingPaths.add(file);
    }

    public void addFullPath(Path file) {
        fullPaths.add(file);
    }

    public void incrementNotFoundCount() {
        notFoundCount++;
    }

    public List<Path> getSettingPaths() {
        return settingPaths;
    }

    public List<Path> getFullPaths() {
        return fullPaths;
    }

    public int getNotFoundCount() {
        return notFoundCount;
    }

    public void printExistingFiles() {


        if (Configurations.getSourceFileSearchMode().equals("autoSearch")) {

            System.out.println(Constants.GREEN + "[autoSearchMode]\nSourceFile 하위 경로에서 아래 소스파일을 찾았습니다." + Constants.RESET);
            System.out.println("찾은 파일 수 : " + settingPaths.size());

        } else {

            System.out.println(Constants.GREEN + "[manualSearch]\nSourcePath.txt 목록을 통해 아래 소스파일을 찾았습니다." + Constants.RESET);
            System.out.println("찾은 파일 수 : " + settingPaths.size());
            System.out.println("찾지 못한 파일 수 : " + notFoundCount);
        }



        for (Path path : settingPaths) {
            System.out.println(path); // 존재하는 파일 경로 출력
        }

        System.out.println();
    }
}
