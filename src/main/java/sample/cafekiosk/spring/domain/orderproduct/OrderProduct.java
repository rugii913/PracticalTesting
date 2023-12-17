package sample.cafekiosk.spring.domain.orderproduct;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sample.cafekiosk.spring.domain.BaseEntity;
import sample.cafekiosk.spring.domain.order.Order;
import sample.cafekiosk.spring.domain.product.Product;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class OrderProduct extends BaseEntity { // 다대다 관계를 회피하기 위해 중간 테이블 역할하는 엔티티

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // 두 엔티티를 이어주기

    @ManyToOne(fetch = FetchType.LAZY) // 기본적으로 지연 로딩 사용
    private Order order; // Order와는 양방향으로 할 것

    @ManyToOne(fetch = FetchType.LAZY) // 기본적으로 지연 로딩 사용
    private Product product; // Product에서는 Order를 모르도록, 단방향으로 할 것

    public OrderProduct(Order order, Product product) {
        this.order = order;
        this.product = product;
    }
}
