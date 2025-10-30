#include"../includes/Table.h"
Table::Table(const vector<vector<char>>& b) : board(b) {}

Table::Table() {
	board=vector(HEIGHT+1, vector<char>(WIDTH+1,'.'));
	board[0][0]=0;
	board[1][1]='R';
	board[1][2]='N';
	board[1][3]='B';
	board[1][4]='A';
	board[1][5]='K';
	board[1][6]='A';
	board[1][7]='B';
	board[1][8]='N';
	board[1][9]='R';
	board[3][2]='C';
	board[3][8]='C';
	board[4][1]='P';
	board[4][3]='P';
	board[4][5]='P';
	board[4][7]='P';
	board[4][9]='P';
	board[10][1]='r';
	board[10][2]='n';
	board[10][3]='b';
	board[10][4]='a';
	board[10][5]='k';
	board[10][6]='a';
	board[10][7]='b';
	board[10][8]='n';
	board[10][9]='r';
	board[8][2]='c';
	board[8][8]='c';
	board[7][1]='p';
	board[7][3]='p';
	board[7][5]='p';
	board[7][7]='p';
	board[7][9]='p';
}

vector<vector<char>> Table::getBoard() {
	return board;
}

/*
the function isValidateMove and getAllValidateMove are implemented in the ValidateMove

the function isPossibleMove and getAllPossibleMove are implemented in the PossibleMove
*/

bool Table::gameOver() {
	auto moves1=getAllValidateMove();
	auto moves2=getAllPossibleMoves();
	return getAllPossibleMoves().size()==0;
}

void Table::doMove(Move move) {
	auto [x1, y1, x2, y2]=move.getMove();
	char piece=board[x1][y1];
	board[x1][y1]='.';
	board[x2][y2]=piece;
	board[0][0]^=1;
}