package agent;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

class MethodTracer extends MethodVisitor {

    public MethodTracer(final MethodVisitor mv) {
        super(Opcodes.ASM4, mv);
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
        // System.err.println("CALL" + name);
        if(!name.contains("init") && !owner.contains("java/")) {
            mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "err", "Ljava/io/PrintStream;");
            mv.visitLdcInsn("CALL " + owner + "." + name);
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
        }

        //do the actual method
        mv.visitMethodInsn(opcode, owner, name, desc);

        // TODO: System.err.println("RETURN" + name);
        if(!name.contains("init") && !owner.contains("java/")) {
            mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "err", "Ljava/io/PrintStream;");
            mv.visitLdcInsn("RETURN " + owner + "." + name);
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
        }
    }
}
