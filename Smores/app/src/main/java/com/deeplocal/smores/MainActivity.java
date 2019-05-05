package com.deeplocal.smores;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;


public class MainActivity extends AppCompatActivity {

    public static final String SLICE_EXTRA = "extra_from_slice";

    // holds sections/screens of the app
    private ViewPager mViewPager;
    private SectionsPagerAdapter mSectionsPagerAdapter;

    private SmoresOrder mPendingOrder; // current order to alter
    public static PrevOrdersAdapter prevOrdersAdapter; // previous orders

    private String userId;
    private FirebaseFirestore mDb;
    private ListenerRegistration mFirestoreListener;

    // currently selected index of the ingredient viewpager
    public static int[] currentIngredientSelection = {-1, -1, -1, -1, -1};
    public static ReviewFragment reviewFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prevOrdersAdapter = new PrevOrdersAdapter(this);

        // get database and create data listener
        mDb = FirebaseFirestore.getInstance();
        this.userId = Utils.loadUserId(this);
        loadPrevOrders();
        createPendingOrderListener();

        // create adapter that returns fragment for each section
        IngredientFragment.mainActivity = this;
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // set up view pager
        mViewPager = findViewById(R.id.ingredient_viewpager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(1); // start on main screen

        Intent intent = getIntent();
        String action = intent.getAction();
        Uri data = intent.getData();

        Log.d(Utils.TAG, String.format("action = %s", action));
        Log.d(Utils.TAG, String.format("data = %s", data));

        // for deep linking
        if (data != null) {

            String itemName = data.getQueryParameter("item_name");
            Log.d(Utils.TAG, String.format("item_name = %s", itemName));

            prepopulateOrder(itemName);

            if (MainActivity.reviewFragment != null) {
                MainActivity.reviewFragment.updateText(mPendingOrder);
                Log.d(Utils.TAG, "Updating ReviewFragment UI");
            }

            mViewPager.setCurrentItem(7);
        }

        String fromSlice = intent.getStringExtra(SLICE_EXTRA);
        Log.d(Utils.TAG, String.format("fromSlice = %s", fromSlice));
        if ((fromSlice != null) && fromSlice.equals("last-order")) {
            mViewPager.setCurrentItem(0); // show previous orders list
        }
    }

