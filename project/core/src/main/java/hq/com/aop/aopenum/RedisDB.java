package hq.com.aop.aopenum;

/**
 * @title : RedisDB分布
 * @describle :
 * <p>
 *     <b>note:</b>
 *     适用于redis单节点，HA，双机热备场景.
 * </p>
 * Create By yinhaiquan
 * @date 2017/7/4 11:58 星期二
 */
public enum RedisDB {
    DB0(0),
    DB1(1),
    DB2(2),
    DB3(3),
    DB4(4),
    DB5(5),
    DB6(6),
    DB7(7),
    DB8(8),
    DB9(9),
    DB10(10),
    DB11(11),
    DB12(12),
    DB13(13),
    DB14(14),
    DB15(15);
    private int dbIndex;

    private RedisDB(int dbIndex) {
        this.dbIndex = dbIndex;
    }

    public int getDbIndex() {
        return dbIndex;
    }

    public void setDbIndex(int dbIndex) {
        this.dbIndex = dbIndex;
    }
}
