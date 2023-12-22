package sample.cafekiosk.spring.api.service.order;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sample.cafekiosk.spring.api.controller.order.request.OrderCreateRequest;
import sample.cafekiosk.spring.api.service.order.response.OrderResponse;
import sample.cafekiosk.spring.domain.order.Order;
import sample.cafekiosk.spring.domain.order.OrderRepository;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.product.ProductType;
import sample.cafekiosk.spring.domain.stock.StockRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class OrderService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final StockRepository stockRepository;

    public OrderResponse createOrder(OrderCreateRequest request, LocalDateTime registeredDateTime) {
        List<String> productNumbers = request.getProductNumbers(); // 요청 상품 번호 목록(중복 제거되어 있지 않음)
        List<Product> products = findProductsBy(productNumbers);

        // 재고 차감 체크가 필요한 상품들 filter
        List<String> stockProductNumbers = products.stream()
                .filter(product -> ProductType.containsStockType(product.getType()))
                .map(Product::getProductNumber)
                .collect(Collectors.toList()); // Product 리스트는 아니고 상품 번호 리스트 가져옴
        // 재고 엔티티 조회
        stockRepository.findAllByProductNumberIn(stockProductNumbers);
        // 상품별 counting
        // 재고 차감 시도

        Order order = Order.create(products, registeredDateTime);
        Order savedOrder = orderRepository.save(order); // PK id 받아온 order를 가져옴
        return OrderResponse.of(savedOrder);
    }

    private List<Product> findProductsBy(List<String> productNumbers) {
        List<Product> products = productRepository.findAllByProductNumberIn(productNumbers); // 주문 대상이나 중복 상품은 없어져버린 목록

        Map<String, Product> productMap = products.stream()
                .collect(Collectors.toMap(Product::getProductNumber, Function.identity())); // productNumber가 key, product가 value

        return productNumbers.stream()
                .map(productMap::get)
                .collect(Collectors.toList());
    }
}
