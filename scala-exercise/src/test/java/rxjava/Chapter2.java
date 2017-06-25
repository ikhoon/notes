package rxjava;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import org.junit.Test;
import org.reactivestreams.Subscription;

import java.math.BigInteger;
import java.util.Iterator;

/**
 * Created by ikhoon on 25/06/2017.
 */
public class Chapter2 {
    // Chapter 2. 리액티브 익스텐션

    // Observable<T>, Observer<T>, Subscriber<T>
    // 이중 뭐니뭐니해도 Observable<T>이 젤 중요

    // ## rx.Observable 해부하기
    // 스트림의 예제 29쪽 젤 밑에줄


    // Observable<T>과 비슷하면서 익숙한 개념 Iterable<T> 있음
    // 하지만 좀 다름
    // Iterable<T>은 next를 요청하면 데이터를 받을수 있음
    // Observable<T>은 subscribe후 데이터를 push해줘야 받을수 있음, 언제 받을지 모름


    // 여기서 우리는 Iterator<T>와 Iterable<T>이 계속 나온다.
    // 우리는 이것들에 대해서 얼마나 알고 있나?
    // QUIZ : Iterable과 Iterator의 interface를 직접 한번 만들어 보자






    public interface MyIterable<T> {
        MyIterator<T> iterator();
    }
    public interface MyIterator<T> {
        boolean hasNext();
        T next();
    }


    /**
     * Iterator 구현하면 아래와 비슷하다.
     * @see Iterable
     *
     */
    class NaturalNumberIterator implements Iterator<BigInteger> {

        BigInteger nat = BigInteger.ZERO;
        @Override
        public boolean hasNext() {
            return true;
        }

        @Override
        public BigInteger next() {
            nat = nat.add(BigInteger.ONE);
            return nat;
        }
    }



    // 그럼 다시 Rx로 돌아와서
    // 31페이지에 rx 를 이용한 예가 있다.
    // rx를 이용해서 뭘 할수 있을까 고민하는 분들이 많다.
    // 비동기로 이벤트가 발생한다면 그곳은 rx가 쓰기 적합한곳이다.
    // 물론 비동기가 이벤트가 아니여도 사용은 할수 있다.



    // ## Observable 알림 구독
    // 쉽다.
    // 그냥 subscribe하면 된다.
    // 이러면 이벤트가 발생하면 들어올것이다.

    class Tweet {
        public Tweet(String message) { this.message = message; }
        String message;

        @Override
        public String toString() {
            return "Tweet(" + message + ")";
        }
    }

    Observable<Tweet> tweets = Observable.just(new Tweet("hello"), new Tweet("world"));
    @Test
    public void subscribeTweets() {

        // 구독하면 된다
        tweets.subscribe(tweet -> System.out.println(tweet));

        // 에러처리도 구독하면 된다.
        // try-catch가 아니다.
        // 비동기에서는 throw가 현재 thread로 전달되지 않는다!
        tweets.subscribe(
                tweet -> System.out.println(tweet),
                ex -> ex.printStackTrace()
        );

        // 이벤트가 끝나면?
        // http stream 전송이 끝나면 처리해야 하는 일이 있을것이다.
        // 그럴땐 3번째 인자를 확인하자.
        tweets.subscribe(
                tweet -> System.out.println(tweet),
                ex -> ex.printStackTrace(),
                () -> System.out.println("Finished")
        );

    }


    // ## Observer<T>로 모든 알림 잡아내기.
    // 하면 된다.
    // QUIZ : observer를 이용하여 이벤트를 구독하고 출력하시오.



    @Test
    public void subscribeWithObserver() {

        Observer<Tweet> observer = new Observer<Tweet>() {
            @Override
            public void onSubscribe(Disposable d) {
                System.out.println("start subscribe");
//                d.dispose();  이 주석을 해제하면 어떻게 될라나?
            }

            @Override
            public void onNext(Tweet tweet) {
                System.out.println(tweet);
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onComplete() {
                System.out.println("Finished");
            }
        };

        tweets.subscribe(observer);
    }



    // ## Subscription 과 Subscribe<T>로 리스러 제어하기!
    // 구독을 취소하는 벙법
    // 이를 지원하는 방법으로 Subscription과 Subscriber가 있다.
    // 우선 Subscription 부터

    public void subscriptionTweet() {
        // 그러나
        // RxJava 2.0 부터인거 같다.
        // Subscription은 사라졌다.
        // 이름이 조금더 명확하게 바뀌었다
        // 해지하는 역할인데 Subscription은 이상하긴했다 옛날부터
//        Subscription subscription = tweets.subscribe(System.out::println);
        Disposable disposable = tweets.subscribe(System.out::println);
        disposable.isDisposed();
        disposable.dispose();

        // dispose를 호출하면 클라이언트 입장에서 구독을 취소할수 있다.
    }

    // 이번엔 Subscriber
    // 이건 listener, 즉 callback 내부에서 구독을 취소하는것이다.
    public void subscriberTweet() {
        // 예상했으지 모르겠지만 Subscriber도 없어졌음
        // 그리고 위에 observer를 다시 보라
//         new Observer<Tweet>() {}
    }



    // ## Observable 만들기
    public void newObservable() {
        // Just
        Observable.just(new Tweet("hello"));

        // from, 얼라 어디갔데?
//        Observable.from()

        // range
        Observable<Integer> range = Observable.range(1, 100);

        // empty
        Observable.empty();

        // never, error


    }


    // # create 정복,
    // TODO 이건 내일 정리하자
}
