#include <iostream>
#include <vector>

#define STB_IMAGE_IMPLEMENTATION
#include "stb_image/stb_image.h"
#define STB_IMAGE_WRITE_IMPLEMENTATION
#include "stb_image/stb_image_write.h"

using namespace std;

typedef struct{
	int r;
	int g;
	int b;
} px;

int main(){
	int width, height, channels;
	unsigned char *img = stbi_load("./ascii-pineapple.jpg", &width, &height, &channels, 0);
	if (img == NULL){
		cout << "Image loading failed!!" << endl;
		return 1;
	}
	cout << "Successfully loaded image!" << endl;
	cout << "Image size: " << width << " x " << height << endl;
	
	vector<vector<px>> pixel_matrix;
	size_t img_size = width * height * channels;
	
	for (int i = 0; i < height; i++){
		vector<px> row;
		for (unsigned char *p = img + (width*channels) * i; p != img + (width*channels) * (i+1); p += channels){
			px q;
			q.r = *p;
			q.g = *(p+1);
			q.b = *(p+2);
			row.push_back(q);
		}
		pixel_matrix.push_back(row);
	}
	
	vector<vector<char>> char_matrix;
	char_matrix.resize(height);
	for (int i = 0; i < height; i++)
		char_matrix[i].resize(width);
	
	string scale = "`^\",:;Il!i~+_-?][}{1)(|\\/tfjrxnuvczXYUJCLQ0OZmwqpdbkhao*#MW&8%B@$";
	
	for (int i = 0; i < height; i++){
		for(int j = 0; j < width; j++){
			int br = pixel_matrix[i][j].r + pixel_matrix[i][j].g
				+ pixel_matrix[i][j].b;
			br /= 3;
			br = (int) scale.size() * (br / 256.0);
			br = scale.size() - (br + 1);
			char_matrix[i][j] = scale[br];
		}
	}
	
	for (int i = 0; i < height; i++){
		for(int j = 0; j < width; j++){
			cout << "\x1b[38;2;" << pixel_matrix[i][j].r
				<< ";" << pixel_matrix[i][j].g
				<< ";" << pixel_matrix[i][j].b << "m";
			cout << char_matrix[i][j];
		}
		cout << "\x1b[38;2;0;0;0m";
		cout << endl;
	}
	
	return 0;
}
