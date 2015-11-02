package main;

/** Represents a game session. Handles game cycling. */
public class Game implements CustomRunnable
{
	/** Thread manager for this object. */
	private ThreadClock clock;
	
	/** Sets up a standard game session. */
	public Game()
	{
		clock = new ThreadClock(10);
	}
	
	@Override
	public void start()
	{
		run();
	}

	@Override
	public void run()
	{
		int i = 10;
		while(i > 0)
		{
			main.input.InputManager.poll();
			main.input.InputManager.clear();
			main.input.InputManager.clear();
			main.input.InputManager.poll();
			clock.pauseThread(100);
			main.input.InputManager.poll();
			main.input.InputManager.poll();
			i -= 1;
			clock.nextCycle();
		}
		clock.pauseThread(1000);
		System.exit(0);
	}
}
