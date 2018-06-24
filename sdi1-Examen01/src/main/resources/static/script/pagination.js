"use strict";

class Pagination {
	constructor(limit) {
		var page = this.getURLParameter('page');
		if (page == undefined) {
			page = 0;
		}
		var searchText = this.getURLParameter('searchText');
		if (searchText == undefined)
			searchText = "";
		else
			searchText = "&searchText=" + searchText;
		this.changeHref(limit, page, searchText);
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
		$("#first").attr("href", "?page=0" + searchText);
		$("#before").attr("href", "?page=" + (parseInt(page) - 1) + searchText);
		$("#now").attr("href", "?page=" + (parseInt(page)) + searchText);
		$("#next").attr("href", "?page=" + (parseInt(page) + 1) + searchText);
		$("#last").attr("href", "?page=" + limit + searchText);
	}
}