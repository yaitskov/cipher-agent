import org.dan.cipher.CipherClassVisitor;
import org.dan.cipher.Main;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PrintTest {
    static class ByteClassLoader extends ClassLoader {
        public Class define(String name, byte[] body) {
            return defineClass(name, body, 0, body.length);
        }
    }

    @Test
    public void x() throws IOException,
            NoSuchMethodException, IllegalAccessException,
            InvocationTargetException, InstantiationException {
        InputStream is = this.getClass().getResourceAsStream("/xxx/SimpleClass.class");

        byte[] bytes = IOUtils.toByteArray(is);

        ClassReader reader = new ClassReader(bytes);
        ClassWriter writer = new ClassWriter(reader, 0);
        //TraceClassVisitor visitor = new TraceClassVisitor(new PrintWriter(System.out));
        //reader.accept(visitor, 0);
        CipherClassVisitor classVisitor = new CipherClassVisitor(Opcodes.ASM4, writer,
                Util.class, "Lxxx/Downcase;", "Lxxx/Upcase;");
        reader.accept(classVisitor, 0);
        byte[] modified = writer.toByteArray();

        assertTrue(classVisitor.isAnnotated());
        ByteClassLoader myLoader = new ByteClassLoader();
        Class myClass = myLoader.define("xxx.SimpleClass", modified);
        Constructor c = myClass.getConstructor(String.class);
        {
            Object o = c.newInstance("xxx");
            Method m = myClass.getMethod("upcase");
            assertEquals("XXX", m.invoke(o));
        }

        {
            Object o = c.newInstance("");
            Method m = myClass.getMethod("downcase", String.class);
            m.invoke(o, "XXX");
            Method mGetAsIs = myClass.getMethod("getAsIs");
            assertEquals("xxx", mGetAsIs.invoke(o));
        }
    }

    @Test
    public void logo() {
        Main.icon();
    }
}
