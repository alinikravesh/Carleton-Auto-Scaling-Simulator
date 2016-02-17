package Application;

import java.math.BigDecimal;
import java.math.RoundingMode;

import Common.IaaS;

public class SoftwareTier {
	private double serviceDemand; 
	private double accessRate; 
	private String name;
	private IaaS iaas;
	
	public SoftwareTier(String tname, double demand, double rate, IaaS infrastructure)
	{
		name = tname;
		serviceDemand = demand;
		accessRate = rate;
		iaas = infrastructure;
	}
	
	public double GetDemand()
	{
		return serviceDemand;
	}
	
	public double GetAccessRate()
	{
		return accessRate;
	}
	
	public double GetResponseTime(double load)
	{
		double responseTime = 0.0;
		double Workload = (load * accessRate)/iaas.GetNumberOfUpVms();
		responseTime = serviceDemand/(1-serviceDemand*Workload); 
		BigDecimal tmp = new BigDecimal(responseTime);
		tmp = tmp.setScale(2, RoundingMode.HALF_UP);
		responseTime = tmp.doubleValue();
		return responseTime;
	}
	
	public void EndExperiment(int duration)
	{
		iaas.EndExperiment(duration);
	}
	
	public int GetOperationalCost()
	{
		return iaas.GetOperationalCost();
	}
	
	public int GetVmThrashing()
	{
		return iaas.GetVmThrashing();
	}
}
