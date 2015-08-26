/**
 * Copyright (C) Ye Bin
 *
 * 2015年8月26日
 *
 * Ye Bin (along0314110@163.com)
 */
package workman.spider;

import java.net.SocketAddress;

/**
 * 蜘蛛接口
 *
 * @anthor yebin
 * @data 2015年8月26日
 */
public interface Spider {
	/**
	 * 获取地址
	 * 
	 * @return
	 */
	public SocketAddress getSocketAddress();
}
