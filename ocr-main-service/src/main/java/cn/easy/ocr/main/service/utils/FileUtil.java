package cn.easy.ocr.main.service.utils;

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
}
