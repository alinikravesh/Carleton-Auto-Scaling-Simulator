package ScalingUnit;
import Application.Application;

public abstract class DecisionMaker {
	public abstract void GenerateScalingAction(double load, int time, Application app);
}
