/**
 * Copyright (C) Ye Bin
 *
 * 2015年8月26日
 *
 * Ye Bin (along0314110@163.com)
 */
package workman.engine;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import workman.spider.Spider;

/**
 * 引擎抽象
 *
 * @anthor yebin
 * @data 2015年8月26日
 */
public abstract class AbstractEngine implements Engine {
	protected BlockingQueue<Spider> spiderQueue;

	protected int status = STATUS_READY;

	/**
	 * 固定容量
	 * 
	 * @param size
	 */
	public AbstractEngine(int size) {
		spiderQueue = new ArrayBlockingQueue<Spider>(size);
	}

	public synchronized boolean add(Spider spider) {
		return spiderQueue.add(spider);
	}

	public int status() {
		return status;
	}

}
