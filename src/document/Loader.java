package document;

import functions.Function;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class Loader extends ClassLoader {
    public static byte[] loadByteFile(File file) throws IOException {
        byte[] result = new byte[(int) file.length()];
        try (FileInputStream f = new FileInputStream(file)) {
            f.read(result);
        }
        return result;
    }

    private File ff;

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        try {
            byte[] byteClass = loadByteFile(ff);
            return defineClass("functions.basic." + name, byteClass,  0, byteClass.length);
        } catch (IOException e) {
            throw new ClassNotFoundException("Cannot load class");
        } catch (ClassFormatError e) {
            throw new ClassFormatError("Incorrect format");
        }
    }

    public Function loadFunction(File file) throws InstantiationException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException {
        ff = file;
        String function = file.getName().split(".java")[0];
        Class<? extends Function> funClass = (Class<? extends Function>) loadClass(function);
        return funClass.getDeclaredConstructor().newInstance();
    }
}
