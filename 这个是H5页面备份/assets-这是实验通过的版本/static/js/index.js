var cpnames = ['tcqxc', 'fcsd', 'tcp3', 'tcp5', 'fcssq', 'tcdlt', 'fcqlc','fcsdsjh','tcp3sjh'];
var sjhnames=['fcsdsjh','tcp3sjh'];
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
		strkjh = '<div class="ballBox">' + strkjh + '</div>';
		if(kjharr[0].length <= 3) {
			strkjh += '<div class="ub-f1"><span class="t_info shortCpqi"></span><span class="t_info">试机号:<b class="cpsjh"></b></span></div>'
		}

		return strkjh;
	}
	//	console.log(defaultCpdata);
$(function() {

	
	initlist(defaultCpdata);

	function initlist(list) {

		Zepto('#cplist').html('');
		$.each(list, function(key) {
			var item = list[key];
			//渲染球
			var strkjh = strKjh(item.opencode);
			var str = '<a href="ggdetail.html?'+encodeURIComponent('name='+item.name+'&title='+item.cname)+'" class="ticket ub ub-ac" data-cpname="' + item.name + '">' +
				'<div class="ub-f1 mr-10">' +
				'<h6 class="name">' + item.cname + '<time class="cpqi"></time><time class="cpdate"></time></h6>' +
				'<div class="ub ub-ac">' + strkjh +
				'</div></div><i class="iconfont icon-arrow"></i></a>';

			$('#cplist').append(str);

		});
	};

//	renderCplist();

	requestHybrid({tagname:'get',param:{url:"http://192.168.0.200:9090/v1/kjh/today?name=fcsdsjh"},callbackId:"renderCplist",callback:renderCplist});

})

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
//callbackfuncname = "renderCplist";
// loadurl("javascript:"+callbackfuncname+"("+jsondata+")");

