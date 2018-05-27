package bgu.spl.mics;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Logger;

import org.omg.CORBA.Current;
public class TimeService extends MicroService {
	private int duration;
	private int speed;
	private   int currentTick;
	private Timer timer;
	private static final Logger LOGGER=Logger.getLogger(ManagementService.class.getName());
	/**
	 * in charge of the time
	 * sending the Current time as tick broadcast to all other services. 
	 * in charge of terminate other services in the end of the duration
	 * 
	 * @param speed for each tick by milliseconds.
	 * @param duration of the program(how much ticks).
	 */
	public TimeService(int speed,int duration) {
		super("timer");
		this.duration=duration;
		this.speed=speed;
		this.currentTick=1;
	}
	class TimerAction extends TimerTask {
		public void run(){
		if(currentTick<=duration)
		LOGGER.info("the current tick is: "+ currentTick);
		TickBroadcast t1=new TickBroadcast(currentTick);
		sendBroadcast(t1);
		currentTick++;
		if(currentTick>duration+1){
			TerminateBrodcast terminate=new TerminateBrodcast();
			sendBroadcast(terminate);
			timer.cancel();
		}
		
		
		}
	}
	
	@Override
	protected void initialize() {
		timer=new Timer();
		subscribeBroadcast(TerminateBrodcast.class,res->{
			LOGGER.info(this.getName()+" terminating");
			terminate();
		});
		timer.schedule(new TimerAction(),speed, speed);
		
	
}
}