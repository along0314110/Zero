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
public class FilePrinter implements Task<Path> {
	private Path path;

	public FilePrinter(Path path) {
		this.path = path;
	}

	public Path getContext() {
		return path;
	}
}
