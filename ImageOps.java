//import std.StdIn;
//import std.StdOut;
//import std.StdDraw;

/**
 * A library of image editing functions.
 */
public class ImageOps {
	// Use these constants, as needed.
	static final int NUM_OF_COLORS = 3;
	static final int R = 0;
	static final int G = 1;
	static final int B = 2;
	static final int MAX_COLOR_VALUE = 255;
	

	public static void main(String[] args) {

		
		/* 
		* Test reading an image from a file
		* Reads image data from a file, into an array
		*/
		int[][][] pic = read("tinypic.ppm");
		// Displays the image matrix.
		showData(pic);
		// Displays the image
		show(pic);

	}	
	

	/**
	 * Reads an image in PPM format from the given filename.
	 * @param fileName - name of the given PPM file
	 * @return - the image, as a 3-dimensional array
	 */
	public static int[][][] read(String filename) {

		StdIn.setInput(filename);
		StdIn.readLine();			      // read the ppm header.
		int columns = StdIn.readInt(); 	   	      // number of columns in the ppm.
		int rows  = StdIn.readInt();                 // number of rows in the ppm.
		StdIn.readInt();			     //ignor the RGB scale header ("255").
		int[][][] pic = new int[rows][columns][3];

		//read the RGB data from the picture file and save it to array "pic". 
		for(int i = 0; i < rows; i++){
			for(int j =0; j < columns ; j++){
				for(int m = 0; m < 3; m++){
					pic[i][j][m] = StdIn.readInt();
				}
			}
		}
		return pic;
	}
	
	/**
	 * Prints the array values, nicely formatted. 
	 * @param pic - the image to display.
	 */
	public static void showData (int[][][] pic) {

		for(int i = 0; i < pic.length; i++){
			for(int j =0; j < pic[i].length ; j++){
				for(int m = 0; m < 3; m++){
					StdOut.printf("%3d" , pic[i][j][m]);
				}
				System.out.print(" ");
			}
			System.out.println();
		}
	}

	/**
	 * Renders an image, using StdDraw. 
	 * 
	 * @param pic - the image to render.
	 */
	public static void show(int[][][] pic) {
		StdDraw.setCanvasSize(pic[0].length, pic.length);
		int height = pic.length;
		int width = pic[0].length;
		StdDraw.setXscale(0, width);
		StdDraw.setYscale(0, height);
		StdDraw.show(30);
		for (int i = 0; i < height; i++){
			for (int j = 0; j < width; j++){
				StdDraw.setPenColor(pic[i][j][R], pic[i][j][G], pic[i][j][B] );
				StdDraw.filledRectangle(j + 0.5, height - i - 0.5, 0.5, 0.5);
			}
		}
		StdDraw.show();
	}
	
	/**
	 * Flips an image horizontally.
	 * SIDE EFFECT: Changes the given image.
	 * @param pic - the image to flip
	 */
	public static void flipHorizontally(int[][][] pic) 
	{
		//"swap" every two opposing pixels horizontelly 
		for(int i = 0; i < pic.length; i++){
			for(int j = 0; j < (pic[i].length)/2; j++){
				swap(pic, i, j, i, pic[i].length - j - 1);
			}
		}		
	}
		
	
	/**
	 * Flips an image vertically
	 * * SIDE EFFECT: Changes the given image.
	 * @param pic - the image to flip
	 */
	public static void flipVertically(int[][][] pic) {
	
		//"swap" every two opposing pixels vertically
		for(int i = 0; i < (pic.length)/2; i++){
			for(int j = 0; j < pic[i].length; j++){
				swap(pic, i, j, pic.length -i - 1,j);
			}
		}
	}

	// Swaps the two given pixels in the given image.
	// SIDE EFFECT: Changes the pixles in the given image.
	// i1,j1 - coordinates of the first pixel
	// i2,j2 - coordinates of the second pixel
	private static void swap(int[][][] pic, int i1, int j1, int i2, int j2) {
		
		// swap the values of the two pixels
		int[][][] tempArray = new int[1][1][3];
		
			tempArray[0][0] = pic[i1][j1];
			pic[i1][j1] = pic[i2][j2];
			pic[i2][j2] = tempArray[0][0];
	}
	
	/**
	 * Turns an RGB color into a greyScale value, using a luminance formula.
	 * The luminance is a weighted mean of the color's value, and is given by:
	 * 0.2126 * r + 0.7152 * b + 0.722 * G. (https://en.wikipedia.org/wiki/Relative_luminance)
	 * @param color - the color to be greyScaled.
	 * @return the greyscale value, as an array {greyscale, greyscale, greyscale}
	 */
	public static int[] luminance(int[] color) {
		
		double red  = 0.2126 * color[0];
		double green = 0.7152 * color[1];
		double blue = 0.722 * color[2];
		double lum = (red + green + blue);
		int i = 0;

		//set the pixel to new shade 
		while(i <3){
			color[i] = (int)lum;
			i++;
		}
		return color;
	}
	
	/**
	 * Creates a greyscaled version of an image.
	 * @param pic - the given image
	 * @return - the greyscaled version of the image
	 */
	public static int[][][] greyScaled(int[][][] pic) {
		
		int[][][] greyScale = new int[pic.length][pic[0].length][3];

		for(int i = 0; i < pic.length; i++){
			for(int j = 0; j < pic[i].length; j++){
				greyScale[i][j] = pic[i][j];
				luminance(greyScale[i][j]);
			}
		}
		return greyScale;
	}	
	
