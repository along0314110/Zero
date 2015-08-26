/**
 * Copyright (C) Ye Bin
 *
 * 2015年8月26日
 *
 * Ye Bin (along0314110@163.com)
 */
package workman.task;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SocketChannel;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 抽象蜘蛛
 *
 * @anthor yebin
 * @data 2015年8月26日
 */
public class Spider implements Task<SelectableChannel> {

	private final Logger logger = LogManager.getLogger(getClass());

	private InetSocketAddress remote;
	private SelectableChannel channel;

	public Spider(String host, int port) {
		assertTrue(StringUtils.isNoneBlank(host));
		remote = new InetSocketAddress(host, port);
		if (remote.getAddress() == null) {
			throw new RuntimeException("无效地址：" + host);
		}
		try {
			channel = SocketChannel.open(remote);
		} catch (IOException e) {
			logger.error("网络错误：" + remote);
			logger.debug(e);
			IOUtils.closeQuietly(channel);
		}
	}

	public SelectableChannel getChannel() {
		return channel;
	}
}
