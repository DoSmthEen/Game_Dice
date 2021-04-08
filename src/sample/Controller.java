package sample;

import java.io.*;
import java.awt.Desktop;
import java.util.Random;
import java.util.Date;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;


public class Controller{
    File file = new File("src/sample/game_log.txt");

    int bet;
    int prediction;

    int first_dice;
    int second_dice;
    int dices_sum;

    int current_points_num;

    String errors_list = "";
    String results = "";

    Random random = new Random();
    Desktop desktop = Desktop.getDesktop();

    Date date;

    @FXML
    private Button NewGame_ResetPoints;

    @FXML
    private Label CurrentPointsNum;

    @FXML
    private Label GameResults;

    @FXML
    private TextField InputBet;

    @FXML
    private TextField InputPrediction;

    @FXML
    private Button ShowGameLog;

    @FXML
    private Button Play;

    @FXML
    private Label Errors;

    @FXML
    private Button SaveInFile;

    @FXML
    void initialize(){
        SaveInFile.setDisable(true);
        SaveInFile.setVisible(false);

        ShowGameLog.setOnAction(event -> {
            if (!file.exists()){
                try {
                    file.createNewFile();
                } catch (IOException ignored) {
                }
            }
            try {
                desktop.open(file);
            } catch (IOException ignored) {
            }
        });

        NewGame_ResetPoints.setOnAction(event -> {
            CurrentPointsNum.setText("100");
            SaveInFile.setDisable(true);
            SaveInFile.setVisible(false);
            GameResults.setText("");
            Errors.setText("");
        });

        Play.setOnAction(event ->{
            current_points_num = Integer.parseUnsignedInt(CurrentPointsNum.getText());
            try {
                bet = Integer.parseUnsignedInt(InputBet.getText());
                prediction = Integer.parseUnsignedInt(InputPrediction.getText());
                if (bet > current_points_num){
                    errors_list = errors_list + "-You don't have enough points!\n";
                    GameResults.setText("");
                }
                if (prediction > 12 || prediction < 2){
                    errors_list = errors_list + "-Prediction must be in range\n of 2 to 12(inclusively)!";
                    GameResults.setText("");
                }
                else if (bet <= current_points_num) {
                    first_dice = random.nextInt(7-1) + 1;
                    second_dice = random.nextInt(7-1) + 1;
                    dices_sum = first_dice + second_dice;

                    if(prediction == dices_sum){
                        results = results + "YOU WIN!" + "\nPoints won: " + String.valueOf(4 * bet) + "\n\n";
                        CurrentPointsNum.setText(String.valueOf(current_points_num + 4 * bet));
                    }
                    else if((prediction <= 7 && dices_sum <= 7) || (prediction > 7 && dices_sum > 7)){
                        results = results + "YOU WIN!" + "\nPoints won: " + String.valueOf(bet) + "\n\n";
                        CurrentPointsNum.setText(String.valueOf(current_points_num + bet));
                    }
                    else {
                        results = results + "YOU LOSE!" + "\nPoints lost: " + String.valueOf(bet) + "\n\n";
                        CurrentPointsNum.setText(String.valueOf(current_points_num - bet));
                    }
                    results = results + "First dice: " + String.valueOf(first_dice) + "\nSecond dice: "
                    + String.valueOf(second_dice) + "\nSum of points on dices: " + String.valueOf(dices_sum)
                            + "\nYour bet: " + String.valueOf(bet) + "\nYour prediction: " + String.valueOf(prediction);
                    GameResults.setText(results);
                    results = "";

                    SaveInFile.setDisable(false);
                    SaveInFile.setVisible(true);
                }
            }
            catch (NumberFormatException nfe){
                errors_list = errors_list + "-Gaps must be filled\nwith Non-negative numbers!\n";
                GameResults.setText("");
            }
            Errors.setText(errors_list);
            errors_list = "";
        });

        SaveInFile.setOnAction(event -> {
            if(GameResults.getText().equals("")){
                Errors.setText("Play first!");
            }
            else {
                date = new Date();
                BufferedWriter write_result;
                {
                    try {
                        write_result = new BufferedWriter(new FileWriter(file, true));
                        write_result.write("-" + date.toString() + "-\n" + GameResults.getText() +
                                "\n********************\n");
                        write_result.close();

                        SaveInFile.setDisable(true);
                        SaveInFile.setVisible(false);
                    } catch (IOException ignored) {
                    }
                }
            }
        });
    }
}