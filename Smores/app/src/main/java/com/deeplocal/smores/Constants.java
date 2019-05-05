package com.deeplocal.smores;

import android.net.Uri;

import java.util.HashMap;
import java.util.Map;

public final class Constants {

    private Constants() {}

    public static final String SHARED_PREFS_NAME = "smores_settings";
    public static final String SHARED_PREFS_USERID = "smuser_id";
    public static final String NEW_USER_FIRST_NAME = "Aemon";
    public static final String NEW_USER_LAST_NAME = "Tully";

    public static final Uri SLICE_URI_LAST_ORDER = Uri.parse("content://com.deeplocal.smores/last-order");

    public static final String NO_PENDING_ORDERS = "No pending order";
    public static Map<String, String> SMORE_TYPES;
    static {
        SMORE_TYPES = new HashMap<>();
        SMORE_TYPES.put("GFC_M_DCH_LT_Q", "dark chocolate smore with a gluten free cracker");
        SMORE_TYPES.put("C_VM_DCH_LT_Q", "dark chocolate smore vanilla marshmallow low toasted");
        SMORE_TYPES.put("C_VM_DCH_LIT_Q", "dark chocolate vanilla marshmallow light toasted");
        SMORE_TYPES.put("HGC_M_CH_MT_Q", "smore with a honey graham cracker medium toasted");
        SMORE_TYPES.put("CC_M_CH_MT_Q", "smore with a chocolate cracker medium toasted");
        SMORE_TYPES.put("C_M_OCH_T_Q", "oreo smore");
        SMORE_TYPES.put("HGC_VM_MCH_T_Q", "milk chocolate with a honey graham cracker and a vanilla marshmallow");
        SMORE_TYPES.put("HGC_M_MCH_LT_Q", "milk chocolate with a honey graham cracker light toasted");
        SMORE_TYPES.put("CC_VM_MCH_T_Q", "milk chocolate with a chocolate cracker and a vanilla marshmallow");
        SMORE_TYPES.put("CC_M_MCH_T_Q", "milk chocolate with a chocolate cracker");
        SMORE_TYPES.put("usual", "usual smore");
    }
}
