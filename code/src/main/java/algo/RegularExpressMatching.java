package algo;


/**
 * https://leetcode.com/problems/regular-expression-matching/
 * 스킵,역시 hard는 시간이 너무 오래 걸린다...
 */
public class RegularExpressMatching {

    public boolean isMatch(String s, String p) {

        int pi = 0;
        int pis = 0;
        int i;
        boolean matched = true;
        for (i = 0; i < s.length() && pi < p.length(); i++) {
            // 문자열이 같을때 넘어감
            if (s.charAt(i) == p.charAt(pi)) {
                pi += 1;
                pis = pi;
            }
            // '.' 인경우는 비교하지 않고 증가함
            else if (p.charAt(pi) == '.') {
                pi += 1;
                pis += 1;
            }
            else if (p.charAt(pi) == '*') {
                if(i == 0) {
                    matched = false;
                    break;
                }
                else if(s.charAt(pis) == s.charAt(i)){
                    continue;
                }
                else {
                    matched = false;
                    break;
                }
            }
        }
        return matched;
    }
}
