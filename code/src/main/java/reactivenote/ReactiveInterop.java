package reactivenote;

import akka.NotUsed;
import akka.actor.ActorSystem;
import akka.stream.javadsl.Sink;
import akka.stream.javadsl.Source;
import com.linecorp.armeria.common.HttpData;
import com.linecorp.armeria.common.HttpResponse;
import com.mongodb.reactivestreams.client.*;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Observable;
import org.bson.Document;
import reactor.core.publisher.Flux;

import static akka.stream.javadsl.AsPublisher.WITHOUT_FANOUT;

public class ReactiveInterop {
    public static void main(String[] args) {

        final ActorSystem system = ActorSystem.apply();
        MongoClient mongoClient = MongoClients.create();

        MongoDatabase database = mongoClient.getDatabase("mydb");
        MongoCollection<Document> collection = database.getCollection("test");
        final FindPublisher<Document> mongoDBUsers = collection.find();
        final Observable<Integer> rxJavaAges = Observable.fromPublisher(mongoDBUsers)
                .map(document -> document.getInteger("age"));
        final Source<Integer, NotUsed> akkaAdult =
                Source.fromPublisher(rxJavaAges.toFlowable(BackpressureStrategy.DROP))
                        .filter(age -> age > 19);
        final Flux<HttpData> rectorFlux = Flux.from(akkaAdult.runWith(Sink.asPublisher(WITHOUT_FANOUT), null))
                .map(age -> HttpData.ofAscii(age.toString()));
        HttpResponse.of(rectorFlux);

    }

}
