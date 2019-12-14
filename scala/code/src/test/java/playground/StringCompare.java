package playground;

public class StringCompare {
    public static void main(String[] args) {
        boolean test = true;
        String a = "abcdef";
        String b = "abc";
        if(test) {
            b += "def";
        }
        System.out.println(a + " " + b);
        System.out.println(a == b);
    }
}
