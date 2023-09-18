package cn.easyocr.uni.auth.compress;

import cn.easyocr.common.utils.FileUtil;
import net.coobird.thumbnailator.Thumbnails;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.util.Base64Utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author : feiya
 * @date : 2023/9/17
 * @description :
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CompressTest {
    @Test
    @Order(1)
    public void testGetAccessToken() throws Exception {
        byte[] file = FileUtil.readAbsoluteFile("C:\\Users\\yufei\\Pictures\\下载2.jpg");
        long start = System.currentTimeMillis();
        ByteArrayInputStream intputStream = new ByteArrayInputStream(file);
        Thumbnails.Builder<? extends InputStream> builder = Thumbnails.of(intputStream)
                .size(350, 350);
        System.out.println("time :" + (System.currentTimeMillis() - start));
        try {
            BufferedImage bufferedImage = builder.asBufferedImage();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            System.out.println("time :" + (System.currentTimeMillis() - start));
            ImageIO.write(bufferedImage, "jpg", baos);
            System.out.println("time :" + (System.currentTimeMillis() - start));
            byte[] byteArray = baos.toByteArray();
            System.out.println(Base64Utils.encodeToString(byteArray));
            System.out.println("time :" + (System.currentTimeMillis() - start));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        System.out.println("time :" + (System.currentTimeMillis() - start));
    }
}
