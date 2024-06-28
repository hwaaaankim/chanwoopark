function videoRegisterF(){
	window.open('videoRegisterF','_blank',
	'toobar=no, menubar=yes, scrollbars=yes, resizable=yes, width=650, height=800');
}
function videoDetail(videoId){
	window.open('/videoDetail?id='+videoId,'_blank',
							'toobar=no, menubar=yes, scrollbars=yes, resizable=yes, width=650, height=800');
}
function videoDelete(videoId){
	var result=confirm("영상을 삭제 하시겠습니까?");
	if(result){
		location.href='/videoDelete?id='+videoId;	
	}
}


function memberDetail(memberId){
	window.open('/memberDetail?id='+memberId,'_blank',
							'toobar=no, menubar=yes, scrollbars=yes, resizable=yes, width=650, height=800');
}
function memberDelete(memberId){
	var result=confirm("해당 유저를 삭제 하시겠습니까?");
	if(result){
		location.href='/memberDelete?id='+memberId;	
	}
}
$(function(){
	$('#passBtn').click(function(){
		$('#userPass').attr('disabled', false);
		$('#userPass').val('');
	});
	
	$('#enabledBtn').click(function(){
		
		if($('#enabledArea').val()=='true'){
			var a = confirm('해당 유저를 비활성 상태로 전환 하시겠습니까?');
			if(a){
				$('#enabledArea').val('false');
			}
		}else if($('#enabledArea').val()=='false'){
			var a = confirm('해당 유저를 이용가능 상태로 전환 하시겠습니까?');
			if(a){
				$('#enabledArea').val('true');
			}
		}
	});
	
});
function pointCharge(id, name, point, username, symbol){
	if(symbol == 'plus'){
		var result=confirm(name+"님에게 "+point + "포인트를 충전하시겠습니까?")
	}else if(symbol=='minus'){
		var result=confirm(name+"님에게 "+point + "포인트를 차감하시겠습니까?")
	}
	
	if(result){
		$.ajax({
			type: 'Post',
			url: '/member/pointCharge',
			async: false,
			data: {
				id : id,
				name:name,
				point:point,
				username:username,
				symbol:symbol,
			},
			success: function(result) { // success
				if (result == 'P') {
					alert('정상적으로 반영 되었습니다.');
					location.reload();
				} else {
					alert('에러 발생. 다시 시도 해 주세요');
					location.reload();
				}
			}, error: function(textStatus) {
				if (textStatus == "timeout") {
					alert("시간이 초과되어 데이터를 수신하지 못하였습니다.");
				} else {
					alert("데이터 전송에 실패했습니다. 다시 시도해 주세요");
				}
			}
		}); // ajax
	}
}

function searchReset(){
	$('#contentSearch').val('');
	$('#searchCondition').val('none').prop("selected", true);
	$('#contentCount').val('default').prop("selected", true);
}

function boardsDetail(boardsId){
	window.open('/community/boardDetail?id='+boardsId,'_blank',
							'toobar=no, menubar=yes, scrollbars=yes, resizable=yes, width=650, height=800');
}
function boardsDelete(boardsId){
	var result=confirm("문의를 삭제 하시겠습니까?");
	if(result){
		location.href='/community/boardDelete?id='+boardsId;	
	}
}


function noticeRegisterF(){
	window.open('/community/noticeRegisterF','_blank',
	'toobar=no, menubar=yes, scrollbars=yes, resizable=yes, width=650, height=800');
}
function noticeDetail(noticeId){
	window.open('/community/noticeDetail?id='+noticeId,'_blank',
							'toobar=no, menubar=yes, scrollbars=yes, resizable=yes, width=650, height=800');
}
function noticeDelete(noticeId){
	var result=confirm("공지사항을 삭제 하시겠습니까?");
	if(result){
		location.href='/community/noticeDelete?id='+noticeId;	
	}
}

function orderDelete(Id){
	var result=confirm("해당 주문을 삭제 하시겠습니까?");
	if(result){
		location.href='/orderDelete?id='+Id;	
	}
}




















