const functions = require("firebase-functions");
const admin = require("firebase-admin");
admin.initializeApp();
const db = admin.firestore();

exports.updateQueue = functions.region("asia-southeast1")
    .pubsub.schedule("*/5 * * * *")
    .onRun(async (context) => {
      const date = new Date();
      const today = date.toLocaleString("en-SG", {
        timeZone: "Singapore",
        hour12: false,
      });
      const currdate = today.substring(0, 6) + today.substring(8, 10);
      const queue = await db.collection("LiveQueue").get();
      queue.forEach(async (snapshot) => {
        if (String(snapshot.get("CurrentDay")) != currdate) {
          await db.collection("LiveQueue")
              .doc(snapshot.id).update({CurrentDay: currdate});
          const checkQuery = await db.collection("Queue")
              .doc(snapshot.id).collection(currdate.split("/").join("-")).get();
          if (checkQuery.empty) {
            await db.collection("LiveQueue")
                .doc(snapshot.id).update({Queueid: []});
            await db.collection("LiveQueue")
                .doc(snapshot.id).update({TimingList: []});
            await db.collection("LiveQueue")
                .doc(snapshot.id).update({FirstAppointment: ""});
          } else {
            const queueid = [];
            const timinglist = [];
            checkQuery.docs.forEach((doc) => {
              timinglist.push(doc.id);
              queueid.push(String(doc.get("uid")));
            });
            await db.collection("LiveQueue")
                .doc(snapshot.id).update({FirstAppointment: timinglist[0]});
            await db.collection("LiveQueue")
                .doc(snapshot.id).update({Queueid: queueid});
            await db.collection("LiveQueue")
                .doc(snapshot.id).update({TimingList: timinglist});
          }
        } else {
          const currqueue = snapshot.get("Queueid");
          const currtime = snapshot.get("TimingList");
          const firstAppointment = String(snapshot.get("FirstAppointment"));
          const currqueuearr = Array.from(currqueue);
          console.log(currqueuearr);
          const currtimearr = Array.from(currtime);
          console.log(currtimearr);
          if (String(currtimearr[0]) < today.substring(12, 17)) {
            currtimearr.shift();
            currqueuearr.shift();
            await db.collection("LiveQueue")
                .doc(snapshot.id).update({Queueid: currqueuearr});
            await db.collection("LiveQueue")
                .doc(snapshot.id).update({TimingList: currtimearr});
          }
          console.log(currqueuearr.length);
          console.log(String(currtimearr[0]));
          console.log(firstAppointment);
          if (currqueuearr
              .length >= 3 && String(currtimearr[0]) >= firstAppointment) {
            const thirdInQueue = await db.collection("Users")
                .doc(String(currqueuearr[2])).get();
            const androidtokenthird = thirdInQueue.get("Token");
            console.log("Token3");
            if (androidtokenthird != null) {
              console.log("Third");
              console.log(String(androidtokenthird));
              sendNotification(String(androidtokenthird), "3");
            }
          }
          if (currqueuearr
              .length >=1 && String(currtimearr[0]) >= firstAppointment) {
            const firstInQueue = await db.collection("Users")
                .doc(String(currqueuearr[0])).get();
            const androidtokenfirst = firstInQueue.get("Token");
            console.log("Token1");
            if (androidtokenfirst != null) {
              console.log("First");
              console.log(String(androidtokenfirst));
              sendNotification(String(androidtokenfirst), "1");
            }
          }
        }
      });
    });

/**
 * Send a message to the device
 * @param {String} androidToken - The fcm token
 * @param {String} msg - The number of patients away
 */
function sendNotification(androidToken, msg) {
  const title = "MCQueue";
  let body = "";
  if (msg == "1") {
    body = `You are ${msg} patient away from your turn`;
  }

  if (msg == "3") {
    body = `You are ${msg} patients away from your turn`;
  }

  const message = {
    notification: {title: title, body: body},
    token: androidToken,
    android: {
      notification: {
        channel_id: "MCQueue101",
      },
    },
  };

  admin.messaging().send(message).then((response) => {
    return console.log("Successful Message Sent");
  }).catch((error) => {
    console.log(error);
    return console.log("Error Sending Message");
  });
}
