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
    private final File file = new File("src/sample/game_log.txt");
    private int bet;
    private int prediction;
    private int first_dice;
    private int second_dice;
    private int dices_sum;
    private int current_points_num;
    private String errors_list = "";
    private String results = "";
    private final Random random = new Random();
    private final Desktop desktop = Desktop.getDesktop();
    private Date date;
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
    void SaveInFile_status(boolean set_disable, boolean set_visible){
        SaveInFile.setDisable(set_disable);
        SaveInFile.setVisible(set_visible);
    }
    @FXML
    void initialize(){
        SaveInFile_status(true, false);
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
            SaveInFile_status(true, false);
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
                        results = results + "YOU WIN!" + "\nPoints won: " + 4 * bet + "\n\n";
                        CurrentPointsNum.setText(String.valueOf(current_points_num + 4 * bet));
                    }
                    else if((prediction <= 7 && dices_sum <= 7) || (prediction > 7 && dices_sum > 7)){
                        results = results + "YOU WIN!" + "\nPoints won: " + bet + "\n\n";
                        CurrentPointsNum.setText(String.valueOf(current_points_num + bet));
                    }
                    else {
                        results = results + "YOU LOSE!" + "\nPoints lost: " + bet + "\n\n";
                        CurrentPointsNum.setText(String.valueOf(current_points_num - bet));
                    }
                    results = results + "First dice: " + first_dice + "\nSecond dice: "
                    + second_dice + "\nSum of points on dices: " + dices_sum
                            + "\nYour bet: " + bet + "\nYour prediction: " + prediction;
                    GameResults.setText(results);
                    results = "";
                    SaveInFile_status(false, true);
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
                        SaveInFile_status(true, false);
                    } catch (IOException ignored) {
                    }
                }
            }
        });
    }
}