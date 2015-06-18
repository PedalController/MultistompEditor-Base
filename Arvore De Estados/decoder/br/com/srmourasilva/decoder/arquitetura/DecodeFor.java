package br.com.srmourasilva.decoder.arquitetura;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)

public @interface DecodeFor {
	int effect();
	int param() default -1;
}
