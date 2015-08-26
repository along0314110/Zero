package workman.task;

import java.io.IOException;
import java.nio.channels.Channel;

public interface Task<C extends Channel> {
	/**
	 * 获得Channel
	 * 
	 * @return
	 * @throws IOException
	 */
	C getChannel() throws Exception;
}
