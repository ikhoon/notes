package algo;

// 포기
// 너무 복잡하다
// 지금 공부하기엔 시간이 너무 걸림
public class MedianSortedArrays {

    public double findMedianSortedArrays(int[] nums1, int[] nums2) {

        // 양쪽의 갯수 저장

        // 제일 큰쪽과 작은쪽을 비교

        // 한쪽의 끝이 다른쪽의 시작보다 작으면 땡큐

        // 일단 가운데를 찾는다
        // 아 알았다

        // nums1의 중간을 찾는다
        // nums2의 중간을 찾는다
        // nums2의 중간이 nums1의 끝보다 크면
        // 큰쪽의 왼쪽부분, 작은쪽의

        return findMedianSortedArrays(nums1, 0, nums1.length - 1, nums2, 0, nums2.length);
    }


    public double findMedianSortedArrays(int[] nums1, int x1, int y1, int[] nums2, int x2, int y2) {
        int mid1 = nums1[mid(x1, y1)];
        int first1 = nums1[x1];
        int last1 = nums1[y1];

        int mid2 = nums2[mid(x1, y1)];
        int first2 = nums2[x1];
        int last2 = nums2[y1];

        return 0d;
    }

    private int mid(int x, int y) {
        return x + y / 2;
    }

}
