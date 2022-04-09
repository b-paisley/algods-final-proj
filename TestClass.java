import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Scanner;
public class TestClass {
    private int runs;
    public TestClass(){
        this.runs = 0;
    }

    public TestClass(int runs){
        this();
        this.runs = runs;
    }

    public int getRuns() {
        return runs;
    }

    public void setRuns(int runs) {
        this.runs = runs;
    }

    public static void main(String[] args){/*
        System.out.print("Hello World!");
        Scanner input = new Scanner(System.in);
        int num = input.nextInt();
        TestClass test = new TestClass(num);
        //test.setRuns(num);
        System.out.println(test.getRuns());
        test.setRuns(0);
        System.out.println(test.getRuns());*/
        BusManagementSystem.fillStops();
        BusManagementSystem.fillGraph();
        BusManagementSystem.findShortestPath("EB W 41 AVE FS COLLINGWOOD ST","WB HASTINGS ST FS SPERLING AVE");
        BusManagementSystem.searchBusStops("HAS");
        BusManagementSystem.searchArrivalTime("25:25:00");
    }
}
