package com.the6hours.groovify

import org.spockframework.runtime.extension.ExtensionAnnotation
import java.lang.annotation.ElementType
import java.lang.annotation.Target
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Retention
/**
 * Помечает тест как выполняющийся в среде GAE
 *
 * @since 23.08.11
 * @author Igor Artamonov
 */
@Retention(RetentionPolicy.RUNTIME)
@Target([ElementType.TYPE, ElementType.METHOD])
@ExtensionAnnotation(GaeExtension)
public @interface WithGae {

}