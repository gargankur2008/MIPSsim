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
	//ArrayList<String> RGF = new ArrayList<String>();
	//ArrayList<String> DAM = new ArrayList<String>();
	public BufferedWriter bwout;
	public static FileReader filereader;
	public static BufferedReader br;
	int[] registers = new int[16];
	int[] memory = new int[16];
	int counter; 
	


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
		String readreg = br.readLine();
		while(readreg != null){
			String readregister = readreg.substring
					(1, readreg.length() - 1);
			String[] splitregister = readregister.split(",");
			//System.out.println(splitregister[0]);
			//System.out.println(splitregister[1]);
			int j = Integer.parseInt
					(splitregister[0].substring(1, splitregister[0].length()));
			ms.registers[j] = Integer.parseInt(splitregister[1]);
			readreg = br.readLine();
		}
		//System.out.println(ms.registers[15]);
		
		try {
			filereader = new FileReader(datafile);
			br = new BufferedReader(filereader);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String readdata = br.readLine();
		while(readdata != null){
			String readmemory = readdata.substring
										(1, readdata.length() - 1);
			String[] splitmemory = readmemory.split(",");
			int j = Integer.parseInt(splitmemory[0]);
			ms.memory[j] = Integer.parseInt(splitmemory[1]);
			readdata = br.readLine();
		}
		
		try {
			filereader = new FileReader(instfile);
			br = new BufferedReader(filereader);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String instread = br.readLine();
		while(instread != null){
			ms.INM.add(instread);
			instread = br.readLine();
		}
		
		ms.bwout = new BufferedWriter(new FileWriter("simulation.txt"));
		boolean notCompleted = ms.ADB.size()>0 || ms.AIB.size()>0 ||
				ms.INB.size()>0 || ms.INM.size()>0 || ms.PRB.size()>0 ||
				ms.REB.size()>0 || ms.SIB.size()>0;
		ms.counter  = 0;
		ms.outfile(ms,notCompleted);
		while(notCompleted){
			//REB to RGF
			if(ms.REB.size() != 0){
				String REBtemp = ms.REB.get(0);	
				ms.REB.remove(0);
				String tempsub = REBtemp.substring(1, REBtemp.length() - 1);
				String[] breakREB = tempsub.split(",");
				int i = Integer.parseInt
					(breakREB[0].substring(1, breakREB[0].length()));
				ms.registers[i] = Integer.parseInt(breakREB[1]);
			}
			
			//PRB to REB
			if(ms.PRB.size() != 0){
				String PRBtemp = ms.PRB.get(0);
				ms.PRB.remove(0);
				String tempsub = PRBtemp.substring(1, PRBtemp.length() - 1);
				String[] breakPRB = tempsub.split(",");
				int val1 = Integer.parseInt(breakPRB[2]);
				int val2 = Integer.parseInt(breakPRB[3]);
				String PRBtoREB = "<" + breakPRB[1] + "," + (val1 * val2) + ">";
				ms.REB.add(PRBtoREB);
			}
			
			//AIB to REB/PRB
			if(ms.AIB.size() != 0){
				String AIBtemp = ms.AIB.get(0);
				ms.AIB.remove(0);
				String tempsub = AIBtemp.substring(1, AIBtemp.length() - 1);
				String[] breakAIB = tempsub.split(",");
			//	System.out.println(breakAIB[0]);
			//	System.out.println(breakAIB[1]);
			//	System.out.println(breakAIB[2]);

				int val1 = Integer.parseInt(breakAIB[2]);
				int val2 = Integer.parseInt(breakAIB[3]);
			//	int val1 = 0, val2 = 0;
				int finalval = 0;
				if(breakAIB[0].equals("MUL")){
					ms.PRB.add(AIBtemp);
				}else{
					if(breakAIB[0].equals("ADD")){
						finalval = val1 + val2;
					}else if(breakAIB[0].equals("SUB")){
						finalval = val1 - val2;
					}
					String AIBtoREB = "<" + breakAIB[1] + "," + finalval + ">";
					ms.REB.add(AIBtoREB);
				}
			}
			
			//ADB to DAM
			if(ms.ADB.size() != 0){
				String ADBtemp = ms.ADB.get(0);
				ms.ADB.remove(0);
				String tempsub = ADBtemp.substring(1, ADBtemp.length() - 1);
				String[] breakADB = tempsub.split(",");
				int i = Integer.parseInt(breakADB[0].substring
						(1, breakADB[0].length()));
				int j = Integer.parseInt(breakADB[1]);
				ms.memory[j] = ms.registers[i];
			}
			
			//SIB to ADB
			if(ms.SIB.size() != 0){
				String SIBtemp = ms.SIB.get(0);
				ms.SIB.remove(0);
				String tempsub = SIBtemp.substring(1, SIBtemp.length() - 1);
				String[] breakSIB = tempsub.split(",");
				int val1 = Integer.parseInt(breakSIB[2]);
				int val2 = Integer.parseInt(breakSIB[3]);
				String SIBtoADB = "<" + breakSIB[1] + "," + (val1 + val2) + ">";
				ms.ADB.add(SIBtoADB);
			}
			
			//INB to AIB/SIB
			if(ms.INB.size() != 0){
				String INBtemp = ms.INB.get(0);
				ms.INB.remove(0);
				String tempsub = INBtemp.substring(1, INBtemp.length() - 1);
				String[] breakINB = tempsub.split(",");
				if(breakINB[0].equals("ST")){
					ms.SIB.add(INBtemp);
				}else{
					ms.AIB.add(INBtemp);
				}
			}
			
			//INM to INB
			if(ms.INM.size()!= 0){
				String INMtemp = ms.INM.get(0);
				boolean regready = true;
				String tempsub = INMtemp.substring(1, INMtemp.length() - 1);
				String[] breakINM = tempsub.split(",");
				int value1 = Integer.MIN_VALUE;
				int value2 = Integer.MIN_VALUE;
				boolean isRegister1 = false;
				boolean isRegister2 = false;
				if(breakINM[2].charAt(0) == 'R'){
					isRegister1 = true;
					int i = Integer.parseInt(breakINM[2].substring
							(1, breakINM[2].length()));
					value1 = ms.registers[i];
				//	System.out.println(value1);
				}
				if(breakINM[3].charAt(0) == 'R'){
					isRegister2 = true;
					int j = Integer.parseInt(breakINM[3].substring
							(1, breakINM[3].length()));
				//	System.out.println(j);
					value2 = ms.registers[j];
				//	System.out.println(value2);
				}
				if(value1 == Integer.MIN_VALUE || value2 == Integer.MIN_VALUE)
				{
					regready = false;
				}
				String reg1value = "";
				String reg2value = "";
				if(isRegister1){
					reg1value = String.valueOf(value1);
				}
				else{
					reg1value = breakINM[2];
				}
				if(isRegister2){
					reg2value = String.valueOf(value2);
				}
				else{
					reg2value = breakINM[3];
				}
				String INMtoINB = "<" + breakINM[0] + "," + breakINM[1] + "," + reg1value + "," + reg2value + ">";
					ms.INB.add(INMtoINB);
					//System.out.println(ms.INB.get(0));
					ms.INM.remove(0);

			}
			
			
			
			notCompleted = ms.ADB.size()>0 || ms.AIB.size()>0 ||
					ms.INB.size()>0 || ms.INM.size()>0 || ms.PRB.size()>0 ||
					ms.REB.size()>0 || ms.SIB.size()>0;
			ms.outfile(ms, notCompleted);
		}
		ms.bwout.close();
	}



	private void outfile(MIPSsim ms, boolean notCompleted) throws IOException {
		// TODO Auto-generated method stub
		String stepnum = "STEP " + ms.counter + ":";
		ms.bwout.write(stepnum);
		ms.bwout.newLine();
		ms.ArrayListSim("INM", ms.INM, ms.bwout);
		ms.ArrayListSim("INB", ms.INB, ms.bwout);
		ms.ArrayListSim("AIB", ms.AIB, ms.bwout);
		ms.ArrayListSim("SIB", ms.SIB, ms.bwout);
		ms.ArrayListSim("PRB", ms.PRB, ms.bwout);
		ms.ArrayListSim("ADB", ms.ADB, ms.bwout);
		ms.ArrayListSim("REB", ms.REB, ms.bwout);
		ms.ArraySim("RGF", ms.registers, ms.bwout, true);
		ms.ArraySim("DAM", ms.memory, ms.bwout, notCompleted);
		if(notCompleted)
			ms.bwout.newLine();
		ms.counter += 1;
	}



	private void ArraySim(String string, int[] Aout, BufferedWriter bwout2, boolean b) throws IOException {
		// TODO Auto-generated method stub
		String buffer = string + ":";
		String tempbuffer = "";
		if(string.equals("RGF")){
			for(int i = 0; i < Aout.length; i++){
				if(Aout[i] != Integer.MIN_VALUE){
					tempbuffer += "<R" + i + "," + Aout[i] + ">,";
				}
			}
			if(tempbuffer.length() > 0){
				buffer = buffer + tempbuffer.substring(0, tempbuffer.length() - 1);
			}
		}else if(string.equals("DAM")){
			for(int i = 0; i < Aout.length; i++){
				if(Aout[i] != Integer.MIN_VALUE){
					tempbuffer += "<" + i + "," + Aout[i] + ">,";
				}
			}
			if(tempbuffer.length() > 0){
				buffer = buffer + tempbuffer.substring(0, tempbuffer.length() - 1);
			}
		}
		bwout2.write(buffer);
		//System.out.println(buffer);
		if(b)
			bwout2.newLine();
		buffer = "";
		tempbuffer = "";
	}



	private void ArrayListSim(String string, ArrayList<String> ALout, BufferedWriter bwout2) throws IOException {
		// TODO Auto-generated method stub
		String buffer = string + ":";
		String tempbuffer = "";
		for(int i = 0; i < ALout.size(); i++){
			tempbuffer += ALout.get(i) + ",";
		}
		if(tempbuffer.length() > 0){
			buffer = buffer + tempbuffer.substring(0, tempbuffer.length() - 1);
		}
		bwout2.write(buffer);
		//System.out.println(buffer);
		bwout2.newLine();
		buffer = "";
		tempbuffer = "";
	}

}
