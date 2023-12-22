package sample.cafekiosk.spring.domain.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * select *
     * from product
     * where selling_status in ('SELLING', 'HOLD);
     * // 위와 같은 쿼리가 나가길 기대하고 작성한 쿼리 메서드
     */
    List<Product> findAllBySellingStatusIn(List<ProductSellingStatus> sellingStatuses);

    List<Product> findAllByProductNumberIn(List<String> productNumbers);

    // 네이티브 쿼리 사용해봄(JPQL도 아님)
    // - 쿼리 메서드를 사용하든, queryDSL을 사용하든, 네이티브 쿼리를 사용하든 테스트는 작성해야 한다.
    @Query(value = "select p.product_number from product p order by id desc limit 1", nativeQuery = true)
    String findLatestProductNumber();
}
