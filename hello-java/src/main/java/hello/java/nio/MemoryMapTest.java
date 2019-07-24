package hello.java.nio;

import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.CRC32;

/**
 * Created by alex on 2019/1/11.
 * 大多数操作系统都可以利用虚拟内存实现将一个文件或者文件的一部分"映射"到内存中。然后，这个文件就可以当作是内存数组来访问，这比传统的文件要快得多。
 * 内存映射文件的一个关键优势是操作系统负责真正的读写，即使你的程序在刚刚写入内存后就挂了，操作系统仍然会将内存中的数据写入文件系统。另外一个更突出的优势是共享内存，内存映射文件可以被多个进程同时访问，起到一种低时延共享内存的作用。
 * 那么，如何将一个文件映射到内存呢？
 *
 * step1:从文件中获得一个通道（channel）
 *  FileChannel channel = FileChannel.open(path,options);
 *  这里options指定映射模式，支持的模式有三种：
 *
     * FileChannel.MapMode.READ_ONLY：所产生的缓冲区是只读的。
     * FileChannel.MapMode.READ_WRITE：所产生的缓冲区是可写的,任何修改都会在某个时刻写回到文件中,注意，其他映射同一个文件的程序可能不能立即看到这些修改，多个程序同时进行文件映射的确切行为是依赖于操作系统的。
     * FileChannel.MapMode.PRIVATE：所产生的缓冲区是可写的，但是任何修改对该缓冲区来说都是私有的，不会传播到文件中。
 *
 * step2:调用FileChannel的map方法
 * MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY,0,length);接下来通过计算一个40MB文件的CRC32校验和来比较传统的文件输入和内存映射文件的速度。
 *
 * 传统的文件输入包括：
     * 普通输入流（InputStream）
     * 带缓冲的输入流（BufferedInputStream）
     * 随机访问文件（RandomAccessFile）
 *
 */
public class MemoryMapTest {
    public static long checksumInputStream(Path filename) throws IOException {  //普通输入流
        try (InputStream in = Files.newInputStream(filename)) {
            CRC32 crc = new CRC32();
            int c;
            while ((c = in.read()) != -1)
                crc.update(c);
            return crc.getValue();
        }
    }

    public static long checksumBufferedInputStream(Path filename) throws IOException {  //带缓冲的输入流
        try (BufferedInputStream in = new BufferedInputStream(Files.newInputStream(filename))){
            CRC32 crc = new CRC32();
            int c;
            while ((c = in.read()) != -1)
                crc.update(c);
            return crc.getValue();
        }
    }

    public static long checksumRandomAccessFile(Path filename) throws IOException {  //随机访问文件
        try (RandomAccessFile file = new RandomAccessFile(filename.toFile(),"r")){
            CRC32 crc = new CRC32();
            long length = file.length();

            for (long p = 0; p < length; p++){
                file.seek(p);
                int c = file.readByte();
                crc.update(c);
            }
            return crc.getValue();
         }
    }

    public static long checksumMappedFile(Path filename) throws IOException {  //内存映射文件
        try (FileChannel channel = FileChannel.open(filename)){
            CRC32 crc = new CRC32();
            int length = (int)channel.size();
            MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY,0,length);

            for (int p = 0; p < length; p++){
                int c = buffer.get(p);
                crc.update(c);
            }
            return crc.getValue();
        }
    }

    public static void main(String[] args) throws IOException {
        System.out.println("Input Stream:");
        long start = System.currentTimeMillis();
        Path filename = Paths.get("/Users/wangleigis163.com/Downloads/SynalyzeItProTA_1.22.zip");
        long crcValue = checksumInputStream(filename);
        long end = System.currentTimeMillis();
        System.out.println(Long.toHexString(crcValue));
        System.out.println((end - start) + " milliseconds");
        System.out.println();

        System.out.println("Buffered Input Stream:");
        start = System.currentTimeMillis();
        crcValue = checksumBufferedInputStream(filename);
        end = System.currentTimeMillis();
        System.out.println(Long.toHexString(crcValue));
        System.out.println((end - start) + " milliseconds");
        System.out.println();

        System.out.println("Random Access File:");
        start = System.currentTimeMillis();
        crcValue = checksumRandomAccessFile(filename);
        end = System.currentTimeMillis();
        System.out.println(Long.toHexString(crcValue));
        System.out.println((end - start) + " milliseconds");
        System.out.println();

        System.out.println("Mapped File:");
        start = System.currentTimeMillis();
        crcValue = checksumMappedFile(filename);
        end = System.currentTimeMillis();
        System.out.println(Long.toHexString(crcValue));
        System.out.println((end - start) + " milliseconds");
    }
}
