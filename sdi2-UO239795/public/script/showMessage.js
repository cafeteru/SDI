"use strict";

class ShowMessage {
    constructor() {
    }

    getURLParameter(sParam) {
        var sPageURL = window.location.search.substring(1);
        var sURLVariables = sPageURL.split('&');
        for (var i = 0; i < sURLVariables.length; i++) {
            var sParameterName = sURLVariables[i].split('=');
            if (sParameterName[0] == sParam) {
                return sParameterName[1];
            }
        }
    }

    showMessage(name, type) {
        var message = this.getURLParameter(name);
        if (message !== undefined) {
            message = this.convertCharactersUTF8(message);
            var error = "<div class=\"alert alert-dismissible " + type + "\">\n" +
                "           <button type=\"button\" class=\"close\" data-dismiss=\"alert\">&times;</button>\n" +
                "                 <strong>" + message + "</strong>\n" +
                "            </div>";
            if (name === "coincidence")
                name = "passwordConfirm";
            $("#" + name).append(error);
        }
    }

    showErrors(name) {
        this.showMessage(name, "alert-danger")
    }

    showInfo(name) {
        this.showMessage(name, "alert-success")
    }

    convertCharactersUTF8(message) {
        message = message.split("%20").join(' ');
        message = message.split("%C3%A1").join('á');
        message = message.split("%C3%A9").join('é');
        message = message.split("%C3%AD").join('í');
        message = message.split("%C3%B3").join('ó');
        message = message.split("%C3%BA").join('ú');
        message = message.split("%C3%B1").join('ñ');
        message = message.split("%C3%81").join('Á');
        message = message.split("%C3%89").join('É');
        message = message.split("%C3%8D").join('Í');
        message = message.split("%C3%93").join('Ó');
        message = message.split("%C3%9A").join('Ú');
        message = message.split("%C3%91").join('Ñ');
        return message;
    }
}

var message = new ShowMessage();

