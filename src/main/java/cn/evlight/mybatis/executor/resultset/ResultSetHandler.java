package cn.evlight.mybatis.executor.resultset;

import java.sql.Statement;
import java.util.List;

/**
 * @Description:
 * @Author: evlight
 * @Date: 2024/7/27
 */
public interface ResultSetHandler {
    <E> List<E> handle(Statement statement);
}
