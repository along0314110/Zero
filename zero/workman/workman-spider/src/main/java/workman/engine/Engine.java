package workman.engine;

import java.io.IOException;

import workman.spider.Spider;

/**
 * 抓取引擎<br>
 * 用于启动不同的抓取任务
 *
 * @anthor yebin
 * @data 2015年8月26日
 */
public interface Engine {
	static final int STATUS_READY = 0;// 准备
	static final int STATUS_RUNNING = 1;// 正在运行
	static final int STATUS_CLOSED = 2;// 已关闭

	/**
	 * 添加蜘蛛
	 * 
	 * @param spider
	 * @return
	 * @throws IOException
	 */
	boolean add(Spider spider) throws IOException;

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
