import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/*
 * Converts image into ascii art, with contrast boost and edge detection (Sobel operator).
 * Doesn't work on transparent images.
 */
public class AsciiArtGenerator {
    private static String DEMO_FILE_NAME = "C:\\Users\\Name\\Desktop\\torus.png";
    
    // ART ADJUSTMENT FIELDS
    private static int ART_CHAR_WIDTH = 160;    // character width of the resulting ascii art
    private static double ART_CONTRAST = 10;    // how much to boost the contrast, 0 = no boost
    
    // EDGE ADJUSTMENT FIELDS
    private static boolean FILL_ART = true;    // whether the art should be filled   
    // threshold for what's considered an edge (0-1), 1 is weakest edge detection
    private static double EDGE_THRESHOLD = 0.6;    //recommend: 0.6 for fill, 0.2 for no fill
       
    // ASCII ART DISPLAY FIELDS
    private static String ASCII_GRADIENT = "@$B%8&WM#oahkbdpq*wmZO0QLCJUYXzcvunxrjft/\\|()1{}[]?+~I<>i!l-_;:,\"^`'.` ";
    // used to adjust the grid used to process the image, because characters printed to console aren't square
    private static double CHAR_HEIGHT_WIDTH_RATIO = 2.3;   
    
    // SOBEL OPERATOR KERNELS
    private static int[][] SOBEL_X_KERNEL = 
        {
            {-1, 0, 1},
            {-2, 0, 2},
            {-1, 0, 1},         
        };
    private static int[][] SOBEL_Y_KERNEL = 
        {
            {-1, -2, -1},
            {0, 0, 0},
            {1, 2, 1},         
        };
    
    // MAIN ------------------------------------------------------------------------------------------------------------
    public static void main(String[] args) {
        BufferedImage img = getImageFromFileName(DEMO_FILE_NAME);            
        printImageAsAsciiArt(img);
    }
    
    // IMAGE PROCESSING METHODS ----------------------------------------------------------------------------------------
    
    // prints the image to console, as ascii art
    private static void printImageAsAsciiArt(BufferedImage img){
        int imgPixelWidth = img.getWidth();
        int imgPixelHeight = img.getHeight();     
        
        // width of one grid square in the image
        double tilePixelWidth = ((double) imgPixelWidth)/ART_CHAR_WIDTH;
        double tilePixelHeight = tilePixelWidth * CHAR_HEIGHT_WIDTH_RATIO;
        
        // iterate over the image as a grid, printing one ascii char at a time
        for(int row = 0; ((row+1)*tilePixelHeight) < imgPixelHeight; row++) {
            for(int col = 0; ((col+1)*tilePixelWidth) < imgPixelWidth; col++) {
                // current x position (pixels) in the picture (right, down)
                int currX = (int)(col*tilePixelWidth);
                int currY = (int)(row*tilePixelHeight);  
                                
                double[][] greyMatrix = getGreyMatrix(img, currX, currY, tilePixelWidth, tilePixelHeight);
                char asciiSymbol = getAsciiCharFromGreyMatrix(greyMatrix);
                System.out.print(asciiSymbol);              
            }
            System.out.println();
        }      
    }
    
    // ASCII CONVERSION METHODS ----------------------------------------------------------------------------------------
    
    // returns ascii symbol representing given 3x3 grey matrix, including edge detection
    private static char getAsciiCharFromGreyMatrix(double[][] greyMatrix) {
        validateGreyMatrix(greyMatrix);
        
        char result = getEdgeChar(greyMatrix);
        
        if(result == ' ' && FILL_ART) { // ie if no edge
            result = getAsciiCharFromGrey(greyMatrix[1][1]);
        }
        
        return result;
    }
    
    // returns the ascii character representing the given grey value (0 <= grey < 1)
    private static char getAsciiCharFromGrey(double grey) {
        int shadeIndex = (int) (grey * ASCII_GRADIENT.length());    
        
        // get shadeIndex-th character from the ascii gradient
        char result = ASCII_GRADIENT.charAt(shadeIndex);
        return result;
    }
    
