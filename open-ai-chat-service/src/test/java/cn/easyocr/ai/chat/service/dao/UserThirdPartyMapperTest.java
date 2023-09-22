package cn.easyocr.ai.chat.service.dao;

import cn.easyocr.ai.chat.service.BaseTest;
import cn.easyocr.common.utils.TimeUtil;
import cn.easyocr.uni.auth.enums.LoginPlatform;
import cn.easyocr.uni.auth.mapper.UserBaseMapper;
import cn.easyocr.uni.auth.mapper.UserThirdPartyMapper;
import cn.easyocr.uni.auth.po.UserThirdParty;
import cn.easyocr.uni.auth.query.UserThirdPartyQuery;
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
public class UserThirdPartyMapperTest extends BaseTest {
    private static long curId = 0;
    private static long curTime = System.currentTimeMillis();
    @Autowired
    private UserThirdPartyMapper userThirdPartyMapper;

    @Autowired
    private UserBaseMapper userBaseMapper;

    @Autowired
    private SnowFlakeUidCreator snowFlakeUidCreator;

    @Test
    @Order(1)
    public void testInset() {
        UserThirdParty userThirdParty = new UserThirdParty();
        curId = snowFlakeUidCreator.genUid();
        String uniId = String.valueOf(snowFlakeUidCreator.genUid());
        userThirdParty.setUserId(curId);
        userThirdParty.setUnionId(uniId);
        userThirdParty.setOpenId(uniId);
        userThirdParty.setPrivilege("setPrivilege" + curTime);
        userThirdParty.setPlatform(LoginPlatform.WX_MINI.getPlatform());
        userThirdParty.setAppId(uniId);
        userThirdParty.setCreatedTime(new Date());
        userThirdParty.setUpdateTime(new Date());
        userThirdParty.setPtd(TimeUtil.getPtd());

        userThirdPartyMapper.insert(userThirdParty);
    }

    @Test
    @Order(2)
    public void testFind() {
        UserThirdPartyQuery userThirdPartyQuery = UserThirdPartyQuery.builder()
                .userId(curId)
                .platform(LoginPlatform.WX_MINI.getPlatform())
                .build();

        List<UserThirdParty> userThirdParties = userThirdPartyMapper.find(userThirdPartyQuery);
        Assertions.assertTrue(userThirdParties.size() > 0);
    }
}
