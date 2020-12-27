package reactivenote;

import com.linecorp.armeria.common.HttpData;
import com.linecorp.armeria.common.HttpObject;
import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.common.HttpStatus;
import com.linecorp.armeria.common.ResponseHeaders;

import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Observable;

public class ArmeriaHttpResponse {
    public static void main(String[] args) {

        Observable<String> dataStream = Observable.just("a", "b", "c", "d", "e");
// Convert data to Armeria HttpData type
        Observable<HttpData> httpDataStream = dataStream.map(HttpData::ofUtf8);
// Prepare response header
        ResponseHeaders httpHeaders = ResponseHeaders.of(HttpStatus.OK);
// Concat http header and body stream
        Observable<HttpObject> httpResponseStream = Observable.concat(Observable.just(httpHeaders), httpDataStream);
// Convert Armeria Response Stream
        HttpResponse httpResponse = HttpResponse.of(httpResponseStream.toFlowable(BackpressureStrategy.BUFFER));
    }
}
