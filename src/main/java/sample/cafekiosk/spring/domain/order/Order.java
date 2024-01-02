package sample.cafekiosk.spring.domain.order;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sample.cafekiosk.spring.domain.BaseEntity;
import sample.cafekiosk.spring.domain.orderproduct.OrderProduct;
import sample.cafekiosk.spring.domain.product.Product;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "orders") // sql에서 order가 예약어라서 회피
@Entity
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING) // enum은 이거 빠뜨리면 안 된다.
    private OrderStatus orderStatus;

    private int totalPrice;

    private LocalDateTime registeredDateTime;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    // mappedBy 연관 관계의 주인인 엔티티의 해당 필드명 - 여기서는 주인이 orderProduct이다.
    // cascade로 생명주기 설정(일단 all로 설정 -> order에 변화가 있으면 orderProduct도 항상 그에 맞춰 작업하도록
    // (아마도) → Product는 처음 주어진 상태로 거의 변화가 없고, Order 데이터 들어와서 DB에 저장될 때, 이에 맞춰서 OrderProduct 데이터도 함께 생성됨
    // order.http POST 요청 날려서 확인해보면 OrderService의 createOrder에서는 orderRepository만 호출하고 있는데,
    // 실제 쿼리 가는 것을 보면 order_product에도 insert 쿼리가 날아가는 것을 확인할 수 있음
    private List<OrderProduct> orderProducts = new ArrayList<>();

    @Builder
    private Order(List<Product> products, OrderStatus orderStatus, LocalDateTime registeredDateTime) {
        this.orderStatus = orderStatus;
        this.totalPrice = calculateTotalPrice(products);
        this.registeredDateTime = registeredDateTime;
        this.orderProducts = products.stream()
                .map(product -> new OrderProduct(this, product))
                .collect(Collectors.toList());
    }

    public static Order create(List<Product> products, LocalDateTime registeredDateTime) { // static 메서드, 밖에서 생성할 때는 생성자 대신 이 static 메서드를 사용
        return Order.builder()
                .orderStatus(OrderStatus.INIT)
                .products(products)
                .registeredDateTime(registeredDateTime)
                .build();
    }

    private int calculateTotalPrice(List<Product> products) {
        return products.stream()
                .mapToInt(Product::getPrice)
                .sum();
    }
}
