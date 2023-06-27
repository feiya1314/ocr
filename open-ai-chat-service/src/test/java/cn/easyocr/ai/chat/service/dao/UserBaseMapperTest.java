package cn.easyocr.ai.chat.service.dao;

import cn.easyocr.ai.chat.service.BaseTest;
import cn.easyocr.common.utils.TimeUtil;
import cn.easyocr.uni.auth.mapper.UserBaseMapper;
import cn.easyocr.uni.auth.po.UserBase;
import cn.easyocr.uni.auth.query.UserBaseQuery;
import cn.easyocr.uni.auth.util.SnowFlakeUidCreator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

/**
 * @author : feiya
 * @date : 2023/6/24
 * @description :
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserBaseMapperTest extends BaseTest {
    private static long curId = 0;
    private static long curTime = System.currentTimeMillis();
    @Autowired
    private UserBaseMapper userBaseMapper;

    @Autowired
    private SnowFlakeUidCreator snowFlakeUidCreator;

    @Test
    @Order(1)
    public void testInset() {
        UserBase userBase = new UserBase();
        curId = snowFlakeUidCreator.genUid();
        userBase.setUserId(curId);
        userBase.setMobile("414545454");
        userBase.setEmail("414545454@email.com");
        userBase.setNickName("nick" + curTime);
        userBase.setUsername("username" + curTime);
        userBase.setPassword("setPassword" + curTime);
        userBase.setGender("1");
        userBase.setAvatar("setAvatar" + curTime);
        userBase.setCreatedTime(new Date());
        userBase.setUpdateTime(new Date());
        userBase.setPtd(TimeUtil.getPtd());

        userBaseMapper.insert(userBase);
    }

    @Test
    @Order(2)
    public void testFind() throws Exception {
        UserBaseQuery uerBaseQuery = UserBaseQuery.builder()
                .userId(curId)
                .build();

        List<UserBase> userBaseList = userBaseMapper.find(uerBaseQuery);
        Assertions.assertEquals(userBaseList.size(), 1);
    }
}
