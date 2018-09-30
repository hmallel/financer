package de.raphaelmuesseler.financer.shared.model.transactions;

import de.raphaelmuesseler.financer.shared.model.Category;

import java.time.LocalDate;

public class Transaction extends AbstractTransaction {
    private String shop;
    private LocalDate valueDate;


    public Transaction(int id, double amount, Category category, String product, String purpose, LocalDate valueDate, String shop) {
        super(id, amount, category, product, purpose);
        this.valueDate = valueDate;
        this.shop = shop;
    }

    public String getShop() {
        return shop;
    }

    public LocalDate getValueDate() {
        return valueDate;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public void setValueDate(LocalDate valueDate) {
        this.valueDate = valueDate;
    }
}