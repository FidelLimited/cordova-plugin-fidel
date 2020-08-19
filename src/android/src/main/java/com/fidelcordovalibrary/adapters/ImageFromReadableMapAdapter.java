package com.fidelcordovalibrary.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

//import com.facebook.common.executors.CallerThreadExecutor;
//import com.facebook.common.internal.Closeables;
//import com.facebook.common.memory.PooledByteBuffer;
//import com.facebook.common.memory.PooledByteBufferInputStream;
//import com.facebook.common.references.CloseableReference;
//import com.facebook.datasource.BaseDataSubscriber;
//import com.facebook.datasource.DataSource;
//import com.facebook.drawee.backends.pipeline.Fresco;
//import com.facebook.imagepipeline.common.ImageDecodeOptions;
//import com.facebook.imagepipeline.core.ImagePipeline;
//import com.facebook.imagepipeline.request.ImageRequest;
//import com.facebook.imagepipeline.request.ImageRequestBuilder;
//import com.fidelcordovalibrary.R;
import io.cordova.hellocordova.R;
import com.fidelcordovalibrary.adapters.abstraction.DataOutput;
import com.fidelcordovalibrary.adapters.abstraction.DataProcessor;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public final class ImageFromReadableMapAdapter implements DataProcessor<Boolean> {

   public DataOutput<Bitmap> bitmapOutput;
   private Context context;

   public ImageFromReadableMapAdapter(Context context) {
      this.context = context;
   }

   @Override
   public void process(Boolean showBannerImage) {
         if (showBannerImage) {
            Bitmap icon = BitmapFactory.decodeResource(this.context.getResources(),
                    R.drawable.banner);
            bitmapOutput.output(icon);
         }
   }
}
