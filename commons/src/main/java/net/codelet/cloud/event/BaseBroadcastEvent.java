package net.codelet.cloud.event;

/**
 * 广播事件基类。
 * @param <P> 携带数据的范型
 */
public abstract class BaseBroadcastEvent<P> extends BaseRemoteApplicationEvent<P> {
    private static final long serialVersionUID = 3310751945116332966L;
}
