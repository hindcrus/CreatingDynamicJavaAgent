package com.company.agent;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

public class ImportantLogClassTransformer implements
ClassFileTransformer {
	 public byte[] transform(ClassLoader loader, String className,
	 	 	 Class classBeingRedefined, ProtectionDomain protectionDomain,
	 	 	 byte[] classfileBuffer) throws IllegalClassFormatException {
System.out.println(" Loading class: " + className);
	 	 return null;
	 }
}
