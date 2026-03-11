import java.io.*;
import java.util.*;

public class Main {

    public static void main(String[] args) throws IOException {
        String fnam = "words-5757-data.txt";
        String ftest = "words-5757-test.txt";

        //Provided reader
        BufferedReader readWords = new BufferedReader(new InputStreamReader(new FileInputStream(fnam)));
        ArrayList<String> words = new ArrayList<String>();
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
        //kvadratisk O(V^2)
        for (String x : words) {
            for (String y : words) {
                if (!x.equals(y) && validatePath(x, y)) {
                    graph.get(x).add(y);
                }
            }
        }


        //provided test reader
        BufferedReader readTest = new BufferedReader(new InputStreamReader(new FileInputStream(ftest)));
        while (true) {
            String line = readTest.readLine();
            if (line == null) {
                break;
            }
            assert line.length() == 11; // indatakoll, om man kör med assertions på
            String start = line.substring(0, 5);
            String goal = line.substring(6, 11);

            System.out.println(bfs(start, goal, graph));
        }
    }

    //this method checks if the connection matches the criteria
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
        Set<String> visited = new HashSet<>();
        Map<String, Integer> dist = new HashMap<>();

        queue.add(start);
        dist.put(start, 0);
        visited.add(start);


        while(!queue.isEmpty()){

            String current = queue.poll();
            int distance = dist.get(current);
            if(current.equals(goal)){
                return dist.get(current);
            }

            for(String matches : graph.get(current)){
                if (!dist.containsKey(matches)){
                    visited.add(matches);
                    distance = dist.get(current)+1;
                    dist.put(matches, dist.get(current) + 1);
                    queue.add(matches);
                }

            }


        }

        //if no path is available, return -1
        return -1;
    }
}