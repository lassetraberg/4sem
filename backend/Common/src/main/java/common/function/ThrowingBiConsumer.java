package common.function;

import java.util.function.BiConsumer;

@FunctionalInterface
public interface ThrowingBiConsumer<T, U> extends BiConsumer<T, U> {
    @Override
    default void accept(T t, U u) {
        try {
            throwingAccept(t, u);
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }

    void throwingAccept(T t, U u) throws Throwable;
}
