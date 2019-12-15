package net.codelet.cloud.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * 数据传输对象基类。
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class BaseDTO implements Serializable {

    private static final long serialVersionUID = -637592088576234886L;

}
