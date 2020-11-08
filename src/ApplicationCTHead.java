import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.io.*;

public class ApplicationCTHead extends javafx.application.Application {

    short cthead[][][]; //store the 3D volume data set
    short min, max; //min/max value in the 3D volume data set
    static int sliderCurr = 0;
    static int sliderCurr1 = 0;
    static int sliderCurr2 = 0;
    static WritableImage writableImageResize;
    static ImageView imageViewResize;

    @Override
    public void start(Stage stage) throws FileNotFoundException, IOException {
        stage.setTitle("CThead Viewer");

        ReadData();

        int width = 256;
        int height = 256;
        WritableImage medical_image = new WritableImage(width, height);
        ImageView imageView = new ImageView(medical_image);

        int width1 = 256;
        int height1 = 113;
        WritableImage medical_image1 = new WritableImage(width1, height1);
        ImageView imageView1 = new ImageView(medical_image1);

        int width2 = 256;
        int height2 = 113;
        WritableImage medical_image2 = new WritableImage(width2, height2);
        ImageView imageView2 = new ImageView(medical_image2);

        Button mip_button = new Button("MIP"); //an example button to switch to MIP mode
        Button mip_button1 = new Button("MIP");
        Button mip_button2 = new Button("MIP");
        Button thumbNail_button = new Button("TN");
        Button thumbNail_button1 = new Button("TN");
        Button thumbNail_button2 = new Button("TN");
        Button resize_button = new Button("Resize");
        Button resize_button1 = new Button("Resize");
        Button resize_button2 = new Button("Resize");

        //sliders to step through the slices (z and y directions) (remember 113 slices in z direction 0-112)
        Slider zslider = new Slider(0, 112, 0);
        Slider yslider1 = new Slider(0, 255, 0);
        Slider yslider2 = new Slider(0, 255, 0);

        mip_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                MIP(medical_image);
            }
        });

        mip_button1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                MIP1(medical_image1);
            }
        });

        mip_button2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                MIP2(medical_image2);

            }
        });

        thumbNail_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                FlowPane flowPane = new FlowPane();
                flowPane.setHgap(5);
                flowPane.setVgap(5);

                ScrollPane pane = new ScrollPane();
                pane.setContent(flowPane);
                pane.setFitToWidth(true);

                Scene secondScene = new Scene(pane, 950, 700);

                Stage newWindow = new Stage();
                newWindow.setTitle("Thumbnails");
                newWindow.setScene(secondScene);
                newWindow.show();

                for (int i = 0; i < 113; i++) {
                    WritableImage medical_image_Thu = new WritableImage(128, 128);
                    ImageView imageView_Th = new ImageView(medical_image_Thu);
                    flowPane.getChildren().add(i, imageView_Th);
                    ThumbNail(medical_image_Thu, i);
                    setThumbnailClick(imageView_Th, i);
                }
            }
        });

        thumbNail_button1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                FlowPane flowPane = new FlowPane();
                flowPane.setHgap(5);
                flowPane.setVgap(5);

                ScrollPane pane = new ScrollPane();
                pane.setContent(flowPane);
                pane.setFitToWidth(true);

                Scene secondScene = new Scene(pane, 950, 700);

                Stage newWindow = new Stage();
                newWindow.setTitle("Thumbnails");
                newWindow.setScene(secondScene);
                newWindow.show();

                for (int i = 0; i < 256; i++) {
                    WritableImage medical_image_Thu = new WritableImage(128, 58);
                    ImageView imageView_Th = new ImageView(medical_image_Thu);
                    flowPane.getChildren().add(i, imageView_Th);
                    ThumbNail1(medical_image_Thu, i);
                    setThumbnailClick1(imageView_Th, i);
                }
            }
        });

        thumbNail_button2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                FlowPane flowPane = new FlowPane();
                flowPane.setHgap(5);
                flowPane.setVgap(5);

                ScrollPane pane = new ScrollPane();
                pane.setContent(flowPane);
                pane.setFitToWidth(true);

                Scene secondScene = new Scene(pane, 950, 700);

                Stage newWindow = new Stage();
                newWindow.setTitle("Thumbnails");
                newWindow.setScene(secondScene);
                newWindow.show();

                for (int i = 0; i < 256; i++) {
                    WritableImage medical_image_Thu = new WritableImage(128, 58);
                    ImageView imageView_Th = new ImageView(medical_image_Thu);
                    flowPane.getChildren().add(i, imageView_Th);
                    ThumbNail2(medical_image_Thu, i);
                    setThumbnailClick2(imageView_Th, i);
                }
            }
        });

        resize_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                FlowPane flowPane = new FlowPane();
                flowPane.setHgap(5);
                flowPane.setVgap(5);

                Scene newWindow = new Scene(flowPane, 700, 430);

                Stage stage = new Stage();
                stage.setTitle("Resize");
                stage.setScene(newWindow);
                stage.show();

                WritableImage writableImage = new WritableImage(380, 380);
                imageViewResize = new ImageView(writableImage);

                Slider sliderResize = new Slider(0, 380, 0);

                flowPane.getChildren().addAll(sliderResize, imageViewResize);


                sliderResize.valueProperty().addListener(
                        new ChangeListener<Number>() {
                            public void changed(ObservableValue<? extends Number>
                                                        observable, Number oldValue, Number newValue) {
                                System.out.println(newValue.intValue());

                                writableImageResize = resizeBilinear(writableImage, newValue.intValue());
                                imageViewResize = new ImageView(writableImageResize);
                                flowPane.getChildren().set(1, imageViewResize);

                            }
                        });
            }
        });

        resize_button1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                FlowPane flowPane = new FlowPane();
                flowPane.setHgap(5);
                flowPane.setVgap(5);

                Scene newWindow = new Scene(flowPane, 700, 430);

                Stage stage = new Stage();
                stage.setTitle("Resize");
                stage.setScene(newWindow);
                stage.show();

                WritableImage writableImage = new WritableImage(512, 226);
                imageViewResize = new ImageView(writableImage);

                Slider sliderResize = new Slider(0, 512, 0);

                flowPane.getChildren().addAll(sliderResize, imageViewResize);


                sliderResize.valueProperty().addListener(
                        new ChangeListener<Number>() {
                            public void changed(ObservableValue<? extends Number>
                                                        observable, Number oldValue, Number newValue) {
                                System.out.println(newValue.intValue());

                                writableImageResize = resizeBilinear1(writableImage, newValue.intValue());
                                imageViewResize = new ImageView(writableImageResize);
                                flowPane.getChildren().set(1, imageViewResize);

                            }
                        });
            }
        });

        resize_button2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                FlowPane flowPane = new FlowPane();
                flowPane.setHgap(5);
                flowPane.setVgap(5);

                Scene newWindow = new Scene(flowPane, 700, 430);

                Stage stage = new Stage();
                stage.setTitle("Resize");
                stage.setScene(newWindow);
                stage.show();

                WritableImage writableImage = new WritableImage(512, 226);
                imageViewResize = new ImageView(writableImage);

                Slider sliderResize = new Slider(0, 512, 0);

                flowPane.getChildren().addAll(sliderResize, imageViewResize);


                sliderResize.valueProperty().addListener(
                        new ChangeListener<Number>() {
                            public void changed(ObservableValue<? extends Number>
                                                        observable, Number oldValue, Number newValue) {
                                System.out.println(newValue.intValue());

                                writableImageResize = resizeBilinear2(writableImage, newValue.intValue());
                                imageViewResize = new ImageView(writableImageResize);
                                flowPane.getChildren().set(1, imageViewResize);

                            }
                        });
            }
        });

        zslider.valueProperty().addListener(
                new ChangeListener<Number>() {
                    public void changed(ObservableValue<? extends Number>
                                                observable, Number oldValue, Number newValue) {
                        System.out.println(newValue.intValue());
                        sliderCurr = newValue.intValue();
                        getSlice(medical_image, newValue.intValue());
                    }
                });

        yslider1.valueProperty().addListener(
                new ChangeListener<Number>() {
                    public void changed(ObservableValue<? extends Number>
                                                observable, Number oldValue, Number newValue) {
                        System.out.println(newValue.intValue());
                        getSlice1(medical_image1, newValue.intValue());
                        sliderCurr1 = newValue.intValue();
                    }
                });

        yslider2.valueProperty().addListener(
                new ChangeListener<Number>() {
                    public void changed(ObservableValue<? extends Number>
                                                observable, Number oldValue, Number newValue) {
                        System.out.println(newValue.intValue());
                        getSlice2(medical_image2, newValue.intValue());
                        sliderCurr2 = newValue.intValue();
                    }
                });

        FlowPane root = new FlowPane();
        root.setVgap(8);
        root.setHgap(4);
