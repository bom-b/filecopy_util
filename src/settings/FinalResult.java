package settings;

import java.nio.file.Path;
import java.util.List;

public class FinalResult {
    public static int backupCnt = 0;
    public static int copySuccessCnt = 0;
    public static int copyFailCnt = 0;

    public static void printFinalResult(List<Path> existingSourceFiles) {
        System.out.println("\n\n" + Constants.BLUE + Constants.LINE3 + Constants.RESET);
        System.out.println(Constants.BLUE + "모든 작업이 완료되었습니다. 최종 결과를 확인해주세요." + Constants.RESET);
        System.out.println("백업한 파일 개수 : " + backupCnt);
        System.out.println("반영을 시도한 파일 개수 : " + existingSourceFiles.size());
        System.out.println("파일 반영 결과 : " + "(성공: "+ copySuccessCnt + " / " + "실패: " + copyFailCnt + ")");
        System.out.println(Constants.BLUE + Constants.LINE3 + Constants.RESET);
        System.out.println();
    }

}
