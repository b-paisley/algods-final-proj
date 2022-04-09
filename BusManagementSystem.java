/**
 * Below are classes taken from the textbook 'Algorithms, 4th Edition' By Robert Sedgewick and Kevin Wayne
 * https://algs4.cs.princeton.edu/home/
 * Directed Edge: Authored by Robert Sedgewick and Kevin Wayne, https://algs4.cs.princeton.edu/44sp Section 4.4
 * Edge Weighted Digraph: Authored by Robert Sedgewick and Kevin Wayne, https://algs4.cs.princeton.edu/44sp Section 4.4
 * Dijkstra Shortest Path: Authored by Robert Sedgewick and Kevin Wayne, https://algs4.cs.princeton.edu/44sp Section 4.4
 * Trinary Search Tree: Authored by Robert Sedgewick and Kevin Wayne, https://algs4.cs.princeton.edu/52trie Section 5.2
 * Red-Black Binary Search Tree: Authored by Robert Sedgewick and Kevin Wayne, https://algs4.cs.princeton.edu/33balanced Section 3.3
 */

import edu.princeton.cs.algs4.DirectedEdge;
import edu.princeton.cs.algs4.EdgeWeightedDigraph;
import edu.princeton.cs.algs4.DijkstraSP;
import edu.princeton.cs.algs4.TST;
import edu.princeton.cs.algs4.RedBlackBST;

//Standard Java jdk 16 packages
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

public class BusManagementSystem {
    //An array list used to index the stops
    public static ArrayList<stop> stops= new ArrayList<>();
    //A hashmap to contain references for every stop's stop id(s)
    public static HashMap<String, ArrayList<Integer>> stops1 = new HashMap<>();
    //A hashtable to quickly reference a stop id to it's index
    public static Hashtable<Integer, Integer> stops2= new Hashtable<>();
    //An Edge Weighted Digraph to describe the city's transport, with stops as vertices and a journey as an edge
    public static EdgeWeightedDigraph vancouver;
    //A data structure to store the stop names in order to quickly search
    public static TST<ArrayList<Integer>> stopsTST = new TST<>();
    //A data structure to store the stops that arrive at every arrival time in an already sorted manner
    public static RedBlackBST<Integer, RedBlackBST<Integer, String>> arrivals = new RedBlackBST<>();

    //If used as a class, a constructor to automatically fill the data structures
    BusManagementSystem(){
        fillStops();
        fillGraph();
    }

    //A class to reference a stopID with its name
    public static class stop {
        public int stopID;
        public String stopName;
    }
    //A class to handle the time formats
    public static class time{
        public int hours;
        public int minutes;
        public int seconds;
        public int totalSeconds;