	/**
	 * Creates a blurred version of an image.
	 * Uses a block blur algorithm, (https://en.wikipedia.org/wiki/Gaussian_blur). 
	 * @param pic - the given image
	 * @return - the blurred version of the image
	 */
	public static int[][][] blurred(int[][][] pic) {

		int[][][] blurredPic = new int[pic.length][pic[0].length][3];
		for(int i = 1; i < pic.length; i++){
			for(int j = 1; j < pic[i].length; j++){
				for(int m = 0; m < 3; m++){
					blurredPic[i][j][m] = pic[i][j][m];
				}
			}
		}

		for(int i = 0; i < pic.length; i++){
			for(int j = 1; j < pic[i].length-1; j++){
				for(int m = 0; m < 3; m++){
				blurColor(pic, blurredPic, i , j , m);
				}
			}
		}
		return blurredPic;
	}

	/* 
	* Blurs a given color of a given pixel in a given image.
	* Stores the result in a blurred version of the given image, without effecting the given image.
	* Uses a block blur algorithm (https://en.wikipedia.org/wiki/Gaussian_blur).
	* @int[][][] pic - the given image
	* @int[][][] blurredPic - the blurred version of the given image
    	* @int row - the row of the pixel
	* @int col - the column of the pixel
	* @int color - the color to blur: 0-red, 1-green, 2-blue
	*/
	private static void blurColor(int[][][] pic, int[][][] blurredPic, int row, int col, int color) {
		
		double tempSum = 0;
		int counter = 0;

		//check if the pixel is in bounds.
		//add all the neighbouring colors within bounds to tempSum.
		//count how many neighboures were "found" that are not out of bounds.
		for(int i = row -1; i <= row + 1; i++){
			for(int j = col -1; j <= col + 1; j++){
				if (getColorIntensity(pic, i, j, color) != -1){
					tempSum +=  getColorIntensity(pic, i , j, color);
					counter += 1;
				}
			}	
		}
		int avarage = (int)(tempSum/counter);
		blurredPic[row][col][color] = avarage;
	}
	
	
	
	/* 
	* @param pic - the given source image
	* @param row - the given row of the pixel
	* @param col - the given column of the pixel
	* @param color - the given color: 0-red, 1-green, 2-blue
	* @return - the color intensity of a pixel, or -1 if the coordinates of the pixel are illegal..
	*/
	private static int getColorIntensity(int[][][] pic, int row, int col, int color) {

		if(row >=0  && row <= pic.length-1 && col >= 0 && col <= pic[row].length){
			int intensity = pic[row][col][color];
			return intensity;
		}
		return -1;
	}
	
	/**
	 * Calculates the horizontal gradient of the greyscaled version of an image
	 * 
	 * @param pic - the given image
	 * @return - the gradient of the greyscaled version of the given image.
	 */
	public static int[][] horizontalGradient(int[][][] pic) {
		// create a new 2d array the same size as pic (hight and width).
		int[][] gardient = new int[pic.length][pic[0].length];

		for(int i = 0; i < gardient.length; i++){
			for(int j = 1; j < gardient[i].length-1; j++){
					gardient[i][j] = pic[i][j+1][1] - pic[i][j-1][1];
			}
		}
		return gardient;
	}
	
	/**
	 * Normalizes a 2D array so that all the values are between 0 to 255
	 * SIDE EFFECT: Changes the given array
	 * 
	 * @param arr - the given array
	 */
	public static void normalize(int[][] arr) {
		
		int max = 0;
		int min =  255;
		// check what are the min and max values in the array and save them for later use.
		for(int i = 0; i < arr.length; i++){
			for(int j = 0; j < arr[i].length; j++){
				if(arr[i][j] > max){
					max = arr[i][j];
				}
				if(arr[i][j] < min){
					min = arr[i][j];
				}
			}
		}
		int diff =  max - min;
		//avoid deviding by zero when "max" and "min" are equal.
		if(max-min  == 0){
			diff = 1;
		}

		// normilize the array using "min" and "max".
		for(int i = 0; i < arr.length; i++){
			for(int j = 1; j < arr[i].length - 1; j++){
				arr[i][j] = max*(arr[i][j] - min)/diff ;
			}
		}
	}
	
	/**
	 * Creates a greyscaled image showing the horizontal edges of a given image.
	 * Uses gradient edge detection.
	 *  
	 * @param pic - the given image
	 * @return - a greyscaled image showing the horizontal edges of the given image
	 */
	public static int[][][] edges(int[][][] pic) {
	
		// create a grey scale image, gardient and normalize the picture.
		int[][][] edgesPic = greyScaled(pic);
		int[][] gardient = horizontalGradient(edgesPic);
		normalize(gardient);

		//set all of the array according to the normilized gardient (each pixel has the same values for R, G and B).
		for(int i = 0;  i < gardient.length; i++){
			for(int j = 0; j < gardient[i].length; j++){
				for(int m = 0 ; m < 3; m++){
					edgesPic[i][j][m] = gardient[i][j];
				}
			}
		}
		return edgesPic;
	}
}
