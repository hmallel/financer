package de.raphaelmuesseler.financer.shared.util.collections;

public interface NumberAction<T> {
    void action(T result, String prefix);
}
