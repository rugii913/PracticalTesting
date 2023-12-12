package sample.cafekiosk.unit;

import lombok.Getter;
import sample.cafekiosk.unit.beverage.Beverage;
import sample.cafekiosk.unit.order.Order;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// 요구사항들을 충족시켜 주는 객체
@Getter
public class CafeKiosk {
    private final List<Beverage> beverages = new ArrayList<>();

    public void add(Beverage beverage) {
        beverages.add(beverage);
    }

    // 테스트케이스 세분화 하기
    // - 요구사항을 받으면 질문한 사람에게 다시 물어볼 수 있어야 한다.
    // → 암묵적이거나 아직 드러나지 않은 요구사항이 있는가? - 경계값(범위, 구간 날짜 등) 테스트 - 해피 케이스 / 예외 케이스
    public void add(Beverage beverage, int count) {
        if (count <= 0) {
            throw new IllegalArgumentException("음료는 1잔 이상 주문하실 수 있습니다.");
        }

        for (int i = 0; i < count; i++) {
            beverages.add(beverage);
        }
    }

    public void remove(Beverage beverage) {
        beverages.remove(beverage);
    }

    public void clear() {
        beverages.clear();
    }

    public int calculateTotalPrice() {
        int totalPrice = 0;
        for (Beverage beverage : beverages) {
            totalPrice += beverage.getPrice();
        }
        return totalPrice;
    }

    public Order createOrder() {
        return new Order(LocalDateTime.now(), beverages);
    }
}
