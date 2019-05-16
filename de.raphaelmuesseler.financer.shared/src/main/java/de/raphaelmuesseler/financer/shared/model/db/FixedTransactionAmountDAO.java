package de.raphaelmuesseler.financer.shared.model.db;

import java.time.LocalDate;

public class FixedTransactionAmountDAO implements DataAccessObject {
    private final static long serialVersionUID = -3901962625430867317L;

    private int id;
    private FixedTransactionDAO fixedTransaction;
    private LocalDate valueDate;
    private double amount;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public FixedTransactionDAO getFixedTransaction() {
        return fixedTransaction;
    }

    public void setFixedTransaction(FixedTransactionDAO fixedTransaction) {
        this.fixedTransaction = fixedTransaction;
    }

    public LocalDate getValueDate() {
        return valueDate;
    }

    public void setValueDate(LocalDate valueDate) {
        this.valueDate = valueDate;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}