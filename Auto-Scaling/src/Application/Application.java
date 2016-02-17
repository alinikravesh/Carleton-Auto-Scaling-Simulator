package Application;
import java.util.ArrayList;
import java.util.List;


public class Application {
	private List<SoftwareTier> tiers = new ArrayList<SoftwareTier>();
	private int tierCount = 0;
	
	public void AddTier(SoftwareTier st)
	{
		tiers.add(st);
		tierCount++;
	}
	
	public int GetTierCount()
	{
		return tierCount; 
	}
	public double GetResponseTime(double load)
	{
		double responseTime = 0.0;
		for(SoftwareTier st : tiers)
		{
			responseTime += st.GetResponseTime(load);
		}
		return responseTime;
	}
	
	public void EndExperiment(int duration)
	{
		for(SoftwareTier st: tiers)
		{
			st.EndExperiment(duration);
		}
	}
	
	public int GetOperationalCost()
	{
		int cost = 0;
		for(SoftwareTier st: tiers)
		{
			cost += st.GetOperationalCost();
		}
		return cost;
	}
	
	public int GetVmThrashing()
	{
		int thrashing = 0;
		for(SoftwareTier st: tiers)
		{
			thrashing += st.GetVmThrashing();
		}
		return thrashing;
	}
}
