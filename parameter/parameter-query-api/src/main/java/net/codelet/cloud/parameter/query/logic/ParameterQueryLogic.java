package net.codelet.cloud.parameter.query.logic;

/**
 * 业务参数查询业务逻辑接口。
 */
public interface ParameterQueryLogic {

    /**
     * 取得业务参数值。
     * @param parameterName 参数名
     * @return 参数值
     */
    String getValue(String parameterName);

    /**
     * 取得业务参数值。
     * @param appId         应用 ID
     * @param parameterName 参数名
     * @return 参数值
     */
    String getValue(String appId, String parameterName);
}
