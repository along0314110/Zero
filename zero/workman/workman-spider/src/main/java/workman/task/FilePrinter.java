/**
 * Copyright (C) Ye Bin
 *
 * 2015年8月26日
 *
 * Ye Bin (along0314110@163.com)
 */
package workman.task;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectableChannel;
import java.nio.file.Path;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 文件打印机
 *
 * @anthor yebin
 * @data 2015年8月26日
 */
public class FilePrinter implements Task<FileChannel> {
	private final Logger logger = LogManager.getLogger(getClass());

	private SelectableChannel selectableChannel;
	private FileChannel fileChannel;

	public FilePrinter(SelectableChannel selectableChannel, Path path) {
		this.selectableChannel = selectableChannel;
		try {
			fileChannel = FileChannel.open(path);
		} catch (IOException e) {
			logger.error("读取文件错误：" + path);
			logger.debug(e);
			IOUtils.closeQuietly(selectableChannel);
			IOUtils.closeQuietly(fileChannel);
		}
	}

	public void print() {
		if (selectableChannel.isOpen() && fileChannel.isOpen()) {

		}
	}

	public FileChannel getChannel() {
		return fileChannel;
	}
}
