import {getData, postData} from "./utils/FetchUtil.js"
import {setCookie, USER_ID_KEY} from "./utils/CookieUtil.js";
import {login} from "./utils/LoginUtil.js";
import {CHESS_URL} from "./URL.js";

const url = CHESS_URL;

window.onload = function () {
  const newGameButton = document.querySelector(".new-game");
  const searchGameButton = document.querySelector(".search-game")
  const registerMemberButton = document.querySelector(".register-member")
  const searchRecordButton = document.querySelector(".search-record")

  newGameButton.addEventListener("click", startNewGame);
  searchGameButton.addEventListener("click", showGames);
  registerMemberButton.addEventListener("click", registerMember);
}

async function startNewGame(e) {
  const hostId = await login();
  if (!hostId) {
    return;
  }
  setCookie(USER_ID_KEY, hostId);
  const roomName = prompt("방 이름을 입력하세요.");
  if (roomName.length === 0) {
    throw Error("방 이름을 입력하지 않았습니다.");
  }
  await createGame(hostId, roomName);
}

async function createGame(hostId, gameName) {
  const body = {
    name: gameName,
    hostId: hostId
  };
  const response = await postData(`${url}/games`, body);
  if (!response) {
    alert("게임을 생성하는데 실패했습니다.");
  }
}

async function showGames() {
  window.location.href = `${url}/rooms`
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
