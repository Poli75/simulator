package hr.fer.project.dinamic;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.stream.Collectors;

import hr.fer.projekt.*;

public class Main {

	public static List<Packet> packet1 = new ArrayList<Packet>();
	public static List<Packet> packet2 = new ArrayList<Packet>();
	public static List<Double> grades1 = new ArrayList<Double>();
	public static List<Double> grades2 = new ArrayList<Double>();
	public static double timeSim = 0;
	public static double interval = 0.5;
	public static double traffic1 = 0;
	public static double traffic2 = 0;
	public static int cntr1 = 0;
	public static int cntr2 = 0;
	public static List<Packet> packetS = new ArrayList<>();
	public static List<Double> staticGrades=new ArrayList<>();
	public static double genTime;
	
	public static List<Double> RUgrades=new ArrayList<>();
	public static List<Double> slice1QoS=new ArrayList<>();
	public static List<Double> slice2QoS=new ArrayList<>();
	public static List<Double> slice1Performance=new ArrayList<>();
	public static List<Double> slice2Performance=new ArrayList<>();
	public static Map<String, List<Packet>> staticPerfs = new TreeMap<String, List<Packet>>();
	public static List<Double> QoSIsolations=new ArrayList<>();

	public static void main(String[] args) {
		
		List<String> files=new ArrayList<>();
		String filesPath="/home/paula/Desktop/paths.txt";
		files=read(filesPath);
		//Double[] intervals= {0.1, 0.25, 0.5, 0.75, 1.0, 2.0,2.5, 5.0, 7.5, 10.0, 15.0, 25.0, 50.00,  100.0, 150.0, 200.0, 250.0, 300.0,500.0};
		Double[] intervals= { 1.0,2.0,5.0,10.0,15.0,25.0,50.0,75.0,100.0,250.0,300.0,500.0,750.0,1000.0,1500.0,2000.0,2500.0,5000.0,
							7500.0,10000.0,15000.0,20000.0,30000.0,40000.0,50000.0,60000.0};
		double factor=1;
		
		for(String file:files) {
			interval=Double.POSITIVE_INFINITY;
			timeSim=0;
			traffic1=0;
			traffic2=0;
			List<Packet> staticQoS=new ArrayList<>();
			staticPerfs.put(file, staticQoS);
			System.out.println("POKRETANJE SIMULACIJE STATICKI...");
			System.out.println("Datoteka: "+file);
			executeStatic(file,70*factor);					
			System.out.println();
		}
		
//		for(Double tmp:staticGrades) {
//			System.out.println("Staticka ocj:" +tmp);
//		}
		
		
		int j=0;
		for(String file:files) {
			for(int i=0; i<intervals.length; i++) {
				interval=intervals[i];
				timeSim=0;
				traffic1=0;
				traffic2=0;
				packetS.removeAll(packetS);
				System.out.println("POKRETANJE SIMULACIJE...");
				System.out.println("Datoteka: "+file);
				System.out.println("Interval: "+interval);
				execute(file,j,70*factor);					
				System.out.println();
			}	
			j=j+2;
		}
		
		saveResults(intervals,files,"/home/paula/Dropbox/rezultati/mmtc/1");

	}
	
