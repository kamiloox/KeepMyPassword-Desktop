package me.goral.keepmypassworddesktop.util;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import me.goral.keepmypassworddesktop.MainApp;
import me.goral.keepmypassworddesktop.controllers.MainAppController;
import me.goral.keepmypassworddesktop.database.DatabaseHandler;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

public class AlertsUtil {

    public static void showErrorDialog(String errTitle, String errHeader, String errBody){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(errTitle);
        alert.setHeaderText(errHeader);
        alert.setContentText(errBody);
        alert.getButtonTypes().clear();
        alert.setGraphic(new ImageView(MainApp.class.getResource("/me/goral/keepmypassworddesktop/images/error-64.png").toString()));
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(MainApp.class.getResourceAsStream("/me/goral/keepmypassworddesktop/images/access-32.png")));
        alert.getDialogPane().getStylesheets().add(MainApp.class.getResource("styles/dialog.css").toExternalForm());
        ButtonType btnConfirm = new ButtonType("OK");
        alert.getDialogPane().getButtonTypes().add(btnConfirm);

        Node confirm = alert.getDialogPane().lookupButton(btnConfirm);
        confirm.getStyleClass().add("btn");
        alert.showAndWait();
    }

    public static void showDeleteDataDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Deleting all data");
        alert.setHeaderText("You are about to wipe out all your data");
        alert.setContentText("Are you sure?");
        alert.getButtonTypes().clear();
        alert.getDialogPane().getStylesheets().add(MainApp.class.getResource("styles/dialog.css").toExternalForm());
        alert.setGraphic(new ImageView(MainApp.class.getResource("/me/goral/keepmypassworddesktop/images/warning-64.png").toString()));
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(MainApp.class.getResourceAsStream("/me/goral/keepmypassworddesktop/images/access-32.png")));

        ButtonType confirm = new ButtonType("Wipe data", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(confirm, cancel);

        Node btnConfirm = alert.getDialogPane().lookupButton(confirm);
        Node btnCancel = alert.getDialogPane().lookupButton(cancel);

        btnConfirm.getStyleClass().add("btn");
        btnCancel.getStyleClass().add("btn");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == confirm) {
            DatabaseHandler.truncateData();
            showInformationDialog("Information Dialog", "Data cleared", "All your passwords have been deleted.\n" +
                    "Have a great day!");
        }
    }

    public static void showLogoutDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logging out");
        alert.setHeaderText("You are about to log out");
        alert.setContentText("Are you sure?");
        alert.getButtonTypes().clear();
        alert.getDialogPane().getStylesheets().add(MainApp.class.getResource("styles/dialog.css").toExternalForm());
        alert.setGraphic(new ImageView(MainApp.class.getResource("/me/goral/keepmypassworddesktop/images/logout-64.png").toString()));
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(MainApp.class.getResourceAsStream("/me/goral/keepmypassworddesktop/images/access-32.png")));

        ButtonType confirm = new ButtonType("Log out", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(confirm, cancel);

        Node btnConfirm = alert.getDialogPane().lookupButton(confirm);
        Node btnCancel = alert.getDialogPane().lookupButton(cancel);

        btnConfirm.getStyleClass().add("btn");
        btnCancel.getStyleClass().add("btn");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == confirm){

            try {
                FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("layouts/main-app-view.fxml"));
                Parent root = loader.load();

                MainAppController controller = loader.getController();
                controller.setIsLogged();
                Scene sc = new Scene(root);
                String css = MainApp.class.getResource("styles/main.css").toExternalForm();
                sc.getStylesheets().add(css);
                MainApp.getStage().setScene(sc);
            } catch (Exception e){
                showExceptionStackTraceDialog(e);
            }
        }
    }
    
    public static void showInformationDialog(String infTitle, String infHeader, String infBody){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(infTitle);
        alert.setHeaderText(infHeader);
        alert.setContentText(infBody);
        alert.getButtonTypes().clear();
        alert.getDialogPane().getStylesheets().add(MainApp.class.getResource("styles/dialog.css").toExternalForm());
        alert.setGraphic(new ImageView(MainApp.class.getResource("/me/goral/keepmypassworddesktop/images/information-64.png").toString()));

        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(MainApp.class.getResourceAsStream("/me/goral/keepmypassworddesktop/images/access-32.png")));

        ButtonType btnConfirm = new ButtonType("OK");
        alert.getDialogPane().getButtonTypes().add(btnConfirm);

        Node confirm = alert.getDialogPane().lookupButton(btnConfirm);
        confirm.getStyleClass().add("btn");

        alert.showAndWait();
    }

    public static void showDeleteAccountDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Deleting account");
        alert.setHeaderText("You are about to delete your whole account");
        alert.setContentText("Are you sure?");
        alert.getButtonTypes().clear();
        alert.getDialogPane().getStylesheets().add(MainApp.class.getResource("styles/dialog.css").toExternalForm());
        alert.setGraphic(new ImageView(MainApp.class.getResource("/me/goral/keepmypassworddesktop/images/warning-64.png").toString()));
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(MainApp.class.getResourceAsStream("/me/goral/keepmypassworddesktop/images/access-32.png")));

        ButtonType confirm = new ButtonType("Delete account", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(confirm, cancel);

        Node btnConfirm = alert.getDialogPane().lookupButton(confirm);
        Node btnCancel = alert.getDialogPane().lookupButton(cancel);

        btnConfirm.getStyleClass().add("btn");
        btnCancel.getStyleClass().add("btn");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == confirm){

            try {

                FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("layouts/main-app-view.fxml"));
                Parent root = loader.load();

                Scene sc = new Scene(root);
                String css = MainApp.class.getResource("styles/main.css").toExternalForm();
                sc.getStylesheets().add(css);
                MainApp.getStage().setScene(sc);
                ConfUtil.deleteConfFiles();
                MainAppController controller = loader.getController();
                controller.handleAppRun();

            } catch (Exception e){
                showExceptionStackTraceDialog(e);
            }

        }
    }

    public static void showExceptionStackTraceDialog(Exception e){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Exception Dialog");
        alert.setHeaderText("Oh no! Error!");
        alert.setContentText("Please report that error to github, so that developer can repair it as soon as possible:\n" +
                "https://github.com/xEdziu/KeepMyPassword-Desktop/issues/new/choose");
        alert.getButtonTypes().clear();
        alert.setGraphic(new ImageView(MainApp.class.getResource("/me/goral/keepmypassworddesktop/images/error-64.png").toString()));
        alert.getDialogPane().getStylesheets().add(MainApp.class.getResource("styles/dialog.css").toExternalForm());

        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getDialogPane().getButtonTypes().add(okButtonType);
        Node okBtn = alert.getDialogPane().lookupButton(okButtonType);
        okBtn.getStyleClass().add("btn");

        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(MainApp.class.getResourceAsStream("/me/goral/keepmypassworddesktop/images/access-32.png")));

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        String exceptionText = sw.toString();

        Label label = new Label("Exception stacktrace: ");

        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);

        alert.getDialogPane().setExpandableContent(expContent);

        alert.showAndWait();
    }

    public static void showGeneratePasswordDialog(){
        Dialog<List<String>> dialog = new Dialog<>();
        dialog.setTitle("Generating new password");
        dialog.setHeaderText("Provide needed parameters:");
        dialog.setGraphic(new ImageView(MainApp.class.getResource("/me/goral/keepmypassworddesktop/images/add-key-64.png").toString()));
        dialog.getDialogPane().getStylesheets().add(MainApp.class.getResource("styles/dialog.css").toExternalForm());
        dialog.getDialogPane().getButtonTypes().clear();

        Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(MainApp.class.getResourceAsStream("/me/goral/keepmypassworddesktop/images/access-32.png")));

        ButtonType generateButtonType = new ButtonType("Generate", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        dialog.getDialogPane().getButtonTypes().setAll(generateButtonType, cancelButtonType);
        Node addBtn = dialog.getDialogPane().lookupButton(generateButtonType);
        Node cancelBtn = dialog.getDialogPane().lookupButton(cancelButtonType);
        addBtn.getStyleClass().add("btn");
        cancelBtn.getStyleClass().add("btn");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField length = new TextField();
        length.setText("1");
        TextField lowerNum = new TextField();
        lowerNum.setText("0");
        TextField upperNum = new TextField();
        upperNum.setText("0");
        TextField digitNum = new TextField();
        digitNum.setText("0");
        TextField specialNum = new TextField();
        specialNum.setText("0");

        grid.add(new Label("Length"), 0, 0);
        grid.add(length, 1, 0);
        grid.add(new Label("Number of lower case characters"),0, 1);
        grid.add(lowerNum, 1,1);
        grid.add(new Label("Number of upper case characters"), 0, 2);
        grid.add(upperNum, 1, 2);
        grid.add(new Label("Number of digits"),0, 3);
        grid.add(digitNum, 1, 3);
        grid.add(new Label("Number of special characters"), 0, 4);
        grid.add(specialNum, 1, 4);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == generateButtonType){
                List<String> r = new ArrayList<>();
                r.add(length.getText());
                r.add(lowerNum.getText());
                r.add(upperNum.getText());
                r.add(digitNum.getText());
                r.add(specialNum.getText());
                return r;
            }
            return null;
        });

        Optional<List<String>> res = dialog.showAndWait();
        res.ifPresent(result -> {
            String len = result.get(0);
            String lower = result.get(1);
            String upper = result.get(2);
            String digit = result.get(3);
            String special = result.get(4);

            int intLen, intLower, intUpper, intDigit, intSpecial;

            if (isInteger(len)) intLen = Integer.parseInt(len);
            else {
                showErrorDialog("Error Dialog", "Whoops!", "Length parameter is not a number!");
                return;
            }

            if (isInteger(lower)) intLower = Integer.parseInt(lower);
            else {
                showErrorDialog("Error Dialog", "Whoops!", "Lower characters parameter is not a number!");
                return;
            }

            if (isInteger(upper)) intUpper = Integer.parseInt(upper);
            else {
                showErrorDialog("Error Dialog", "Whoops!", "Upper parameter is not a number!");
                return;
            }

            if (isInteger(digit)) intDigit = Integer.parseInt(digit);
            else {
                showErrorDialog("Error Dialog", "Whoops!", "Digits parameter is not a number!");
                return;
            }


            if (isInteger(special)) intSpecial = Integer.parseInt(special);
            else {
                showErrorDialog("Error Dialog", "Whoops!", "Special chars parameter is not a number!");
                return;
            }
            String pwd = PasswordGeneratorUtil.generatePassword(intLen, intLower, intUpper, intDigit, intSpecial);
            if (pwd != null) showGeneratedPasswordDialog(pwd);

        });
    }

    public static void showGeneratedPasswordDialog(String pwd){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("New Password Dialog");
        alert.setHeaderText("Here is your new password!");
        alert.setContentText(pwd);
        alert.getButtonTypes().clear();
        alert.getDialogPane().getStylesheets().add(MainApp.class.getResource("styles/dialog.css").toExternalForm());
        alert.setGraphic(new ImageView(MainApp.class.getResource("/me/goral/keepmypassworddesktop/images/information-64.png").toString()));

        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(MainApp.class.getResourceAsStream("/me/goral/keepmypassworddesktop/images/access-32.png")));

        ButtonType btnConfirm = new ButtonType("OK", ButtonBar.ButtonData.CANCEL_CLOSE);
        ButtonType btnCopy = new ButtonType("Copy");
        alert.getDialogPane().getButtonTypes().addAll(btnCopy, btnConfirm);

        Node confirm = alert.getDialogPane().lookupButton(btnConfirm);
        Node copy = alert.getDialogPane().lookupButton(btnCopy);
        confirm.getStyleClass().add("btn");
        copy.getStyleClass().add("btn");

        alert.setResultConverter(dialogButton -> {
            if (dialogButton == btnCopy){
                final Clipboard clipboard = Clipboard.getSystemClipboard();
                final ClipboardContent clipboardContent = new ClipboardContent();
                clipboardContent.putString(pwd);
                clipboard.setContent(clipboardContent);
                if (clipboard.hasString()){
                    System.out.println(clipboard.getString());
                }
            }
            return null;
        });

        alert.showAndWait();
    }

    public static void showAddPasswordDialog(SecretKey key) {
        Dialog<List<String>> dialog = new Dialog<>();
        dialog.setTitle("Adding new password");
        dialog.setHeaderText("Fulfill form to add password to your database:");
        dialog.setGraphic(new ImageView(MainApp.class.getResource("/me/goral/keepmypassworddesktop/images/add-key-64.png").toString()));
        dialog.getDialogPane().getStylesheets().add(MainApp.class.getResource("styles/dialog.css").toExternalForm());
        dialog.getDialogPane().getButtonTypes().clear();

        Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(MainApp.class.getResourceAsStream("/me/goral/keepmypassworddesktop/images/access-32.png")));

        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().setAll(addButtonType, cancelButtonType);
        Node addBtn = dialog.getDialogPane().lookupButton(addButtonType);
        Node cancelBtn = dialog.getDialogPane().lookupButton(cancelButtonType);
        addBtn.getStyleClass().add("btn");
        cancelBtn.getStyleClass().add("btn");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField description = new TextField();
        description.setPromptText("Description");
        TextField username = new TextField();
        username.setPromptText("Username");
        TextField password = new TextField();
        password.setPromptText("Password");

        grid.add(new Label("Description"), 0, 0);
        grid.add(description, 1, 0);
        grid.add(new Label("Username"),0, 1);
        grid.add(username, 1,1);
        grid.add(new Label("Password"), 0, 2);
        grid.add(password, 1, 2);

        dialog.getDialogPane().setContent(grid);
        Platform.runLater(description::requestFocus);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType){
                List<String> r = new ArrayList<>();
                r.add(description.getText());
                r.add(username.getText());
                r.add(password.getText());
                return r;
            }
            return null;
        });

        Optional<List<String>> res = dialog.showAndWait();
        res.ifPresent(result -> {
            String descPlain = result.get(0);
            String unamePlain = result.get(1);
            String passPlain = result.get(2);
            String alg = "AES/CBC/PKCS5Padding";

            IvParameterSpec iv = AESUtil.generateIv();

            try {
                String descEnc = Base64.getEncoder().encodeToString(AESUtil.encrypt(alg, descPlain, key, iv).getBytes());
                String unameEnc = Base64.getEncoder().encodeToString(AESUtil.encrypt(alg, unamePlain, key, iv).getBytes());
                String passEnc = Base64.getEncoder().encodeToString(AESUtil.encrypt(alg, passPlain, key, iv).getBytes());

                String ivString = Base64.getEncoder().encodeToString(iv.getIV());

                if (DatabaseHandler.insertPassword(descEnc, unameEnc, passEnc, ivString)) {
                    showInformationDialog("Confirmation Dialog", "Password added",
                            "Your password has been added to database");
                } else {
                    showErrorDialog("Error dialog", "Something wrong happened",
                            "Please report that error to github, so that developer can repair it as soon as possible");
                }
            } catch (InvalidAlgorithmParameterException | InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException | IllegalBlockSizeException | BadPaddingException e) {
                showExceptionStackTraceDialog(e);
            }
        });
    }

    public static void showUpdatePasswordDialog(int id, String desc, String login, String pwd, SecretKey key, String iv) {
        Dialog<List<String>> dialog = new Dialog<>();
        dialog.setTitle("Updating password");
        dialog.setHeaderText("Fulfill form to update password in your database:");
        dialog.setGraphic(new ImageView(MainApp.class.getResource("/me/goral/keepmypassworddesktop/images/add-key-64.png").toString()));
        dialog.getDialogPane().getStylesheets().add(MainApp.class.getResource("styles/dialog.css").toExternalForm());
        dialog.getDialogPane().getButtonTypes().clear();

        Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(MainApp.class.getResourceAsStream("/me/goral/keepmypassworddesktop/images/access-32.png")));

        ButtonType addButtonType = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().setAll(addButtonType, cancelButtonType);
        Node addBtn = dialog.getDialogPane().lookupButton(addButtonType);
        Node cancelBtn = dialog.getDialogPane().lookupButton(cancelButtonType);
        addBtn.getStyleClass().add("btn");
        cancelBtn.getStyleClass().add("btn");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField description = new TextField();
        description.setText(desc);
        TextField username = new TextField();
        username.setText(login);
        TextField password = new TextField();
        password.setText(pwd);

        grid.add(new Label("Description"), 0, 0);
        grid.add(description, 1, 0);
        grid.add(new Label("Username"),0, 1);
        grid.add(username, 1,1);
        grid.add(new Label("Password"), 0, 2);
        grid.add(password, 1, 2);

        dialog.getDialogPane().setContent(grid);
        Platform.runLater(description::requestFocus);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType){
                List<String> r = new ArrayList<>();
                r.add(description.getText());
                r.add(username.getText());
                r.add(password.getText());
                return r;
            }
            return null;
        });

        Optional<List<String>> res = dialog.showAndWait();
        res.ifPresent(result -> {
            String descPlain = result.get(0);
            String unamePlain = result.get(1);
            String passPlain = result.get(2);
            String alg = "AES/CBC/PKCS5Padding";

            IvParameterSpec ivSpec = AESUtil.generateIv();

            try {
                String descEnc = Base64.getEncoder().encodeToString(AESUtil.encrypt(alg, descPlain, key, ivSpec).getBytes());
                String unameEnc = Base64.getEncoder().encodeToString(AESUtil.encrypt(alg, unamePlain, key, ivSpec).getBytes());
                String passEnc = Base64.getEncoder().encodeToString(AESUtil.encrypt(alg, passPlain, key, ivSpec).getBytes());

                String newIv = Base64.getEncoder().encodeToString(ivSpec.getIV());

                if (DatabaseHandler.updatePassword(descEnc, unameEnc, passEnc, newIv, id)) {
                    showInformationDialog("Confirmation Dialog", "Data updated",
                            "Your credentials has been updated");
                } else {
                    showErrorDialog("Error dialog", "Something wrong happened",
                            "Please report that error to github, so that developer can repair it as soon as possible");
                }
            } catch (InvalidAlgorithmParameterException | InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException | IllegalBlockSizeException | BadPaddingException e) {
                showExceptionStackTraceDialog(e);
            }
        });
    }

    public static boolean isInteger(String str) {
        if (str == null) {
            return false;
        }
        int length = str.length();
        if (length == 0) {
            return false;
        }
        int i = 0;
        if (str.charAt(0) == '-') {
            if (length == 1) {
                return false;
            }
            i = 1;
        }
        for (; i < length; i++) {
            char c = str.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }
}
