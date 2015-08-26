package workman.task;

/**
 * 任务
 * 
 * @author yebin
 *
 */
public interface Task<C> {
	C getContext();
}