    // returns 3x3 matrix with grey values based the specified grid square in the image
    private static double[][] getGreyMatrix(BufferedImage img, int startX, int startY, double tileWidth, double tileHeight){
        // Use pixels from start, halfway through, and at end of tile (in x and y direction)
        double subTileWidth = tileWidth/2;  
        double subTileHeight = tileHeight/2;
        
        double[][] greyMatrix = new double[3][3];
        
        for(int row = 0; row <3; row++) {
            for(int col = 0; col < 3; col++) {
                 int rgb = img.getRGB((int)(startX + row*subTileWidth), (int)(startY + col*subTileHeight));                 
                 double grey = getGreyFromRgb(rgb);
                 greyMatrix[row][col] = grey;
            }
        }
        
        return greyMatrix;
    }
    
    private static void validateGreyMatrix(double[][] greyMatrix) {
        // may also want to check if values are 0-1
        if(greyMatrix.length != 3 || greyMatrix[0].length != 3) {
            throw new IllegalArgumentException("Given grey matrix is not 3x3.");
        }
    }
    
    // EDGE DETECTION METHODS ------------------------------------------------------------------------------------------
    
    // returns the edge character representing this 3x3 grey matrix; returns ' ' (space) if no edge detected
    private static char getEdgeChar(double[][] greyMatrix) {
        validateGreyMatrix(greyMatrix);
        
        // if matrix is too dark, don't create edge
        if(greyMatrix[1][1] <0.2) {
            return ' ';
        }
        
        double sobelX = 0;
        double sobelY = 0;
        
        for(int row = 0; row <3; row++) {
            for(int col = 0; col < 3; col++) {
                 double grey = greyMatrix[row][col];                 
                 sobelX += grey * SOBEL_X_KERNEL[row][col];
                 sobelY += grey * SOBEL_Y_KERNEL[row][col];
            }
        }
        
        // divide by 5.66 to normalize magnitude (max edge magnitude is sqrt(32), or ~5.66)
        double edgeMagnitude = Math.abs(Math.sqrt(sobelX*sobelX + sobelY*sobelY)) / 5.66;
        double edgeOrientation = Math.atan(sobelY/sobelX);
        
        double straightThreshold = 1.1;
        double slightSlantThreshold = 0.8;
        double slantThreshold = 0.35;
        if(edgeMagnitude > EDGE_THRESHOLD) {
            double absEdgeOrientation = Math.abs(edgeOrientation);
            boolean isPositive = edgeOrientation >0;
            
            if(absEdgeOrientation > straightThreshold) {   // check straight
                return '|';
            } else if(absEdgeOrientation > slightSlantThreshold) {   // check slight slant
                return isPositive ? ')' : '(';                       
            } else if(absEdgeOrientation > slantThreshold) {
                return isPositive ? '/' : '\\';
            } else {
                return '-';
            }
        }
        
        return ' ';       
    }
    
    // GREY VALUE METHODS ----------------------------------------------------------------------------------------------
    
    // returns a double grey [0, 1) representing the given rgb value in greyscale. 0 is black, 0.999... is white
    // increases contrast if appropriate
    private static double getGreyFromRgb(int rgb) {
        Color color = new Color(rgb);
        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();
        
        // compute weighted average of rgb values, normalized
        double grey = (0.2989 * r + 0.5870 * g + 0.1140 * b)/255.0;     
        
        if(ART_CONTRAST != 0) {
            grey = increaseGreyContrast(grey);
        }
        return grey;
    }
    
    // takes a grey value [0, 1) and returns a grey value with increased contrast [0, 1), using sigmoid function
    private static double increaseGreyContrast(double grey) {
        double x = grey;
        
        // input x into sigmoid (s-curve) function, to make dark greys darker and light greys lighter
        double y = 0.5 + (-0.5/(Math.atan(ART_CONTRAST*(-0.5)))) * (Math.atan(ART_CONTRAST*(x - 0.5)));        
        return y;
    }    
    
    // IMAGE FILE METHODS ----------------------------------------------------------------------------------------------
    
    // Returns image from the given filename, but returns null if image was not found
    private static BufferedImage getImageFromFileName(String filename) {
        BufferedImage image = null;      
        try {
            image = ImageIO.read(new File(filename));   
        } catch(IOException e) {  
            System.out.println("ERROR: Image file not found.");
        }      
        return image;
    }  
}
