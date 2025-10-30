#include"../includes/Shared.h"
#include"../includes/Table.h"
#include"../includes/Interactive.h"

void runGame() {
	Table table;
	int side=0;
	while(!table.gameOver()) {
		printBoard(table);
		if(side==0) {
			printMessage("Red's turn:");
		} else {
			printMessage("Black's turn:");
		}
		Move move=getUserMove();
		auto [x1, y1, x2, y2] = move.getMove();
		if(!isValidatePosition(x1, y1) || !isValidatePosition(x2, y2)) {
			printMessage("Positions out of bounds, try again.");
			continue;
		}
		if(table.isPossibleMove(move)) {
			table.doMove(move);
			side=!side;
		} else {
			printMessage("Invalid move, try again.");
		}
	}
	if(side==0) {
		printMessage("Black wins!");
	} else {
		printMessage("Red wins!");
	}
}

int main() {
	runGame();
	return 0;
}