package common.spi;

import io.javalin.Context;
import io.javalin.Handler;
import io.javalin.security.Role;

import java.util.Set;

public interface IAccessManagerService {
    void configure(Handler handler, Context ctx, Set<Role> permittedRoles) throws Exception;
}
