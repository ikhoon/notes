package playground;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ikhoon on 2018-09-30.
 */
public class Permutation {
    public List<String> permute(String string) {

        // GIVEN abc

        return doPermute(string, "");
    }

    List<String> doPermute(String origin, String selected) {
        if(origin.length() == selected.length()) {
            List<String> single = new ArrayList<>();
            single.add(selected);
            return single;
        } else {
            List<String> results = new ArrayList<>();
            for (int i = 0; i < origin.length(); i++) {
                char x = origin.charAt(i);
                if(selected.indexOf(x) < 0) {
                   results.addAll(doPermute(origin, selected + x));
                }
            }
            return results;
        }

    }


}

