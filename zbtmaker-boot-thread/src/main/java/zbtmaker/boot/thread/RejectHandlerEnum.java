package zbtmaker.boot.thread;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author zoubaitao
 * date 2022/08/20
 */
public enum RejectHandlerEnum {

    CallerRunsPolicy("CallerRunsPolicy", ThreadPoolExecutor.CallerRunsPolicy.class),
    ;
    private String name;

    private Class clazz;

    RejectHandlerEnum(String name, Class clazz) {
        this.name = name;
        this.clazz = clazz;
    }
}
