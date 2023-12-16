package sample.cafekiosk.spring.domain.product;

import org.springframework.data.jpa.repository.JpaRepository;
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
}
