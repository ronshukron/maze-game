package ViewModel;

import Model.IModel;
import javafx.scene.ImageCursor;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import src.Client.Client;
import src.Client.IClientStrategy;
import src.IO.MyDecompressorInputStream;
import src.algorithms.mazeGenerators.*;
import src.algorithms.search.AState;
import src.algorithms.search.Solution;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class MyViewModel extends Observable implements Observer {
    private IModel model;
    private Maze maze;
    private int rowChar;
    private int colChar;

    public MyViewModel(IModel model) {
        this.model = model;
        this.model.assignObserver(this);
        this.maze = null;
    }


    public Maze getMaze() {
        return maze;
    }

    public int getRowChar() {
        return rowChar;
    }

    public int getColChar() {
        return colChar;
    }

    @Override
    public void update(Observable o, Object arg) {
        if(o instanceof IModel)
        {
            if(maze == null)//generateMaze
            {
                this.maze = model.getMaze();
            }
            else {
                Maze maze = model.getMaze();

                if (maze == this.maze)//Not generateMaze
                {
                    int rowChar = model.getRowChar();
                    int colChar = model.getColChar();
                    if(this.colChar == colChar && this.rowChar == rowChar)//Solve Maze
                    {
                        model.getSolution();
                    }
                    else//Update location
                    {
                        this.rowChar = rowChar;
                        this.colChar = colChar;
                    }


                }
                else//GenerateMaze
                {
                    this.maze = maze;
                }
            }

            setChanged();
            notifyObservers();
        }
    }

    public void generateMaze(int row,int col) {
        this.model.generateRandomMaze(row, col);
        this.maze = this.model.getMaze();
    }

    public void moveCharacter(KeyEvent keyEvent)
    {
        int direction = -1;
        //System.out.println(keyEvent.getCode().getCode());
        if (keyEvent.getCode().getCode() == 97){
            direction = 1;
        }
        else if (keyEvent.getCode().getCode() == 98){
            direction = 2;
        }
        else if (keyEvent.getCode().getCode() == 99) {
            direction = 3;
        }
        else if (keyEvent.getCode().getCode() == 100) {
            direction = 4;
        }
        else if (keyEvent.getCode().getCode() == 101) {
            direction = 5;
        }
        else if (keyEvent.getCode().getCode() == 102) {
            direction = 6;
        }
        else if (keyEvent.getCode().getCode() == 103) {
            direction = 7;
        }
        else if (keyEvent.getCode().getCode() == 104) {
            direction = 8;
        }
        else if (keyEvent.getCode().getCode() == 105) {
            direction = 9;
        }
        model.updateCharacterLocation(direction);
    }

    public void solveMaze(Maze maze)
    {
        this.model.solveMaze(maze);
    }

    public Solution getSolution()
    {
        return model.getSolution();
    }

    public void load(String path){
        model.load(path);
        this.maze = this.model.getMaze();
    }
    public void save(){
        model.save();
    }

    public void stopPlaying(){
        model.stopPlaying();
    }

}