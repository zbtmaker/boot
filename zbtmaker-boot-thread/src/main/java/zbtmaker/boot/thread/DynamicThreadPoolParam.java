package zbtmaker.boot.thread;

/**
 * @author zoubaitao
 * date 2022/08/20
 */

public class DynamicThreadPoolParam {

    /**
     * 线程池名称
     */
    private String poolName;

    /**
     * 核心线程数
     */
    private Integer corePoolSize;

    /**
     * 最大线程数
     */
    private Integer maximumPoolSize;

    /**
     * 拒绝策略
     */
    private String rejectExecutorHandler;

    public String getPoolName() {
        return poolName;
    }

    public void setPoolName(String poolName) {
        this.poolName = poolName;
    }

    public Integer getCorePoolSize() {
        return corePoolSize;
    }

    public void setCorePoolSize(Integer corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public Integer getMaximumPoolSize() {
        return maximumPoolSize;
    }

    public void setMaximumPoolSize(Integer maximumPoolSize) {
        this.maximumPoolSize = maximumPoolSize;
    }

    public String getRejectExecutorHandler() {
        return rejectExecutorHandler;
    }

    public void setRejectExecutorHandler(String rejectExecutorHandler) {
        this.rejectExecutorHandler = rejectExecutorHandler;
    }
}
