package algo;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CheckMagazine {
    // Complete the checkMagazine function below.
    static void checkMagazine(String[] magazine, String[] note) {

        Map<String, Integer> map = new HashMap<>();
        for (String m : magazine) {
            map.put(m, map.getOrDefault(m, 0) + 1);
        }
        boolean matched = true;
        for (String n : note) {
            Integer count = map.getOrDefault(n, 0);
            if(count == 0) {
               matched = false;
               break;
            }
            else {
                map.put(n, count - 1);
            }
        }
        if(matched) {
            System.out.println("Yes");
        }
        else {
            System.out.println("No");
        }

    }
}
