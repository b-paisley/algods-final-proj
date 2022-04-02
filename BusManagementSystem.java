import edu.princeton.cs.algs4.EdgeWeightedDigraph;
import edu.princeton.cs.algs4.DirectedEdge;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Hashtable;

public class BusManagementSystem {
    public static ArrayList<stop> stops = new ArrayList<>();
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
                    stops2.put(st.stopID, stops.size() - 1);
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
            vancouver = new EdgeWeightedDigraph(stops.size());
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
