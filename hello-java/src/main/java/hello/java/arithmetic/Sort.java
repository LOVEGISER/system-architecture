package hello.java.arithmetic;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.Collections;

public class Sort {
    public static int[]  bubbleSort(int[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {//外层循环控制排序趟数
            for (int j = 0; j < arr.length - 1 - i; j++) {//内层循环控制每一趟排序多少次
                if (arr[j] > arr[j + 1]) {
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }
        return arr;
    }

    public static int[] insertort(int arr[])
    {
        for(int i =1; i<arr.length;i++)
        {
            //插入的数
            int insertVal = arr[i];
            //被插入的位置(准备和前一个数比较)
            int index = i-1;
            //如果插入的数比被插入的数小
            while(index>=0&&insertVal<arr[index])
            {
                //将把arr[index] 向后移动
                arr[index+1]=arr[index];
                //让index向前移动
                index--;
            }
            //把插入的数放入合适位置
            arr[index+1]=insertVal;
        }
        return arr;
    }

    public  static  int[] quickSort(int[] arr,int low,int high){
        int start = low;
        int end = high;
        int key = arr[low];
        while(end>start){
            //从后往前比较
            while(end>start&&arr[end]>=key)
                //如果没有比关键值小的，比较下一个，直到有比关键值小的交换位置，然后又从前往后比较
                end--;
            if(arr[end]<=key){
                int temp = arr[end];
                arr[end] = arr[start];
                arr[start] = temp;
            }
            //从前往后比较
            while(end>start&&arr[start]<=key)
//如果没有比关键值大的，比较下一个，直到有比关键值大的交换位置
                start++;
            if(arr[start]>=key){
                int temp = arr[start];
                arr[start] = arr[end];
                arr[end] = temp;
            }
            //此时第一次循环比较结束，关键值的位置已经确定了。左边的值都比关键值小，右边的值都比关键值大，但是两边的顺序还有可能是不一样的，进行下面的递归调用
        }
        //递归
        if(start>low) quickSort(arr,low,start-1);//左边序列。第一个索引位置到关键值索引-1
        if(end<high) quickSort(arr,end+1,high);//右边序列。从关键值索引+1到最后一个
        return arr;
    }



    public static  int[] shellSort(int[] arr) {
        int dk = arr.length/3 +1;
        while( dk == 1  ){
            ShellInsertSort(arr, dk);
            dk = dk/3 + 1;
        }
        return arr;
    }
    public static void ShellInsertSort(int[] a, int dk) {
//类似插入排序，只是插入排序增量是1，这里增量是dk,把1换成dk就可以了
        for(int i=dk;i<a.length;i++){
            if(a[i]<a[i-dk]){
                int j;
                int x=a[i];//x为待插入元素
                a[i]=a[i-dk];
                for(j=i-dk;  j>=0 && x<a[j];j=j-dk){
//通过循环，逐个后移一位找到要插入的位置。
                    a[j+dk]=a[j];
                }
                a[j+dk]=x;//插入
            }
        }
    }


    public static int[] mergeSort(int[] data) {
        sort(data, 0, data.length - 1);
        return data;
    }
    //对左右两边数据进行递归
    public static void sort(int[] data, int left, int right) {
        if (left >= right)
            return;
        // 找出中间索引
        int center = (left + right) / 2;
        // 对左边数组进行递归
        sort(data, left, center);
        // 对右边数组进行递归
        sort(data, center + 1, right);
        // 合并
        merge(data, left, center, right);
    }
    /**
     * 将两个数组进行归并，归并前面2个数组已有序，归并后依然有序
     * @param data:数组对象;left:左数组的第一个元素的索引;
     *            center左数组的最后一个元素的索引，center+1是右数组第一个元素的索引
     *            right:右数组最后一个元素的索引
     */
    public static void merge(int[] data, int left, int center, int right) {
        // 临时数组
        int[] tmpArr = new int[data.length];
        // 右数组第一个元素索引
        int mid = center + 1;
        // third 记录临时数组的索引
        int third = left;
        // 缓存左数组第一个元素的索引
        int tmp = left;
        while (left <= center && mid <= right) {
            // 从两个数组中取出最小的放入临时数组
            if (data[left] <= data[mid]) {
                tmpArr[third++] = data[left++];
            } else {
                tmpArr[third++] = data[mid++];
            }
        }
        // 剩余部分依次放入临时数组（实际上两个while只会执行其中一个）
        while (mid <= right) {
            tmpArr[third++] = data[mid++];
        }
        while (left <= center) {
            tmpArr[third++] = data[left++];
        }
        // 将临时数组中的内容拷贝回原数组中
        // （原left-right范围的内容被复制回原数组）
        while (tmp <= right) {
            data[tmp] = tmpArr[tmp++];
        }
    }

    public static int[]  bucketSort(int[] arr){

        int max = Integer.MIN_VALUE;
        int min = Integer.MAX_VALUE;
        for(int i = 0; i < arr.length; i++){
            max = Math.max(max, arr[i]);
            min = Math.min(min, arr[i]);
        }
        //创建桶
        int bucketNum = (max - min) / arr.length + 1;
        ArrayList<ArrayList<Integer>> bucketArr = new ArrayList(bucketNum);
        for(int i = 0; i < bucketNum; i++){
            bucketArr.add(new ArrayList<Integer>());
        }
        //将每个元素放入桶
        for(int i = 0; i < arr.length; i++){
            int num = (arr[i] - min) / (arr.length);
            bucketArr.get(num).add(arr[i]);
        }
        //对每个桶进行排序
        for(int i = 0; i < bucketArr.size(); i++){
            Collections.sort(bucketArr.get(i));
        }
        return arr;
    }


    //array：数组 maxigit:数组最大位数
    private static int[] radixSort(int[] array,int maxDigit)
    {
        //数组最大位数的数据上限,比如3位数的最大上限为1000
        double max = Math.pow(10,maxDigit+1);
        int n=1;//代表位数对应的数：1,10,100...
        int k=0;//保存每一位排序后的结果用于下一位的排序输入
        int length=array.length;
        //排序桶用于保存每次排序后的结果，这一位上排序结果相同的数字放在同一个桶里
        int[][] bucket=new int[10][length];
        int[] order=new int[length];//用于保存每个桶里有多少个数字
        while(n<max)
        {
            for(int num:array) //将数组array里的每个数字放在相应的桶里
            {
                int digit=(num/n)%10;
                bucket[digit][order[digit]]=num;
                order[digit]++;
            }
            for(int i=0;i<length;i++)//将前一个循环生成的桶里的数据覆盖到原数组中用于保存这一位的排序结果
            {
                if(order[i]!=0)//这个桶里有数据，从上到下遍历这个桶并将数据保存到原数组中
                {
                    for(int j=0;j<order[i];j++)
                    {
                        array[k]=bucket[i][j];
                        k++;
                    }
                }
                order[i]=0;//将桶里计数器置0，用于下一次位排序
            }
            n*=10;
            k=0;//将k置0，用于下一轮保存位排序结果
            System.out.println("11");
        }
        return array;
    }

    public static void main(String[] args) {
        int[] myList = {1, 2, 3, 4, 6, 7};
       // System.out.println(JSON.toJSONString(bubbleSort(myList)));
       // System.out.println(JSON.toJSONString(insertort(myList)));
       // System.out.println(JSON.toJSONString(quickSort(myList,0,myList.length-1)));
       // System.out.println(JSON.toJSONString(shellSort(myList)));
        int[] A=new int[]{73,22, 93, 43, 55, 14, 28, 65, 309, 81};

        System.out.println(JSON.toJSONString( radixSort(A, 3)));

    }
}
