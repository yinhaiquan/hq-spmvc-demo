package hq.com.base;

import com.github.pagehelper.Page;
import hq.com.aop.annotation.TableField;
import hq.com.aop.utils.StringUtils;
import hq.com.auth.po.InterfaceAuth;
import hq.com.enums.Sort;
import hq.com.enums.SystemCodeEnum;
import hq.com.exception.IllegalOptionException;

import java.lang.reflect.Field;

/**
 * @title : 通用SERVICE
 * @describle :
 * <p>
 * Create By yinhaiquan
 * @date 2017/9/9 14:23 星期六
 */
public abstract class BaseService {

    public void isNull(Object object) throws IllegalOptionException {
        if (StringUtils.isEmpty(object)){
            throw new IllegalOptionException(SystemCodeEnum.SYSTEM_PARAMS_NOT_EXIST);
        }
    }

    /**
     * 设置排序
     * @param page
     * @param order
     * @param sort
     */
    public void setOrderBy(Page page,String order,String sort){
        if (StringUtils.isNotEmpty(order)) {
            StringBuffer sb = new StringBuffer(order);
            sb.append(StringUtils.BLANK).append(Sort.newInstance(sort).getCode());
            page.setOrderBy(sb.toString());
        }
    }

    /**
     * 实体字段转换表字段
     * @param cls          对应表实体类class
     * @param entityField  实体字段
     * @return
     */
    public String converTotableField(Class<?> cls,String entityField) throws IllegalOptionException {
        try {
            Field field = cls.getDeclaredField(entityField);
            TableField tableField = field.getAnnotation(TableField.class);
            return tableField.value();
        } catch (Exception e) {
            throw new IllegalOptionException(SystemCodeEnum.SYSTEM_FIELD_MAPPING_NOT_FOUND);
        }
    }

    /**
     * 异常处理
     * @param e
     * @throws IllegalOptionException
     */
    public void exception(Exception e) throws IllegalOptionException {
        if (e instanceof IllegalOptionException){
            throw new IllegalOptionException(((IllegalOptionException) e).getS_());
        }
        throw new IllegalOptionException(SystemCodeEnum.SYSTEM_ERROR);
    }
}
