/*
 * Copyright 2022 The TensorFlow Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *             http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.tensorflow.lite.examples.facerecognition.fragments.CameraFragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.AspectRatio;
import androidx.camera.core.CameraInfoUnavailableException;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.common.util.concurrent.ListenableFuture;

import org.tensorflow.lite.examples.facerecognition.FaceDB;
import org.tensorflow.lite.examples.facerecognition.FaceRecognitionPipeline;
import org.tensorflow.lite.examples.facerecognition.R;
import org.tensorflow.lite.examples.facerecognition.RecognizedFace;
import org.tensorflow.lite.examples.facerecognition.activity.StartActivity;
import org.tensorflow.lite.examples.facerecognition.databinding.FragmentCameraBinding;
import org.tensorflow.lite.examples.facerecognition.tflite.FaceEmbedding;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Fragment for displaying and controlling the device camera and other UI
 */
public class CameraFragment extends Fragment
        implements FaceRecognitionPipeline.Listener {
    private static final String TAG = "CameraFragment";
    private FragmentCameraBinding fragmentCameraBinding;
    private FaceRecognitionPipeline faceRecognitionPipeline;
    private Bitmap bitmapBuffer;
    private Preview preview;
    private ImageAnalysis imageAnalyzer;
    private ProcessCameraProvider cameraProvider;
    private int lensFacing = CameraSelector.LENS_FACING_FRONT;
    private final Object task = new Object();
    /**
     * Blocking camera operations are performed using this executor
     */
    private ExecutorService cameraExecutor;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        Log.d(TAG, "⚽️ onCreateView");
        fragmentCameraBinding = FragmentCameraBinding.inflate(inflater, container, false);

        return fragmentCameraBinding.getRoot();
    }

    @Override
    public void onResume() {
        Log.d(TAG, "⚽️ onResume");
        super.onResume();

        if (!PermissionsFragment.hasPermission(requireContext())) {
            Navigation.findNavController(requireActivity(), R.id.fragment_container)
                    .navigate(
                            CameraFragmentDirections.actionCameraToPermissions()
                    );
        }
    }

    @Override
    public void onStart() {
        Log.d(TAG, "⚽️ onStart");
        super.onStart();

        faceRecognitionPipeline.start();
    }

    @Override
    public void onStop() {
        Log.d(TAG, "⚽️ onStop");
        super.onStop();

        faceRecognitionPipeline.stop();
    }

    @Override
    public void onDestroyView() {
        fragmentCameraBinding = null;
        Log.d(TAG, "⚽️ onDestroyView");
        super.onDestroyView();

        // Shut down our background executor
        cameraExecutor.shutdown();
        synchronized (task) {
            faceRecognitionPipeline.clear();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "⚽️ onViewCreated");
        super.onViewCreated(view, savedInstanceState);
        cameraExecutor = Executors.newSingleThreadExecutor();
        faceRecognitionPipeline = FaceRecognitionPipeline.create(requireContext(), this);

        // Set up the camera and its use cases
        fragmentCameraBinding.viewFinder.post(() -> {
            updateCameraUi();
            setUpCamera();
        });
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        bindCameraUseCases();
        updateCameraSwitchButton();

        //        imageAnalyzer.setTargetRotation(
        //                fragmentCameraBinding.viewFinder.getDisplay().getRotation()
        //        );
    }



    private void updateCameraUi() {
        fragmentCameraBinding.cameraSwitchButton.setEnabled(false);

        // Set up flip camera button
        fragmentCameraBinding.cameraSwitchButton.setOnClickListener(imageButtonView -> {
            if (lensFacing == CameraSelector.LENS_FACING_BACK) {
                lensFacing = CameraSelector.LENS_FACING_FRONT;
            } else {
                lensFacing = CameraSelector.LENS_FACING_BACK;
            }
            // Re-bind use cases to update selected camera
            bindCameraUseCases();
        });
    }

    // Initialize CameraX, and prepare to bind the camera use cases
    private void setUpCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture =
                ProcessCameraProvider.getInstance(requireContext());
        cameraProviderFuture.addListener(() -> {
            try {
                cameraProvider = cameraProviderFuture.get();

                // Enable or disable switching between cameras
                // https://github.com/android/camera-samples/blob/main/CameraXBasic/app/src/main/java/com/android/example/cameraxbasic/fragments/CameraFragment.kt#L251
                updateCameraSwitchButton();

                // Build and bind the camera use cases
                bindCameraUseCases();
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }, ContextCompat.getMainExecutor(requireContext()));
    }


    private void updateCameraSwitchButton() {
        fragmentCameraBinding.cameraSwitchButton.setEnabled(hasBackCamera() && hasFrontCamera());
    }

    private boolean hasBackCamera() {
        try {
            return cameraProvider.hasCamera(CameraSelector.DEFAULT_BACK_CAMERA);
        } catch (CameraInfoUnavailableException e) {
            return false;
        }
    }

    private boolean hasFrontCamera() {
        try {
            return cameraProvider.hasCamera(CameraSelector.DEFAULT_FRONT_CAMERA);
        } catch (CameraInfoUnavailableException e) {
            return false;
        }
    }

    // Declare and bind preview, capture and analysis use cases
    private void bindCameraUseCases() {
        // CameraSelector
        CameraSelector.Builder cameraSelectorBuilder = new CameraSelector.Builder();
        CameraSelector cameraSelector = cameraSelectorBuilder
                .requireLensFacing(lensFacing).build();

        setUpAnalyzer();

        // Must unbind the use-cases before rebinding them
        cameraProvider.unbindAll();

        try {
            // A variable number of use-cases can be passed here -
            // camera provides access to CameraControl & CameraInfo
            cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    preview,
                    imageAnalyzer
            );

            // Attach the viewfinder's surface provider to preview use case
            preview.setSurfaceProvider(
                    fragmentCameraBinding.viewFinder.getSurfaceProvider()
            );
        } catch (Exception exc) {
            Log.e(TAG, "Use case binding failed", exc);
        }
    }

    private void setUpAnalyzer() {
        // Preview. Only using the 4:3 ratio because this is the closest to
        // our model
        preview = new Preview.Builder()
                .setTargetAspectRatio(AspectRatio.RATIO_4_3)
                .setTargetRotation(fragmentCameraBinding.viewFinder.getDisplay().getRotation())
                .build();

        // ImageAnalysis. Using RGBA 8888 to match how our models work
        imageAnalyzer = new ImageAnalysis.Builder()
                .setTargetAspectRatio(AspectRatio.RATIO_4_3)
                .setTargetRotation(fragmentCameraBinding.viewFinder.getDisplay().getRotation())
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_RGBA_8888)
                .build();

        // The analyzer can then be assigned to the instance
        imageAnalyzer.setAnalyzer(cameraExecutor, imageProxy -> {
            if (bitmapBuffer == null) {
                bitmapBuffer = Bitmap.createBitmap(
                        imageProxy.getWidth(),
                        imageProxy.getHeight(),
                        Bitmap.Config.ARGB_8888);
            }
            recognizeFaces(imageProxy);
        });
    }

    private void recognizeFaces(@NonNull ImageProxy imageProxy) {
        // Copy out RGB bits to the shared bitmap buffer
        bitmapBuffer.copyPixelsFromBuffer(imageProxy.getPlanes()[0].getBuffer());
        int imageRotation = imageProxy.getImageInfo().getRotationDegrees();

        imageProxy.close();
        synchronized (task) {
            // Pass Bitmap and rotation to the image classifier helper for
            // processing and classification
            faceRecognitionPipeline.process(bitmapBuffer, imageRotation);
        }
    }

    @Override
    public void onFaceRecognitionPipelineError(String error) {
        try {
            requireActivity().runOnUiThread(() -> {
                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show();
            });
        } catch (IllegalStateException e) {
            Log.e(TAG, e.toString());
        }
    }

    @Override
    public void onFaceRecognitionPipelineResults(List<RecognizedFace> results, long inferenceTime, int rotationDegree) {
        try {
            requireActivity().runOnUiThread(() -> {
                if (fragmentCameraBinding == null) {
                    return;
                }

                fragmentCameraBinding.inferenceTimeVal
                        .setText(String.format(Locale.US, "%d ms", inferenceTime));

                // Pass necessary information to OverlayView for drawing on the canvas
                fragmentCameraBinding.overlay.setResults(
                        results,
                        rotationDegree,
                        lensFacing
                );

                // Force a redraw
                fragmentCameraBinding.overlay.invalidate();
            });
        } catch (IllegalStateException e) {
            Log.e(TAG, e.toString());
        }
    }


    // 여기 수정 해야해
    // 화면 전환
    @Override
    public void onFaceRecognitionPipelineFoundUnknownFaces(List<Pair<Bitmap, FaceEmbedding>> unknownFaces, FaceDB db) {
        try {
            requireActivity().runOnUiThread(() -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                View dialogLayout = getLayoutInflater().inflate(R.layout.dialog_register_face, null);
                ImageView ivFace = dialogLayout.findViewById(R.id.dlg_image);
                EditText etName = dialogLayout.findViewById(R.id.dlg_input);
                ivFace.setImageBitmap(unknownFaces.get(0).first);

//                builder.setPositiveButton("Ok", (dialog, id) -> {
//                            db.register(etName.getText().toString(), unknownFaces.get(0).second);
//                            //                            db.setIsRegistering(false);
//                            faceRecognitionPipeline.start();
//                        })
//                        .setNegativeButton("Cancel", (dialog, id) -> {
//                            //                            db.setIsRegistering(false);
//                            faceRecognitionPipeline.start();
//                        })
//                        .setView(dialogLayout).show();
            });



            Intent intent = new Intent(getActivity(), StartActivity.class);
            startActivity(intent);

        } catch (IllegalStateException e) {
            Log.e(TAG, e.toString());
        }

    }

}
