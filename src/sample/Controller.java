package sample;

import java.io.*;
import java.awt.Desktop;
import java.util.Random;
import java.util.Date;

import java.net.URL;
import java.util.ResourceBundle;
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

    Date date = new Date();

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button Button_NewGame_ResetPoints;

    @FXML
    private Label Current_Points_Num_Text;

    @FXML
    private Label Game_Results;

    @FXML
    private TextField Field_Input_Bet;

    @FXML
    private TextField Field_Input_Prediction;

    @FXML
    private Button Button_ShowGameLog;

    @FXML
    private Button Button_Play;

    @FXML
    private Label Errors;

    @FXML
    private Button Button_Save_In_File;

    @FXML
    void initialize(){
        Button_Save_In_File.setDisable(true);
        Button_Save_In_File.setVisible(false);

        Button_ShowGameLog.setOnAction(event -> {
            if (!file.exists()){
                try {
                    file.createNewFile();
                } catch (IOException e) {
                }
            }
            try {
                desktop.open(file);
            } catch (IOException e) {
            }
        });

        Button_NewGame_ResetPoints.setOnAction(event -> {
            Current_Points_Num_Text.setText("100");

        });

        Button_Play.setOnAction(event ->{
            current_points_num = Integer.parseUnsignedInt(Current_Points_Num_Text.getText());
            try {
                bet = Integer.parseUnsignedInt(Field_Input_Bet.getText());
                prediction = Integer.parseUnsignedInt(Field_Input_Prediction.getText());
                if (bet > current_points_num){
                    errors_list = errors_list + "-You don't have enough points!\n";
                    Game_Results.setText("");
                }
                if (prediction > 12 || prediction < 2){
                    errors_list = errors_list + "-Prediction must be in range\n of 2 to 12(inclusively)!";
                    Game_Results.setText("");
                }
                else if (bet <= current_points_num) {
                    first_dice = random.nextInt(7-1) + 1;
                    second_dice = random.nextInt(7-1) + 1;
                    dices_sum = first_dice + second_dice;

                    if(prediction == dices_sum){
                        results = results + "YOU WIN!" + "\nPoints won: " + String.valueOf(4 * bet) + "\n\n";
                        Current_Points_Num_Text.setText(String.valueOf(current_points_num + 4 * bet));
                    }
                    else if((prediction <= 7 && dices_sum <= 7) || (prediction > 7 && dices_sum > 7)){
                        results = results + "YOU WIN!" + "\nPoints won: " + String.valueOf(bet) + "\n\n";
                        Current_Points_Num_Text.setText(String.valueOf(current_points_num + bet));
                    }
                    else {
                        results = results + "YOU LOSE!" + "\nPoints lost: " + String.valueOf(bet) + "\n\n";
                        Current_Points_Num_Text.setText(String.valueOf(current_points_num - bet));
                    }
                    results = results + "First dice: " + String.valueOf(first_dice) + "\nSecond dice: "
                    + String.valueOf(second_dice) + "\nSum of points on dices: " + String.valueOf(dices_sum)
                            + "\nYour bet: " + String.valueOf(bet) + "\nYour prediction: " + String.valueOf(prediction);
                    Game_Results.setText(results);
                    results = "";

                    Button_Save_In_File.setDisable(false);
                    Button_Save_In_File.setVisible(true);
                }
            }
            catch (NumberFormatException nfe){
                errors_list = errors_list + "-Gaps must be filled\nwith Non-negative numbers!\n";
                Game_Results.setText("");
            }
            Errors.setText(errors_list);
            errors_list = "";
        });

        Button_Save_In_File.setOnAction(event -> {
            if(Game_Results.getText().equals("")){
                Errors.setText("Play first!");
            }
            else {
                date = new Date();
                BufferedWriter write_result;
                {
                    try {
                        write_result = new BufferedWriter(new FileWriter(file, true));
                        write_result.write("-" + date.toString() + "-\n" + Game_Results.getText() +
                                "\n********************\n");
                        write_result.close();

                        Button_Save_In_File.setDisable(true);
                        Button_Save_In_File.setVisible(false);
                    } catch (IOException e) {
                    }
                }
            }
        });
    }
}