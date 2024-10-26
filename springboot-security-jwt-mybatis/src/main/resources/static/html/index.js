const getBaseURL = () => {
  if (window.location.hostname === "localhost" || window.location.hostname === "127.0.0.1") {
    return "http://localhost:8080"; // 로컬 URL
  } else {
    return "https://my.realserver.com"; // 운영 URL
  }
};

// 페이지 전환 함수
function showPage(page) {
  // 모든 페이지 숨기기
  document.getElementById("signupPage").style.display = "none";
  document.getElementById("allUsersPage").style.display = "none";
  document.getElementById("loginUsersPage").style.display = "none";
  document.getElementById("salesUsersPage").style.display = "none";
  document.getElementById("financeUsersPage").style.display = "none";
  document.getElementById("adminPage").style.display = "none";
  document.getElementById("loginPage").style.display = "none";

  // 선택된 페이지만 표시
  switch (page) {
    case "signup":
      document.getElementById("signupPage").style.display = "block";
      break;
    case "allUsers":
      document.getElementById("allUsersPage").style.display = "block";
      fetchPageData("allUsers", `${getBaseURL()}/all`);
      break;
    case "loginUsers":
      document.getElementById("loginUsersPage").style.display = "block";
      fetchPageData("loginUsers", `${getBaseURL()}/loginUserPage`);
      break;      
    case "salesUsers":
      document.getElementById("salesUsersPage").style.display = "block";
      fetchPageData("salesUsers", `${getBaseURL()}/sale`);
      break;
    case "financeUsers":
      document.getElementById("financeUsersPage").style.display = "block";
      fetchPageData("financeUsers", `${getBaseURL()}/acct`);
      break;
    case "admin":
      document.getElementById("adminPage").style.display = "block";
      fetchPageData("admin", `${getBaseURL()}/admin`);
      break;
    case "login":
      document.getElementById("loginPage").style.display = "block";
      break;
  }
}

async function fetchPageData(pageId, url, retryCount = 1) {
  const token = localStorage.getItem("access_token");

  var myHeaders = new Headers();
  myHeaders.append("Content-Type", "application/json");

  if (token) {
    myHeaders.append("Authorization", `Bearer ${token}`); // 토큰을 Authorization 헤더에 추가
  }

  try {
    const response = await fetch(url, {
      method: "GET",
      headers: myHeaders,
    });

    if (response.ok) {
      const data = await response.json();
      document.getElementById(`${pageId}Content`).textContent = JSON.stringify(data);
    } else if (response.status === 401 && retryCount > 0) {
      // Access token이 만료된 경우, 401 상태 코드 확인
      await reissueToken(); // refresh 토큰으로 access 토큰 재발급
      await fetchPageData(pageId, url, retryCount - 1); // 새로운 토큰으로 재시도
    } else {
      throw new Error("네트워크 응답이 올바르지 않습니다.");
    }
  } catch (error) {
    document.getElementById(`${pageId}Content`).textContent = "데이터를 가져오는데 실패했습니다: " + error.message;
  }
}

async function reissueToken() {
  try {
    const response = await fetch(`${getBaseURL()}/reissue`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      credentials: "include", // 쿠키를 포함하여 요청
    });

    if (response.ok) {
      const authHeader = response.headers.get("Authorization");
      if (authHeader) {
        const token = authHeader.split(" ")[1];
        localStorage.setItem("access_token", token); // 재발급 받은 access token 을 localstorage 저장
        console.log("refresh 토큰으로 access 토큰 재발급하여 로컬스토리에 저장완료!!!");
      } else {
        console.log("Authorization 헤더가 없음!!!");
      }
    } else {
      console.log("refresh 토큰으로 access token 발급실패!!!", response.status);
      throw new Error("토큰 재발급에 실패했습니다.");
    }
  } catch (error) {
    throw new Error("토큰 재발급에 실패했습니다: " + error.message);
  }
}

