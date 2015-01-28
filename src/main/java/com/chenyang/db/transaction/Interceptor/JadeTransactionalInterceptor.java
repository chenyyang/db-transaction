package com.chenyang.db.transaction.Interceptor;

import com.chenyang.db.transaction.annotation.JadeTransactional;
import com.chenyang.db.transaction.utils.DBContextHolder;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;


@Service
public class JadeTransactionalInterceptor implements MethodInterceptor {

    private static Logger logger = LoggerFactory.getLogger(JadeTransactionalInterceptor.class);

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        final StopWatch stopWatch = new StopWatch(invocation.getMethod().toGenericString());
        stopWatch.start("invocation.proceed()");
        logger.debug("JadeTransactionalInterceptor invoke start");
        DBContextHolder.setCatelog(invocation.getMethod().getAnnotation(JadeTransactional.class).catelog());
        try {
            return invocation.proceed();
        } finally {
            stopWatch.stop();
            DBContextHolder.clearCatelog();
            logger.debug("JadeTransactionalInterceptor invoke end");
        }
    }
}
