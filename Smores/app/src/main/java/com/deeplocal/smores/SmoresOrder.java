package com.deeplocal.smores;

import java.util.HashMap;
import java.util.Map;

public class SmoresOrder {

    public static final String CRACKER_UNKNOWN = "UNKNOWN";
    public static final String CRACKER_HONEY_GRAHAM = "HONEY_GRAHAM";
    public static final String CRACKER_CHOCOLATE = "CHOCOLATE";
    public static final String CRACKER_GLUTEN_FREE = "GLUTEN_FREE";

    public static final String MARSHMALLOW_UNKNOWN = "UNKNOWN";
    public static final String MARSHMALLOW_VANILLA = "VANILLA";
    public static final String MARSHMALLOW_CHOCOLATE = "CHOCOLATE";

    public static final String CHOCOLATE_UNKNOWN = "UNKNOWN";
    public static final String CHOCOLATE_MILK = "MILK";
    public static final String CHOCOLATE_DARK = "DARK";
    public static final String CHOCOLATE_OREO = "OREO";

    public static final String ORDER_STATUS_UNKNOWN = "UNKNOWN";
    public static final String ORDER_STATUS_NOT_PLACED = "NOT_PLACED";
    public static final String ORDER_STATUS_CONFIRMED = "CONFIRMED";
    public static final String ORDER_STATUS_TOASTING = "TOASTING";
    public static final String ORDER_STATUS_EN_ROUTE = "EN_ROUTE";
    public static final String ORDER_STATUS_DELIVERED = "DELIVERED";

    public String _firebaseId;
    public String cracker = CRACKER_UNKNOWN;
    public String marshmallow = MARSHMALLOW_UNKNOWN;
    public String chocolate = CHOCOLATE_UNKNOWN;
    public int toastLevel = 0; // 1-3
    public int quantity = 0;
    public String orderStatus = ORDER_STATUS_UNKNOWN;
    public long lastUpdate = 0;

    public SmoresOrder() {}

    public SmoresOrder(String cracker, String marshmallow, String chocolate, int toastLevel, int quantity, String orderStatus, long lastUpdate) {
        this.cracker = cracker;
        this.marshmallow = marshmallow;
        this.chocolate = chocolate;
        this.toastLevel = toastLevel;
        this.quantity = quantity;
        this.orderStatus = orderStatus;
        this.lastUpdate = lastUpdate;
    }

    public void updateTs() {
        lastUpdate = System.currentTimeMillis() / 1000;
    }

    public String getCrackerString() {

        if (this.cracker.equals(CRACKER_HONEY_GRAHAM)) {
            return "Honey Graham Cracker";
        }

        if (this.cracker.equals(CRACKER_CHOCOLATE)) {
            return "Chocolate Graham Cracker";
        }

        if (this.cracker.equals(CRACKER_GLUTEN_FREE)) {
            return "Gluten Free Graham Cracker";
        }

        return "Unknown Cracker";
    }

    public String getMarshmallowString() {

        if (this.marshmallow.equals(MARSHMALLOW_VANILLA)) {
            return "Vanilla Marshmallow";
        }

        if (this.marshmallow.equals(MARSHMALLOW_CHOCOLATE)) {
            return "Chocolate Marshmallow";
        }

        return "Unknown Marshmallow";
    }

    public String getChocolateString() {

        if (this.chocolate.equals(CHOCOLATE_MILK)) {
            return "Milk Chocolate";
        }

        if (this.chocolate.equals(CHOCOLATE_DARK)) {
            return "Dark Chocolate";
        }

        if (this.chocolate.equals(CHOCOLATE_OREO)) {
            return "Oreo Chocolate";
        }

        return "Unknown Chocolate";
    }

    public Map<String, Object> getMap() {

        HashMap<String, Object> map = new HashMap<>();
        map.put("cracker", cracker);
        map.put("marshmallow", marshmallow);
        map.put("chocolate", chocolate);
        map.put("toastLevel", toastLevel);
        map.put("quantity", quantity);
        map.put("orderStatus", orderStatus);
        map.put("lastUpdate", lastUpdate);

        return map;
    }

    public void update(SmoresOrder o) {
        this.cracker = o.cracker;
        this.marshmallow = o.marshmallow;
        this.chocolate = o.chocolate;
        this.toastLevel = o.toastLevel;
        this.quantity = o.quantity;
        this.orderStatus = o.orderStatus;
        this.lastUpdate = o.lastUpdate;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public boolean equals(SmoresOrder o) {

        if (this._firebaseId == null || o._firebaseId == null) {
            return false;
        }

        return (this._firebaseId.equals(o._firebaseId));
    }

    public String toString() {
        return String.format("%s, %s, %s, Toast Level %d, Quantity %d (%s) Status %s",
                this.getCrackerString(),
                this.getMarshmallowString(),
                this.getChocolateString(),
                this.toastLevel,
                this.quantity,
                this._firebaseId,
                this.orderStatus);
    }
}
