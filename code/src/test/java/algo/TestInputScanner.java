package algo;

import java.io.InputStream;
import java.util.Scanner;

public class TestInputScanner {

    public static Scanner getScanner(String input) {
        InputStream resourceAsStream = TestInputScanner.class.getResourceAsStream("/" + input);
        return new Scanner(resourceAsStream);
    }
}
