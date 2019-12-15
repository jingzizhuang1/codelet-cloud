package net.codelet.cloud.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * 异步任务执行工具。
 */
@Component
@Configuration
@EnableAsync
public class AsyncHelper {

    private static final String ASYNC_EXECUTOR_BEAN_NAME = "threadPoolTaskExecutor";

    private final AsyncMethodProvider asyncService;

    @Autowired
    public AsyncHelper(AsyncMethodProvider asyncService) {
        this.asyncService = asyncService;
    }

    /**
     * 线程池中任务队列的配置。
     */
    @Bean(ASYNC_EXECUTOR_BEAN_NAME)
    public Executor executor() {
        // TODO: read configuration from project profiles
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(4);
        executor.setMaxPoolSize(16);
        executor.setQueueCapacity(256);
        executor.setThreadNamePrefix("CodeletCloud-");
        executor.initialize();
        return executor;
    }

    /**
     * 异步执行任务队列，并等待所有任务完成。
     * @param tasks 任务列表
     * @param <T>   返回结果的范型
     * @return 任务的执行结果的列表
     */
    @SafeVarargs
    public final <T> List<T> allOf(Supplier<T>... tasks) throws ExecutionException, InterruptedException {
        return allOf(Arrays.asList(tasks));
    }

    /**
     * 异步执行任务队列，并等待所有任务完成。
     * @param tasks 任务列表
     * @param <T>   返回结果的范型
     * @return 任务的执行结果的列表
     */
    public final <T> List<T> allOf(List<Supplier<T>> tasks) throws ExecutionException, InterruptedException {
        return executeTasks(promises -> CompletableFuture.allOf(promises).join(), tasks);
    }

    /**
     * 异步执行任务队列，任意一个任务执行完成即结束。
     * @param tasks 任务数组
     * @param <T>   返回结果的范型
     * @return 任务的执行结果
     */
    @SafeVarargs
    public final <T> T anyOf(Supplier<T>... tasks) throws ExecutionException, InterruptedException {
        return anyOf(Arrays.asList(tasks));
    }


    /**
     * 异步执行任务队列，任意一个任务执行完成即结束。
     * @param tasks 任务数组
     * @param <T>   返回结果的范型
     * @return 任务的执行结果
     */
    public final <T> T anyOf(List<Supplier<T>> tasks) throws ExecutionException, InterruptedException {
        for (T result : executeTasks(promises -> CompletableFuture.anyOf(promises).join(), tasks)) {
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    /**
     * 异步执行任务队列。
     * @param executor 任务列表执行逻辑
     * @param tasks    任务列表
     * @param <T>      返回结果的范型
     * @return 任务的执行结果的列表
     */
    private <T> List<T> executeTasks(
        Consumer<CompletableFuture[]> executor,
        List<Supplier<T>> tasks
    ) throws ExecutionException, InterruptedException {
        // 设置待执行任务的执行队列
        List<CompletableFuture<T>> promises = new ArrayList<>();
        for (Supplier<T> task : tasks) {
            promises.add(asyncService.execute(task));
        }

        // 执行任务队列
        executor.accept(promises.toArray(new CompletableFuture[] {}));

        // 设置返回结果列表
        List<T> results = new ArrayList<>();
        for (CompletableFuture<T> promise : promises) {
            results.add(promise.get());
        }

        return results;
    }

    /**
     * 提供异步方法的组件。
     */
    @Component
    public static class AsyncMethodProvider {

        /**
         * 异步执行代执行任务。
         * @param task 待执行任务
         * @param <T>  待执行任务返回结果的范型
         * @return 待执行任务的返回结果的执行计划
         */
        @Async(ASYNC_EXECUTOR_BEAN_NAME)
        public <T> CompletableFuture<T> execute(Supplier<T> task) {
            return CompletableFuture.completedFuture(task.get());
        }
    }
}
