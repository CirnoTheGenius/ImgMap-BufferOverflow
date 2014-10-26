package net.yukkuricraft.tenko.imgmap.nms;

import com.avaje.ebean.enhance.asm.ClassWriter;
import com.avaje.ebean.enhance.asm.Label;
import com.avaje.ebean.enhance.asm.MethodVisitor;
import com.avaje.ebean.enhance.asm.Opcodes;
import net.yukkuricraft.tenko.imgmap.ImgMap;

import java.lang.reflect.Method;

public class SpigotProtocolFix {

	private static Class<Abstraction> result;

	private static void loadClass(){
		try {
			//Oh boy...
			ClassLoader loader = ImgMap.class.getClassLoader();
			Method method = ClassLoader.class.getDeclaredMethod("defineClass", String.class, byte[].class, int.class, int.class);
			method.setAccessible(true);
			byte[] dumpedClass = dump();
			result = (Class<Abstraction>)method.invoke(loader, "net.yukkuricraft.tenko.imgmap.nms.v1_7_R4.AbstractionImpl", dumpedClass, 0, dumpedClass.length);
		} catch (ReflectiveOperationException e){
			e.printStackTrace();
		}
	}

	public static Class<Abstraction> getSpigotClass(){
		if(result == null){
			loadClass();
		}

		return result;
	}

