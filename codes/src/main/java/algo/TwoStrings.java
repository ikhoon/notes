package algo;

import java.util.HashSet;
import java.util.Set;

public class TwoStrings {

    static String twoStrings(String s1, String s2) {
        Set<Character> s1Set = new HashSet<>();
        for (char c1: s1.toCharArray()) {
           s1Set.add(c1);
        }

        boolean matched = false;
        for (char c2 : s2.toCharArray()) {
            if(s1Set.contains(c2)) {
                matched = true;
                break;
            }
        }
        if(matched) {
            return "YES";
        } else {
            return "NO";
        }
    }
}
