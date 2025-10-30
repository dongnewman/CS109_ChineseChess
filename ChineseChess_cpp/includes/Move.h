#ifndef _H_INCLUDES_MOVE_H_
#define _H_INCLUDES_MOVE_H_
#include"Shared.h"

class Move {
	private:
		int x1, y1, x2, y2;
	public:
	// Constructor
		Move(int a, int b, int c, int d);
	// Getter
		tuple<int, int, int, int> getMove();
};

#endif