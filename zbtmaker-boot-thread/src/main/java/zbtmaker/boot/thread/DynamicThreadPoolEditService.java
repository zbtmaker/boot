package zbtmaker.boot.thread;

import org.springframework.stereotype.Service;
import zbtmaker.boot.common.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 通过线程池修改
 *
 * @author zoubaitao
 * date 2022/08/20
 */
@Service

public class DynamicThreadPoolEditService {

    private static final Map<String, Class<?>> rejectHandlerNameMapClass = new HashMap<>();

    static {
        rejectHandlerNameMapClass.put("CallerRunsPolicy", ThreadPoolExecutor.CallerRunsPolicy.class);
        rejectHandlerNameMapClass.put("AbortPolicy", ThreadPoolExecutor.AbortPolicy.class);
        rejectHandlerNameMapClass.put("DiscardPolicy", ThreadPoolExecutor.DiscardPolicy.class);
        rejectHandlerNameMapClass.put("DiscardOldestPolicy", ThreadPoolExecutor.DiscardOldestPolicy.class);
    }

    public void edit(DynamicThreadPoolParam param) {
        String poolName = param.getPoolName();
        if (StringUtils.isEmpty(poolName)) {
            return;
        }

        Map<String, DynamicThreadPoolExecutor> poolExecutorMap = DynamicThreadPoolExecutorFactory.getPoolNameMapExecutor();
        DynamicThreadPoolExecutor threadPoolExecutor = poolExecutorMap.get(poolName);
        if (threadPoolExecutor == null) {
            return;
        }
        // 修改核心线程数
        Integer corePoolSize = param.getCorePoolSize();
        if (Objects.nonNull(corePoolSize) && corePoolSize > 0) {
            threadPoolExecutor.setCorePoolSize(corePoolSize);
        }

        // 修改最大线程数
        Integer maximumPoolSize = param.getMaximumPoolSize();
        if (Objects.nonNull(maximumPoolSize) && maximumPoolSize >= corePoolSize) {
            threadPoolExecutor.setMaximumPoolSize(maximumPoolSize);
        }

        // 修改拒绝策略
        String rejectExecutedHandlerName = param.getRejectExecutorHandler();
        if (StringUtils.isEmpty(rejectExecutedHandlerName)) {
            return;
        }
        Class<?> clazz = rejectHandlerNameMapClass.get(rejectExecutedHandlerName);
        if (Objects.isNull(clazz)) {
            return;
        }
        try {
            RejectedExecutionHandler rejectedExecutionHandler = (RejectedExecutionHandler) clazz.newInstance();
            threadPoolExecutor.setRejectedExecutionHandler(rejectedExecutionHandler);
        } catch (Exception ex) {
            // do nothing
        }
    }
}
