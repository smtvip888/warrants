$(document).ready(function() {

	$('.globalnav >li').hover(

	function() {
		$(this).addClass("active");
		var current_li = $(this);
		if(!$(current_li).children('.dropdown').is(':visible')&& $('.dropdown').find('a').length>0) {
			$(current_li).find('.dropdown').slideDown(200);
		}

	}, function() {

		$(this).find('.dropdown').slideUp(100);
		$(this).removeClass("active");


	});
	if(li_id) {
		$("#" + li_id + " >a").addClass("active1");
	}

	$(".menu").hover(function() {
		$(this).find(".menu-bd").show();
		$(this).addClass("menu-hover");
	}, function() {
		$(this).find(".menu-bd").hide();
		$(this).removeClass("menu-hover");
	});
})

function checkImg(img) {
	if ($(img).attr("width") == 64){
        $(img).removeAttr("width")
        $(img).removeAttr("height")
	}else {
		$(img).attr("width", 64);
        $(img).attr("height", 64);
	}
}
