package Application;
import java.util.ArrayList;
import java.util.List;

import Common.IaaS;


public class Application {
	public static double responseTimeThreshold = 6.0;
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
	
	public List<SoftwareTier> GetTiers()
	{
		return tiers; 
	}
	
	public SoftwareTier GetTier(int num)
	{
		SoftwareTier res = new SoftwareTier("null", 0.0, 0.0, new IaaS(0, 0.0), 0); 
		for(SoftwareTier st : tiers)
		{
			if (st.GetNumber() == num)
			{
				res = st;
			}
		}
		return res;
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
