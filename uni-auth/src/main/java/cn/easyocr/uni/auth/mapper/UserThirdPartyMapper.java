package cn.easyocr.uni.auth.mapper;

import cn.easyocr.uni.auth.po.UserThirdParty;
import cn.easyocr.uni.auth.query.UserThirdPartyQuery;

import java.util.List;

/**
 * @author : feiya
 * @date : 2023/6/24
 * @description :
 */
public interface UserThirdPartyMapper {
    void insert(UserThirdParty userThirdParty);

    List<UserThirdParty> find(UserThirdPartyQuery userThirdPartyQuery);
}
