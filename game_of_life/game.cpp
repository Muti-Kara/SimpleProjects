#include <unistd.h>
#include "board.cpp"

using namespace std;

int main(){
	
	Board board(250, 100);
	board.loadShape("gosper_glider_gun", 0, 30);
	board.loadShape("gosper_glider_gun", 0, 70);
	board.loadShape("gosper_glider_gun_rev", 0, 121);
	board.loadShape("gosper_glider_gun_rev", 0, 161);
	while (1){
		board.print();
		board.nextGen();
		usleep(10000);
	}
	
}
