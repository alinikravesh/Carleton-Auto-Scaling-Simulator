//Sets infrastructural variables
public class InfrastructurePropertires {
	protected static int appLayerVmBootUpTime = 10; //Boot-up time of application layer VMs
	protected static int dbLayerVmBootUpTime = 15; //Boot-up time of database layer VMs
	protected static int appVmCapacityPerMinute = 20; //Capacity of application layer VMs
	protected static int dbVmCapacityPerMinute = 30; //Capacity of database layer VMs
	protected static int monitoringInterval = 5; //Monitoring interval of the auto-scaling system
}
