package com.deeplocal.smores;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.text.format.DateFormat;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class Utils {

    public static final String TAG = "abcd";

    private static final String GSANS_BOLD = "fonts/GoogleSans-Bold.ttf";

    public static String getDate(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time * 1000);
        String date = DateFormat.format("d/M/yy h:mm a", cal).toString();
        return date;
    }

    public static Typeface getTypeface(Context c) {
        return Typeface.createFromAsset(c.getAssets(), GSANS_BOLD);
    }

    public static String loadUserId(Context context) {

        SharedPreferences prefs = context.getApplicationContext().getSharedPreferences(Constants.SHARED_PREFS_NAME,
                Context.MODE_PRIVATE);
        String userId = prefs.getString(Constants.SHARED_PREFS_USERID, null);

        if (userId == null) {
            Log.w(Utils.TAG, "No userId in shared prefs");
            createNewUser(context);
            userId = prefs.getString(Constants.SHARED_PREFS_USERID, null);
        } else {
            Log.d(Utils.TAG, String.format("Loaded userId = %s from shared prefs", userId));
        }

        return userId;
    }

    private static void storeUserId(Context context, String userId) {

        if (userId == null) {
            Log.w(Utils.TAG, "No userId to store");
            return;
        }

        SharedPreferences prefs = context.getApplicationContext().getSharedPreferences(Constants.SHARED_PREFS_NAME,
                Context.MODE_PRIVATE);
        prefs.edit().putString(Constants.SHARED_PREFS_USERID, userId).commit();
        Log.d(Utils.TAG, "Saved userId to shared prefs");
    }

    private static void createNewUser(final Context context) {
        FirebaseFirestore mDb = FirebaseFirestore.getInstance();
        if (mDb == null) {
            Log.e(Utils.TAG, "No Firestore database");
            return;
        }

        Map<String, Object> user = new HashMap<>();
        user.put("first", Constants.NEW_USER_FIRST_NAME);
        user.put("last", Constants.NEW_USER_LAST_NAME);

        mDb.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {

                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        String userId = documentReference.getId();
                        Log.d(Utils.TAG, String.format("Created user with userId = %s", userId));
                        storeUserId(context, userId);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {

                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(Utils.TAG, "Error adding user", e);
                    }
                });
    }

    public static CompletableFuture<SmoresOrder> getLatestOrderFuture(FirebaseFirestore database,
                                                                      String userId, final String collection) {

        Log.d(Utils.TAG, String.format("getLatestOrderFuture()"));

        final CompletableFuture<SmoresOrder> orderFuture = new CompletableFuture<>();

        String path = String.format("users/%s/%s", userId, collection);
        Log.d(TAG, String.format("path = %s", path));

        database.collection(path)
                .orderBy("lastUpdate", Query.Direction.DESCENDING).limit(1)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        Log.d(TAG, "onComplete()");

                        if (task.isSuccessful()) {
                            List<DocumentSnapshot> docs = task.getResult().getDocuments();
                            if (docs.size() == 0) {
                                orderFuture.complete(null);
                            } else {
                                QueryDocumentSnapshot doc = (QueryDocumentSnapshot) task.getResult().getDocuments().get(0);
                                SmoresOrder order = doc.toObject(SmoresOrder.class);
                                Log.e(TAG, String.format("Found %s status from firebase", collection));
                                Log.e(TAG, String.format("Order %s", order.toString()));
                                orderFuture.complete(order);
                            }
                        } else {
                            Log.e(TAG, String.format("Error loading %s", collection));
                            orderFuture.complete(null);
                        }
                    }
                });

        try {
            return orderFuture;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