    private void prepopulateOrder(String itemId) {

        if (itemId == null) {
            return;
        }

        if (mPendingOrder == null) {
            mPendingOrder = new SmoresOrder();
        }

        // <entity identifier="GFC_M_DCH_LT_Q" name="dark chocolate smore with a gluten free cracker"/>
        if (itemId.equals("GFC_M_DCH_LT_Q")) {
            mPendingOrder.cracker = SmoresOrder.CRACKER_GLUTEN_FREE;
            mPendingOrder.marshmallow = SmoresOrder.MARSHMALLOW_VANILLA;
            mPendingOrder.chocolate = SmoresOrder.CHOCOLATE_DARK;
            mPendingOrder.toastLevel = 2;
            mPendingOrder.quantity = 1;
            return;
        }

        // <entity identifier="GFC_M_DCH_LT_Q" name="dark chocolate smore with a gluten free cracker"/>
        if (itemId.equals("GFC_M_DCH_LT_Q")) {
            mPendingOrder.cracker = SmoresOrder.CRACKER_GLUTEN_FREE;
            mPendingOrder.marshmallow = SmoresOrder.MARSHMALLOW_VANILLA;
            mPendingOrder.chocolate = SmoresOrder.CHOCOLATE_DARK;
            mPendingOrder.toastLevel = 2;
            mPendingOrder.quantity = 1;
            return;
        }

        // <entity identifier="C_VM_DCH_LT_Q" name="dark chocolate smore vanilla marshmallow low toasted"/>
        // <entity identifier="C_VM_DCH_LIT_Q" name="dark chocolate vanilla marshmallow light toasted"/>
        if (itemId.equals("C_VM_DCH_LT_Q") || itemId.equals("C_VM_DCH_LT_Q")) {
            mPendingOrder.cracker = SmoresOrder.CRACKER_HONEY_GRAHAM;
            mPendingOrder.marshmallow = SmoresOrder.MARSHMALLOW_VANILLA;
            mPendingOrder.chocolate = SmoresOrder.CHOCOLATE_DARK;
            mPendingOrder.toastLevel = 1;
            mPendingOrder.quantity = 1;
            return;
        }

        // <entity identifier="HGC_M_CH_MT_Q" name="smore with a honey graham cracker medium toasted"/>
        if (itemId.equals("HGC_M_CH_MT_Q")) {
            mPendingOrder.cracker = SmoresOrder.CRACKER_HONEY_GRAHAM;
            mPendingOrder.marshmallow = SmoresOrder.MARSHMALLOW_VANILLA;
            mPendingOrder.chocolate = SmoresOrder.CHOCOLATE_MILK;
            mPendingOrder.toastLevel = 2;
            mPendingOrder.quantity = 1;
            return;
        }

        // <entity identifier="CC_M_CH_MT_Q" name="smore with a chocolate cracker medium toasted"/>
        if (itemId.equals("CC_M_CH_MT_Q")) {
            mPendingOrder.cracker = SmoresOrder.CRACKER_CHOCOLATE;
            mPendingOrder.marshmallow = SmoresOrder.MARSHMALLOW_VANILLA;
            mPendingOrder.chocolate = SmoresOrder.CHOCOLATE_MILK;
            mPendingOrder.toastLevel = 2;
            mPendingOrder.quantity = 1;
            return;
        }

        // <entity identifier="C_M_OCH_T_Q" name="oreo smore"/>
        if (itemId.equals("C_M_OCH_T_Q")) {
            mPendingOrder.cracker = SmoresOrder.CRACKER_HONEY_GRAHAM;
            mPendingOrder.marshmallow = SmoresOrder.MARSHMALLOW_VANILLA;
            mPendingOrder.chocolate = SmoresOrder.CHOCOLATE_OREO;
            mPendingOrder.toastLevel = 2;
            mPendingOrder.quantity = 1;
            return;
        }

        // <entity identifier="HGC_VM_MCH_T_Q" name="milk chocolate with a honey graham cracker and a vanilla marshmallow"/>
        if (itemId.equals("HGC_VM_MCH_T_Q")) {
            mPendingOrder.cracker = SmoresOrder.CRACKER_HONEY_GRAHAM;
            mPendingOrder.marshmallow = SmoresOrder.MARSHMALLOW_VANILLA;
            mPendingOrder.chocolate = SmoresOrder.CHOCOLATE_MILK;
            mPendingOrder.toastLevel = 2;
            mPendingOrder.quantity = 1;
            return;
        }

        // <entity identifier="HGC_M_MCH_LT_Q" name="milk chocolate with a honey graham cracker light toasted"/>
        if (itemId.equals("HGC_M_MCH_LT_Q")) {
            mPendingOrder.cracker = SmoresOrder.CRACKER_HONEY_GRAHAM;
            mPendingOrder.marshmallow = SmoresOrder.MARSHMALLOW_VANILLA;
            mPendingOrder.chocolate = SmoresOrder.CHOCOLATE_MILK;
            mPendingOrder.toastLevel = 1;
            mPendingOrder.quantity = 1;
            return;
        }

        // <entity identifier="CC_VM_MCH_T_Q" name="milk chocolate with a chocolate cracker and a vanilla marshmallow"/>
        // <entity identifier="CC_M_MCH_T_Q" name="milk chocolate with a chocolate cracker"/>
        if (itemId.equals("CC_VM_MCH_T_Q") || itemId.equals("CC_M_MCH_T_Q")) {
            mPendingOrder.cracker = SmoresOrder.CRACKER_CHOCOLATE;
            mPendingOrder.marshmallow = SmoresOrder.MARSHMALLOW_VANILLA;
            mPendingOrder.chocolate = SmoresOrder.CHOCOLATE_MILK;
            mPendingOrder.toastLevel = 2;
            mPendingOrder.quantity = 1;
            return;
        }

        // <entity identifier="usual" name="the usual smore"/>
        // <entity identifier="usual" name="my usual smore"/>
        if (itemId.equals("usual")) {

            CompletableFuture<SmoresOrder> latestFuture = Utils.getLatestOrderFuture(mDb, userId, "pending-orders");
            latestFuture.thenAccept(new Consumer<SmoresOrder>() {

                @Override
                public void accept(SmoresOrder latestOrder) {

                    if (latestOrder != null) {
                        Log.d(Utils.TAG, String.format("Order review from pending: %s", latestOrder));
                        mPendingOrder = latestOrder;
                        updateReviewFragmentUI(latestOrder);
                        return;
                    }

                    CompletableFuture<SmoresOrder> latestFuture = Utils.getLatestOrderFuture(mDb, userId, "completed-orders");
                    latestFuture.thenAccept(new Consumer<SmoresOrder>() {

                        @Override
                        public void accept(SmoresOrder latestOrder) {

                            if (latestOrder == null) {
                                // There is no previous order, let's create a fake one
                                latestOrder = new SmoresOrder();
                                latestOrder.cracker = SmoresOrder.CRACKER_CHOCOLATE;
                                latestOrder.marshmallow = SmoresOrder.MARSHMALLOW_VANILLA;
                                latestOrder.chocolate = SmoresOrder.CHOCOLATE_MILK;
                                latestOrder.toastLevel = 2;
                                latestOrder.quantity = 1;
                            }
                            mPendingOrder = latestOrder;
                            Log.d(Utils.TAG, String.format("Order review from completed: %s", latestOrder));
                            updateReviewFragmentUI(latestOrder);
                        }
                    });
                }
            });

            return;
        }

        if (!Constants.SMORE_TYPES.containsKey(itemId)) {
            mPendingOrder.cracker = SmoresOrder.CRACKER_HONEY_GRAHAM;
            mPendingOrder.marshmallow = SmoresOrder.MARSHMALLOW_VANILLA;
            mPendingOrder.chocolate = SmoresOrder.CHOCOLATE_MILK;
            mPendingOrder.toastLevel = 2;
            mPendingOrder.quantity = 1;
            return;
        }
    }

