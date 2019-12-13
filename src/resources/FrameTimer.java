package resources;

public class FrameTimer {

	/*
	 * simple frame counter and is in a class so I dont have to make an additional 2 vars
	 * every class just to delay it (E.g Int delay, Int limit)
	 * with additional functionality like pausing and less bugs.
	 */
	
	private int delay, limit;
	private boolean isDone = false, isPaused;
	
	public FrameTimer(int limit, boolean isDone) {
		this.limit = limit;
		if(isDone) {
			delay = limit;
		}
	}
	
	public void update() {
		if(!isPaused) {
			if(delay < limit) delay++;
			else isDone = true;
		}
	}
	
	public boolean isDone() {
		return isDone;
	}
	public boolean isPaused() {
		return isPaused;
	}
	public int getFrameCount() {
		return delay;
	}
	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}
	public void resetTimer() {
		delay = 0;
		isDone = false;
	}
	public void completeTimer() {
		delay = limit;
	}
	
	public void pause() {
		isPaused = true;
	}
	public void start() {
		isPaused = false;
	}
}
