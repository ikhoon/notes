package algo;

import java.util.*;
import java.util.stream.Collectors;

public class CountTriplets {
    // Complete the countTriplets function below.
    static long countTriplets(List<Long> arr, long r) {
        Map<Long, Long> map2 = new HashMap<>();
        Map<Long, Long> map3 = new HashMap<>();

        Long count = 0L;

        for (Long x : arr) {
            count += map3.getOrDefault(x, 0L);
            Long c2 = map2.getOrDefault(x, 0L);
            map3.compute(x * r, (k, v) -> v != null ? v + c2 : c2);
            map2.compute(x * r, (k, v) -> v != null ? v + 1 : 1);
        }

        return count;
    }

    static long countTriplets2(List<Long> arr, long r) {
        HashMap<Long, List<Integer>> map = new HashMap<>();
        for (int i = 0; i < arr.size(); i++) {
            Long key = arr.get(i);
            List<Integer> value = map.getOrDefault(key, new ArrayList<>());
            value.add(i);
            map.put(key, value);
        }

        int total = 0;
        Set<List<Integer>> set = new HashSet<>();
        for (int i = 0; i < arr.size(); i++) {
            Long first = arr.get(i);
            Long second = first * r;
            int finalI = i;
            List<Integer> seconds = map.getOrDefault(second, Collections.emptyList())
                    .stream().filter(s -> s > finalI).collect(Collectors.toList());
            Long third = first * r * r;
            List<Integer> thirdsFull = map.getOrDefault(third, Collections.emptyList());

            for (Integer s : seconds) {
                List<Integer> thirds = thirdsFull.stream().filter(t -> t > s).collect(Collectors.toList());
                for (Integer t : thirds) {
                    if (s > i && t > s) {
                        total += 1;
                    }
                }
            }
        }
        return total;
    }
}
