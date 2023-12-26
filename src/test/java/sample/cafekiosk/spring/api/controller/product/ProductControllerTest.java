package sample.cafekiosk.spring.api.controller.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import sample.cafekiosk.spring.api.controller.product.dto.request.ProductCreateRequest;
import sample.cafekiosk.spring.api.service.product.ProductService;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;
import sample.cafekiosk.spring.domain.product.ProductType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ProductController.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean // @MockBean mockito 라이브러리의 어노테이션 - 컨테이너에 Mockito로 만든 mock 객체를 넣어주는 역할
    private ProductService productService; // Mockito 공식 문서 site.mockito.org 참고
    // @WebMvcTest를 사용하고 있기 때문에, ProductService 타입 mock 객체를 컨테이너에 같이 올려주지 않으면 의존성이 해결되지 않아서 다음의 에러가 발생한다.
    // Caused by: ...UnsatisfiedDependencyException: Error creating bean with name 'productController' ...
    // No qualifying bean of type 'sample.cafekiosk.spring.api.service.product.ProductService' available: expected at least 1 bean which qualifies as autowire candidate...

    @DisplayName("신규 상품을 등록한다.")
    @Test
    void createProduct() throws Exception {
        // given
        ProductCreateRequest request = ProductCreateRequest.builder()
                .type(ProductType.HANDMADE)
                .sellingStatus(ProductSellingStatus.SELLING)
                .name("아메리카노")
                .price(4000)
                .build();

        // when & then
        // - mockMvc.perform - API 수행을 나타냄
        // - post는 http body에 데이터를 넣으므로 직렬화, 역직렬화 과정을 거쳐야 함
        // --- 위 request 객체를 JSON으로 직렬화(byte 배열 형태 혹은 String 형태로)
        // --- 직렬화하기 위해 ObjectMapper의 도움을 받음
        mockMvc.perform(
                        post("/api/v1/products/new")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }
}
// cf. 아래의 JPA 관련 에러 발생할 경우 - app 메인의 @EnableJpaAuditing 어노테이션 때문임 → config 분리해주면 된다.
// org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'jpaAuditingHandler':
// Cannot resolve reference to bean 'jpaMappingContext' while setting constructor argument;
// nested exception is org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'jpaMappingContext':
// Invocation of init method failed; nested exception is java.lang.IllegalArgumentException: JPA metamodel must not be empty!
