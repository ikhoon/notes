package learningtypelevel;

import java.util.List;

/**
 * Created by ikhoon on 2016. 9. 4..
 */
public class NestedExistentialJava {
    int llLength(final List<List<?>> xss) {
        return 0;
//        return llLengthTP(xss); // 컴파일 안됨
    }
//  Error:(8, 16) java: method llLengthTP in class NestedExistentialJava cannot be applied to given types;
//  required: java.util.List<java.util.List<T>>
//  found: java.util.List<java.util.List<?>>
//  reason: cannot infer type-variable(s) T
//  (argument mismatch; java.util.List<java.util.List<?>> cannot be converted to java.util.List<java.util.List<T>>)

    <T> int llLengthTP(final List<List<T>> xss) {
        return 0; // 타입에 대한 것만 확인해보기 위해서 만듬.
    }


}
