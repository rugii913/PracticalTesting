package sample.cafekiosk.spring.api.controller.product.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sample.cafekiosk.spring.api.service.product.request.ProductCreateServiceRequest;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;
import sample.cafekiosk.spring.domain.product.ProductType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@NoArgsConstructor // JSON String을 이 타입 객체로 매핑(역직렬화)할 때 ObjectMapper가 기본 생성자를 사용함
public class ProductCreateRequest {

    @NotNull(message = "상품 타입은 필수입니다.")
    // bean validation 어노테이션에서 message 속성을 사용해서 메시지도 함께 던지기 - 클라이언트 단에 보내주고 싶은 메시지
    private ProductType type;

    @NotNull(message = "상품 판매상태는 필수입니다.")
    private ProductSellingStatus sellingStatus;

    @NotBlank(message = "상품 이름은 필수입니다.") // @NotBlank @NotEmpty @NotNull의 차이
    // @Max(20) - 정책에 의한 검증을 어디서 할 것이냐 문제 고민 - controller에서 튕겨내는 게 적절하지 않다.
    private String name;

    @Positive(message = "상품 가격은 양수여야 합니다.")
    private int price;

    @Builder
    private ProductCreateRequest(ProductType type, ProductSellingStatus sellingStatus, String name, int price) {
        this.type = type;
        this.sellingStatus = sellingStatus;
        this.name = name;
        this.price = price;
    }

    public ProductCreateServiceRequest toServiceRequest() {
        return ProductCreateServiceRequest.builder()
                .type(this.type)
                .sellingStatus(this.sellingStatus)
                .name(this.name)
                .price(this.price)
                .build();
    }
}
