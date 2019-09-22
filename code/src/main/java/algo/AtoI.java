package algo;

public class AtoI {
    public int myAtoi(String str) {
        // 48 ~ 57
        boolean startParsing = false;
        long number = 0;
        boolean minus = false;
        for (int i = 0; i < str.length(); i++) {
            if(!startParsing && str.charAt(i) == ' ') continue;
            if (str.charAt(i) != ' ' && str.charAt(i) != '-' && str.charAt(i) != '+'   && (str.charAt(i) < '0' || str.charAt(i) > '9')) {
                break;
            }
            if (str.charAt(i) >= '0' && str.charAt(i) <= '9') {
                if (i > 0 && str.charAt(i - 1) == '-' && startParsing == false) {
                    minus = true;
                }
                if (startParsing == false) {
                    startParsing = true;
                }
                number = number * 10 + str.charAt(i) - 48;
            }
        }

        number = minus ? number * -1 : number;

        return (int) Math.max(
                Math.min(Integer.MAX_VALUE, number),
                Integer.MIN_VALUE
        );
    }
}
