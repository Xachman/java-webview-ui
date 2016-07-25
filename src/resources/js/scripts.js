/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
$(document).ready(function() {
    $("#addFile").click(function (e) {
        e.preventDefault();
        java.selectFile(function(file) {
            console.log("file: "+file);
            $(".filename").text(file);
        });
        java.log('finished');
    })
    $("#addDest").click(function (e) {
        e.preventDefault();
        java.selectFolder(function(Folder) {
            console.log("Folder: "+Folder);
            $(".foldername").text(Folder);
        });
        java.log('finished');
    })
    $("#process").click(function (e) {
        e.preventDefault();
        console.log('processFile');
        java.processFile($(".filename").text(), $('#output').val(), $(".foldername").text(), $('#outputFileName').val());
    })
    console.log("test");
});
