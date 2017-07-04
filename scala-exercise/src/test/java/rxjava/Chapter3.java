package rxjava;


import io.reactivex.Observable;
import org.junit.Test;

import java.time.DayOfWeek;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * Created by ikhoon on 03/07/2017.
 */
public class Chapter3 {
    // Chapter 3. 연산자와 변환

    // 이제부터 연산자에 대해서 알아보자.
    // scala의 collection api와 많은 부분이 유사하다.
    // 물론 event stream에 특화된 observable 많의 api도 있다.


public static void main(String[] args) throws InterruptedException {
    // 이번 쳅터는 실습이다!

    // QUIZ : 1 ~ 10 까지의 값을 가지고 있는 observable을 하나 만들어보자.
    Observable<Integer> ints = Observable.empty();

    // QUIZ : filter를 이용해서 짝수만 뽑아내라
    Observable<Integer> evens = ints;

    // QUIZ : map 연산자를 이용해서 모든 값을 1씩 증가시켜라
    Observable<Integer> addOnes = evens;




    // doOnNext - 연산자 사이에 로깅하기
    Observable
            .just(8, 9, 10)
            .doOnNext(i -> System.out.println("A: " + i))
            .filter(i -> i % 3 > 0)
            .doOnNext(i -> System.out.println("B: " + i))
            .map(i -> "#" + i * 10)
            .doOnNext(s -> System.out.println("C: " + s))
            .filter(s -> s.length() < 4)
            .subscribe(s -> System.out.println("D: " + s));

    // QUIZ : 위의 연산의 결과값은 어떤 순서로 나올까요?
    // 1. A -> B -> C -> D -> A -> B -> C ....
    // 2. A -> A -> A -> B -> B -> B -> C ....


    // QUIZ : flatMap 연산자를 이용해서 모든 값을 1씩 증가 시켜라
    // 우린 flatMap이란 용어를 참 많이 본다.
    // 언제 유용한가?



}

public void delay() throws InterruptedException {

    // delay
    // 지연을 시킨다
    System.out.println(new Date() + "# before delay test");
    Observable<Integer> delayed = Observable.just(1, 2, 3)
            .delay(3, TimeUnit.SECONDS);
    System.out.println(new Date() + "# delay test starting...");
    delayed.subscribe(i ->
            System.out.println(new Date() + " " + Thread.currentThread().getName() + " " + i)
    );
    System.out.println(new Date() + "# after delay test ");

    TimeUnit.SECONDS.sleep(10);
    //


    // with timer
    System.out.println("using delay");
    Observable
            .just("Lorem", "ipsum", "dolor", "sit", "amet")
            .delay(word -> Observable.timer(word.length(), TimeUnit.SECONDS))
            .subscribe(System.out::println);

    System.out.println("using flatMap + timer");
    Observable
            .just("Lorem", "ipsum", "dolor", "sit", "amet")
            .flatMap(word ->
                    Observable.timer(word.length(), TimeUnit.SECONDS).map(i -> word)
            )
            .subscribe(System.out::println);


    TimeUnit.SECONDS.sleep(10);

}

Observable<String> loadRecodesFor(DayOfWeek dow) {
    switch (dow) {
        case SUNDAY:
            return Observable
                    .interval(90, MILLISECONDS)
                    .take(5)
                    .map(i -> "Sun-" + i);
        case MONDAY:
            return Observable
                    .interval(90, MILLISECONDS)
                    .take(5)
                    .map(i -> "Mon-" + i);
        default:
            return Observable.empty();
    }
}

// concatMap으로 순서 유지하기
@Test
public void flatMapNotOrdered() throws InterruptedException {
    Observable<String> days = Observable
            .just(DayOfWeek.SUNDAY, DayOfWeek.MONDAY)
            .flatMap(this::loadRecodesFor);

    days.subscribe(d -> System.out.println(d));

    TimeUnit.SECONDS.sleep(3);
}

@Test
public void concatMap() throws InterruptedException {
    Observable<String> days = Observable
            .just(DayOfWeek.SUNDAY, DayOfWeek.MONDAY)
            .concatMap(this::loadRecodesFor);

    days.subscribe(d -> System.out.println(d));

    TimeUnit.SECONDS.sleep(3);

}

Observable<Integer> slowRequest(Integer integer) throws InterruptedException {
    Integer result = integer * 10;
    System.out.println(new Date() + " start request " + result);
//    TimeUnit.SECONDS.sleep(3);
//    System.out.println(new Date() + " finish request " + result);
    return Observable.just(result).delay(10, TimeUnit.SECONDS);
}
// flatMap의 동시성 제어
@Test
public void flatMapNoConcurrentControl() throws InterruptedException {
    Observable<Integer> ints = Observable.range(1, 10000);
    ints
        .flatMap(this::slowRequest)
        .subscribe(i -> System.out.println(new Date() + " ## result : " + i));
    TimeUnit.SECONDS.sleep(100);
}


@Test
public void flatMapConcurrentControl() throws InterruptedException {
    Observable<Integer> ints = Observable.range(1, 10000);
    ints
            .flatMap(this::slowRequest, 1)
            .subscribe(i -> System.out.println(new Date() + " ## result : " + i));
    TimeUnit.SECONDS.sleep(100);
}

// 그냥 flatMap 함수자체가 제어되는건 아니고 다음에 반환하는 Observable을 제어하는것 같다.


// TODO merge, zip, zipWith, combineLast,

//

}
