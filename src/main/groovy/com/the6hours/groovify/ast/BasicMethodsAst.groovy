package com.the6hours.groovify.ast

import org.codehaus.groovy.control.CompilePhase
import org.codehaus.groovy.transform.GroovyASTTransformation
import org.codehaus.groovy.transform.ASTTransformation
import org.codehaus.groovy.ast.ASTNode
import org.codehaus.groovy.control.SourceUnit
import org.codehaus.groovy.ast.ModuleNode
import com.googlecode.objectify.annotation.Entity
import org.codehaus.groovy.ast.ClassNode

/**
 * TODO
 *
 * @since 17.08.11
 * @author Igor Artamonov (http://igorartamonov.com)
 */
@GroovyASTTransformation(phase = CompilePhase.CANONICALIZATION)
class BasicMethodsAst implements ASTTransformation {

    private static final ClassNode ENTITY = new ClassNode(Entity)

    void visit(ASTNode[] nodes, SourceUnit sourceUnit) {
        if (nodes.length != 1 || !(nodes[0] instanceof ModuleNode)) {
            throw new RuntimeException("Internal error: expecting [ModuleNode] but got: " + Arrays.asList(nodes))
        }

        nodes[0].getClasses().each { ClassNode owner ->
            if (owner.getAnnotations(ENTITY)) return // do not process this class

            injectBasicMethods(owner)
        }
    }

    private void injectBasicMethods(ClassNode classNode) {

    }
}
