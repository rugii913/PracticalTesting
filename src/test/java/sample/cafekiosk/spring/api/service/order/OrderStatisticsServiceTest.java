package sample.cafekiosk.spring.api.service.order;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import sample.cafekiosk.spring.client.mail.MailSendClient;
import sample.cafekiosk.spring.domain.history.mail.MailSendHistory;
import sample.cafekiosk.spring.domain.history.mail.MailSendHistoryRepository;
import sample.cafekiosk.spring.domain.order.Order;
import sample.cafekiosk.spring.domain.order.OrderRepository;
import sample.cafekiosk.spring.domain.order.OrderStatus;
import sample.cafekiosk.spring.domain.orderproduct.OrderProductRepository;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.product.ProductType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static sample.cafekiosk.spring.domain.product.ProductSellingStatus.SELLING;

@SpringBootTest
class OrderStatisticsServiceTest {

    @Autowired
    private OrderStatisticsService orderStatisticsService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderProductRepository orderProductRepository;
    // OrderProductRepository 추가해서 아래 @AfterEach에서 orderProductRepository.deleteAllInBatch();하지 않으면
    // org.springframework.dao.DataIntegrityViolationException: could not execute statement; SQL [n/a]; constraint ["FKL5MNJ9N0DI7K1V90YXNTHKC73: PUBLIC.ORDER_PRODUCT FOREIGN KEY(ORDER_ID) REFERENCES PUBLIC.ORDERS(ID) (CAST(1 AS BIGINT))"; SQL statement:
    // delete from orders [23503-214]]; nested exception is org.hibernate.exception.ConstraintViolationException: could not execute statement
    // 위와 같은 에러 발생함

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MailSendHistoryRepository mailSendHistoryRepository;


    @MockBean // @Component로 스프링 빈으로 등록되어 있는 MailSendClient를 mocking
    private MailSendClient mailSendClient;
    // 기본적으로 mock는 가짜 객체를 넣어놓고, 이 가짜 객체가 어떻게 행동했으면 하는지(어떤 요청에 대해 어떤 결과)를 지정 가능

    @AfterEach
    void tearDown() {
        orderProductRepository.deleteAllInBatch(); // orderProductRepository를 가장 먼저 지워줘야 함, 이유는 나중에 설명
        orderRepository.deleteAllInBatch();
        productRepository.deleteAllInBatch();
        mailSendHistoryRepository.deleteAllInBatch();
    }

    @DisplayName("결제완료 주문들을 조회하여 매출 통계 메일을 전송한다.")
    @Test
    void sendOrderStatisticsMail() {
        // given
        LocalDateTime now = LocalDateTime.of(2023, 3, 5, 0, 0);

        Product product1 = createProduct(ProductType.HANDMADE, "001", 1000);
        Product product2 = createProduct(ProductType.HANDMADE, "002", 2000);
        Product product3 = createProduct(ProductType.HANDMADE, "003", 3000);
        List<Product> products = List.of(product1, product2, product3);
        productRepository.saveAll(products);

        // 헬퍼 메서드 createPaymentCompleteOrder에서 orderRepository.save(order)까지 함
        Order order1 = createPaymentCompletedOrder(LocalDateTime.of(2023, 3, 4, 23, 59, 59), products);
        Order order2 = createPaymentCompletedOrder(now, products);
        Order order3 = createPaymentCompletedOrder(LocalDateTime.of(2023, 3, 5, 23, 59, 59), products);
        Order order4 = createPaymentCompletedOrder(LocalDateTime.of(2023, 3, 6, 0, 0, 0), products);

        // stubbing
        // Mockito.when() static import + org.mockito.ArgumentMatchers.any 사용
        when(mailSendClient.sendEmail(any(String.class), any(String.class), any(String.class), any(String.class)))
                .thenReturn(true); // mock 객체의 행위를 지정한 것

        // when
        boolean result = orderStatisticsService
                .sendOrderStatisticsMail(LocalDate.of(2023, 3, 5), "test@test.com");

        // then
        assertThat(result).isTrue(); // send 결과가 참인지 확인

        List<MailSendHistory> histories = mailSendHistoryRepository.findAll(); // history 꺼내와서 list 검증
        assertThat(histories).hasSize(1)
                .extracting("content")
                .contains("총 매출 합계는 12000원입니다.");
    }

    private Order createPaymentCompletedOrder(LocalDateTime now, List<Product> products) {
        Order order = Order.builder()
                .products(products)
                .orderStatus(OrderStatus.PAYMENT_COMPLETED)
                .registeredDateTime(now)
                .build();
        return orderRepository.save(order);
    }

    private Product createProduct(ProductType type, String productNumber, int price) {
        return Product.builder()
                .type(type)
                .productNumber(productNumber)
                .price(price)
                .sellingStatus(SELLING)
                .name("메뉴 이름")
                .build();
    }
}