	/**
	 * Do NOT push any changes to this method. The code listed is all dynamically generated via ASM.
	 */
	private static byte[] dump(){
		ClassWriter cw = new ClassWriter(0);
		MethodVisitor mv;

		cw.visit(52, Opcodes.ACC_PUBLIC + Opcodes.ACC_SUPER, "net/yukkuricraft/tenko/imgmap/nms/v1_7_R4/AbstractionImpl", null, "java/lang/Object", new String[]{"net/yukkuricraft/tenko/imgmap/nms/Abstraction"});
		cw.visitSource("AbstractionImpl.java", null);
		cw.visitInnerClass("net/yukkuricraft/tenko/imgmap/nms/v1_7_R4/AbstractionImpl$1", null, null, 0);

		{
			mv = cw.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V", null, null);
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(23, l0);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V");
			mv.visitInsn(Opcodes.RETURN);
			Label l1 = new Label();
			mv.visitLabel(l1);
			mv.visitLocalVariable("this", "Lnet/yukkuricraft/tenko/imgmap/nms/v1_7_R4/AbstractionImpl;", null, l0, l1, 0);
			mv.visitMaxs(1, 1);
			mv.visitEnd();
		}
		{
			mv = cw.visitMethod(Opcodes.ACC_PUBLIC, "getDefaultRenderer", "(SLorg/bukkit/World;)Lorg/bukkit/map/MapRenderer;", null, null);
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(27, l0);
			mv.visitTypeInsn(Opcodes.NEW, "org/bukkit/inventory/ItemStack");
			mv.visitInsn(Opcodes.DUP);
			mv.visitFieldInsn(Opcodes.GETSTATIC, "org/bukkit/Material", "MAP", "Lorg/bukkit/Material;");
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "org/bukkit/inventory/ItemStack", "<init>", "(Lorg/bukkit/Material;)V");
			mv.visitVarInsn(Opcodes.ASTORE, 3);
			Label l1 = new Label();
			mv.visitLabel(l1);
			mv.visitLineNumber(28, l1);
			mv.visitVarInsn(Opcodes.ALOAD, 3);
			mv.visitVarInsn(Opcodes.ILOAD, 1);
			mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "org/bukkit/inventory/ItemStack", "setDurability", "(S)V");
			Label l2 = new Label();
			mv.visitLabel(l2);
			mv.visitLineNumber(29, l2);
			mv.visitVarInsn(Opcodes.ALOAD, 3);
			mv.visitMethodInsn(Opcodes.INVOKESTATIC, "org/bukkit/craftbukkit/v1_7_R4/inventory/CraftItemStack", "asNMSCopy", "(Lorg/bukkit/inventory/ItemStack;)Lnet/minecraft/server/v1_7_R4/ItemStack;");
			mv.visitVarInsn(Opcodes.ASTORE, 4);
			Label l3 = new Label();
			mv.visitLabel(l3);
			mv.visitLineNumber(33, l3);
			mv.visitTypeInsn(Opcodes.NEW, "org/bukkit/craftbukkit/v1_7_R4/map/CraftMapRenderer");
			mv.visitInsn(Opcodes.DUP);
			mv.visitInsn(Opcodes.ACONST_NULL);
			mv.visitVarInsn(Opcodes.ALOAD, 4);
			mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "net/minecraft/server/v1_7_R4/ItemStack", "getItem", "()Lnet/minecraft/server/v1_7_R4/Item;");
			mv.visitTypeInsn(Opcodes.CHECKCAST, "net/minecraft/server/v1_7_R4/ItemWorldMap");
			mv.visitVarInsn(Opcodes.ALOAD, 4);
			mv.visitVarInsn(Opcodes.ALOAD, 2);
			mv.visitTypeInsn(Opcodes.CHECKCAST, "org/bukkit/craftbukkit/v1_7_R4/CraftWorld");
			mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "org/bukkit/craftbukkit/v1_7_R4/CraftWorld", "getHandle", "()Lnet/minecraft/server/v1_7_R4/WorldServer;");
			mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "net/minecraft/server/v1_7_R4/ItemWorldMap", "getSavedMap", "(Lnet/minecraft/server/v1_7_R4/ItemStack;Lnet/minecraft/server/v1_7_R4/World;)Lnet/minecraft/server/v1_7_R4/WorldMap;");
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "org/bukkit/craftbukkit/v1_7_R4/map/CraftMapRenderer", "<init>", "(Lorg/bukkit/craftbukkit/v1_7_R4/map/CraftMapView;Lnet/minecraft/server/v1_7_R4/WorldMap;)V");
			mv.visitInsn(Opcodes.ARETURN);
			Label l4 = new Label();
			mv.visitLabel(l4);
			mv.visitLocalVariable("this", "Lnet/yukkuricraft/tenko/imgmap/nms/v1_7_R4/AbstractionImpl;", null, l0, l4, 0);
			mv.visitLocalVariable("id", "S", null, l0, l4, 1);
			mv.visitLocalVariable("world", "Lorg/bukkit/World;", null, l0, l4, 2);
			mv.visitLocalVariable("bukkitStack", "Lorg/bukkit/inventory/ItemStack;", null, l1, l4, 3);
			mv.visitLocalVariable("nmsStack", "Lnet/minecraft/server/v1_7_R4/ItemStack;", null, l3, l4, 4);
			mv.visitMaxs(6, 5);
			mv.visitEnd();
		}
		{
			mv = cw.visitMethod(Opcodes.ACC_PUBLIC, "getPacketData", "(I[B)Ljava/lang/Object;", null, null);
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(38, l0);
			mv.visitTypeInsn(Opcodes.NEW, "net/minecraft/server/v1_7_R4/PacketPlayOutMap");
			mv.visitInsn(Opcodes.DUP);
			mv.visitVarInsn(Opcodes.ILOAD, 1);
			mv.visitVarInsn(Opcodes.ALOAD, 2);
			mv.visitInsn(Opcodes.ICONST_0);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "net/minecraft/server/v1_7_R4/PacketPlayOutMap", "<init>", "(I[BB)V");
			mv.visitInsn(Opcodes.ARETURN);
			Label l1 = new Label();
			mv.visitLabel(l1);
			mv.visitLocalVariable("this", "Lnet/yukkuricraft/tenko/imgmap/nms/v1_7_R4/AbstractionImpl;", null, l0, l1, 0);
			mv.visitLocalVariable("id", "I", null, l0, l1, 1);
			mv.visitLocalVariable("data", "[B", null, l0, l1, 2);
			mv.visitMaxs(5, 3);
			mv.visitEnd();
		}
		{
			mv = cw.visitMethod(Opcodes.ACC_PUBLIC, "newChannel", "(Lorg/bukkit/entity/Player;)Lnet/yukkuricraft/tenko/imgmap/nms/ProxyChannel;", null, null);
			mv.visitCode();
			Label l0 = new Label();
			Label l1 = new Label();
			Label l2 = new Label();
			mv.visitTryCatchBlock(l0, l1, l2, "java/lang/ReflectiveOperationException");
			Label l3 = new Label();
			mv.visitLabel(l3);
			mv.visitLineNumber(43, l3);
			mv.visitVarInsn(Opcodes.ALOAD, 1);
			mv.visitTypeInsn(Opcodes.INSTANCEOF, "org/bukkit/craftbukkit/v1_7_R4/entity/CraftPlayer");
			Label l4 = new Label();
			mv.visitJumpInsn(Opcodes.IFNE, l4);
			Label l5 = new Label();
			mv.visitLabel(l5);
			mv.visitLineNumber(44, l5);
			mv.visitFieldInsn(Opcodes.GETSTATIC, "net/yukkuricraft/tenko/imgmap/nms/Abstraction", "LOGGER", "Ljava/util/logging/Logger;");
			mv.visitFieldInsn(Opcodes.GETSTATIC, "java/util/logging/Level", "WARNING", "Ljava/util/logging/Level;");
			mv.visitLdcInsn("Detected Non-CraftBukkit player! Kinda odd that this plugin still functions.");
			mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/util/logging/Logger", "log", "(Ljava/util/logging/Level;Ljava/lang/String;)V");
			Label l6 = new Label();
			mv.visitLabel(l6);
			mv.visitLineNumber(45, l6);
			mv.visitInsn(Opcodes.ACONST_NULL);
			mv.visitInsn(Opcodes.ARETURN);
			mv.visitLabel(l4);
			mv.visitLineNumber(48, l4);
			mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
			mv.visitVarInsn(Opcodes.ALOAD, 1);
			mv.visitTypeInsn(Opcodes.CHECKCAST, "org/bukkit/craftbukkit/v1_7_R4/entity/CraftPlayer");
			mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "org/bukkit/craftbukkit/v1_7_R4/entity/CraftPlayer", "getHandle", "()Lnet/minecraft/server/v1_7_R4/EntityPlayer;");
			mv.visitFieldInsn(Opcodes.GETFIELD, "net/minecraft/server/v1_7_R4/EntityPlayer", "playerConnection", "Lnet/minecraft/server/v1_7_R4/PlayerConnection;");
			mv.visitFieldInsn(Opcodes.GETFIELD, "net/minecraft/server/v1_7_R4/PlayerConnection", "networkManager", "Lnet/minecraft/server/v1_7_R4/NetworkManager;");
			mv.visitVarInsn(Opcodes.ASTORE, 2);
			mv.visitLabel(l0);
			mv.visitLineNumber(50, l0);
			mv.visitVarInsn(Opcodes.ALOAD, 2);
			mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/Object", "getClass", "()Ljava/lang/Class;");
			mv.visitLdcInsn("m");
			mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/Class", "getDeclaredField", "(Ljava/lang/String;)Ljava/lang/reflect/Field;");
			mv.visitVarInsn(Opcodes.ASTORE, 3);
			Label l7 = new Label();
			mv.visitLabel(l7);
			mv.visitLineNumber(51, l7);
			mv.visitVarInsn(Opcodes.ALOAD, 3);
			mv.visitInsn(Opcodes.ICONST_1);
			mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/reflect/Field", "setAccessible", "(Z)V");
			Label l8 = new Label();
			mv.visitLabel(l8);
			mv.visitLineNumber(52, l8);
			mv.visitVarInsn(Opcodes.ALOAD, 3);
			mv.visitVarInsn(Opcodes.ALOAD, 2);
			mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/reflect/Field", "get", "(Ljava/lang/Object;)Ljava/lang/Object;");
			mv.visitTypeInsn(Opcodes.CHECKCAST, "net/minecraft/util/io/netty/channel/Channel");
			mv.visitVarInsn(Opcodes.ASTORE, 4);
			Label l9 = new Label();
			mv.visitLabel(l9);
			mv.visitLineNumber(53, l9);
			mv.visitTypeInsn(Opcodes.NEW, "net/yukkuricraft/tenko/imgmap/nms/v1_7_R4/AbstractionImpl$1");
			mv.visitInsn(Opcodes.DUP);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitVarInsn(Opcodes.ALOAD, 4);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "net/yukkuricraft/tenko/imgmap/nms/v1_7_R4/AbstractionImpl$1", "<init>", "(Lnet/yukkuricraft/tenko/imgmap/nms/v1_7_R4/AbstractionImpl;Lnet/minecraft/util/io/netty/channel/Channel;)V");
			mv.visitLabel(l1);
			mv.visitInsn(Opcodes.ARETURN);
			mv.visitLabel(l2);
			mv.visitLineNumber(77, l2);
			mv.visitFrame(Opcodes.F_FULL, 3, new Object[]{"net/yukkuricraft/tenko/imgmap/nms/v1_7_R4/AbstractionImpl", "org/bukkit/entity/Player", "java/lang/Object"}, 1, new Object[]{"java/lang/ReflectiveOperationException"});
			mv.visitVarInsn(Opcodes.ASTORE, 3);
			Label l10 = new Label();
			mv.visitLabel(l10);
			mv.visitLineNumber(78, l10);
			mv.visitVarInsn(Opcodes.ALOAD, 3);
			mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/ReflectiveOperationException", "printStackTrace", "()V");
			Label l11 = new Label();
			mv.visitLabel(l11);
			mv.visitLineNumber(79, l11);
			mv.visitInsn(Opcodes.ACONST_NULL);
			mv.visitInsn(Opcodes.ARETURN);
			Label l12 = new Label();
			mv.visitLabel(l12);
			mv.visitLocalVariable("f", "Ljava/lang/reflect/Field;", null, l7, l2, 3);
			mv.visitLocalVariable("channel", "Lnet/minecraft/util/io/netty/channel/Channel;", null, l9, l2, 4);
			mv.visitLocalVariable("e", "Ljava/lang/ReflectiveOperationException;", null, l10, l12, 3);
			mv.visitLocalVariable("this", "Lnet/yukkuricraft/tenko/imgmap/nms/v1_7_R4/AbstractionImpl;", null, l3, l12, 0);
			mv.visitLocalVariable("player", "Lorg/bukkit/entity/Player;", null, l3, l12, 1);
			mv.visitLocalVariable("netty", "Ljava/lang/Object;", null, l0, l12, 2);
			mv.visitMaxs(4, 5);
			mv.visitEnd();
		}
		cw.visitEnd();

		return cw.toByteArray();
	}

}