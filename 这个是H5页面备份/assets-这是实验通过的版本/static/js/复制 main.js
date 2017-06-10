window.Hybrid=window.Hybrid || {};
//window.Hybrid={}

//Hybrid基本逻辑
Hybrid.callback = function(data) {
	console.log('-hybrid_Hybrid.callback响应成功_')
        var callbackId = data.callback;
        if (!callbackId) return;
        if (typeof data == 'string') data = JSON.parse(data);
		 Hybrid[callbackId] && Hybrid[callbackId](data.data || {}, data);
        return true;
    };
var bridgePostMsg = function(params) {
	var url = _getHybridUrl(params);

	//兼容ios6
	var ifr = $('<iframe style="display: none;" src="' + url + '"/>');

	console.log(params.tagname + '-hybrid请求发出-bridgePostMsg' + new Date().getTime() + 'url: ' + url)

	$('body').append(ifr);
	
	setTimeout(function() {
		ifr.remove();
		ifr = null;
	}, 1000);

	return;
};

var _getHybridUrl = function(params) {
	var k, paramStr = '',
		url = 'medmedlinkerhybrid://',
		flag = '?';
	url += params.tagname; //时间戳，防止url不起效

	if(params.callback) {
		flag = '&';
		url += '?callback=' + params.callback;
		//delete params.callback;
	}
	if(params.param) {
		paramStr = typeof params.param == 'object' ? JSON.stringify(params.param) : params.param;
		url += flag + 'param=' + encodeURIComponent(paramStr);
	}
	return url;
};
var requestHybrid = function(params) {
	if(!params.tagname) {
		alert('必须包含tagname');
	}
	//生成唯一执行函数，执行后销毁
	var tt = (new Date().getTime()) + '_' + '_';
	var t = 'hybrid_' + params.tagname + '_' + tt;
	var tmpFn;

	////针对组件通信做的特殊处理
	//if(params.param && params.param.events) {
	//    params.param.events =  _handleMessage(params.param.events, params.tagname);
	//}

	//处理有回调的情况
	if(params.callback) {
		tmpFn = params.callback;
		params.callback = t;

		window.Hybrid[t] = function(data) {

			console.log(params.tagname + '-hybrid请求响应-requestHybrid' + new Date().getTime())

			tmpFn(data);
			//delete window.Hybrid[t];
		}
	}

	bridgePostMsg(params);
};


