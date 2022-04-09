import java.util.Scanner;
public class AlgoFinalProj {
    public static void main(String[] args){
        Scanner input = new Scanner(System.in);
        boolean finished = false;
        BusManagementSystem.fillStops();
        BusManagementSystem.fillGraph();
        while(!finished){
            System.out.print("""
                    Welcome to Vancouver's Bus Management Service.
                    Please enter to corresponding number for the service you want to use:
                    \tShortest Path Between two stops: '1'
                    \tSearch For a Bust Stop: '2'
                    \tSearch For Trips Arriving at a Certain Time: '3'
                    \tTo exit the program type 'exit'
                    Enter:""");
            String entry = input.nextLine();
            if(entry.equalsIgnoreCase("exit")){
                finished = true;
                System.out.println("thank you for using the service");
                continue;
            }
            int i;
            try{
                i = Integer.parseInt(entry);
            }catch (NumberFormatException nfe){
                System.out.println("Invalid Entry");
                continue;
            }
            if(i == 1){
                boolean ext = false;
                while(!ext) {
                    System.out.print("""
                            You have selected the Shortest Path between two stops, please enter the two stops separated by a comma: A,B
                            \te.g.'EB W 41 AVE FS COLLINGWOOD ST,WB HASTINGS ST FS SPERLING AVE'
                            Enter:""");
                    entry = input.nextLine();
                    if(entry.equalsIgnoreCase("\\back")){
                        ext = true;
                        continue;
                    }
                    BusManagementSystem.findShortestPath(entry);
                }
            }
            if(i == 2){
                boolean ext = false;
                while(!ext){
                    System.out.print("""
                            You have selected the Search for Bus Stops, please enter the Keyword you want to search with
                            Enter:""");
                    entry = input.nextLine();
                    if(entry.equalsIgnoreCase("\\back")){
                        ext = true;
                        continue;
                    }
                    BusManagementSystem.searchBusStops(entry);
                }
            }
            if(i == 3){
                boolean ext = false;
                while(!ext){
                    System.out.print("""
                            You have selected the Search for Trips arriving at a given time, please enter the time you want in the form hh:mm:ss
                            \te.g. '12:25:00'
                            Enter:""");
                    entry = input.nextLine();
                    if(entry.equalsIgnoreCase("\\back")){
                        ext = true;
                        continue;
                    }
                    BusManagementSystem.searchArrivalTime(entry);
                }
            }
        }
    }
}
