package io.github.railroad.objects;

import java.io.File;
import java.io.IOException;

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

// TODO: Come up with a cleaner way of doing this
public class GenerateForgeModWindow {

	public static void displayWindow(LanguageConfig langConfig) {
		Stage window = new Stage();
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle(langConfig.get("window.generateMod.forge"));
		window.setWidth(400);
		window.setHeight(300);
		window.setResizable(false);

		Label label = new Label(langConfig.get("menu.generateMod.modId"));
		Label label1 = new Label(langConfig.get("menu.generateMod.name"));
		Label label2 = new Label(langConfig.get("menu.generateMod.package"));
		Label label3 = new Label(langConfig.get("menu.generateMod.version"));
		Label label4 = new Label(langConfig.get("menu.generateMod.title.forge"));
		label4.setScaleX(2);
		label4.setScaleY(2);
		label4.setScaleZ(2);

		TextField tf = new TextField();
		tf.setMaxSize(200, 20);
		tf.setTranslateY(38);
		TextField tf1 = new TextField();
		tf1.setMaxSize(200, 20);
		tf1.setTranslateY(38);
		TextField tf2 = new TextField();
		tf2.setMaxSize(200, 20);
		tf2.setTranslateY(38);

		String[] versions = { "1.16", "1.16.1", "1.16.2", "1.16.3", "1.16.4", "1.16.5" };
		ComboBox c = new ComboBox(FXCollections.observableArrayList(versions));
		c.setMinHeight(20);
		c.setMinWidth(80);
		c.setTranslateX(-60);
		c.setTranslateY(37);
		String[] versionVersions = { "Latest", "Recommended" };
		ComboBox c2 = new ComboBox(FXCollections.observableArrayList(versionVersions));
		c2.setMinWidth(120);
		c2.setMinHeight(22);
		c2.setTranslateY(0);
		c2.setTranslateX(50);

		Button btn2 = UIUtils.createButton(langConfig.get("menu.generateMod.confirm"), event -> {
			if (tf1.getText() != null && c.getValue() != null && tf2 != null) {
				if (System.getProperty("java.version").startsWith("1.8")) {
					generateMod(window, tf, tf1, tf2, c);
				} else {
					boolean shouldClose = JavaVersionConfirmWindow.displayWindow("Java Version Warning",
							langConfig.get("dialog.versionWarning.prompt.1") + System.getProperty("java.version")
									+ langConfig.get("dialog.versionWarning.prompt.2"),
							langConfig);
					if (shouldClose) {
						generateMod(window, tf, tf1, tf2, c);
					}
				}
			} else {
				confirmWindow(langConfig.get("dialog.fillBoxes"));
			}
		});
		btn2.setTranslateX(-35);
		Button btn3 = UIUtils.createButton(langConfig.get("menu.generateMod.cancel"), event -> {
			window.close();
		});
		btn3.setTranslateY(-35);
		btn3.setTranslateX(35);

		VBox layout = new VBox(20);
		layout.getChildren().addAll(label1, label, label2, label3);
		layout.setAlignment(Pos.CENTER_LEFT);
		layout.setTranslateX(60);
		layout.setTranslateY(30);

		VBox layout2 = new VBox(12);
		layout2.getChildren().addAll(tf1, tf, tf2, c, c2);
		layout2.setAlignment(Pos.CENTER);
		layout2.setTranslateX(0);
		layout2.setTranslateY(13);

		VBox layout3 = new VBox(10);
		layout3.getChildren().addAll(btn2, btn3);
		layout3.setAlignment(Pos.CENTER);
		layout3.setTranslateY(25);

		VBox layout4 = new VBox(10);
		layout4.getChildren().addAll(label4);
		layout4.setAlignment(Pos.BOTTOM_CENTER);
		layout4.setTranslateY(20);

		BorderPane border = new BorderPane();
		border.setTop(layout4);
		border.setLeft(layout);
		border.setBottom(layout3);
		border.setCenter(layout2);

		Scene scene = new Scene(border);
		window.setScene(scene);
		window.showAndWait();
		window.centerOnScreen();
	}

	public static void generateMod(Stage window, TextField tf, TextField tf1, TextField tf2, ComboBox c) {
		//TODO: Get workspace
		File workspace = new File(/* workspace */"");
		if (!workspace.exists()) {
			workspace.mkdirs();
		}
		File proj = new File(workspace.getAbsolutePath() + "\\" + tf1.getText());
		if (!proj.exists()) {
			proj.mkdirs();
		}
		File packageDir = new File(proj + "\\" + "src\\main\\java\\" + tf2.getText().replace(".", "\\"));
		if (!packageDir.exists()) {
			packageDir.mkdirs();
		}
		File main = new File(packageDir + "\\" + tf1.getText() + ".java");
		if (!main.exists()) {
			try {
				main.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		File assetsDir = new File(proj + "\\" + "src\\main\\resources\\assets\\" + tf.getText());
		if (!assetsDir.exists()) {
			assetsDir.mkdirs();
		}
		File dataDir = new File(proj + "\\" + "src\\main\\resources\\data\\" + tf.getText());
		if (!dataDir.exists()) {
			dataDir.mkdirs();
		}
		File modtoml = new File(proj + "\\" + "src\\main\\resources\\mods.toml");
		if (!modtoml.exists()) {
			try {
				modtoml.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		window.close();
		// TODO: Setup the other forge modding stuff here
	}

	public static void confirmWindow(String s) {
		Stage error = new Stage();
		VBox layout = new VBox(10);
		Label boxlabel = new Label(s);
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