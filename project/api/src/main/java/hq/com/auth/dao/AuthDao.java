package hq.com.auth.dao;

import hq.com.auth.po.InterfaceAuth;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @title : 接口权限安全管理DB层
 * @describle :
 * <p>
 *     <b>note:</b>
 *     1. 验签权限配置
 *     2. 令牌权限配置(接口登录验证token权限)
 * </p>
 * Create By yinhaiquan
 * @date 2017/9/7 14:37 星期四
 */
public interface AuthDao {
    /**
     * 查询接口权限配置列表
     *
     * @param name       类名/方法名
     * @param startDate  开始时间
     * @param endDate    结束时间
     * @return
     */
    public List<InterfaceAuth> findInterfaceAuthList(@Param("name") String name,
                                                     @Param("startDate") String startDate,
                                                     @Param("enDate") String endDate);

    /**
     * 更新验签权限
     *
     * @param iSign 是否验签 true 1 是 false 0 否
     * @param id    接口权限配置ID
     */
    public void updateISign(@Param("iSign") boolean iSign,
                            @Param("id") int id);

    /**
     * 更新令牌权限
     *
     * @param iSLogin 是否验证令牌 true 1 是 false 0 否
     * @param id      接口权限配置ID
     */
    public void updateISLogin(@Param("iSLogin") boolean iSLogin,
                              @Param("id") int id);
}
