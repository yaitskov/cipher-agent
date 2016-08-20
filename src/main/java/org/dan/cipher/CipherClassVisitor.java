package org.dan.cipher;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

public class CipherClassVisitor extends ClassVisitor {
    private final String utilClass;
    private final String decryptAnnotation;
    private final String encryptAnnotation;

    private boolean annotatedWithEncrypt;
    private boolean annotatedWithDecrypt;

    public boolean isAnnotated() {
        return annotatedWithEncrypt || annotatedWithDecrypt;
    }

    public CipherClassVisitor(int api, ClassVisitor classVisitor,
                              Class utilClass, String decryptAnnotation,
                              String encryptAnnotation) {
        super(api, classVisitor);
        this.utilClass = utilClass.getName().replace(".", "/");
        this.decryptAnnotation = decryptAnnotation;
        this.encryptAnnotation = encryptAnnotation;
    }

    @Override
    public AnnotationVisitor visitAnnotation(String name, boolean b) {
        if (name.equals(encryptAnnotation)) {
            annotatedWithEncrypt = true;
        } else if (name.equals(decryptAnnotation)) {
            annotatedWithDecrypt = true;
        }
        return super.visitAnnotation(name, b);
    }

    @Override
    public MethodVisitor visitMethod(int access, final String name,
                                     final String desc, String signature,
                                     String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
        if (annotatedWithEncrypt) {
            mv = new TransformMethodVisitor(this.api, mv, utilClass,
                    "encrypt", name, encryptAnnotation);
        }
        if (annotatedWithDecrypt) {
            mv = new TransformMethodVisitor(this.api, mv, utilClass,
                    "decrypt", name, decryptAnnotation);
        }
        return mv;
    }

}
