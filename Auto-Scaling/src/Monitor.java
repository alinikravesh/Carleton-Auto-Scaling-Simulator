import java.io.File; 
import java.io.IOException;
import jxl.*;
import jxl.read.biff.BiffException;

//Emulates monitor component of auto-scaling system
public class Monitor extends InfrastructurePropertires{
	private String inputFile;
	
	//Sets path of input excel file that contains workload trace
	public void SetInputFile(String path)
	{
		this.inputFile = path;
	}
	
	//Returns workload of the specified timeslot
	public double GetWorkload(int timeslot) throws IOException
	{
		double workload = 0;
		int timeIndex = timeslot/monitoringInterval;
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
