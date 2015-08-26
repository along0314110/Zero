package workman.engine;

import java.io.IOException;
import java.nio.channels.Channel;

import workman.task.Task;

/**
 * 引擎<br>
 * 用于启动不同的抓取任务
 *
 * @anthor yebin
 * @data 2015年8月26日
 */
public interface Engine<T extends Task<? extends Channel>> {
	static final int STATUS_UNREADY = -1;// 未准备
	static final int STATUS_READY = 0;// 准备
	static final int STATUS_RUNNING = 1;// 正在运行
	static final int STATUS_CLOSED = 2;// 已关闭

	/**
	 * 添加任务
	 * 
	 * @param T
	 * @return
	 * @throws IOException
	 */
	boolean add(T t) throws IOException;

	/**
	 * 开始
	 * 
	 * @return
	 */
	void start();

	/**
	 * 关闭
	 */
	void close();

	/**
	 * 引擎状态
	 * 
	 */
	int status();

}
