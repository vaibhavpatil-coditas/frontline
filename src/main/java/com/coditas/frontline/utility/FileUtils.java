package com.coditas.frontline.utility;

import com.coditas.frontline.constants.ExceptionMessage;
import com.coditas.frontline.exception.InvalidFileFormatException;

import java.io.ByteArrayOutputStream;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class FileUtils {
    private FileUtils(){}
    public static byte[] compressFile(byte[] data) {
        Deflater deflater = new Deflater();
        deflater.setLevel(Deflater.BEST_COMPRESSION);
        deflater.setInput(data);
        deflater.finish();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] tmp = new byte[4 * 1024];
        while (!deflater.finished()) {
            int size = deflater.deflate(tmp);
            outputStream.write(tmp, 0, size);
        }
        try {
            outputStream.close();
        } catch (Exception ignored) {
            throw new InvalidFileFormatException(ExceptionMessage.FILE_FORMAT_NOT_VALID);
        }
        return outputStream.toByteArray();
    }


    public static byte[] decompressFileData(byte[] data) {
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] tmp = new byte[4 * 1024];
        try {
            while (!inflater.finished()) {
                int count = inflater.inflate(tmp);
                outputStream.write(tmp, 0, count);
            }
            outputStream.close();
        } catch (Exception ignored) {
            throw new InvalidFileFormatException(ExceptionMessage.FILE_FORMAT_NOT_VALID);
        }
        return outputStream.toByteArray();
    }
}
