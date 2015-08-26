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
import java.net.SocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import workman.spider.Spider;

/**
 * NIO引擎
 *
 * @anthor yebin
 * @data 2015年8月26日
 */
public class DefaultEngine extends AbstractEngine {

	private final Logger logger = LogManager.getLogger(getClass());
	private Selector selector;

	public DefaultEngine(int size) {
		super(size);
		initialize();
	}

	/**
	 * 初始化
	 * 
	 * @throws IOException
	 */
	private void initialize() {
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
	 * 注册蜘蛛
	 * 
	 * @param spider
	 */
	protected void register(Spider spider) {
		assertNotNull(spider);
		assertNotNull(selector);
		SocketAddress address = spider.getSocketAddress();
		SocketChannel channel = null;
		try {
			channel = SocketChannel.open(address);
			channel.register(selector, SelectionKey.OP_READ);
		} catch (IOException e) {
			logger.error("注册SocketChannel失败：" + address);
			logger.debug(e);
		} finally {
			IOUtils.closeQuietly(channel);
		}
	}

	public void start() {
		assertNotNull(selector);
		int count = 0;
		int limit = limit();
		while (true) {
			// 读入数据
			while (limit > count) {
				register(spiderQueue.poll());
				count++;
			}
			Iterator<SelectionKey> it = selector.selectedKeys().iterator();
			while (it.hasNext()) {
				SelectionKey key = it.next();
				if (key.isReadable()) {
					count--;
				}
				it.remove();
			}
		}
	}

	public void close() {
		IOUtils.closeQuietly(selector);
	}

	/**
	 * 数量限制
	 * 
	 * @return
	 */
	private synchronized int limit() {
		return spiderQueue.remainingCapacity() + spiderQueue.size();
	}
}
