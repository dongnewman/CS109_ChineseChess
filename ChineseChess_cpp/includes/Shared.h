#ifndef _H_INCLUDES_SHARED_H_
#define _H_INCLUDES_SHARED_H_

// Shared definitions and utilities
#include<bits/stdc++.h>
using namespace std;

const int HEIGHT = 10, WIDTH = 9;

static inline bool isValidatePosition(int x, int y) {
	return (x > 0 && x <= HEIGHT && y > 0 && y <= WIDTH);
}

static inline bool isBlackSide(char c) {
	return (c >= 'a' && c <= 'z');
}

static inline bool isValidateType(char type) {
	const string validTypes = "KARCNPSkarnps";
	return (validTypes.find(type) != string::npos);
}

#endif