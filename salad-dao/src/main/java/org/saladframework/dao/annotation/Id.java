package org.saladframework.dao.annotation;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Id {
	public boolean updatable() default false;
	public boolean insertable() default false;
	
}
