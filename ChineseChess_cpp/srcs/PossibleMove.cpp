#include"../includes/Table.h"

bool Table::isPossibleMove(Move move) {
	if(!isValidateMove(move)) return false;
	int side=board[0][0];
	Table simulation=*this;
	simulation.doMove(move);
	auto antiAllValidateMove=simulation.getAllValidateMove();
	for(Move antiMove:antiAllValidateMove) {
		auto [x1, y1, x2, y2]=antiMove.getMove();
		char piece=simulation.board[x2][y2];
		if(piece=='K' || piece=='k') {
			return false;
		}
	}
	return true;
}

vector<Move> Table::getAllPossibleMoves() {
	int side=board[0][0];
	vector<Move> allPossibleMove;
	for(int x1=1; x1<=HEIGHT; x1++) {
		for(int y1=1; y1<=WIDTH; y1++) {
			if(board[x1][y1]=='.') continue;
			if(isBlackSide(board[x1][y1])!=side) continue;
			for(int x2=1; x2<=HEIGHT; x2++) {
				for(int y2=1; y2<=WIDTH; y2++) {
					if(isPossibleMove(Move(x1, y1, x2, y2))) {
						allPossibleMove.push_back(Move(x1, y1, x2, y2));
					}
				}
			}
		}
	}
	return allPossibleMove;
}