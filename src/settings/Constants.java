package settings;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class Constants {

    public static final Scanner scanner = new Scanner(System.in);

    // ANSI 이스케이프 코드로 색상 설정
    public static String RED = "\u001B[31m";    // 빨간색
    public static String GREEN = "\u001B[32m";  // 초록색
    public static String BLUE = "\u001B[34m";   // 파란색
    public static String RESET = "\u001B[0m";   // 기본 색상으로 리셋

    public static final String LINE1 = "****************"; // 구분선
    public static final String LINE2 = "********************************"; // 구분선
    public static final String LINE3 = "************************************************"; // 구분선

    public static Path rootDir = Paths.get(".");
}
