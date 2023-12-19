var url = "ws://localhost:8080/ws";
var client = new StompJs.Client({
    brokerURL:url});

var initSubscription;
client.onConnect = (frame) => {
    console.log('Connected: ' + frame);
  
    

    initSubscription = client.subscribe('/user/queue/updates', (msg) => {
        console.log("Message received from : "+msg.body);
        appendMessage(msg.body)
        unsubscribe()
    });


    client.subscribe('/topic/updates', (msg) => {

        console.log("Message received from : "+msg.body);
        appendMessage(msg.body)
    });

    client.publish({
        destination: "/app/init/logs",
        skipContentLengthHeader: true,
      });

    



};

client.onWebSocketError = (error) => {
    console.error('Error with websocket', error);
};




client.onStompError = function (frame) {
  // Will be invoked in case of error encountered at Broker
  // Bad login/passcode typically will cause an error
  // Complaint brokers will set `message` header with a brief message. Body may contain details.
  // Compliant brokers will terminate the connection after any error
  console.log("Broker reported error: " + frame.headers["message"]);
  console.log("Additional details: " + frame.body);
};




function unsubscribe() { 
    console.log("Init message achieved closing this  init subscription");
    initSubscription.unsubscribe();
 }
function appendMessage(message){
    var json = JSON.parse(message)
    var formattedString = json.content.replace(/\n/g, '<br>');
    console.log(formattedString)
    $('#messageBox').append('<p>' + formattedString + '</p>');
}

$(function(){
    client.activate()

})