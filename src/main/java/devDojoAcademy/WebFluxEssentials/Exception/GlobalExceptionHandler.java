
package devDojoAcademy.WebFluxEssentials.Exception;

import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;


import org.springframework.stereotype.Component; //1
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Optional;
@Component
@Order(-2)
public class GlobalExceptionHandler extends AbstractErrorWebExceptionHandler {
    /**
     * Create a new {@code AbstractErrorWebExceptionHandler}.
     *
     * @param errorAttributes    the error attributes
     * @param resources          the resources configuration properties
     * @param applicationContext the application context
     * @since 2.4.0
     */
    public GlobalExceptionHandler(ErrorAttributes errorAttributes, WebProperties.Resources resources, ApplicationContext applicationContext, ServerCodecConfigurer codecConfigurer) {
        super(errorAttributes, resources, applicationContext);
        this.setMessageWriters(codecConfigurer.getWriters());
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::formaterrorResponse);
    }

    private Mono<ServerResponse> formaterrorResponse(ServerRequest request){
        Map<String, Object> errorAttributesMap = getErrorAttributes(request, ErrorAttributeOptions.of(ErrorAttributeOptions.Include.STACK_TRACE));
        //int status = (int) Optional.ofNullable((errorAttributesMap.get("status")));
      int status = (int) Optional.ofNullable(errorAttributesMap.get("status")).orElse("500");

        return  ServerResponse
                .status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(errorAttributesMap));
    }
}
