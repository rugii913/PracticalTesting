package sample.cafekiosk.spring.domain.order;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sample.cafekiosk.spring.domain.BaseEntity;
import sample.cafekiosk.spring.domain.orderproduct.OrderProduct;
import sample.cafekiosk.spring.domain.product.Product;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    private List<OrderProduct> orderProducts = new ArrayList<>();

    public Order(List<Product> products) {
        this.orderStatus = OrderStatus.INIT;
        this.totalPrice = 0; // 작성 중
    }

    public static Order create(List<Product> products) { // static 메서드, 밖에서 생성할 때는 생성자 대신 이 static 메서드를 사용
        return new Order(products);
    }
}
