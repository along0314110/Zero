/**
 * Copyright (C) Ye Bin
 *
 * 2015年8月26日
 *
 * Ye Bin (along0314110@163.com)
 */
package workman.engine;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;

import org.apache.commons.io.IOUtils;

import workman.task.Spider;

/**
 * 抓取引擎
 *
 * @anthor yebin
 * @data 2015年8月26日
 */
public class SpiderEngine extends AbstractEngine<Spider> {

	private Selector selector;

	public SpiderEngine(int size) {
		super(size);
	}

	/**
	 * 初始化
	 * 
	 * @throws IOException
	 */
	protected void initialize() {
		try {
			selector = Selector.open();
			logger.info("引擎初始化完毕");
		} catch (IOException e) {
			logger.error("引擎初始化失败");
			logger.debug(e);
		} finally {
			IOUtils.closeQuietly(selector);
			selector = null;
		}
	}

	/**
	 * 注册任务
	 * 
	 * @param spider
	 */
	protected void register(Spider spider) {
		assertNotNull(spider);
		assertNotNull(selector);
		SelectableChannel channel = spider.getChannel();
		try {
			channel.register(selector, SelectionKey.OP_READ);
		} catch (IOException e) {
			logger.error("注册任务失败：" + spider);
			logger.debug(e);
			IOUtils.closeQuietly(channel);
		}
	}

	public void start() {
		assertNotNull(selector);
		int count = 0;
		status = STATUS_RUNNING;
		while (true) {
			// 读入数据
			while (limit() > count) {
				register(taskQueue.poll());
				count++;
			}
			Iterator<SelectionKey> it = selector.selectedKeys().iterator();
			while (it.hasNext()) {
				SelectionKey key = it.next();
				if (key.isReadable()) {
					SelectableChannel channel = key.channel();
					count--;
				}
				it.remove();
			}
		}
	}

	public void close() {
		IOUtils.closeQuietly(selector);
		status = STATUS_CLOSED;
	}
}
