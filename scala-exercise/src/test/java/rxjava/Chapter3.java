package rxjava;


import io.reactivex.Observable;
import io.reactivex.Single;
import org.junit.Test;
import scalaz.Alpha;

import java.time.DayOfWeek;
import java.util.Date;
import java.util.Random;
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


// merge, zip, zipWith, combineLast, 겁나 많이 있다.
// 이건 알아서 읽어보길 바란다.
// 왜냐면 내가 말해도 기억을 못할것이고 나도 봐야지 안다.
// 중요한 기본연사자 몇개만 알고 있자.
// 나머진 필요할때 찾아보면 된다
// 이렇게 많은 이유는 이벤트는 시간을 가지고 있고 그 시간에 따라 언제 데이터가 나타날지 모르기 때문인거 같다.
// 한쪽은 데이터가 빨리 들어오고, 한쪽은 데이터가 천천히 들어올때 우리는 이런경우를 해결해야 한다. 월급받으니까.


    // 마지막으로 괜찮은 예를 하나 보자
    // 우리는 if else를 없애려고 노력해야한다.
    // 이뿐만 아니가 match case, switch 등도 줄이면 좋다.
    // 없앨려면 어떻게 해야하나?
    // 조건문이 없을수는 없을것이다. 그대신 최대한 뒤로 숨겨라. 눈에 뛰지 않게

    // 아래 코드는 우리가 종종사용하는 로직이다.
    // if(hasCache()) {
    //  return cache;
    // else {
    //  return loadFromDb();
    // }
    // 단순한 로직이지만 if else 가 있다.
    // 숨기자!

    Observable<String> loadFromCache() {
        Random rand = new Random();
        boolean next = rand.nextBoolean();
        if(next) {
            return Observable.just("hello from cache");
        }
        else {
            return Observable.empty();
        }
    }

    Observable<String> loadFromDb() {
        Random rand = new Random();
        boolean next = rand.nextBoolean();
        if(next) {
            return Observable.just("world from db");
        }
        else {
            return Observable.empty();
        }
    }

    @Test
    public void concat() throws InterruptedException {
        Observable
                .interval(300, TimeUnit.MILLISECONDS)
                .take(100)
                .subscribe(i -> {
                    Single<String> message = Observable
                        .concat(loadFromCache(), loadFromDb())
                        .first("nothing in cache & db");

                    message.subscribe(m -> System.out.println("#" + i + " " + m));
                });
        TimeUnit.SECONDS.sleep(20);
    }

}

