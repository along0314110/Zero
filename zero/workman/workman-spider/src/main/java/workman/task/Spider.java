/**
 * Copyright (C) Ye Bin
 *
 * 2015年8月28日
 *
 * Ye Bin (along0314110@163.com)
 */
package workman.task;

/**
 * 爬虫
 *
 * @anthor yebin
 * @data 2015年8月28日
 */
public interface Spider {
	String getUrl();

	Callback<String> getCallback();
}
