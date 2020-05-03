package reactivenote;

import static akka.stream.javadsl.AsPublisher.WITHOUT_FANOUT;

import com.mongodb.reactivestreams.client.MongoClients;

import com.linecorp.armeria.common.HttpData;
import com.linecorp.armeria.common.HttpResponse;

import akka.NotUsed;
import akka.actor.ActorSystem;
import akka.stream.Materializer;
import akka.stream.javadsl.Sink;
import akka.stream.javadsl.Source;
import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Observable;
import reactor.core.publisher.Flux;

public class ReactiveInterop {
    public static void main(String[] args) {

        final var system = ActorSystem.apply();
        var mongoClient = MongoClients.create();

        var database = mongoClient.getDatabase("mydb");
        var collection = database.getCollection("test");
        final var mongoDBUsers = collection.find();
        final Observable<Integer> rxJavaAges = Observable.fromPublisher(mongoDBUsers)
                .map(document -> document.getInteger("age"));
        final Source<Integer, NotUsed> akkaAdult =
                Source.fromPublisher(rxJavaAges.toFlowable(BackpressureStrategy.DROP))
                        .filter(age -> age > 19);
        final Flux<HttpData> rectorFlux = Flux.from(akkaAdult.runWith(Sink.asPublisher(WITHOUT_FANOUT), system))
                .map(age -> HttpData.ofAscii(age.toString()));
        HttpResponse.of(rectorFlux);

    }

}
