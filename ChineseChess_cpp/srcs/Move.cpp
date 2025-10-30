#include"../includes/Move.h"

Move::Move(int a, int b, int c, int d) {
	/*
	The validation checkment is done in rungame.cpp
	*/
	x1 = a;
	y1 = b;
	x2 = c;
	y2 = d;
}

tuple<int, int, int, int> Move::getMove() {
	return make_tuple(x1, y1, x2, y2);
}