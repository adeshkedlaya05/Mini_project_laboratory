  <p align="center">
  <img src="https://github.com/user-attachments/assets/5b3eaef2-8ec0-4c00-83af-5b3201034f87" alt="![Screenshot 2024-08-19 004159]" width="200" height="200">
</p>

## Plant Disease Detector

*Plant Disease Detector* is an Android application developed to help farmers and gardening enthusiasts identify plant diseases through image recognition. The app leverages machine learning models trained with TensorFlow Lite to diagnose diseases based on images captured by the device's camera or uploaded from the gallery. The application then provides remedies and treatment suggestions, which can be downloaded as a PDF for offline access.

## Features

- *Image-Based Disease Detection:* Identify plant diseases by capturing an image or uploading it from your gallery.
- *Machine Learning Integration:* Uses TensorFlow Lite models trained with Teachable Machine for accurate disease diagnosis.
- *Detailed Results:* Provides the name of the disease, a brief description, and suggested remedies and treatments.
- *PDF Report Generation:* Generates a downloadable PDF report containing the diagnosis and treatment information.
- *User-Friendly Interface:* Simple and intuitive UI design for ease of use.



### Prerequisites

### Android Studio: 
Android Studio is the official integrated development environment (IDE) for Android app development, based on JetBrains' IntelliJ IDEA software. It is designed specifically for Android       
development and provides a comprehensive suite of tools for creating, testing, and debugging Android applications.

*Link:* https://developer.android.com/studio

<p align="center">
  <img src="https://github.com/user-attachments/assets/9a7cadd8-13e4-4115-b909-4ed1b6f5d884" alt="![Screenshot 2024-08-18 235333]" width="300" height="300">
</p>
  
### Java Development Kit (JDK):

(Version 8 or higher) The Java Development Kit (JDK) is a crucial software development environment used for building applications and components that run on the Java platform. It   
  includes a comprehensive set of tools and libraries, such as the Java Runtime Environment (JRE), a compiler (`javac`), an interpreter/loader (`java`), an archiver (`jar`), and a documentation generator 
  (`javadoc`). The JDK enables developers to write, compile, debug, and execute Java programs. It provides all the essential components required to develop Java applications, including the Java API and tools for 
  creating and managing Java applications across various platforms. The JDK is available for different operating systems, such as Windows, macOS, and Linux, making it a versatile and indispensable tool for Java 
  developers.

  <p align="center">
  <img src="https://github.com/user-attachments/assets/17ae4955-33f5-4ed4-83dd-1b4b0bb6f0dc" alt="![Screenshot 2024-08-18 235357]" width="300" height="300">
</p>

  
### TensorFlow (TF):
TensorFlow is an open-source machine learning framework developed by Google that allows developers to build, train, and deploy machine learning models with ease. It is highly versatile, supporting a wide range of tasks, from deep learning and neural networks to natural language processing and computer vision. TensorFlow provides a comprehensive suite of tools and libraries, including TensorFlow Extended (TFX) for production ML pipelines, and TensorFlow Hub for reusable model components. It supports multiple languages, with Python being the most common, and offers flexibility to deploy models across various platforms, including cloud, mobile, web, and edge devices. TensorFlow's modular design and rich ecosystem make it a popular choice for both research and enterprise applications, enabling developers to bring AI solutions to life at scale.

*Link:* https://www.tensorflow.org/

### TensorFlow Lite (TFLite):
TensorFlow Lite is a lightweight version of TensorFlow designed specifically for mobile and embedded devices. It enables on-device machine learning by allowing developers to run TensorFlow models efficiently on devices with limited computing resources, such as smartphones, tablets, IoT devices, and microcontrollers. TensorFlow Lite provides a streamlined model conversion process, where developers can convert their trained TensorFlow models into a compact format optimized for speed and efficiency. It supports various optimizations like quantization, which reduces the model size and improves inference speed without significant loss in accuracy. With its focus on low-latency, real-time applications, TensorFlow Lite is widely used in applications like image recognition, natural language processing, and object detection, making it a powerful tool for bringing AI to edge devices.


*Link:* https://www.tensorflow.org/lite

<p align="center">
  <img src="https://github.com/user-attachments/assets/26d0b1e5-ed40-4d3b-a3c3-6200768d5490" alt="![Screenshot 2024-08-18 235410]" width="300" height="300">
</p>

### Dataset for ML Model :
https://www.kaggle.com/datasets/emmarex/plantdisease

### Screenshots of end results

<p align="center">
  <img src="https://github.com/user-attachments/assets/39c69ce5-2681-47b1-9e97-6fee54fafaa4" alt="![img2]" width="200" height ="400" >
  &nbsp;&nbsp;&nbsp;
  <img src="https://github.com/user-attachments/assets/b8f616d9-11e6-4ae5-83cf-9c485edd560f" alt="![img1]" width="200" height ="400" >
  &nbsp;&nbsp;&nbsp;
  <img src="https://github.com/user-attachments/assets/53bf0839-2fdc-4c1b-9e88-88c836687926" alt="![img3]" width="200" height ="400">
</p>


#  Project Setup Guide
 Follow the steps below to clone, open, configure, and run this project using Android Studio.
## Cloning the Repository
1. **Open Terminal (or Command Prompt on Windows).**
2. Navigate to the directory where you want to clone the project.
3. Run the following command:
   
    ```bash
   
        git clone https://github.com/adeshkedlaya05/Mini_project_laboratory
   

## Opening the Project in Android Studio

1. **Launch Android Studio.**
2. On the Welcome screen, click on **Open an Existing Project**.
3. Navigate to the directory where you cloned the repository and select the project folder.

## Configuring the Project

1. **Sync Project with Gradle Files:**

   - Android Studio will automatically prompt you to sync the project with Gradle files.
   - Click **Sync Now** to download and set up all required dependencies.

2. **Resolve Dependencies:**

   - If there are any missing dependencies or updates, Android Studio will display notifications.
   - Follow the prompts to resolve these issues by updating or installing required libraries.

## Running the Project

1. **Set Up an Emulator or Connect a Device:**

   - **Emulator:** Go to **AVD Manager** (Android Virtual Device) in Android Studio and create a new virtual device if you don’t have one set up. Start the emulator.
   - **Physical Device:** Connect your Android device via USB and enable
   - **USB debugging** in Developer Options.

2. **Run the Application:**

   - Click the **Run** button (green play icon) in the toolbar or press `Shift + F10`.
   - Choose the target device (emulator or connected device) and click **OK** to build and deploy the app.

3. **Monitor the Build Process:**

   - Check the **Build Output** window for progress and error messages.
   - Once the build completes, the app will be installed and launched on the selected device.
  
## Troubleshooting
 **Check Logcat:**
   - Open **Logcat** to view runtime logs and identify errors.




  ## Note:  This repository is currently not accepting outside contributions. Contributions in the form of code changes will not be considered. 
      








