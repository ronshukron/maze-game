package Model;

import src.IO.MyDecompressorInputStream;
import src.Server.Server;
import src.Server.ServerStrategyGenerateMaze;
import src.Server.ServerStrategySolveSearchProblem;
import src.algorithms.mazeGenerators.IMazeGenerator;
import src.algorithms.mazeGenerators.Maze;
import src.algorithms.mazeGenerators.MyMazeGenerator;
import src.algorithms.search.Solution;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Observable;
import java.util.Observer;

public class MyModel  extends Observable implements IModel{
    private Maze maze;
    private int rowChar;
    private int colChar;
    private  Server mazeGeneratingServer;
    private Server solveSearchProblemServer;
    Solution sulotion;
//    Socket socket;

    public MyModel() {
        maze = null;
        rowChar =0;
        colChar =0;
        sulotion = null;

        mazeGeneratingServer = new Server(5400, 1000, new ServerStrategyGenerateMaze());
        solveSearchProblemServer = new Server(5401, 1000, new ServerStrategySolveSearchProblem());

        solveSearchProblemServer.start();
        mazeGeneratingServer.start();
//        try {
//            socket = new Socket(InetAddress.getLocalHost(),5400);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        //System.out.println("starting server");

    }

    public void updateCharacterLocation(int direction)
    {
        /*
            direction = 1 -> Down left
            direction = 2 -> Down
            direction = 3 -> Down right
            direction = 4 -> Left
            direction = 6 -> Right
            direction = 7 -> up left
            direction = 8 -> up
            direction = 9 -> up right

         */

        switch(direction)
        {
            case 1: //Down left
                if(rowChar!=0 && colChar != 0 && maze.getindexValue(rowChar+1,colChar-1) != 1){
                    rowChar++;
                    colChar--;
                    setChanged();
                    notifyObservers();
                }
                break;

            case 2: //Down
                if(rowChar!=maze.getRow()-1 && maze.getindexValue(rowChar+1,colChar) != 1){
                    rowChar++;
                    setChanged();
                    notifyObservers();
                }
                break;
            case 3: //Down right
                if(rowChar!=maze.getRow()-1 && colChar!=maze.getColumn()-1 && maze.getindexValue(rowChar+1,colChar+1) != 1){
                    rowChar++;
                    colChar++;
                    setChanged();
                    notifyObservers();
                }
                break;
            case 4: //Left
                if(colChar!=0 && maze.getindexValue(rowChar,colChar-1) != 1){
                    colChar--;
                    setChanged();
                    notifyObservers();
                }
                break;
            case 6: //Right
                if(colChar!= maze.getColumn()-1 && maze.getindexValue(rowChar,colChar+1) != 1){
                    colChar++;
                    setChanged();
                    notifyObservers();
                }
                break;
            case 7: //up left
                if(rowChar!=0 && colChar != 0 && maze.getindexValue(rowChar-1,colChar-1) != 1){
                    rowChar--;
                    colChar--;
                    setChanged();
                    notifyObservers();
                }
                break;
            case 8: //up
                if(rowChar!=0 && maze.getindexValue(rowChar-1,colChar) != 1){
                    rowChar--;
                    setChanged();
                    notifyObservers();
                }
                break;
            case 9: //up right
                if(rowChar!=0 && colChar!= maze.getColumn()-1 && maze.getindexValue(rowChar-1,colChar+1) != 1){
                    rowChar--;
                    colChar++;
                    setChanged();
                    notifyObservers();
                }
                break;
        }
    }

    public int getRowChar() {
        return rowChar;
    }

    public int getColChar() {
        return colChar;
    }

    @Override
    public void assignObserver(Observer o) {
        this.addObserver(o);
    }

    @Override
    public void solveMaze(Maze maze) {
        try {
            Socket socket = new Socket(InetAddress.getLocalHost(),5401);

            ObjectOutputStream toServer = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream fromServer = new ObjectInputStream(socket.getInputStream());
            toServer.flush();

            toServer.writeObject(maze); //send maze to server
            toServer.flush();
            sulotion = (Solution) fromServer.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        setChanged();
        notifyObservers();
    }

    @Override
    public Solution getSolution() {
        return this.sulotion;
    }

    public void generateRandomMaze(int row,int col)
    {
        try {
            Socket socket = new Socket(InetAddress.getLocalHost(),5400);
            ObjectOutputStream toServer = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream fromServer = new ObjectInputStream(socket.getInputStream());
            toServer.flush();
            int[] mazeDimensions = new int[]{row, col};
            toServer.writeObject(mazeDimensions); //send maze dimensions to server
            toServer.flush();
            byte[] compressedMaze = (byte[]) fromServer.readObject(); //read generated maze (compressed with MyCompressor) from server
            InputStream is = new MyDecompressorInputStream(new ByteArrayInputStream(compressedMaze));
            byte[] decompressedMaze = new byte[Maze.maze_size(compressedMaze)]; //allocating byte[] for the decompressed maze -
            is.read(decompressedMaze); //Fill decompressedMaze with bytes
            this.maze = new Maze(decompressedMaze);
            rowChar = maze.getStartPosition().getRowIndex();
            colChar = maze.getStartPosition().getColumnIndex();
            //maze.print();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        setChanged();
        notifyObservers();
    }

    public Maze getMaze() {
        return this.maze;
    }

    public void save(){
        try{
            String tempDirectoryPath = System.getProperty("java.io.tmpdir");

            String newPath = tempDirectoryPath + String.valueOf(maze.toString().hashCode());
            FileOutputStream f = new FileOutputStream(new File(newPath));
            System.out.println(newPath);
            System.out.println(f);
            ObjectOutputStream sul = new ObjectOutputStream(f);

            // write in the file the solution of the maze:
            sul.writeObject(this.maze);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void load(String path){
        try {


            FileInputStream mypath = new FileInputStream(path);
            ObjectInputStream solutionn = new ObjectInputStream(mypath);

            Maze finalSol =(Maze) solutionn.readObject();

            mypath.close();
            solutionn.close();
            this.maze = finalSol;
            rowChar = maze.getStartPosition().getRowIndex();
            colChar = maze.getStartPosition().getColumnIndex();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        setChanged();
        notifyObservers();
    }


    public void stopPlaying(){
        solveSearchProblemServer.stop();
        mazeGeneratingServer.stop();
        // should i update?
//        setChanged();
//        notifyObservers();
    }


}