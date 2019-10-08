public class StackNode {
    public boolean [][] lookbackVisited;
    public int lookbackI;
    public int lookbackJ;
    public String lookbackStr;

    public StackNode( boolean[][] visited, int i, int j, String str){
        lookbackVisited = copyArray( visited );
        lookbackI = i;
        lookbackJ = j;
        lookbackStr = str;
    }

    boolean [][] copyArray(boolean visited[][]){
        int size = visited[0].length;
        boolean [][] result = new boolean[size][size];
        for(int row=0; row<size; row++){
            System.arraycopy(visited[row],0, result[row], 0, size);
        }
        return result;
    }
}