var renderCplist = function(data) {
	var data = data || '{"err":0,"msg":"","stime":1496377039,"data":[{"id":73,"czkind":1,"cztype":0,"name":"fcsdsjh","cname":"福彩3D试机号","qi":2017139,"opencode":"2,2,0","tzmoney":"42451252","nextmoney":"","date":"2017-05-26T00:00:00+08:00","updatetime":"2017-05-27T08:16:09+08:00","orderint":0},{"id":90,"czkind":2,"cztype":2,"name":"tcfj22x5","cname":"体彩福建22选5","qi":2017139,"opencode":"04,05,12,14,22","tzmoney":"133170","nextmoney":"0","date":"2017-05-26T00:00:00+08:00","updatetime":"2017-05-26T20:08:04+08:00","orderint":0},{"id":91,"czkind":1,"cztype":2,"name":"fchn22x5","cname":"福彩河南22选5","qi":2017139,"opencode":"04,08,09,11,18","tzmoney":"433922","nextmoney":"2523371","date":"2017-05-26T00:00:00+08:00","updatetime":"2017-05-26T23:02:15+08:00","orderint":0},{"id":92,"czkind":1,"cztype":2,"name":"fchb30x5","cname":"福彩湖北30选5","qi":2017139,"opencode":"05,07,09,26,28","tzmoney":"77540","nextmoney":"189765","date":"2017-05-26T00:00:00+08:00","updatetime":"2017-05-26T22:22:23+08:00","orderint":0},{"id":93,"czkind":1,"cztype":2,"name":"fcxj25x7","cname":"福彩新疆25选7","qi":2017040,"opencode":"04,06,08,11,13,15,22+16","tzmoney":"42434","nextmoney":"151680","date":"2017-05-26T00:00:00+08:00","updatetime":"2017-05-26T20:44:31+08:00","orderint":0},{"id":94,"czkind":1,"cztype":2,"name":"fcxj35x7","cname":"福彩新疆35选7","qi":2017040,"opencode":"06,10,12,16,18,20,27+15","tzmoney":"462834","nextmoney":"30431514","date":"2017-05-26T00:00:00+08:00","updatetime":"2017-05-26T21:01:59+08:00","orderint":0},{"id":95,"czkind":1,"cztype":2,"name":"fcny36x7","cname":"福彩广东南粤36选7","qi":2017139,"opencode":"01,03,10,19,27,36+32","tzmoney":"546860","nextmoney":"2963849","date":"2017-05-26T00:00:00+08:00","updatetime":"2017-05-26T20:18:53+08:00","orderint":0},{"id":96,"czkind":1,"cztype":2,"name":"fchljp62","cname":"福彩黑龙江P62","qi":2017139,"opencode":"0,9,7,0,2,4+1","tzmoney":"61794","nextmoney":"468466","date":"2017-05-26T00:00:00+08:00","updatetime":"2017-05-26T19:56:20+08:00","orderint":0},{"id":97,"czkind":2,"cztype":2,"name":"tchlj61","cname":"体彩黑龙江6+1","qi":2017040,"opencode":"1,1,2,2,7,3+2","tzmoney":"150502","nextmoney":"5076154","date":"2017-05-26T00:00:00+08:00","updatetime":"2017-05-26T20:41:19+08:00","orderint":0},{"id":98,"czkind":1,"cztype":2,"name":"fcgxklsc","cname":"福彩广西快乐双彩","qi":2017139,"opencode":"03,04,06,15,20,23+14","tzmoney":"95208","nextmoney":"0","date":"2017-05-26T00:00:00+08:00","updatetime":"2017-05-26T21:59:05+08:00","orderint":0},{"id":99,"czkind":1,"cztype":3,"name":"fcgd11x5","cname":"","qi":2017052714,"opencode":"11,08,01,03,09","tzmoney":"","nextmoney":"","date":"2017-05-27T11:20:15+08:00","updatetime":"2017-05-27T11:21:42+08:00","orderint":0},{"id":100,"czkind":2,"cztype":1,"name":"tcqxc","cname":"体彩七星彩","qi":2017060,"opencode":"1,7,7,6,3,4,5","tzmoney":"15536294","nextmoney":"5908639","date":"2017-05-26T00:00:00+08:00","updatetime":"2017-05-26T21:16:49+08:00","orderint":0},{"id":101,"czkind":0,"cztype":3,"name":"fchbk3","cname":"","qi":170527015,"opencode":"3,3,6","tzmoney":"","nextmoney":"","date":"2017-05-27T11:30:00+08:00","updatetime":"2017-05-27T11:31:51+08:00","orderint":0},{"id":102,"czkind":1,"cztype":2,"name":"fcgdhc1","cname":"广东好彩1","qi":2017140,"opencode":"02+牛,春,南","tzmoney":"0","nextmoney":"0","date":"2017-05-27T00:00:00+08:00","updatetime":"2017-05-27T11:57:52+08:00","orderint":0},{"id":89,"czkind":2,"cztype":2,"name":"tcfj36x7","cname":"体彩福建36选7","qi":2017059,"opencode":"02,06,09,11,21,23,33+30","tzmoney":"1508206","nextmoney":"54115839","date":"2017-05-25T00:00:00+08:00","updatetime":"2017-05-25T20:16:12+08:00","orderint":0},{"id":88,"czkind":2,"cztype":2,"name":"tcfj31x7","cname":"体彩福建31选7","qi":2017139,"opencode":"05,16,17,18,19,20,22+26","tzmoney":"719844","nextmoney":"180095","date":"2017-05-26T00:00:00+08:00","updatetime":"2017-05-26T20:10:53+08:00","orderint":0},{"id":74,"czkind":1,"cztype":0,"name":"fcsdkjih","cname":"福彩3D开机号","qi":2017140,"opencode":"4,6,9","tzmoney":"0","nextmoney":"","date":"2017-05-27T00:00:00+08:00","updatetime":"2017-05-27T08:16:09+08:00","orderint":0},{"id":75,"czkind":1,"cztype":1,"name":"fcsd","cname":"福彩3D","qi":2017139,"opencode":"6,9,5","tzmoney":"42451252","nextmoney":"","date":"2017-05-26T00:00:00+08:00","updatetime":"2017-05-27T08:16:09+08:00","orderint":0},{"id":76,"czkind":2,"cztype":0,"name":"tcp3sjh","cname":"体彩P3试机号","qi":2017139,"opencode":"3,9,4","tzmoney":"14044600","nextmoney":"","date":"2017-05-26T00:00:00+08:00","updatetime":"2017-05-26T21:54:15+08:00","orderint":0},{"id":77,"czkind":2,"cztype":1,"name":"tcp3","cname":"体彩P3","qi":2017139,"opencode":"3,6,0","tzmoney":"14044600","nextmoney":"","date":"2017-05-26T00:00:00+08:00","updatetime":"2017-05-26T21:54:15+08:00","orderint":0},{"id":78,"czkind":2,"cztype":1,"name":"tcp5","cname":"体彩P5","qi":2017139,"opencode":"3,6,0,0,2","tzmoney":"9757062","nextmoney":"","date":"2017-05-26T00:00:00+08:00","updatetime":"2017-05-26T21:54:15+08:00","orderint":0},{"id":79,"czkind":1,"cztype":1,"name":"fcssq","cname":"福彩双色球","qi":2017060,"opencode":"05,10,13,24,26,31+04","tzmoney":"320553910","nextmoney":"320553910","date":"2017-05-25T00:00:00+08:00","updatetime":"2017-05-25T22:59:51+08:00","orderint":0},{"id":80,"czkind":2,"cztype":1,"name":"tcdlt","cname":"体彩大乐透","qi":2017059,"opencode":"08,11,13,15,17+03,10","tzmoney":"223675219","nextmoney":"3607004261","date":"2017-05-24T00:00:00+08:00","updatetime":"2017-05-24T21:49:05+08:00","orderint":0},{"id":81,"czkind":1,"cztype":1,"name":"fcqlc","cname":"福彩七乐彩","qi":2017060,"opencode":"03,06,07,09,17,22,29+13","tzmoney":"7782984","nextmoney":"0","date":"2017-05-26T00:00:00+08:00","updatetime":"2017-05-26T22:21:27+08:00","orderint":0},{"id":82,"czkind":1,"cztype":2,"name":"fcdf61","cname":"福彩东方6+1","qi":2017059,"opencode":"2,9,5,4,7,9+龙","tzmoney":"664820","nextmoney":"58104087","date":"2017-05-24T00:00:00+08:00","updatetime":"2017-05-24T21:45:19+08:00","orderint":0},{"id":84,"czkind":1,"cztype":2,"name":"fchd15x5","cname":"福彩华东15选5","qi":2017139,"opencode":"02,04,06,10,14","tzmoney":"597540","nextmoney":"1160781","date":"2017-05-26T00:00:00+08:00","updatetime":"2017-05-26T20:29:42+08:00","orderint":0},{"id":85,"czkind":2,"cztype":2,"name":"tcjs7ws","cname":"体彩江苏7位数","qi":2017080,"opencode":"4,6,0,4,2,5,8","tzmoney":"3698742","nextmoney":"117622256","date":"2017-05-26T00:00:00+08:00","updatetime":"2017-05-26T22:00:36+08:00","orderint":0},{"id":86,"czkind":2,"cztype":2,"name":"tczj61","cname":"体彩浙江6+1","qi":2017060,"opencode":"4,6,3,4,3,2+7","tzmoney":"2509028","nextmoney":"13821226","date":"2017-05-26T00:00:00+08:00","updatetime":"2017-05-26T20:24:06+08:00","orderint":0},{"id":87,"czkind":2,"cztype":2,"name":"tczj20x5","cname":"体彩浙江20选5","qi":2017139,"opencode":"01,02,04,09,15","tzmoney":"186820","nextmoney":"0","date":"2017-05-26T00:00:00+08:00","updatetime":"2017-05-26T19:54:08+08:00","orderint":0},{"id":103,"czkind":0,"cztype":0,"name":"fcssqkjih","cname":" 福彩双色球开机号","qi":2017061,"opencode":"01,02,03,04,05,06+12","tzmoney":"","nextmoney":"","date":"2017-05-27T00:00:00+08:00","updatetime":"0001-01-01T08:00:00+08:00","orderint":0}]}';
	console.log('-hybrid请求响应-renderCplist_转json   ' + new Date().getTime() + '   data: ' + data)
var obj = JSON.parse(data);
if(obj.err == '0') {
	var newCpdata = {}; //请求回来的数据
	obj.data.map(function(item) {
		if($.inArray(item.name, cpnames) != -1) { 
			newCpdata[item.name] = item;
		}
	});
	sjhnames.map(function(item){
		newCpdata[item.substr(0,item.length-3)].sjh=newCpdata[item].opencode;
		delete(newCpdata[item]);
	});
	renderList(newCpdata);
} else {
	alert(data.msg);
}

function renderList(list) {
	console.log(list);
	$('#cplist').find('a.ticket').map(function() {
		var cpname = $(this).attr('data-cpname');
		$(this).find('.cpqi').html(list[cpname].qi + "期");
		$(this).find('.cpdate').html(formateDate(list[cpname].date));
		var kjharr = list[cpname].opencode.split(/[,|+]/);
		$(this).find('.ballBox').find('.ball').map(function(index) {
			$(this).html(kjharr[index]);
		})
		$(this).find(".shortCpqi").text(list[cpname].qi.toString().substr(5,7)+"期");
		if($(this).find(".cpsjh").length>0){
			$(this).find(".cpsjh").text((list[cpname]).sjh.replace(/,/g,' '));
		}
//		$(this).attr('href','ggdetail.html?name='+cpname)
	})
}
}