package algo;

import java.util.HashMap;
import java.util.Map;

public class LengthOfLongestSubstring {
    public int lengthOfLongestSubstring(String s) {

        int max = 0;
        int n = s.length();
        Map<Character, Integer> map = new HashMap<>();
        for (int j = 0, i = 0; j < n; j++) {
            // j가 문자열에 포함이 되어 있지 않으면 j를 확장한다.
            // 그리고 map에 문자를 넣는다.
            // 그리고 최대값을 갱신한다.
            char jChar = s.charAt(j);
            if (map.get(jChar) != null) {
                // j가 문자열에 포함되어 있으면 i의 위치를 j+1의 위치로 옮긴다.
                i = Math.max(map.get(jChar), i);
            }

            max = Math.max(max, (j - i) + 1);
            map.put(jChar, j + 1);
        }
        return max;
    }


}
