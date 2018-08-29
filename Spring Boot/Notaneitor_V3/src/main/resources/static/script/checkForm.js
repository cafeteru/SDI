"use strict";

class Check {
    constructor() {}

    checkAll() {
        var checks = [];
        checks.push(this.checkDni());
        checks.push(this.checkPasswords());
        for (var i = 0; i < inputs.length; i++) {
            if (!checks[i]) {
                return false;
            }
        }
        return true;
    }

    checkDni() {
        var dni = $('input[name=username]').val();
        if (dni === undefined) {
            dni = $('input[name=dni]').val();
        }
        if (this.validate(dni))
            return true;
        return false;
    }

    validate(dni) {
        var numero, letr, letra, expresion_regular_dni;
        expresion_regular_dni = /^\d{8}[a-zA-Z]$/;

        if (expresion_regular_dni.test(dni) == true) {
            numero = dni.substr(0, dni.length - 1);
            letr = dni.substr(dni.length - 1, 1);
            numero = numero % 23;
            letra = 'TRWAGMYFPDXBNJZSQVHLCKET';
            letra = letra.substring(numero, numero + 1);
            if (letra != letr.toUpperCase()) {
                alert('Dni erroneo, la letra del NIF no se corresponde');
            } 
        } else {
            alert('Dni erroneo, formato no válido');
        }
    }

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
        $('button').prop('disabled', false);
        var inputs = $('input');
        for (var i = 0; i < inputs.length; i++) {
            var value = inputs[i].value;
            if (inputs[i].value === "") {
                $('button').prop('disabled', true);
                break;
            }
        }
    }
}

var check = new Check();