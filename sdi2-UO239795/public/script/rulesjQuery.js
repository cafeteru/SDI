$(document).ready(function () {
    var token;
    var URLbase = "http://localhost:8081/api";
    $("main").load("/widgets/widget-login.html");
    agregarNavFooter();
});

function agregarNavFooter() {
    var url = window.location.href.toString();
    if (url.indexOf("login") >= 0) {
        $("nav").load("/widgets/widget-navlogin.html");
    } else {
        $("nav").load("/widgets/widget-navlogout.html");
    }
}