package View;


import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import src.algorithms.mazeGenerators.Maze;
import src.algorithms.mazeGenerators.Position;
import src.algorithms.search.AState;
import src.algorithms.search.MazeState;
import src.algorithms.search.Solution;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;



public class MazeDisplayer extends Canvas {
    private Maze maze;

    private int row_player =0;
    private int col_player =0;

    StringProperty imageFileNameWall = new SimpleStringProperty();
    StringProperty imageFileNamePlayer = new SimpleStringProperty();
    StringProperty imageFileNamePlayerEnd = new SimpleStringProperty();
    StringProperty imageWonGame = new SimpleStringProperty();
    StringProperty imageSolPath = new SimpleStringProperty();



    public String getImageSolPath() {
        return imageSolPath.get();
    }

    public void setImageSolPath(String imageSolPath) {
        this.imageSolPath.set(imageSolPath);
    }


    public String getImageWonGame() {
        return imageWonGame.get();
    }

    public void setImageWonGame(String imageWonGame) {
        this.imageWonGame.set(imageWonGame);
    }


    public String getImageFileNamePlayerEnd() {
        return imageFileNamePlayerEnd.get();
    }

    public void setImageFileNamePlayerEnd(String imageFileNamePlayerEnd) {
        this.imageFileNamePlayerEnd.set(imageFileNamePlayerEnd);
    }


    public String getImageFileNameWall() {
        return imageFileNameWall.get();
    }

    public void setImageFileNameWall(String imageFileNameWall) {
        this.imageFileNameWall.set(imageFileNameWall);
    }

    public String getImageFileNamePlayer() {
        return imageFileNamePlayer.get();
    }

    public void setImageFileNamePlayer(String imageFileNamePlayer) {
        this.imageFileNamePlayer.set(imageFileNamePlayer);
    }


    public int getRow_player() {
        return row_player;
    }

    public int getCol_player() {
        return col_player;
    }

    public void set_player_position(int row, int col){
        this.row_player = row;
        this.col_player = col;

        draw();

    }

    public String getInfo(){
        return "In this game you need to bring the knight to the princes\n you can use the buttons 1,2,3,4,6,7,8,9 ";
    }

    public void drawMaze(Maze maze)
    {
        this.maze = maze;
        draw();
    }

    public void draw()
    {
        if( maze!=null)
        {
            double canvasHeight = getHeight();
            double canvasWidth = getWidth();
            int row = maze.getRow();
            int col = maze.getColumn();
            double cellHeight = canvasHeight/row;
            double cellWidth = canvasWidth/col;
            GraphicsContext graphicsContext = getGraphicsContext2D();
            graphicsContext.clearRect(0,0,canvasWidth,canvasHeight);
            graphicsContext.setFill(Color.RED);
            double w,h;
            //Draw Maze
            Image wallImage = null;
            try {
                wallImage = new Image(new FileInputStream(getImageFileNameWall()));
            } catch (FileNotFoundException e) {
                System.out.println("There is no file....");
            }
            for(int i=0;i<row;i++)
            {
                for(int j=0;j<col;j++)
                {
                    if(maze.getindexValue(i,j) == 1) // Wall
                    {
                        h = i * cellHeight;
                        w = j * cellWidth;
                        if (wallImage == null){
                            graphicsContext.fillRect(w,h,cellWidth,cellHeight);
                        }else{
                            graphicsContext.drawImage(wallImage,w,h,cellWidth,cellHeight);
                        }
                    }

                }
            }


            double h_player = getRow_player() * cellHeight;
            double w_player = getCol_player() * cellWidth;
            double h_End = maze.getGoalPosition().getRowIndex() * cellHeight;
            double w_End = maze.getGoalPosition().getColumnIndex() * cellWidth;
            Image playerImage = null;
            Image EndPlayer = null;
            try {
                playerImage = new Image(new FileInputStream(getImageFileNamePlayer()));
                EndPlayer = new Image(new FileInputStream(getImageFileNamePlayerEnd()));
            } catch (FileNotFoundException e) {
                System.out.println("There is no Image player....");
            }

            graphicsContext.drawImage(playerImage,w_player,h_player,cellWidth,cellHeight);
            graphicsContext.drawImage(EndPlayer,w_End,h_End,cellWidth,cellHeight);
        }

    }

