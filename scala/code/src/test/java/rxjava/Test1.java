package rxjava;

import org.junit.Test;

import java.util.Scanner;

import io.reactivex.rxjava3.core.Observable;

public class Test1 {
    @Test
    public void test() {
        // SAM
        Observable<String> hello = Observable.create(s -> {
            s.onNext("hello");
        });
        // hello -> hello world -> sout
        hello
           .map(str -> str + " world")
           .subscribe(string -> System.out.println(string));

        // world
        // hello world
    }

    // 외부의 인풋 -> Rx
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Observable.create(s -> {
            scanner.forEachRemaining(input -> s.onNext(input));
        });
        // "hello " + input
        // 변형
        // 구독
    }
}
