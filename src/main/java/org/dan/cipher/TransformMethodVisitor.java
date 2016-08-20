package org.dan.cipher;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class TransformMethodVisitor extends MethodVisitor {
    private static final Logger logger = LoggerFactory.getLogger(TransformMethodVisitor.class);

    private final String utilClass;
    private final String filterMethod;
    private final String name;
    private final String annotationSwitch;
    private boolean isTransform;

    public TransformMethodVisitor(int api, MethodVisitor mv, String utilClass,
                                  String filterMethod, String name,
                                  String annotationSwitch) {
        super(api, mv);
        this.utilClass = utilClass;
        this.filterMethod = filterMethod;
        this.name = name;
        this.annotationSwitch = annotationSwitch;
    }

    @Override
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        if (desc.equals(annotationSwitch)) {
            isTransform = true;
            logger.info("Transform method [{}] with [{}]", name, filterMethod);
        }
        return super.visitAnnotation(desc, visible);
    }

    @Override
    public void visitVarInsn(int opcode, int var) {
        super.visitVarInsn(opcode, var);
        if (isTransform && opcode == Opcodes.ALOAD && var == 1) {
            super.visitMethodInsn(Opcodes.INVOKESTATIC, utilClass, filterMethod,
                    "(Ljava/lang/String;)Ljava/lang/String;", false);
        }
    }

    @Override
    public void visitInsn(int opcode) {
        if (isTransform && opcode == Opcodes.ARETURN) {
            super.visitMethodInsn(Opcodes.INVOKESTATIC, utilClass, filterMethod,
                    "(Ljava/lang/String;)Ljava/lang/String;", false);
        }
        super.visitInsn(opcode);
    }
}
