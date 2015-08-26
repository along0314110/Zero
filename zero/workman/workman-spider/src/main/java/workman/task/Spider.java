/**
 * Copyright (C) Ye Bin
 *
 * 2015年8月26日
 *
 * Ye Bin (along0314110@163.com)
 */
package workman.task;

import static org.junit.Assert.assertTrue;

import java.net.InetSocketAddress;

import org.apache.commons.lang3.StringUtils;

/**
 * 抽象蜘蛛
 *
 * @anthor yebin
 * @data 2015年8月26日
 */
public class Spider implements Task<InetSocketAddress> {
	
	private InetSocketAddress remote;

	public Spider(String host, int port) {
		assertTrue(StringUtils.isNoneBlank(host));
		remote = new InetSocketAddress(host, port);
		if (remote.getAddress() == null) {
			throw new RuntimeException("无效地址：" + host);
		}

	}

	public InetSocketAddress getContext() {
		return remote;
	}
}
