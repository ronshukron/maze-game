package View;

import ViewModel.MyViewModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import src.algorithms.mazeGenerators.Maze;
import src.algorithms.mazeGenerators.MyMazeGenerator;
import src.algorithms.search.Solution;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.PublicKey;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

public class MyViewController  implements IView,Initializable,Observer {

    public TextField pathMaze;
    public AnchorPane anchorPane;

    private MyViewModel viewModel;

    @FXML
    private TextField RowNum;

    @FXML
    private TextField columnNum;
    @FXML

    private Label PlayerColumn;

    @FXML
    private Label PlayersRow;

    public MazeDisplayer mazeDisplayer;
    StringProperty update_player_position_row = new SimpleStringProperty();
    StringProperty update_player_position_col = new SimpleStringProperty();

    private Maze maze;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        PlayersRow.textProperty().bind(update_player_position_row);
        PlayerColumn.textProperty().bind(update_player_position_col);
    }

    public void setViewModel( MyViewModel viewModel) {
        this.viewModel = viewModel;
    }

    public String get_update_player_position_row() {
        return update_player_position_row.get();
    }

    public void set_update_player_position_row(String update_player_position_row) {
        this.update_player_position_row.set(update_player_position_row);
    }

    public String get_update_player_position_col() {
        return update_player_position_col.get();
    }
    public void set_update_player_position_col(String update_player_position_col) {
        this.update_player_position_col.set(update_player_position_col);
    }

    @FXML
    public void SolveMazeButton(ActionEvent event) {
        if( maze==null){
            showAlert("There is No Maze to Solve ... ");
            return;
        }
        viewModel.solveMaze(this.maze);

    }

    public void StartNewGameButton()
    {
        int rows = Integer.valueOf(RowNum.getText());
        int cols = Integer.valueOf(columnNum.getText());
        viewModel.generateMaze(rows,cols);
    }


    public void keyPressed(KeyEvent keyEvent) {
        viewModel.moveCharacter(keyEvent);
        keyEvent.consume();
    }

    public void mouseClicked(MouseEvent mouseEvent) {
        mazeDisplayer.requestFocus();
    }




    public void LoadMazeButton(ActionEvent actionEvent) {
        if( String.valueOf(pathMaze.getText()).isEmpty()){
            showAlert("There is No Maze Path to load ... ");
            return;
        }
        Path p = Paths.get(pathMaze.getText());
        if( Files.notExists(p )){
            showAlert("There is No Maze Path to load ... ");
            return;
        }

        String path = String.valueOf(pathMaze.getText());
        viewModel.load(path);
    }

    public void SaveMazeButton(ActionEvent actionEvent) {
        if( maze==null){
            showAlert("There is No Maze to Save ... ");
            return;
        }
       viewModel.save();
    }

    public void showAlert(String message)
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);;
        alert.show();
    }

    @Override
    public void update(Observable o, Object arg) {
        if(o instanceof MyViewModel)
        {
            if(maze == null)//generateMaze
            {
                this.maze = viewModel.getMaze();
                drawMaze();
                this.mazeDisplayer.set_player_position(maze.getStartPosition().getRowIndex(),maze.getStartPosition().getColumnIndex());

            }
            else {
                Maze maze = viewModel.getMaze();

                if (maze == this.maze)//Not generateMaze
                {
                    int rowChar = mazeDisplayer.getRow_player();
                    int colChar = mazeDisplayer.getCol_player();
                    int rowFromViewModel = viewModel.getRowChar();
                    int colFromViewModel = viewModel.getColChar();
                    if(rowFromViewModel == rowChar && colFromViewModel == colChar)//Solve Maze
                    {
                        //viewModel.getSolution();
                        Solution sol = viewModel.getSolution();
                        this.mazeDisplayer.ShowSolution(sol);
                        showAlert("Solving Maze ... ");
                    }
                    else//Update location
                    {
                        set_update_player_position_row(rowFromViewModel + "");
                        set_update_player_position_col(colFromViewModel + "");
                        this.mazeDisplayer.set_player_position(rowFromViewModel,colFromViewModel);
                        if( rowFromViewModel == maze.getGoalPosition().getRowIndex() && colFromViewModel == maze.getGoalPosition().getColumnIndex()){
                            this.mazeDisplayer.Won(this.maze);
                        }
                    }
                }
                else//GenerateMaze
                {
                    this.maze = maze;
                    this.mazeDisplayer.set_player_position(maze.getStartPosition().getRowIndex(),maze.getStartPosition().getColumnIndex());
                    drawMaze();
                }
            }
        }
    }

    public void drawMaze()
    {
        mazeDisplayer.drawMaze(maze);
    }


    public void AboutButton(ActionEvent actionEvent) {
        String Info = "Noa Brosh and Ron Shukron ";
        showAlert(Info);
    }


    public void CloseButton(ActionEvent actionEvent) {
        anchorPane.getScene().getWindow().hide();
        viewModel.stopPlaying();
    }


    public void StartNewGameMenuButton(ActionEvent actionEvent) {
        int rows = Integer.valueOf(RowNum.getText());
        int cols = Integer.valueOf(columnNum.getText());
        viewModel.generateMaze(rows,cols);
    }

    public void PropertiesButton(ActionEvent actionEvent) {

    }

    public void SaveGameMenuButton(ActionEvent actionEvent) {
        viewModel.save();
    }


    public void LodeGameMenuButton(ActionEvent actionEvent) {
        String path = String.valueOf(pathMaze.getText());
        viewModel.load(path);
    }


    public void HelpButton(ActionEvent actionEvent) {
        String Info = mazeDisplayer.getInfo();
        showAlert(Info);
    }
}
