import edu.princeton.cs.algs4.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

public class BusManagementSystem {
    public static ArrayList<stop> stops= new ArrayList<>();
    public static HashMap<String, ArrayList<Integer>> stops1 = new HashMap<>();
    public static Hashtable<Integer, Integer> stops2= new Hashtable<>();
    public static EdgeWeightedDigraph vancouver;
    public static TST<ArrayList<Integer>> stopsTST = new TST<>();
    public static RedBlackBST<Integer, RedBlackBST<Integer, String>> arrivals = new RedBlackBST<>();

    BusManagementSystem(){
        fillStops();
        fillGraph();
    }

    public static class stop {
        public int stopID;
        public String stopName;
    }
    public static class time{
        public int hours;
        public int minutes;
        public int seconds;
        public int totalSeconds;

        time(String time){
            time = time.replace(" ", "");
            String[] st = time.split(":");
            this.hours = Integer.parseInt(st[0]);
            this.minutes = Integer.parseInt(st[1]);
            this.seconds = Integer.parseInt(st[2]);
            this.totalSeconds = (this.hours * 60 * 60) + (this.minutes * 60) + this.seconds;
        }

        boolean isValid(){
            return (hours >= 0 && hours <= 23) && (minutes >= 0 && minutes <= 59) && (seconds >= 0 && seconds <= 59);
        }

        int getTotal(String time){
            time newTime = new time(time);
            return newTime.totalSeconds;
        }
    }
    public static void TSTManager(String s, int id){
        String[] split = s.split(" ");
        if(split[0].equals("WB")||split[0].equals("EB")||split[0].equals("SB")||split[0].equals("NB")){
            s = s.replaceFirst(split[0] + " ","" );
            s = s + " " + split[0];
        }
        ArrayList<Integer> a;
        if(stopsTST.contains(s)){
            a = stopsTST.get(s);
            a.add(id);
            //a = stops1.get(s);
            //a.add(id);
        }
        else{
            a = new ArrayList<>();
            a.add(id);
            stopsTST.put(s, a);
            //stops1.put(s, a);
        }
    }


