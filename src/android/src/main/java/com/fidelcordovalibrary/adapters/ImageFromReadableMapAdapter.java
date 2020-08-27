package com.fidelcordovalibrary.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.fidelcordovalibrary.R;
import com.fidelcordovalibrary.adapters.abstraction.DataOutput;
import com.fidelcordovalibrary.adapters.abstraction.DataProcessor;

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
