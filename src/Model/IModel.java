package Model;

import src.algorithms.mazeGenerators.Maze;
import src.algorithms.search.Solution;

import java.util.Observer;

public interface IModel {
    public void generateRandomMaze(int row,int col );
    public Maze getMaze();
    public void updateCharacterLocation(int direction);
    public int getRowChar();
    public int getColChar();
    public void assignObserver(Observer o);
    public void solveMaze(Maze maze);
    public Solution getSolution();
    public void load(String path);
    public void save();
    public void stopPlaying();
}