    public void Won(Maze maze) {
        double canvasHeight = getHeight();
        double canvasWidth = getWidth();
        int row = maze.getRow();
        int col = maze.getColumn();
        double cellHeight = canvasHeight/row;
        double cellWidth = canvasWidth/col;
        GraphicsContext graphicsContext = getGraphicsContext2D();
        graphicsContext.clearRect(0,0,canvasWidth,canvasHeight);
        graphicsContext.setFill(Color.RED);
        double h_End = cellHeight;
        double w_End = cellWidth;
        Image ImageWonGame = null;
        try {
            ImageWonGame = new Image(new FileInputStream(getImageWonGame()));
        }
        catch (FileNotFoundException e){
            System.out.println("There is no Image player....");

        }

        graphicsContext.drawImage(ImageWonGame,h_End,w_End,cellWidth+150,cellHeight+150);
    }

    public void ShowSolution(Solution sol) {
        if( maze!=null)
        {
            double canvasHeight = getHeight();
            double canvasWidth = getWidth();
            int row = maze.getRow();
            int col = maze.getColumn();
            double cellHeight = canvasHeight/row;
            double cellWidth = canvasWidth/col;
            GraphicsContext graphicsContext = getGraphicsContext2D();
            graphicsContext.clearRect(0,0,canvasWidth,canvasHeight);
            graphicsContext.setFill(Color.RED);
            double w,h;
            //Draw Maze

            Image wallImage = null;
            try {
                wallImage = new Image(new FileInputStream(getImageFileNameWall()));
            } catch (FileNotFoundException e) {
                System.out.println("There is no file....");
            }


            Image imageSolPath = null;
            try {
                imageSolPath= new Image(new FileInputStream(getImageSolPath()));
            } catch (FileNotFoundException e) {
                System.out.println("There is no file....");
            }

            ArrayList<AState> mazeSolutionSteps = sol.getSolutionPath();
            for (int i = 0; i < mazeSolutionSteps.size(); i++) {
                MazeState g = (MazeState) mazeSolutionSteps.get(i);
                int n=g.getPosition().getRowIndex();
                int r=g.getPosition().getColumnIndex();
                h = n * cellHeight;
                w = r * cellWidth;
                if (imageSolPath == null){
                    graphicsContext.fillRect(w,h,cellWidth,cellHeight);
                }else{
                    graphicsContext.drawImage(imageSolPath,w,h,cellWidth,cellHeight);
                }
            }


            for(int i=0;i<row;i++)
            {
                for(int j=0;j<col;j++)
                {

                    if(maze.getindexValue(i,j) == 1) // Wall
                    {
                        h = i * cellHeight;
                        w = j * cellWidth;
                        if (wallImage == null){
                            graphicsContext.fillRect(w,h,cellWidth,cellHeight);
                        }else{
                            graphicsContext.drawImage(wallImage,w,h,cellWidth,cellHeight);
                        }
                    }
                    }
                }

            double h_player = getRow_player() * cellHeight;
            double w_player = getCol_player() * cellWidth;
            double h_End = maze.getGoalPosition().getRowIndex() * cellHeight;
            double w_End = maze.getGoalPosition().getColumnIndex() * cellWidth;
            Image playerImage = null;
            Image EndPlayer = null;
            try {
                playerImage = new Image(new FileInputStream(getImageFileNamePlayer()));
                EndPlayer = new Image(new FileInputStream(getImageFileNamePlayerEnd()));
            } catch (FileNotFoundException e) {
                System.out.println("There is no Image player....");
            }

            graphicsContext.drawImage(playerImage,w_player,h_player,cellWidth,cellHeight);
            graphicsContext.drawImage(EndPlayer,w_End,h_End,cellWidth,cellHeight);
        }
    }


}
