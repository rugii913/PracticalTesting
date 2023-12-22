package sample.cafekiosk.spring.domain.stock;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sample.cafekiosk.spring.domain.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Stock extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productNumber; // 연관 관계를 안 맺고 간편하게 진행할 수 있다면 그 쪽이 나을 수도 있다.

    private int quantity;

    @Builder
    private Stock(String productNumber, int quantity) {
        this.productNumber = productNumber;
        this.quantity = quantity;
    }

    public static Stock create(String productNumber, int quantity) {
        return Stock.builder().
                productNumber(productNumber)
                .quantity(quantity)
                .build();
    }

    public boolean isQuantityLessThan(int quantity) {
        return this.quantity < quantity;
    }

    public void deductQuantity(int quantity) {
        if (isQuantityLessThan(quantity)) {
            throw new IllegalArgumentException("차감할 재고 수량이 없습니다.");
        }
        // 그런데 orderService에서 deductQuantity(~) 호출하기 전에 체크하는데 왜 여기서도 또 체크하는가?
        // - service에서 수행한 체크는 주문 생성 로직을 수행하다가 stock의 차감을 시도하는 과정이고
        // - stock은 밖의 service를 전혀 알지 못한다. 이 객체 자체만으로 일정한 로직, 예외 처리를 보장해줘야한다.
        // --- 따라서 두 경우는 완전히 다른 경우이며 오히려 stock에서 이 체크를 해야할 우선순위가 더 높다.
        // - 그러면 service에서 체크를 없애버릴 수도 있지 않은가? - 예외 메시지를 다르게 주고 싶을 수 있다.
        // --- entity 쪽은 개발자에게 주는 메시지, service 쪽은 사용자 친화적인 메시지 이런 식으로
        this.quantity -= quantity;
    }
}
