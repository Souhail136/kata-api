package com.kata.shopping.domain.service;

import com.kata.shopping.domain.model.valueobject.Money;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;

class MoneyTest {

    @Test
    void shouldCreateMoneyWithTwoDecimalPlaces() {
        Money m = Money.of("12.5");
        assertThat(m.value()).isEqualByComparingTo("12.50");
    }

    @Test
    void shouldRejectNegativeAmount() {
        assertThatThrownBy(() -> Money.of("-0.01"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldRejectNullAmount() {
        assertThatThrownBy(() -> Money.of((BigDecimal) null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldSupportAddition() {
        assertThat(Money.of("1.50").add(Money.of("2.50")))
                .isEqualTo(Money.of("4.00"));
    }

    @Test
    void shouldSupportMultiplication() {
        assertThat(Money.of("3.00").multiply(4))
                .isEqualTo(Money.of("12.00"));
    }

    @Test
    void zeroIsIdentityForAddition() {
        Money m = Money.of("5.99");
        assertThat(m.add(Money.ZERO)).isEqualTo(m);
    }

    @ParameterizedTest(name = "{0} arrondi aux 5 centimes → {1}")
    @CsvSource({
            "0.00, 0.00",
            "0.95, 0.95",
            "0.99, 1.00",
            "1.00, 1.00",
            "1.01, 1.05",
            "1.02, 1.05",
            "1.05, 1.05",
            "1.06, 1.10",
    })
    void shouldRoundToNearestFiveCents(String input, String expected) {
        Money result = Money.of(input).roundToNearestFiveCents();
        assertThat(result).isEqualTo(Money.of(expected));
    }
}
