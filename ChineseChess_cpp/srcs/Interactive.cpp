#include"../includes/Interactive.h"

void printBoard(Table table) {
	auto board = table.getBoard();
	cout << "   1 2 3 4 5 6 7 8 9\n";
	for (int i = HEIGHT; i>0 ; i--) {
		cout << setw(3) << setiosflags(ios::left) << i;
		for (int j = 1; j<= WIDTH; j++) {
			cout << board[i][j] << " ";
		}
		cout << endl;
	}
}

Move getUserMove() {
	int x1, y1, x2, y2;
	cout << "Enter your move (x1 y1 x2 y2): ";
	cin >> x1 >> y1 >> x2 >> y2;
	return Move(x1, y1, x2, y2);
}

void printMessage(string message) {
	cout << message << endl;
}