//https://examples.javacodegeeks.com/desktop-java/javafx/scene/image-scene/javafx-image-example/

        root.getChildren().addAll(imageView, mip_button, zslider, resize_button, thumbNail_button);
        root.getChildren().addAll(imageView1, mip_button1, yslider1, resize_button1, thumbNail_button1);
        root.getChildren().addAll(imageView2, mip_button2, yslider2, resize_button2, thumbNail_button2);

        Scene scene = new Scene(root, 640, 500);
        stage.setScene(scene);
        stage.show();

    }

    //Function to read in the cthead data set
    public void ReadData() throws IOException {
        //File name is hardcoded here - much nicer to have a dialog to select it and capture the size from the user
        File file = new File("CThead.raw");
        //Read the data quickly via a buffer (in C++ you can just do a single fread - I couldn't find if there is an equivalent in Java)
        DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));

        int i, j, k; //loop through the 3D data set

        min = Short.MAX_VALUE;
        max = Short.MIN_VALUE; //set to extreme values
        short read; //value read in
        int b1, b2; //data is wrong Endian (check wikipedia) for Java so we need to swap the bytes around

        cthead = new short[113][256][256]; //allocate the memory - note this is fixed for this data set
        //loop through the data reading it in
        for (k = 0; k < 113; k++) {
            for (j = 0; j < 256; j++) {
                for (i = 0; i < 256; i++) {
                    //because the Endianess is wrong, it needs to be read byte at a time and swapped
                    b1 = ((int) in.readByte()) & 0xff; //the 0xff is because Java does not have unsigned types
                    b2 = ((int) in.readByte()) & 0xff; //the 0xff is because Java does not have unsigned types
                    read = (short) ((b2 << 8) | b1); //and swizzle the bytes around
                    if (read < min) min = read; //update the minimum
                    if (read > max) max = read; //update the maximum
                    cthead[k][j][i] = read; //put the short into memory (in C++ you can replace all this code with one fread)
                }
            }
        }
        System.out.println(min + " " + max); //diagnostic - for CThead this should be -1117, 2248
        //(i.e. there are 3366 levels of grey (we are trying to display on 256 levels of grey)
        //therefore histogram equalization would be a good thing
    }


    /*
       This function shows how to carry out an operation on an image.
       It obtains the dimensions of the image, and then loops through
       the image carrying out the copying of a slice of data into the
       image.
   */
    public void MIP(WritableImage image) {
        //Get image dimensions, and declare loop variables
        int w = (int) image.getWidth(), h = (int) image.getHeight(), i, j, c, k;
        PixelWriter image_writer = image.getPixelWriter();

        float col;
        short datum;

        short maximum = Short.MIN_VALUE;

        //Shows how to loop through each pixel and colour
        //Try to always use j for loops in y, and i for loops in x
        //as this makes the code more readable
        for (j = 0; j < h; j++) {
            for (i = 0; i < w; i++) {
                //at this point (i,j) is a single pixel in the image
                //here you would need to do something to (i,j) if the image size
                //does not match the slice size (e.g. during an image resizing operation
                //If you don't do this, your j,i could be outside the array bounds
                //In the framework, the image is 256x256 and the data set slices are 256x256
                //so I don't do anything - this also leaves3 you something to do for the assignment
                //datum=cthead[7][j][i]; //get values from slice 76 (change this in your assignment)
                //calculate the colour by performing a mapping from [min,max] -> [0,255]

                maximum = Short.MIN_VALUE;

                for (k = 0; k < 113; k++) {
                    maximum = (short) Math.max(cthead[k][j][i], maximum);
                }

                col = (((float) maximum - (float) min) / ((float) (max - min)));
                for (c = 0; c < 3; c++) {
                    //and now we are looping through the bgr components of the pixel
                    //set the colour component c of pixel (i,j)
                    image_writer.setColor(i, j, Color.color(col, col, col, 1.0));
                    //					data[c+3*i+3*j*w]=(byte) col;
                } // colour loop
            } // column loop
        } // row loop
    }


    public void MIP1(WritableImage image) {

        int w = (int) image.getWidth(), h = (int) image.getHeight(), i, j, c, k;
        PixelWriter image_writer = image.getPixelWriter();

        float col;
        short maximum;

        for (j = 0; j < h; j++) {
            for (i = 0; i < w; i++) {

                maximum = Short.MIN_VALUE;

                for (k = 0; k < 256; k++) {
                    maximum = (short) Math.max(cthead[j][k][i], maximum);
                }

                col = (((float) maximum - (float) min) / ((float) (max - min)));
                for (c = 0; c < 3; c++) {

                    image_writer.setColor(i, j, Color.color(col, col, col, 1.0));
                }
            }
        }
    }


    public void MIP2(WritableImage image) {
        int w = (int) image.getWidth(), h = (int) image.getHeight(), i, j, c, k;
        PixelWriter image_writer = image.getPixelWriter();

        float col;
        short maximum;

        for (j = 0; j < h; j++) {
            for (i = 0; i < w; i++) {

                maximum = Short.MIN_VALUE;

                for (k = 0; k < 256; k++) {
                    maximum = (short) Math.max(cthead[j][i][k], maximum);
                }

                col = (((float) maximum - (float) min) / ((float) (max - min)));
                for (c = 0; c < 3; c++) {

                    image_writer.setColor(i, j, Color.color(col, col, col, 1.0));
                }
            }
        }
    }


    public void getSlice(WritableImage image, int value) {

        int w = (int) image.getWidth(), h = (int) image.getHeight(), i, j, c, k;
        PixelWriter image_writer = image.getPixelWriter();

        float col;
        short datum;

        for (j = 0; j < h; j++) {
            for (i = 0; i < w; i++) {

                datum = cthead[value][j][i];

                //calculate the colour by performing a mapping from [min,max] -> [0,255]
                col = (((float) datum - (float) min) / ((float) (max - min)));
                for (c = 0; c < 3; c++) {
                    //and now we are looping through the bgr components of the pixel
                    //set the colour component c of pixel (i,j)
                    image_writer.setColor(i, j, Color.color(col, col, col, 1.0));
                    //					data[c+3*i+3*j*w]=(byte) col;
                } // colour loop
            } // column loop
        } // row loop
    }


    public void getSlice1(WritableImage image, int value) {
        //Get image dimensions, and declare loop variables
        int w = (int) image.getWidth(), h = (int) image.getHeight(), i, j, c, k;
        PixelWriter image_writer = image.getPixelWriter();

        float col;
        short datum;
        //Shows how to loop through each pixel and colour
        //Try to always use j for loops in y, and i for loops in x
        //as this makes the code more readable
        for (j = 0; j < h; j++) {
            for (i = 0; i < w; i++) {
                //at this point (i,j) is a single pixel in the image
                //here you would need to do something to (i,j) if the image size
                //does not match the slice size (e.g. during an image resizing operation
                //If you don't do this, your j,i could be outside the array bounds
                //In the framework, the image is 256x256 and the data set slices are 256x256
                //so I don't do anything - this also leaves you something to do for the assignment
                datum = cthead[j][value][i]; //get values from slice 76 (change this in your assignment)
                //calculate the colour by performing a mapping from [min,max] -> [0,255]
                col = (((float) datum - (float) min) / ((float) (max - min)));
                for (c = 0; c < 3; c++) {
                    //and now we are looping through the bgr components of the pixel
                    //set the colour component c of pixel (i,j)
                    image_writer.setColor(i, j, Color.color(col, col, col, 1.0));
                    //					data[c+3*i+3*j*w]=(byte) col;
                } // colour loop
            } // column loop
        } // row loop
    }

    public void getSlice2(WritableImage image, int value) {
        //Get image dimensions, and declare loop variables
        int w = (int) image.getWidth(), h = (int) image.getHeight(), i, j, c, k;
        PixelWriter image_writer = image.getPixelWriter();

        float col;
        short datum;
        //Shows how to loop through each pixel and colour
        //Try to always use j for loops in y, and i for loops in x
        //as this makes the code more readable
        for (j = 0; j < h; j++) {
            for (i = 0; i < w; i++) {
                //at this point (i,j) is a single pixel in the image
                //here you would need to do something to (i,j) if the image size
                //does not match the slice size (e.g. during an image resizing operation
                //If you don't do this, your j,i could be outside the array bounds
                //In the framework, the image is 256x256 and the data set slices are 256x256
                //so I don't do anything - this also leaves you something to do for the assignment
                datum = cthead[j][i][value]; //get values from slice 76 (change this in your assignment)
                //calculate the colour by performing a mapping from [min,max] -> [0,255]
                col = (((float) datum - (float) min) / ((float) (max - min)));
                for (c = 0; c < 3; c++) {
                    //and now we are looping through the bgr components of the pixel
                    //set the colour component c of pixel (i,j)
                    image_writer.setColor(i, j, Color.color(col, col, col, 1.0));
                    //					data[c+3*i+3*j*w]=(byte) col;
                } // colour loop
            } // column loop
        } // row loop
    }

    public void ThumbNail(WritableImage image, int volume) {

        int w = (int) image.getWidth(), h = (int) image.getHeight(), i, j, c, k;
        PixelWriter image_writer = image.getPixelWriter();

        float col;
        short datum;

        for (j = 0; j < 128; j++) {
            for (i = 0; i < 128; i++) {

                double y = j * 256 / 128;
                double x = i * 256 / 128;

                datum = cthead[volume][(int) y][(int) x];
                col = (((float) datum - (float) min) / ((float) (max - min)));

                for (c = 0; c < 3; c++) {
                    image_writer.setColor(i, j, Color.color(col, col, col, 1.0));
                }
            }
        }
    }

    public void ThumbNail1(WritableImage image, int volume) {

        int w = (int) image.getWidth(), h = (int) image.getHeight(), i, j, c, k;
        PixelWriter image_writer = image.getPixelWriter();

        float col;
        short datum;

        for (j = 0; j < 58; j++) {
            for (i = 0; i < 128; i++) {

                double x = i * 256 / 128;
                double y = j * 113 / 58;

                datum = cthead[(int) y][volume][(int) x];
                col = (((float) datum - (float) min) / ((float) (max - min)));

                for (c = 0; c < 3; c++) {
                    image_writer.setColor(i, j, Color.color(col, col, col, 1.0));
                }
            }
        }
    }


    public void ThumbNail2(WritableImage image, int volume) {

        int w = (int) image.getWidth(), h = (int) image.getHeight(), i, j, c, k;
        PixelWriter image_writer = image.getPixelWriter();

        float col;
        short datum;

        for (j = 0; j < 58; j++) {
            for (i = 0; i < 128; i++) {

                double y = j * 113 / 58;
                double x = i * 256 / 128;

                datum = cthead[(int) y][(int) x][volume];
                col = (((float) datum - (float) min) / ((float) (max - min)));

                for (c = 0; c < 3; c++) {
                    image_writer.setColor(i, j, Color.color(col, col, col, 1.0));
                }
            }
        }
    }

    public WritableImage resizeBilinear(WritableImage image, int size) {
        int w = (int) image.getWidth(), h = (int) image.getHeight(), i, j, c, k;
        WritableImage writableImage = new WritableImage(380, 380);
        PixelWriter pixelWriter = writableImage.getPixelWriter();

        float col;

        for (j = 0; j < size; j++) {
            for (i = 0; i < size; i++) {

                float x = i * 255 / size;
                float y = j * 255 / size;

                double x1 = Math.floor(x);
                double y1 = Math.floor(y);
                double x2 = x1 + 1;
                double y2 = y1 + 1;

                short v1 = cthead[sliderCurr][(int) y2][(int) x1];
                short v2 = cthead[sliderCurr][(int) y2][(int) x2];
                short v3 = cthead[sliderCurr][(int) y1][(int) x1];
                short v4 = cthead[sliderCurr][(int) y1][(int) x2];

                double v = (v1 + ((v2 - v1) * ((x - x1) / (x2 - x1))));
                double vv = (v3 + ((v4 - v3) * ((x - x1) / (x2 - x1))));
                double vxy = (vv + ((v - vv) * ((y - y1) / (y2 - y1))));

                col = (((float) vxy - (float) min) / ((float) (max - min)));

                for (c = 0; c < 3; c++) {
                    pixelWriter.setColor(i, j, Color.color(col, col, col, 1.0));
                }
            }
        }
        return writableImage;
    }

    public WritableImage resizeBilinear1(WritableImage image, int size) {
        int w = (int) image.getWidth(), h = (int) image.getHeight(), i, j, c, k;
        WritableImage writableImage = new WritableImage(512, 226);
        PixelWriter pixelWriter = writableImage.getPixelWriter();

        float col;

        for (j = 0; j < (0.441 * size); j++) {
            for (i = 0; i < size; i++) {

                float x = i * 255 / size;
                float y = j * 112 / (float) (0.441 * size);

                double x1 = Math.floor(x);
                double y1 = Math.floor(y);
                double x2 = x1 + 1;
                double y2 = y1 + 1;

                short v1 = cthead[(int) y2][sliderCurr1][(int) x1];
                short v2 = cthead[(int) y2][sliderCurr1][(int) x2];
                short v3 = cthead[(int) y1][sliderCurr1][(int) x1];
                short v4 = cthead[(int) y1][sliderCurr1][(int) x2];

                double v = (v1 + ((v2 - v1) * ((x - x1) / (x2 - x1))));
                double vv = (v3 + ((v4 - v3) * ((x - x1) / (x2 - x1))));
                double vxy = (vv + ((v - vv) * ((y - y1) / (y2 - y1))));

                col = (((float) vxy - (float) min) / ((float) (max - min)));

                for (c = 0; c < 3; c++) {
                    pixelWriter.setColor(i, j, Color.color(col, col, col, 1.0));
                }
            }
        }
        return writableImage;
    }

    public WritableImage resizeBilinear2(WritableImage image, int size) {
        int w = (int) image.getWidth(), h = (int) image.getHeight(), i, j, c, k;
        WritableImage writableImage = new WritableImage(512, 226);
        PixelWriter pixelWriter = writableImage.getPixelWriter();

        float col;

        for (j = 0; j < (0.441 * size); j++) {
            for (i = 0; i < size; i++) {

                float x = i * 255 / size;
                float y = j * 112 / (float) (0.441 * size);

                double x1 = Math.floor(x);
                double y1 = Math.floor(y);
                double x2 = x1 + 1;
                double y2 = y1 + 1;

                short v1 = cthead[(int) y2][(int) x1][sliderCurr2];
                short v2 = cthead[(int) y2][(int) x2][sliderCurr2];
                short v3 = cthead[(int) y1][(int) x1][sliderCurr2];
                short v4 = cthead[(int) y1][(int) x2][sliderCurr2];

                double v = (v1 + ((v2 - v1) * ((x - x1) / (x2 - x1))));
                double vv = (v3 + ((v4 - v3) * ((x - x1) / (x2 - x1))));
                double vxy = (vv + ((v - vv) * ((y - y1) / (y2 - y1))));

                col = (((float) vxy - (float) min) / ((float) (max - min)));

                for (c = 0; c < 3; c++) {
                    pixelWriter.setColor(i, j, Color.color(col, col, col, 1.0));
                }
            }
        }
        return writableImage;
    }

    public void setThumbnailClick(ImageView image, int i) {
        image.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                WritableImage image = new WritableImage(256, 256);
                ImageView imageView = new ImageView(image);
                getSlice(image, i);
                FlowPane flowPane = new FlowPane();
                flowPane.getChildren().add(imageView);
                Scene scene = new Scene(flowPane, 256, 256);
                Stage s = new Stage();
                s.setScene(scene);
                s.show();
            }
        });
    }

    public void setThumbnailClick1(ImageView image, int i) {
        image.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                WritableImage image = new WritableImage(256, 113);
                ImageView imageView = new ImageView(image);
                getSlice1(image, i);
                FlowPane flowPane = new FlowPane();
                flowPane.getChildren().add(imageView);
                Scene scene = new Scene(flowPane, 256, 113);
                Stage s = new Stage();
                s.setScene(scene);
                s.show();
            }
        });
    }

    public void setThumbnailClick2(ImageView image, int i) {
        image.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                WritableImage image = new WritableImage(256, 113);
                ImageView imageView = new ImageView(image);
                getSlice2(image, i);
                FlowPane flowPane = new FlowPane();
                flowPane.getChildren().add(imageView);
                Scene scene = new Scene(flowPane, 256, 113);
                Stage s = new Stage();
                s.setScene(scene);
                s.show();
            }
        });
    }

    public static void main(String[] args) {
        launch();
    }

}
