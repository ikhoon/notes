package algo;

/**
 * https://leetcode.com/problems/container-with-most-water/
 */
public class ContainerWithMostWater {
    public int maxArea(int[] height) {

        // 양쪽 끝을 잡는다.
        // 길이가 더 짧은 쪽을 줄인다.
        // maxArea는 계속 추적하면서 갱신한다.
        int l = 0;
        int r = height.length - 1;
        int max = 0;
        while (l < r) {
            max = Math.max(area(height, l, r), max);
            if(height[l] < height[r]) {
                l += 1;
            }
            else {
                r -= 1;
            }
        }
        return max;
    }

    int area(int[] height, int i, int j) {
        return Math.min(height[i], height[j]) * (j - i);
    }
}

