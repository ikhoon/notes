package playground;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by ikhoon on 2018-10-02.
 */
public class Combination {

    public List<String> combination(String string, int r) {
        System.out.println("string " + string + " " + r);
        if(r == 0) {
            return Collections.emptyList();
        } else if(r == string.length()) {
            List<String> xs = new ArrayList<>();
            xs.add(string);
            return xs;
        }
        else {
            List<String> xs = new ArrayList<>();
            xs.addAll(combination(string.substring(0, string.length() - 1), r - 1));
            xs.addAll(combination(string.substring(0, string.length() - 1), r));
            return xs;
        }
    }
}
