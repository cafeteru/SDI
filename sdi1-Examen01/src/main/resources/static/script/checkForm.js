"use strict";

class Check {
    constructor() {}

    checkPasswords() {
        var password = $('input[name=password]').val();
        var repassword = $('input[name=passwordConfirm]').val();
        if (password !== repassword) {
            alert("Error: las contraseñas no coinciden");
            return false;
        }
        return true;
    }

    checkInputs() {
        $('input[type=submit]').prop('disabled', false);
        var inputs = $('input');
        for (var i = 0; i < inputs.length; i++) {
            var value = inputs[i].value;
            if (inputs[i].value === "") {
                $('input[type=submit]').prop('disabled', true);
                break;
            }
        }
    }
}

var check = new Check();