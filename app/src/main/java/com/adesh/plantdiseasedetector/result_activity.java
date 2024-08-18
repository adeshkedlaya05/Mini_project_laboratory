package com.adesh.plantdiseasedetector;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.tensorflow.lite.Interpreter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;

public class result_activity extends Activity {

    private static final String TAG = "ResultActivity";
    private ImageView resultImage; // ImageView to display the result image
    private TextView resultTextView, precautions; // TextViews for result and precautions
    private Interpreter tflite; // TensorFlow Lite Interpreter
    private Button download; // Button to trigger PDF download

    // Class labels for plant diseases
    private static final String[] CLASS_LABELS = {
            "Healthy",
            "Cedar_Apple",
            "Black Rot",
            "Apple Scab"
    };

    // Precautions corresponding to each class label
    private static final String[] PRECAUTIONS = {
            "Your plant is healthy.",
            "• Remove infected plant parts.\n• Apply fungicides in early spring to prevent spread.\n• Use fungicides like 'Mancozeb' or 'Myclobutanil'.",
            "• Destroy infected fruits and leaves.\n• Prune affected branches to reduce disease spread.\n• Use fungicides such as 'Captan' or 'Chlorothalonil'.",
            "• Use fungicides to manage and prevent apple scab.\n• Remove fallen leaves to prevent reinfection.\n• Recommended fungicides include 'Dithane M-45' and 'Mancozeb'."
    };

