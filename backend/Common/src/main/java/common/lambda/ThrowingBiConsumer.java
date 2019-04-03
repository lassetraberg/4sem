package common.lambda;

import java.util.function.BiConsumer;

@FunctionalInterface
public interface ThrowingBiConsumer<T, U> extends BiConsumer<T, U> {
    @Override
    default void accept(T t, U u) {
        try {
            throwingAccept(t, u);
        } catch (Throwable ex) {
            Throwing.sneakyThrow(ex);
        }
    }

    void throwingAccept(T t, U u) throws Throwable;
}
