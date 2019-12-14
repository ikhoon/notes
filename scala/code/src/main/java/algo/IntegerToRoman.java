package algo;

import java.util.HashMap;
import java.util.Map;

/**
 * https://leetcode.com/problems/integer-to-roman/
 * https://leetcode.com/problems/integer-to-roman/discuss/242510/c%2B%2B-Recursion-solution-52ms
 */
public class IntegerToRoman {
    public String intToRoman(int num) {
        return intToRoman(num, new StringBuilder(), 1000).toString();
    }

    static Map<Integer, String> romans;
    static {
        romans = new HashMap<>();
        romans.put(1, "I");
        romans.put(5, "V");
        romans.put(10, "X");
        romans.put(50, "L");
        romans.put(100, "C");
        romans.put(500, "D");
        romans.put(1000, "M");
    }

    private StringBuilder intToRoman(int num, StringBuilder result, int div) {

        if(num == 0) return result;

        int digit = num / div;


        String one = romans.get(div);
        String five = romans.get(div * 5);
        String ten = romans.get(div * 10);
        if(digit >= 1 && digit <= 3) {
            for (int i = 0; i < digit; i++) {
                result.append(one);
            }
        }
        else if(digit == 4) {
            result.append(one).append(five);
        }
        else if(digit == 5) {
            result.append(five);
        }
        else if(digit >= 6 && digit <= 8) {
            result.append(five);
            for (int i = 5; i < digit; i++) {
               result.append(one);
            }
        }
        else if(digit == 9) {
            result.append(one).append(ten);
        }
        return intToRoman(num % div, result, div / 10);
    }


}
