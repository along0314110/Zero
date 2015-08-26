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
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import workman.task.Spider;

/**
 * 抓取引擎
 *
 * @anthor yebin
 * @data 2015年8月26日
 */
public class SpiderEngine extends
		AbstractEngine<Spider, AsynchronousSocketChannel> {

	private static final Logger logger = LogManager
			.getLogger(SpiderEngine.class);

	private Selector selector;
	
	private AsynchronousChannelGroup group;

	public SpiderEngine(int size) {
		super(size);
	}

	/**
	 * 注册任务
	 * 
	 * @param spider
	 */
	protected void register(Spider spider) {
		assertNotNull(spider);
		assertNotNull(selector);
		AsynchronousSocketChannel channel;
		try {
			channel = toChannel(spider);
			// channel.
		} catch (IOException e) {
			logger.error("注册任务失败：" + spider, e);
		}
	}

	public void start() {
		assertNotNull(selector);
		int count = 0;
		status = STATUS_RUNNING;
		while (true) {
			// 读入数据
			if (limit() > count) {
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

	protected void initialize() {
		try {
			selector = Selector.open();
			group = AsynchronousChannelGroup.withThreadPool(getTaskExecutor());
			logger.info("引擎初始化完毕");
		} catch (IOException e) {
			logger.error("引擎初始化失败", e);
		} finally {
			IOUtils.closeQuietly(selector);
			selector = null;
		}
	}

	@Override
	protected AsynchronousSocketChannel toChannel(Spider t) throws IOException {
		return AsynchronousSocketChannel.open(group);
	}
}
