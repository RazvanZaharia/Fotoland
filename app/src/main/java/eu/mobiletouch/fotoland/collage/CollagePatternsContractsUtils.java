package eu.mobiletouch.fotoland.collage;

import java.util.LinkedHashMap;

import eu.mobiletouch.fotoland.R;

/**
 * Created on 27-Oct-16.
 */
public class CollagePatternsContractsUtils {
    public static LinkedHashMap<Integer, Integer> collagePatternsContracts;

    /**
     *
     *  HashMap /<CollagePatternIconRes, CollagePatternLayoutRes>
     */
    static {
        collagePatternsContracts = new LinkedHashMap<>();
        collagePatternsContracts.put(R.drawable.gallery_collage_2_01, R.layout.collage_layout_2_1);
        collagePatternsContracts.put(R.drawable.gallery_collage_2_02, R.layout.collage_layout_2_2);
        collagePatternsContracts.put(R.drawable.gallery_collage_3_01, R.layout.collage_layout_3_1);
        collagePatternsContracts.put(R.drawable.gallery_collage_3_02, R.layout.collage_layout_3_2);
        collagePatternsContracts.put(R.drawable.gallery_collage_3_03, R.layout.collage_layout_3_3);
        collagePatternsContracts.put(R.drawable.gallery_collage_3_04, R.layout.collage_layout_3_4);
        collagePatternsContracts.put(R.drawable.gallery_collage_4_01, R.layout.collage_layout_4_1);
        collagePatternsContracts.put(R.drawable.gallery_collage_4_03, R.layout.collage_layout_4_3);
    }
}