    private void updateReviewFragmentUI(SmoresOrder latestOrder) {
        Log.d(Utils.TAG, "Checking latestOrder: " + latestOrder);

        if (mPendingOrder == null) {
            mPendingOrder = new SmoresOrder();
            mPendingOrder.orderStatus = SmoresOrder.ORDER_STATUS_NOT_PLACED;
        }

        mPendingOrder.cracker = latestOrder.cracker;
        mPendingOrder.marshmallow = latestOrder.marshmallow;
        mPendingOrder.chocolate = latestOrder.chocolate;
        mPendingOrder.toastLevel = latestOrder.toastLevel;
        mPendingOrder.quantity = latestOrder.quantity;

        // update ui on review fragment
        if (MainActivity.reviewFragment != null) {
            MainActivity.reviewFragment.updateText(mPendingOrder);
            Log.d(Utils.TAG, "Updating ReviewFragment UI");
        }
    }

    @Override
    public void onDestroy() {
        mFirestoreListener.remove();
        super.onDestroy();
    }

    public SmoresOrder getPendingOrder() {
        return mPendingOrder;
    }

    private void loadPrevOrders() {

        if (mDb == null) {
            Log.e(Utils.TAG, "No Firestore database");
            return;
        }

        mDb.collection(String.format("users/%s/completed-orders", userId))
                .orderBy("lastUpdate", Query.Direction.DESCENDING).limit(10)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {

            @Override
            public void onSuccess(QuerySnapshot snapshots) {

                for (DocumentChange dc : snapshots.getDocumentChanges()) {

                    QueryDocumentSnapshot doc = dc.getDocument();
                    SmoresOrder order = doc.toObject(SmoresOrder.class);
                    order._firebaseId = doc.getId();
                    prevOrdersAdapter.addItem(order);
                    Log.d(Utils.TAG, String.format("Added %s to prev orders", order._firebaseId));
                }
            }
        });
    }

    private void createPendingOrderListener() {

        if (mDb == null) {
            Log.d(Utils.TAG, "No database");
            return;
        }

        mFirestoreListener = mDb.collection(String.format("users/%s/pending-orders", userId))
                .addSnapshotListener(new EventListener<QuerySnapshot>() {

                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {

                        if (e != null) {
                            Log.w(Utils.TAG, "Listen failed.", e);
                            return;
                        }

                        for (DocumentChange dc : snapshots.getDocumentChanges()) {

                            QueryDocumentSnapshot doc = dc.getDocument();
                            SmoresOrder order = doc.toObject(SmoresOrder.class);
                            order._firebaseId = doc.getId();

                            switch (dc.getType()) {
                                case ADDED:
                                    Log.d(Utils.TAG, "New Msg: " + order.orderStatus);
                                    prevOrdersAdapter.addItem(0, order);
                                    break;

                                case MODIFIED:
                                    Log.d(Utils.TAG, "Modified Msg: " + order.orderStatus);
                                    prevOrdersAdapter.checkUpdate(order);
                                    break;

//                        case REMOVED:
//                            Log.d(Utils.TAG, "Removed Msg: " + order.orderStatus);
//                            break;
                            }

                            // update slice
                            Intent intent = new Intent(MainActivity.this, SliceBroadcastReceiver.class);
                            intent.putExtra(SliceBroadcastReceiver.EXTRA_ORDER_STATUS, order.orderStatus);
                            sendBroadcast(intent);
                        }
                    }
                });

    }

    public void newOrder(View v) {
        mPendingOrder = new SmoresOrder();
        mViewPager.setCurrentItem(2, true);
    }

    public void prevOrders(View v) {
        mViewPager.setCurrentItem(0, true);
    }

    public void next(View v) {

        int i = mViewPager.getCurrentItem();
//        Log.d(Utils.TAG, String.format("view pager i = %d", i));

        // update pending order based on ingredient selection
        if ((i >= 2) && (i <= 6)) {
            updateOrder(i);
        }

        // update ui on review fragment
        if ((i == 6) && (MainActivity.reviewFragment != null)) {
            MainActivity.reviewFragment.updateText(mPendingOrder);
            Log.d(Utils.TAG, "Updating ReviewFragment UI");
        }

        mViewPager.setCurrentItem(i + 1, true);
    }

    @Override
    public void onBackPressed() {
        back(null);
    }

    public void back(View v) {

        int i = mViewPager.getCurrentItem();

        if (i == 0) { // go to home from previous orders
            mViewPager.setCurrentItem(1, true);
        } else if (i == 1) { // close if on the home screen
            finish();
        } else if (i == 8) { // if order is complete, go home
            mViewPager.setCurrentItem(1);
        } else { // just go to previous ingredient
            mViewPager.setCurrentItem(i - 1, true);
        }
    }

    private void updateOrder(int currentIndex) {

        Log.d(Utils.TAG, "MainActivity: updateOrder()");
        Log.d(Utils.TAG, String.format("currentIndex = %d", currentIndex));

        int currentSelection = MainActivity.this.currentIngredientSelection[currentIndex - 2];
        Log.d(Utils.TAG, String.format("currentSelection = %d", currentSelection));
        if (currentSelection == -1) {
            Log.e(Utils.TAG, "No currentSelection index");
            return;
        }

        switch (currentIndex) {

            // cracker selection
            case 2:

                mPendingOrder.orderStatus = SmoresOrder.ORDER_STATUS_NOT_PLACED;

                if (currentSelection == 0) {
                    mPendingOrder.cracker = SmoresOrder.CRACKER_HONEY_GRAHAM;
                } else if (currentSelection == 1) {
                    mPendingOrder.cracker = SmoresOrder.CRACKER_CHOCOLATE;
                } else if (currentSelection == 2) {
                    mPendingOrder.cracker = SmoresOrder.CRACKER_GLUTEN_FREE;
                } else {
                    mPendingOrder.cracker = SmoresOrder.CRACKER_UNKNOWN;
                }
                break;

            // marshmallow selection
            case 3:

                if (currentSelection == 0) {
                    mPendingOrder.marshmallow = SmoresOrder.MARSHMALLOW_VANILLA;
                } else if (currentSelection == 1) {
                    mPendingOrder.marshmallow = SmoresOrder.MARSHMALLOW_CHOCOLATE;
                } else {
                    mPendingOrder.marshmallow = SmoresOrder.MARSHMALLOW_UNKNOWN;
                }
                break;

            // chocolate selection
            case 4:

                if (currentSelection == 0) {
                    mPendingOrder.chocolate = SmoresOrder.CHOCOLATE_MILK;
                } else if (currentSelection == 1) {
                    mPendingOrder.chocolate = SmoresOrder.CHOCOLATE_DARK;
                } else if (currentSelection == 2) {
                    mPendingOrder.chocolate = SmoresOrder.CHOCOLATE_OREO;
                } else {
                    mPendingOrder.chocolate = SmoresOrder.CHOCOLATE_UNKNOWN;
                }
                break;

            // toast level selection
            case 5:

                if ((currentSelection >= 0) && (currentSelection <= 2)) {
                    mPendingOrder.toastLevel = currentSelection + 1;
                } else {
                    mPendingOrder.toastLevel = 0;
                }
                break;

            // quantity selection
            case 6:

                if ((currentSelection >= 0) && (currentSelection <= 2)) {
                    mPendingOrder.quantity = currentSelection + 1;
                } else {
                    mPendingOrder.quantity = 0;
                }
                break;
        }
    }

    public void confirm(View v) {

        mPendingOrder.setOrderStatus(SmoresOrder.ORDER_STATUS_NOT_PLACED);

        // save in firebase
        String collectionName = String.format("users/%s/pending-orders", userId);
        Log.d(Utils.TAG, String.format("Adding to collection %s", collectionName));
        mDb.collection(collectionName)
                .add(mPendingOrder.getMap())
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {

                    @Override
                    public void onSuccess(DocumentReference ref) {

                        Log.d(Utils.TAG, "DocumentSnapshot successfully written!");

                        // advance to next fragment
                        int i = mViewPager.getCurrentItem();
                        mViewPager.setCurrentItem(i + 1, true);
                    }

                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(Utils.TAG, "Error writing document", e);
                    }
                });
    }

    public void newWay(View v) {
        startMain();
        Toast.makeText(MainActivity.this, getResources().getString(R.string.order_the_usual), Toast.LENGTH_LONG).show();
        startActivity(new Intent(Intent.ACTION_VOICE_COMMAND).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    private void startMain() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }
}
