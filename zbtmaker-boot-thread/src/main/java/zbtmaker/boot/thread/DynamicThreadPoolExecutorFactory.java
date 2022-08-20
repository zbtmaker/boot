package zbtmaker.boot.thread;

import zbtmaker.boot.common.util.StringUtils;

import java.util.Map;
import java.util.concurrent.*;

/**
 * @author zoubaitao
 * date 2022/07/10
 */
public class DynamicThreadPoolExecutorFactory {

    /**
     * 线程池名称映射线程池，方便根据线程池名称获取线程池，然后修改配置项
     */
    private static final Map<String, DynamicThreadPoolExecutor> POOL_NAME_MAP_EXECUTOR = new ConcurrentHashMap<>();

    public static DynamicThreadPoolExecutor createDynamicThreadPool(String threadPoolName,
                                                                    int corePoolSize,
                                                                    int maximumPoolSize,
                                                                    long keepAliveTime,
                                                                    TimeUnit unit,
                                                                    BlockingQueue<Runnable> workQueue) {

        validThreadPool(threadPoolName);
        DynamicThreadPoolExecutor threadPoolExecutor = new DynamicThreadPoolExecutor(threadPoolName,
                corePoolSize,
                maximumPoolSize,
                keepAliveTime,
                unit,
                workQueue
        );
        POOL_NAME_MAP_EXECUTOR.put(threadPoolName, threadPoolExecutor);
        return threadPoolExecutor;
    }

    public static DynamicThreadPoolExecutor create(String threadPoolName,
                                                   int corePoolSize,
                                                   int maximumPoolSize,
                                                   long keepAliveTime,
                                                   TimeUnit unit,
                                                   BlockingQueue<Runnable> workQueue,
                                                   ThreadFactory threadFactory) {
        validThreadPool(threadPoolName);
        DynamicThreadPoolExecutor threadPoolExecutor = new DynamicThreadPoolExecutor(threadPoolName,
                corePoolSize,
                maximumPoolSize,
                keepAliveTime,
                unit,
                workQueue,
                threadFactory
        );
        POOL_NAME_MAP_EXECUTOR.put(threadPoolName, threadPoolExecutor);
        return threadPoolExecutor;
    }

    public static DynamicThreadPoolExecutor create(String threadPoolName,
                                                   int corePoolSize,
                                                   int maximumPoolSize,
                                                   long keepAliveTime,
                                                   TimeUnit unit,
                                                   BlockingQueue<Runnable> workQueue,
                                                   RejectedExecutionHandler handler) {
        validThreadPool(threadPoolName);
        DynamicThreadPoolExecutor threadPoolExecutor = new DynamicThreadPoolExecutor(threadPoolName,
                corePoolSize,
                maximumPoolSize,
                keepAliveTime,
                unit,
                workQueue,
                handler
        );
        POOL_NAME_MAP_EXECUTOR.put(threadPoolName, threadPoolExecutor);
        return threadPoolExecutor;
    }

    public static DynamicThreadPoolExecutor create(String threadPoolName,
                                                   int corePoolSize,
                                                   int maximumPoolSize,
                                                   long keepAliveTime,
                                                   TimeUnit unit,
                                                   BlockingQueue<Runnable> workQueue,
                                                   ThreadFactory threadFactory,
                                                   RejectedExecutionHandler handler) {
        validThreadPool(threadPoolName);
        DynamicThreadPoolExecutor threadPoolExecutor = new DynamicThreadPoolExecutor(threadPoolName,
                corePoolSize,
                maximumPoolSize,
                keepAliveTime,
                unit,
                workQueue,
                threadFactory,
                handler
        );
        POOL_NAME_MAP_EXECUTOR.put(threadPoolName, threadPoolExecutor);
        return threadPoolExecutor;
    }

    private static void validThreadPool(String threadPoolName) {
        if (StringUtils.isEmpty(threadPoolName)) {
            throw new IllegalArgumentException("线程池名称不可为空");
        }
        if (POOL_NAME_MAP_EXECUTOR.get(threadPoolName) != null) {
            throw new IllegalArgumentException("当前线程池名称对应的线程已存在");
        }
    }

    public static Map<String, DynamicThreadPoolExecutor> getPoolNameMapExecutor() {
        return POOL_NAME_MAP_EXECUTOR;
    }
}
