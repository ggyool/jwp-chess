import {getData, postData} from "./utils/FetchUtil.js"

const url = "http://localhost:8080";

window.onload = function () {
  const newGameButton = document.querySelector(".new-game");
  const loadGameButton = document.querySelector(".load-game")
  const registerMemberButton = document.querySelector(".register-member")
  const searchRecordButton = document.querySelector(".search-record")

  newGameButton.addEventListener("click", startNewGame);
  loadGameButton.addEventListener("click", loadGame);
  registerMemberButton.addEventListener("click", registerMember);
}

async function startNewGame(e) {
  const userName = prompt("유저 이름을 입력하세요.");
  const password = prompt("비밀번호를 입력하세요.");

  try {
    validateInput(userName, password);
  } catch (e) {
    alert(e.message);
    return;
  }
  const response = await requestAuthentication(userName, password);
  if (!response) {
    alert("로그인에 실패했습니다.");
    return;
  }
  console.log(response);
  const roomName = prompt("방 이름을 입력하세요.");
  if (roomName.length === 0) {
    throw Error("방 이름을 입력하지 않았습니다.");
  }
  await createGame(response["id"], roomName);
}

function validateInput(userName, password) {
  if (userName.length === 0) {
    throw Error("이름을 입력하지 않았습니다.");
  }
  if (password.length === 0) {
    throw Error("비밀번호를 입력하지 않았습니다.");
  }
}

async function requestAuthentication(userName, password) {
  const params = {
    name: userName,
    password: password
  }
  return await postData(`${url}/api/users/authentication`, params);
}

async function createGame(hostId, gameName) {
  const body = {
    name: gameName,
    hostId: hostId
  };
  await postData(`${url}/games`, body);
}

function loadGame() {
  const gameId = prompt("이동할 방번호를 입력하세요.");
  if (gameId) {
    window.location.href = `${url}/games/${gameId}`
  }
}

async function registerMember() {
  const name = prompt("원하는 이름을 입력하세요.");
  if (!name) {
    alert("이름을 입력하지 않았습니다.");
    return;
  }
  const response = await getData(`${url}/api/users`, {name: name});
  if (response) {
    alert("이미 존재하는 이름입니다.");
    return;
  }
  const password = prompt("원하는 비밀번호를 입력하세요.");
  if (!password) {
    alert("비밀번호를 입력하지 않았습니다.");
    return;
  }
  const body = {
    name: name,
    password: password
  };
  if (await postData(`${url}/api/users`, body)) {
    alert("회원가입 완료!")
  } else {
    alert("회원가입이 실패했습니다. 잠시 후 다시 시도해 주세요.")
  }

}
