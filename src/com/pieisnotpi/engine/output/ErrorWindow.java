package com.pieisnotpi.engine.output;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class ErrorWindow extends Application
{
    public static void create(Exception exception)
    {
        File f = new File("crash.log");
        try(PrintWriter writer = new PrintWriter(f))
        {
            writer.println(exception.getMessage());
            StackTraceElement[] stacktrace = exception.getStackTrace();
            for(StackTraceElement element : stacktrace) writer.println("\t" + element);
            Logger.SYSTEM.log("Crash log written to " + f.getAbsolutePath());
        }
        catch(IOException e) { Logger.SYSTEM.err("Unable to write crash log to " + f.getAbsolutePath()); }
        launch(exception.getLocalizedMessage());
    }

    @Override
    public void start(Stage stage) throws Exception
    {
        String message = getParameters().getUnnamed().get(0);

        StackPane pane = new StackPane();
        pane.setPadding(new Insets(25));
        Scene scene = new Scene(pane);

        HBox tBox = new HBox(), okBox = new HBox();
        tBox.setAlignment(Pos.TOP_LEFT);
        okBox.setAlignment(Pos.BOTTOM_RIGHT);

        Text text = new Text(message);
        text.setWrappingWidth(250);
        text.setTextAlignment(TextAlignment.LEFT);
        tBox.getChildren().add(text);

        Button okButton = new Button("OK");
        okButton.setOnAction(ae -> stage.close());
        okBox.getChildren().add(okButton);

        pane.getChildren().addAll(tBox, okBox);
        stage.setScene(scene);
        stage.setTitle("Pi-Engine");
        stage.show();
    }
}
