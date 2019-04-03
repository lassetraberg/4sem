package common.util;

import io.javalin.Context;

public class JavalinUtils {
    public static String getUsername(Context ctx) {
        return ctx.attribute("username");
    }
}
