package nc.isi.fragaria_reflection.utils;

import static com.google.common.base.Preconditions.checkArgument;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;

import nc.isi.fragaria_reflection.exceptions.MultipleAnnotationDefinitionException;

import org.springframework.beans.BeanUtils;

import com.google.common.base.Throwables;

/**
 * Aide à l'utilisation de Reflection
 * 
 * @author justin
 * 
 */
public final class ReflectionUtils {
	private ReflectionUtils() {

	}

	/**
	 * vérifie si une propriété existe pour une classe donnée
	 * 
	 * @param clazz
	 * @param fieldName
	 * @return
	 */
	public static boolean propertyExists(Class<?> clazz, String fieldName) {
		return BeanUtils.getPropertyDescriptor(clazz, fieldName) != null;
	}

	/**
	 * récupère la valeur d'une propritété pour un object donné en utilisant la
	 * ReadMethod
	 * 
	 * @param o
	 * @param propertyName
	 * @return
	 */
	public static Object getPropertyValue(Object o, String propertyName) {
		PropertyDescriptor propertyDescriptor = getPropertyDescriptor(
				o.getClass(), propertyName);
		try {
			return propertyDescriptor.getReadMethod().invoke(o);
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			throw Throwables.propagate(e);
		}
	}

	/**
	 * Essaye de récupérer la {@link Class} depuis un {@link Type}
	 * 
	 * @param type
	 * @return
	 */
	public static Class<?> getClass(Type type) {
		try {
			return type.toString().equals("?") ? Object.class : Class
					.forName(type.toString().substring(
							type.toString().lastIndexOf(' ') + 1));
		} catch (ClassNotFoundException e) {
			throw Throwables.propagate(e);
		}
	}

	/**
	 * cherche une annotation sur une propriété ou sa méthode read
	 * <p>
	 * si recursive = true => vérifie sur les méthodes des superclasses
	 * 
	 * @param clazz
	 * @param annotation
	 * @param key
	 * @param recursive
	 * @return
	 */
	public static <T extends Annotation> T getPropertyAnnotation(
			Class<?> clazz, Class<T> annotation, String key) {
		T fieldAnnotation = null;
		for (Field field : clazz.getDeclaredFields()) {
			if (field.getName().equals(key)) {
				fieldAnnotation = field.getAnnotation(annotation);
				break;
			}
		}
		PropertyDescriptor propertyDescriptor = getPropertyDescriptor(clazz,
				key);
		T readMethodAnnotation = propertyDescriptor.getReadMethod() != null ? propertyDescriptor
				.getReadMethod().getAnnotation(annotation) : null;
		T writeMethodAnnotation = propertyDescriptor.getWriteMethod() != null ? propertyDescriptor
				.getWriteMethod().getAnnotation(annotation) : null;
		if ((fieldAnnotation != null && readMethodAnnotation != null)
				|| (fieldAnnotation != null && writeMethodAnnotation != null)
				|| (readMethodAnnotation != null && writeMethodAnnotation != null)) {
			throw new MultipleAnnotationDefinitionException();
		}
		return readMethodAnnotation != null ? readMethodAnnotation
				: fieldAnnotation != null ? fieldAnnotation
						: writeMethodAnnotation;

	}

	/**
	 * @see public static <T extends Annotation> T getPropertyAnnotation(
	 *      Class<?> clazz, Class<T> annotation, String key, boolean recursive)
	 *      <p>
	 *      avec recursive = true;
	 * @param clazz
	 * @param annotation
	 * @param key
	 * @return
	 */
	public static <T extends Annotation> T getRecursivePropertyAnnotation(
			Class<?> clazz, Class<T> annotationClass, String key) {
		for (Class<?> tempClazz = clazz; Object.class
				.isAssignableFrom(tempClazz) && !tempClazz.equals(Object.class); tempClazz = tempClazz
				.getSuperclass()) {
			try {
				T annotation = getPropertyAnnotation(tempClazz,
						annotationClass, key);
				if (annotation != null)
					return annotation;
			} catch (IllegalArgumentException e) {
				break;
			}
		}
		return null;

	}

	/**
	 * récupère la propriété d'une classe par rapport à son nom
	 * 
	 * @param clazz
	 * @param fieldName
	 * @return
	 * @throws NoSuchFieldException
	 */
	public static Field getField(Class<?> clazz, String fieldName)
			throws NoSuchFieldException {
		for (Class<?> tempClazz = clazz; Object.class
				.isAssignableFrom(tempClazz) && !tempClazz.equals(Object.class); tempClazz = tempClazz
				.getSuperclass()) {
			for (Field field : tempClazz.getDeclaredFields()) {
				if (field.getName().equals(fieldName)) {
					return field;
				}
			}
		}
		throw new NoSuchFieldException();

	}

	/**
	 * récupère le {@link PropertyDescriptor} d'une propriété pour une classe
	 * 
	 * @param clazz
	 * @param key
	 * @return
	 */
	public static PropertyDescriptor getPropertyDescriptor(Class<?> clazz,
			String key) {
		PropertyDescriptor propertyDescriptor = BeanUtils
				.getPropertyDescriptor(clazz, key);
		checkArgument(propertyDescriptor != null, "Pas de propriété %s for %s",
				key, clazz);
		return propertyDescriptor;
	}

	/**
	 * récupère la valeur d'une annotation pour un type donné
	 * <p>
	 * si recursive = true, cherche dans les super
	 * 
	 * @param clazz
	 * @param annotation
	 * @param recursive
	 * @return
	 */
	public static <T extends Annotation> T getTypeAnnotation(Class<?> clazz,
			Class<T> annotation, boolean recursive) {
		if (recursive) {
			return getTypeAnnotation(clazz, annotation);
		}
		return clazz.getAnnotation(annotation);
	}

	/**
	 * @see <T extends Annotation> T getTypeAnnotation(Class<?> clazz, Class<T>
	 *      annotation, boolean recursive)
	 *      <p>
	 *      avec recursive = true
	 * @param clazz
	 * @param annotation
	 * @return
	 */
	public static <T extends Annotation> T getTypeAnnotation(Class<?> clazz,
			Class<T> annotation) {
		for (Class<?> tempClazz = clazz; Object.class
				.isAssignableFrom(tempClazz) && !Object.class.equals(tempClazz); tempClazz = tempClazz
				.getSuperclass()) {
			if (tempClazz.getAnnotation(annotation) != null) {
				return tempClazz.getAnnotation(annotation);
			}
		}
		return null;

	}
}
