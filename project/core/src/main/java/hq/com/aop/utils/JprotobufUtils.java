package hq.com.aop.utils;

import com.baidu.bjf.remoting.protobuf.Codec;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;

import java.io.IOException;

/**
 * @title : jprotobuf序列化工具
 * @describle :
 * <p>
 * note:
 * 1. 属性注解 eg: @Protobuf(fieldType = FieldType.STRING, order = 1, required =true)
 * 2. 适用分布式服务间通信数据传输
 * </p>
 * Create By yinhaiquan
 * @date 2017/5/26 9:55 星期五
 */
public final class JprotobufUtils {
    private static ThreadLocal<Codec> codec = new ThreadLocal<>();

    private JprotobufUtils(Class cls) {
        if (StringUtils.isEmpty(codec.get())) {
            codec.set(ProtobufProxy.create(cls));
        }
    }

    /**
     * 获取序列化对象Codec
     *
     * @param cls
     * @return
     */
    public final static JprotobufUtils getInstance(Class cls) {
        return new JprotobufUtils(cls);
    }

    /**
     * 序列化
     *
     * @param obj
     * @return
     * @throws IOException
     */
    public final byte[] serialization(Object obj) throws IOException {
        return codec.get().encode(obj);
    }

    /**
     * 反序列化
     *
     * @param obj
     * @return
     * @throws IOException
     */
    public final Object deserialization(byte[] obj) throws IOException {
        return codec.get().decode(obj);
    }
}
