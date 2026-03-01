package com.kata.shopping.domain.model.valueobject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * Value Object représentant un montant monétaire.
 * Toujours positif ou nul, toujours à 2 décimales.
 * Immutable — toutes les opérations retournent une nouvelle instance.
 */
public final class Money {

    public static final Money ZERO = new Money(BigDecimal.ZERO);

    private final BigDecimal amount;

    private Money(BigDecimal amount) {
        this.amount = amount.setScale(2, RoundingMode.HALF_UP);
    }

    public static Money of(BigDecimal amount) {
        Objects.requireNonNull(amount, "Le montant ne peut pas être null");
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Un montant monétaire ne peut pas être négatif : " + amount);
        }
        return new Money(amount);
    }

    public static Money of(String amount) {
        return of(new BigDecimal(amount));
    }

    public Money add(Money other) {
        return new Money(this.amount.add(other.amount));
    }

    public Money multiply(int factor) {
        if (factor < 0) throw new IllegalArgumentException("Le facteur de multiplication ne peut pas être négatif");
        return new Money(this.amount.multiply(BigDecimal.valueOf(factor)));
    }

    public Money multiplyByRate(BigDecimal rate) {
        return new Money(this.amount.multiply(rate).divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP));
    }

    /**
     * Arrondi aux 5 centimes supérieurs.
     * Exemples : 0.99 → 1.00 | 1.01 → 1.05 | 1.00 → 1.00
     */
    public Money roundToNearestFiveCents() {
        BigDecimal multiplied = this.amount.multiply(BigDecimal.valueOf(20));
        BigDecimal ceiled = multiplied.setScale(0, RoundingMode.CEILING);
        return new Money(ceiled.divide(BigDecimal.valueOf(20), 2, RoundingMode.UNNECESSARY));
    }

    public BigDecimal value() {
        return amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Money money)) return false;
        return amount.compareTo(money.amount) == 0;
    }

    @Override
    public int hashCode() {
        return amount.stripTrailingZeros().hashCode();
    }

    @Override
    public String toString() {
        return amount.toPlainString();
    }
}
