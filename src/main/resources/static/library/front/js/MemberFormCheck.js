var uCheck = false;
var pCheck = false;
var pcCheck = false;
var eCheck = false;
var nCheck = false;
var c1Check = false;

$(function() {
	$('#username').focus();
	$('#username').focusout(function() {
		$.ajax({
			type: 'Post',
			url: '/member/idCheck',
			async: false,
			data: {
				username: $('#username').val(),
			},
			success: function(result) { // success
				if (result == 'P') {
					$('#usernameHelp').html('사용 가능한 아이디 입니다');
				} else {
					$('#usernameHelp').html('중복된 아이디 입니다');
					$('#username').focus();
				}
			}, error: function(textStatus) {
				if (textStatus == "timeout") {
					alert("시간이 초과되어 데이터를 수신하지 못하였습니다.");
				} else {
					alert("데이터 전송에 실패했습니다. 다시 시도해 주세요");
				}
			}
		}); // ajax
		uCheck = usernameCheck();
	});
	$('#password').focusout(function() {
		pCheck = passwordCheck();
	});
	$('#password-confirm').focusout(function() {
		pcCheck = passwordConfirmCheck();
	});
	$('#email').focusout(function() {
		eCheck = emailCheck();
	});
	$('#nickname').focusout(function() {
		$.ajax({
			type: 'Post',
			url: '/member/nickCheck',
			async: false,
			data: {
				nickname: $('#nickname').val(),
			},
			success: function(result) { // success
				if (result == 'P') {
					$('#nicknameHelp').html('사용 가능한 닉네임 입니다');
					return true;
				} else {
					$('#nicknameHelp').html('중복된 닉네임 입니다');
					$('#nickname').focus();
					return false;
				}
			}, error: function(textStatus) {
				if (textStatus == "timeout") {
					alert("시간이 초과되어 데이터를 수신하지 못하였습니다.");
				} else {
					alert("데이터 전송에 실패했습니다. 다시 시도해 주세요");
				}
			}
		}); // ajax 
		nCheck = nicknameCheck();
	});
	$('#check1').change(function(){
		c1Check=check1Check();
	});
})

function inCheck() {
	if (!$("input:checkbox[id='check1']").is(":checked")) {
		$('#check1').focus();
		$('#check1Help').html('약관에 동의하셔야 회원가입이 가능합니다');
		return false;
	} else if(uCheck == false) {
		uCheck = usernameCheck();
	}else if(pCheck == false) {
		pCheck = passwordCheck();
	}else if(pcCheck == false) {
		pcCheck = passwordConfirmCheck();
	}else if(eCheck == false) {
		eCheck = emailCheck();
	}else if(nCheck == false) {
		nCheck = nicknameCheck();
	}
	
	if(uCheck == true && pCheck == true && pcCheck == true && eCheck == true && nCheck == true){
		return true;
	}else{
		return false;
	}
}

function usernameCheck() {
	if ($('#username').val().length < 5 || $('#username').val().length > 20) {
		$('#usernameHelp').html('ID는 4자이상 20자 이하로 입력 해 주세요');
		$('#username').focus();
		return false;
		// 사용 문자체크	
	} else if ($('#username').val().replace(/[a-z.0-9]/gi, '').length > 0) {
		$('#usernameHelp').html('ID는 영문과 숫자로만 입력 해 주세요');
		$('#username').focus();
		return false;
	} else {
		return true;	 
	}
}

function passwordCheck() {
	if ($('#password').val().length < 5) {
		$('#passwordHelp').html('Password 는 5 글자 이상 입력하세요');
		$('#password').focus();
		return false;
	} else if ($('#password').val().replace(/[!-*]/gi, '').length >= $('#password').val().length) {
		$('#passwordHelp').html('Password는 특수문자를 반드시 1개 이상 입력하세요');
		$('#password').focus();
		return false;
	} else {
		$('#passwordHelp').html('---');
		return true;
	}
}

function passwordConfirmCheck() {
	if ($('#password-confirm').val() != $('#password').val()) {
		$('#password-confirm-Help').html('비밀번호를 정확하게 확인 해 주세요');
		$('#password').focus();
	} else {
		$('#password-confirm-Help').html('---');
		return true;
	}
}

function emailCheck() {
	if (!/^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/i.test($('#email').val())) {
		$('#emailHelp').html('이메일을 정확하게 입력 하세요');
		$('#email').focus();
		return false;
	} else {
		$('#emailHelp').html('---');
		return true;
	}
}
function nicknameCheck() {
	if ($('#nickname').val().length < 3 || $('#nickname').val().length > 20) {
		$('#nicknameHelp').html('ID는 3자이상 20자 이하로 입력 해 주세요');
		$('#nickname').focus();
		return false;
		// 사용 문자체크	
	} else if ($('#nickname').val().replace(/[a-z.가-힇.0-9]/gi, '').length > 0) {
		$('#nicknameHelp').html('Name 은 한글, 영문 또는 숫자로만 입력하세요');
		$('#nickname').focus();
		return false;
	} else {
		return true;
	}
}

function check1Check() {
	if (!$("input:checkbox[id='check1']").is(":checked")) {
		$('#check1Help').html('약관에 동의하셔야 회원가입이 가능합니다');
		$('#check1').focus();
	} else {
		$('#check1Help').html('약관에 동의해 주셔서 감사합니다');
	}
}














