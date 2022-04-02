import edu.princeton.cs.algs4.DijkstraSP;
import edu.princeton.cs.algs4.EdgeWeightedDigraph;
import edu.princeton.cs.algs4.DirectedEdge;
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
                    if(stops1.containsKey(st.stopName)){
                        ArrayList<Integer> a = stops1.get(st.stopName);
                        a.add(st.stopID);
                        System.out.println("Stop4");
                    }
                    else {
                        ArrayList<Integer> a = new ArrayList<>();
                        a.add(st.stopID);
                        stops1.put(st.stopName, a);
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
                            (st[3].equals("0") ? 2 : (Integer.parseInt(st[3])/100)));
                    vancouver.addEdge(e);
                }
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
                    if(st1[0].equals(st2[0])){
                        DirectedEdge edge = new DirectedEdge(stops2.get(Integer.parseInt(st2[3])),stops2.get(Integer.parseInt(st1[3])),1);
                        vancouver.addEdge(edge);
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
    public static void findShortestPath(String startStop, String endStop){
        int stop1 = stops1.get(startStop).get(0);
        stop1 = stops2.get(stop1);
        int stop2 = stops1.get(endStop).get(0);
        stop2 = stops2.get(stop2);
        DijkstraSP sp = new DijkstraSP(vancouver, stop1);
        if(!sp.hasPathTo(stop2)){
            System.out.println();
        }
        for(DirectedEdge de : sp.pathTo(stop2)){
            String path = de.toString();
            String[] splitPath = path.split("->");
            String startPath = splitPath[0];
            splitPath = splitPath[1].split("  ");
            String endPath = splitPath[0];
            int outStr1 = stops.get(Integer.parseInt(startPath)).stopID;
            int outStr2 = stops.get(Integer.parseInt(endPath)).stopID;
            double outStr3 = Double.parseDouble(splitPath[1]);
            System.out.printf("%d->%d  %.2f\n", outStr1, outStr2, outStr3);
        }
        System.out.println("Stop3");
    }
    public static void searchBusStops(String entry){

    }
    public static void searchArrivalTime(String time){

    }
    public static class stop {
        public int stopID;
        public String stopName;
    }

}
