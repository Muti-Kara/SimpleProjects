#include <cmath>
#include <cstdlib>
#include <iostream>
#include <string>
#include <vector>

#define STB_IMAGE_RESIZE_IMPLEMENTATION
#include "stb_image/stb_image_resize.h"
#define STB_IMAGE_IMPLEMENTATION
#include "stb_image/stb_image.h"
#define STB_IMAGE_WRITE_IMPLEMENTATION
#include "stb_image/stb_image_write.h"

#define MINI_PICTURE_SIZE 50
#define SOURCE_NUM 1000
#define RESIZE_FAC 10

using namespace std;

unsigned char* makeSquare(unsigned char*, int, int, int);
int preproccess(int num);
int mosaic();

string sourcePath = "./sources/";
string processedPath = "./processed/";
string inputPath = "./input/";
string outputPath = "./output.jpg";

int main(int argc, char** argv){
	
	preproccess(stoi(argv[1]));
	mosaic();
	
	return 0;
}

int preproccess(int num){
	
	int PHOTO_NUM = num;
	
	for (int photo = 1; photo <= PHOTO_NUM; photo++){
		
		string filename = "./sources/source_" + to_string(photo) + ".jpg";
		int width, height, channels;
		unsigned char *img = stbi_load(filename.c_str(), &width, &height, &channels, 0);
		
		if (img == NULL){
			cout << filename << " can't loaded!!" << endl;
			return 1;
		}
		
		cout << "Image has been loaded." << endl;
		cout << "Width: " << width << "\t\tHeight: " << height << "\t\tChannels: " << channels << endl;
		
		unsigned char *newImg = makeSquare(img, width, height, channels);
		int newWidth = min(width, height);
		int newHeight = newWidth;
		
		filename = "./psources/psource_" + to_string(photo) + ".jpg";
		stbi_write_jpg(filename.c_str(), newWidth, newHeight, channels, newImg, 0);
		stbi_image_free(img);
		stbi_image_free(newImg);
	}
	
	return 0;
}

unsigned char* makeSquare(unsigned char* img, int width, int height, int channels){
	int newWidth = min(width, height);
	int newHeight = newWidth;
	unsigned char *newImg = (unsigned char*) malloc(newWidth * newHeight * channels);
	
	for (int h = (height - newHeight)/2, n_h = 0; h < (height + newHeight)/2; h++, n_h++){
		for(int w = (width - newWidth)/2, n_w = 0; w < (width + newWidth)/2; w++, n_w++){
			unsigned char *img_p = img + h*width*channels + w*channels;
			unsigned char *nimg_p = newImg + n_h*newWidth*channels + n_w*channels;
			for(int c = 0; c < channels; c++){
				*(nimg_p + c) = *(img_p + c);
			}
		}
	}
	
	return newImg;
}

