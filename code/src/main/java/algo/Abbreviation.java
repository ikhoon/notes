package algo;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Abbreviation {
    // Complete the abbreviation function below.
    static String abbreviation(String a, String b) {
        return isEqual(a, b) ? "YES" : "NO";
    }

    static HashMap<List<String>, Boolean> cache = new HashMap<>(1000);
    static boolean isEqual(String a, String b) {
        List<String> cacheKey = Arrays.asList(a, b);
        Boolean cached = cache.get(cacheKey);
        if(cached != null) {
            return cached;
        }

        if (a.length() == 0 && b.length() == 0) {
            return true;
        }
        if (a.length() == 0 && b.length() > 0) {
            cache.putIfAbsent(cacheKey, false);
            return false;
        }

        char charA = a.charAt(a.length() - 1);
        if (b.length() == 0) {
            boolean result;
            if(charA >= 97) {
                result = isEqual(a.substring(0, a.length() - 1), b);
            } else {
                result = false;
            }
            cache.putIfAbsent(cacheKey, result);
            return result;
        }

        char charB = b.charAt(b.length() - 1);

        if(a.length() == 1 && b.length() == 1 && charA < 97 && charA != charB) {
            return false;
        }

        boolean result;
        if (charA == charB) {
            result = isEqual(a.substring(0, a.length() - 1), b.substring(0, b.length() - 1));
        }
        else if (charA - 32 == charB) {
            result = (
                isEqual(a.substring(0, a.length() - 1), b.substring(0, b.length() - 1)) ||
                isEqual(a.substring(0, a.length() - 1), b)
            );
        } else if (charA < 97) {
            result = false;
        } else {
            result = isEqual(a.substring(0, a.length() - 1), b);
        }
        cache.putIfAbsent(cacheKey, result);
        return result;
    }
}
