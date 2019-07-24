package hello.java.arithmetic;

public class Search {
    public static int binarySearch(int[] array, int a) {
        int low = 0;
        int high = array.length - 1;
        int mid;
        while (low <= high) {
            mid = (low + high) / 2;//中间位置
            if (array[mid] == a) {
                return mid;
            } else if (a > array[mid]) { //向右查找
                low = mid + 1;
            } else { //向左查找
                high = mid - 1;
            }
        }
        return -1;
    }



    public static void main(String[] args) {
        int[] myList = {1, 2, 3, 4, 6, 7};
        System.out.println(binarySearch(myList, 61));
    }
}
