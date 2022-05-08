#include <iostream>
#include <fstream>
#include <vector>
#include <string>

using namespace std;

int main(int argc, char **argv){
	
	fstream file;
	file.open(argv[1]);
	
	cout << argv[1] << endl;
	
	vector<string> fileContent;
	string line;
	
	while (getline(file, line)){
		string *str = new string(line);
		fileContent.push_back(*str);
	}
	
	for (string line : fileContent){
		for (int i = line.size()-1; i >= 0; i--){
			cout << line[i];
		}
		cout << endl;
	}
	file.flush();
	
	return 0;
}
