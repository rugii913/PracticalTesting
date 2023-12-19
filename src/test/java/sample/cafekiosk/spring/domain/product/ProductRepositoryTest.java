package sample.cafekiosk.spring.domain.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static sample.cafekiosk.spring.domain.product.ProductSellingStatus.*;
import static sample.cafekiosk.spring.domain.product.ProductType.HANDMADE;

@ActiveProfiles("test") // 테스트할 때는 Spring 실행 구성에서 이 profile을 사용하도록 한다
// @SpringBootTest // 테스트할 때 Spring 띄워서 테스트
@DataJpaTest // 마찬가지로 Spring 띄우지만, JPA 관련된 빈들만 주입하므로 가볍다 // 하지만 단점이 있는데 그건 나중에 설명
// 여기서는 왜 오염된 데이터로 인한 테스트 실패가 발생하지 않았는가? rollback 때문 - 왜? @DataJpaTest는 @Transactional을 갖고 있다.
    // → 이 부분이 @SpringBootTest와 @DataJpaTest의 가장 큰 차이점이다
    // 왜 OrderServiceTest에는 @Transactional을 안 붙이고, 데이터 클렌징 작업 메서드를 사용했는가? - 문제점은 다음에 설명
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @DisplayName("원하는 판매상태를 가진 상품들을 조회한다.")
    @Test
    void findAllBySellingStatusIn() {
        // given
        Product product1 = Product.builder()
                .productNumber("001")
                .type(HANDMADE)
                .sellingStatus(SELLING)
                .name("아메리카노")
                .price(4000)
                .build();
        Product product2 = Product.builder()
                .productNumber("002")
                .type(HANDMADE)
                .sellingStatus(HOLD)
                .name("카페라떼")
                .price(4500)
                .build();
        Product product3 = Product.builder()
                .productNumber("003")
                .type(HANDMADE)
                .sellingStatus(STOP_SELLING)
                .name("팥빙수")
                .price(7000)
                .build();
        productRepository.saveAll(List.of(product1, product2, product3));

        // when
        List<Product> products = productRepository.findAllBySellingStatusIn(List.of(SELLING, HOLD));

        // then - list에 대한 검증은 보통 1.사이즈 체크 2. extracting-contains(특정 필드만 체크, 이 때 순서까지 정확히 맞는지 체크할 수도 있음)
        assertThat(products).hasSize(2)
                .extracting("productNumber", "name", "sellingStatus")
                .containsExactlyInAnyOrder(
                        tuple("001", "아메리카노", SELLING),
                        tuple("002", "카페라떼", HOLD)
                );
    }

    @DisplayName("상품번호 리스트로 상품들을 조회한다.")
    @Test
    void findAllByProductNumberIn() {
        // given
        Product product1 = Product.builder()
                .productNumber("001")
                .type(HANDMADE)
                .sellingStatus(SELLING)
                .name("아메리카노")
                .price(4000)
                .build();
        Product product2 = Product.builder()
                .productNumber("002")
                .type(HANDMADE)
                .sellingStatus(HOLD)
                .name("카페라떼")
                .price(4500)
                .build();
        Product product3 = Product.builder()
                .productNumber("003")
                .type(HANDMADE)
                .sellingStatus(STOP_SELLING)
                .name("팥빙수")
                .price(7000)
                .build();
        productRepository.saveAll(List.of(product1, product2, product3));

        // when
        List<Product> products = productRepository.findAllByProductNumberIn(List.of("001", "002"));

        // then - list에 대한 검증은 보통 1.사이즈 체크 2. extracting-contains(특정 필드만 체크, 이 때 순서까지 정확히 맞는지 체크할 수도 있음)
        assertThat(products).hasSize(2)
                .extracting("productNumber", "name", "sellingStatus")
                .containsExactlyInAnyOrder(
                        tuple("001", "아메리카노", SELLING),
                        tuple("002", "카페라떼", HOLD)
                );
    }
}