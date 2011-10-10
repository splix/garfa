package com.the6hours.groovify

import org.spockframework.runtime.extension.AbstractAnnotationDrivenExtension
import org.spockframework.runtime.model.SpecInfo
import org.spockframework.runtime.model.FeatureInfo

/**
 * Привязка аннотации и настройки
 *
 * @since 23.08.11
 * @author Igor Artamonov
 */
class GaeExtension extends AbstractAnnotationDrivenExtension<WithGae> {

    @Override
    void visitSpecAnnotation(WithGae annotation, SpecInfo spec) {
    }

    @Override
    void visitFeatureAnnotation(WithGae annotation, FeatureInfo feature) {
    }

    @Override
    void visitSpec(SpecInfo spec) {
        super.visitSpec(spec)
        spec.addListener(new GaeExtensionListener())
    }

}