    public static void fillStops(){
        try{
            BufferedReader br = new BufferedReader(new FileReader("stops.txt"));
            String v;
            int i = 0;
            while((v = br.readLine()) != null){
                if(i > 0) {
                    String[] s = v.split(",");
                    stop st = new stop();
                    st.stopID = Integer.parseInt(s[0]);
                    st.stopName = s[2];
                    stops.add(st);
                    TSTManager(st.stopName, stops.size()-1);
                    if(stops1.containsKey(st.stopName)){
                        ArrayList<Integer> a = stops1.get(st.stopName);
                        a.add(st.stopID);
                        //a = stopsTST.get(st.stopName);
                        //a.add(st.stopID);
                    }
                    else {
                        ArrayList<Integer> a = new ArrayList<>();
                        a.add(st.stopID);
                        stops1.put(st.stopName, a);
                        //stopsTST.put(st.stopName, a);
                    }
                    stops2.put(st.stopID, stops2.size());
                }
                i++;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("Stop");
    }
    public static void fillGraph(){
        try{
            vancouver = new EdgeWeightedDigraph(stops2.size());
            BufferedReader br = new BufferedReader(new FileReader("transfers.txt"));
            String s;
            int i = 0;
            while((s = br.readLine())!=null){
                if (i > 0) {
                    String[] st = s.split(",");
                    DirectedEdge e = new DirectedEdge(stops2.get(Integer.parseInt(st[0])),
                            stops2.get(Integer.parseInt(st[1])),
                            (st[2].equals("0") ? 2 : (Integer.parseInt(st[3])/100.0)));
                    vancouver.addEdge(e);
                }
                ++i;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        try{
            BufferedReader br = new BufferedReader(new FileReader("stop_times.txt"));
            String s,prevS = null;
            int i = 0;
            while((s = br.readLine())!=null){
                if(i>0){
                    String[] st1 = s.split(",");
                    String[] st2 = prevS.split(",");
                    if(new time(st1[1].replace(" ", "")).isValid())
                        BSTManager(st1[1].replace(" ", ""), st1[0],s);
                    if(st1[0].equals(st2[0])){
                        time time1 = new time(st1[1]);
                        time time2 = new time(st1[2]);
                        time time3 = new time(st2[1]);
                        time time4 = new time(st2[2]);
                        if(time1.isValid()&&time2.isValid()&&time3.isValid()&&time4.isValid()) {
                            DirectedEdge edge = new DirectedEdge(stops2.get(Integer.parseInt(st2[3])), stops2.get(Integer.parseInt(st1[3])), 1);
                            vancouver.addEdge(edge);
                        }
                    }
                }
                ++i;
                prevS = s;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("Stop 2");
    }
    public static void BSTManager(String time,String id, String content){
        time t = new time(time);
        int total = t.totalSeconds;
        RedBlackBST<Integer, String> bst;
        if(arrivals.contains(total)){
            bst = arrivals.get(total);
            bst.put(Integer.parseInt(id),content);
        }
        else{
            bst = new RedBlackBST<>();
            bst.put(Integer.parseInt(id),content);
            arrivals.put(total, bst);
        }

    }
    public static void findShortestPath(String startStop, String endStop){
        int stop1 = stops1.get(startStop).get(0);
        stop1 = stops2.get(stop1);
        int stop2 = stops1.get(endStop).get(0);
        stop2 = stops2.get(stop2);
        DijkstraSP sp = new DijkstraSP(vancouver, stop1);
        if(!sp.hasPathTo(stop2)){
            System.out.println("No Path available");
        }
        for(DirectedEdge de : sp.pathTo(stop2)){
            String path = de.toString();
            String[] splitPath = path.split("->");
            String startPath = splitPath[0];
            splitPath = splitPath[1].split(" {2}");
            String endPath = splitPath[0];
            String outStr1 = stops.get(Integer.parseInt(startPath)).stopName;
            String outStr2 = stops.get(Integer.parseInt(endPath)).stopName;
            double outStr3 = Double.parseDouble(splitPath[1]);
            System.out.printf("%s->%s  %.2f\n", outStr1, outStr2, outStr3);
        }
        System.out.println("Stop 3");
    }
    public static void searchBusStops(String entry){
        Iterable<String> matches = stopsTST.keysWithPrefix(entry);
        ArrayList<Integer> linesNeeded = new ArrayList<>();
        for (String stop : matches){
            ArrayList<Integer> matches2 = stopsTST.get(stop);
            linesNeeded.addAll(matches2);
            //linesNeeded.add(stops1.get(stop).get(0));
        }
        linesNeeded.sort(null);
        try{
            BufferedReader br = new BufferedReader(new FileReader("stops.txt"));
            int i = -2;
            int j=0,k;
            k = linesNeeded.get(j);
            String s;
            while((s = br.readLine())!=null){
                i++;
                if(i==k){
                    System.out.println(s);
                    j++;
                    k = linesNeeded.get((j >= linesNeeded.size() ? linesNeeded.size() -1: j));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("Stop 4");
    }
    public static void searchArrivalTime(String time){
        if(arrivals.contains(new time(time).totalSeconds)){
            RedBlackBST<Integer, String> bst = arrivals.get(new time(time).totalSeconds);
            Iterable<Integer> keys = bst.keys();
            for(int i : keys){
                String[] s = bst.get(i).split(",");
                System.out.printf("Trip ID: %s, Arrival Time:%s, Departure Time:%s, Stop ID: %s, Stop in Sequence: %s, " +
                        "Stop Headsign: %s, Pickup Type: %s, Drop-off Type: %s, Distance of Shape Travelled: %s\n",
                        s[0], s[1], s[2], s[3], s[4], s[5], s[6], s[7], (s.length > 8 ? s[8] : ""));
            }
        }
        else{
            System.out.println("No trips arriving at that time");
        }
        System.out.println("Stop 5");
    }
}
