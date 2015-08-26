/**
 * Copyright (C) Ye Bin
 *
 * 2015年8月26日
 *
 * Ye Bin (along0314110@163.com)
 */
package workman.printer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.channels.Channels;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 文件打印机
 *
 * @anthor yebin
 * @data 2015年8月26日
 */
public class FilePrinter implements Printer {
	private final Logger logger = LogManager.getLogger(getClass());

	private final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
	private final int DEFAULT_BUFFER_SIZE = 100;
	private SocketChannel socketChannel;
	private File file;

	public FilePrinter(SocketChannel socketChannel, File file) {
		this.socketChannel = socketChannel;
		this.file = file;
	}

	public void run() {
		CharsetDecoder cd = DEFAULT_CHARSET.newDecoder();
		Writer output = null;
		Reader input = null;
		try {
			output = new FileWriter(file);
			input = Channels.newReader(socketChannel, cd, DEFAULT_BUFFER_SIZE);
			IOUtils.copy(input, output);
		} catch (FileNotFoundException e) {
			logger.error("文件不存在：" + file.getAbsolutePath());
			logger.debug(e);
		} catch (IOException e) {
			logger.error("数据传输错误：" + file.getAbsolutePath());
			logger.debug(e);
		} finally {
			IOUtils.closeQuietly(output);
			IOUtils.closeQuietly(input);
			IOUtils.closeQuietly(socketChannel);
		}
	}

}
