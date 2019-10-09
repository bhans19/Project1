import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Stack;

// Java program to find words in Boggle game grid of characters
class Main {
    // instance data
    static private ArrayList<String> lexicon = new ArrayList<>(); // word list
    static private ArrayList<String> foundWords = new ArrayList<>(); // result list
    static private Grid grid;     // represents a Boggle board
    static private int gridSize;  // set after Grid is read from file
    static private int longestWordLength = 0;  // used to cut down on search

    // read the lexicon or word list from a file, create an ArrayList of Strings
    // NOTE: assumes words are in alphabetical order
    static private void readWords() {
        try{
            BufferedReader br = new BufferedReader(new FileReader("src/twl06.txt"));
            String line;
            try {
                while ((line = br.readLine()) != null) {
                    if(line.length() > longestWordLength)
                        longestWordLength = line.length();
                    lexicon.add(line);
                }
            }
            finally {
                br.close();
            }
        } catch (IOException e){
            System.out.println("Exception while processing word list file.");
        }
    }

    // binary search on the lexicon to determine if a specific word (key)
    // is in the list -- assumes words in lexicon are in alphabetical order
    static private boolean binSearch(ArrayList<String> lexicon, String key){
        int first = 0;
        int last = lexicon.size() - 1;
        int mid = (first + last)/2;
        while(first<=last){
            if( (lexicon.get(mid)).compareTo(key) < 0 ){
                first = mid + 1;
            } else if (lexicon.get(mid).equals(key)){
                return true;
            } else {
                last = mid - 1;
            }
            mid = (first + last)/2;
        }
        return false;
    }

    // A method to check if a given string is present in lexicon.
    static private boolean isWord(String str)
    {
        // binary search lexicon
        return binSearch(lexicon, str);
    }

    boolean [][] copyArray(boolean visited[][]){
        boolean [][] result = new boolean[gridSize][gridSize];
        for(int row=0; row<gridSize; row++){
            System.arraycopy(visited[row],0, result[row], 0, gridSize);
        }
        return result;
    }

    // A recursive function to find all words present in Boggle grid
    // and add them to an ArrayList named foundWords
    static private void findWordsUtil(boolean visited[][],
                                      int i, int j, String str){

        Stack<StackNode> stack = new Stack<>();
        stack.push(new StackNode(visited, i, j, str));

        while( !stack.empty() ) {
            StackNode top = stack.pop();
            visited = top.lookbackVisited;
            i = top.lookbackI;
            j = top.lookbackJ;
            str = top.lookbackStr;

            // no need to keep exploring if longest possible word found
            if (str.length() == longestWordLength) {
                //return;
                continue;
            }

            // Mark current cell as visited and append current character
            // to String being built
            visited[i][j] = true;
            str = str + grid.getLetter(i, j);

            // If str is present in dictionary, then append it to foundWords
            if (str.length() > 1 && isWord(str) && !foundWords.contains(str)) {
                foundWords.add(str);
            }

            // Traverse 8 adjacent cells of boggle[i][j]
            for (int row = i - 1; row <= i + 1 && row < gridSize; row++) {
                if (row >= 0) {
                    for (int col = j - 1; col <= j + 1 && col < gridSize; col++)
                        if (col >= 0 && !visited[row][col]) {
                            //findWordsUtil(grid, visited, row, col, str);
                            stack.push(new StackNode(visited, row, col, str));
                        }
                }
            }

            // Mark visited of current cell as false and remove last char
            str = str.substring(0, str.length() - 1);
            visited[i][j] = false;

        }
    }

    // Prints all words present in dictionary.
    static void findWords(){

        // Mark all characters as not visited
        boolean visited[][] = new boolean[gridSize][gridSize];

        // Consider every character and look for all words
        // starting with this character
        for (int row = 0; row < gridSize; row++) {
            for (int col = 0; col < gridSize; col++) {
                findWordsUtil(visited, row, col,"");
            }
        }
    }

    // add up the score for the words stored in the ArrayList
    public static int score(ArrayList<String> words) {
        int total = 0;
        for (String s : words) {
            int wordLength = s.length();
            if (wordLength <= 4)
                total += 1;
            else if (wordLength == 5)
                total += 2;
            else if (wordLength == 6)
                total += 3;
            else if (wordLength == 7)
                total += 5;
            else if (wordLength > 7)
                total += 11;
        }
        return total;
    }

    // Driver program to test above function
    public static void main(String args[])
    {
        // read word list / lexicon
        readWords();
        System.out.println( "Words read:\n" + lexicon.size() + "\n\n");

        try {
            BufferedReader br = new BufferedReader(new FileReader("src/grid.txt"));
            boolean repeat = true;
            while(repeat){
                foundWords.clear();
                grid = new Grid(br);
                gridSize = grid.getGridSize();
                if(gridSize == 0)
                    repeat = false;
                else {
                    System.out.println("\n\nSolving Boggle Grid:\n" + grid);
                    findWords();
                    System.out.format("Found %d words\n", foundWords.size());
                    System.out.println("Words: " + foundWords);
                    System.out.println("Max score for this grid is: " + score(foundWords));
                }
            }
        } catch(IOException e) {
            System.out.println("Error reading size from grid.txt");
        }
    }
}
