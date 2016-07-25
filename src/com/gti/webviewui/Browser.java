/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gti.webviewui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker.State;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author xach
 */
public class Browser extends Region {

    final WebView browser = new WebView();
    final WebEngine webEngine = browser.getEngine();
    private List<String> styleSheets = new ArrayList<String>();
    private List<String> javaScriptFiles = new ArrayList<String>();
    private Resources resources;
    private Object jsObject;

    public Browser(Resources resources, Object jsObject) {
        this.resources = resources;
        this.jsObject = jsObject;
        afterLoad();
        addListener();
    }

    public Browser run(String startfile) {
        //apply the styles
        getStyleClass().add("browser");
        // load the web page
        webEngine.load("");
        System.out.println(startfile);
        webEngine.load(startfile);
        //add the web view to the scene
        getChildren().add(browser);
        //add stylesheets;

        return this;
    }

    private Node createSpacer() {
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        return spacer;
    }

    public void addStyle(String style) {
        styleSheets.add(this.getClass().getClassLoader().getResource(style).toExternalForm());
    }

    public void addJs(String JS) {
        javaScriptFiles.add(this.getClass().getClassLoader().getResource(JS).toExternalForm());
    }

    public List<String> styleSheets() {
        return styleSheets;
    }

    public List<String> jsFiles() {
        return javaScriptFiles;
    }

    private void addStyleNodes() {
        Document doc = webEngine.getDocument();
        for (String styleSheet : styleSheets()) {
            Element styleNode = doc.createElement("link");

            styleNode.setAttribute("href", styleSheet);
            styleNode.setAttribute("rel", "stylesheet");
            styleNode.setAttribute("type", "text/css");
            doc.getDocumentElement().getElementsByTagName("head").item(0).appendChild(styleNode);
        }

    }

    private void addJsFiles() {
        Document doc = webEngine.getDocument();
        for (String jsFile : jsFiles()) {
            Element styleNode = doc.createElement("script");

            styleNode.setAttribute("src", jsFile);
            styleNode.setAttribute("type", "text/javascript");
            doc.getDocumentElement().getElementsByTagName("body").item(0).appendChild(styleNode);
        }

    }

    private void afterLoad() {
        webEngine.getLoadWorker().stateProperty().addListener(new ChangeListener<State>() {
            @Override
            public void changed(ObservableValue ov, State oldState, State newState) {
                System.out.println("newState: "+newState);
                if (newState == State.SUCCEEDED) {
                    JSObject jsobj = (JSObject) webEngine.executeScript("window");
                    jsobj.setMember("java", jsObject);

                    webEngine.executeScript("console.log = function(message)\n"
                            + "{\n"
                            + "    java.log(message);\n"
                            + "};");

                    System.out.println(webEngine.executeScript("document.documentElement.outerHTML"));

                }
            }

        });
    }

    @Override
    protected void layoutChildren() {
        double w = getWidth();
        double h = getHeight();
        layoutInArea(browser, 0, 0, w, h, 0, HPos.CENTER, VPos.CENTER);
    }

    @Override
    protected double computePrefWidth(double height) {
        return 750;
    }

    @Override
    protected double computePrefHeight(double width) {
        return 500;
    }

    private void addListener() {
        System.out.println("addListener");
        final Browser browserObj = this;
        webEngine.locationProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> prop, final String before, final String after) {
                System.out.println("Loaded: " + after);
                final String testString = "file:";
                System.out.println(testString);
                if (after.contains(testString)) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {

                            browserObj.processFileUrl(after);

                        }
                    });
                }

            }
        });
    }

    public void processFileUrl(String fileUrl) {
        try {

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(new URL(fileUrl).openStream()));

            String inputLine;
            String html = "";
            while ((inputLine = in.readLine()) != null) {
                html += inputLine + "\n";
            }
            in.close();
            resources.addContents(html);
            webEngine.loadContent(resources.html());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
