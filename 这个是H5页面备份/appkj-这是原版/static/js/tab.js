function tabs(params) {

	var options = {
		ctrlClass: '.tab',
		tabClass: '.tabContainer',
		lineClass: '.line',
		storage: null
	};
	options = $.extend(options, params);
	var ctrl = $(options.ctrlClass);
	var tab = $(options.tabClass);
	if (options.storage != null) {
		if (localStorage[options.storage]) {
			ctrl.find('.ub-f1').eq(localStorage[options.storage]).addClass('active').siblings().removeClass('active');
		} else {
			ctrl.find('.ub-f1').eq(0).addClass('active').siblings().removeClass('active');
		}
	}


	//初始化线位置
	resetLine(options);
	$$(options.ctrlClass).find('.ub-f1').tap(function() {
		$$(this).addClass('active').siblings().removeClass('active');
		if (options.storage != null) {
			localStorage[options.storage] = ctrl.find('.active').index();
			// console.log(localStorage[options.storage]);
		}

		resetLine(options);
	});

}

function resetLine(params) {

	var options = {
		ctrlClass: '.tab',
		tabClass: '.tabContainer',
		lineClass: '.line',
		storage: null
	};
	options = $.extend(options, params);
	// console.log(options);
	var speed = 200;
	var ctrl = $(options.ctrlClass);
	var tab = $(options.tabClass);
	var line = ctrl.find(options.lineClass)
	var activeCtrl = ctrl.find('.active');
	line.animate({
		'width': activeCtrl.find('span').outerWidth(),
		'left': activeCtrl.find('span').offset().left
	}, speed);
	var index = activeCtrl.index();
	tab.find('.tab').eq(index).addClass('active').siblings().removeClass('active');
}

$(function() {
	$$('.cplike').tap(function() {
		if ($(this).find('.iconfont').hasClass('icon-and')) {
			$(this).html('<p class="cplike"><i class="iconfont icon-check mr-5"></i>已关注</p>')
		}
	})
})