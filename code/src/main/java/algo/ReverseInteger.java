package algo;

public class ReverseInteger {
    public int reverse(int x) {

        boolean minus = x < 0;
        String s = String.valueOf(Math.abs((long)x));
        StringBuilder reverse = new StringBuilder(s).reverse();
        if(minus) {
            reverse.insert(0, "-");
        }

        return ((int) (Long.parseLong(reverse.toString())));
    }
}
