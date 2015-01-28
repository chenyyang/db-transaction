package com.chenyang.db.transaction.utils;

import org.springframework.util.Assert;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.security.AccessControlException;
import java.util.Arrays;

/**
 * copy from spring 4.0.6
 * 
 */
public abstract class ClassUtils {

    /** The CGLIB class separator character "$$" */
    public static final String CGLIB_CLASS_SEPARATOR = "$$";

    /** The package separator character '.' */
    private static final char PACKAGE_SEPARATOR = '.';

    /**
     * Return the user-defined class for the given class: usually simply the given
     * class, but the original class in case of a CGLIB-generated subclass.
     * 
     * @param clazz the class to check
     * @return the user-defined class
     */
    public static Class<?> getUserClass(Class<?> clazz) {
        if (clazz != null && clazz.getName().contains(CGLIB_CLASS_SEPARATOR)) {
            Class<?> superClass = clazz.getSuperclass();
            if (superClass != null && !Object.class.equals(superClass)) {
                return superClass;
            }
        }
        return clazz;
    }

    /**
     * Given a method, which may come from an interface, and a target class used
     * in the current reflective invocation, find the corresponding target method
     * if there is one. E.g. the method may be {@code IFoo.bar()} and the
     * target class may be {@code DefaultFoo}. In this case, the method may be {@code DefaultFoo.bar()}. This enables
     * attributes on that method to be found.
     * <p>
     * <b>NOTE:</b> In contrast to {@link org.springframework.aop.support.AopUtils#getMostSpecificMethod}, this method
     * does <i>not</i> resolve Java 5 bridge methods automatically. Call
     * {@link org.springframework.core.BridgeMethodResolver#findBridgedMethod} if bridge method resolution is desirable
     * (e.g. for obtaining metadata from the original method definition).
     * <p>
     * <b>NOTE:</b> Since Spring 3.1.1, if Java security settings disallow reflective access (e.g. calls to
     * {@code Class#getDeclaredMethods} etc, this implementation will fall back to returning the originally provided
     * method.
     * 
     * @param method the method to be invoked, which may come from an interface
     * @param targetClass the target class for the current invocation.
     *            May be {@code null} or may not even implement the method.
     * @return the specific target method, or the original method if the {@code targetClass} doesn't implement it or is
     *         {@code null}
     */
    public static Method getMostSpecificMethod(Method method, Class<?> targetClass) {
        if (method != null && isOverridable(method, targetClass) &&
                targetClass != null && !targetClass.equals(method.getDeclaringClass())) {
            try {
                if (Modifier.isPublic(method.getModifiers())) {
                    try {
                        return targetClass.getMethod(method.getName(), method.getParameterTypes());
                    } catch (NoSuchMethodException ex) {
                        return method;
                    }
                }
                else {
                    Method specificMethod = findMethod(targetClass, method.getName(), method.getParameterTypes());
                    return (specificMethod != null ? specificMethod : method);
                }
            } catch (AccessControlException ex) {
                // Security settings are disallowing reflective access; fall back to 'method' below.
            }
        }
        return method;
    }

    /**
     * Attempt to find a {@link Method} on the supplied class with the supplied name
     * and parameter types. Searches all superclasses up to <code>Object</code>.
     * <p>
     * Returns <code>null</code> if no {@link Method} can be found.
     * 
     * @param clazz the class to introspect
     * @param name the name of the method
     * @param paramTypes the parameter types of the method
     *            (may be <code>null</code> to indicate any signature)
     * @return the Method object, or <code>null</code> if none found
     */
    private static Method findMethod(Class clazz, String name, Class[] paramTypes) {
        Assert.notNull(clazz, "Class must not be null");
        Assert.notNull(name, "Method name must not be null");
        Class searchType = clazz;
        while (!Object.class.equals(searchType) && searchType != null) {
            Method[] methods = (searchType.isInterface() ? searchType.getMethods() : searchType.getDeclaredMethods());
            for (int i = 0; i < methods.length; i++) {
                Method method = methods[i];
                if (name.equals(method.getName()) &&
                        (paramTypes == null || Arrays.equals(paramTypes, method.getParameterTypes()))) {
                    return method;
                }
            }
            searchType = searchType.getSuperclass();
        }
        return null;
    }

    /**
     * Determine the name of the package of the given class:
     * e.g. "java.lang" for the <code>java.lang.String</code> class.
     * 
     * @param clazz the class
     * @return the package name, or the empty String if the class
     *         is defined in the default package
     */
    private static String getPackageName(Class<?> clazz) {
        Assert.notNull(clazz, "Class must not be null");
        String className = clazz.getName();
        int lastDotIndex = className.lastIndexOf(PACKAGE_SEPARATOR);
        return (lastDotIndex != -1 ? className.substring(0, lastDotIndex) : "");
    }

    /**
     * Determine whether the given method is overridable in the given target class.
     * 
     * @param method the method to check
     * @param targetClass the target class to check against
     */
    private static boolean isOverridable(Method method, Class targetClass) {
        if (Modifier.isPrivate(method.getModifiers())) {
            return false;
        }
        if (Modifier.isPublic(method.getModifiers()) || Modifier.isProtected(method.getModifiers())) {
            return true;
        }
        return getPackageName(method.getDeclaringClass()).equals(getPackageName(targetClass));
    }


}


