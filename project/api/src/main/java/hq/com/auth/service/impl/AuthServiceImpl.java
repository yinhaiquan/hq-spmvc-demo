package hq.com.auth.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import hq.com.aop.utils.DateUtils;
import hq.com.aop.vo.OutParam;
import hq.com.aop.vo.Pager;
import hq.com.auth.dao.AuthDao;
import hq.com.auth.dto.InterfaceAuthDto;
import hq.com.auth.po.InterfaceAuth;
import hq.com.auth.service.AuthService;
import hq.com.auth.vo.InterfaceAuthInParam;
import hq.com.base.BaseService;
import hq.com.enums.MonkeyStatus;
import hq.com.enums.SystemCodeEnum;
import hq.com.exception.IllegalOptionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @title : 接口权限安全管理业务实现层
 * @describle :
 * <p>
 * Create By yinhaiquan
 * @date 2017/9/9 14:04 星期六
 */
@Service("authSvc")
public class AuthServiceImpl extends BaseService implements AuthService{
    private static Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);

    @Resource
    private AuthDao authDao;

    /**
     * 分页查询接口权限配置列表
     *
     * @param interfaceAuthInParam 入参
     * @return
     */
    @Override
    public OutParam findInterfaceAuthList(InterfaceAuthInParam interfaceAuthInParam) throws IllegalOptionException {
        OutParam op = new OutParam();
        try{
            isNull(interfaceAuthInParam);
            Pager pager = new Pager();
            List<InterfaceAuthDto> data = new ArrayList<>();
            Page page = PageHelper.startPage(interfaceAuthInParam.getPage(), interfaceAuthInParam.getPageSize(), true);
            setOrderBy(page,converTotableField(InterfaceAuthDto.class,interfaceAuthInParam.getOrder()),interfaceAuthInParam.getOrderType());
            List<InterfaceAuth> list = authDao.findInterfaceAuthList(interfaceAuthInParam.getName(),interfaceAuthInParam.getStartDate(),interfaceAuthInParam.getEnDate());
            for (InterfaceAuth ifa : list) {
                InterfaceAuthDto ifad = new InterfaceAuthDto();
                ifad.setClassName(ifa.getClassName());
                ifad.setMethodName(ifa.getMethodName());
                ifad.setId(ifa.getId());
                ifad.setiSign(ifa.isiSign());
                ifad.setLogin(ifa.isLogin());
                ifad.setCreateDate(DateUtils.dateToString(ifa.getCreateDate(),DateUtils.YYYY_MM_DD));
                ifad.setUpdateDate(DateUtils.dateToString(ifa.getUpdateDate(),DateUtils.YYYY_MM_DD));
                data.add(ifad);
            }
            pager.setRows(data);
            pager.setPages(page.getPages());
            pager.setTotal(page.getTotal());
            pager.setPageSize(page.getPageSize());
            op.setCode(SystemCodeEnum.SYSTEM_OK.getCode());
            op.setDesc(SystemCodeEnum.SYSTEM_OK.getDesc());
            op.setContent(pager);
        } catch (Exception e) {
            log.info("分页查询接口权限配置列表【findInterfaceAuthList】抛出异常:{}", e.getMessage());
            exception(e);
        }
        return op;
    }

    /**
     * 更新验签权限/令牌权限
     *
     * @description:
     *               type: 1 验签  2 令牌
     *               tag : 1 true 0 false
     * @param interfaceAuthInParam 入参
     * @return
     */
    @Override
    public OutParam updateAuth(InterfaceAuthInParam interfaceAuthInParam) throws IllegalOptionException {
        OutParam op = new OutParam();
        try{
            isNull(interfaceAuthInParam);
            MonkeyStatus ms = MonkeyStatus.newInstance(interfaceAuthInParam.getTag());
            switch(interfaceAuthInParam.getType()){
                case 1 :
                    authDao.updateISign(ms.getBool(),interfaceAuthInParam.getId());
                    break;
                case 2 :
                    authDao.updateISLogin(ms.getBool(),interfaceAuthInParam.getId());
                    break;
            }
            op.setCode(SystemCodeEnum.SYSTEM_OK.getCode());
            op.setDesc(SystemCodeEnum.SYSTEM_OK.getDesc());
        } catch (Exception e) {
            log.info("更新验签权限/令牌权限【updateAuth】抛出异常:{}", e.getMessage());
            exception(e);
        }
        return op;
    }
}
