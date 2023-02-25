package View;

import ViewModel.MyViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import src.algorithms.mazeGenerators.Maze;
import src.algorithms.search.Solution;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Observable;
import java.util.ResourceBundle;

public interface IView {
    public void initialize(URL url, ResourceBundle resourceBundle);
    public void setViewModel( MyViewModel viewModel);
    public String get_update_player_position_row();
    public void set_update_player_position_row(String update_player_position_row);
    public String get_update_player_position_col();
    public void SolveMazeButton(ActionEvent event);
    public void StartNewGameButton();
    public void keyPressed(KeyEvent keyEvent);
    public void mouseClicked(MouseEvent mouseEvent);
    public void LoadMazeButton(ActionEvent actionEvent);
    public void SaveMazeButton(ActionEvent actionEvent);
    public void showAlert(String message);
    public void update(Observable o, Object arg);
    public void drawMaze();
    public void AboutButton(ActionEvent actionEvent);
    public void CloseButton(ActionEvent actionEvent);
    public void StartNewGameMenuButton(ActionEvent actionEvent);
    public void PropertiesButton(ActionEvent actionEvent);
    public void SaveGameMenuButton(ActionEvent actionEvent);
    public void LodeGameMenuButton(ActionEvent actionEvent);
    public void HelpButton(ActionEvent actionEvent);

}
