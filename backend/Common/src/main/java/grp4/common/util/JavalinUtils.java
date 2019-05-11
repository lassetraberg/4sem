package grp4.common.util;

import io.javalin.Context;
import io.javalin.security.Role;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class JavalinUtils {
    public static String getUsername(Context ctx) {
        return ctx.attribute("username");
    }

    public static Set<Role> roles(Role... roles) {
        return new HashSet<>(Arrays.asList(roles));
    }

    public static String getRole(Context ctx) {
        return ctx.attribute("role");
    }
}
