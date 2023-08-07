package it.csi.smranag.smrgaa.presentation.client;

import it.csi.smranag.smrgaa.business.GaaFacadeBean;
import it.csi.smranag.smrgaa.business.GaaFacadeLocal;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class GaaFacadeWrapper extends GaaFacadeBean implements InvocationHandler{

	private Object wrapped;

	private GaaFacadeWrapper(Object wrapped) {
		super();
		this.wrapped = wrapped;
	}
	
	 public static GaaFacadeLocal wrap(Object wrapped) {
	        return (GaaFacadeLocal) Proxy.newProxyInstance(GaaFacadeLocal.class.getClassLoader(), new Class[] { GaaFacadeLocal.class }, new GaaFacadeWrapper(wrapped));
	    }

	 @Override
	    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
	        /*Method m = findMethod(this.getClass(), method);
	        if (m != null) {
	            return m.invoke(this, args);
	        }*/
	        Method m = findMethod(wrapped.getClass(), method);
	        if (m != null) {
	            return m.invoke(wrapped, args);
	        }
	        return null;
	    }

	    private Method findMethod(Class<?> clazz, Method method) throws Throwable {
	        try {
	            return clazz.getDeclaredMethod(method.getName(), method.getParameterTypes());
	        } catch (NoSuchMethodException e) {
	            return null;
	        }
	    }
	
}
