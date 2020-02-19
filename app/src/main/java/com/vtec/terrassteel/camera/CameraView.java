package com.vtec.terrassteel.camera;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.ScaleGestureDetector;
import android.view.Surface;
import android.view.TextureView;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.vtec.terrassteel.R;

import java.io.ByteArrayOutputStream;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import jpegkit.Jpeg;

import static android.hardware.camera2.CameraMetadata.LENS_FACING_BACK;
import static android.hardware.camera2.CameraMetadata.LENS_FACING_FRONT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class CameraView extends FrameLayout {

    public static final int PERMISSION_CAMERA = 1;
    public static final int PERMISSION_MICROPHONE = 1 << 1;
    public static final int PERMISSION_STORAGE = 1 << 2;
    public static final int PERMISSION_LOCATION = 1 << 3;
    private static final int PERMISSION_REQUEST_CODE = 99107;
    private boolean mAdjustViewBounds;
    private float mAspectRatio;
    private int mFacing;
    private int mFlash;
    private int mFocus;
    private float mZoomFactor;
    private int mSensorPreset;
    private int mPreviewEffect;
    private int mPermissions;
    private float mImageMegaPixels;
    private int mImageJpegQuality;
    private GestureListener mGestureListener;
    private CameraListener mCameraListener;
    private PreviewListener mPreviewListener;
    private ErrorListener mErrorListener;
    private PermissionsListener mPermissionsListener;
    private CameraPreview mCameraPreview;
    private ScaleGestureDetector mScaleGestureDetector;
    private GestureDetector mGestureDetector;
    private GestureDetector.SimpleOnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            performTap(e.getX() / (float) getWidth(), e.getY() / (float) getHeight());
            return super.onSingleTapConfirmed(e);
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            performDoubleTap(e.getX() / (float) getWidth(), e.getY() / (float) getHeight());
            return super.onDoubleTap(e);
        }

        @Override
        public void onLongPress(MotionEvent e) {
            performLongTap(e.getX() / (float) getWidth(), e.getY() / (float) getHeight());
        }
    };
    private ScaleGestureDetector.OnScaleGestureListener mScaleGestureListener = new ScaleGestureDetector.OnScaleGestureListener() {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float dsx = detector.getCurrentSpanX() - detector.getPreviousSpanX();
            float dsy = detector.getCurrentSpanY() - detector.getPreviousSpanY();
            performPinch(dsx, dsy);
            return true;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
        }
    };

    public CameraView(Context context) {
        super(context);
        initialize();
        obtainAttributes(context, null);
    }

    public CameraView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
        obtainAttributes(context, attrs);
    }

    public CameraView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
        obtainAttributes(context, attrs);
    }

    private void initialize() {
        mScaleGestureDetector = new ScaleGestureDetector(getContext(), mScaleGestureListener);
        mGestureDetector = new GestureDetector(getContext(), gestureListener);
    }

    private void obtainAttributes(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CameraView);

        mAdjustViewBounds = a.getBoolean(R.styleable.CameraView_android_adjustViewBounds, false);
        mAspectRatio = a.getFloat(R.styleable.CameraView_camera_aspectRatio, -1f);
        mFacing = a.getInteger(R.styleable.CameraView_camera_facing, CameraCommon.FACING_BACK);
        mFlash = a.getInteger(R.styleable.CameraView_camera_flash, CameraCommon.FLASH_OFF);
        mFocus = a.getInteger(R.styleable.CameraView_camera_focus, CameraCommon.FOCUS_AUTO);
        mZoomFactor = a.getFloat(R.styleable.CameraView_camera_zoomFactor, 1.0f);
        mPermissions = a.getInteger(R.styleable.CameraView_camera_permissions, PERMISSION_CAMERA);
        mImageMegaPixels = a.getFloat(R.styleable.CameraView_camera_imageMegaPixels, 2f);
        mImageJpegQuality = a.getInteger(R.styleable.CameraView_camera_imageJpegQuality, 100);

        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mAdjustViewBounds) {
            ViewGroup.LayoutParams layoutParams = getLayoutParams();
            if (layoutParams.width == WRAP_CONTENT && layoutParams.height == WRAP_CONTENT) {
                throw new CameraException("android:adjustViewBounds=true while both layout_width and layout_height are setView to wrap_content - only 1 is allowed.");
            } else if (layoutParams.width == WRAP_CONTENT) {
                int width;
                int height = MeasureSpec.getSize(heightMeasureSpec);

                if (mAspectRatio > 0) {
                    width = (int) (height * mAspectRatio);
                    widthMeasureSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
                } else if (mCameraPreview != null && mCameraPreview.getPreviewSize() != null) {
                    Size previewSize = mCameraPreview.getAdjustedPreviewSize();

                    width = (int) (((float) height / (float) previewSize.getHeight()) * previewSize.getWidth());
                    widthMeasureSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
                }
            } else if (layoutParams.height == WRAP_CONTENT) {
                int width = MeasureSpec.getSize(widthMeasureSpec);
                int height;

                if (mAspectRatio > 0) {
                    height = (int) (width * mAspectRatio);
                    heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
                } else if (mCameraPreview != null && mCameraPreview.getPreviewSize() != null) {
                    Size previewSize = mCameraPreview.getAdjustedPreviewSize();

                    height = (int) (((float) width / (float) previewSize.getWidth()) * previewSize.getHeight());
                    heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
                }
            }
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    protected void onTap(float x, float y) {
        if (mGestureListener != null) {
            mGestureListener.onTap(this, x, y);
        }
    }

    protected void onLongTap(float x, float y) {
        if (mGestureListener != null) {
            mGestureListener.onLongTap(this, x, y);
        }
    }

    protected void onDoubleTap(float x, float y) {
        if (mGestureListener != null) {
            mGestureListener.onDoubleTap(this, x, y);
        }
    }

    protected void onPinch(float ds, float dsx, float dsy) {
        if (mGestureListener != null) {
            mGestureListener.onPinch(this, ds, dsx, dsy);
        }
    }

    public void performTap(float x, float y) {
        onTap(x, y);
    }

    public void performLongTap(float x, float y) {
        onLongTap(x, y);
    }

    public void performDoubleTap(float x, float y) {
        onDoubleTap(x, y);
    }

    public void performPinch(float dsx, float dsy) {
        float ds = (float) Math.sqrt((dsx * dsx) + (dsy * dsy));
        onPinch(ds, dsx, dsy);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        mScaleGestureDetector.onTouchEvent(event);
        return true;
    }

    public void onResume() {
        if (isInEditMode()) {
            return;
        }

        removeAllViews();

        mCameraPreview = new Camera2(getContext(), mFacing);

        addView(mCameraPreview);
    }

    public void onPause() {
        if (isInEditMode()) {
            return;
        }

        if (mCameraPreview != null) {
            mCameraPreview.stop();
            mCameraPreview = null;
        }
    }

    public void captureImage(final ImageCallback callback) {
        if (mCameraPreview != null) {
            mCameraPreview.captureImage(new JpegCallback() {
                @Override
                public void onJpeg(Jpeg jpeg) {
                    byte[] jpegBytes = jpeg.getJpegBytes();
                    jpeg.release();

                    callback.onImage(CameraView.this, jpegBytes);
                }
            });
        }
    }

    public void startVideo() {

    }

    public void stopVideo() {

    }

    public void captureVideo(VideoCallback callback) {

    }

    public void captureFrame(FrameCallback callback) {

    }

    public void setFrameCallback(FrameCallback callback) {

    }

    public boolean getAdjustViewBounds() {
        return mAdjustViewBounds;
    }

    public void setAdjustViewBounds(boolean adjustViewBounds) {
        mAdjustViewBounds = adjustViewBounds;
    }

    public float getAspectRatio() {
        return mAspectRatio;
    }

    public void setAspectRatio(float aspectRatio) {
        this.mAspectRatio = aspectRatio;
    }

    @CameraCommon.Facing
    public int getFacing() {
        return mFacing;
    }

    public void setFacing(@CameraCommon.Facing int facing) {
        mFacing = facing;

        if (mCameraPreview != null) {
            final CameraListener cameraListener = getCameraListener();
            setCameraListener(new CameraListener() {
                @Override
                public void onOpened() {
                    if (cameraListener != null) {
                        cameraListener.onOpened();
                    }

                    setCameraListener(cameraListener);
                }

                @Override
                public void onClosed() {
                    if (cameraListener != null) {
                        cameraListener.onClosed();
                    }

                    setCameraListener(cameraListener);
                    onResume();
                }
            });

            onPause();
        }
    }

    public void toggleFacing() {
        if (getFacing() == CameraCommon.FACING_BACK) {
            setFacing(CameraCommon.FACING_FRONT);
        } else {
            setFacing(CameraCommon.FACING_BACK);
        }
    }

    @CameraCommon.Flash
    public int getFlash() {
        return mFlash;
    }

    public void setFlash(@CameraCommon.Flash int flash) {
        mFlash = flash;

        if (mCameraPreview != null) {
            mCameraPreview.reconfigure();
        }
    }

    @CameraCommon.Focus
    public int getFocus() {
        return mFocus;
    }

    public void setFocus(@CameraCommon.Focus int focus) {
        mFocus = focus;
    }

    public float getZoomFactor() {
        return mZoomFactor;
    }

    public void setZoomFactor(float zoomFactor) {
        mZoomFactor = zoomFactor;
    }

    @CameraCommon.SensorPreset
    public int getSensorPreset() {
        return mSensorPreset;
    }

    public void setSensorPreset(@CameraCommon.SensorPreset int sensorPreset) {
        mSensorPreset = sensorPreset;
    }

    @CameraCommon.PreviewEffect
    public int getPreviewEffect() {
        return mPreviewEffect;
    }

    public void setPreviewEffect(@CameraCommon.PreviewEffect int previewEffect) {
        mPreviewEffect = previewEffect;
    }

    @Permission
    public int getPermissions() {
        return mPermissions;
    }

    public void setPermissions(@Permission int permissions) {
        mPermissions = permissions;
    }

    public GestureListener getGestureListener() {
        return mGestureListener;
    }

    public void setGestureListener(GestureListener gestureListener) {
        mGestureListener = gestureListener;
    }

    public CameraListener getCameraListener() {
        return mCameraListener;
    }

    public void setCameraListener(CameraListener cameraListener) {
        mCameraListener = cameraListener;
    }

    public PreviewListener getPreviewListener() {
        return mPreviewListener;
    }

    public void setPreviewListener(PreviewListener previewListener) {
        mPreviewListener = previewListener;
    }

    public ErrorListener getErrorListener() {
        return mErrorListener;
    }

    public void setErrorListener(ErrorListener errorListener) {
        mErrorListener = errorListener;
    }

    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
    @Retention(RetentionPolicy.SOURCE)
    @IntDef(flag = true,
            value = {PERMISSION_CAMERA, PERMISSION_MICROPHONE, PERMISSION_STORAGE, PERMISSION_LOCATION})
    @interface Permission {
    }

    public interface CameraListener {

        void onOpened();

        void onClosed();

    }

    public interface PreviewListener {

        void onStart();

        void onStop();

    }

    public interface ErrorListener {

        void onError(CameraView view, CameraException error);

    }

    public interface GestureListener {

        void onTap(CameraView view, float x, float y);

        void onLongTap(CameraView view, float x, float y);

        void onDoubleTap(CameraView view, float x, float y);

        void onPinch(CameraView view, float ds, float dsx, float dsy);

    }

    public interface PermissionsListener {

        void onPermissionsSuccess();

        void onPermissionsFailure();

    }

    public interface ImageCallback {

        void onImage(CameraView view, byte[] jpeg);

    }

    public interface VideoCallback {

        void onVideo(CameraView view, Object video);

    }

    public interface FrameCallback {

        void onFrame(CameraView view, byte[] jpeg);

    }

    interface CameraApi {

        Attributes getAttributes();

        void openCamera();

        void closeCamera();

        void startPreview();

        void stopPreview();

        void setDisplayRotation(int displayRotation);

        void setFlash(int flash);

        void setFocus(int focus);

        void captureImage(ImageCallback callback);

        interface ImageCallback {
            void onImage(byte[] data);
        }

    }

    public interface JpegCallback {
        void onJpeg(Jpeg jpeg);
    }

    public static class GestureListenerAdapter implements GestureListener {

        @Override
        public void onTap(CameraView view, float x, float y) {
        }

        @Override
        public void onLongTap(CameraView view, float x, float y) {
        }

        @Override
        public void onDoubleTap(CameraView view, float x, float y) {
        }

        @Override
        public void onPinch(CameraView view, float ds, float dsx, float dsy) {
        }

    }

    public static class Attributes {

        public int facing;

        public int sensorOrientation;

        public List<Integer> supportedFocuses;

        public List<Size> supportedPreviewSizes;

        public List<Size> supportedImageSizes;

    }

    public static class Size implements Comparable<Size> {

        private final int mWidth;
        private final int mHeight;

        public Size(int width, int height) {
            mWidth = width;
            mHeight = height;
        }

        public int getWidth() {
            return mWidth;
        }

        public int getHeight() {
            return mHeight;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null) {
                return false;
            } else if (this == o) {
                return true;
            } else if (o instanceof Size) {
                Size size = (Size) o;
                return mWidth == size.mWidth && mHeight == size.mHeight;
            } else {
                return false;
            }
        }

        @Override
        public String toString() {
            return mWidth + "x" + mHeight;
        }

        @Override
        public int hashCode() {
            return mHeight ^ ((mWidth << (Integer.SIZE / 2)) | (mWidth >>> (Integer.SIZE / 2)));
        }

        @Override
        public int compareTo(@NonNull Size another) {
            return mWidth * mHeight - another.mWidth * another.mHeight;
        }

    }

    public static class CameraException extends RuntimeException {

        public CameraException() {
            super();
        }

        public CameraException(String message) {
            super(message);
        }

        public CameraException(String message, Throwable cause) {
            super(message, cause);
        }

        public boolean isFatal() {
            return false;
        }

    }

    abstract class CameraPreview extends ViewGroup {

        static final int EVENT_CAMERA_OPENED = 0;
        static final int EVENT_CAMERA_CLOSED = 1;
        static final int EVENT_CAMERA_ERROR = 2;

        static final int EVENT_SURFACE_CREATED = 3;
        static final int EVENT_SURFACE_CHANGED = 4;
        static final int EVENT_SURFACE_DESTROYED = 5;
        static final int EVENT_SURFACE_ERROR = 6;

        static final int EVENT_PREVIEW_STARTED = 7;
        static final int EVENT_PREVIEW_STOPPED = 8;
        static final int EVENT_PREVIEW_ERROR = 9;

        protected final CameraApi mApi;
        protected int mFacing;

        private Handler mHandler;
        private HandlerThread mHandlerThread;

        private int mDisplayRotation;
        private OrientationEventListener mOrientationEventListener;

        private Attributes mAttributes;
        private Size mPreviewSize;

        public CameraPreview(Context context) {
            this(context, CameraCommon.FACING_BACK);
        }

        public CameraPreview(Context context, @CameraCommon.Facing int facing) {
            super(context);

            mFacing = facing;
            mApi = getApi();

            mHandlerThread = new HandlerThread("CameraPreview@" + System.currentTimeMillis());
            mHandlerThread.start();
            mHandler = new Handler(mHandlerThread.getLooper());

            mOrientationEventListener = new OrientationEventListener(context) {
                @Override
                public void onOrientationChanged(int orientation) {
                    switch (orientation) {
                        case Surface.ROTATION_0: {
                            mDisplayRotation = 0;
                            break;
                        }

                        case Surface.ROTATION_90: {
                            mDisplayRotation = 90;
                            break;
                        }

                        case Surface.ROTATION_180: {
                            mDisplayRotation = 180;
                            break;
                        }

                        case Surface.ROTATION_270: {
                            mDisplayRotation = 270;
                            break;
                        }
                    }

                    mApi.setDisplayRotation(mDisplayRotation);
                }
            };


            WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
            if (windowManager != null) {
                mOrientationEventListener.onOrientationChanged(windowManager.getDefaultDisplay().getRotation());
            }
        }

        public void stop() {
            mApi.stopPreview();
            mApi.closeCamera();
        }

        public void reconfigure() {
        }

        @Nullable
        public Size getPreviewSize() {
            if (mPreviewSize == null && mAttributes != null) {
                int width = getWidth();
                int height = getHeight();

                if (mDisplayRotation % 180 != mAttributes.sensorOrientation % 180) {
                    width = getHeight();
                    height = getWidth();
                }

                Size bestSize = null;
                float bestRatio = Float.MAX_VALUE;

                for (Size size : mAttributes.supportedPreviewSizes) {
                    if (bestSize == null) {
                        float widthRatio = (float) width / (float) size.getWidth();
                        float heightRatio = (float) height / (float) size.getHeight();

                        bestSize = size;
                        bestRatio = Math.max(widthRatio, heightRatio);
                        continue;
                    }

                    float widthRatio = (float) width / (float) size.getWidth();
                    float heightRatio = (float) height / (float) size.getHeight();

                    float ratio = Math.max(widthRatio, heightRatio);
                    if (ratio < bestRatio) {
                        bestSize = size;
                        bestRatio = ratio;
                    }
                }

                mPreviewSize = bestSize;
            }

            return mPreviewSize;
        }

        public Size getAdjustedPreviewSize() {
            Size previewSize = getPreviewSize();
            if (previewSize == null) {
                return null;
            }

            if (mAttributes != null && mDisplayRotation % 180 != mAttributes.sensorOrientation % 180) {
                return new Size(previewSize.getHeight(), previewSize.getWidth());
            }

            return previewSize;
        }

        public void captureImage(final JpegCallback callback) {
            mApi.setFlash(mFlash);

            mApi.captureImage(new CameraApi.ImageCallback() {
                @Override
                public void onImage(byte[] data) {
                    Jpeg jpeg = new Jpeg(data);

                    callback.onJpeg(jpeg);
                }
            });
        }

        protected synchronized void dispatchEvent(final int event) {
            post(new Runnable() {
                @Override
                public void run() {
                    switch (event) {
                        case EVENT_CAMERA_OPENED: {
                            mAttributes = mApi.getAttributes();

                            if (mCameraListener != null) {
                                mCameraListener.onOpened();
                            }

                            mApi.startPreview();
                            break;
                        }

                        case EVENT_CAMERA_CLOSED: {
                            if (mCameraListener != null) {
                                mCameraListener.onClosed();
                            }

                            break;
                        }

                        case EVENT_CAMERA_ERROR: {
                            mApi.closeCamera();
                            break;
                        }

                        case EVENT_SURFACE_CREATED: {
                            mApi.openCamera();
                            break;
                        }

                        case EVENT_SURFACE_CHANGED: {
                            if (mAttributes != null) {
                                mApi.stopPreview();
                                mApi.startPreview();
                            }
                            break;
                        }

                        case EVENT_SURFACE_DESTROYED: {
                            mApi.stopPreview();
                            mApi.closeCamera();
                            break;
                        }

                        case EVENT_SURFACE_ERROR: {
                            break;
                        }

                        case EVENT_PREVIEW_STARTED: {
                            mApi.setDisplayRotation(mDisplayRotation);
                            requestLayout();

                            if (mPreviewListener != null) {
                                mPreviewListener.onStart();
                            }
                            break;
                        }

                        case EVENT_PREVIEW_STOPPED: {
                            if (mPreviewListener != null) {
                                mPreviewListener.onStop();
                            }
                            break;
                        }

                        case EVENT_PREVIEW_ERROR: {
                            break;
                        }
                    }
                }
            });

        }

        protected void background(Runnable runnable) {
            mHandler.post(runnable);
        }

        @Override
        protected abstract void onLayout(boolean changed, int left, int top, int right, int bottom);

        public abstract CameraApi getApi();

    }

    @TargetApi(21)
    private class Camera2 extends CameraPreview implements TextureView.SurfaceTextureListener {

        private TextureView mTextureView;

        private CameraManager mCameraManager;
        private CameraDevice mCameraDevice;

        private CameraCharacteristics mCameraCharacteristics;

        private CameraCaptureSession mCaptureSession;

        private CaptureRequest.Builder mPreviewRequestBuilder;

        public Camera2(Context context) {
            this(context, CameraCommon.FACING_BACK);
        }

        protected Camera2(Context context, @CameraCommon.Facing int facing) {
            super(context, facing);

            mTextureView = new TextureView(context);
            mTextureView.setSurfaceTextureListener(this);
        }

        @Override
        protected void onAttachedToWindow() {
            super.onAttachedToWindow();
            addView(mTextureView);
        }

        @Override
        protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
            mTextureView.layout(left, top, right, bottom);

            Size previewSize = getAdjustedPreviewSize();
            if (previewSize == null) {
                return;
            }

            float viewSizeRatio = (float) right / (float) bottom;
            float previewSizeRatio = (float) previewSize.getWidth() / (float) previewSize.getHeight();

            float sx;
            float sy;

            if (viewSizeRatio < previewSizeRatio) {
                sx = previewSizeRatio / viewSizeRatio;
                sy = 1;
            } else {
                sx = 1;
                sy = viewSizeRatio / previewSizeRatio;
            }

            Matrix matrix = new Matrix();
            matrix.setScale(sx, sy);

            float scaledWidth = right * sx;
            float scaledHeight = bottom * sy;
            float dx = (right - scaledWidth) / 2;
            float dy = (bottom - scaledHeight) / 2;

            matrix.postTranslate(dx, dy);
            mTextureView.setTransform(matrix);
        }

        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            dispatchEvent(EVENT_SURFACE_CREATED);
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
            dispatchEvent(EVENT_SURFACE_CHANGED);
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            dispatchEvent(EVENT_SURFACE_DESTROYED);
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        }

        @Override
        public CameraApi getApi() {
            return new CameraApi() {

                String mCameraId = null;
                boolean mFlashing = false;

                @Override
                public Attributes getAttributes() {
                    if (mCameraCharacteristics == null) {
                        return null;
                    }

                    StreamConfigurationMap map = mCameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
                    if (map == null) {
                        return null;
                    }

                    Attributes attributes = new Attributes();

                    Integer sensorOrientation = mCameraCharacteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
                    if (sensorOrientation != null) {
                        attributes.sensorOrientation = sensorOrientation;
                    }

                    attributes.facing = mFacing;

                    attributes.supportedPreviewSizes = new ArrayList<>();
                    for (android.util.Size size : map.getOutputSizes(SurfaceTexture.class)) {
                        attributes.supportedPreviewSizes.add(new Size(size.getWidth(), size.getHeight()));
                    }

                    return attributes;
                }

                @Override
                @SuppressWarnings("MissingPermission")
                public void openCamera() {
                    background(new Runnable() {
                        @Override
                        public void run() {
                            int facingTarget = mFacing == CameraCommon.FACING_BACK ? LENS_FACING_BACK : LENS_FACING_FRONT;
                            mCameraManager = (CameraManager) getContext().getSystemService(Context.CAMERA_SERVICE);

                            if (mCameraManager == null) {

                                return;
                            }

                            String[] cameraIdList;
                            try {
                                cameraIdList = mCameraManager.getCameraIdList();
                            } catch (Exception e) {

                                return;
                            }

                            for (final String cameraId : cameraIdList) {
                                try {
                                    CameraCharacteristics characteristics = mCameraManager.getCameraCharacteristics(cameraId);
                                    Integer facing = characteristics.get(CameraCharacteristics.LENS_FACING);
                                    if (facing != null && facing != facingTarget) {
                                        continue;
                                    }

                                    StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
                                    if (map == null) {
                                        continue;
                                    }

                                    mCameraCharacteristics = characteristics;

                                    mCameraManager.openCamera(cameraId, new CameraDevice.StateCallback() {

                                        @Override
                                        public void onOpened(final @NonNull CameraDevice camera) {
                                            background(new Runnable() {
                                                @Override
                                                public void run() {
                                                    mCameraId = cameraId;
                                                    mCameraDevice = camera;
                                                    dispatchEvent(EVENT_CAMERA_OPENED);
                                                }
                                            });
                                        }

                                        @Override
                                        public void onDisconnected(final @NonNull CameraDevice camera) {
                                            background(new Runnable() {
                                                @Override
                                                public void run() {
                                                    camera.close();
                                                    mCameraDevice = null;
                                                    mCameraId = null;

                                                    dispatchEvent(EVENT_CAMERA_CLOSED);
                                                }
                                            });
                                        }

                                        @Override
                                        public void onError(final @NonNull CameraDevice camera, int error) {
                                            background(new Runnable() {
                                                @Override
                                                public void run() {
                                                    camera.close();
                                                    mCameraDevice = null;
                                                }
                                            });
                                        }

                                    }, null);
                                } catch (Exception e) {
                                }
                            }
                        }
                    });
                }

                @Override
                public void closeCamera() {
                    background(new Runnable() {
                        @Override
                        public void run() {
                            if (mCameraDevice != null) {
                                mCameraDevice.close();
                                mCameraDevice = null;
                            }

                            dispatchEvent(EVENT_CAMERA_CLOSED);
                        }
                    });
                }

                @Override
                public void startPreview() {
                    background(() -> {
                        Size previewSize = getPreviewSize();

                        try {
                            SurfaceTexture surfaceTexture = mTextureView.getSurfaceTexture();
                            surfaceTexture.setDefaultBufferSize(Objects.requireNonNull(previewSize).getWidth(), previewSize.getHeight());

                            Surface surface = new Surface(surfaceTexture);

                            mPreviewRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
                            mPreviewRequestBuilder.addTarget(surface);

                            mCameraDevice.createCaptureSession(Arrays.asList(surface), new CameraCaptureSession.StateCallback() {
                                @Override
                                public void onConfigured(final @NonNull CameraCaptureSession session) {
                                    background(() -> {
                                        mCaptureSession = session;
                                        CaptureRequest previewRequest = mPreviewRequestBuilder.build();
                                        try {
                                            mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
                                            session.setRepeatingRequest(previewRequest, null, null);

                                            dispatchEvent(EVENT_PREVIEW_STARTED);
                                        } catch (Exception ignored) {
                                        }
                                    });
                                }

                                @Override
                                public void onConfigureFailed(final @NonNull CameraCaptureSession session) {
                                    background(() -> {

                                    });
                                }
                            }, null);
                        } catch (Exception ignored) {

                        }
                    });
                }

                @Override
                public void stopPreview() {
                    background(() -> {
                        if (mCaptureSession != null) {
                            mCaptureSession.close();
                            mCaptureSession = null;

                            dispatchEvent(EVENT_PREVIEW_STOPPED);
                        }
                    });
                }

                @Override
                public void setDisplayRotation(int displayRotation) {

                }

                @Override
                public void setFlash(int flash) {

                }

                @Override
                public void setFocus(int focus) {
                    background(() -> {

                    });
                }

                @Override
                public void captureImage(final ImageCallback callback) {
                    if (mFlash == CameraCommon.FLASH_ON && !mFlashing) {
                        try {
                            mPreviewRequestBuilder.set(CaptureRequest.FLASH_MODE, CaptureRequest.FLASH_MODE_TORCH);
                            mCaptureSession.setRepeatingRequest(mPreviewRequestBuilder.build(), null, null);
                            mFlashing = true;

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    captureImage(callback);
                                }
                            }, 1000);
                            return;
                        } catch (Exception e) {
                        }
                    }

                    background(() -> {
                        if (getAdjustedPreviewSize() != null) {
                            Size previewSize = getAdjustedPreviewSize();

                            int width = getWidth();
                            int height = getHeight();

                            float widthRatio = (float) width / (float) previewSize.getWidth();
                            float heightRatio = (float) height / (float) previewSize.getHeight();

                            float ratio = Math.min(widthRatio, heightRatio);
                            if (widthRatio > 1 || heightRatio > 1) {
                                ratio = Math.max(widthRatio, heightRatio);
                            }

                            Bitmap bitmap = mTextureView.getBitmap((int) (previewSize.getWidth() * ratio), (int) (previewSize.getHeight() * ratio));

                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                            byte[] byteArray = stream.toByteArray();

                            callback.onImage(byteArray);

                            if (mFlashing) {
                                try {
                                    mPreviewRequestBuilder.set(CaptureRequest.FLASH_MODE, CaptureRequest.FLASH_MODE_OFF);
                                    mCaptureSession.setRepeatingRequest(mPreviewRequestBuilder.build(), null, null);
                                    mFlashing = false;
                                } catch (Exception ignored) {
                                }
                            }
                        }
                    });
                }

            };
        }

    }

}