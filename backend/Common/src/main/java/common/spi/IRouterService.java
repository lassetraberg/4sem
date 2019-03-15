package common.spi;

import io.javalin.apibuilder.EndpointGroup;

public interface IRouterService {
    EndpointGroup getRoutes();
}
