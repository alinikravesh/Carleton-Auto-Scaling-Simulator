package Common;
import java.io.File; 
import java.io.IOException;
import jxl.*;
import jxl.read.biff.BiffException;

//Emulates monitor component of auto-scaling system
public class Monitor {
	private String inputFile;
	private String inputFileActual;
	
	//Sets path of input excel file that contains workload trace
	public void SetInputFile(String path)
	{
		this.inputFile = path;
	}
	
	public void SetInputFileActual(String path)
	{
		this.inputFileActual = path;
	}
	public double GetWorkloadActual(int timeslot) throws IOException
	{
		double workload = 0;
		int timeIndex = timeslot/Simulator.monitoringInterval;
		File inputWorkBook = new File(inputFileActual);
		Workbook w;
		try{
			w = Workbook.getWorkbook(inputWorkBook);
			Sheet sheet = w.getSheet(0);
			workload = Double.parseDouble(sheet.getCell(0,timeIndex).getContents());
		} catch (BiffException e){
			e.printStackTrace();
		}
		return workload;
	}
	
	//Returns workload of the specified timeslot
	public double GetWorkload(int timeslot) throws IOException
	{
		double workload = 0;
		int timeIndex = timeslot/Simulator.monitoringInterval;
		File inputWorkBook = new File(inputFile);
		Workbook w;
		try{
			w = Workbook.getWorkbook(inputWorkBook);
			Sheet sheet = w.getSheet(0);
			workload = Double.parseDouble(sheet.getCell(0,timeIndex).getContents());
		} catch (BiffException e){
			e.printStackTrace();
		}
		return workload;
	}
	
	//Returns duration of the experiment
	public int GetExperimentDuration() throws IOException
	{
		int duration = 0;
		File inputWorkBook = new File(inputFile);
		Workbook w;
		try{
			w = Workbook.getWorkbook(inputWorkBook);
			Sheet sheet = w.getSheet(0);
			duration = sheet.getRows();
		} catch (BiffException e){
			e.printStackTrace();
		}
		return duration;
	}
}
