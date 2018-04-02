"use strict";

class ShowMessageError {
    constructor() {
        this.showErrors("email");
        this.showErrors("name");
        this.showErrors("surName");
        this.showErrors("password");
        this.showErrors("passwordConfirm");
        this.showErrors("coincidence");
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

    showErrors(name) {
        var message = this.getURLParameter(name);
        message =  message.split("%20").join(' ');
        message =  message.split("%C3%AD").join('í');
        message =  message.split("%C3%B1").join('ñ');
        if (message != "") {
            var error = "<div class=\"alert alert-dismissible alert-danger\">\n" +
                "           <button type=\"button\" class=\"close\" data-dismiss=\"alert\">&times;</button>\n" +
                "                 <strong>" + message + "</strong>\n" +
                "            </div>";
            if(name === "coincidence")
                name = "passwordConfirm";
            $("#" + name).append(error);
        }
    }
}

var errors = new ShowMessageError();

