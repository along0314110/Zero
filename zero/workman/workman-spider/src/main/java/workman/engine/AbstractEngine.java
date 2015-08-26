/**
 * Copyright (C) Ye Bin
 *
 * 2015年8月26日
 *
 * Ye Bin (along0314110@163.com)
 */
package workman.engine;

import java.io.IOException;
import java.nio.channels.Channel;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import workman.task.Task;

/**
 * 引擎抽象
 *
 * @anthor yebin
 * @data 2015年8月26日
 */
public abstract class AbstractEngine<T extends Task<?>, C extends Channel>
		implements Engine<T> {
	private ExecutorService taskExecutor;

	protected int status = STATUS_UNREADY;

	protected int limit = 0;

	protected BlockingQueue<T> taskQueue;

	/**
	 * 固定容量
	 * 
	 * @param size
	 */
	public AbstractEngine(int size) {
		taskQueue = new ArrayBlockingQueue<T>(size);
		status = STATUS_READY;
		limit = size;
		taskExecutor = Executors.newFixedThreadPool(size);
		initialize();
	}

	/**
	 * 初始化
	 */
	protected abstract void initialize();

	protected abstract void register(T t);

	protected abstract C toChannel(T t) throws IOException;

	public boolean add(T t) {
		return taskQueue.add(t);
	}

	public int status() {
		return status;
	}

	/**
	 * 数量限制
	 * 
	 * @return
	 */
	public int limit() {
		return limit;
	}

	public ExecutorService getTaskExecutor() {
		return taskExecutor;
	}
}
