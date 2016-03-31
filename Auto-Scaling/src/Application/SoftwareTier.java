package Application;

import java.math.BigDecimal;
import java.math.RoundingMode;

import Common.IaaS;

public class SoftwareTier {
	private double serviceDemand; 
	private double accessRate; 
	private String name;
	private int number;
	private IaaS iaas;
	
	public SoftwareTier(String tname, double demand, double rate, IaaS infrastructure, int num)
	{
		name = tname;
		serviceDemand = demand;
		accessRate = rate;
		iaas = infrastructure;
		number = num;
	}
	
	public double GetDemand()
	{
		return serviceDemand;
	}
	
	public double GetAccessRate()
	{
		return accessRate;
	}
	
	public int GetNumber()
	{
		return number;
	}
	
	public String GetName()
	{
		return name;
	}
	
	public IaaS GetIaaS()
	{
		return iaas;
	}
	
	public double GetResponseTime(double load)
	{
		double responseTime = 0.0;
		double Workload = (load * accessRate)/iaas.GetNumberOfUpVms();
		responseTime = serviceDemand/(1-serviceDemand*Workload); 
		BigDecimal tmp = new BigDecimal(responseTime);
		tmp = tmp.setScale(2, RoundingMode.HALF_UP);
		responseTime = tmp.doubleValue();
		if (responseTime < 0)
			responseTime = 100;
		return responseTime;
	}
	
	public double GetResponseTimeFloor(double load)
	{
		double responseTime = 0.0;
		double Workload = 0.0;
		if (iaas.GetNumberOfUpVms() > 1)
		{
			Workload = (load * accessRate)/(iaas.GetNumberOfUpVms() - 1);	
		}
		else
		{
			Workload = (load * accessRate)/(iaas.GetNumberOfUpVms());
		}
		responseTime = serviceDemand/(1-serviceDemand*Workload); 
		BigDecimal tmp = new BigDecimal(responseTime);
		tmp = tmp.setScale(2, RoundingMode.HALF_UP);
		responseTime = tmp.doubleValue();
		if (responseTime < 0)
			responseTime = GetResponseTime(load);
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
	
	public int GetVmCount(int s, int e)
	{
		return iaas.hourVmGet(s, e);
	}
	
	public int GetVmThrashing()
	{
		return iaas.GetVmThrashing();
	}
}