// 회원가입 버튼 클릭 시 이벤트 처리
document.getElementById("registerBtn").addEventListener("click", async function () {
  const username = document.getElementById("USERNAME").value;
  const password = document.getElementById("PASSWORD").value;
  const role = document.getElementById("ROLE").value;
  const messageDiv = document.getElementById("message");

  if (!username || !password) {
    messageDiv.textContent = "아이디와 패스워드를 입력해주세요.";
    return;
  }

  const data = {
    USERNAME: username,
    PASSWORD: password,
    ROLE: role,
  };

  try {
    const response = await fetch(`${getBaseURL()}/join`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(data),
    });

    if (response.ok) {
      messageDiv.textContent = "회원가입이 성공적으로 완료되었습니다!";
    } else {
      messageDiv.textContent = "회원가입에 실패했습니다.";
    }
  } catch (error) {
    messageDiv.textContent = "네트워크 오류가 발생했습니다.";
  }
});

// 로그인 버튼 클릭 시 이벤트 처리
document.getElementById("loginBtn").addEventListener("click", function () {
  const username = document.getElementById("loginUsername").value;
  const password = document.getElementById("loginPassword").value;
  const loginMessageDiv = document.getElementById("loginMessage");

  if (!username || !password) {
    loginMessageDiv.textContent = "아이디와 패스워드를 입력해주세요.";
    return;
  }

  var myHeaders = new Headers();
  myHeaders.append("Content-Type", "application/json");

  var raw = JSON.stringify({ USERNAME: username, PASSWORD: password });

  var requestOptions = {
    method: "POST",
    headers: myHeaders,
    body: raw,
    redirect: "follow",
  };

  fetch(`${getBaseURL()}/login`, requestOptions)
    .then((response) => {
      if (response.ok) {
        // Authorization 헤더에서 토큰 추출
        const authHeader = response.headers.get("Authorization");
        if (authHeader) {
          const token = authHeader.split(" ")[1]; // "Bearer <token>"에서 token만 추출
          localStorage.setItem("access_token", token);
          updateUIOnLogin(username); // 로그인 성공 시 UI 업데이트
        } else {
          loginMessageDiv.textContent = "Authorization 헤더가 없습니다.";
        }
      } else {
        document.getElementById("userInfo").style.display = "none";
        loginMessageDiv.textContent = "로그인 실패: " + response.statusText;
      }
    })
    .catch((error) => {
      document.getElementById("userInfo").style.display = "none";
      loginMessageDiv.textContent = "로그인 실패: " + error;
    });
});

// 로그인 성공 시 UI 업데이트 함수
function updateUIOnLogin(username) {
  // 로그인 링크를 로그아웃으로 변경
  const authActionLink = document.getElementById("authActionLink");
  authActionLink.textContent = "로그아웃";
  authActionLink.dataset.page = "logout";

  // 로그인 폼을 '환영합니다' 메시지로 변경
  document.getElementById("loginPage").style.display = "none";
  const userInfo = document.getElementById("userInfo");
  userInfo.textContent = `${username}님 환영합니다.`;
  userInfo.style.display = "block";

  // 로그아웃 클릭 이벤트 처리
  authActionLink.addEventListener("click", async function () {
    const response = await fetch(`${getBaseURL()}/logout`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      credentials: "include", // 쿠키 포함
    });

    if (response.ok) {
      alert("로그아웃 되었습니다.");
      localStorage.removeItem("access_token");
      location.reload(); // 페이지 새로고침으로 초기 상태로 되돌리기
    } else {
      alert("로그아웃에 실패했습니다.");
    }
  });
}

// 네비게이션 링크 클릭 이벤트 처리
document.querySelectorAll("nav a[data-page]").forEach((link) => {
  link.addEventListener("click", (event) => {
    event.preventDefault();
    const page = event.target.getAttribute("data-page");
    showPage(page);
  });
});
