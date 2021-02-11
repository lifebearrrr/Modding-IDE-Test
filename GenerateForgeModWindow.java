package io.github.railroad.objects;

import java.io.File;

import io.github.railroad.config.LanguageConfig;
import io.github.railroad.utility.UIUtils;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

// TODO: Come up with a cleaner way of doing this. This code was rushed.
public class GenerateForgeModWindow {

	public static void displayWindow(LanguageConfig langConfig) {
		Stage window = new Stage();
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle(langConfig.get("window.generateMod.forge"));
		window.setWidth(500);
		window.setHeight(500);
		window.setResizable(false);

		Label label1 = new Label(langConfig.get("menu.generateMod.name"));
		Label label2 = new Label(langConfig.get("menu.generateMod.package"));
		Label label3 = new Label(langConfig.get("menu.generateMod.version"));
		Label label4 = new Label(langConfig.get("menu.generateMod.title.forge"));
		label4.setScaleX(3);
		label4.setScaleY(3);
		label4.setScaleZ(3);

		TextField tf1 = new TextField();
		tf1.setMaxSize(200, 20);
		TextField tf2 = new TextField();
		tf2.setMaxSize(200, 20);

		String[] versions = { "1.16", "1.16.1", "1.16.2", "1.16.3", "1.16.4", "1.16.5" };
		ComboBox c = new ComboBox(FXCollections.observableArrayList(versions));
		Button btn = UIUtils.createButton(langConfig.get("menu.generateMod.browse"), event -> {
			CreateNewFolderWindow w = new CreateNewFolderWindow(langConfig.get("menu.generateMod.confirm"), langConfig.get("menu.ok"));
			w.makeWindow();
			tf1.setText(w.txt);
		});

		Button btn2 = UIUtils.createButton(langConfig.get("menu.generateMod.confirm"), event -> {
			if (tf1.getText() != null && c.getValue() != null && tf2 != null) {
				if (System.getProperty("java.version").startsWith("1.8")) {
					generateMod(window, tf1, tf2, c);
				} else {
					boolean shouldClose = JavaVersionConfirmWindow.displayWindow("Java Version Warning",
							langConfig.get("dialog.versionWarning.prompt.1") + System.getProperty("java.version")
									+ langConfig.get("dialog.versionWarning.prompt.2"), langConfig);
					if (shouldClose) {
						generateMod(window, tf1, tf2, c);
					}
				}
			} else {
				confirmWindow();
			}
		});
		Button btn3 = UIUtils.createButton(langConfig.get("menu.generateMod.cancel"), event -> {
			window.close();
		});

		VBox layout = new VBox(20);
		layout.getChildren().addAll(label1, label2, label3);
		layout.setAlignment(Pos.CENTER_LEFT);
		layout.setTranslateX(70);

		VBox layout2 = new VBox(10);
		layout2.getChildren().addAll(tf1, tf2, c);
		layout2.setAlignment(Pos.CENTER);

		VBox layout3 = new VBox(10);
		layout3.getChildren().addAll(btn);
		layout3.setAlignment(Pos.CENTER_RIGHT);
		layout3.setTranslateX(-70);
		layout3.setTranslateY(-35);

		VBox layout4 = new VBox(10);
		layout4.getChildren().addAll(btn2, btn3);
		layout4.setAlignment(Pos.CENTER);
		layout4.setTranslateY(-20);

		VBox layout5 = new VBox(10);
		layout5.getChildren().addAll(label4);
		layout5.setAlignment(Pos.BOTTOM_CENTER);
		layout5.setTranslateY(60);

		BorderPane border = new BorderPane();
		border.setTop(layout5);
		border.setLeft(layout);
		border.setCenter(layout2);
		border.setRight(layout3);
		border.setBottom(layout4);

		Scene scene = new Scene(border);
		window.setScene(scene);
		window.showAndWait();
		window.centerOnScreen();
	}

	public static void generateMod(Stage window, TextField tf1, TextField tf2, ComboBox c) {
		File workspace = new File(tf1.getText());
		if (!workspace.exists()) {
			workspace.mkdirs();
		}
		File proj = new File(workspace.getAbsolutePath() + "\\" + tf2.getText());
		if (!proj.exists()) {
			proj.mkdirs();
		}
		window.close();
		// Setup the forge stuff here
	}

	public static void confirmWindow() {
		Stage error = new Stage();
		VBox layout = new VBox(10);
		Label boxlabel = new Label("All boxes need to be filled");
		Button okbtn = UIUtils.createButton("OK", okevent -> {
			error.close();
		});
		layout.getChildren().addAll(boxlabel, okbtn);
		layout.setAlignment(Pos.CENTER);
		Scene scene = new Scene(layout);
		error.setScene(scene);
		error.setWidth(300);
		error.setHeight(100);
		error.showAndWait();
	}
	
}