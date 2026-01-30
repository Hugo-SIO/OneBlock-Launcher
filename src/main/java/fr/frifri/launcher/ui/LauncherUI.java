package fr.frifri.launcher.ui;

import fr.frifri.launcher.minecraft.MinecraftLauncher;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LauncherUI extends Application {

    @Override
    public void start(Stage stage) {
        Button play = new Button("Jouer");
        play.setOnAction(e -> MinecraftLauncher.launch());

        VBox root = new VBox(play);
        root.setAlignment(Pos.CENTER);

        stage.setScene(new Scene(root, 500, 300));
        stage.setTitle("OneBlock Launcher");
        stage.show();
    }

    public static void launchUI() {
        launch();
    }
}