	private static void saveResults(Double[] intervals, List<String> files, String name) {
		
		DecimalFormat df = new DecimalFormat("#0.0000000");
		DecimalFormat dfInt=new DecimalFormat("#0.00");
	    DecimalFormatSymbols sym = DecimalFormatSymbols.getInstance();
	    sym.setDecimalSeparator(',');
	    df.setDecimalFormatSymbols(sym);
	    dfInt.setDecimalFormatSymbols(sym);
	    
	  try {
	 		 FileWriter myWriter = new FileWriter(name+"/RUGrade.txt");
	 		 myWriter.write("\t");
	 		 for(Double interval:intervals) {
	 			 myWriter.write(":"+dfInt.format(interval));
	 		 }
	 		 myWriter.write("\n");
	 		 int k=0;
	 		 for(int i = 0; i<files.size(); i++) {
	 			myWriter.write(files.get(i).substring(files.get(i).lastIndexOf("/")).replace(".txt", "").replace("/", ""));
	 			for(int j=0; j<intervals.length; j++) {
	 				 myWriter.write(":"+df.format(RUgrades.get(j+k)));
	 			 }
	 			 k=k+intervals.length;
	 			 myWriter.write("\n");
	 		 }
	 		 myWriter.close();
	 		 } catch (IOException e) {
	 			 System.out.println("An error occurred.");
	 			 e.printStackTrace();
	 	}
	  
//	  try {
//	 		 FileWriter myWriter = new FileWriter(name+"/QoSGradeSlice1.txt");
//	 		 myWriter.write("\t");
//	 		 for(Double interval:intervals) {
//	 			 myWriter.write(":"+dfInt.format(interval));
//	 		 }
//	 		 myWriter.write("\n");
//	 		 int k=0;
//	 		 for(int i = 0; i<files.size(); i++) {
//	 			myWriter.write(files.get(i).substring(files.get(i).lastIndexOf("/")).replace(".txt", "").replace("/", ""));
//	 			for(int j=0; j<intervals.length; j++) {
//	 				 myWriter.write(":"+df.format(slice1QoS.get(j+k)));
//	 			 }
//	 			 k=k+intervals.length;
//	 			 myWriter.write("\n");
//	 		 }
//	 		 myWriter.close();
//	 		 } catch (IOException e) {
//	 			 System.out.println("An error occurred.");
//	 			 e.printStackTrace();
//	 	}
//	 
//	  try {
//	 		 FileWriter myWriter = new FileWriter(name+"/QoSGradeSlice2.txt");
//	 		 myWriter.write("\t");
//	 		 for(Double interval:intervals) {
//	 			 myWriter.write(":"+dfInt.format(interval));
//	 		 }
//	 		 myWriter.write("\n");
//	 		 int k=0;
//	 		 for(int i = 0; i<files.size(); i++) {
//	 			myWriter.write(files.get(i).substring(files.get(i).lastIndexOf("/")).replace(".txt", "").replace("/", ""));
//	 			for(int j=0; j<intervals.length; j++) {
//	 				 myWriter.write(":"+df.format(slice2QoS.get(j+k)));
//	 			 }
//	 			 k=k+intervals.length;
//	 			 myWriter.write("\n");
//	 		 }
//	 		 myWriter.close();
//	 		 } catch (IOException e) {
//	 			 System.out.println("An error occurred.");
//	 			 e.printStackTrace();
//	 	}
//	  
	  
	  
	  try {
	 		 FileWriter myWriter = new FileWriter(name+"/PerformanceGradeSlice1.txt");
	 		 myWriter.write("\t");
	 		 for(Double interval:intervals) {
	 			 myWriter.write(":"+dfInt.format(interval));
	 		 }
	 		 myWriter.write("\n");
	 		 int k=0;
	 		 for(int i = 0; i<files.size(); i++) {
	 			myWriter.write(files.get(i).substring(files.get(i).lastIndexOf("/")).replace(".txt", "").replace("/", ""));
	 			for(int j=0; j<intervals.length; j++) {
	 				 myWriter.write(":"+df.format(slice1Performance.get(j+k)));
	 			 }
	 			 k=k+intervals.length;
	 			 myWriter.write("\n");
	 		 }
	 		 myWriter.close();
	 		 } catch (IOException e) {
	 			 System.out.println("An error occurred.");
	 			 e.printStackTrace();
	 	}
	  
	  try {
	 		 FileWriter myWriter = new FileWriter(name+"/PerformanceGradeSlice2.txt");
	 		 myWriter.write("\t");
	 		 for(Double interval:intervals) {
	 			 myWriter.write(":"+dfInt.format(interval));
	 		 }
	 		 myWriter.write("\n");
	 		 int k=0;
	 		 for(int i = 0; i<files.size(); i++) {
	 			myWriter.write(files.get(i).substring(files.get(i).lastIndexOf("/")).replace(".txt", "").replace("/", ""));
	 			for(int j=0; j<intervals.length; j++) {
	 				 myWriter.write(":"+df.format(slice2Performance.get(j+k)));
	 			 }
	 			 k=k+intervals.length;
	 			 myWriter.write("\n");
	 		 }
	 		 myWriter.close();
	 		 } catch (IOException e) {
	 			 System.out.println("An error occurred.");
	 			 e.printStackTrace();
	 	}
	  
	  try {
	 		 FileWriter myWriter = new FileWriter(name+"/QoSIsolation.txt");
	 		 myWriter.write("\t");
	 		 for(Double interval:intervals) {
	 			 myWriter.write(":"+dfInt.format(interval));
	 		 }
	 		 myWriter.write("\n");
	 		 int k=0;
	 		 for(int i = 0; i<files.size(); i++) {
	 			myWriter.write(files.get(i).substring(files.get(i).lastIndexOf("/")).replace(".txt", "").replace("/", ""));
	 			for(int j=0; j<intervals.length; j++) {
	 				 myWriter.write(":"+df.format(QoSIsolations.get(j+k)));
	 			 }
	 			 k=k+intervals.length;
	 			 myWriter.write("\n");
	 		 }
	 		 myWriter.close();
	 		 } catch (IOException e) {
	 			 System.out.println("An error occurred.");
	 			 e.printStackTrace();
	 	}
	 
	    
	}

