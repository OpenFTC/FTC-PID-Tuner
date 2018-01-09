/*
 * Copyright (c) 2018 FTC team 4634 FROGbots
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package net.frogbots.ftcpidtuner;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.Optional;

public class Main extends Application
{
    @Override
    public void start(Stage primaryStage) throws Exception
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("FTC PID Tuner");
        primaryStage.setScene(new Scene(root, 300, 275));

        TextInputDialog dialog = new TextInputDialog("192.168.49.1");
        dialog.setTitle("FTC PID Tuner");
        dialog.setHeaderText("Enter target IP address");
        dialog.setContentText("Please enter the IP address of your RC phone:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent() && !result.get().isEmpty())
        {
            System.out.println("Target IP: " + result.get());
            StaticVars.targetIP = result.get();
            primaryStage.show();
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("FTC PID Tuner");
            alert.setHeaderText("Application terminated!");
            alert.setContentText("You did not input an IP address!");
            alert.showAndWait();
            System.exit(1);
        }

        Controller controller = loader.getController();
        primaryStage.setOnCloseRequest((new EventHandler<WindowEvent>()
        {
            public void handle(WindowEvent we)
            {
                controller.exit();
            }
        }));
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}
