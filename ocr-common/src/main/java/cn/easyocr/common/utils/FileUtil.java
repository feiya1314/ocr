package cn.easyocr.common.utils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author : feiya
 * @date : 2022/9/6
 * @description :
 */
public class FileUtil {
    public static byte[] readResourceFile(String path) throws URISyntaxException, IOException {
        URL resource = Thread.currentThread().getContextClassLoader().getResource(path);
        URI uri = resource.toURI();

        return Files.readAllBytes(Paths.get(uri));
    }

    public static byte[] readAbsoluteFile(String path) throws IOException {
        return Files.readAllBytes(Paths.get(path));
    }

    public static void main(String[] args) throws Exception {
        byte[] file = readAbsoluteFile("E:\\Desktop\\logo\\easy.png");
        System.out.println();
    }
}