	private static void execute(String pathIn, int i, double res) {
		String path=pathIn;
		List<Packet> packets=new ArrayList<>();
		List<Double> ruis = new ArrayList<>();
		List<Double> performances=new ArrayList<>();
		DecimalFormat formatter = new DecimalFormat("#0.00");
		
		Helpers.readData(path,packets);
		
		packetS.addAll(packets);
		String[] allSizes=path.split("_");
		double sliceSize0=Double.parseDouble(allSizes[1]);
		double sliceSize1=Double.parseDouble(allSizes[2]);
		genTime=Double.parseDouble(allSizes[3].replaceFirst(".txt", ""));

		for(Packet temp:packets) {
			if(temp.userID==0) {
				packet1.add(temp);
				//			Packet.print(temp);
			}else if(temp.userID==1) {
				packet2.add(temp);
			}
		}		
		
		double resource=res;
		double ratio=sliceCalcStatic(sliceSize0,sliceSize1);
		
		double x = resource*ratio;		
		double y = resource*(1-ratio);
		
		
	//	System.out.println("x = " + x + " y = " + y);

		List<Packet> currentPacks1 = new ArrayList<Packet>();
		List<Packet> currentPacks2 = new ArrayList<Packet>();

		do{
			
			if(currentPacks1.size() > 0 || packet1.size() > 0) PFsend2(x,packet1, currentPacks1, "1");
			if((currentPacks2.size() > 0 || packet2.size() > 0)) PFsend2(y,packet2, currentPacks2, "2");
			
			ruis.add((traffic1+traffic2)/(resource*interval));
			
			double currentSize1 = currentPacks1.stream().mapToDouble(a -> a.size).sum();
			double currentSize2 = currentPacks2.stream().mapToDouble(a -> a.size).sum();
			
			timeSim += interval;
			double slice_ratio;
			
			slice_ratio = sliceCalcLoad(sliceSize0,sliceSize1, currentSize1,currentSize2);
			//slice_ratio = sliceCalcLoadExpect(sliceSize0,sliceSize1, currentSize1,currentSize2);
			//slice_ratio = sliceCalcPredict(sliceSize0,sliceSize1, timeSim);
			//slice_ratio = sliceCalcLoadPredict(sliceSize0,sliceSize1, currentSize1,currentSize2, timeSim);
			
			x = resource*slice_ratio;
			y = resource-x;
			
		//	System.out.println("x= "+ x+ " y= "+ y);
		//	System.out.println("Packet 1 count: "+traffic1+ " Packet 2: "+ traffic2+ " in period from "+ timeSim+ " to "+ (timeSim+interval));
		//	timeSim += interval;
//			System.out.println("We are currently at: " + formatter.format(timeSim));
//			System.out.println();
			traffic1=0;
			traffic2=0;
		} while((currentPacks1.size() > 0 || packet1.size() > 0) || ((currentPacks2.size() > 0 || packet2.size() > 0)));
	
//		for(Packet pack:packetS) {
//			System.out.println();
//			if((pack.trTime- pack.arrTime)<0) System.out.println("Greska!");
//		//	System.out.println("Velicina: "+ pack.size);
//			System.out.println("Paket dosao: "+ pack.arrTime+ " Zavrsio: "+ pack.trTime);
//			System.out.println("Prolazak kroz mrezu: "+ (pack.trTime- pack.arrTime)+ " a trazeni delay: "+pack.qosDelay);
//		}
		
	//	System.out.println();
		
		
		double QoSis=0;
		double QoStemp;
		System.out.println("RUI: " + ruis.stream().mapToDouble(a -> a).average().getAsDouble());
		RUgrades.add(ruis.stream().mapToDouble(a -> a).average().getAsDouble());
		
		QoStemp=isolationGrade(packetS, path.replaceFirst(".txt",""),0);
		System.out.println("QoS performance for slice 1: " +QoStemp);
		slice1Performance.add(QoStemp);
		
		QoStemp=isolationGrade(packetS, path.replaceFirst(".txt",""),1);
//		QoSis=QoStemp-staticGrades.get(i+1);
		System.out.println("QoS performance for slice 2: " +QoStemp);
		slice2Performance.add(QoStemp);
		//resourceUtGrade(packetS, path.replaceFirst(".txt",""));
		
		Map<Double,Double> perfs=performance(packetS);
		QoSIsolations.add(calculateIsolation(pathIn,perfs));
		System.out.println("QoS isolation: " +calculateIsolation(pathIn,perfs));

	}
		
