package com.liashenko.app.authorization;

import java.lang.annotation.*;

@Target(value = ElementType.TYPE)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface Authorization {

    @Target(value = ElementType.TYPE)
    @Retention(value = RetentionPolicy.RUNTIME)
    @interface Allowed {
        long[] roles();

        String defAction() default "";

        String description() default "";
    }

    @Target(value = ElementType.TYPE)
    @Retention(value = RetentionPolicy.RUNTIME)
    @Repeatable(Restrictions.class)
    @interface Restricted {
        long[] roles();

        String action();

        int priority() default 0;

        String description() default "";
    }

    @Target(value = ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @interface Restrictions {
        Restricted[] value();
    }
}
