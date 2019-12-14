package algo;

import java.util.HashMap;
import java.util.Map;

public class RomanToInteger {

    static Map<Character, Integer> romans;
    static {
        romans = new HashMap<>();
        romans.put('I',1);
        romans.put('V',5);
        romans.put('X',10);
        romans.put('L',50);
        romans.put('C',100);
        romans.put('D',500);
        romans.put('M',1000);
    }

    public int romanToInt(String s) {

        int result = 0;
        for (int i = 0; i < s.length(); i++) {
            int num = romans.get(s.charAt(i));
            // I가 나왔을때가 문제이다.
            // 그렇지 않으면 나온 값을 그대로 result에 더하기만 하면 된다.
            // I가 V X 앞에 나오면
            // X가 L C 앞에 나오면
            // C가 D M 앞에 나오면
            // 그 값을 하나식 빼줘야한다.
            if(i < s.length() - 1) {
                if(isMinus(s.charAt(i), s.charAt(i + 1)) ) {
                    num *= -1;
                }
            }
            result += num;
        }
        return result;
    }

    private boolean isMinus(char current, char next) {
        return (
           (current == 'I' && (next == 'V' || next == 'X')) ||
           (current == 'X' && (next == 'L' || next == 'C')) ||
           (current == 'C' && (next == 'D' || next == 'M'))
        );
    }

}
