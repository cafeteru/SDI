"use strict";

class Pagination {

    constructor(pgCurrent, pgLast) {
        var searchText = this.getURLParameter('searchText');
        if (searchText == undefined)
            searchText = "";
        else
            searchText = "&searchText=" + searchText;
        this.changeHref(pgLast, pgCurrent, searchText);
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

    changeHref(limit, page, searchText) {
        $("#first").attr("href", "?pg=1" + searchText);
        $("#before").attr("href", "?pg=" + (parseInt(page) - 1) + searchText);
        $("#now").attr("href", "?pg=" + (parseInt(page)) + searchText);
        $("#next").attr("href", "?pg=" + (parseInt(page) + 1) + searchText);
        $("#last").attr("href", "?pg=" + limit + searchText);
    }
}