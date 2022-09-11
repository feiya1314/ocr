package cn.easy.ocr.main.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * @author : feiya
 * @date : 2022/9/5
 * @description :
 */
@ActiveProfiles("dev")
// junit4 使用 @RunWith(SpringRunner.class)
// junit5 使用 @ExtendWith(SpringExtension.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class BaseTest {
}
