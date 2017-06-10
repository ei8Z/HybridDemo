
$(function() {
	FastClick.attach(document.body);  
	var params = {
		url:"http://192.168.0.200:9090/v1/kjh/today",
		type:"get",
		isdebug:true,
		data:{},
		callback: GetData,
		render:renderCplist,
	}
	//调用获取数据方法
	requestHybrid(params);
    //根据参数获取数据并调用渲染页面方法
	function requestHybrid(paramas) {
		console.log(paramas);
		
		
		
		$.ajax({
			type:paramas.type,
			url:GetUrl(paramas),
			data:params.data,
			async:true,
			success:function(data){
				tempFunc = paramas.callback;
				tempFunc(data,paramas);
				//GetDate(data,paramas);
			}
		});

	}
});


function GetUrl(paramas){
	var url="";
	if(paramas.isdebug)
		url = paramas.url;
	else
		url ="asdfdfdf://"+paramas.url + paramas.type+paramas.callback

	return url;
}

function GetData(data,paramas){
	console.log(data);
	if(data.err == 0) {
		//调用参数中传过来的回调函数
		//paramas: renderCplist(data.data);
		paramas.render(data.data);
	}
	
}
