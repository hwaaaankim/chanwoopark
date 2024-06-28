var nCheck = false;
var passCheck = false;
var nickCheck = false;
var emailCheck = false;

function nickInCheck() {
	if (!nickCheck) {
		$('#nickname').attr("disabled", false);
		$('#nickname').val('');
		$('#nickCBtn').val('수정완료');
		nickCheck = !nickCheck;
		return false;
	} else if ($('#nickname').val().length < 3 || $('#nickname').val().length > 20) {
		$('#nicknameHelp').html('ID는 3자이상 20자 이하로 입력 해 주세요');
		$('#nickname').focus();
		return false;
	} else if ($('#nickname').val().replace(/[a-z.가-힇.0-9]/gi, '').length > 0) {
		$('#nicknameHelp').html('Name 은 한글,영문 또는 숫자로만 입력하세요');
		$('#nickname').focus();
		return false;
	} else {
		$.ajax({
			type: 'Post',
			url: '/member/nickCheck',
			async: false,
			data: {
				nickname: $('#nickname').val(),
			},
			success: function(result) { // success
				if (result == 'P') {
					nCheck = !nCheck;
				} else {
					$('#nicknameHelp').html('중복된 닉네임 입니다');
				}
			}, error: function(textStatus) {
				if (textStatus == "timeout") {
					alert("시간이 초과되어 데이터를 수신하지 못하였습니다.");
				} else {
					alert("데이터 전송에 실패했습니다. 다시 시도해 주세요");
				}
			}
		}); // ajax 
		return nCheck;
	}
}

function passwordInCheck() {
	if (!passCheck) {
		$('#password').attr("disabled", false);
		$('#password').val('');
		$('#passwordCBtn').val('수정완료');
		passCheck = !passCheck;
		return false;
	} else if ($('#password').val().length < 5) {
		$('#passwordHelp').html('Password 는 5 글자 이상 입력하세요');
		$('#password').focus();
		return false;
	} else if ($('#password').val().replace(/[!-*]/gi, '').length >= $('#password').val().length) {
		$('#passwordHelp').html('Password는 특수문자를 반드시 1개 이상 입력하세요');
		$('#password').focus();
		return false;
	} else {
		return true;
	}
}

function emailInCheck() {
	if (!emailCheck) {
		$('#email').attr("disabled", false);
		$('#email').val('');
		$('#emailCBtn').val('수정완료');
		emailCheck = !emailCheck;
		return false;
	} else if (!/^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/i.test($('#email').val())) {
		$('#emailHelp').html('이메일을 정확하게 입력 하세요');
		$('#email').focus();
		return false;
	} else {
		$('#emailHelp').html('---');
		return true;
	}
}

