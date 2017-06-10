$(function(){
	var cpnames = ['tcqxc', 'fcsd', 'tcp3', 'tcp5', 'fcssq', 'tcdlt', 'fcqlc'];
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
//	console.log(defaultCpdata);
	initlist(defaultCpdata);
	function initlist(list) {

		Zepto('#cplist').html('');
		$.each(list, function(key) {
			var item = list[key];
			//渲染球
			var strkjh=strKjh(item.opencode);
			var str='<a href="fc3dkjgg.html" class="ticket ub ub-ac" data-cpname="'+item.name+'">'+
				'<div class="ub-f1 mr-10">'+
					'<h6 class="name">'+item.cname+'<time class="cpqi"></time><time class="cpdate"></time></h6>'+
					'<div class="ub ub-ac">'+strkjh+
					'</div></div><i class="iconfont icon-arrow"></i></a>';
			
			$('#cplist').append(str);
			
			
		});
	};
	//格式化开奖号
	function formatekjh (kjh) {
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
		var strkjh='';
		if(kjharr[0]) {
			kjharr[0].map(function(item, index) {
				strkjh+='<span class="ball red"></span>';
			})
		}
		if(kjharr[1]) {
			kjharr[1].map(function(item, index) {
				strkjh+='<span class="ball blue"></span>';
			})
		}
		strkjh='<div class="ballBox">'+strkjh+'</div>';
		if(kjharr[0].length<=3){
			strkjh+='<div class="ub-f1"><span class="t_info shortCpqi"></span><span class="t_info">试机号:<b class="cpsjh"></b></span></div>'
		}
		
		return strkjh;
	}
	function formateDate (datestr) {
		var weekday=['周日','周一','周二','周三','周四','周五','周六'];
		var cpdate=new Date(datestr);
		var y=cpdate.getFullYear();
		var m=(cpdate.getMonth()<10)?('0'+(cpdate.getMonth()+1)):cpdate.getMonth();
		var d=(cpdate.getDate()<10)?('0'+(cpdate.getDate())):cpdate.getDate();
		var wd=cpdate.getDay();
		var formatedate=(y+"-"+m+"-"+d+'('+weekday[Number(wd)]+')');
		return formatedate;
	}
	
requestHybrid({tagname:'renderCplist',param:{url:"http://192.168.0.200:9090/v1/kjh/today",type:"get"},callback:renderCplist});
	
})
var renderCplist=function(data){
	var newCpdata = {};//请求回来的数据
	data.map(function(item) {
		if($.inArray(item.name, cpnames) != -1) {
			newCpdata[item.name] = item;
		}
	});
	renderList(newCpdata);
//	console.log(newCpdata);
	function renderList (list) {
		console.log(list);
		$('#cplist').find('a.ticket').map(function(){
			var cpname=$(this).attr('data-cpname');
			$(this).find('.cpqi').html(list[cpname].qi+"期");
			$(this).find('.cpdate').html(formateDate (list[cpname].date));
			var kjharr=list[cpname].opencode.split(/[,|+]/);
			$(this).find('.ballBox').find('.ball').map(function(index){
				$(this).html(kjharr[index]);
			})
		})
	}
	
	
}
