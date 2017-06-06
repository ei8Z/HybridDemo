$(function(){
	//	初始化开奖详情
	var cpname=getUrlOptions ('name');
	//初始化球格式
	$('.ballinfo').html(strKjh(defaultCpdata[cpname].opencode));
	$('.cptitle').text(getUrlOptions ('title')+"开奖公告");
	requestHybrid({tagname:'get',param:{url:'http://192.168.0.200:9090/v1/kjh/one?name='+cpname},callback:renderDetail});
	
	/*$.ajax({
		url:'http://192.168.0.200:9090/v1/kjh/one?name='+cpname,
		type:"get",
		data:{},
		success:function(data){
			renderDetail (data);
		}
	});*/
//	初始化最近30期
	defaultLimit30 (cpname);
	requestHybrid({tagname:'get',param:{url:"http://192.168.0.200:9090/v1/kjh/limit?name="+cpname+"&limit=30"},callback:renderLimit30});
	
	/*$.ajax({
		type:"get",
		url:"http://192.168.0.200:9090/v1/kjh/limit?name="+cpname+"&limit=30",
		async:true,
		success:function(data){
			renderLimit30 (data);
		}
	});*/
	
})
var defaultCpdata = {
		'tcqxc': {
			"name": "tcqxc",
			"cname": "体彩七星彩",
			"opencode": "1,7,7,6,3,4,5",
		},
		'fcsd': {
			"name": "fcsd",
			"cname": "福彩3D",
			"opencode": "6,9,5",
		},
		'tcp3': {
			"name": "tcp3",
			"cname": "体彩P3",
			"opencode": "3,6,0",
		},
		'tcp5': {
			"name": "tcp5",
			"cname": "体彩P5",
			"opencode": "3,6,0,0,2",
		},
		'fcssq': {
			"name": "fcssq",
			"cname": "福彩双色球",
			"opencode": "05,10,13,24,26,31+04",
		},
		'tcdlt': {
			"name": "tcdlt",
			"cname": "体彩大乐透",
			"opencode": "08,11,13,15,17+03,10",
		},
		'fcqlc': {
			"name": "fcqlc",
			"cname": "福彩七乐彩",
			"opencode": "03,06,07,09,17,22,29+13",
		}
	};
		//格式化开奖号
	function formatekjh(kjh) {
		var kjharr = {};
		if(kjh != '') {
			var test = kjh.split('+');
			test.map(function(item, index) {
				kjharr[index] = item.split(',');
			})
		}
		return kjharr;
	}
	//初始化开奖号球
	function strKjh(kjh) {
		var kjharr = formatekjh(kjh);
		var strkjh = '';
		if(kjharr[0]) {
			kjharr[0].map(function(item, index) {
				strkjh += '<span class="ball red"></span>';
			})
		}
		if(kjharr[1]) {
			kjharr[1].map(function(item, index) {
				strkjh += '<span class="ball blue"></span>';
			})
		}
		strkjh = '<div class="ballBox ub-f1">' + strkjh + '</div>';
		if(kjharr[0].length <= 3) {
			strkjh += '<span class="t_info"><b class="cpqishort"></b>开机号:<b class="cpkjh"></b></span>'
		}

		return strkjh;
	}
//初始化最近30期
function defaultLimit30 (cpname) {
	$('.limit30').html('');
	var ballstr=strKjh(defaultCpdata[cpname].opencode);
	var liststr='<a href="#" class="ticket ub ub-ac">'+
					'<div class="ub-f1 mr-10">'+
						'<h6 class="name"><time class="l-0 cpqi">xx期</time><time class="cpdate">开奖日期</time></h6>'+
						'<div class="ub ub-ac">'
						+ballstr+
						'</div></div><i class="iconfont icon-arrow"></i></a>';
	for(var i=0;i<30;i++){
		$('.limit30').append(liststr);
	}
}
//最近30期填入数据
function renderLimit30 (data) {
	var obj = (typeof(data)=='string')?JSON.parse(data):data;
	console.log('-hybrid请求响应-renderLimit30回调方法获取到数据为：'+data)
	if(obj.err==0){
		var list=obj.data;
		$('.limit30').find('.ticket').map(function(i,item){

			$(this).find('.cpqi').text(list[i].qi+'期');
			$(this).find('.cpdate').text(formateDate(list[i].date));
			var kjharr = list[i].opencode.split(/[,|+]/);
			$(this).find('.ballBox').find('.ball').map(function(index) {
				$(this).html(kjharr[index]);
			});
			$(this).find('.cpkjh').text(list[i].sjh);
		})
		
	}
}
//格式化日期
function formateDate(datestr) {
	var weekday = ['周日', '周一', '周二', '周三', '周四', '周五', '周六'];
	var cpdate = new Date(datestr);
	var y = cpdate.getFullYear();
	var m = (cpdate.getMonth() < 10) ? ('0' + (cpdate.getMonth() + 1)) : cpdate.getMonth();
	var d = (cpdate.getDate() < 10) ? ('0' + (cpdate.getDate())) : cpdate.getDate();
	var wd = cpdate.getDay();
	var formatedate = (y + "-" + m + "-" + d + '(' + weekday[Number(wd)] + ')');
	return formatedate;
}
function formatNum (str) {
	var interval=3,punctuation=",";
	var newstr='';
	if(typeof(str)=='string'){
		str.split('').reverse().map(function(item,i){
			if(i%interval==0){
				newstr=item+punctuation+newstr;
			}else{
				newstr=item+newstr;
			}
		})
	}
	return newstr.substr(0,newstr.length-1);
}
function getUrlOptions (name) {
	var options={};
	var optionStr=decodeURIComponent(window.location.search).substr(1).split('&');
	optionStr.map(function(item){
		var key=item.split('=')[0];
		var value=item.split('=')[1];
		options[key]=value;
	});
	if(options[name]){
		
		return options[name];
	}
}
function renderDetail (data) {
//	var data=(data)?data: '{"err":0,"msg":"","stime":1496457061,"data":{"id":15216,"qi":2017136,"n1":4,"n2":8,"n3":9,"opencode":"4,8,9,3,4+6,8,8","hz":21,"hw":1,"xt":6,"kd":5,"zj1":"","zj2":"","zj3":"","week":0,"date":"2017-05-23T00:00:00+08:00","tzmoney":"","sjh":291,"kjih":358}}';
	var obj = (typeof(data)=='string')?JSON.parse(data):data;
	console.log('-hybrid请求响应-renderDetail回调方法获取到数据为：'+data)
	if(obj.err==0){
		var cpinfo=obj.data;
		$('.pagemain .cpqi').text(cpinfo.qi+"期开奖结果");
		$('.pagemain .cpdate').text(formateDate(cpinfo.date));
		var kjharr = cpinfo.opencode.split(/[,|+]/);
		$('.pagemain .ballBox').find('.ball').map(function(index) {
			$(this).html(kjharr[index]);
		})
		
		$('.pagemain .cpkjh').text(cpinfo.sjh);
		
		$('.pagemain .cpsail').text("本期销量："+formatNum(cpinfo.tzmoney));
//		console.log(date)
	}
}