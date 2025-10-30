#include"../includes/Table.h"

bool Table::isValidateMove(Move move) {
	// Implementation of move validation logic goes here
	auto [x1, y1, x2, y2]=move.getMove();
	if(x1==x2 && y1==y2) return false;
	bool side=board[0][0];
	if(board[x1][y1]=='.' || isBlackSide(board[x1][y1])!=side) return false;
	if(board[x2][y2]!='.' && isBlackSide(board[x2][y2])==side) return false;
	// Add specific piece movement rules here
	if(board[x1][y1]=='R' || board[x1][y1]=='r') {
		// Rook movement logic
		if(x1==x2) {
			for(int i=min(y1, y2)+1; i<max(y1, y2); i++) {
				if(board[x1][i]!='.') return false;
			}
			return true;
		}
		else if(y1==y2) {
			for(int i=min(x1, x2)+1; i<max(x1, x2); i++) {
				if(board[i][y1]!='.') return false;
			}
			return true;
		}
		else {
			return false;
		}
	}
	else if(board[x1][y1]=='N' || board[x1][y1]=='n') {
		// Knight movement logic
		int dx=abs(x2 - x1);
		int dy=abs(y2 - y1);
		if(dx==2 && dy==1) {
			if(board[(x1 + x2) / 2][y1]!='.') return false;
			return true;
		}
		else if(dx==1 && dy==2) {
			if(board[x1][(y1 + y2) / 2]!='.') return false;
			return true;
		}
		else {
			return false;
		}
	}
	else if(board[x1][y1]=='B' || board[x1][y1]=='b') {
		// Bishop movement logic
		int dx=abs(x2 - x1);
		int dy=abs(y2 - y1);
		if(dx==2 && dy==2) {
			if(board[(x1 + x2) / 2][(y1 + y2) / 2]!='.') return false;
			return true;
		}
		else {
			return false;
		}
	}
	else if(board[x1][y1]=='A' || board[x1][y1]=='a') {
		// Advisor movement logic
		int dx=abs(x2 - x1);
		int dy=abs(y2 - y1);
		if(dx==1 && dy==1) {
			if(side) {
				if(x2<8 || y2<4 || y2>6) return false;
			}
			else {
				if(x2>3 || y2<4 || y2>6) return false;
			}
			return true;
		}
		else {
			return false;
		}
	}
	else if(board[x1][y1]=='K' || board[x1][y1]=='k') {
		// King movement logic
		int dx=abs(x2 - x1);
		int dy=abs(y2 - y1);
		if(dx+dy==1) {
			if(side) {
				if(x2<8 || y2<4 || y2>6) return false;
			}
			else {
				if(x2>3 || y2<4 || y2>6) return false;
			}
			return true;
		}
		else {
			return false;
		}
	}
	else if(board[x1][y1]=='C' || board[x1][y1]=='c') {
		// Cannon movement logic
		if(x1==x2) {
			int count=0;
			for(int i=min(y1, y2)+1; i<max(y1, y2); i++) {
				if(board[x1][i]!='.') count++;
			}
			if(board[x2][y2]=='.') {
				return (count==0);
			}
			else {
				return (count==1);
			}
		}
		else if(y1==y2) {
			int count=0;
			for(int i=min(x1, x2)+1; i<max(x1, x2); i++) {
				if(board[i][y1]!='.') count++;
			}
			if(board[x2][y2]=='.') {
				return (count==0);
			}
			else {
				return (count==1);
			}
		}
		else {
			return false;
		}
	}
	else if(board[x1][y1]=='P' || board[x1][y1]=='p') {
		// Pawn movement logic
		int dx=x2 - x1;
		int dy=abs(y2 - y1);
		if(side==0) {
			if(x1<=5) {
				if(dx==1 && dy==0) return true;
				else return false;
			}
			else {
				if((dx==1 && dy==0) || (dx==0 && dy==1)) return true;
				else return false;
			}
		}
		else {
			if(x1>=6) {
				if(dx==-1 && dy==0) return true;
				else return false;
			}
			else {
				if((dx==-1 && dy==0) || (dx==0 && dy==1)) return true;
				else return false;
			}
		}
	}
	{//the last check: kings can't see each other
		int Kx=0, Ky=0, kx=0, ky=0;
		for(int x=1; x<=3; x++) {
			for(int y=4; y<=6; y++) {
				if(board[x][y]=='K') {
					Kx=x;
					Ky=y;
				}
			}
		}
		for(int x=8; x<=10; x++) {
			for(int y=4; y<=6; y++) {
				if(board[x][y]=='k') {
					kx=x;
					ky=y;
				}
			}
		}
		if(!Kx || !Ky || !kx || !ky) {
			cerr<<"RUNERR : Cant find kings"<<endl;
		}
		if(ky!=Ky) return true;
		int count=0;
		for(int x=Kx+1; x<kx; x++) {
			if(board[x][Ky]!='.') count++;
		}
		if(count==0) {
			cerr<<"RUNERR : Kings see each other\n";
			return false;
		}
		else if(count==1) {
			if(y1==ky && (x1>Kx && x1<kx)) {
				return y2==ky && (x2>Kx && x2<kx);
			}
			else return true;
		}
		else return true;
	}
}

vector<Move> Table::getAllValidateMove() {
	bool side=board[0][0];
	vector<Move> allValidateMove;
	for(int x1=1; x1<=HEIGHT; x1++) {
		for(int y1=1; y1<=WIDTH; y1++) {
			if(board[x1][y1]=='.') continue;
			if(isBlackSide(board[x1][y1])!=side) continue;
			for(int x2=1; x2<=HEIGHT; x2++) {
				for(int y2=1; y2<=WIDTH; y2++) {
					if(isValidateMove(Move(x1, y1, x2, y2))) {
						allValidateMove.push_back(Move(x1, y1, x2, y2));
					}
				}
			}
		}
	}
	return allValidateMove;
}