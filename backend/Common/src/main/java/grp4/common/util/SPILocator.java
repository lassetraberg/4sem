package grp4.common.util;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Collection;
import java.util.Optional;

public class SPILocator {
    private static final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
    private static boolean hasRefreshed = false;

    public static <T> Collection<T> locateAll(Class<T> service) {
        if (!hasRefreshed) {
            context.scan("grp4");
            hasRefreshed = true;
            context.refresh();
        }

        return context.getBeansOfType(service).values();
    }

    public static <T> T locateSpecific(Class<T> service) {
        Optional<T> impl = SPILocator.locateAll(service).stream().findFirst();
        if (!impl.isPresent()) {
            throw new RuntimeException(String.format("Implementation of %s not found!", service.getName()));
        }

        return impl.get();
    }
}
