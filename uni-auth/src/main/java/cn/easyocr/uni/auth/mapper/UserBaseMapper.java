package cn.easyocr.uni.auth.mapper;

import cn.easyocr.uni.auth.po.UserBase;
import cn.easyocr.uni.auth.query.UserBaseQuery;

import java.util.List;

/**
 * @author : feiya
 * @date : 2023/6/24
 * @description :
 */
public interface UserBaseMapper {
    void insert(UserBase userBase);

    List<UserBase> find(UserBaseQuery userBaseQuery);
}
