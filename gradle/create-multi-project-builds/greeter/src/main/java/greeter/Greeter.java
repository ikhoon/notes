package greeter;

import greeting.GreetingFormatter;

public class Greeter {
    public static void main(String[] args) {
        final String out = GreetingFormatter.greeting(args[0]);
        System.out.println(out);
    }
}