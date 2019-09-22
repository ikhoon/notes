package algo;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

// 풀다가 실패
public class ReverseShuttleMerge {
    // Complete the reverseShuffleMerge function below.
    static String reverseShuffleMerge(String s) {

        // 아무것도 없을때 1개 혹은 빈문자 그대로 반환하자
        if (s.isEmpty() || s.length() == 1) {
            return s;
        }

        List<String> matched = new ArrayList<>();
        // 한개씩 빼서 merge ... 돌린다.
        // 만약 한개 그 결과가 만족하면 돌린다.
        // 만족하지 못하면 하나씩 붙여서 반환한다.
        for (int i = 0; i < s.length(); i++) {
            String input = s.substring(0, i) + s.substring(i + 1);
            List<String> shuffled = shuffle(input);
            for (String s1 : shuffled) {
                for (String s2 : merge(reverse(input), s1)) {
                    if(s2.equals(s)) {
                        matched.add(s2);
                    }
                }
            }

        }
        Collections.sort(matched);
        System.out.println("matched = " + matched);
        return "";
    }



    static List<String> merge(String a, String b) {
        return shuffle(a + b);
    }

    static String reverse(String s) {
        return new StringBuilder(s).reverse().toString();
    }

    static HashMap<String, List<String>> shuffleCache = new HashMap<>();

    static List<String> shuffle(String a) {
        if(shuffleCache.get(a) != null) {
            shuffleCache.get(a);
        }
        if (a.length() == 1) {
            return Collections.singletonList(a);
        }
        List<String> strings = new ArrayList<>();
        for (int i = 0; i < a.length(); i++) {
            int finalI = i;
            List<String> subShuffle = shuffle(a.substring(0, i) + a.substring(i + 1))
                    .stream()
                    .map(str -> a.charAt(finalI) + str).collect(Collectors.toList());
            strings.addAll(subShuffle);
        }
        shuffleCache.putIfAbsent(a, strings);
        return strings;
    }

}
