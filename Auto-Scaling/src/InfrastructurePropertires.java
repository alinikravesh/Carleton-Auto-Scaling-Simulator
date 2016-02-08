//Sets infrastructural variables
public class InfrastructurePropertires {
	protected static int appLayerVmBootUpTime = 10; //Boot-up time of application layer VMs
	protected static int dbLayerVmBootUpTime = 15; //Boot-up time of database layer VMs
	protected static int appVmCapacityPerMinute = 13; //Capacity of application layer VMs
	protected static int dbVmCapacityPerMinute = 30; //Capacity of database layer VMs
	protected static int monitoringInterval = 5; //Monitoring interval of the auto-scaling system
	protected static double databaseAccessRate = 0.7;
	protected static boolean dontKillVmBeforeFullHour = true; 
	protected static double serviceDemand = 0.076;
	protected static double responseTimeThreshold = 6.0; 
}
