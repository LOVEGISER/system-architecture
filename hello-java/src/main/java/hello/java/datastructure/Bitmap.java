package hello.java.datastructure;

import java.util.Arrays;
import java.util.BitSet;

/**
 * 以bit为存储单位的数据结构
 * 对于给定第i位，1表示true，0表示false
 * 对于只需进行布尔存取的情况来说，是非常节省空间且高效的方案
 * TODO：任意下标指定，动态扩容?
 * @author SimpleIto
 * @date 2019-04-19
 */
public class Bitmap {

    private byte[] bytes;
    private int length;
    /**
     * @param length 位图长度，实际可操作下标为[0,length)
     */
    public Bitmap(int length){
        this.length = length;
        bytes = new byte[length%8==0 ? length/8 : length/8+1];
    }

    /**
     * 设置指定位的值
     */
    public void set(int index, boolean value){
        if(value)
            //通过给定位index，先定位到对应byte
            //并根据value值进行不同位操作：
            //  1.如果value为true，则目标位应该做“或”运算。则需构建“目标位为1，其他为0”的操作数，为了只合理操作目标位，而不影响其他位
            //  2.如果value为false，则目标位应该做“与”运算。则需构建“目标位为0，其他为1”的操作数
            bytes[index >> 3] |= 1 << (index & 7); // bytes[index/8] = bytes[index/8] | (0b0001 << (index%8))
        else
            bytes[index >> 3] &= ~(1 << (index & 7));
    }

    /**
     * 获取指定位的值
     */
    public boolean get(int index){
        int i = index & 7;
        //构建到index结束的低位掩码并做&运算（为了将高位置0），然后将结果一直右移直到目标位(index位)移到第1位
        //然后根据其值返回结果
        if((bytes[index >> 3] & (011111111>>>(7-i))) >> i == 0)
            return false;
        else
            return true;
    }


}