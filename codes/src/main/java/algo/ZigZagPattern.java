package algo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ZigZagPattern {
    public String convert(String s, int numRows) {
        if(numRows == 1) return s;

        List<StringBuffer> stringBuffers = new ArrayList<>();
        for (int i = 0; i < numRows; i++) {
           stringBuffers.add(new StringBuffer());
        }

        boolean down = true;
        int cursor = 0;
        for (int i = 0; i < s.length(); i++) {
            stringBuffers.get(cursor).append(s.charAt(i));
            if(down) {
                cursor += 1;
            }
            else {
                cursor -= 1;
            }
            if(cursor % numRows == 0 || cursor % numRows == (numRows - 1)) {
                down = !down;
            }

        }
        return stringBuffers.stream()
                .reduce(StringBuffer::append)
                .get().toString();
    }
}
