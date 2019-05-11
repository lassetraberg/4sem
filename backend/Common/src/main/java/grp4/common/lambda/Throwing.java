package grp4.common.lambda;

import java.util.function.BiConsumer;

// https://gist.github.com/myui/9722c1301434a3b69cf898ccd9090ff1
public class Throwing {
    private Throwing() {
    }

    public static <T, U> BiConsumer<T, U> rethrow(final ThrowingBiConsumer<T, U> biConsumer) {
        return biConsumer;
    }

    @SuppressWarnings("unchecked")
    public static <E extends Throwable> void sneakyThrow(Throwable ex) throws E {
        throw (E) ex;
    }

}
