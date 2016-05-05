package Common;

import Application.Application;

public interface ScalingUnitInterface {
	public void GenerateScalingAction(double load, int time, Application app, double actualLoad);
}
