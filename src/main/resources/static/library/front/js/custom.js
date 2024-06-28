/*
Template: Streamit - Responsive Bootstrap 4 Template
Author: iqonicthemes.in
Design and Developed by: iqonicthemes.in
NOTE: This file contains the styling for responsive Template.
*/

/*----------------------------------------------
Index Of Script
------------------------------------------------

:: Sticky Header Animation & Height
:: Back to Top
:: Header Menu Dropdown
:: Slick Slider
:: Owl Carousel
:: Page Loader
:: Mobile Menu Overlay
:: Equal Height of Tab Pane
:: Active Class for Pricing Table
:: Select 2 Dropdown
:: Video Popup
:: Flatpicker
:: Custom File Uploader

------------------------------------------------
Index Of Script
----------------------------------------------*/

(function (jQuery) {
	"use strict";
	jQuery(document).ready(function() {

		function activaTab(pill) {
			jQuery(pill).addClass('active show');
		}

		/*---------------------------------------------------------------------
			Sticky Header Animation & Height
		----------------------------------------------------------------------- */
		function headerHeight() {
			var height = jQuery("#main-header").height();
			jQuery('.iq-height').css('height', height + 'px');
		}
		jQuery(function() {
			var header = jQuery("#main-header"),
				yOffset = 0,
				triggerPoint = 80;

			headerHeight();

			jQuery(window).resize(headerHeight);
			jQuery(window).on('scroll', function() {

				yOffset = jQuery(window).scrollTop();

				if (yOffset >= triggerPoint) {
					header.addClass("menu-sticky animated slideInDown");
				} else {
					header.removeClass("menu-sticky animated slideInDown");
				}

			});
		});
		
		/*---------------------------------------------------------------------
			Back to Top
		---------------------------------------------------------------------*/
		var btn = $('#back-to-top');
		$(window).scroll(function () {
			if ($(window).scrollTop() > 50) {
				btn.addClass('show');
			} else {
				btn.removeClass('show');
			}
		});
		btn.on('click', function (e) {
			e.preventDefault();
			$('html, body').animate({ scrollTop: 0 }, '300');
		});

		/*---------------------------------------------------------------------
			Header Menu Dropdown
		---------------------------------------------------------------------*/
		jQuery('[data-toggle=more-toggle]').on('click', function() {
			jQuery(this).next().toggleClass('show');
		});

		jQuery(document).on('click', function(e) {
			let myTargetElement = e.target;
			let selector, mainElement;
			if (jQuery(myTargetElement).hasClass('search-toggle') || jQuery(myTargetElement).parent().hasClass('search-toggle') || jQuery(myTargetElement).parent().parent().hasClass('search-toggle')) {
				if (jQuery(myTargetElement).hasClass('search-toggle')) {
					selector = jQuery(myTargetElement).parent();
					mainElement = jQuery(myTargetElement);
				} else if (jQuery(myTargetElement).parent().hasClass('search-toggle')) {
					selector = jQuery(myTargetElement).parent().parent();
					mainElement = jQuery(myTargetElement).parent();
				} else if (jQuery(myTargetElement).parent().parent().hasClass('search-toggle')) {
					selector = jQuery(myTargetElement).parent().parent().parent();
					mainElement = jQuery(myTargetElement).parent().parent();
				}
				if (!mainElement.hasClass('active') && jQuery(".navbar-list li").find('.active')) {
					jQuery('.navbar-right li').removeClass('iq-show');
					jQuery('.navbar-right li .search-toggle').removeClass('active');
				}

				selector.toggleClass('iq-show');
				mainElement.toggleClass('active');

				e.preventDefault();
			} else if (jQuery(myTargetElement).is('.search-input')) {} else {
				jQuery('.navbar-right li').removeClass('iq-show');
				jQuery('.navbar-right li .search-toggle').removeClass('active');
			}
		});

		/*---------------------------------------------------------------------
			Slick Slider
		----------------------------------------------------------------------- */
		$('#home-slider').slick({
			autoplay: false,
			speed: 800,
			lazyLoad: 'progressive',
			arrows: true,
			dots: false,
			prevArrow: '<div class="slick-nav prev-arrow"><i></i><svg><use xlink:href="#circle"></svg></div>',
			nextArrow: '<div class="slick-nav next-arrow"><i></i><svg><use xlink:href="#circle"></svg></div>',
			responsive: [
				{
					breakpoint: 992,
					settings: {
						dots: true,
						arrows: false,
					}
				}
			]
		}).slickAnimation();
		$('.slick-nav').on('click touch', function (e) {

			e.preventDefault();

			var arrow = $(this);

			if (!arrow.hasClass('animate')) {
				arrow.addClass('animate');
				setTimeout(() => {
					arrow.removeClass('animate');
				}, 1600);
			}

		});
		jQuery('.favorites-slider').slick({
			dots: false,
			arrows: true,
			infinite: true,
			speed: 300,
			autoplay: false,
			slidesToShow: 4,
			slidesToScroll: 1,		
			// appendArrows: $('#sm-slick-arrow'),
			
			nextArrow: '<a href="#" class="slick-arrow slick-next"><i class= "fa fa-chevron-right"></i></a>',
			prevArrow: '<a href="#" class="slick-arrow slick-prev"><i class= "fa fa-chevron-left"></i></a>',
			responsive: [
			{
				breakpoint: 1200,
				settings: {
				slidesToShow: 3,
				slidesToScroll: 1,
				infinite: true,
				dots: true
				}
			},
			{
				breakpoint: 768,
				settings: {
				slidesToShow: 2,
				slidesToScroll: 1
				}
			},
			{
				breakpoint: 480,
				settings: {
				// arrows: false,
				slidesToShow: 1,
				slidesToScroll: 1
				}
			}
			]
		});

		jQuery('#top-ten-slider').slick({
			slidesToShow: 1,
			slidesToScroll: 1,
			arrows: false,
			fade: true,
			asNavFor: '#top-ten-slider-nav',
			responsive: [
			{
				breakpoint: 992,
				settings: {
				asNavFor: false,
				arrows: true,
				nextArrow: '<button class="NextArrow"><i class="ri-arrow-right-s-line"></i></button>',
				prevArrow: '<button class="PreArrow"><i class="ri-arrow-left-s-line"></i></button>',
				}
			}
			]
		});
		jQuery('#top-ten-slider-nav').slick({
			slidesToShow: 3,
			slidesToScroll: 1,
			asNavFor: '#top-ten-slider',
			dots: false,
			arrows: true,
			infinite: true,
			vertical:true,
			verticalSwiping: true,
			centerMode: false,
			nextArrow:'<button class="NextArrow"><i class="ri-arrow-down-s-line"></i></button>',
			prevArrow:'<button class="PreArrow"><i class="ri-arrow-up-s-line"></i></button>',
			focusOnSelect: true,
			responsive: [		    
				{
				breakpoint: 1200,
				settings: {
					slidesToShow: 2,
				}
				},
				{
					breakpoint: 600,
					settings: {
						asNavFor: false,
					}
				},
			]
		});

		jQuery('#episodes-slider2').slick({
			dots: false,
			arrows: true,
			infinite: true,
			speed: 300,
			autoplay: false,
			slidesToShow: 4,
			slidesToScroll: 1,
			responsive: [
			{
				breakpoint: 1024,
				settings: {
				slidesToShow: 3,
				slidesToScroll: 1,
				infinite: true,
				dots: true,
				}
			},
			{
				breakpoint: 600,
				settings: {
				slidesToShow: 2,
				slidesToScroll: 1,
				}
			},
			{
				breakpoint: 480,
				settings: {
				slidesToShow: 1,
				slidesToScroll: 1,
				}
			}
			]
		});

		jQuery('#episodes-slider3').slick({
			dots: false,
			arrows: true,
			infinite: true,
			speed: 300,
			autoplay: false,
			slidesToShow: 4,
			slidesToScroll: 1,
			responsive: [
			{
				breakpoint: 1024,
				settings: {
				slidesToShow: 3,
				slidesToScroll: 1,
				infinite: true,
				dots: true,
				}
			},
			{
				breakpoint: 600,
				settings: {
				slidesToShow: 2,
				slidesToScroll: 1,
				}
			},
			{
				breakpoint: 480,
				settings: {
				slidesToShow: 1,
				slidesToScroll: 1,
				}
			}
			]
		});

		jQuery('#trending-slider').slick({
			slidesToShow: 1,
			slidesToScroll: 1,		 
			arrows: false,
			fade: true,
			draggable:false,
			asNavFor: '#trending-slider-nav',	
		});
		jQuery('#trending-slider-nav').slick({
			slidesToShow: 5,
			slidesToScroll: 1,
			asNavFor: '#trending-slider',
			dots: false,
			arrows: true,
			nextArrow: '<a href="#" class="slick-arrow slick-next"><i class= "fa fa-chevron-right"></i></a>',
			prevArrow: '<a href="#" class="slick-arrow slick-prev"><i class= "fa fa-chevron-left"></i></a>',
			infinite: true,
			centerMode: true,
			centerPadding:0,
			focusOnSelect: true,
			responsive: [
			{
				breakpoint: 1024,
				settings: {
				slidesToShow: 2,
				slidesToScroll: 1,
				}
			},
			{
				breakpoint: 600,
				settings: {
				slidesToShow: 1,
				slidesToScroll: 1
				}
			}
			]
		});
		
		jQuery('#tvshows-slider').slick({
			centerMode: true,
			centerPadding: '200px',
			slidesToShow: 1,
			nextArrow: '<button class="NextArrow"><i class="ri-arrow-right-s-line"></i></button>',
			prevArrow: '<button class="PreArrow"><i class="ri-arrow-left-s-line"></i></button>',
			arrows:true,
			dots:false,
			responsive: [
				{
					breakpoint: 991,
					settings: {
						arrows: false,
						centerMode: true,
						centerPadding: '20px',
						slidesToShow: 1
					}
				},
				{
					breakpoint: 480,
					settings: {
						arrows: false,
						centerMode: true,
						centerPadding: '20px',
						slidesToShow: 1
					}
				}
			]
		});

		/*---------------------------------------------------------------------
			Owl Carousel
		----------------------------------------------------------------------- */
		jQuery('.episodes-slider1').owlCarousel({
			loop:true,
			margin:20,
			nav:true,
			navText: ["<i class='ri-arrow-left-s-line'></i>", "<i class='ri-arrow-right-s-line'></i>"],
			dots:false,
			responsive:{
				0:{
					items:1
				},
				600:{
					items:1
				},
				1000:{
					items:4
				}
			}
		});
		
		/*---------------------------------------------------------------------
			Page Loader
		----------------------------------------------------------------------- */
		jQuery("#load").fadeOut();
		jQuery("#loading").delay(0).fadeOut("slow");
		
		jQuery('.widget .fa.fa-angle-down, #main .fa.fa-angle-down').on('click', function () {
			jQuery(this).next('.children, .sub-menu').slideToggle();
		});

		/*---------------------------------------------------------------------
		Mobile Menu Overlay
		----------------------------------------------------------------------- */
		jQuery(document).on("click", function(event){
	    var $trigger = jQuery(".main-header .navbar");
	    if($trigger !== event.target && !$trigger.has(event.target).length){
			jQuery(".main-header .navbar-collapse").collapse('hide');
			jQuery('body').removeClass('nav-open');
	    }            
		});
		jQuery('.c-toggler').on("click", function(){
			jQuery('body').addClass('nav-open');
		}); 

		/*---------------------------------------------------------------------
		  Equal Height of Tab Pane
		-----------------------------------------------------------------------*/		
		jQuery('.trending-content').each(function () {			
			var highestBox = 0;			
			jQuery('.tab-pane', this).each(function () {				
				if (jQuery(this).height() > highestBox) {
					highestBox = jQuery(this).height();
				}
			});			 
			jQuery('.tab-pane', this).height(highestBox);
		}); 

		/*---------------------------------------------------------------------
	 		Active Class for Pricing Table
  	 	-----------------------------------------------------------------------*/
		jQuery("#my-table tr th").on("click", function (){
			jQuery('#my-table tr th').children().removeClass('active');
			jQuery(this).children().addClass('active');
			jQuery("#my-table td").each(function () {
				if (jQuery(this).hasClass('active')) {
					jQuery(this).removeClass('active')
				}
			});
			var col = jQuery(this).index();
			jQuery("#my-table tr td:nth-child(" + parseInt(col + 1) + ")").addClass('active');
		});
		
		/*---------------------------------------------------------------------
			Select 2 Dropdown
		-----------------------------------------------------------------------*/
		if (jQuery('select').hasClass('season-select')){
			jQuery('select').select2({
				theme: 'bootstrap4',
				allowClear: false,
				width: 'resolve'
			});
		}
		if (jQuery('select').hasClass('pro-dropdown')) {			
			jQuery('.pro-dropdown').select2({
				theme: 'bootstrap4',			
				minimumResultsForSearch: Infinity,			
				width: 'resolve'
			});	
			jQuery('#lang').select2({
				theme: 'bootstrap4',
				placeholder: 'Language Preference',
				allowClear: true,
				width: 'resolve'
			});
		}

		/*---------------------------------------------------------------------
			Video popup
		-----------------------------------------------------------------------*/
		jQuery('.video-open').magnificPopup({
			type: 'iframe',
			mainClass: 'mfp-fade',
			removalDelay: 160,
			preloader: false,
			fixedContentPos: false,
			iframe: {
				markup: '<div class="mfp-iframe-scaler">' +
					'<div class="mfp-close"></div>' +
					'<iframe class="mfp-iframe" frameborder="0" allowfullscreen></iframe>' +
					'</div>',

				srcAction: 'iframe_src',
			}
		});

		/*---------------------------------------------------------------------
			Flatpicker
		-----------------------------------------------------------------------*/
		if (jQuery('.date-input').hasClass('basicFlatpickr')) {
			jQuery('.basicFlatpickr').flatpickr();
		}
		/*---------------------------------------------------------------------
			Custom File Uploader
		-----------------------------------------------------------------------*/
		jQuery(".file-upload").on("change", function () {
			! function (e) {
				if (e.files && e.files[0]) {
					var t = new FileReader;
					t.onload = function (e) {
						jQuery(".profile-pic").attr("src", e.target.result)
					}, t.readAsDataURL(e.files[0])
				}
			}(this)
		}), jQuery(".upload-button").on("click", function () {
			jQuery(".file-upload").click();
		});
		// new WOW().init();
		// var swiper = new Swiper('.swiper-container', {
		// 	effect: 'fade',
		// 	grabCursor: true,
		// 	centeredSlides: false,
		// 	slidesPerView: 'auto',
		// 	freeMode: true,
		// 	loop: true,
		// 	parallax: true,
		// 	on: {
		// 		slideChangeTransitionEnd: function () {
		// 			$('.aos-slide').show(0);
		// 			AOS.init();
		// 		},
		// 		slideChangeTransitionStart: function () {
		// 			$('.aos-slide').hide(0);
		// 			$('.aos-slide').removeClass('aos-init').removeClass('aos-animate');
					
		// 		},
		// 	},
		// 	pagination: {
		// 		el: '.swiper-pagination',
		// 	},
		// 	navigation: {
		// 		nextEl: '.swiper-button-next',
		// 		prevEl: '.swiper-button-prev',
		// 	},
		// });
	
		// AOS.init();	

	});
})(jQuery);

