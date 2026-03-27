import java.io.*;
import java.util.*;

public class Main {

    public static void main(String[] args) throws IOException {
        String fnam = "words-5757-data.txt";
        String ftest = "words-5757-test.txt";

        //Provided reader
        BufferedReader readWords = new BufferedReader(new InputStreamReader(new FileInputStream(fnam)));
        ArrayList<String> words = new ArrayList<String>();
        long startTime = System.currentTimeMillis();

        while (true) {
            String word = readWords.readLine();
            if (word == null) {
                break;
            }
            assert word.length() == 5;
            words.add(word);
        }

        //this will be a map of which words matches a certain word in the words list.
        Map<String, List<String>> graph = new HashMap<>();
        for (String w : words) {
            graph.put(w, new ArrayList<>());
        }


        //go through each word and create a list of words that matches that words criteria.
        // Build graph by comparing all word pairs: O(V^2)
        for (String x : words) {
            for (String y : words) {
                if (!x.equals(y) && validatePath(x, y)) {
                    graph.get(x).add(y);
                }
            }
        }

         /*
         for(String key : graph.keySet()){
         System.out.println(key + " : " + graph.get(key));
         }
         */



        //provided test reader
        BufferedReader readTest = new BufferedReader(new InputStreamReader(new FileInputStream(ftest)));
        while (true) {
            String line = readTest.readLine();
            if (line == null) {
                long end = System.currentTimeMillis();
                System.out.println("Tid: " + (end - startTime) + " ms");
                break;
            }
            assert line.length() == 11; // indatakoll, om man kör med assertions på
            String start = line.substring(0, 5);
            String goal = line.substring(6, 11);

            System.out.println(bfs(start, goal, graph));

        }
    }

    // checks if there is a valid edge from 'from' to 'to'
    // counts the occurrences of each letter using arrays indexed by the alphabet
    // ensures that the last four letters of 'from' exist in 'to' with the same multiplicity
    //complexity is O(1) since the word size as well as the alphabet are constant
    public static boolean validatePath(String from, String to) {
        int[] neededLetters = new int[26];
        int[] gotLetters = new int[26];

        for (int i = 1; i < 5; i++) {
            neededLetters[from.charAt(i) - 'a']++;
        }

        for (int i = 0; i < 5; i++) {
            gotLetters[to.charAt(i) - 'a']++;
        }

        for (int i = 0; i < 26; i++) {
            if (neededLetters[i] > gotLetters[i]) {
                return false;
            }
        }
        return true;

    }


    public static int bfs(String start, String goal, Map<String, List<String>> graph) {
        Queue<String> queue = new LinkedList<>(); //incoming
        Map<String, Integer> totalDistance = new HashMap<>();

        //add start to initialize BFS
        queue.add(start);
        //distance from start to itself is 0
        totalDistance.put(start, 0);


        while (!queue.isEmpty()) {
            String current = queue.poll();

            //if current is equal to goal, return distance to current node.
            if (current.equals(goal)) {
                return totalDistance.get(current);
            }

            // explore all neighbors of current node
            // update their distance and enqueue them if not visited
            for (String reachableWord : graph.get(current)) {
                if (!totalDistance.containsKey(reachableWord)) {
                    //update totalDistance
                    totalDistance.put(reachableWord, totalDistance.get(current) + 1);
                    queue.add(reachableWord);
                }
            }

        }

        //if no path is available, return -1
        return -1;
    }
}