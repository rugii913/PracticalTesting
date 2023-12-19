package sample.cafekiosk.spring.api.controller.order.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
// Builder를 만들기 위해 private 해놓은 생성자만 있고, 기본 생성자 같은 다른 creator 없으면 jackson이 deserialize를 할 수가 없다.
// com.fasterxml.jackson.databind.exc.InvalidDefinitionException: Cannot construct instance of ...
// 그래서 @NoArgsConstructor 달아줌
public class OrderCreateRequest {

    private List<String> productNumbers;

    @Builder
    private OrderCreateRequest(List<String> productNumbers) {
        this.productNumbers = productNumbers;
    }
}
