import java.util.ArrayList;
import java.util.List;

/**
 * Created by ikhoon on 2019-02-15.
 */
public class Test {

    public static void main(String[] args) {
        List<Integer> xs = new ArrayList<>();
        xs.add(10);
        xs.add(Integer.valueOf(10));

        int x = xs.get(0).intValue();
        Integer y = xs.get(0);
    }
}
