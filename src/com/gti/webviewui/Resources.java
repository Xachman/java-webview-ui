/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gti.webviewui;


import com.sun.deploy.util.StringUtils;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author xach
 */
public class Resources {
    private String contents;
    private List<String> styles = new ArrayList();
    private List<String> footerScripts = new ArrayList();
    private List<String> headerScripts = new ArrayList();
    private List<String> locations = Arrays.asList("footerScripts", "headerScripts", "styles");
    
    public Resources() {

    }
    
    public void addContents(String html) {
        contents=html;
    }
    
    public void addStyle(String path) {
        styles.add(path);
    }
    
    public void addJsHead(String path) {
        headerScripts.add(path);
    }
    public void addJsFooter(String path) {
        footerScripts.add(path);
    }
    
    public String html() {
        String html = contents;
        for(String location: locations) {
            html = html.replace("{{"+location+"}}", processLocation(location));
        }
        return html;
    }
    
    private String compileStyles(List<String> styles) {
        List<String> compiledStyles = new ArrayList();
        for (String style: styles) {
            compiledStyles.add("<link href=\""+style+"\" type=\"text/css\" rel=\"stylesheet\" />");
        }
        
        return StringUtils.join(compiledStyles, "\n");
    }
    private String compileScripts(List<String> scripts) {
        List<String> compiledScripts = new ArrayList();
        for (String script: scripts) {
            compiledScripts.add("<script src=\""+script+"\" type=\"text/javascript\"></script>");
        }
        
        return StringUtils.join(compiledScripts ,"\n");
    }
    
    private String processLocation(String location) {
        switch(location) {
            case "footerScripts":
                return compileScripts(footerScripts);
            case "styles":
                return compileStyles(styles);
            case "headerScripts":
                return compileScripts(headerScripts);
        }
        
        return "";
    }
    public String randomHash() {
        try {
        String timeStamp = ""+new java.util.Date().getTime();
        String s= timeStamp;
        MessageDigest m=MessageDigest.getInstance("MD5");
        m.update(s.getBytes(),0,s.length());
        return new BigInteger(1,m.digest()).toString(16);
        }catch(Exception e) {
            e.printStackTrace();
        }
        return "false";
    }
}
