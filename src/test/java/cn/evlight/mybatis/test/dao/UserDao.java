package cn.evlight.mybatis.test.dao;

import cn.evlight.mybatis.test.po.User;

/**
 * @Description:
 * @Author: evlight
 * @Date: 2024/7/21
 */
public interface UserDao {

    User getById(Long id);

}
