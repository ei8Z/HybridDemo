window.Hybrid=window.Hybrid || {};
//window.Hybrid={}

//Hybrid基本逻辑
//这个方法是统一的回调接口，所有native回调都通过这个接口
Hybrid.callback = function(data) {
	console.log('-hybrid_Hybrid.callback响应成功_'+data.data)
        var callbackId = data.callback;
        if (!callbackId) return;

        if (typeof data == 'string') 
        data = JSON.parse(data);

		Hybrid[callbackId] && Hybrid[callbackId](data.data || {}, data);//拿到id，直接执行
        return true;
    };
var bridgePostMsg = function(params) {
	var url = _getHybridUrl(params);

	//兼容ios6
	var ifr = $('<iframe style="display: none;" src="' + url + '"/>');

	console.log(params.tagname + '-hybrid请求发出-bridgePostMsg' + new Date().getTime() + 'url: ' + url)

	$('body').append(ifr);//发出
	
	setTimeout(function() {
	//延迟一秒再发出，避免处理不过来
		ifr.remove();
		ifr = null;
	}, 1000);
	return;
};

var _getHybridUrl = function(params) {
//地址修改，便于本地拦截获取数据
	var k, paramStr = '',
		url = 'medmedlinkerhybrid://',
		flag = '?';
	url += params.tagname; //时间戳，防止url不起效

	if(params.callback) {
		flag = '&';
		url += '?callback=' + params.callback;
		console.log("处理请求地址准备发出：当前回调的CallbackId为：" + params.callback);
		//delete params.callback;
	}
	if(params.param) {
	//参数转为json
		paramStr = typeof params.param == 'object' ? JSON.stringify(params.param) : params.param;
		url += flag + 'param=' + encodeURIComponent(paramStr);
	}
	return url;
};
var requestHybrid = function(params) {
console.log(params.tagname + '-hybrid请求响应-requestHybrid开始执行'+params.callback)
	if(!params.tagname) {
		alert('必须包含tagname');
	}
	//生成唯一执行函数，执行后销毁，生成执行id？
//	var tt = (new Date().getTime()) + "_" + "_";
	var tt = params.callbackId+ '_' + '_';
	var t = "hybrid_" + params.tagname + '_' + tt;
	var tmpFn;

	////针对组件通信做的特殊处理
	//if(params.param && params.param.events) {
	//    params.param.events =  _handleMessage(params.param.events, params.tagname);
	//}
	//处理有回调的情况
	if(params.callback) {
		tmpFn = params.callback;
		params.callback = t;
        console.log(params.tagname + '-hybrid请求响应-requestHybrid' + new Date().getTime() +"当前的callbackID为：" +t +"   当前回调的方法是：" + tmpFn)
		window.Hybrid[t] = function(data) {

			console.log(params.tagname + '-hybrid请求响应-requestHybrid' + new Date().getTime() + "回调的数据为：" + data)

			tmpFn(data);
			//delete window.Hybrid[t];
		}
	}
	bridgePostMsg(params);
};


