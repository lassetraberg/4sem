package grp4.common.spi;

import io.javalin.apibuilder.EndpointGroup;

public interface IRouterService {
    EndpointGroup getRoutes();
}
