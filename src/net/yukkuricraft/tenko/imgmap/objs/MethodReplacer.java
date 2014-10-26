package net.yukkuricraft.tenko.imgmap.objs;

import com.avaje.ebean.enhance.asm.*;

/*
 * Code based off of http://stackoverflow.com/questions/11770353/java-asm-bytecode-modification-changing-method-bodies
 * Un-used and untested.
 */
public class MethodReplacer extends ClassWriter implements Opcodes{

	private String targetMethodName, targetMethodDesc, className;
	private ClassVisitor visitor;

	public MethodReplacer(ClassVisitor visitor, String methodName, String methodDesc){
		super(ClassWriter.COMPUTE_FRAMES);
		this.targetMethodName = methodName;
		this.targetMethodDesc = methodDesc;
		this.visitor = visitor;
	}

	@Override
	public void visit(int version, int access, String name, String signature, String superName, String[] interfaces){
		this.className = name;
		visitor.visit(version, access, name, signature, superName, interfaces);
	}

	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions){
		String newName = name;
		if(name.equals(targetMethodName) && desc.equals(targetMethodDesc)){
			newName = "orig$" + name;
			rewriteMethod();
		}
		return super.visitMethod(access, name, desc, signature, exceptions);
	}

	public void rewriteMethod(){
		MethodVisitor mv = visitor.visitMethod(ACC_PUBLIC, "getPacketData", "(I[B)Ljava/lang/Object;", null, null);
		mv.visitCode();
		Label l0 = new Label();
		mv.visitLabel(l0);
		mv.visitLineNumber(38, l0);
		mv.visitTypeInsn(NEW, "net/minecraft/server/v1_7_R4/PacketPlayOutMap");
		mv.visitInsn(DUP);
		mv.visitVarInsn(ILOAD, 1);
		mv.visitVarInsn(ALOAD, 2);
		mv.visitInsn(ICONST_0);
		mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/server/v1_7_R4/PacketPlayOutMap", "<init>", "(I[BB)V");
		mv.visitInsn(ARETURN);
		Label l1 = new Label();
		mv.visitLabel(l1);
		mv.visitLocalVariable("this", "Lnet/yukkuricraft/tenko/imgmap/nms/v1_7_R4/AbstractionImpl;", null, l0, l1, 0);
		mv.visitLocalVariable("id", "I", null, l0, l1, 1);
		mv.visitLocalVariable("data", "[B", null, l0, l1, 2);
		mv.visitMaxs(5, 3);
		mv.visitEnd();
	}

}