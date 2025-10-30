#ifndef _H_INCLUDES_TABLE_H_
#define _H_INCLUDES_TABLE_H_

#include"Shared.h"
#include"Move.h"

class Table {
	private:
		vector<vector<char>> board;
	public:
	// Constructors
		Table(const vector<vector<char>>& b);
		Table();
	// Getters
		vector<vector<char>> getBoard();
	//functions
		//Possible move state checkers and geters
		bool isValidateMove(Move move);
		vector<Move> getAllValidateMove();
		bool isPossibleMove(Move move);
		vector<Move> getAllPossibleMoves();
		bool gameOver();
		//state changer
		void doMove(Move move);
};

#endif