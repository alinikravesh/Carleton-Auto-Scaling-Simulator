package Application;
import java.math.BigDecimal;
import java.math.RoundingMode;

import Common.IaaS;


public class Application {
	public double businessTierServiceDemand;
	public double databaseTierServiceDemand; 
	private IaaS businessTierIaas;
	private IaaS databaseTierIaas; 
	private double databaseAccessRate;
	
	public Application(IaaS biaas, IaaS diaas, double bServDemand, double dServDemand, double dbAccessrate){
		businessTierIaas = biaas;
		databaseTierIaas = diaas;
		businessTierServiceDemand = bServDemand;
		databaseTierServiceDemand = dServDemand;
		databaseAccessRate = dbAccessrate;
	}
	
	public double GetResponseTime(double load)
	{
		double responseTime = 0.0;
		double businessWorkload = load / businessTierIaas.GetNumberOfUpVms();
		double databaseWorkload = (load * databaseAccessRate) / databaseTierIaas.GetNumberOfUpVms();
		responseTime = ((businessTierServiceDemand/(1-businessTierServiceDemand*businessWorkload)) + 
				(databaseTierServiceDemand/(1-databaseTierServiceDemand*databaseWorkload)));
		BigDecimal tmp = new BigDecimal(responseTime);
		tmp = tmp.setScale(2, RoundingMode.HALF_UP);
		responseTime = tmp.doubleValue();
		return responseTime;
	}
	
	public void EndExperiment(int duration)
	{
		businessTierIaas.EndExperiment(duration);
		databaseTierIaas.EndExperiment(duration);
	}
	
	public int GetOperationalCost()
	{
		return businessTierIaas.GetOperationalCost() + databaseTierIaas.GetOperationalCost();
	}
	
	public int GetVmThrashing()
	{
		return businessTierIaas.GetVmThrashing() + databaseTierIaas.GetVmThrashing();
	}
	
	public IaaS GetBtIaaS()
	{
		return this.businessTierIaas;
	}
	
	public IaaS GetDtIaaS()
	{
		return this.databaseTierIaas;
	}
	
	public double GetBussServDemand()
	{
		return businessTierServiceDemand;
	}
	
	public double GetDbServDemand()
	{
		return databaseTierServiceDemand;
	}
	
	public double GetDbAccessRate()
	{
		return databaseAccessRate;
	}
}
