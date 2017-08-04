package hq.com.base;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * <h1>JPA基础包</h1>
 * <p>所有JPA的Dao层均集成此接口</p>
 *
 * @param <T>
 * @author yinhaiquan
 *         <p>create by 2014-11-20</p>
 */
public interface BaseDao<T, Object extends Serializable> extends JpaRepository<T, Serializable> {

}
