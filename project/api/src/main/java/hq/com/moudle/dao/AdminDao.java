package hq.com.moudle.dao;

import hq.com.moudle.po.Admin;

import java.util.List;
import java.util.Map;

/**
 * @Describle:
 * @Author: YinHq
 * @Date: Created By 下午 8:56 2017/5/29 0029
 * @Modified By
 */
public interface AdminDao {
    public List<Admin> findUsers(Map<String, Object> map);

    public Admin find(int id);
}
