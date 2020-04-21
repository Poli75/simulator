package hr.fer.project.dinamic;

import hr.fer.projekt.Packet;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class Helpers {
    public static void readData(String path, List<Packet> packets) {
        String times="";
        String sizes="";
        String delays="";
        String[] txt= {"", "", "", "",""};
        int cntr=0;
        String IDs="";

        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(path));
            String line = reader.readLine();
            while (line != null) {
                //System.out.println(line);
                txt[cntr++]=line;
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        times=txt[0].replaceAll("\\[", "").replaceAll("\\]","");
        sizes=txt[1].replaceAll("\\[", "").replaceAll("\\]","");
        delays=txt[2].replaceAll("\\[", "").replaceAll("\\]","");
        IDs=txt[3].replaceAll("\\[", "").replaceAll("\\]","");
        String[] tms=times.split(",");
        String[] szs=sizes.split(",");
        String[] dlys=delays.split(",");
        String[] ides=IDs.split(",");
        int loop=tms.length;
        int i=0;

        while (loop>0) {
            Packet pack = new Packet(tms[i].trim(),szs[i].trim(),dlys[i].trim(),ides[i].trim());
            i++;
            packets.add(pack);
            loop--;
        }

    }

    public static double sliceCalc(List<Packet> ch1, List<Packet> ch2) {
        double size=0;
        double pro1,pro2;
        for(Packet pack: ch1) {
            size+=pack.size;
        }
        pro1=size/ch1.size();
        System.out.println("Velicina 1: "+ size);
        size=0;
        for(Packet pack: ch2) {
            size+=pack.size;
        }
        pro2=size/ch2.size();
        System.out.println("Velicina 2: "+ size);
        return pro1/pro2;
    }
}