int mosaic(){
	
	int photoRGB[SOURCE_NUM][3];
	unsigned char *psources[SOURCE_NUM];
	for(int i = 0; i < SOURCE_NUM; i++)
		for(int j = 0; j < 3; j++)
			photoRGB[i][j] = 0;
	
	for (int photo = 0; photo < SOURCE_NUM; photo++){
		
		string filename = sourcePath + "source_" + to_string(photo + 1) + ".jpg";
		int width, height, channels;
		psources[photo] = stbi_load(filename.c_str(), &width, &height, &channels, 0);
		
		if (psources[photo] == NULL){
			cout << filename << " can't loaded!!" << endl;
			return 1;
		}
		
		cout << filename << " has been loaded." << endl;
		cout << "Width: " << width << "\t\tHeight: " << height << "\t\tChannels: " << channels << endl;
		
		for (int h = 0; h < height; h++){
			for(int w = 0; w < width; w++){
				unsigned char *img_p = psources[photo] + h*width*channels + w*channels;
				photoRGB[photo][0] += *img_p;
				photoRGB[photo][1] += *(img_p + 1);
				photoRGB[photo][2] += *(img_p + 2);
			}
		}
		
		photoRGB[photo][0] /= width * height;
		photoRGB[photo][1] /= width * height;
		photoRGB[photo][2] /= width * height;
		
		unsigned char *resizedPic = (unsigned char*) malloc(MINI_PICTURE_SIZE * MINI_PICTURE_SIZE * channels);
		stbir_resize_uint8(psources[photo], width, height, 0, resizedPic, MINI_PICTURE_SIZE, MINI_PICTURE_SIZE, 0, channels);
		
		stbi_image_free(psources[photo]);
		psources[photo] = resizedPic;
	}
	
	cout << "All psource images has been scanned!" << endl;
	
	int width, height, channels;
	unsigned char *img = stbi_load((inputPath + "input_img.jpg").c_str(), &width, &height, &channels, 0);
	
	if (img == NULL){
		cout << "Input image can't loaded!!" << endl;
		return 1;
	}
	
	cout << "Input image has been loaded." << endl;
	cout << "Width: " << width << "\t\tHeight: " << height << "\t\tChannels: " << channels << endl;
	
	int rHeight = (int) height * RESIZE_FAC;
	int rWidth = (int) width * RESIZE_FAC;
	unsigned char *resizedInp = (unsigned char*) malloc(rHeight * rWidth * channels);
	stbir_resize_uint8(img, width, height, 0, resizedInp, rWidth, rHeight, 0, channels);
	stbi_image_free(img);
	img = resizedInp;
	width = rWidth;
	height = rHeight;
	
	int pHeight, pWidth;
	if (height % MINI_PICTURE_SIZE == 0)
		pHeight = height/MINI_PICTURE_SIZE;
	else
		pWidth = height/MINI_PICTURE_SIZE + 1;
	if (width % MINI_PICTURE_SIZE == 0)
		pWidth = width/MINI_PICTURE_SIZE;
	else
		pWidth = width/MINI_PICTURE_SIZE + 1;
	int picture[pHeight][pWidth][channels + 1];	
	int photos[pHeight][pWidth];	
	
	for (int h = 0; h < pHeight; h++){
		for(int w = 0; w < pWidth; w++){
			for (int c = 0; c < channels + 1; c++){
				picture[h][w][c] = 0;
			}
		}
	}
	
	cout << "picture array initialized" << endl;
	
	for (int h = 0; h < height; h++){
		for(int w = 0; w < width; w++){
			unsigned char *img_p = img + h*width*channels + w*channels;
			int p_h = h/MINI_PICTURE_SIZE;
			int p_w = w/MINI_PICTURE_SIZE;
			for (int c = 0; c < channels; c++){
				picture[p_h][p_w][c] += *(img_p + c);
			}
			picture[p_h][p_w][channels]++;
		}
	}
	
	for (int h = 0; h < pHeight; h++){
		for(int w = 0; w < pWidth; w++){
			for (int c = 0; c < channels; c++){
				picture[h][w][c] /= picture[h][w][channels];
			}
		}
	}
	
	cout << "\nInput picture pixelled" << endl;
	
	for (int h = 0; h < pHeight; h++){
		for (int w = 0; w < pWidth; w++){
			double minDist = 1e9;
			for (int ph = 0; ph < SOURCE_NUM; ph++){
				double dist = 0;
				dist += (picture[h][w][0] - photoRGB[ph][0]) * (picture[h][w][0] - photoRGB[ph][0]);
				dist += (picture[h][w][1] - photoRGB[ph][1]) * (picture[h][w][1] - photoRGB[ph][1]);
				dist += (picture[h][w][2] - photoRGB[ph][2]) * (picture[h][w][2] - photoRGB[ph][2]);
				dist = sqrt(dist);
				if (dist < minDist){
					minDist = dist;
					photos[h][w] = ph;
				}
			}
		}
	}
	
	for (int h = 0; h < height; h++){
		for(int w = 0; w < width; w++){
			unsigned char *img_p = img + h*width*channels + w*channels;
			int p_h = h / MINI_PICTURE_SIZE;
			int p_w = w / MINI_PICTURE_SIZE;
			int p_hh = h % MINI_PICTURE_SIZE;
			int p_ww = w % MINI_PICTURE_SIZE;
			unsigned char *nimg_p = psources[photos[p_h][p_w]] + p_hh * MINI_PICTURE_SIZE * channels + p_ww * channels;
			for (int c = 0; c < channels; c++){
				*(img_p + c) = *(nimg_p + c);
			}
		}
	}
	
	cout << "Small pictures placed!" << endl;
	
	stbi_write_jpg(outputPath.c_str(), width, height, channels, img, 0);
	
	cout << "Output image printed" << endl;
}
