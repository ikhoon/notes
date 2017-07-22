package learningtypelevel;

import java.util.List;

/**
 * Created by ikhoon on 2016. 8. 28..
 */
public class MethodEquivalenceJava {
    // #2
    public static void copyToZero(final List<?> xs) {
//        xs.add(xs.get(0));  // 이것또한 컴파일이 안된다.
    }

    // Error:(8, 11) java: no suitable method found for add(capture#1 of ?)
    // method java.util.Collection.add(capture#2 of ?) is not applicable
    //         (argument mismatch; java.lang.Object cannot be converted to capture#2 of ?)
    // method java.util.List.add(capture#2 of ?) is not applicable
    //         (argument mismatch; java.lang.Object cannot be converted to capture#2 of ?)

    // 에러가 무슨말을 하는지 잘 이해되지 않는다.
    // xs.get(0)한건 Object Type이고 java.util.List.add(? e) wildcard 타입이라 변형이 되지 않는다는 이야기 같다.
    // 왜 xs.get(0)한건 object type인가? type erasure때문인가? wildcard 때문인가?
    // wildcard type 이면 object type이라도 받아들여하는것 아닌가?


    // #4
    // 아래 코드는 이제 컴파일된다.
    // Unit Test도 잘 통과한다.
    public static void copyToZeroE(final List<?> xs) {
        copyToZeroP(xs);
    }

    public static <T> void copyToZeroP(final List<T> xs) {
        final T zv = xs.get(0);
        xs.add(zv);
    }


    // #8
    // 스칼라에서 컴파일 안되는 코드가 java에서는 가능하다.
    public static <T> T holdOnNow(T t) {
        return null;
    }


}