    // Confidence threshold for classifying predictions
    private static final float CONFIDENCE_THRESHOLD = 0.5f; // Adjust as needed

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result); // Set the layout for this activity

        // Initialize UI components
        resultImage = findViewById(R.id.imageViewresultw);
        resultTextView = findViewById(R.id.resultTextView);
        precautions = findViewById(R.id.textViewprecautions);
        download = findViewById(R.id.button_download);

        // Set click listener for download button
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generateAndSavePdf(); // Generate and save the PDF when the button is clicked
            }
        });

        // Load TensorFlow Lite model
        try {
            tflite = new Interpreter(loadModelFile("model_unquant.tflite")); // Load the model from assets
        } catch (IOException e) {
            Log.e(TAG, "Error loading TensorFlow Lite model", e);
            resultTextView.setText("Failed to load model");
            return;
        }

        // Get the image URI from the Intent
        String imageUriString = getIntent().getStringExtra("imageUri");
        if (imageUriString != null) {
            Uri imageUri = Uri.parse(imageUriString);
            resultImage.setImageURI(imageUri); // Set the image URI to the ImageView
            processImage(imageUri); // Process the image for prediction
        } else {
            Log.e(TAG, "Image URI is null");
            resultTextView.setText("No image received");
        }
    }

    private MappedByteBuffer loadModelFile(String modelFilename) throws IOException {
        // Load the model file from assets
        try (FileInputStream fileInputStream = new FileInputStream(getAssets().openFd(modelFilename).getFileDescriptor());
             FileChannel fileChannel = fileInputStream.getChannel()) {
            long startOffset = getAssets().openFd(modelFilename).getStartOffset();
            long declaredLength = getAssets().openFd(modelFilename).getDeclaredLength();
            return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength); // Map the model file to memory
        }
    }

    @SuppressLint("SetTextI18n")
    private void processImage(Uri imageUri) {
        // Process the image for model inference
        try (InputStream imageStream = getContentResolver().openInputStream(imageUri)) {
            if (imageStream != null) {
                Bitmap bitmap = BitmapFactory.decodeStream(imageStream); // Decode the image from the URI
                Bitmap processedBitmap = preprocessImage(bitmap); // Preprocess the image for model input
                float[] prediction = classifyImage(processedBitmap); // Classify the image using TensorFlow Lite model
                Log.d(TAG, "Processed Bitmap Dimensions: " + processedBitmap.getWidth() + "x" + processedBitmap.getHeight());
                Log.d(TAG, "Prediction Output: " + Arrays.toString(prediction));
                String result = getPredictionLabel(prediction); // Get the prediction label
                resultTextView.setText("Prediction: " + result);
                precautions.setText(getPrecautions(result)); // Set precautions text
                if (result.equals("No valid class detected")) {
                    // If no valid class detected, show a toast and redirect to MainActivity
                    Toast.makeText(this, "Upload a valid image", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    finish(); // Close the current activity
                }
            } else {
                Log.e(TAG, "Failed to open image stream");
                resultTextView.setText("Failed to process image");
            }
        } catch (Exception e) {
            Log.e(TAG, "Error processing image", e);
            resultTextView.setText("Error processing image");
        }
    }

    private Bitmap preprocessImage(Bitmap bitmap) {
        // Resize the image to match model input dimensions
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, 224, 224, true);
        return resizedBitmap;
    }

    private float[] classifyImage(Bitmap bitmap) {
        if (tflite == null) {
            Log.e(TAG, "tflite is not initialized");
            return new float[0];
        }

        // Convert the bitmap to a 4D array of floats
        float[][][][] input = new float[1][224][224][3];
        for (int i = 0; i < 224; i++) {
            for (int j = 0; j < 224; j++) {
                int pixel = bitmap.getPixel(i, j);
                input[0][i][j][0] = (pixel >> 16 & 0xFF) / 255.0f; // Red channel
                input[0][i][j][1] = (pixel >> 8 & 0xFF) / 255.0f;  // Green channel
                input[0][i][j][2] = (pixel & 0xFF) / 255.0f;       // Blue channel
            }
        }

        // Update output tensor shape to match model's output
        float[][] output = new float[1][4]; // Adjust size based on model output
        try {
            tflite.run(input, output); // Run model inference
            Log.d(TAG, "Model output: " + Arrays.toString(output[0]));
        } catch (Exception e) {
            Log.e(TAG, "Error running model inference", e);
        }
        return output[0];
    }

    private String getPredictionLabel(float[] prediction) {
        if (prediction == null || prediction.length == 0) {
            return "No prediction";
        }

        // Find the index of the maximum value
        int maxIndex = 0;
        float maxValue = prediction[0];
        for (int i = 1; i < prediction.length; i++) {
            if (prediction[i] > maxValue) {
                maxValue = prediction[i];
                maxIndex = i;
            }
        }

        // Check if the maximum value is below the threshold
        if (maxValue < CONFIDENCE_THRESHOLD) {
            return "No valid class detected";
        }

        // Return the corresponding class label if valid
        if (maxIndex < CLASS_LABELS.length) {
            return CLASS_LABELS[maxIndex];
        } else {
            return "Invalid image";
        }
    }

    private String getPrecautions(String result) {
        for (int i = 0; i < CLASS_LABELS.length; i++) {
            if (result.equals(CLASS_LABELS[i])) {
                return PRECAUTIONS[i];
            }
        }
        return "";
    }

    private void generateAndSavePdf() {
        PdfDocument pdfDocument = new PdfDocument(); // Create a new PDF document

        // Create a page description
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(595, 842, 1).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo); // Start a new page

        // Draw content on the page
        Canvas canvas = page.getCanvas(); // Get the canvas to draw on
        Paint paint = new Paint(); // Create a Paint object for drawing
        paint.setTextSize(16); // Set text size
        paint.setAntiAlias(true); // Enable anti-aliasing

        // Draw border around the page
        paint.setStyle(Paint.Style.STROKE); // Set paint style to stroke
        paint.setStrokeWidth(5); // Set stroke width
        canvas.drawRect(20, 20, pageInfo.getPageWidth() - 20, pageInfo.getPageHeight() - 20, paint); // Draw the border

        // Draw title
        paint.setStyle(Paint.Style.FILL); // Set paint style to fill
        paint.setStrokeWidth(0); // Reset stroke width
        canvas.drawText("Plant Disease Detection Report", 40, 50, paint); // Draw title text

        // Draw image with border
        Bitmap bitmap = ((BitmapDrawable) resultImage.getDrawable()).getBitmap(); // Get the bitmap from ImageView
        int imageLeft = 40;
        int imageTop = 70;
        int imageWidth = 515;
        int imageHeight = 300;
        canvas.drawRect(imageLeft - 2, imageTop - 2, imageLeft + imageWidth + 2, imageTop + imageHeight + 2, paint); // Draw border around the image
        canvas.drawBitmap(Bitmap.createScaledBitmap(bitmap, imageWidth, imageHeight, true), imageLeft, imageTop, paint); // Draw the image

        // Draw prediction result
        String result = resultTextView.getText().toString();
        paint.setTextSize(18); // Set text size for result
        canvas.drawText("Prediction: " + result, 40, imageTop + imageHeight + 40, paint); // Draw result text

        // Draw precautions
        String precautionsText = precautions.getText().toString();
        paint.setTextSize(16); // Set text size for precautions
        int yPos = imageTop + imageHeight + 60; // Position for the precautions
        canvas.drawText("Precautions:", 40, yPos, paint); // Draw "Precautions:" text
        String[] precautionsLines = precautionsText.split("\n"); // Split precautions text into lines
        for (String line : precautionsLines) {
            yPos += 20; // Move down for each line
            canvas.drawText("• " + line, 40, yPos, paint); // Draw each line of precautions
        }

        // Finish the page
        pdfDocument.finishPage(page); // End the current page

        // Save PDF to public Documents directory
        savePdfDocument(pdfDocument);
    }

    private void savePdfDocument(PdfDocument pdfDocument) {
        // Define the file name and path
        String fileName = "PlantDiseaseReport_" + System.currentTimeMillis() + ".pdf";

        // Use the MediaStore API for saving to public storage
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName); // Set file display name
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf"); // Set MIME type
        contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOCUMENTS + "/PlantDiseaseReports"); // Set file path

        // Insert the file into MediaStore
        Uri uri = getContentResolver().insert(MediaStore.Files.getContentUri("external"), contentValues);
        if (uri != null) {
            try (FileOutputStream outputStream = (FileOutputStream) getContentResolver().openOutputStream(uri)) {
                pdfDocument.writeTo(outputStream); // Write the PDF content to output stream
                pdfDocument.close(); // Close the PDF document
                Toast.makeText(this, "PDF saved: " + uri.toString(), Toast.LENGTH_LONG).show(); // Show success message
            } catch (IOException e) {
                Log.e(TAG, "Error saving PDF document", e);
                Toast.makeText(this, "Error saving PDF", Toast.LENGTH_SHORT).show(); // Show error message
            }
        }
    }
}
