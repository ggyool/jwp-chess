import {Tiles} from "../tile/Tiles.js"
import {Pieces} from "../piece/Pieces.js"
import {Turn} from "./Turn.js"
import {getData, putData} from "../utils/FetchUtil.js"
import {CHESS_URL} from "../URL.js";

const url = CHESS_URL;

export class Board {
  #tiles
  #pieces
  #component
  #sourceTile
  #turn
  #role

  constructor(pieceDtos, turn, role) {
    this.#tiles = new Tiles();
    this.#pieces = new Pieces(pieceDtos);
    this.#component = document.querySelector(".grid");
    this.#sourceTile = null;
    this.#turn = new Turn(turn);
    this.#role = role;
    this.#addEvent()
  }

  get tiles() {
    return this.#tiles;
  }

  get pieces() {
    return this.#pieces;
  }

  get component() {
    return this.#component;
  }

  get sourceTile() {
    return this.#sourceTile;
  }

  #addEvent() {
    this.#component.addEventListener("dragover", this.#allowDrop);
    this.#component.addEventListener("dragstart",
        e => this.#markSourceTile(e, this));
    this.#component.addEventListener("dragEnd",
        e => this.#unmarkSourceTile(e, this));
    this.#component.addEventListener("dragenter",
        e => this.#enterPiece(e, this));
    this.#component.addEventListener("dragleave",
        e => this.#leavePiece(e, this));
    this.#component.addEventListener("drop", e => this.#dropPiece(e, this));
  }

  #allowDrop(e) {
    e.preventDefault()
  }

  #markSourceTile(e, board) {
    board.#sourceTile = board.#findTileByPieceComponent(e.target);
  }

  #findTileByPieceComponent(component) {
    const piece = this.#pieces.findByComponent(component);
    return this.#tiles.findByPosition(piece.x, piece.y);
  }

  #unmarkSourceTile(e, board) {
    board.#sourceTile = null;
  }

  async #enterPiece(e, board) {
    if (!this.#checkTurn()) {
      return;
    }
    if (!e.target.classList.contains("tile") &&
        !e.target.classList.contains("piece")) {
      return;
    }
    const targetTile = this.#getTargetTile(e.target, board);
    const sourceTile = board.#sourceTile;
    if (sourceTile.same(targetTile)) {
      return;
    }
    const params = {
      source: board.#sourceTile.component.id,
      target: targetTile.component.id,
      color: this.#pieces.findByPosition(sourceTile.x, sourceTile.y).team
    }

    const gameId = this.#findGameIdInUri();
    try {
      const response = await getData(
          `${url}/api/games/${gameId}/move/check`, params
      );
      targetTile.highlight(response["movable"]);
    } catch (e) {
      console.log(e);
    }
  }

  #findGameIdInUri() {
    const path = window.location.pathname
    const gameId = path.substr(path.lastIndexOf("/") + 1);
    return gameId;
  }

  #getTargetTile(target, board) {
    if (target.classList.contains("tile")) {
      return board.#tiles.findByComponent(target);
    }
    if (target.classList.contains("piece")) {
      return board.#findTileByPieceComponent(target);
    }
  }

  #leavePiece(e, board) {
    const copy = e.target;
    board.#unhighlight(copy);
  }

  async #dropPiece(e, board) {
    if (!this.#checkTurn()) {
      return;
    }
    const sourcePosition = e.dataTransfer.getData("sourcePosition");
    const piece = board.#pieces.findBySourcePosition(sourcePosition);
    const sourceTile = board.#tiles.findByPosition(piece.x, piece.y);
    const targetTile = this.#getTargetTile(e.target, board);
    if (sourceTile.same(targetTile)) {
      piece.unhighlight();
      return;
    }

    const params = {
      source: sourceTile.component.id,
      target: targetTile.component.id,
      color: piece.team
    }
    const gameId = this.#findGameIdInUri();
    const response = await getData(
        `${url}/api/games/${gameId}/move/check`, params);
    const movable = response["movable"]
    if (movable) {
      await this.#requestMove(piece, targetTile, params, gameId);
    } else {
      piece.unhighlight();
    }
    targetTile.unhighlight();
  }

  async #requestMove(piece, targetTile, body, gameId) {
    const response = await putData(
        `${url}/api/games/${gameId}/move`, body);

    if (!response) {
      return;
    }
    this.#pieces.move(piece, targetTile)
    this.#turn.changeTurn();
    if (response["finished"]) {
      const back = confirm(`게임이 끝났습니다. 확인을 누르면 홈으로 돌아갑니다.`)
      if (back) {
        window.location.href = "/";
      }
    }
  }

  #unhighlight(target) {
    if (target.classList.contains("tile")) {
      const tile = this.#tiles.findById(target.id);
      window.setTimeout(function () {
        tile.unhighlight.call(tile);
      }, 150);
    }
    if (target.classList.contains("piece")) {
      const tile = this.#findTileByPieceComponent(target);
      window.setTimeout(function () {
        tile.unhighlight.call(tile);
      }, 150);
    }
  }

  findPieceBySourcePosition(sourcePosition) {
    return this.#pieces.findBySourcePosition(sourcePosition);
  }

  #checkTurn() {
    if (this.#turn.isWhite() && this.#role.isHost()) {
      return true;
    }
    if (this.#turn.isBlack() && this.#role.isGuest()) {
      return true;
    }
    return false;
  }

  moveOtherSide(source, target, color) {
    if (color === "white" && this.#role.isHost()) {
      return;
    }
    if (color === "black" && this.#role.isGuest()) {
      return;
    }
    const sourceTile = this.#tiles.findById(source);
    const targetTile = this.#tiles.findById(target);
    const sourcePiece = this.#pieces.findByPosition(sourceTile.x, sourceTile.y);
    this.#pieces.move(sourcePiece, targetTile)
    this.#turn.changeTurn();
  }

}
