package org.saladframework.dao.annotation;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ProcedureName {
	public String delete();

	public String insert();

	public String update();

	public String updateByID();

	public String load();

	public String locaByPage();
}