package pl.spring.demo.aop;

import java.lang.reflect.Method;

import org.springframework.aop.MethodBeforeAdvice;

import net.bytebuddy.implementation.Implementation;
import pl.spring.demo.annotation.NullableBooksId;
import pl.spring.demo.annotation.NullableId;
import pl.spring.demo.common.Sequence;
import pl.spring.demo.dao.impl.BookDaoImpl;
import pl.spring.demo.exception.BookNotNullIdException;
import pl.spring.demo.to.BookTo;
import pl.spring.demo.to.IdAware;

public class BookIDAdvisor implements MethodBeforeAdvice{
	
	private Sequence sequence;
	
	@Override
    public void before(Method method, Object[] objects, Object o) throws Throwable {

        if (hasAnnotation(method, o, NullableBooksId.class)) {
            checkNotNullId(o, objects[0]);
        }
    }

    private void checkNotNullId(Object o, Object p) {
        if (p instanceof IdAware && ((IdAware) p).getId() == null) {
        	BookDaoImpl bookDaoImpl = (BookDaoImpl) o;
        	((BookTo) p).setId(sequence.nextValue(bookDaoImpl.findAll()));
        }
    }

    private boolean hasAnnotation (Method method, Object o, Class annotationClazz) throws NoSuchMethodException {
        boolean hasAnnotation = method.getAnnotation(annotationClazz) != null;

        if (!hasAnnotation && o != null) {
            hasAnnotation = o.getClass().getMethod(method.getName(), method.getParameterTypes()).getAnnotation(annotationClazz) != null;
        }
        return hasAnnotation;
    }
    
    public void setSequence(Sequence sequence) {
        this.sequence = sequence;
    }
}
	

