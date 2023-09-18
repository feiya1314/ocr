package cn.easyocr.uni.auth.util;

import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

/**
 * @author : feiya
 * @date : 2023/9/18
 * @description :
 */
@Slf4j
public class ImageUtil {
    public static String compressImage(byte[] image, int width, int height) {
        ByteArrayInputStream intputStream = new ByteArrayInputStream(image);
        Thumbnails.Builder<? extends InputStream> builder = Thumbnails.of(intputStream)
                .size(width, height);
        try {
            BufferedImage bufferedImage = builder.asBufferedImage();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "jpg", baos);
            byte[] byteArray = baos.toByteArray();

            return Base64.getEncoder().encodeToString(byteArray);
        } catch (IOException e) {
            log.error("compressImage error", e);
        }

        return Base64.getEncoder().encodeToString(image);
    }
}
