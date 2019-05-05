
const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();

const OrderStatus = {
  UNKNOWN: 'UNKNOWN',
  NOT_PLACED: 'NOT_PLACED',
  CONFIRMED: 'CONFIRMED',
  TOASTING: 'TOASTING',
  EN_ROUTE: 'EN_ROUTE',
  DELIVERED: 'DELIVERED'
};

const wait = async (ms) => {
  return new Promise((resolve, reject) => { setTimeout(resolve, ms); });
};

const getTimestamp = () => {
  return Math.floor(new Date().getTime() / 1000); // secs
};

exports.orderAdded = functions.firestore
    .document('users/{userId}/pending-orders/{orderId}')
    .onCreate((snap, context) => {

      // get an object representing the document
      const newData = snap.data();
      console.log(`order added: newData = ${ JSON.stringify(newData) }`);

      // basic validity check
      if (newData.orderStatus !== OrderStatus.NOT_PLACED) {
        console.error(`unexpected newData.orderStatus = ${ newData.orderStatus }`);
        return 0;
      }

      // updated status to received
      return snap.ref.set({
        orderStatus: OrderStatus.CONFIRMED,
        lastUpdate: getTimestamp()
      }, { merge: true });
    });

exports.orderUpdated = functions.firestore
    .document('users/{userId}/pending-orders/{orderId}')
    .onUpdate((change, context) => {

      const newData = change.after.data();
      // const previousData = change.before.data();

      console.log(`order updated: newData = ${ JSON.stringify(newData) }`);

      // if this is the last status update
      if (newData.orderStatus === OrderStatus.DELIVERED) {

        // add completed order doc
        const completedDoc = admin.firestore().doc(`users/${ context.params.userId }/completed-orders/${ context.params.orderId }`);
        return completedDoc.set(newData)
            .then(() => { return change.after.ref.delete() })
            .then(() => {
              console.log(`chain is done, orderId = ${ context.params.orderId } moved to completed-orders`);
              return 0;
            })
            .catch(e => {
              console.error(e);
              return 0;
            });
      }

      // wait then advance to next order status
      return wait(5000).then(() => {

        let newStatus = OrderStatus.UNKNOWN;
        if (newData.orderStatus === OrderStatus.CONFIRMED)
          newStatus = OrderStatus.TOASTING;
        else if (newData.orderStatus === OrderStatus.TOASTING)
          newStatus = OrderStatus.EN_ROUTE;
        else if (newData.orderStatus === OrderStatus.EN_ROUTE)
          newStatus = OrderStatus.DELIVERED;
        else {
          console.error(`unknown orderStatus = ${ newData.orderStatus }`)
          return 0;
        }

        return change.after.ref.set({
          orderStatus: newStatus,
          lastUpdate: getTimestamp()
        }, { merge: true });
      });
    });
