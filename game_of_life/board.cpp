#include <cstdio>
#include <fstream>
#include <iostream>
#include <vector>
#include <random>
#include <string>

using namespace std;

class Board{
	public:
		vector<vector<int>> board, next_board;
		int width;
		int height;
		
		Board(int width, int height){
			srand(time(0));
			this->width = width;
			this->height = height;
			board.resize(width+2);
			for(int w = 0; w < width+2; w++){
				board[w].resize(height+2);
			}
			for(int h = 0; h < height+2; h++){
				board[0][h] = 0;
				board[width-1][h] = 0;
			}
			for(int w = 0; w < width+2; w++){
				for(int h = 0; h < height+2; h++){
					board[w][h] = 0;
				}
			}
		}
		
		void randomizeBoard(){
			for(int w = 1; w < width+1; w++){
				for(int h = 1; h < height+1; h++){
					board[w][h] = rand() & 1;
				}
			}
		}
		
		void loadShape(string name, int sRow, int sCol){
			ifstream in;
			in.open("shapes/" + name + ".txt");
			string line;
			int w = sCol;
			int h = sRow;
			while(getline(in, line)){
				for(int i = 0; i < line.size(); i++){
					board[w][h] = line[i] - '0';
					w++;
				}
				h++;
				w = sCol;
			}
			in.close();
		}
		
		void print(){
			cout.write("\x1b[2J", 4);
			cout.write("\x1b[H", 3);
			string str = "";
			for(int i = 0; i < width+2; i++){
				str.append("-");
			}
			str.append("\n");
			for(int h = 1; h < height+1; h++){
				str.append("|");
				for(int w = 1; w < width+1; w++){
					if (board[w][h] == 0) {
						str.append(" ");
					} else {
						str.append("*");
					}
				}
				str.append("|\n");
			}
			for(int i = 0; i < width+2; i++){
				str.append("-");
			}
			str.append("\n");
			cout << str;
		}
		
		void nextGen(){
			next_board.clear();
			next_board.resize(width+2);
			for(int w = 0; w < width+2; w++){
				next_board[w].resize(height+2);
			}
			for(int h = 0; h < height+2; h++){
				next_board[0][h] = 0;
				next_board[width-1][h] = 0;
			}
			for(int w = 1; w < width+1; w++){
				next_board[w][0] = 0;
				for(int h = 1; h < height+1; h++){
					next_board[w][h] = rand() & 1;
				}
				next_board[w][height+1] = 0;
			}
			for(int w = 1; w < width+1; w++){
				for(int h = 1; h < height+1; h++){
					int sum = board[w-1][h-1] + board[w-1][h] + board[w-1][h+1]
						+ board[w][h-1] + board[w][h+1]
						+ board[w+1][h-1] + board[w+1][h] + board[w+1][h+1];
					if(sum > 3){
						next_board[w][h] = 0;
					}else if(sum == 3){
						next_board[w][h] = 1;
					}else if(sum == 2 && board[w][h]){
						next_board[w][h] = 1;
					}else{
						next_board[w][h] = 0;
					}
				}
			}
			swap(board, next_board);
		}
};
