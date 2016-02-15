/* On my honor, I have neither given nor received unauthorized aid on this assignment */
import java.io.*;
import java.util.*;

public class MIPSsim {
	
	ArrayList<String> INM = new ArrayList<String>();
	ArrayList<String> INB = new ArrayList<String>();
	ArrayList<String> AIB = new ArrayList<String>();
	ArrayList<String> SIB = new ArrayList<String>();
	ArrayList<String> PRB = new ArrayList<String>();
	ArrayList<String> ADB = new ArrayList<String>();
	ArrayList<String> REB = new ArrayList<String>();
	ArrayList<String> RGF = new ArrayList<String>();
	ArrayList<String> DAM = new ArrayList<String>();
	public static BufferedWriter bwout;
	public static FileReader filereader;
	public static BufferedReader br;
	int[] registers = new int[16];
	int[] memory = new int[16]; 
	


	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		MIPSsim ms = new MIPSsim();
		File regfile = new File("registers.txt");
		File instfile = new File("instructions.txt");
		File datafile = new File("datamemory.txt");
		for(int i = 0; i < 16 ; i++){
			ms.registers[i] = Integer.MIN_VALUE;
			ms.memory[i] = Integer.MIN_VALUE;
		}
		try {
			filereader = new FileReader(regfile);
			br = new BufferedReader(filereader);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while(br.readLine() != null){
			String readregister = (br.readLine()).substring
										(1, (br.readLine()).length() - 1);
			String[] splitregister = readregister.split(",");
			int j = Integer.parseInt
					(splitregister[0].substring(1, splitregister[0].length()));
			ms.registers[j] = Integer.parseInt(splitregister[1]);
		}
		
		try {
			filereader = new FileReader(datafile);
			br = new BufferedReader(filereader);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while(br.readLine() != null){
			String readmemory = (br.readLine()).substring
										(1, (br.readLine()).length() - 1);
			String[] splitmemory = readmemory.split(",");
			int j = Integer.parseInt(splitmemory[0]);
			ms.registers[j] = Integer.parseInt(splitmemory[1]);
		}
		
		try {
			filereader = new FileReader(instfile);
			br = new BufferedReader(filereader);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while(br.readLine() != null){
			(ms.INM).add(br.readLine());
		}
		
		bwout = new BufferedWriter(new FileWriter("correct_simulation.txt"));
		boolean notCompleted = ms.ADB.size()>0 || ms.AIB.size()>0 ||
				ms.INB.size()>0 || ms.INM.size()>0 || ms.PRB.size()>0 ||
				ms.REB.size()>0 || ms.SIB.size()>0;
	}

}
