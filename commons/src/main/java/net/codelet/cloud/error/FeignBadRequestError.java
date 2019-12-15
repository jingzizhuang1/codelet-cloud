package net.codelet.cloud.error;

import com.netflix.hystrix.exception.HystrixBadRequestException;
import lombok.Getter;

/**
 * <p>FeignClient 业务错误。</p>
 * <p>通过扩展 HystrixBadRequestException 避免断路器熔断。</p>
 */
public class FeignBadRequestError extends HystrixBadRequestException {

    private static final long serialVersionUID = -8173942950071908802L;

    @Getter
    private final BaseError error;

    public FeignBadRequestError(BaseError error) {
        super(error.getMessage());
        this.error = error;
    }
}
