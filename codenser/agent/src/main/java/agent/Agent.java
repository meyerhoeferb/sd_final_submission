package agent;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

public class Agent {

    public static void premain(String agentArgs, Instrumentation inst) {
        inst.addTransformer(new ClassFileTransformer() {
            @Override
            public byte[] transform(ClassLoader classLoader, String s, Class<?> aClass, ProtectionDomain protectionDomain, byte[] bytes) throws IllegalClassFormatException {
                //the commented out if statement and return null allow us to only run code on specific classes, but having it run every time doesn't appear
                //to make it print any java library classes and such which is interesting
                if (/*!(s.contains("/"))*/true) {
                    // ASM Code
                    ClassReader reader = new ClassReader(bytes);
                    ClassWriter writer = new ClassWriter(reader, ClassWriter.COMPUTE_MAXS);
                    Tracer visitor = new Tracer(writer);
                    reader.accept(visitor, 0);
                    return writer.toByteArray();

                }

                return null;
            }
        });
    }

}
