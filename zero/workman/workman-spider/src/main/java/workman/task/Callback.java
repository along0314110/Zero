package workman.task;

public interface Callback<T> {
	void completed(T result);
}
