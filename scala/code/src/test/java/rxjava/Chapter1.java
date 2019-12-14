package rxjava;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import org.junit.Test;

import java.util.Iterator;
import java.util.Objects;
import java.util.Scanner;

/**
 * Created by ikhoon on 20/06/2017.
 */
public class Chapter1 {
    // 신규 java project setting, gradle이 편하지 않을까 싶다
    //
    // # RxJava가 무엇이가에 대해서 전체적으로 알아본다.
    // 전체 책구성은 앞의 1, 2장에서 많은 포괄적인 내용이 나온다.
    // 리엄 - 시작부터 몰아 많은 내용에 당황하지 않길 바란다.
    //     - 뒤로 갈수록 난이도를 높이면 좋을련만...


    // ## 리액티브 프로그래밍, 설명 책 2쪽에 나와있음 읽어보길 바람
    // - 이벤트의 변화의 반응에 초점을 맞추 프로그래밍이라 나와있음
    // - 맞는 말이긴 이해하기 쉽지 않음, 외부 이벤트를 반응형으로 잘 처리할수 있는 프로그래밍인것 같음
    // 어떻게 보면 Observer 패턴의 확장판 혹은 일반화라 볼수 있을것 같다는 생각이 듦.


    // ## 언제 리액티브 프로그래밍이 필요한가.
    // * user input
    // * file, network io등 nio를 사용한 경우?
    // 그외는 책 4쪽을 읽어보라.


    // ## RxJava는 어떻게 동작하는가?

    // push, push, push!!!
    // 기본적으로 push방식을 지향한다.
    // pull방식으로도 사용은 가능하다고 한다.

    // push 방식? 뭐가 push 방식이당가?

    /**
     * push 방식은 onNext에 값을 밀어넣어준다.
     * {@link io.reactivex.Observer#onNext(Object)}
     * @param t
     * @param <T>
     */
    <T> void onNext(T t) {

    }

    /**
     * 우리가 자주 사용하는 pull방식은 값을 꺼내온다.
     * {@link Iterator#next()}
     * @param <T>
     * @return 어떤값
     */
    <T> T next() {
        return (T) new Object();
    }


    /**
     * push 방식을 구현하기 위해서는
     * Observable 과 Observer 쌍을
     * {@link Observable#subscribe(Observer)} 함수를 이용해서 연결한다.
     */


    // 자료 구조현 내부를 한번 살펴보자.
    interface MyObserver<T> {

        // 구독을 취소할때 쓰인다.
        void onSubscribe(Disposable d);

        // 이벤트의 끝을 알린다.
        void onComplete();
        // 새로운 이벤트를 알린다.
        void onNext(T t);
        // 에러를 알려준다.
        void onError(Throwable e);
    }

    interface MyObservable<T> {
        // 구독을 시작할때 쓰인다.
        void subscribe(MyObserver observer);
    }


    // Observable <=> List 은 데이터 스트림, 이벤트 스트림을 의미한다.
    // 즉 Observable ~= Stream 이라 생각해도 무방하다.

    // 그러면 Observer 는 뭔가?
    // 데이터를 push하는 녀석이다.
    // Observer에서 onNext(T t) 함수를 통해서 밀어준 데이터를 Observable 이 풀어준다

    // pull 방식을 사용하려먼 Producer라는게 있다는데 나중에 알아보자. 나중에 나온단다
    // RxJava 최신버전이랑 책이랑 API가 다른것 같다. 뭐 상관없다.

    // 일단 요는
    // 1. Observer - 데이터를 만든다.
    // 2. Observable - 데이터를 표현한다.
    // 3. Observable#subscribe 로 구독한다.


    @Test
    public void createObservable() {
        Observable<Object> helloWorld = Observable.create(s -> {
            s.onNext("Hello World");
            s.onComplete();
        });
        helloWorld.subscribe(hello -> System.out.println(hello));
    }


    // 연습문제
    //
    public static void main(String[] args) throws InterruptedException {
       // blocking();
        nonblocking();
    }

    private static void blocking() throws InterruptedException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("## input any");
        Observable<String> inputStream = Observable.create(s -> {
            while (!s.isDisposed() && scanner.hasNext()) {
                String next = scanner.next();
                s.onNext(next);
                if (Objects.equals(next, "END")) {
                    scanner.close();
                    s.onComplete();
                }
            }
        });
        Disposable subscribe = inputStream
                .map(input -> "Blocking Hello, " + input)
                .subscribe(input -> System.out.println(input));

        System.out.println("## start sleep");
        Thread.sleep(10000);
        System.out.println("## end sleep");
        subscribe.dispose();

    }
    private static void nonblocking() throws InterruptedException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("## input any");
        Observable<String> inputStream = Observable.create(s -> {
            new Thread(() -> {
                while (!s.isDisposed() && scanner.hasNext()) {
                    String next = scanner.next();
                    s.onNext(next);
                    if (Objects.equals(next, "END")) {
                        s.onComplete();
                        scanner.close();
                    }
                }
            }).start();
        });
        Disposable subscribe = inputStream
                .map(input -> "Non Blocking Hello, " + input)
                .subscribe(input -> System.out.println(input));
        System.out.println("## start sleep");
        Thread.sleep(10000);
        System.out.println("## end sleep");
        subscribe.dispose();

    }

}

