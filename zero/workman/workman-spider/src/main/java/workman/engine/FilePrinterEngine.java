package workman.engine;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.file.OpenOption;
import java.nio.file.StandardOpenOption;
import java.util.Set;
import java.util.TreeSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import workman.task.FilePrinter;

public class FilePrinterEngine extends
		AbstractEngine<FilePrinter, AsynchronousFileChannel> {
	private static final Logger logger = LogManager
			.getLogger(FilePrinterEngine.class);

	private Set<OpenOption> options;

	public FilePrinterEngine(int size) {
		super(size);
	}

	/**
	 * 注册任务
	 * 
	 * @param spider
	 */
	protected void register(FilePrinter filePrinter) {
		assertNotNull(filePrinter);

	}

	public void start() {
		int count = 0;
		status = STATUS_RUNNING;
		while (true) {
			if (limit() > count) {
				register(taskQueue.poll());
				count++;
			}
		}
	}

	public void close() {
		getTaskExecutor().shutdown();
		status = STATUS_CLOSED;
	}

	protected void initialize() {
		options = new TreeSet<OpenOption>();
		options.add(StandardOpenOption.WRITE);
	}

	@Override
	protected AsynchronousFileChannel toChannel(FilePrinter t)
			throws IOException {

		return AsynchronousFileChannel.open(t.getContext(), options,
				getTaskExecutor());
	}

}
