package sample.cafekiosk.spring.domain.product;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class ProductTypeTest {

    @DisplayName("상품 타입이 재고 관련 타입인지를 체크한다.")
    @Test
    void containsStockType1() {
        // given
        ProductType givenType = ProductType.HANDMADE;

        // when
        boolean result = ProductType.containsStockType(givenType);

        // then
        assertThat(result).isFalse();
    }

    @DisplayName("상품 타입이 재고 관련 타입인지를 체크한다.")
    @Test
    void containsStockType2() {
        // given
        ProductType givenType = ProductType.BAKERY;

        // when
        boolean result = ProductType.containsStockType(givenType);

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("상품 타입이 재고 관련 타입인지를 체크한다.")
    @Test
    void containsStockTypeBadEx() {
        // given
        ProductType[] productTypes = ProductType.values();

        for (ProductType productType : productTypes) {
            if (productType == ProductType.HANDMADE) {
                // when
                boolean result = ProductType.containsStockType(productType);

                // then
                assertThat(result).isFalse();
            }

            if (productType == ProductType.BAKERY || productType == ProductType.BOTTLE) {
                // when
                boolean result = ProductType.containsStockType(productType);

                // then
                assertThat(result).isTrue();
            }
        }
    }
}