        time(){
            this.seconds = 0;
            this.minutes = 0;
            this.hours = 0;
            this.totalSeconds = 0;
        }
        time(String time){
            time = time.replace(" ", "");
            String[] st = time.split(":");
            this.hours = Integer.parseInt(st[0]);
            this.minutes = Integer.parseInt(st[1]);
            this.seconds = Integer.parseInt(st[2]);
            this.totalSeconds = (this.hours * 60 * 60) + (this.minutes * 60) + this.seconds;
        }
        //the time must be between 00:00:00 and 23:59:59
        boolean isValid(){
            return (hours >= 0 && hours <= 23) && (minutes >= 0 && minutes <= 59) && (seconds >= 0 && seconds <= 59);
        }
        //makes sure the value entered contains numbers
        boolean correctForm(String time){
            String[] s = time.split(":");
            try{
                Integer.parseInt(s[0]);
                Integer.parseInt(s[1]);
                Integer.parseInt(s[2]);
            }catch (NumberFormatException e){
                return false;
            }
            return true;
        }
    }
    //A method used to put data entries into the TST
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
        }
        else{
            a = new ArrayList<>();
            a.add(id);
            stopsTST.put(s, a);
        }
    }
    //A method to fill the data structures for the stops
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
    }
    //A method that fills the graph to run the Shortest path algorithm on
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
    }
    //A method that manages adding values to the binary search tree(s)
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
    //A method that finds the shortest path from stop A to stop B
    public static void findShortestPath(String entry){
        String startStop, endStop;
        //General error checking and handling
        if(entry == null || entry.equals("") || entry.split(",").length !=2){
            System.out.println("Invalid entry");
            return;
        }
        startStop = entry.split(",")[0];
        endStop = entry.split(",")[1];
        if(startStop == null || startStop.equals("")){
            System.out.println("Please enter a value for Stop A");
            return;
        }
        if(endStop == null || endStop.equals("")){
            System.out.println("Please enter a value for Stop B");
            return;
        }
        if(!stops1.containsKey(startStop)){
            System.out.println("Stop entry A not found");
            return;
        }
        if(!stops1.containsKey(endStop)){
            System.out.println("Stop entry B not found");
            return;
        }
        //finds the stops indexes
        int stop1 = stops1.get(startStop).get(0);
        stop1 = stops2.get(stop1);
        int stop2 = stops1.get(endStop).get(0);
        stop2 = stops2.get(stop2);
        //Runs the Dijkstra Algorithm to find the shortest path to all stops from stop A
        DijkstraSP sp = new DijkstraSP(vancouver, stop1);
        //Checks if there is a path to stop B
        if(!sp.hasPathTo(stop2)){
            System.out.println("No Path available");
            return;
        }
        //If there was output the path as strings to the console
        for(DirectedEdge de : sp.pathTo(stop2)){
            String path = de.toString();
            String[] splitPath = path.split("->");
            String startPath = splitPath[0];
            splitPath = splitPath[1].split(" {2}");
            String endPath = splitPath[0];
            String outStr1 = stops.get(Integer.parseInt(startPath)).stopName;
            String outStr2 = stops.get(Integer.parseInt(endPath)).stopName;
            double outStr3 = Double.parseDouble(splitPath[1]);
            System.out.printf("%s - >%s , Cost: %.2f\n", outStr1, outStr2, outStr3);
        }
    }
    //A method to search the TST for the stops starting with the input provided
    public static void searchBusStops(String entry){
        //Error checking
        if(entry == null || entry.equals("")){
            System.out.println("Please enter a value to search for");
            return;
        }
        //Method that returns all the Keys in the TST that start with the input and are indexed to a value
        Iterable<String> matches = stopsTST.keysWithPrefix(entry);
        //More error checking, making sure there are matches before proceeding
        String errorCheck = matches.toString();
        if(errorCheck == null || errorCheck.equals("") ){
            System.out.println("No Bus Stops Found");
            return;
        }
        //Add to an array list all the lines in the stops.txt file that match the criteria
        ArrayList<Integer> linesNeeded = new ArrayList<>();
        for (String stop : matches){
            ArrayList<Integer> matches2 = stopsTST.get(stop);
            linesNeeded.addAll(matches2);
        }
        //sort the array list to print the stops as going through the reader to save space
        linesNeeded.sort(null);
        try{
            BufferedReader br = new BufferedReader(new FileReader("stops.txt"));
            int i = -2;
            int j=0,k;
            k = linesNeeded.get(j);
            String s;
            String[] split;
            while((s = br.readLine())!=null){
                i++;
                if(i==k){
                    split = s.split(",");
                    System.out.printf("Stop ID: %s, Stop Code: %s, Stop Name: %s, Description: %s, Latitude and Longitude: (%s, %s), Zone ID: %s, URL for Stop: %s, Location Type: %s, Parent Station (If any): %s\n",
                            split[0],split[1],split[2],split[3],split[4],split[5],split[6],split[7],split[8],(split.length > 9?split[9]:""));
                    j++;
                    k = linesNeeded.get((j >= linesNeeded.size() ? linesNeeded.size() -1: j));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    //A method that searches stop_times.txt for trips that arrive anywhere at a given time
    public static void searchArrivalTime(String time){
        //Error checking and handling
        if(time==null||time.equals("")||time.split(":").length!=3){
            System.out.println("Please enter a time in the form hh:mm:ss");
            return;
        }
        if(!new time().correctForm(time)){
            System.out.println("Invalid entry for time please use numbers 0-23 for hours and 0-59 for minutes/seconds");
            return;
        }
        if(!new time(time).isValid()){
            System.out.println("Invalid entry for time please use numbers 0-23 for hours and 0-59 for minutes/seconds");
            return;
        }
        //Prints all the lines in stop_time.txt that have an arrival time that was given, sorted by the trip ID
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
    }
}
