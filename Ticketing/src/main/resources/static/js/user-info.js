document.addEventListener('DOMContentLoaded',function(){

  a = document.querySelectorAll('a');

  a.forEach(function (element) {
    for (var i = 1; i <= 20; i++) {
        var userkey = 'data-user' + i;
        var userString = element.getAttribute(userkey);
        if (userString) {
          var userData = JSON.parse(userString);
          addUserInfoToDiv(userData);
        }
    }
  });

})

function addUserInfoToDiv(user) {
    const memberCardDiv = document.getElementById('member');

    const userInfoElement = document.createElement('div');
    userInfoElement.classList.add('member-card'); // 새로운 클래스 추가
    //출력할 형태 지정
    userInfoElement.innerHTML = `
      <h2>${user.name}</h2>
      <p>아이디: ${user.id}</p>
      <p>이메일: ${user.email}</p>
      <p>성별: ${user.gender}</p>
      <p>휴대폰번호: ${user.phoneNumber}</p>
      <p>나이: ${user.age}</p>
    `;


    memberCardDiv.appendChild(userInfoElement);
}
