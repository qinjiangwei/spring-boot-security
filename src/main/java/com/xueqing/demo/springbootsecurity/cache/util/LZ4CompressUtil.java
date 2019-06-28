package com.xueqing.demo.springbootsecurity.cache.util;

import net.jpountz.lz4.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class LZ4CompressUtil {

    /**
     *
     * @param srcBytes
     * @param blockSize 一次压缩的大小（64bit - 32M）
     * @return
     */
    public static byte[] lz4Compress(byte[] srcBytes, int blockSize){
        LZ4Factory lz4Factory = LZ4Factory.fastestInstance();
        LZ4Compressor lz4Compressor = lz4Factory.fastCompressor();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (LZ4BlockOutputStream lz4BlockOutputStream = new LZ4BlockOutputStream(byteArrayOutputStream, blockSize, lz4Compressor)){
            lz4BlockOutputStream.write(srcBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return byteArrayOutputStream.toByteArray();
    }

    /**
     *
     * @param compressorByte
     * @param blockSize 一次压缩大小（64bit - 32M）
     * @return
     * @throws IOException
     */
    public static byte[] lz4Decompress(byte[] compressorByte, int blockSize) throws IOException {
        LZ4Factory factory = LZ4Factory.fastestInstance();
        ByteArrayOutputStream baos = new ByteArrayOutputStream(blockSize);
        LZ4FastDecompressor decompresser = factory.fastDecompressor();
        LZ4BlockInputStream lzis = new LZ4BlockInputStream(new ByteArrayInputStream(compressorByte), decompresser);
        int count;
        byte[] buffer = new byte[blockSize];
        while ((count = lzis.read(buffer)) != -1) { baos.write(buffer, 0, count);
        } lzis.close();
        return baos.toByteArray();
    }
}