	private static void executeStatic(String pathIn, double res) {
		String path=pathIn;
		List<Packet> packets=new ArrayList<>();
		List<Double> ruis = new ArrayList<>();
		DecimalFormat formatter = new DecimalFormat("#0.00");
		
		Helpers.readData(path,packets);
		
		packetS.addAll(packets);
		String[] allSizes=path.split("_");
		double sliceSize0=Double.parseDouble(allSizes[1]);
		double sliceSize1=Double.parseDouble(allSizes[2]);
		genTime=Double.parseDouble(allSizes[3].replaceFirst(".txt", ""));

		for(Packet temp:packets) {
			if(temp.userID==0) {
				packet1.add(temp);
				//			Packet.print(temp);
			}else if(temp.userID==1) {
				packet2.add(temp);
			}
		}		
		
		double resource=res;
		double ratio=sliceCalcStatic(sliceSize0,sliceSize1);
		
		double x = resource*ratio;
		double y = resource*(1-ratio);
		
	//	System.out.println("x = " + x + " y = " + y);

		List<Packet> currentPacks1 = new ArrayList<Packet>();
		List<Packet> currentPacks2 = new ArrayList<Packet>();

		do{
			
			if(currentPacks1.size() > 0 || packet1.size() > 0) PFsend2(x,packet1, currentPacks1, "1");
			if((currentPacks2.size() > 0 || packet2.size() > 0)) PFsend2(y,packet2, currentPacks2, "2");
			
			ruis.add((traffic1+traffic2)/(resource*interval));
			
			x = resource*sliceCalcStatic(sliceSize0,sliceSize1);
			y = resource-x;
			
		//	System.out.println("x= "+ x+ " y= "+ y);
		//	System.out.println("Packet 1 count: "+traffic1+ " Packet 2: "+ traffic2+ " in period from "+ timeSim+ " to "+ (timeSim+interval));
			timeSim += interval;
//			System.out.println("We are currently at: " + formatter.format(timeSim));
//			System.out.println();
			traffic1=0;
			traffic2=0;
		} while((currentPacks1.size() > 0 || packet1.size() > 0) || ((currentPacks2.size() > 0 || packet2.size() > 0)));
	
//		for(Packet pack:packetS) {
//			System.out.println();
//			if((pack.trTime- pack.arrTime)<0) System.out.println("Greska!");
//		//	System.out.println("Velicina: "+ pack.size);
//			System.out.println("Paket dosao: "+ pack.arrTime+ " Zavrsio: "+ pack.trTime);
//			System.out.println("Prolazak kroz mrezu: "+ (pack.trTime- pack.arrTime)+ " a trazeni delay: "+pack.qosDelay);
//		}
		
		System.out.println("RUI: " + ruis.stream().mapToDouble(a -> a).average().getAsDouble());
	//	System.out.println();
		staticGrades.add(isolationGradeStatic(packetS,0,pathIn));
		staticGrades.add(isolationGradeStatic(packetS,1,pathIn));
		//resourceUtGrade(packetS, path.replaceFirst(".txt",""));

	}

