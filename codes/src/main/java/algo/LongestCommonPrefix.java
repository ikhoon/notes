package algo;

/**
 * https://leetcode.com/problems/longest-common-prefix/
 */
public class LongestCommonPrefix {

    public String longestCommonPrefix(String[] strs) {
        if(strs.length == 0) {
            return "";
        }
        StringBuilder builder = new StringBuilder();

        int minLength= Integer.MAX_VALUE;
        for (String str : strs) {
            minLength = Math.min(str.length(), minLength);
        }

        String str = strs[0];
        for (int i = 0; i < minLength; i++) {
            char target = str.charAt(i);
            boolean matched = true;
            for (int j = 1; j < strs.length; j++) {
               if(strs[j].charAt(i) != target) {
                   matched = false;
                   break;
               }
            }
            if(matched) {
                builder.append(target);
            }
            else {
                break;
            }
        }
        return builder.toString();
    }
}
