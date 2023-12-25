package sample.cafekiosk.spring.api.service.product;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sample.cafekiosk.spring.api.controller.product.dto.request.ProductCreateRequest;
import sample.cafekiosk.spring.api.service.product.response.ProductResponse;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;

import java.util.List;
import java.util.stream.Collectors;

/*
 * @ Transactional 어노테이션의 readOnly 속성의 값이 true인 경우 읽기 전용
 * → CRUD에서 CUD 동작 x, 조회만 가능
 * → JPA: CUD 스냅샷 저장, 변경감지 X (성능 향상)
 *
 * CQRS - Command / Query의 분리 - 보통 command형 행위(CUD, 데이터의 생성, 변경, 삭제)보다 query형 행위의 빈도가 훨씬 높다. (보통 2:8 정도)
 * → command와 query에 대한 responsibility(책임)을 의도적으로 separate해서 서로 연관 없게끔 하자는 것
 * → query 때문에 장애가 생겨도 command는 가능하도록, command에 장애가 발생해도 query는 가능하도록
 * →→→ 이 CQRS의 첫 시작 지점으로 readOnly = true에 신경 쓰는 것을 생각해볼 수 있다.
 * →→→ ex. query용 서비스와 command용 서비스를 app 단에서 분리할 수 있다.
 * →→→ ex. DB 엔드 포인트를 구분할 수 있다. - read용 DB, write용 DB 나눠서 사용하는 경우 - readOnly = true 붙어있으면 slave DB로 false이면 master DB로
 * →→→→→ 장애 상황의 격리 가능 - AWS Aurora DB 클러스트 모드 사용하는 경우 같은 end point로 보내도 알아서 분리해줌 // Spring에서 명시적으로 end point를 다르게 주는 것도 가능
 */
@Transactional(readOnly = true) // 추천하는 방법은 Service 클래스에 readOnly = true를 걸고, CUD 메서드에만 false 주기
@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional
    // 원래는 동시성 이슈 고려해야되는 부분임
    // - ex1. 동시 접근 확률이 낮은 경우 DB에서 unique index로 걸고, 충돌할 경우 재시도하게 만들기
    // - ex2. 아예 정책을 변경해서 상품 번호를 UUID로 사용
    public ProductResponse createProduct(ProductCreateRequest request) {
        // productNumber 부여
        // DB에서 마지막 저장된 Product의 상품 번호를 읽어와서 + 1 (ex.)009 → 010
        String nextProductNumber = createNextProductNumber();

        Product product = request.toEntity(nextProductNumber);
        Product savedProduct = productRepository.save(product);

        return ProductResponse.of(savedProduct);
    }

    private String createNextProductNumber() {
        String latestProductNumber = productRepository.findLatestProductNumber();
        if (latestProductNumber == null) {
            return "001";
        }

        int latestProductNumberInt = Integer.parseInt(latestProductNumber);
        int nextProductNumberInt = latestProductNumberInt + 1;

        return String.format("%03d", nextProductNumberInt);
    }

//    @Transactional(readOnly = true) // 추천하는 방법은 Service 클래스에 readOnly = true를 걸고, CUD 메서드에만 false 주기
    public List<ProductResponse> getSellingProducts() {
        List<Product> products = productRepository.findAllBySellingStatusIn(ProductSellingStatus.forDisplay());

        return products.stream()
                .map(ProductResponse::of)
                .collect(Collectors.toList());
    }
}
