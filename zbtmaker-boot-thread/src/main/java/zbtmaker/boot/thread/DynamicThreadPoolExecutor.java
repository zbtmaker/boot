package zbtmaker.boot.thread;


import java.util.concurrent.BlockingQueue;

/**
 * @author zoubaitao
 * date 2022/07/10
 */

public class DynamicThreadPoolExecutor {

    /**
     * 核心线程数
     */
    private Integer corePoolSize;

    /**
     * 最大线程数
     */
    private Integer maximumPoolSize;

    /**
     * 线程数不够时将任务添加到Queue中
     */
    private BlockingQueue blockingQueue;

    /**
     * 线程池名称
     */
    private String threadPoolName;


}
