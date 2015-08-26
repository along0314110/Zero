/**
 * Copyright (C) Ye Bin
 *
 * 2015年8月26日
 *
 * Ye Bin (along0314110@163.com)
 */
package workman.spider;

import static org.junit.Assert.assertTrue;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

import org.apache.commons.lang3.StringUtils;

/**
 * 抽象蜘蛛
 *
 * @anthor yebin
 * @data 2015年8月26日
 */
public abstract class AbstractSpider implements Spider {
	private InetSocketAddress address;

	public AbstractSpider(String host, int port) {
		assertTrue(StringUtils.isNoneBlank(host));
		address = new InetSocketAddress(host, port);
		if (address.getAddress() == null) {
			throw new RuntimeException("无效地址：" + host);
		}
	}

	/**
	 * 获取地址
	 * 
	 * @return
	 */
	public SocketAddress getSocketAddress() {
		return address;
	}
}
