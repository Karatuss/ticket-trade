document.getElementById("submit").addEventListener("click", function(){
    var n_ID = document.getElementById("n_ID").value;
    var n_PW = document.getElementById("n_PW").value;
    var username = document.getElementById("username").value;
    var email = document.getElementById("email").value;
    var gender = document.querySelector('input[name="gender"]:checked').value;
    var phoneNumber = document.getElementById("phoneNumber").value;
    var age = document.getElementById("age").value;

    var data = JSON.stringify({
        n_ID: n_ID,
        n_PW: n_PW,
        username: username,
        email: email,
        gender: gender,
        phoneNumber: phoneNumber,
        age : age
    });
    var myHeaders = new Headers();
    myHeaders.append("Content-Type", "application/json");

    var requestOptions = {
        method: 'POST',
        headers: myHeaders,
        body: data,
        redirect: 'follow'
    };

    if(age >= 0 && age <= 100){
        fetch('http://localhost:8080/register',requestOptions)


            .then(response => {
                if(!response.ok) {
                    throw new Error('네트워크 오류');
                }
                return response.json();
            })
            .then(data => {
                if(data.submit){
                    alert('회원가입이 완료되었습니다!\n다시 로그인 해주세요');
                    window.location.href = 'login'
                }
                /*<!--아이디, 비밀번호 등의 중복 오류가 발생했을 시 data.submit는 false를 반환
                추후 중복에 관한 오류 삽입 예정*/
                else{
                    alert('회원가입 중 오류가 발생하였습니다.<br>다시 시도해 주세요');
                    document.getElementById("n_ID").value = "";
                    document.getElementById("n_PW").value = "";
                    document.getElementById("username").value = "";
                    document.getElementById("email").value = "";
                    document.querySelector('input[name="gender"]:checked').checked = false;
                    document.getElementById("phoneNumber").value = "";
                    document.getElementById("age").value = "";
                }
            })
            .catch(error => console.error('Error', error));
    } else{
        alert("나이가 범위를 초과하였습니다 ( 0 < 나이 < 100)")
        document.getElementById("age").value = "";
    }

    
});