package common.util;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

public class SPILocator {
    public static <T> List<T> locateAll(Class<T> service) {
        ServiceLoader<T> serviceLoader = ServiceLoader.load(service);

        List<T> implementations = new ArrayList<>();
        serviceLoader.iterator().forEachRemaining(implementations::add);

        System.out.println(String.format("Found %d implementations for %s", implementations.size(), service.getName()));

        return implementations;
    }

    public static <T> T locateSpecific(Class<T> service) {
        ServiceLoader<T> serviceLoader = ServiceLoader.load(service);
        T impl = serviceLoader.iterator().next();

        if (impl == null) {
            throw new RuntimeException(String.format("Implementation of %s not found!", service.getName()));
        }

        return impl;
    }
}
