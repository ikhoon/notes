package rxjava;

import io.reactivex.Observable;
import io.reactivex.Single;
import scala.collection.immutable.ListSet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static io.reactivex.Observable.*;

/**
 * Created by ikhoon on 16/07/2017.
 */
public class Chapter4 {
    // 4장 기존 프로그래밍에 리액티브 프로그래밍 적용하기

    // 내가 만드는 프로그래밍은 이벤트가 없다 생각한다.
    // 그리고 Rx를 적용할때가 없다고 생각을 한다.
    // 하지만 그건 착각일것이다.
    // API를 요청하고 받는것도 event가 되고
    // db를 select후 결과를 받는것도 event가 된다.
    // 그러면 이제 우리의 코드를 Rx로 바꾸어 보자.

    class Person { public int age; }
    List<Person> query(String sql)  { return Collections.emptyList(); }

    // # 컬랙션에서 Observable로

    List<Person> listPeople() {
       return query("SELECT * FROM people");
    }

    // Iterable<T>과 Observable<T>은 쌍대성을 이룬다. 즉 서로 바끌수 있다.
    // QUIZ : List<T>를 Observable<T>로 변환해서 반환하라.
    Observable<Person> listPeople2() {
        return null;
    }








    Observable<Person> listPeople3() {
        List<Person> people = query("SELECT * FROM people");
        return fromIterable(people);
    }











    // Observable를 한번 만들면 줄줄이 들고 다녀야 한다.
    // Future를 사용해봐서 알겠지만 한번 non blocking에서 동작하면 blocking하면 아무런 효과가 없다.

    // 그렇지만 이렇게 바꾸기에는 한번에 바꾸어야 하는 코드량이 많다.
    // Rx를 순차 적용하기 위해서 일부만 Observable을 사용하고(nonblocking) 다시 Blocking IO로 바꾸는방식을 취해보겠다.
















    // ## BlockingObservable: 리액티브 세상 벗어나기
    // QUIZ : Observable<T>를 다시 List<T>로 바꾸기
    // API몰라도 된다. 언제는 알고 했나, smart한 intellij를 이용해서 programming해보자.
    public List<Person> toBlockingList(Observable<Person> peopleStream)  {
        return null;
    }





















    public List<Person> toBlockingList1(Observable<Person> peopleStream)  {
        Single<List<Person>> listSingle = peopleStream.toList();
        return listSingle.blockingGet();
    }








    // ## 느긋한 포용하기
    // Observable 만드는 것 자체를 늦추어 보자.
    // 이방식은 아주 좋다. 왜냐면 필요한 action을 넘겨주고 필요할때 호출하면 되니까.
    // 뒤에 왜 좋은지 알려주겠다.

    // 이제 아래 코드는 실제 호출(subscribe)할때 까지 실제 동작하지 않는다.
    // 이게 아니면 observable을 만들면서 query가 실행되서 observable에 할당될것이다.
    public Observable<Person> listPeople4() {
        return defer(() ->
                fromIterable(query("SELECT * FROM people")));
    }














    // 이게왜 유용할까?
    // 얼마나 유용할까?
    // 리소스를 언제, 어떤순서로 가져올지 우리는 신경쓸 필요가 없어진다.
    class Book { public String title; }
    Book recommend(Person person) { throw new UnknownError("unknown error"); }
    Book bestSeller() { return new Book(); }
    void display(String title) {}


















    // FIXME 흔히 우리가 너와 내가 만드는 코드이다.
    // 역시나 자바의 고질적인 문제는 try catch 문이 실제 로직을 방해한다.
    void bestBookFor(Person person) {
        Book book;
        try {
            book = recommend(person);
        } catch (Exception e) {
            book = bestSeller();
        }
        display(book.title);
    }


    Observable<Book> recommend2(Person person) { return error(new UnknownError("unknown error")); }
    Observable<Book> bestSeller2() { return just(new Book()); }

    // 이코드를 보고 놀랐다
    // 아주 선언적이며 직관적이다.
    void bestBestFor2(Person person) {
        Observable<Book> recommend = recommend2(person);
        Observable<Book> bestSeller = bestSeller2();
        Observable<Book> book = recommend.onErrorResumeNext(bestSeller);
        Observable<String> title = book.map(b -> b.title);
        title.subscribe(this::display);
    }

    // 위의 코드는 좀 길게 되었고 실제 구현하면
    // 훨씬더 깔끔하게 된다.
    // 에러일때만 best seller 호출한다. lazy하지 않으면 best seller는 에러건 아니건 일단 무조건 호출될것이다.
    void bestBestFor3(Person person) {
        recommend2(person)
        .onErrorResumeNext(bestSeller2())
        .map(b -> b.title)
        .subscribe(this::display);
    }




    // onErrorResumeNext => 이것이 Rx에서 try-catch를 구현하는 방식이다!!!
    // try-catch에 대해서는 276쪽에 좀더 자세히 나온다고 한다.



    // ## Observable 구성하기
    // 이제 Observable이 얼마나 우리의 삶을 편리하게 해줄건지 볼수 있다.

    // 위의 쿼리 SELECT * FROM PEOPLE 은 좋은 쿼리가 아니다. 데이터가 많으면 위험한 쿼리이다.

    // 그래서 우리는 보통 이렇게 코드를 만든다.
    int PAGE_SIZE = 20;
    List<Person> listPeoplePage(int page) {
        return query(String.format("SELECT * FROM PEOPLE LIMIT %d OFFSET %d",  PAGE_SIZE, PAGE_SIZE * page));
    }


    // 그리고 보통 이렇게 구현한다. 나도 그랬다. Rx를 만나기 전까지.
    // 어설프게 구현하긴 했지만 데이터가 부족하면 다시 호출헤서 채우고 하는 형태의 코드는 많았다.
    // 하지만 정형화 되어 있지 않다.
    // Source.scala
    List<Person> getFriends() {
       List<Person> friends = listPeoplePage(0);
       List<Person> agedFriends = friends.stream().filter(friend -> friend.age > 20).collect(Collectors.toList());
       if(agedFriends.size() < 10) {
           List<Person> friends2 = listPeoplePage(1);
           List<Person> agedFriends2 = friends.stream().filter(friend -> friend.age > 20).collect(Collectors.toList());
           agedFriends.addAll(agedFriends2);
           // 10명이 만족할때가지 추가 행위를 할수도 있음
       }
       // 그리고 결과값중에 10명을 반환한다.
       return agedFriends.subList(0, 10);
    }
    // Rx로 구현한다면?
    // 놀라지 마라.
    Observable<Person> allPeople(int initialPage) {
        return defer(() -> fromIterable(listPeoplePage(initialPage)))
                .concatWith(defer(() -> allPeople(initialPage + 1)));
    }
    // 이게 끝이다.
    // 신기 방기 하지 않는가?
    Observable<Person> getFriends2() {
        return allPeople(1)
                .filter(person -> person.age > 20)
                .take(10);
    }







    // Rx Observable과 비슷하게 finagle, finatra에서 사용할수 있는건 AsyncStream이라고 있다.
    // 이건 아래 AsyncStreamSpec 예를 참고하자.
    /**
     * @see twitternote.AsyncStreamSpec
     */


}


