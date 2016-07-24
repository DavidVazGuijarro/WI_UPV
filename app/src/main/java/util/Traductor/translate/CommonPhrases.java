package util.Traductor.translate;

import android.content.Context;
import android.content.res.Resources;

import upv.welcomeincoming.com.R;

/**
 * Created by Marcos on 20/07/14.
 */
public class CommonPhrases {

    final public static int length = 8;//numero de common phrases en los arrays

    public static String[] getArrayPhrases(Context mContext) {
        Resources res = mContext.getResources();
        String[] array = {res.getString(R.string.cphrase_1), res.getString(R.string.cphrase_2), res.getString(R.string.cphrase_3), res.getString(R.string.cphrase_4),
                res.getString(R.string.cphrase_5), res.getString(R.string.cphrase_6), res.getString(R.string.cphrase_7), res.getString(R.string.cphrase_8)};
        return array;
    }

    public static String[] getArrayPhrases_ES(Context mContext) {
        Resources res = mContext.getResources();
        String[] array = {res.getString(R.string.cphrase_1_es), res.getString(R.string.cphrase_2_es), res.getString(R.string.cphrase_3_es), res.getString(R.string.cphrase_4_es),
                res.getString(R.string.cphrase_5_es), res.getString(R.string.cphrase_6_es), res.getString(R.string.cphrase_7_es), res.getString(R.string.cphrase_8_es)};
        return array;
    }

}
