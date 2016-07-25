/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gti.webviewui;

import java.net.URL;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 *
 * @author xach
 */
public class JavaWebviewUi extends Application {

    private Scene scene;
    private static Stage stage;

    @Override
    public void start(Stage pStage) {
        // create the scene
        stage = pStage;
        stage.setTitle("Web View");

        URL startFile = this.getClass().getResource("/resources/html/layout.html");
        
        System.out.println("startFile: "+startFile);

        Resources resources = new Resources();
        resources.addStyle(this.getResource("bower_components/bootstrap/dist/css/bootstrap.css").toExternalForm());
        resources.addStyle(this.getResource("css/styles.css").toExternalForm());
        resources.addJsHead(this.getResource("bower_components/jquery/dist/jquery.js").toExternalForm());
        resources.addJsHead(this.getResource("js/scripts.js").toExternalForm());
        Browser browser = new Browser(resources, new JsObject(stage));

        scene = new Scene(browser.run(startFile.toExternalForm()), 750, 500, Color.web("#666970"));
        stage.setScene(scene);
        scene.getStylesheets().add("webviewsample/BrowserToolbar.css");
        stage.show();

    }

    public static void main(String[] args){
        launch(args);
    }
    
    public URL getResource(String path) {
        return this.getClass().getResource("/resources/"+path);
    }

}

class JsObject extends JavaScriptBridge {
        
    public JsObject(Stage stage) {
        super(stage);
    }
    
    
}