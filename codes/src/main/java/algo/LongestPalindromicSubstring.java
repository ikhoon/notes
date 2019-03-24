package algo;

/**
 * https://leetcode.com/problems/longest-palindromic-substring
 */
public class LongestPalindromicSubstring {
    public String longestPalindrome(String s) {

        if(s.isEmpty() || s.length() == 1) {
            return s;
        }

        // 앞에서 부터 순차적으로 스캔한다.
        // (i, i) 와 (i, i + 1) 에서 왼쪽과 오른쪽을 증가시키며 같은 문자열이 되는 길이를 확인한다
        // 더 큰것을 유지한다.
        // start와 end도 갱신한다.
        int start = 0;
        int end = 0;
        for (int i = 0; i < s.length(); i++) {
            int len1 = expendAroundCenter(s, i, i);
            int len2 = expendAroundCenter(s, i, i + 1);
            int len = Math.max(len1, len2);
            if(len > end - start) {
                // len 3, i = 2, start = 1, end = 3
                // len 4, i = 2, start = 1, end = 4
                start = i - (len - 1)/ 2;
                end = i + len / 2;
            }
        }

        return s.substring(start, end + 1);
    }
    int expendAroundCenter(String s, int left, int right) {
        while(left >= 0 && right < s.length() && s.charAt(left) == s.charAt(right)) {
            left -= 1;
            right += 1;
        }
        return right - left - 1;
    }
}
