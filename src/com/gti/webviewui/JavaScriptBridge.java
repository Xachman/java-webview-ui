/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gti.webviewui;

import java.io.File;
import javafx.application.Platform;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import netscape.javascript.JSObject;

/**
 *
 * @author xach
 */
public abstract class JavaScriptBridge {
    private Stage stage;

    public JavaScriptBridge(Stage stage) {
        
    }
    public void selectFile(final JSObject callback) {
        System.out.println(callback);
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            System.out.println(selectedFile);
            final Object f[] = new Object[2];
            f[0] = null;
            f[1] = selectedFile.toString();

            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    callback.call("call", f);

                }
            });

        }
    }

    public void selectFolder(final JSObject callback) {
        DirectoryChooser dirChooser = new DirectoryChooser();
        File selectedFile = dirChooser.showDialog(stage);
        if (selectedFile != null) {
            System.out.println(selectedFile);
            final Object f[] = new Object[2];
            f[0] = null;
            f[1] = selectedFile.toString();

            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    callback.call("call", f);

                }
            });

        }
    }

}
