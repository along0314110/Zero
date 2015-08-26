package workman.engine;

import static org.junit.Assert.assertNotNull;

import java.nio.channels.FileChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import workman.task.FilePrinter;

public class FilePrinterEngine extends AbstractEngine<FilePrinter> {
	private ExecutorService executorService;

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
		FileChannel channel = filePrinter.getChannel();
	}

	public void start() {
		assertNotNull(executorService);
		int count = 0;
		status = STATUS_RUNNING;
		while (true) {
			while (limit() > count) {
				register(taskQueue.poll());
				count++;
			}
		}
	}

	public void close() {
		executorService.shutdown();
		status = STATUS_CLOSED;
	}

	protected void initialize() {
		executorService = Executors.newFixedThreadPool(limit());
	}

}
