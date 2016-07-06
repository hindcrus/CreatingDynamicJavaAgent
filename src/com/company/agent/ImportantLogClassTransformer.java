package com.company.agent;

import javassist.*;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.MethodInfo;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.ArrayMemberValue;
import javassist.bytecode.annotation.MemberValue;
import javassist.bytecode.annotation.StringMemberValue;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ImportantLogClassTransformer implements
        ClassFileTransformer {
    public byte[] transform(ClassLoader loader, String className,
                            Class classBeingRedefined, ProtectionDomain protectionDomain,
                            byte[] classfileBuffer) throws IllegalClassFormatException {


        ClassPool pool = ClassPool.getDefault();
        pool.insertClassPath(new ByteArrayClassPath(className, classfileBuffer));
        CtClass cclass = null;
        try {
            cclass = pool.get(className.replaceAll("/", "."));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        if (!cclass.isFrozen()) {
            for (CtMethod currentMethod : cclass.getDeclaredMethods()) {
                Annotation annotation = getAnnotation(currentMethod);
                if (annotation != null) {
                    List<String> parameterIndexes = getParamIndexes(annotation);
                    try {
                        currentMethod.insertBefore(createJavaString(currentMethod, className, parameterIndexes));
                    } catch (CannotCompileException e) {
                        e.printStackTrace();
                    }
                }

            }
            try {
                return cclass.toBytecode();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (CannotCompileException e) {
                e.printStackTrace();
            }
        }


        return null;
    }


    private Annotation getAnnotation(CtMethod method) {
        MethodInfo mInfo = method.getMethodInfo();
// the attribute we are looking for is a runtime invisible attribute
// use Retention(RetentionPolicy.RUNTIME) on the annotation to make it
// visible at runtime
        AnnotationsAttribute attInfo = (AnnotationsAttribute) mInfo
                .getAttribute(AnnotationsAttribute.invisibleTag);
        if (attInfo != null) {
            // this is the type name meaning use dots instead of slashes
            return attInfo.getAnnotation("ImportantLog");
        }
        return null;
    }

    private List<String> getParamIndexes(Annotation annotation) {
        ArrayMemberValue fields = (ArrayMemberValue) annotation
                .getMemberValue("fields");
        if (fields != null) {
            MemberValue[] values = (MemberValue[]) fields.getValue();
            List<String> parameterIndexes = new ArrayList<String>();
            for (MemberValue val : values) {
                parameterIndexes.add(((StringMemberValue) val).getValue());
            }
            return parameterIndexes;
        }
        return Collections.emptyList();
    }


    private String createJavaString(CtMethod currentMethod, String className,
                                    List<String> indexParameters) {
        StringBuilder sb = new StringBuilder();
        for (String index : indexParameters) {
            try {
                int localVar = Integer.parseInt(index) + 1;
                sb.append("sb.append(\"\\n Index: \");");
                sb.append("sb.append(\"");
                sb.append(index);
                sb.append("\");sb.appcd end(\" value: \");");
                sb.append("sb.append($" + localVar + ");");
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        sb.append("System.out.println(sb.toString());}");
        return sb.toString();
        // $0, $1, $2, ... can be used to access“this” and the actual method parameters
    }
}