package ci.dgmp.sigomap.sharedmodule.utilities;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;

@Component
public class ObjectCopier <T>
{
    public T copy(T src){
        if(src == null) return null;
        T target = null;
        try {
            target = (T) src.getClass().getDeclaredConstructor().newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        BeanUtils.copyProperties(src, target);
        return (T)target;
    }
}
