package com.citi.cbk.util;

import lombok.extern.slf4j.Slf4j;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@Slf4j
public class IOUtil {
    public static String readString(String path) {
        try (FileChannel fileChannel = FileChannel.open(Paths.get(path), StandardOpenOption.READ);
        ) {
            StringBuilder builder = new StringBuilder();
            ByteBuffer byteBuffer = ByteBuffer.allocate(1000);
            while (fileChannel.read(byteBuffer) != -1) {
                byteBuffer.flip();
                builder.append(new String(byteBuffer.array()));
                byteBuffer.clear();
            }
            return builder.toString();
        } catch (FileNotFoundException e) {
            log.error("File Not Found", e);
            return "";
        } catch (IOException e) {
            log.error("IO error", e);
            return "";
        }
    }
}
