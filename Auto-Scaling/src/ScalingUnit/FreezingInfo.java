package ScalingUnit;

public class FreezingInfo {
	public int number = -1; 
	public boolean status = false;
	public int duration = 0;
	
	public FreezingInfo(int num, boolean state, int dur) {
		number = num;
		status = state;
		duration = dur;
	}
	
}
