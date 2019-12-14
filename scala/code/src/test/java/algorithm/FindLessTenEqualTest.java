package algorithm;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ikhoon on 2019-01-18.
 */
public class FindLessTenEqualTest {

    @Test
    public void testRun() {
        int foo = FindLessTenEqual.foo();
        System.out.println(foo);
        System.out.println("Hello world");

    }

    @Test
    public void test() {
        List<List<Integer>> routeList = new ArrayList<>();
        append(routeList, 1, 5);
        append(routeList, 2, 7);
        append(routeList, 3, 14);
        append(routeList, 4, 15);
        append(routeList, 5, 17);
        append(routeList, 6, 19);
        List<Integer> integers = lessThenMax(routeList, 16, 0, routeList.size() -1);
        System.out.println(integers);
    }

    @Test
    public void test1() {
        List<List<Integer>> routeList = new ArrayList<>();
        append(routeList, 1, 5);
        append(routeList, 2, 7);
        append(routeList, 3, 14);
        append(routeList, 4, 15);
        append(routeList, 5, 17);
        append(routeList, 6, 19);
        List<Integer> integers = lessThenMax(routeList, 14, 0, routeList.size() -1);
        System.out.println(integers);
    }


    @Test
    public void test2() {
        List<List<Integer>> routeList = new ArrayList<>();
        append(routeList, 1, 5);
        append(routeList, 2, 7);
        append(routeList, 3, 14);
        append(routeList, 4, 15);
        append(routeList, 5, 17);
        append(routeList, 6, 19);
        List<Integer> integers = lessThenMax(routeList, 16, 0, routeList.size() -1);
        System.out.println(integers);
    }

    @Test
    public void testr3() {
        List<List<Integer>> routeList = new ArrayList<>();
        append(routeList, 1, 5);
        append(routeList, 2, 7);
        append(routeList, 3, 14);
        append(routeList, 4, 15);
        append(routeList, 5, 17);
        append(routeList, 6, 19);
        List<Integer> integers = lessThenMax(routeList, 17, 0, routeList.size() -1);
        System.out.println(integers);
    }

    @Test
    public void testr4() {
        List<List<Integer>> routeList = new ArrayList<>();
        append(routeList, 1, 5);
        append(routeList, 2, 7);
        append(routeList, 3, 14);
        append(routeList, 4, 15);
        append(routeList, 5, 17);
        append(routeList, 6, 19);
        List<Integer> integers = lessThenMax(routeList, 13, 0, routeList.size() -1);
        System.out.println(integers);
    }



    public static void append(List<List<Integer>> routes, Integer id, Integer dist) {
        ArrayList<Integer> tmp = new ArrayList<>();
        tmp.add(id);
        tmp.add(dist);
        routes.add(tmp);
    }

    private List<Integer> lessThenMax(
        List<List<Integer>> routeList,
        int targetDistance,
        int startIdx,
        int endIdx
    ) {
        int midIdx = getMidIdx(startIdx, endIdx);
        List<Integer> midRoute = routeList.get(midIdx);

        if (targetDistance == midRoute.get(1)) {
            return midRoute;
        } else if (startIdx == endIdx) {
            return routeList.get(startIdx);
        } else if (midRoute.get(1) < targetDistance) {
            return lessThenMax(routeList, targetDistance, midIdx + 1, endIdx);
        } else {
            return lessThenMax(routeList, targetDistance, startIdx, midIdx - 1);
        }
    }

    private int getMidIdx(int startIdx, int endIdx) {
        return (startIdx + endIdx) / 2;
    }

}