var s = true;
function contentOpen(a){
	$('#faq-content'+a).slideToggle();
	if(s){
		$.ajax({
		type: 'Get',
		url: '/noticeClick',
		async:false,
		data: {
			id: a,
		},
		success: function(result) { // success
			if(result == 'P'){
				var click = $('#faqc'+a+' span.clicks').text()*1;
				$('#faqc'+a+' span.clicks').text(click+1);	
			}
		}, error: function(textStatus) {
			if (textStatus == "timeout") {
				alert("시간이 초과되어 데이터를 수신하지 못하였습니다.");
			} else {
				alert("데이터 전송에 실패했습니다. 다시 시도해 주세요");
			}
		} 
	});
		s=!s;
	}else{
		s=!s;
	}
}

function ready01(){
	var a = confirm('찬우박 사이트를 이용 해 주세요');
	if(a){
		location.href="http://xn--bh3bz7qlme.com/";
	}
}

function memberRegister(){
	window.open('/certification/apply','_blank',
							'toobar=no, menubar=yes, scrollbars=yes, resizable=yes, width=300, height=200');
}
function buyVideo(videoId){
	$.ajax({
		type: 'Get',
		url: '/videoCheck',
		async:false,
		data: {
			id: videoId,
		},
		success: function(result) { // success
			if(result == 'P'){
				var result = confirm('구매한 영상입니다 다시 감상하시겠습니까?')
				if(result){
					location.href="/securityVideoDetail?id="+videoId
				}
			}else{
				var result = confirm('영상 구매시 포인트가 차감됩니다 구매하시겠습니까?')
				if(result){
					location.href="/securityVideoDetail?id="+videoId
				}
			}
		}, error: function(textStatus) {
			if (textStatus == "timeout") {
				alert("시간이 초과되어 데이터를 수신하지 못하였습니다.");
			} else {
				alert("데이터 전송에 실패했습니다. 다시 시도해 주세요");
			}
		} 
	});
}

function findIdF(){
	window.open('/certification/findIdF','_blank',
							'toobar=no, menubar=yes, scrollbars=yes, resizable=yes, width=300, height=200');
}

function findPwF(){
	window.open('/certification/findPwF','_blank',
							'toobar=no, menubar=yes, scrollbars=yes, resizable=yes, width=300, height=200');	
}

function withdraw(){
	var result = confirm("계정을 비활성화 하시겠습니까?");
	if(result){
		alert('계정은 비활성화 처리되며, 1년후 삭제 됩니다. 삭제 이후에는 미사용 포인트를 사용하실 수 없습니다')
		location.href="/member/withdraw";
	}
}

function orderInsert(point){
	var result = confirm(point + "원을 충전 하시겠습니까?");
	if(result){
		alert('고객님의 연락처로 입금정보를 발송 해 드리겠습니다 감사합니다')
		location.href="/orderInsert?order_price_amount="+point;
	}
}


