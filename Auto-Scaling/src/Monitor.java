import java.util.List;
import java.io.File; 
import java.io.IOException;
import java.util.ArrayList;

import jxl.*;
import jxl.write.*;
import jxl.read.*;
import jxl.read.biff.BiffException;

public class Monitor extends InfrastructurePropertires{
	private String inputFile;
	private static double p = 0.7;	
	List<Integer> upactions = new ArrayList<Integer>();
	public void setInputFile(String path)
	{
		this.inputFile = path;
	}
	
	public void Run() throws IOException
	{
		DecisionMaker mldm = new DecisionMaker();
		mldm.Setup(); 
		File inputWorkBook = new File(inputFile);
		Workbook w;
		try{
			w = Workbook.getWorkbook(inputWorkBook);
			Sheet sheet = w.getSheet(0);
			int count_all = sheet.getRows();	
			Timer t = new Timer(count_all, monitoringInterval, appVmCapacityPerMinute, dbVmCapacityPerMinute, appLayerVmBootUpTime, dbLayerVmBootUpTime);
			for(int j=0; j< sheet.getRows(); j++)
			{
				if (j ==6);
				double load = Double.parseDouble(sheet.getCell(0,j).getContents());
				String appLayerScalingDecision = mldm.SimpleAppLayerDecisionMaker(load);
				int time = UpdateTimer(appLayerScalingDecision, j * monitoringInterval);
				if (time > j * monitoringInterval)
				{
					t.updateActionLog(time, appLayerScalingDecision);
					t.updateActionLog(j * monitoringInterval, "N");
				}
				else if (time == j * monitoringInterval){
					t.updateActionLog(time, appLayerScalingDecision);
				}
					
				//String dbLayerScalingDecision = mldm.DBLayerDecisionMaker((int)(load * p + 0.5)); // rounds up the load value at the database layer
			}
			t.tick();
			t.print();

		} catch (BiffException e){
			e.printStackTrace();
		}
	}
	
	private int UpdateTimer(String appLayerScalingDecision, int t) {
		int time = 0; 
		switch (appLayerScalingDecision) {
		case "BU":
			int tbu1 = (t + appLayerVmBootUpTime) / monitoringInterval;
			int tbu = 0;
			if ((t + appLayerVmBootUpTime) % monitoringInterval != 0)
				tbu = (tbu1 + 1) * monitoringInterval;
			else
				tbu = tbu1 * monitoringInterval; 
			time = tbu;
			upactions.add(time);
			//System.out.println(time);
			break;
		case "N":
			if (upactions.contains(t))
				time = -1;
			else
				time = t;
			break;
		case "BD":
			if (upactions.contains(t))
				time = -1;
			else
				time = t;
			break;
		default:
			break;
		}
		return time;
	}

	public int GetExperimentDuration() throws IOException
	{
		int duration = 0;
		File inputWorkBook = new File(inputFile);
		Workbook w;
		try{
			w = Workbook.getWorkbook(inputWorkBook);
			Sheet sheet = w.getSheet(0);
			int count_all = sheet.getRows();
			duration = count_all;
		} catch (BiffException e){
			e.printStackTrace();
		}
		return duration;
	}
		
	
	public static void main(String[] args) throws IOException
	{
		Monitor test = new Monitor(); 
		test.setInputFile("M:\\Research\\CloudComputing\\DatabaseExperiment\\CyclicWorkload.xls");
		test.Run();
	}
}