	private static List<String> read(String path) {
		BufferedReader reader;
		 List<String> text=new ArrayList<>();	 
	        try {
	            reader = new BufferedReader(new FileReader(path));
	            String line = reader.readLine();
	            while (line != null) {
	           //     System.out.println(line);
	            	text.add(line);
	            	line = reader.readLine();
	            }
	            reader.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        return text;
	}

	private static double sliceCalcStatic(double sliceSize0, double sliceSize1) {
		return sliceSize0 / (sliceSize0 + sliceSize1);
	}

	private static double sliceCalcLoad(double slSize1, double slSize2, double currentSize1, double currentSize2) {
		if (currentSize1 == 0.0 && currentSize2==0.0)
			return slSize1 / (slSize1 + slSize2);
		return currentSize1 / (currentSize1+currentSize2);
	}
	
	private static double sliceCalcLoadExpect(double slSize1, double slSize2, double currentSize1, double currentSize2) {
		return (currentSize1+slSize1*interval) / (currentSize1+currentSize2+(slSize1+slSize2)*interval);
	}
	
	private static double sliceCalcPredict(double slSize1, double slSize2, double time) {
		double sl1_size_future = packet1.stream().filter(a -> a.arrTime<time+interval).mapToDouble(a -> a.size).sum();
		double sl2_size_future = packet2.stream().filter(a -> a.arrTime<time+interval).mapToDouble(a -> a.size).sum();
		
		if (sl1_size_future == 0.0 && sl2_size_future==0.0)
			return slSize1 / (slSize1 + slSize2);
		return sl1_size_future/(sl1_size_future+sl2_size_future);	
	}
	
	private static double sliceCalcLoadPredict(double slSize1, double slSize2, double currentSize1, double currentSize2, double time) {
		double sl1_size_future = packet1.stream().filter(a -> a.arrTime<time+interval).mapToDouble(a -> a.size).sum();
		double sl2_size_future = packet2.stream().filter(a -> a.arrTime<time+interval).mapToDouble(a -> a.size).sum();
		
		if (sl1_size_future+currentSize1 == 0.0 && sl2_size_future+currentSize2 == 0.0)
			return slSize1 / (slSize1 + slSize2);
		return (currentSize1+sl1_size_future) / (currentSize1+currentSize2+sl1_size_future+sl2_size_future);
	}

	public static void PFsend2(double speed, List<Packet> packs, List<Packet> currentPacks, String id) {
		List<Double> arrivalsNew = packs.stream()
				.filter(packet -> packet.arrTime >= Main.timeSim && packet.arrTime <= Main.timeSim + interval)
				.map(Packet::getArrTime).collect(Collectors.toCollection(ArrayList::new));

		List<Double> ends = new ArrayList<Double>();
		// System.out.println(currentPacks.size());

		double previousTime = timeSim;

		if (arrivalsNew.size() > 0) {
			for (Double arrival : arrivalsNew) {
				ends = changeSize(currentPacks, arrival, speed, previousTime);

				currentPacks.add(packs.get(0));
				Collections.sort(currentPacks);
				packs.remove(0);
				previousTime = arrival;
				currentPacks.removeIf(packet -> packet.size <= 0);

			}
			ends = changeSize(currentPacks, timeSim + interval, speed, previousTime);
		} else {
			Collections.sort(currentPacks);
			ends = changeSize(currentPacks, timeSim + interval, speed, timeSim);

		}

		Collections.sort(currentPacks);

		// if(Integer.parseInt(id)==1) {
		// cntr1=currentPacks.size();
		//
		// }else {
		// cntr2=currentPacks.size();
		// }

		// System.out.println("-------->" + id);
		currentPacks.removeIf(packet -> packet.getSize() <= 0);
		//currentPacks.forEach(packet -> System.out.println(packet.size));

	}

	private static List<Double> changeSize(List<Packet> packets, double time, double speed, double previousTime) {
		double currentSpeed = speed / packets.stream().filter(packet -> packet.size > 0).map(Packet::getArrTime)
				.collect(Collectors.toCollection(ArrayList::new)).size();
		List<Double> stopTimes = new ArrayList<Double>();
		double size;
		double minTime;
		double previousTime2 = previousTime;
		double lastTime = timeSim;
		for (Packet pack : packets) {
			size = pack.size;
			minTime = previousTime2 <= pack.arrTime ? pack.arrTime : previousTime2;
			
			pack.size = (pack.size - ((time - minTime) * currentSpeed));
			
			if( pack.userID==0 && pack.size>=0) {
				traffic1+=(time - minTime) * currentSpeed;
			//	System.out.println("Dodajem na load: "+ (time - minTime) * currentSpeed + " za slice 0");
			}else if (pack.userID==0) {
				traffic1+=size;
			//	System.out.println("Dodajem na load koji je gotov: "+ (time - minTime) * currentSpeed + " za slice 0");
			}
	
			if( pack.userID==1 && pack.size>=0) {
				traffic2+=((time - minTime) * currentSpeed);
			}else if(pack.userID==1) traffic2+=size;
			
			if (pack.size <= 0) {		
				double finalSize = size;
				double finalCurrentSpeed = currentSpeed;
//				if(pack.userID==0) {
//					traffic1+=finalSize;
//				}
//				if(pack.userID==1) {
//					traffic2+=finalSize;
//				}
				
				previousTime2 += size / currentSpeed;
				lastTime = previousTime2;
				pack.setEndTime(previousTime2);
			//	System.out.println("Vrijeme za check: " + previousTime2);
			//	System.out.println("Promjenio za paket "+ pack.arrTime);
//				System.out.println("Vrijeme za dodati: " + lastTime);
//				System.out.println("Brzina: " + finalCurrentSpeed);
//				System.out.println("Velicina: " + finalSize);
//				System.out.println("Promjenio za paket koji je dosao u " + pac.arrTime + " u " + pack.trTime);
					
				}
			currentSpeed = speed / packets.stream().filter(packet -> packet.size > 0).map(Packet::getArrTime)
					.collect(Collectors.toCollection(ArrayList::new)).size();

		}

		return stopTimes;
	}

	private static int findMin(List<Packet> packets) {
		double minValue = Double.POSITIVE_INFINITY;
		int i = 0;
		int min = 0;
		for (Packet pack : packets) {
			if (pack.size < minValue) {
				min = i;
				minValue = pack.size;
			}

			i++;
		}

		return min;
	}

	private static double isolationGrade(List<Packet> packs, String name, Integer ID) {
		List<Double> gradeovi = new ArrayList<>();
		for (Packet temp : packs) {
			// System.out.println("QOS grade: "+
			// grade((temp.trTime-temp.arrTime)/temp.qosDelay));
			if (temp.trTime <= genTime && temp.userID == ID)
				gradeovi.add((temp.trTime - temp.arrTime) / temp.qosDelay);
		}
	//	System.out.println("QoS performans za slice " + (ID + 1) + ": "+ (gradeovi.stream().mapToDouble(a -> a).average()).getAsDouble());
		return (gradeovi.stream().mapToDouble(a -> a).average()).getAsDouble();
		
		// for(Packet temp:packs) {
		// myWriter.write(grade((temp.trTime-temp.arrTime)/temp.qosDelay)+" ");
	}
	
	private static double isolationGradeStatic(List<Packet> packs, Integer ID, String path) {
		List<Double> gradeovi = new ArrayList<>();
		List<Double> ocjene=new ArrayList<>();
		List<Packet> paketi=new ArrayList<>();
		for (Packet temp : packs) {
			// System.out.println("QOS grade: "+
			// grade((temp.trTime-temp.arrTime)/temp.qosDelay));
			if (temp.trTime <= genTime && temp.userID == ID) {
				gradeovi.add((temp.trTime - temp.arrTime) / temp.qosDelay);
			}
			if (temp.trTime <= genTime && ID == 0) {
				temp.setQoS((temp.trTime - temp.arrTime) / temp.qosDelay);
			}
			paketi.add(temp);
		}
		if(ID==0) {
			staticPerfs.put(path, paketi);
		}
	//	System.out.println("QoS performans za slice " + (ID + 1) + ": "+ (gradeovi.stream().mapToDouble(a -> a).average()).getAsDouble());
		return (gradeovi.stream().mapToDouble(a -> a).average()).getAsDouble();
		// for(Packet temp:packs) {
		// myWriter.write(grade((temp.trTime-temp.arrTime)/temp.qosDelay)+" ");
	}

	private static double calculateIsolation(String path, Map<Double,Double> perfs) {
		List<Packet> statPer=staticPerfs.get(path);
		List<Double> perfGrades=new ArrayList<>();
		for(Packet temp:statPer) {
			if(perfs.containsKey(temp.arrTime)) {
			if(perfs.get(temp.arrTime) >= temp.staticQoS && temp.staticQoS!=0 ) {
				perfGrades.add(1.0);
			}else if(temp.staticQoS!=0){
				perfGrades.add( 1- ( (temp.staticQoS-(perfs.get(temp.arrTime))) /temp.staticQoS ));
				
			}
		}
		}
		return perfGrades.stream().mapToDouble(a->a).average().getAsDouble();
	}

	private static Map<Double,Double> performance(List<Packet> packs){
		Map<Double,Double> grades = new TreeMap<>();
		for(Packet temp:packs) {
			if (temp.trTime <= genTime) grades.put(temp.arrTime, (temp.trTime-temp.arrTime)/temp.qosDelay);
		}
		return grades;
	}


}
