package hq.com.moudle.service;

import hq.com.aop.vo.OutParam;
import hq.com.moudle.vo.AdminInParam;

/**
 * @title :
 * @describle :
 * <p>
 * Create By yinhaiquan
 * @date 2017/8/3 12:36 星期四
 */
public interface AdminService {
    public OutParam findUsers(AdminInParam adminInParam);
}
