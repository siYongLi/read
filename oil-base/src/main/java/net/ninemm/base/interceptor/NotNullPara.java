package net.ninemm.base.interceptor;

import java.lang.annotation.*;

/**
 * 参数不为空
 */
@Documented
@Target(ElementType.METHOD)
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface NotNullPara {

    String[] value();

    String errorRedirect() default "";
}
