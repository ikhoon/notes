package rxjava;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.junit.Test;
import org.reactivestreams.Subscription;
import twitter4j.*;
import twitter4j.auth.AccessToken;

import java.awt.*;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.function.Consumer;

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
    @Test
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


    // # create 정복

    /**
     * Quiz : RxJava를 이용해서 아래와 같이 출력하시오. range api를 사용해보자.

     Before
     5
     6
     7
     After

     */
    @Test
    public void createObserver() {
        System.out.println("Before");
        // Fill here.
        System.out.println("After");

    }





    /*
        Observable
                .range(5, 3)
                .subscribe(i -> System.out.println(i));
    */

    // 지난번 시간엔 마티니에게 뻥을 친게 되었다
    // pub-sub 같은 느낌이 된다. 다만 create 안에 함수가 두번 호출되는건 긴가민가하다.
    // 결과를 저장하려면 cache

    @Test
    public void publishSubscribe1() {
        Observable<Integer> ints =
                Observable.create(subscriber -> {
                    System.out.println("Create : " + Thread.currentThread().getName());
                    subscriber.onNext(1);
                    subscriber.onComplete();
                });
        System.out.println("Starting");
        ints.subscribe(i -> System.out.println("Element A : " + i));
        ints.subscribe(i -> System.out.println("Element B : " + i));
        System.out.println("Exit");
    }

    @Test
    public void publishSubscribe2() {
        Observable<Integer> ints =
                Observable.just(1, 2, 3, 4, 5);
        System.out.println("Starting");
        ints.subscribe(i -> System.out.println("Element A : " + i));
        ints.subscribe(i -> System.out.println("Element B : " + i));
        System.out.println("Exit");
    }

    @Test
    public void publishSubscriber3() {
        Observable<Integer> ints =
                Observable.range(1, 10);
        System.out.println("Starting");
        ints.subscribe(i -> System.out.println("Element A : " + i));
        ints.subscribe(i -> System.out.println("Element B : " + i));
        System.out.println("Exit");
    }

    // timer와 inteval같은 api가 있다.
    // 그러나 아직은 알필요가 없다.
    // 라고 개인적으로 생각한다.


   // ## 뜨거운 Observable vs. 차가운 Observable

    // 차가운 Observable - 이벤트를 구독하지 않으면 시작하지 않는다.
    // 구독자가 없으면 그냥 자료구조일뿐
    // 즉, 이벤트는 느긋하게 만들어지며, cache처리하지 않는다
    // 또다시 즉 모든 구독자는 stream의 복사본을 갖게 된다.
    // create함수에 "Create: "가 구독자마다 출력되는것과 같은 이치
    // 예 - create(), just(), range, from() 등이 있다.


    // 뜨거운 Observable - 구독(subscriber)와 상관없이 바로 이벤트를 방출한다.
    // 소비자로부터 독립적이다.
    // Subscriber의 여부가 Observable의 동작에 영향을 미치지 않는다.
    // Observable.inteval()도 뜨거운게 아니라 한다.

    // 뜨거운 Observable은 이벤트 소스를 통제할수 없을때 발생
    // 예 - 마우스 움직임, 키보드 입력

    // 시간이 중하다, 그에 따라 hot과 cold가 나뉜다.




    // # 사례 : 콜백 API를 Observable 스트림으로
    public void callbackToObservable() {
        TwitterStream  twitterStream = TwitterStreamFactory.getSingleton();
        twitterStream.setOAuthConsumer("ThH4c18RfcZqFV3WwOhNphOjy", "bVOLRagShDuzE3rokJi0MtcaOGEN8hIppgqaee1JUYNDWJaGCT");
        twitterStream.addListener(new StatusListener() {
            @Override
            public void onStatus(Status status) {
                System.out.println("Status: " + status);
            }
            @Override
            public void onException(Exception ex) {
                System.out.println("Error: " + ex);
            }

            // 기타 등등
            @Override public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) { }
            @Override public void onTrackLimitationNotice(int numberOfLimitedStatuses) { }
            @Override public void onScrubGeo(long userId, long upToStatusId) { }
            @Override public void onStallWarning(StallWarning warning) { }
        });

        // 이코드는 아무것도 할수없다.
        // 트윗 메시지(status) 받고 나서 db에 저장한다든가, api호출한다는가 해야하는데
        // callback에 그걸 넣으면 결합도가 겁내 높다. 안좋다.

    }

    // 그럼 비지니스 영역과 twitter 모듈 영역을 분리하자.
    // 일단 자바 8님에게 감사하자.
    // lambda가 없었다면 이 코드는 아주 구렸을것이다.
    void consume(Consumer<Status> onStatus, Consumer<Exception> onException) {
        TwitterStream  twitterStream = TwitterStreamFactory.getSingleton();
        twitterStream.setOAuthConsumer("ThH4c18RfcZqFV3WwOhNphOjy", "bVOLRagShDuzE3rokJi0MtcaOGEN8hIppgqaee1JUYNDWJaGCT");
        twitterStream.addListener(new StatusListener() {
            @Override
            public void onStatus(Status status) {
                // callback함수에 상태 전달
                // 자바스크립트에서 비동기 처리하려고 많이 썼던 방식이다.
                // 자바라고 비동기 처리하는데 다를게 없다.
                onStatus.accept(status);
            }
            @Override
            public void onException(Exception ex) {
                onException.accept(ex);
            }

            // 기타 등등
            @Override public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) { }
            @Override public void onTrackLimitationNotice(int numberOfLimitedStatuses) { }
            @Override public void onScrubGeo(long userId, long upToStatusId) { }
            @Override public void onStallWarning(StallWarning warning) { }
        });

    }

    @Test
    public void consumeTweet() {
        // 이제 트위터 로직이랑 비지니스 로직을 조금은 분리할수 있다.
        consume(
            status -> System.out.println("Status: " + status),
            error -> error.printStackTrace()
        );
    }

    // 허나 사실 문제점을 다른 계층으로 옮긴것 뿐이다.
    // * 초당 트윗이 몇개인지 세아리려면?
    // * 처음 N개의 트윗만 가져오려면?
    // * 여러면에게 구독자에게 트윗을 전송하려면?
    // * 구독해지는 가능한가?

    // 이런 다양한 문제점을 지금의 코드에서는 해결할수가 없다.
    // 어떻게 끼워 넣으면 해결될수는 있을것이다. 하지만 왜?
    // 우리에겐 Rx가 있다.

    Observable<Status> observe() {
        TwitterStream twitterStream = TwitterStreamFactory.getSingleton();
        return Observable.<Status>create(subscribe -> {
            twitterStream.addListener(new StatusListener() {
                @Override
                public void onStatus(Status status) {
                    subscribe.onNext(status);
                }

                @Override
                public void onException(Exception ex) {
                    subscribe.onError(ex);
                }

                // 기타 등등
                @Override public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) { }
                @Override public void onTrackLimitationNotice(int numberOfLimitedStatuses) { }
                @Override public void onScrubGeo(long userId, long upToStatusId) { }
                @Override public void onStallWarning(StallWarning warning) { }
            });
            if(subscribe.isDisposed()) {
                twitterStream.shutdown();
            }
        });
    }


    @Test
    public void observeSubscribe() {
        // 여기에서의 특징은
        // observe는 더이상 callback을 필요로 하지 않는다
        // 그냥 함수를 호출하고 필요하면 subscribe하면 된다
        // 훨씬더 유연한 함수가 된것이다.

        observe().subscribe(
            status -> System.out.println("Status: " + status),
            ex -> ex.printStackTrace()
        );
    }

    // rx.subjects.Subject
    // 한발짝만 더 나가보자

    // 지금은 Observable.create의 callback으로 코드가 구현이 되어 있다.
    // 이 콜백도 없애고 싶지 않은가?

    public Observable<Status> subject() {
        PublishSubject<Status> subject = PublishSubject.create();

        TwitterStream twitterStream = TwitterStreamFactory.getSingleton();

        twitterStream.addListener(new StatusListener() {
            @Override
            public void onStatus(Status status) {
                subject.onNext(status);
            }

            @Override
            public void onException(Exception ex) {
                subject.onError(ex);
            }

            // 기타 등등
            @Override public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) { }
            @Override public void onTrackLimitationNotice(int numberOfLimitedStatuses) { }
            @Override public void onScrubGeo(long userId, long upToStatusId) { }
            @Override public void onStallWarning(StallWarning warning) { }
        });
        return subject;
    }

}
