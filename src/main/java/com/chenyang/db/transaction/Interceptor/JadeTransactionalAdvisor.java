package com.chenyang.db.transaction.Interceptor;


import com.chenyang.db.transaction.annotation.JadeTransactional;
import com.chenyang.db.transaction.utils.ClassUtils;
import org.aopalliance.aop.Advice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.StaticMethodMatcherPointcut;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;

import java.lang.reflect.Method;

public class JadeTransactionalAdvisor extends AbstractPointcutAdvisor implements InitializingBean {

    private static final long serialVersionUID = 1L;

    private static Logger logger = LoggerFactory.getLogger(JadeTransactionalAdvisor.class);

    private final StaticMethodMatcherPointcut pointcut = new StaticMethodMatcherPointcut() {

        @Override
        public boolean matches(java.lang.reflect.Method method, Class targetClass) {

            Class<?> userClass = ClassUtils.getUserClass(targetClass);
            // The method may be on an interface, but we need attributes from the target class.
            // If the target class is null, the method will be unchanged.
            Method specificMethod = ClassUtils.getMostSpecificMethod(method, userClass);
            boolean annotationPresent = specificMethod.isAnnotationPresent(JadeTransactional.class);
            if (annotationPresent) {
                logger.debug("matches success method : {} targetClass : {}", method, targetClass);
            }
            return annotationPresent;

        }
    };

    @Autowired
    private JadeTransactionalInterceptor interceptor;

    @Override
    public Pointcut getPointcut() {
        return this.pointcut;
    }

    @Override
    public Advice getAdvice() {
        return this.interceptor;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        setOrder(Ordered.LOWEST_PRECEDENCE - 1);
    }
}
