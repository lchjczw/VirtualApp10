package mirror;
import com.lody.virtual.helper.utils.VLog;
import java.lang.reflect.Field;
@SuppressWarnings("unchecked")
public class RefObject<T> {
    private Field field;
    public RefObject(Class<?> cls, Field field) throws NoSuchFieldException {
        this.field = cls.getDeclaredField(field.getName());
        this.field.setAccessible(true);
    }
    public T get(Object object) {
        try {
            return (T) this.field.get(object);
        } catch (Exception e) {
            VLog.printException(e);
            return null;
        }
    }
    public void set(Object obj, T value) {
        try {
            this.field.set(obj, value);
        } catch (Exception e) {
            VLog.printException(e);
        }
    }
}